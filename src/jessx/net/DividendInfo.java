// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.net;

import jessx.business.DividendLimitation;
import org.jdom.Content;
import jessx.server.net.NetworkCore;
import jessx.business.BusinessCore;
import java.util.Iterator;
import org.jdom.Element;
import java.util.Vector;

public class DividendInfo implements NetworkWritable, NetworkReadable
{
    private float experimentHoldingValue;
    private float windowHoldingValue;
    private int windowPeriodCount;
    private int expPeriodCount;
    private float interestRate;
    private Vector periodInfo;
    private String assetName;
    private boolean showOperationsCosts;
    
    public void setAssetName(final String assetName) {
        this.assetName = assetName;
    }
    
    public float getExperimentHoldingValue() {
        return this.experimentHoldingValue;
    }
    
    public float getWindowHoldingValue() {
        return this.windowHoldingValue;
    }
    
    public int getWindowPeriodCount() {
        return this.windowPeriodCount;
    }
    
    public int getExpPeriodCount() {
        return this.expPeriodCount;
    }
    
    public float getInterestRate() {
        return this.interestRate;
    }
    
    public Vector getPeriodInfo() {
        return this.periodInfo;
    }
    
    public boolean getIsShowingOperationsCosts() {
        return this.showOperationsCosts;
    }
    
    public String getAssetName() {
        return this.assetName;
    }
    
    public DividendInfo() {
        this.experimentHoldingValue = -1.0f;
        this.windowHoldingValue = -1.0f;
        this.windowPeriodCount = -1;
        this.expPeriodCount = -1;
        this.interestRate = -1.0f;
        this.periodInfo = new Vector();
        this.showOperationsCosts = true;
    }
    
    public String produceInfoReport() {
        String message = "";
        message = String.valueOf(message) + ":: " + this.assetName + " ::\n";
        if (this.expPeriodCount != -1) {
            message = String.valueOf(message) + "The session has " + this.getExpPeriodCount() + " periods.\n";
        }
        if (this.experimentHoldingValue != -1.0f) {
            message = String.valueOf(message) + "The holding value for the session is " + this.experimentHoldingValue + ".\n";
        }
        if (this.windowPeriodCount != -1) {
            message = String.valueOf(message) + "Following Info are given for the next " + this.windowPeriodCount + " periods\n";
        }
        if (this.windowHoldingValue != -1.0f) {
            message = String.valueOf(message) + "The holding value is " + this.windowHoldingValue + "\n";
        }
        for (int i = 0; i < this.periodInfo.size(); ++i) {
            message = String.valueOf(message) + this.periodInfo.elementAt(i);
        }
        return message;
    }
    
    public boolean initFromNetworkInput(final Element rootNode) {
        if (!rootNode.getName().equals("DividendInfo")) {
            return false;
        }
        final String assetName = rootNode.getAttributeValue("asset");
        if (assetName == null) {
            return false;
        }
        this.assetName = assetName;
        final String sessionLength = rootNode.getAttributeValue("sessionLength");
        final String expHV = rootNode.getAttributeValue("expHV");
        final String windowLength = rootNode.getAttributeValue("windowLength");
        final String windowHV = rootNode.getAttributeValue("windowHV");
        final String interestRate = rootNode.getAttributeValue("interestRate");
        final String showOperationsCosts = rootNode.getAttributeValue("showOperationsCosts");
        if (sessionLength != null) {
            this.expPeriodCount = Integer.parseInt(sessionLength);
        }
        if (expHV != null) {
            this.experimentHoldingValue = Float.parseFloat(expHV);
        }
        if (windowLength != null) {
            this.windowPeriodCount = Integer.parseInt(windowLength);
        }
        if (windowHV != null) {
            this.windowHoldingValue = Float.parseFloat(windowHV);
        }
        if (interestRate != null) {
            this.interestRate = Float.parseFloat(interestRate);
        }
        if (showOperationsCosts != null) {
            if (showOperationsCosts.equals("true")) {
                this.showOperationsCosts = true;
            }
            else {
                this.showOperationsCosts = false;
            }
        }
        final Iterator details = rootNode.getChildren("Details").iterator();
        this.periodInfo.removeAllElements();
        while (details.hasNext()) {
            final Element node = (Element) details.next();
            final String period = node.getAttributeValue("period");
            if (period != null) {
                this.periodInfo.add("For the period " + Integer.toString(Integer.parseInt(period) + 1) + ": " + node.getText() + "\n");
            }
        }
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        if (this.assetName == null) {
            return null;
        }
        final Element divInfo = new Element("DividendInfo");
        divInfo.setAttribute("asset", this.assetName);
        final DividendLimitation divLimit = BusinessCore.getScenario().getPlayerType(pt).getDividendInfo(this.assetName);
        if (divLimit.isDisplayingSessionLength()) {
            divInfo.setAttribute("sessionLength", Integer.toString(BusinessCore.getGeneralParameters().getPeriodCount()));
        }
        final int period = NetworkCore.getExperimentManager().getPeriodNum() + 1;
        if (divLimit.isDisplayingHoldingValueForExperiment()) {
            divInfo.setAttribute("expHV", Float.toString(BusinessCore.getAsset(this.assetName).getDividendModel().getExperimentHoldingValue(period)));
        }
        if (divLimit.isDisplayingWindowSize()) {
            divInfo.setAttribute("windowLength", Integer.toString(divLimit.getWindowSize()));
        }
        final int windowSize = divLimit.getWindowSize();
        if (divLimit.isDisplayHoldingValueForWindow()) {
            divInfo.setAttribute("windowHV", Float.toString(BusinessCore.getAsset(this.assetName).getDividendModel().getWindowHoldingValue(period, windowSize)));
        }
        if (divLimit.isDisplayingOperationsCosts()) {
            divInfo.setAttribute("showOperationsCosts", "true");
        }
        else {
            divInfo.setAttribute("showOperationsCosts", "false");
        }
        divInfo.setAttribute("interestRate", Float.toString(BusinessCore.getGeneralParameters().getInterestRate(NetworkCore.getExperimentManager().getPeriodNum() + 1)));
        if (3 != divLimit.getDividendDetailledproperties()) {
            if (1 == divLimit.getDividendDetailledproperties()) {
                final Element details = new Element("Details");
                details.setAttribute("period", Integer.toString(period));
                details.setText(BusinessCore.getAsset(this.assetName).getDividendModel().getDividendAt(period).getDetails());
                divInfo.addContent(details);
            }
            else if (2 == divLimit.getDividendDetailledproperties()) {
                for (int i = period; i < period + windowSize; ++i) {
                    final Element details2 = new Element("Details");
                    details2.setAttribute("period", Integer.toString(i));
                    details2.setText(BusinessCore.getAsset(this.assetName).getDividendModel().getDividendAt(i).getDetails());
                    divInfo.addContent(details2);
                }
            }
            else {
                for (int i = period; i < period + windowSize; ++i) {
                    final Element details2 = new Element("Details");
                    details2.setAttribute("period", Integer.toString(i));
                    details2.setText(BusinessCore.getAsset(this.assetName).getDividendModel().getDividendAt(i).getDetails());
                    divInfo.addContent(details2);
                }
            }
        }
        return divInfo;
    }
}
