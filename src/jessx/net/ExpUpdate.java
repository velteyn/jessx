// 
// Decompiled by Procyon v0.6.0
// 

package jessx.net;

import jessx.utils.Utils;
import org.jdom.Element;
import java.io.Serializable;

public class ExpUpdate implements Serializable, NetworkReadable, NetworkWritable
{
    public static final int EXPERIMENT_BEGINNING = 0;
    public static final int EXPERIMENT_FINISHING = 1;
    public static final int PERIOD_BEGINNING = 2;
    public static final int PERIOD_FINISHING = 3;
    public static final int EXPERIMENT_OFF = 4;
    public static final int EXPERIMENT_ON = 5;
    @Deprecated
    public static final int TIME_UPDATE = 6;
    public static final int CLIENT_READY = 7;
    private int updateType;
    private String updateInfo;
    private int periodNum;
    
    public int getUpdateType() {
        return this.updateType;
    }
    
    public String getUpdateMessage() {
        return this.updateInfo;
    }
    
    public int getCurrentPeriod() {
        return this.periodNum;
    }
    
    public ExpUpdate(final int updateType, final String updateInfo, final int periodNum) {
        this.periodNum = -1;
        this.updateInfo = updateInfo;
        this.updateType = updateType;
        this.periodNum = periodNum;
    }
    
    public Element prepareForNetworkOutput(final String playerType) {
        Utils.logger.debug("Xml Formatting experiment update message...");
        final Element experimentUpdate = new Element("ExperimentUpdate");
        experimentUpdate.setAttribute("updateType", Integer.toString(this.updateType)).setAttribute("updateInfo", this.getUpdateMessage()).setAttribute("currentPeriod", Integer.toString(this.periodNum));
        return experimentUpdate;
    }
    
    public boolean initFromNetworkInput(final Element root) {
        Utils.logger.debug("Analysing xml node...");
        if (!root.getName().equals("ExperimentUpdate")) {
            Utils.logger.warn("received a bad xml formatted node: bad node name. [NODE IGNORED]");
            return false;
        }
        final String updateType = root.getAttributeValue("updateType");
        final String updateInfo = root.getAttributeValue("updateInfo");
        final String currentPeriod = root.getAttributeValue("currentPeriod");
        if (updateInfo == null || updateType == null || currentPeriod == null) {
            Utils.logger.warn("received a bad xml formatted node: attribute updateInfo or updateType not found [IGNORED]");
            return false;
        }
        this.updateInfo = updateInfo;
        this.updateType = Integer.parseInt(updateType);
        this.periodNum = Integer.parseInt(currentPeriod);
        return true;
    }
}
