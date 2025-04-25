// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import jessx.business.event.PlayerTypeEvent;
import org.jdom.Content;
import java.util.Map;
import jessx.utils.Utils;
import jessx.business.event.AssetEvent;
import org.jdom.Element;
import java.util.Iterator;
import jessx.business.event.PlayerTypeListener;
import java.util.Vector;
import java.util.HashMap;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import jessx.business.event.AssetListener;

public class PlayerType implements AssetListener, XmlLoadable, XmlExportable
{
    private HashMap operatorList;
    private Vector institutionsVect;
    private Portfolio initialPortfolio;
    private Vector playerTypeListeners;
    private String playerTypeName;
    private HashMap dividendInfo;
    
    public String getPlayerTypeName() {
        return this.playerTypeName;
    }
    
    public HashMap getOperatorsPlayed() {
        return this.operatorList;
    }
    
    public Operator getOperatorPlayed(final String operatorCompleteName) {
        return (Operator) this.operatorList.get(operatorCompleteName);
    }
    
    public Vector getInstitutionsWherePlaying() {
        return this.institutionsVect;
    }
    
    public Portfolio getPortfolio() {
        return this.initialPortfolio;
    }
    
    public void setPortfolio(final Portfolio portfolio) {
        this.initialPortfolio = portfolio;
    }
    
    public void addOperatorPlayed(final Operator oper) {
        this.operatorList.put(oper.toString(), oper);
        this.fireOperatorPlayedAdded(oper);
    }
    
    public void addInstitution(final String institution) {
        this.institutionsVect.add(institution);
    }
    
    public void removeOperatorPlayed(final Operator oper) {
        this.operatorList.remove(oper.toString());
        this.fireOperatorPlayedRemoved(oper);
    }
    
    public DividendLimitation getDividendInfo(final String assetName) {
        return (DividendLimitation) this.dividendInfo.get(assetName);
    }
    
    public void setDividendInfo(final String assetName, final DividendLimitation divInfo) {
        this.dividendInfo.put(assetName, divInfo);
    }
    
    public void addPlayerTypeListener(final PlayerTypeListener listener) {
        this.playerTypeListeners.add(listener);
    }
    
    public void removePlayerTypListener(final PlayerTypeListener listener) {
        this.playerTypeListeners.remove(listener);
    }
    
    public PlayerType(final String name) {
        this.operatorList = new HashMap();
        this.institutionsVect = new Vector();
        this.playerTypeListeners = new Vector();
        this.dividendInfo = new HashMap();
        this.playerTypeName = name;
        this.initialPortfolio = new Portfolio(0.0f, new HashMap());
        Iterator assetIter = BusinessCore.getAssets().keySet().iterator();
        while (assetIter.hasNext()) {
          String key = (String) assetIter.next();
            this.setDividendInfo(key, new DividendLimitation(this.getPlayerTypeName(), key));
        }
        BusinessCore.addAssetListener(this);
    }
    
    public PlayerType(final Element xmlPTNode) {
        this("");
        this.loadFromXml(xmlPTNode);
    }
    
    @Override
    public String toString() {
        return this.getPlayerTypeName();
    }
    
    public void assetsModified(final AssetEvent e) {
        if (e.getEvent() == 1) {
            if (this.dividendInfo.get(e.getAssetName()) == null) {
                this.dividendInfo.put(e.getAssetName(), new DividendLimitation(this.getPlayerTypeName(), e.getAssetName()));
            }
        }
        else if (e.getEvent() == 0) {
            this.dividendInfo.remove(e.getAssetName());
        }
    }
    
    public void saveToXml(final Element node) {
        Utils.logger.debug("Saving playerType " + this.getPlayerTypeName() + "...");
        Utils.logger.debug("Saving playerType name...");
        node.setAttribute("name", this.getPlayerTypeName());
        final Vector keys = Utils.convertAndSortMapToVector(this.getOperatorsPlayed());
        final int keysCount = keys.size();
        Utils.logger.debug("Saving operator list...");
        final Element opPlayedNode = new Element("OperatorsPlayed");
        for (int i = 0; i < keysCount; ++i) {
            final Element op = new Element("Operator");
            op.setAttribute("name", keys.get(i).toString());
            opPlayedNode.addContent(op);
            Utils.logger.info("\n" + keys.get(i).toString() + "\n");
        }
        node.addContent(opPlayedNode);
        Utils.logger.debug("Saving initial portfolio...");
        final Element portfolioNode = new Element("Portfolio");
        this.getPortfolio().saveToXml(portfolioNode);
        node.addContent(portfolioNode);
        Utils.logger.debug("Saving dividends Info...");
        Iterator divInfosIter = this.dividendInfo.keySet().iterator();
        while(divInfosIter.hasNext()) {
          String key = (String)divInfosIter.next();
            Utils.logger.debug("Saving dividend info on the asset: " + key);
            final Element divInfoNode = new Element("DividendInfo");
            this.getDividendInfo(key).saveToXml(divInfoNode);
            node.addContent(divInfoNode);
        }
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading playerType...");
        Utils.logger.debug("Loading name...");
        final String ptName = node.getAttributeValue("name");
        if (ptName == null) {
            Utils.logger.error("Invalid xml playertype node: attribute name not found.");
            return;
        }
        this.playerTypeName = ptName;
        Utils.logger.info("playertype name : " + ptName);
        Utils.logger.debug("operator played...");
        Iterator opPlayedIter = node.getChild("OperatorsPlayed").getChildren("Operator").iterator();
        while(opPlayedIter.hasNext()) {
          Element op = (Element)opPlayedIter.next();
            final String opCompleteName = op.getAttributeValue("name");
            if (opCompleteName == null) {
                Utils.logger.error("Invalid operatorPlayed xml node : attribue name not found.");
                return;
            }
            Utils.logger.debug("Operator found. Complete name: " + opCompleteName);
            final int index = opCompleteName.lastIndexOf(" on ");
            final String opName = opCompleteName.substring(0, index);
            final String institutionName = opCompleteName.substring(index + 4);
            Utils.logger.debug("Operator name: >-" + opName + "-< institution name: >-" + institutionName + "-<");
            this.addOperatorPlayed(BusinessCore.getInstitution(institutionName).getOperator(opName));
            this.addInstitution(institutionName);
        }
        Utils.logger.debug("Loading portfolio...");
        final Portfolio portfolio = new Portfolio(0.0f, new HashMap());
        Utils.logger.debug("\nPlayerType avant " + this.initialPortfolio.getNonInvestedCash() + "\n");
        portfolio.loadFromXml(node.getChild("Portfolio"));
        this.setPortfolio(portfolio);
        Utils.logger.debug("\nPlayerType apres " + this.initialPortfolio.getNonInvestedCash() + "\n");
     // - loading dividendInfo
        Utils.logger.debug("Loading dividend Infos...");
        Iterator divInfoNodes = node.getChildren("DividendInfo").iterator();
        while(divInfoNodes.hasNext()) {
          DividendLimitation divInfo = new DividendLimitation(this.getPlayerTypeName(),(Element)divInfoNodes.next());
          this.setDividendInfo(divInfo.getAssetName(),divInfo);
        }
    }
    
    private void fireOperatorPlayedAdded(final Operator oper) {
        for (int i = 0; i < this.playerTypeListeners.size(); ++i) {
            ((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(this, 2, oper));
        }
    }
    
    private void fireOperatorPlayedRemoved(final Operator oper) {
        for (int i = 0; i < this.playerTypeListeners.size(); ++i) {
            ((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(this, 3, oper));
        }
    }
}
