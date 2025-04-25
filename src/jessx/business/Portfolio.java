// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import jessx.business.event.PortfolioEvent;
import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.business.event.PortfolioListener;
import java.util.Iterator;
import java.util.Vector;
import java.util.HashMap;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public class Portfolio implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable
{
    private float cash;
    private float nonInvestedCash;
    private HashMap investedCash;
    private HashMap ownings;
    private HashMap nonInvestedOwnings;
    private HashMap investedOwnings;
    private Vector listeners;
    
    public Portfolio() {
        this.investedCash = new HashMap();
        this.ownings = new HashMap();
        this.nonInvestedOwnings = new HashMap();
        this.investedOwnings = new HashMap();
        this.listeners = new Vector();
        try {
            this.jbInit();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public HashMap getOwnings() {
        return this.ownings;
    }
    
    public int getOwnings(final String assetName) {
        return (Integer) this.ownings.get(assetName);
    }
    
    public HashMap getNonInvestedOwnings() {
        return this.nonInvestedOwnings;
    }
    
    public void setCash(final float cash) {
        if (cash != this.cash) {
            this.cash = cash;
            this.fireCashUpdated();
        }
    }
    
    public void setNonInvestedCash(final float nonInvestedCash) {
        this.nonInvestedCash = nonInvestedCash;
    }
    
    public void setNonInvestedOwnings(final HashMap nonInvestedOwnings) {
        this.nonInvestedOwnings = (HashMap)nonInvestedOwnings.clone();
    }
    
    public float getCash() {
        return this.cash;
    }
    
    public float getNonInvestedCash() {
        return this.nonInvestedCash;
    }
    
    public float getInvestedCash(final String institutionName) {
        if (this.investedCash.containsKey(institutionName)) {
            return (float) this.investedCash.get(institutionName);
        }
        return 0.0f;
    }
    
    public int getInvestedOwnings(final String institutionName) {
        if (this.investedOwnings.containsKey(institutionName)) {
            return (int) this.investedOwnings.get(institutionName);
        }
        return 0;
    }
    
    public int getNonInvestedOwnings(final String assetName) {
        if (this.nonInvestedOwnings.containsKey(assetName)) {
            return (int) this.nonInvestedOwnings.get(assetName);
        }
        return this.getOwnings(assetName);
    }
    
    public void preInitializeInvestments() {
        this.setNonInvestedCash(this.getCash());

        Iterator owningsIter = this.getOwnings().keySet().iterator();
        while(owningsIter.hasNext()) {
                String key = (String)owningsIter.next();
                this.setNonInvestedOwnings(key, this.getOwnings(key));
        }
    }
    
    public void setInvestmentsWhenKeepingOrderBook(final String institutionName, final String assetName) {
        if (this.investedCash.containsKey(institutionName)) {
            this.addToNonInvestedCash(-this.getInvestedCash(institutionName));
        }
        if (this.nonInvestedOwnings.containsKey(assetName)) {
            this.addToNonInvestedOwnings(-this.getInvestedOwnings(institutionName), assetName);
        }
    }
    
    public void setInvestmentsWhenNotKeepingOrderBook(final String institutionName) {
        if (this.investedCash.containsKey(institutionName)) {
            this.investedCash.put(institutionName, new Float(0.0f));
            this.investedOwnings.put(institutionName, new Integer(0));
        }
    }
    
    public void addToInvestedCash(final float amount, final String institutionName) {
        this.investedCash.put(institutionName, new Float(this.getInvestedCash(institutionName) + amount));
    }
    
    public void addToNonInvestedCash(final float amount) {
        this.setNonInvestedCash(this.getNonInvestedCash() + amount);
    }
    
    public void addToInvestedOwnings(final int quantity, final String institutionName) {
        this.investedOwnings.put(institutionName, new Integer(this.getInvestedOwnings(institutionName) + quantity));
    }
    
    public void addToNonInvestedOwnings(final int quantity, final String assetName) {
        this.nonInvestedOwnings.put(assetName, new Integer(this.getNonInvestedOwnings(assetName) + quantity));
    }
    
    public void addToOwnings(final int quantity, final String assetName) {
        this.ownings.put(assetName, new Integer(this.getOwnings(assetName) + quantity));
    }
    
    public void addToCash(final float amount) {
        this.cash += amount;
    }
    
    public void setOwnings(final String assetName, final int qtty) {
        if (this.ownings.containsKey(assetName)) {
            if (this.getOwnings(assetName) != qtty) {
                this.ownings.put(assetName, new Integer(qtty));
                this.fireAssetUpdated(assetName);
            }
        }
        else {
            this.ownings.put(assetName, new Integer(qtty));
            this.fireAssetAdded(assetName);
        }
    }
    
    public void setNonInvestedOwnings(final String assetName, final int qtty) {
        this.nonInvestedOwnings.put(assetName, new Integer(qtty));
    }
    
    public void removeAssetFromOwnings(final String assetName) {
        this.ownings.remove(assetName);
        this.fireAssetRemoved(assetName);
    }
    
    public Portfolio(final float cash, final HashMap ownings) {
        this.investedCash = new HashMap();
        this.ownings = new HashMap();
        this.nonInvestedOwnings = new HashMap();
        this.investedOwnings = new HashMap();
        this.listeners = new Vector();
        this.setCash(cash);
        Iterator assetNameIterator = ownings.keySet().iterator();
        while(assetNameIterator.hasNext()) {
          String key = (String)assetNameIterator.next();
            this.setOwnings(key, (Integer) ownings.get(key));
        }
    }
    
    public float operationCost(final int quantity, final float price, final float percentageCost, final float minimalCost) {
        final float proportionalCost = quantity * price * percentageCost / 100.0f;
        return Math.max(minimalCost, proportionalCost);
    }
    
    public void boughtAssets(final String assetName, final float amount, final int quantity) {
        this.addToCash(-amount);
        this.addToNonInvestedCash(-amount);
        this.addToOwnings(quantity, assetName);
        this.addToNonInvestedOwnings(quantity, assetName);
    }
    
    public void boughtAssetsInOrderBook(final String assetName, final float dealPrice, final int quantity, final String institutionName, final float percentageCost, final float minimalCost) {
        this.addToCash(-quantity * dealPrice - this.operationCost(quantity, dealPrice, percentageCost, minimalCost));
        this.addToNonInvestedCash(quantity * this.operationCost(1, dealPrice, percentageCost, minimalCost) - this.operationCost(quantity, dealPrice, percentageCost, minimalCost));
        this.addToInvestedCash(-quantity * (dealPrice + quantity * this.operationCost(1, dealPrice, percentageCost, minimalCost)), institutionName);
        this.addToOwnings(quantity, assetName);
        this.addToNonInvestedOwnings(quantity, assetName);
    }
    
    public void soldAssets(final String assetName, final float amount, final int quantity) {
        this.addToCash(amount);
        this.addToNonInvestedCash(amount);
        this.addToOwnings(-quantity, assetName);
        this.addToNonInvestedOwnings(-quantity, assetName);
    }
    
    public void soldAssetsInOrderBook(final String assetName, final float dealPrice, final int quantity, final String institutionName, final float percentageCost, final float minimalCost) {
        this.addToCash(quantity * dealPrice - this.operationCost(quantity, dealPrice, percentageCost, minimalCost));
        this.addToInvestedCash(-quantity * this.operationCost(1, dealPrice, percentageCost, minimalCost), institutionName);
        this.addToNonInvestedCash(quantity * dealPrice + quantity * this.operationCost(1, dealPrice, percentageCost, minimalCost) - this.operationCost(quantity, dealPrice, percentageCost, minimalCost));
        this.addToOwnings(-quantity, assetName);
        this.addToInvestedOwnings(-quantity, institutionName);
    }
    
    public void cancelOrder(final int side, final float price, final int quantity, final String assetName, final String institutionName, final float orderPercentageCost, final float orderMinimalCost, final float deletionPercentageCost, final float deletionMinimalCost) {
        this.setCash(this.getCash() - this.operationCost(quantity, price, deletionPercentageCost, deletionMinimalCost));
        this.addToNonInvestedCash(side * price * quantity + quantity * this.operationCost(1, price, orderPercentageCost, orderMinimalCost) - this.operationCost(quantity, price, deletionPercentageCost, deletionMinimalCost));
        this.addToInvestedCash(-side * price * quantity - quantity * this.operationCost(1, price, orderPercentageCost, orderMinimalCost), institutionName);
        if (side == 0) {
            this.addToNonInvestedOwnings(quantity, assetName);
            this.addToInvestedOwnings(-quantity, institutionName);
        }
    }
    
    public void wantedToBeBoughtAssets(final float amount, final String institutionName) {
        this.addToNonInvestedCash(-amount);
        this.addToInvestedCash(amount, institutionName);
    }
    
    public void wantedToBeSoldAssets(final float amount, final int quantity, final String assetName, final String institutionName) {
        this.addToNonInvestedCash(-amount);
        this.addToInvestedCash(amount, institutionName);
        this.addToNonInvestedOwnings(-quantity, assetName);
        this.addToInvestedOwnings(quantity, institutionName);
    }
    
    public float payDividend(final String assetName, final float dividend) {
        this.setCash(this.getCash() + this.getOwnings(assetName) * dividend);
        return this.getOwnings(assetName) * dividend;
    }
    
    public void addListener(final PortfolioListener listener) {
        if (!this.listeners.contains(listener)) {}
        this.listeners.add(listener);
    }
    
    public void removeListener(final PortfolioListener listener) {
        this.listeners.remove(listener);
    }
    
    public void saveToXml(final Element node) {
        Utils.logger.debug("Saving portfolio to xml...");
        node.setAttribute("cash", Float.toString(this.getCash()));
        Iterator owningsIter = this.getOwnings().keySet().iterator();
        while(owningsIter.hasNext()) {
          String key = (String)owningsIter.next();
            node.addContent(new Element("Owning").setAttribute("asset", key).setAttribute("qtty", Integer.toString(this.getOwnings(key))));
        }
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading portfolio from xml...");
        final String cash = node.getAttributeValue("cash");
        if (cash == null) {
            Utils.logger.error("Invalid xml portfolio node: attribute cash not found.");
            return;
        }
        this.setCash(Float.parseFloat(cash));
        Iterator owningsIter = node.getChildren("Owning").iterator();
        while (owningsIter.hasNext()) {
          Element owning = (Element)owningsIter.next();
            final String asset = owning.getAttributeValue("asset");
            final String qtty = owning.getAttributeValue("qtty");
            if (asset == null || qtty == null) {
                Utils.logger.error("Invalid owning xml node: attributes asset and/or qtty not found.");
                return;
            }
            this.setOwnings(asset, Integer.parseInt(qtty));
        }
    }
    
    public Object clone() {
        return new Portfolio(this.cash, this.ownings);
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element rootPortfolio = new Element("Portfolio");
        this.saveToXml(rootPortfolio);
        return rootPortfolio;
    }
    
    public boolean initFromNetworkInput(final Element rootNode) {
        this.loadFromXml(rootNode);
        return true;
    }
    
    private void fireAssetUpdated(final String assetName) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((PortfolioListener) this.listeners.elementAt(i)).portfolioModified(new PortfolioEvent(assetName, 1));
        }
    }
    
    private void fireCashUpdated() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((PortfolioListener) this.listeners.elementAt(i)).portfolioModified(new PortfolioEvent(0));
        }
    }
    
    private void fireAssetAdded(final String assetName) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((PortfolioListener) this.listeners.elementAt(i)).portfolioModified(new PortfolioEvent(assetName, 2));
        }
    }
    
    private void fireAssetRemoved(final String assetName) {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((PortfolioListener) this.listeners.elementAt(i)).portfolioModified(new PortfolioEvent(assetName, 3));
        }
    }
    
    private void jbInit() throws Exception {
    }
}
