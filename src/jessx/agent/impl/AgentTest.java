// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.agent.impl;

import jessx.net.DividendInfo;
import org.jdom.Document;
import jessx.net.NetworkWritable;
import jessx.business.operations.LimitOrder;
import jessx.utils.Utils;
import java.util.Date;
import java.util.Iterator;
import jessx.client.ClientCore;
import java.util.Hashtable;
import java.util.Vector;
import jessx.business.Institution;
import jessx.client.event.NetworkListener;
import jessx.agent.Agent;

public class AgentTest extends Agent implements NetworkListener
{
    private int numberOfOrderPerMinute;
    private Institution institution;
    private Vector institutions;
    private int numberInstitutions;
    Hashtable assetProperties;
    
    public AgentTest(final int numberOfOrderPerMinute, final int minNumOfDealPerMinute) {
        super(60000 / numberOfOrderPerMinute);
        this.institutions = new Vector();
        this.numberInstitutions = 0;
        this.assetProperties = new Hashtable();
        ClientCore.addNetworkListener(this, "DividendInfo");
        this.numberOfOrderPerMinute = numberOfOrderPerMinute;
    }
    
    @Override
    protected void experimentStarting() {
        super.experimentStarting();
        final Iterator iterInstit = ClientCore.getInstitutions().keySet().iterator();
        while (iterInstit.hasNext()) {
            this.institutions.add(ClientCore.getInstitution((String) iterInstit.next()));
            ++this.numberInstitutions;
        }
    }
    
    @Override
    protected void runExperimentOnPeriodOn() {
        final Date begin = new Date();
        this.institution = (Institution) this.institutions.elementAt(Math.round((float)Math.random() * this.numberInstitutions - 0.5f));
        final Hashtable properties = (Hashtable) this.assetProperties.get(this.institution.getAssetName());
        if (properties != null && properties.get("buyOrSellAllowed") != "false") {
            final int side = (int)Math.round(Math.random());
            float price = this.getValue(this.institution.getAssetName());
            if (price < 0.0f) {
                Utils.logger.error("Price is negative");
            }
            else {
                price = (float)Math.floor(100.0f * price) / 100.0f;
                final int qtty = (int)(Math.random() * 50.0) + 1;
                final LimitOrder lo = new LimitOrder();
                lo.setEmitter(ClientCore.getLogin());
                lo.setInstitutionName(this.institution.getName());
                lo.setPrice(price);
                lo.setQuantity(qtty);
                lo.setSide(side);
                ClientCore.send(lo);
            }
            final Date end = new Date();
            final long duree = end.getTime() - begin.getTime();
            super.pauseDuration = 60000 / this.numberOfOrderPerMinute - duree;
            if (duree > 60000 / this.numberOfOrderPerMinute) {
                Utils.logger.warn("Client trop lent - Temps de passage d'ordre: " + duree + " ms");
            }
            else {
                Utils.logger.debug("Temps de passage d'ordre: " + duree);
            }
        }
    }
    
    @Override
    protected void periodEnding() {
    }
    
    @Override
    protected void runExperimentOff() {
    }
    
    @Override
    protected void periodStarting() {
    }
    
    @Override
    protected void runExperimentOnPeriodOff() {
    }
    
    @Override
    public void objectReceived(final Document xmlDoc) {
        super.objectReceived(xmlDoc);
        if (xmlDoc.getRootElement().getName().equals("DividendInfo")) {
            final DividendInfo divInfo = new DividendInfo();
            divInfo.initFromNetworkInput(xmlDoc.getRootElement());
            final float experimentHoldingValue = divInfo.getExperimentHoldingValue();
            final float windowHoldingValue = divInfo.getWindowHoldingValue();
            final float variance = 1.0f;
            if (experimentHoldingValue > 0.0f) {
                this.setAssetProperties(true, divInfo.getAssetName(), experimentHoldingValue, variance, this.getMinValue(experimentHoldingValue, variance), this.getMaxValue(experimentHoldingValue, variance), this.getNewProbabilityArray(experimentHoldingValue, variance));
            }
            else if (windowHoldingValue > 0.0f) {
                this.setAssetProperties(true, divInfo.getAssetName(), windowHoldingValue, variance, this.getMinValue(windowHoldingValue, variance), this.getMaxValue(windowHoldingValue, variance), this.getNewProbabilityArray(windowHoldingValue, variance));
            }
            else {
                this.setAssetProperties(false, divInfo.getAssetName(), 0.0f, 0.0f, 0.0, 0.0, null);
            }
        }
    }
    
    private float getValue(final String assetName) {
        final Hashtable properties = (Hashtable) this.assetProperties.get(assetName);
        final double[] probability = (double[]) properties.get("probability");
        final double minValue = Double.parseDouble((String) properties.get("minValue"));
        final double maxValue = Double.parseDouble((String) properties.get("maxValue"));
        float prob;
        int i;
        for (prob = (float)Math.random(), prob = prob * (float)probability[0] + (1.0f - prob) * (float)probability[probability.length - 1], i = 0; i < probability.length && prob > probability[i]; ++i) {}
        --i;
        return (float)Math.floor(100.0 * (minValue + i * (maxValue - minValue) / probability.length)) / 100.0f;
    }
    
    private void setAssetProperties(final boolean infoKnown, final String assetName, final float mean, final float variance, final double minValue, final double maxValue, final double[] probability) {
        if (infoKnown) {
            final Hashtable properties = new Hashtable();
            this.assetProperties.put(assetName, properties);
            properties.put("buyOrSellAllowed", new StringBuilder().append(infoKnown).toString());
            properties.put("mean", new StringBuilder().append(mean).toString());
            properties.put("variance", new StringBuilder().append(variance).toString());
            properties.put("minValue", new StringBuilder().append(minValue).toString());
            properties.put("maxValue", new StringBuilder().append(maxValue).toString());
            properties.put("probability", probability);
        }
    }
    
    private double getMinValue(final float mean, final float variance) {
        return Math.max(mean - variance * Math.sqrt(Math.log(100.0)), 0.0);
    }
    
    private double getMaxValue(final float mean, final float variance) {
        return 2.0f * mean - this.getMinValue(mean, variance);
    }
    
    private double[] getNewProbabilityArray(final float variance, final float mean) {
        final double[] probability = new double[25];
        double xmin = mean - variance * Math.sqrt(Math.log(100.0));
        xmin = Math.max(xmin, 0.0);
        final double xmax = 2.0f * mean - xmin;
        final double delta = (xmax - xmin) / (probability.length - 1);
        probability[0] = delta / (Math.sqrt(6.283185307179586) * variance) * Math.exp(-1.0 * Math.pow((xmin - mean) / (Math.sqrt(2.0) * variance), 2.0));
        for (int i = 1; i < probability.length; ++i) {
            probability[i] = probability[i - 1] + delta / (Math.sqrt(6.283185307179586) * variance) * Math.exp(-1.0 * Math.pow((xmin + delta * i - mean) / (Math.sqrt(2.0) * variance), 2.0));
        }
        final double pLeft = (1.0 - (probability[probability.length - 1] + probability[0])) / 2.0;
        for (int j = 0; j < probability.length; ++j) {
            final double[] array = probability;
            final int n = j;
            array[n] += pLeft;
        }
        return probability;
    }
}
