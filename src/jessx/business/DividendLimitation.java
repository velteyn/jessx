// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

import jessx.business.event.DividendInfoEvent;
import org.jdom.Content;
import jessx.utils.Utils;
import jessx.business.event.DividendInfoListener;
import org.jdom.Element;
import java.util.Vector;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public class DividendLimitation implements XmlExportable, XmlLoadable
{
    private int dividendDetailled;
    private int windowSize;
    private boolean displayWindowSize;
    private boolean displaySessionLength;
    private boolean displayHoldingValueForExperiment;
    private boolean displayHoldingValueForWindow;
    private boolean displayOperationsCosts;
    private DividendModel divModel;
    private String assetName;
    private String playerType;
    private Vector dividendInfoListener;
    
    public void setDisplayWindowSize(final boolean displayWindowSize) {
        if (this.displayWindowSize != displayWindowSize) {
            this.displayWindowSize = displayWindowSize;
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setDisplaySessionLength(final boolean displaySessionLength) {
        if (this.displaySessionLength != displaySessionLength) {
            this.displaySessionLength = displaySessionLength;
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setDisplayHoldingValueForExperiment(final boolean displayHoldingValueForExperiment) {
        if (this.displayHoldingValueForExperiment != displayHoldingValueForExperiment) {
            this.displayHoldingValueForExperiment = displayHoldingValueForExperiment;
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setWindowSize(final int windowSize) {
        if (this.windowSize != windowSize) {
            this.windowSize = windowSize;
            this.divModel.setWindowSize(this.playerType, windowSize);
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setDisplayHoldingValueForWindow(final boolean displayHoldingValueForWindow) {
        if (this.displayHoldingValueForWindow != displayHoldingValueForWindow) {
            this.displayHoldingValueForWindow = displayHoldingValueForWindow;
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setDisplayOperationsCosts(final boolean displayOperationsCosts) {
        if (this.displayOperationsCosts != displayOperationsCosts) {
            this.displayOperationsCosts = displayOperationsCosts;
            this.fireDividendInfoUpdated();
        }
    }
    
    public void setDividendDetailledproperties(final int numberOfTheButton) {
        if (this.dividendDetailled != numberOfTheButton) {
            this.dividendDetailled = numberOfTheButton;
            this.fireDividendInfoUpdated();
        }
    }
    
    public int getDividendDetailledproperties() {
        return this.dividendDetailled;
    }
    
    public boolean isDisplayingWindowSize() {
        return this.displayWindowSize;
    }
    
    public boolean isDisplayingSessionLength() {
        return this.displaySessionLength;
    }
    
    public boolean isDisplayingHoldingValueForExperiment() {
        return this.displayHoldingValueForExperiment;
    }
    
    public int getWindowSize() {
        return this.windowSize;
    }
    
    public boolean isDisplayingOperationsCosts() {
        return this.displayOperationsCosts;
    }
    
    public String getAssetName() {
        return this.assetName;
    }
    
    public String getPlayerType() {
        return this.playerType;
    }
    
    public boolean isDisplayHoldingValueForWindow() {
        return this.displayHoldingValueForWindow;
    }
    
    public DividendLimitation(final String playerType, final String asset) {
        this.dividendDetailled = 3;
        this.windowSize = 1;
        this.displayWindowSize = false;
        this.displaySessionLength = false;
        this.displayHoldingValueForExperiment = false;
        this.displayHoldingValueForWindow = false;
        this.displayOperationsCosts = false;
        this.dividendInfoListener = new Vector();
        this.playerType = playerType;
        this.assetName = asset;
        this.divModel = BusinessCore.getAsset(asset).getDividendModel();
    }
    
    public DividendLimitation(final String playerType, final Element node) {
        this.dividendDetailled = 3;
        this.windowSize = 1;
        this.displayWindowSize = false;
        this.displaySessionLength = false;
        this.displayHoldingValueForExperiment = false;
        this.displayHoldingValueForWindow = false;
        this.displayOperationsCosts = false;
        this.dividendInfoListener = new Vector();
        this.playerType = playerType;
        this.loadFromXml(node);
    }
    
    public void addListener(final DividendInfoListener listener) {
        this.dividendInfoListener.add(listener);
    }
    
    public void removeListener(final DividendInfoListener listener) {
        this.dividendInfoListener.remove(listener);
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading dividend info...");
        final String assetName = node.getAttributeValue("asset");
        if (assetName == null) {
            Utils.logger.error("Invalid xml DividendInfo node : attribute asset not found.");
            return;
        }
        this.assetName = assetName;
        this.divModel = BusinessCore.getAsset(assetName).getDividendModel();
        final String dividendDetailledProperties = node.getChild("DividendDetailledProperties").getAttributeValue("value");
        final String windowSize = node.getChild("WindowSize").getAttributeValue("value");
        final String displayWindowSize = node.getChild("DisplayWindowSize").getAttributeValue("value");
        final String displaySessionLength = node.getChild("DisplaySessionLength").getAttributeValue("value");
        final String displayHoldingValueForExperiment = node.getChild("DisplayHoldingValueForExperiment").getAttributeValue("value");
        final String displayHoldingValueForWindow = node.getChild("DisplayHoldingValueForWindow").getAttributeValue("value");
        final String displayOperationsCosts = node.getChild("DisplayOperationsCosts").getAttributeValue("value");
        if (dividendDetailledProperties == null || windowSize == null || displayWindowSize == null || displaySessionLength == null || displayHoldingValueForExperiment == null || displayHoldingValueForWindow == null || displayOperationsCosts == null) {
            Utils.logger.error("Invalid xml dividend info node. One of the child node is missing or the attribute value of one or more child could not be found. ");
            return;
        }
        Utils.logger.debug("Reading dividendDetailledProperties... " + dividendDetailledProperties);
        int dividendProperties;
        if ("Dividends shown for the next period".equals(dividendDetailledProperties)) {
            dividendProperties = 1;
        }
        else if ("Dividends shown for the window".equals(dividendDetailledProperties)) {
            dividendProperties = 2;
        }
        else if ("Dividends shown for the experiment".equals(dividendDetailledProperties)) {
            dividendProperties = 0;
        }
        else {
            dividendProperties = 3;
        }
        this.setDividendDetailledproperties(dividendProperties);
        Utils.logger.debug("Reading windowSize... " + windowSize);
        this.setWindowSize(Integer.parseInt(windowSize));
        Utils.logger.debug("Reading displayWindowSize... " + displayWindowSize);
        this.setDisplayWindowSize(Boolean.valueOf(displayWindowSize));
        Utils.logger.debug("Reading displaySessionLength... " + displaySessionLength);
        this.setDisplaySessionLength(Boolean.valueOf(displaySessionLength));
        Utils.logger.debug("Reading displayHoldingValueForExperiment... " + displayHoldingValueForExperiment);
        this.setDisplayHoldingValueForExperiment(Boolean.valueOf(displayHoldingValueForExperiment));
        Utils.logger.debug("Reading displayHoldingValueForWindow... " + displayHoldingValueForWindow);
        this.setDisplayHoldingValueForWindow(Boolean.valueOf(displayHoldingValueForWindow));
        Utils.logger.debug("Reading displayOperationsCosts... " + displayOperationsCosts);
        this.setDisplayOperationsCosts(Boolean.valueOf(displayOperationsCosts));
    }
    
    public void saveToXml(final Element node) {
        node.setAttribute("asset", this.getAssetName());
        String dividendProperties;
        if (this.getDividendDetailledproperties() == 0) {
            dividendProperties = "Dividends shown for the experiment";
        }
        else if (this.getDividendDetailledproperties() == 1) {
            dividendProperties = "Dividends shown for the next period";
        }
        else if (this.getDividendDetailledproperties() == 2) {
            dividendProperties = "Dividends shown for the window";
        }
        else {
            dividendProperties = "Dividends not shown";
        }
        node.addContent(new Element("DividendDetailledProperties").setAttribute("value", dividendProperties));
        node.addContent(new Element("WindowSize").setAttribute("value", Integer.toString(this.getWindowSize())));
        node.addContent(new Element("DisplayWindowSize").setAttribute("value", Boolean.toString(this.isDisplayingWindowSize())));
        node.addContent(new Element("DisplaySessionLength").setAttribute("value", Boolean.toString(this.isDisplayingSessionLength())));
        node.addContent(new Element("DisplayHoldingValueForExperiment").setAttribute("value", Boolean.toString(this.isDisplayingHoldingValueForExperiment())));
        node.addContent(new Element("DisplayHoldingValueForWindow").setAttribute("value", Boolean.toString(this.isDisplayHoldingValueForWindow())));
        node.addContent(new Element("DisplayOperationsCosts").setAttribute("value", Boolean.toString(this.isDisplayingOperationsCosts())));
    }
    
    private void fireDividendInfoUpdated() {
        for (int i = 0; i < this.dividendInfoListener.size(); ++i) {
            ((DividendInfoListener) this.dividendInfoListener.elementAt(i)).dividendInfoModified(new DividendInfoEvent(this.assetName, this.playerType, DividendInfoEvent.DIVIDEND_INFO_UPDATED));
        }
    }
}
