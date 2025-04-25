// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Element;
import jessx.utils.Utils;

public class NormalDividend extends Dividend
{
    private float mean;
    private float variance;
    private double[] probability;
    private double minDividend;
    private double maxDividend;
    
    public float getMean() {
        return this.mean;
    }
    
    public float getVariance() {
        return this.variance;
    }
    
    @Override
    public float getDividend() {
        this.setProbabilityArray();
        float prob;
        int i;
        for (prob = (float)Math.random(), i = 0; i < this.probability.length && prob > this.probability[i]; ++i) {}
        --i;
        return (float)Math.floor(100.0 * (this.minDividend + i * (this.maxDividend - this.minDividend) / this.probability.length)) / 100.0f;
    }
    
    public void setProbabilityArray() {
        this.probability = new double[25];
        double xmin = this.mean - this.variance * Math.sqrt(Math.log(100.0));
        xmin = Math.max(xmin, 0.0);
        final double xmax = 2.0f * this.mean - xmin;
        final double delta = (xmax - xmin) / (this.probability.length - 1);
        this.probability[0] = delta / (Math.sqrt(6.283185307179586) * this.variance) * Math.exp(-1.0 * Math.pow((xmin - this.mean) / (Math.sqrt(2.0) * this.variance), 2.0));
        for (int i = 1; i < this.probability.length; ++i) {
            this.probability[i] = this.probability[i - 1] + delta / (Math.sqrt(6.283185307179586) * this.variance) * Math.exp(-1.0 * Math.pow((xmin + delta * i - this.mean) / (Math.sqrt(2.0) * this.variance), 2.0));
        }
        final double pLeft = (1.0 - (this.probability[this.probability.length - 1] + this.probability[0])) / 2.0;
        for (int j = 0; j < this.probability.length; ++j) {
            final double[] probability = this.probability;
            final int n = j;
            probability[n] += pLeft;
        }
        this.probability[this.probability.length - 1] = 1.0;
        this.minDividend = xmin;
        this.maxDividend = xmax;
    }
    
    @Override
    public void setParameter(final int i, final Object value) {
        Utils.logger.debug("Setting parameter " + i + ". Value class: " + value.getClass().toString() + ", Value=" + value.toString());
        if (i == 0) {
            this.mean = (float)value;
        }
        else {
            this.variance = (float)value;
        }
    }
    
    @Override
    public Object getParameter(final int i) {
        return (i == 0) ? new Float(this.mean) : new Float(this.variance);
    }
    
    @Override
    public int getParamCount() {
        return 2;
    }
    
    @Override
    public Class getParamClass(final int i) {
        return Float.class;
    }
    
    @Override
    public String[] getParamNames() {
        return new String[] { "Mean", "Variance" };
    }
    
    @Override
    public float getNormalValue() {
        return this.mean;
    }
    
    @Override
    public String getDetails() {
        String details = "";
        details = "Mean dividend: " + this.mean + " ; standard deviation: " + this.variance + ".";
        return details;
    }
    
    public NormalDividend() {
        Utils.logger.debug("Creating normal dividend ... ");
        this.mean = 0.0f;
        this.variance = 0.0f;
    }
    
    @Override
    public void loadFromXml(final Element node) {
        Utils.logger.info("Loading dividends...");
        final String mean = node.getAttributeValue("mean");
        final String variance = node.getAttributeValue("variance");
        if (mean == null || variance == null) {
            Utils.logger.error("invalid xml dividend node: attributes mean and/or variance not found.");
            return;
        }
        Utils.logger.debug("Value found: mean=" + mean + ", variance=" + variance);
        this.setParameter(0, new Float(mean));
        this.setParameter(1, new Float(variance));
    }
    
    @Override
    public void saveToXml(final Element node) {
        Utils.logger.debug("Saving dividends...");
        node.setAttribute("mean", Float.toString(this.getMean())).setAttribute("variance", Float.toString(this.getVariance()));
    }
    
    @Override
    public Object clone() {
        final NormalDividend div = new NormalDividend();
        div.mean = this.mean;
        div.variance = this.variance;
        return div;
    }
}
