package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import jessx.business.event.PortfolioEvent;
import jessx.business.event.PortfolioListener;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public class Portfolio implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable {
  private float cash;
  
  private float nonInvestedCash;
  
  public Portfolio() {
    try {
      jbInit();
    } catch (Exception ex) {
      ex.printStackTrace();
    } 
  }
  
  private HashMap investedCash = new HashMap<Object, Object>();
  
  private HashMap ownings = new HashMap<Object, Object>();
  
  private HashMap nonInvestedOwnings = new HashMap<Object, Object>();
  
  private HashMap investedOwnings = new HashMap<Object, Object>();
  
  private Vector listeners = new Vector();
  
  public HashMap getOwnings() {
    return this.ownings;
  }
  
  public int getOwnings(String assetName) {
    return ((Integer)this.ownings.get(assetName)).intValue();
  }
  
  public HashMap getNonInvestedOwnings() {
    return this.nonInvestedOwnings;
  }
  
  public void setCash(float cash) {
    if (cash != this.cash) {
      this.cash = cash;
      fireCashUpdated();
    } 
  }
  
  public void setNonInvestedCash(float nonInvestedCash) {
    this.nonInvestedCash = nonInvestedCash;
  }
  
  public void setNonInvestedOwnings(HashMap nonInvestedOwnings) {
    this.nonInvestedOwnings = (HashMap)nonInvestedOwnings.clone();
  }
  
  public float getCash() {
    return this.cash;
  }
  
  public float getNonInvestedCash() {
    return this.nonInvestedCash;
  }
  
  public float getInvestedCash(String institutionName) {
    if (this.investedCash.containsKey(institutionName))
      return ((Float)this.investedCash.get(institutionName)).floatValue(); 
    return 0.0F;
  }
  
  public int getInvestedOwnings(String institutionName) {
    if (this.investedOwnings.containsKey(institutionName))
      return ((Integer)this.investedOwnings.get(institutionName)).intValue(); 
    return 0;
  }
  
  public int getNonInvestedOwnings(String assetName) {
    if (this.nonInvestedOwnings.containsKey(assetName))
      return ((Integer)this.nonInvestedOwnings.get(assetName)).intValue(); 
    return getOwnings(assetName);
  }
  
  public void preInitializeInvestments() {
    setNonInvestedCash(getCash());
    Iterator<String> owningsIter = getOwnings().keySet().iterator();
    while (owningsIter.hasNext()) {
      String key = owningsIter.next();
      setNonInvestedOwnings(key, getOwnings(key));
    } 
  }
  
  public void setInvestmentsWhenKeepingOrderBook(String institutionName, String assetName) {
    if (this.investedCash.containsKey(institutionName))
      addToNonInvestedCash(-getInvestedCash(institutionName)); 
    if (this.nonInvestedOwnings.containsKey(assetName))
      addToNonInvestedOwnings(-getInvestedOwnings(institutionName), assetName); 
  }
  
  public void setInvestmentsWhenNotKeepingOrderBook(String institutionName) {
    if (this.investedCash.containsKey(institutionName)) {
      this.investedCash.put(institutionName, new Float(0.0F));
      this.investedOwnings.put(institutionName, new Integer(0));
    } 
  }
  
  public void addToInvestedCash(float amount, String institutionName) {
    this.investedCash.put(institutionName, new Float(getInvestedCash(institutionName) + amount));
  }
  
  public void addToNonInvestedCash(float amount) {
    setNonInvestedCash(getNonInvestedCash() + amount);
  }
  
  public void addToInvestedOwnings(int quantity, String institutionName) {
    this.investedOwnings.put(institutionName, new Integer(getInvestedOwnings(institutionName) + quantity));
  }
  
  public void addToNonInvestedOwnings(int quantity, String assetName) {
    this.nonInvestedOwnings.put(assetName, new Integer(getNonInvestedOwnings(assetName) + quantity));
  }
  
  public void addToOwnings(int quantity, String assetName) {
    this.ownings.put(assetName, new Integer(getOwnings(assetName) + quantity));
  }
  
  public void addToCash(float amount) {
    this.cash += amount;
  }
  
  public void setOwnings(String assetName, int qtty) {
    if (this.ownings.containsKey(assetName)) {
      if (getOwnings(assetName) != qtty) {
        this.ownings.put(assetName, new Integer(qtty));
        fireAssetUpdated(assetName);
      } 
    } else {
      this.ownings.put(assetName, new Integer(qtty));
      fireAssetAdded(assetName);
    } 
  }
  
  public void setNonInvestedOwnings(String assetName, int qtty) {
    this.nonInvestedOwnings.put(assetName, new Integer(qtty));
  }
  
  public void removeAssetFromOwnings(String assetName) {
    this.ownings.remove(assetName);
    fireAssetRemoved(assetName);
  }
  
  public Portfolio(float cash, HashMap ownings) {
    setCash(cash);
    Iterator<String> assetNameIterator = ownings.keySet().iterator();
    while (assetNameIterator.hasNext()) {
      String key = assetNameIterator.next();
      setOwnings(key, ((Integer)ownings.get(key)).intValue());
    } 
  }
  
  public float operationCost(int quantity, float price, float percentageCost, float minimalCost) {
    float proportionalCost = quantity * price * percentageCost / 100.0F;
    return Math.max(minimalCost, proportionalCost);
  }
  
  public void boughtAssets(String assetName, float amount, int quantity) {
    addToCash(-amount);
    addToNonInvestedCash(-amount);
    addToOwnings(quantity, assetName);
    addToNonInvestedOwnings(quantity, assetName);
  }
  
  public void boughtAssetsInOrderBook(String assetName, float dealPrice, int quantity, String institutionName, float percentageCost, float minimalCost) {
    addToCash(-quantity * dealPrice - 
        operationCost(quantity, dealPrice, percentageCost, minimalCost));
    addToNonInvestedCash(quantity * operationCost(1, dealPrice, percentageCost, minimalCost) - 
        operationCost(quantity, dealPrice, percentageCost, minimalCost));
    addToInvestedCash(-quantity * (dealPrice + quantity * operationCost(1, dealPrice, percentageCost, minimalCost)), institutionName);
    addToOwnings(quantity, assetName);
    addToNonInvestedOwnings(quantity, assetName);
  }
  
  public void soldAssets(String assetName, float amount, int quantity) {
    addToCash(amount);
    addToNonInvestedCash(amount);
    addToOwnings(-quantity, assetName);
    addToNonInvestedOwnings(-quantity, assetName);
  }
  
  public void soldAssetsInOrderBook(String assetName, float dealPrice, int quantity, String institutionName, float percentageCost, float minimalCost) {
    addToCash(quantity * dealPrice - operationCost(quantity, dealPrice, percentageCost, minimalCost));
    addToInvestedCash(-quantity * operationCost(1, dealPrice, percentageCost, minimalCost), institutionName);
    addToNonInvestedCash(quantity * dealPrice + 
        quantity * operationCost(1, dealPrice, percentageCost, minimalCost) - 
        operationCost(quantity, dealPrice, percentageCost, minimalCost));
    addToOwnings(-quantity, assetName);
    addToInvestedOwnings(-quantity, institutionName);
  }
  
  public void cancelOrder(int side, float price, int quantity, String assetName, String institutionName, float orderPercentageCost, float orderMinimalCost, float deletionPercentageCost, float deletionMinimalCost) {
    setCash(getCash() - operationCost(quantity, price, deletionPercentageCost, deletionMinimalCost));
    addToNonInvestedCash(side * price * quantity + 
        quantity * operationCost(1, price, orderPercentageCost, orderMinimalCost) - 
        operationCost(quantity, price, deletionPercentageCost, deletionMinimalCost));
    addToInvestedCash(-side * price * quantity - 
        quantity * operationCost(1, price, orderPercentageCost, orderMinimalCost), institutionName);
    if (side == 0) {
      addToNonInvestedOwnings(quantity, assetName);
      addToInvestedOwnings(-quantity, institutionName);
    } 
  }
  
  public void wantedToBeBoughtAssets(float amount, String institutionName) {
    addToNonInvestedCash(-amount);
    addToInvestedCash(amount, institutionName);
  }
  
  public void wantedToBeSoldAssets(float amount, int quantity, String assetName, String institutionName) {
    addToNonInvestedCash(-amount);
    addToInvestedCash(amount, institutionName);
    addToNonInvestedOwnings(-quantity, assetName);
    addToInvestedOwnings(quantity, institutionName);
  }
  
  public float payDividend(String assetName, float dividend) {
    setCash(getCash() + getOwnings(assetName) * dividend);
    return getOwnings(assetName) * dividend;
  }
  
  public void addListener(PortfolioListener listener) {
    if (!this.listeners.contains(listener));
    this.listeners.add(listener);
  }
  
  public void removeListener(PortfolioListener listener) {
    this.listeners.remove(listener);
  }
  
  public void saveToXml(Element node) {
    Utils.logger.debug("Saving portfolio to xml...");
    node.setAttribute("cash", Float.toString(getCash()));
    Iterator<String> owningsIter = getOwnings().keySet().iterator();
    while (owningsIter.hasNext()) {
      String key = owningsIter.next();
      node.addContent((Content)(new Element("Owning"))
          .setAttribute("asset", key)
          .setAttribute("qtty", 
            Integer.toString(getOwnings(key))));
    } 
  }
  
  public void loadFromXml(Element node) {
    Utils.logger.debug("Loading portfolio from xml...");
    String cash = node.getAttributeValue("cash");
    if (cash == null) {
      Utils.logger.error("Invalid xml portfolio node: attribute cash not found.");
      return;
    } 
    setCash(Float.parseFloat(cash));
    Iterator<Element> owningsIter = node.getChildren("Owning").iterator();
    while (owningsIter.hasNext()) {
      Element owning = owningsIter.next();
      String asset = owning.getAttributeValue("asset");
      String qtty = owning.getAttributeValue("qtty");
      if (asset == null || qtty == null) {
        Utils.logger.error("Invalid owning xml node: attributes asset and/or qtty not found.");
        return;
      } 
      setOwnings(asset, Integer.parseInt(qtty));
    } 
  }
  
  public Object clone() {
    return new Portfolio(this.cash, this.ownings);
  }
  
  public Element prepareForNetworkOutput(String pt) {
    Element rootPortfolio = new Element("Portfolio");
    saveToXml(rootPortfolio);
    return rootPortfolio;
  }
  
  public boolean initFromNetworkInput(Element rootNode) {
    loadFromXml(rootNode);
    return true;
  }
  
  private void fireAssetUpdated(String assetName) {
    for (int i = 0; i < this.listeners.size(); i++)
      ((PortfolioListener)this.listeners.elementAt(i))
        .portfolioModified(new PortfolioEvent(assetName, 
            1)); 
  }
  
  private void fireCashUpdated() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((PortfolioListener)this.listeners.elementAt(i))
        .portfolioModified(new PortfolioEvent(0)); 
  }
  
  private void fireAssetAdded(String assetName) {
    for (int i = 0; i < this.listeners.size(); i++)
      ((PortfolioListener)this.listeners.elementAt(i))
        .portfolioModified(new PortfolioEvent(assetName, 
            2)); 
  }
  
  private void fireAssetRemoved(String assetName) {
    for (int i = 0; i < this.listeners.size(); i++)
      ((PortfolioListener)this.listeners.elementAt(i))
        .portfolioModified(new PortfolioEvent(assetName, 
            3)); 
  }
  
  private void jbInit() throws Exception {}
}
