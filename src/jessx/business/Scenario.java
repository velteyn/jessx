// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import java.util.Iterator;
import org.jdom.Content;
import java.util.Map;
import org.jdom.Element;
import jessx.business.event.ProgrammedInfoEvent;
import jessx.business.event.ProgrammedInfoListener;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.PlayerTypeListener;
import jessx.utils.Utils;
import java.util.Vector;
import java.util.HashMap;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public class Scenario implements XmlExportable, XmlLoadable
{
    private HashMap playerTypes;
    private Vector playerTypeListeners;
    private Vector vectorInformation;
    private Vector infoListners;
    private String password;
    private boolean passwordUsed;
    private boolean listOfParticipantsUsed;
    private Vector listOfParticipants;
    
    public Scenario() {
        this.playerTypes = new HashMap();
        this.playerTypeListeners = new Vector();
        this.vectorInformation = new Vector();
        this.infoListners = new Vector();
        this.password = "";
        this.passwordUsed = false;
        this.listOfParticipantsUsed = false;
        this.listOfParticipants = new Vector();
    }
    
    public DividendLimitation getDividendInfo(final String assetName, final String playerType) {
        Utils.logger.debug("Getting dividend info for asset : " + assetName + " and playerType: " + playerType);
        return ((PlayerType) this.playerTypes.get(playerType)).getDividendInfo(assetName);
    }
    
    public void setDividendInfo(final String assetName, final String playerType, final DividendLimitation value) {
        ((PlayerType) this.playerTypes.get(playerType)).setDividendInfo(assetName, value);
    }
    
    public PlayerType getPlayerType(final String name) {
        return (PlayerType) this.playerTypes.get(name);
    }
    
    public HashMap getPlayerTypes() {
        return this.playerTypes;
    }
    
    public void addPlayerType(final PlayerType playerType) {
        if (playerType != null) {
            this.playerTypes.put(playerType.getPlayerTypeName(), playerType);
            this.firePlayerTypeAdded(playerType);
        }
    }
    
    public void removePlayerType(final PlayerType playerType) {
        if (playerType != null && this.playerTypes.containsValue(playerType)) {
            this.playerTypes.remove(playerType.getPlayerTypeName());
            this.firePlayerTypeRemoved(playerType);
        }
    }
    
    protected void firePlayerTypeAdded(final PlayerType playerType) {
        for (int i = 0; i < this.playerTypeListeners.size(); ++i) {
            ((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(playerType, 1, null));
        }
    }
    
    protected void firePlayerTypeRemoved(final PlayerType playerType) {
        for (int i = 0; i < this.playerTypeListeners.size(); ++i) {
            ((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(playerType, 0, null));
        }
    }
    
    public void addPlayerTypeListener(final PlayerTypeListener listener) {
        this.playerTypeListeners.add(listener);
    }
    
    public void removePlayerTypeListener(final PlayerTypeListener listener) {
        this.playerTypeListeners.remove(listener);
    }
    
    public Vector getListInformation() {
        return this.vectorInformation;
    }
    
    public void addInformation(final String[] info) {
        this.vectorInformation.add(info);
        this.fireInfoAdded(info);
        Utils.logger.debug("add " + info);
    }
    
    public void removeInformation(final int i) {
        this.vectorInformation.remove(i);
        this.fireInfoRemoved(i);
        Utils.logger.debug("remove info number " + i);
    }
    
    public void removeAllInformation() {
        this.vectorInformation.clear();
        this.fireInfoClear();
        Utils.logger.debug("remove all info");
    }
    
    public void changeInformation(final int i, final String[] message) {
        this.vectorInformation.set(i, message);
        Utils.logger.debug("change " + i);
    }
    
    protected void fireInfoAdded(final String[] info) {
        for (int i = 0; i < this.infoListners.size(); ++i) {
            ((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(info, 2));
            Utils.logger.debug("fireadd" + info);
        }
    }
    
    protected void fireInfoRemoved(final int num) {
        for (int i = 0; i < this.infoListners.size(); ++i) {
            ((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(new Integer(num), 0));
            Utils.logger.debug("fireremov" + num);
        }
    }
    
    protected void fireInfoClear() {
        for (int i = 0; i < this.infoListners.size(); ++i) {
            ((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(new Integer(-1), 1));
        }
        Utils.logger.debug("fireremovall-1");
    }
    
    protected void fireProgammedInfoLoad() {
        for (int i = 0; i < this.infoListners.size(); ++i) {
            ((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(this.vectorInformation, 3));
        }
    }
    
    public void addProgrammedInfoListener(final ProgrammedInfoListener listener) {
        this.infoListners.add(listener);
    }
    
    public void removedInfoListener(final ProgrammedInfoListener listener) {
        this.infoListners.remove(listener);
    }
    
    public void saveToXml(final Element node) {
        Utils.logger.debug("Saving scenario...");
        final Vector keys = Utils.convertAndSortMapToVector(this.getPlayerTypes());
        for (int keysCount = keys.size(), i = 0; i < keysCount; ++i) {
            final Element ptNode = new Element("PlayerType");
            this.getPlayerType((String) keys.get(i)).saveToXml(ptNode);
            node.addContent(ptNode);
        }
        final int size = this.vectorInformation.size();
        final Element informationNode = new Element("Information");
        for (int j = 0; j < size; ++j) {
            final Element infoNode = new Element("Information");
            infoNode.setAttribute("Content", ((String[])this.vectorInformation.get(j))[0]);
            infoNode.setAttribute("Category", ((String[])this.vectorInformation.get(j))[1]);
            infoNode.setAttribute("Period", ((String[])this.vectorInformation.get(j))[2]);
            infoNode.setAttribute("Time", ((String[])this.vectorInformation.get(j))[3]);
            informationNode.addContent(infoNode);
        }
        node.addContent(informationNode);
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading scenario...");
        Iterator ptIter = node.getChildren("PlayerType").iterator();
        while (ptIter.hasNext()) {
          Element ptElem = (Element) ptIter.next();
            final PlayerType pt = new PlayerType(ptElem);
            this.addPlayerType(pt);
        }
        final Iterator infoIter = node.getChild("Information").getChildren("Information").iterator();
        Utils.logger.debug(infoIter);
        if (this.vectorInformation.size() > 0) {
            this.removeAllInformation();
        }
        while (infoIter.hasNext()) {
        	  Element infoElem = (Element) infoIter.next();
            Utils.logger.debug("crea infoElem =" + infoElem);
            final String infoContent = new String(infoElem.getAttributeValue("Content"));
            Utils.logger.debug("------" + infoContent);
            final String infoReceivers = new String(infoElem.getAttributeValue("Category"));
            final String infoPeriode = new String(infoElem.getAttributeValue("Period"));
            final String infoTime = new String(infoElem.getAttributeValue("Time"));
            this.vectorInformation.add(new String[] { infoContent, infoReceivers, infoPeriode, infoTime });
            Utils.logger.debug(this.vectorInformation);
        }
        this.fireProgammedInfoLoad();
    }
    
    public void setPasswordUsed(final boolean b) {
        this.passwordUsed = b;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public boolean isPasswordUsed() {
        return this.passwordUsed;
    }
    
    public void setlistOfParticipantsUsed(final boolean b) {
        this.listOfParticipantsUsed = b;
    }
    
    public void setlistOfParticipants(final Vector listOfParticipants) {
        this.listOfParticipants = listOfParticipants;
    }
    
    public Vector getlistOfParticipants() {
        return this.listOfParticipants;
    }
    
    public boolean islistOfParticipantsUsed() {
        return this.listOfParticipantsUsed;
    }
}
