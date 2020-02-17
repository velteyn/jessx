package jessx.business;

import java.util.Iterator;
import java.util.Vector;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import org.jdom.Content;
import org.jdom.Element;

public class OrderBook implements NetworkWritable, NetworkReadable, XmlExportable {
  private String institution;
  
  private Vector bid = new Vector();
  
  private Vector ask = new Vector();
  
  public Vector getBid() {
    return this.bid;
  }
  
  public Vector getAsk() {
    return this.ask;
  }
  
  public String getInstitution() {
    return this.institution;
  }
  
  public void setInstitution(String institution) {
    this.institution = institution;
  }
  
  public void reinit() {
    this.ask.clear();
    this.bid.clear();
  }
  
  public boolean initFromNetworkInput(Element root) {
    String institution = root.getAttributeValue("institution");
    if (institution == null) {
      Utils.logger.debug("Invalid orderbook xml node: institution attribute is missing.");
      return false;
    } 
    this.institution = institution;
    this.bid.removeAllElements();
    this.ask.removeAllElements();
    Element askNode = root.getChild("Ask");
    Element bidNode = root.getChild("Bid");
    if (askNode != null) {
      Iterator<Element> iter = askNode.getChildren().iterator();
      while (iter.hasNext()) {
        try {
          Operation op = Operation.initOperationFromXml(iter.next());
          if (op instanceof Order)
            this.ask.add(op); 
        } catch (OperationNotCreatedException ex) {
          Utils.logger.error("Unable to load operation. [IGNORED]");
        } 
      } 
    } 
    if (bidNode != null) {
      Iterator<Element> iter = bidNode.getChildren().iterator();
      while (iter.hasNext()) {
        try {
          Operation op = Operation.initOperationFromXml(iter.next());
          if (op instanceof Order)
            this.bid.add(op); 
        } catch (OperationNotCreatedException ex) {
          Utils.logger.error("Unable to load operation. [IGNORED]");
        } 
      } 
    } 
    return true;
  }
  
  public Element prepareForNetworkOutput(String pt) {
    Element askNode = new Element("Ask");
    Element bidNode = new Element("Bid");
    int i = 0;
    while (i < this.ask.size()) {
      if (((Order)this.ask.elementAt(i)).isVisibleInOrderbook())
        askNode.addContent((Content)((Order)this.ask.elementAt(i)).prepareForNetworkOutput(pt)); 
      i++;
    } 
    i = 0;
    while (i < this.bid.size()) {
      if (((Order)this.bid.elementAt(i)).isVisibleInOrderbook())
        bidNode.addContent((Content)((Order)this.bid.elementAt(i)).prepareForNetworkOutput(pt)); 
      i++;
    } 
    Element root = new Element("OrderBook");
    root.addContent((Content)askNode);
    root.addContent((Content)bidNode);
    root.setAttribute("institution", this.institution);
    return root;
  }
  
  public void saveToXml(Element rootNode) {
    Element askNode = new Element("Ask");
    Element bidNode = new Element("Bid");
    int i = 0;
    while (i < this.ask.size()) {
      askNode.addContent((Content)((Order)this.ask.elementAt(i)).prepareForNetworkOutput(""));
      i++;
    } 
    i = 0;
    while (i < this.bid.size()) {
      bidNode.addContent((Content)((Order)this.bid.elementAt(i)).prepareForNetworkOutput(""));
      i++;
    } 
    rootNode.addContent((Content)askNode);
    rootNode.addContent((Content)bidNode);
    rootNode.setAttribute("institution", this.institution);
  }
  
  public Order getOrder(int orderId) {
    int i;
    for (i = this.ask.size() - 1; i >= 0; i--) {
      if (((Order)this.ask.elementAt(i)).getId() == orderId)
        return (Order) this.ask.elementAt(i); 
    } 
    for (i = this.bid.size() - 1; i >= 0; i--) {
      if (((Order)this.bid.elementAt(i)).getId() == orderId)
        return (Order) this.bid.elementAt(i); 
    } 
		return (Order) this.ask.elementAt(0);
  }
  
  public void deleteOrder(int orderId) {
    Utils.logger.debug("Deleting order Id = " + orderId + " ...");
    int i;
    for (i = this.ask.size() - 1; i >= 0; i--) {
      if (((Order)this.ask.elementAt(i)).getId() == orderId) {
        Utils.logger.debug("Order found. It was an ask. Removing ...");
        this.ask.removeElementAt(i);
        Utils.logger.debug("Order successfully removed.");
      } 
    } 
    for (i = this.bid.size() - 1; i >= 0; i--) {
      if (((Order)this.bid.elementAt(i)).getId() == orderId) {
        Utils.logger.debug("Order found. It was an bid. Removing ...");
        this.bid.removeElementAt(i);
        Utils.logger.debug("Order successfully removed.");
      } 
    } 
  }
  
  public void clearPlayer(String playerName) {
    int i;
    for (i = this.ask.size() - 1; i >= 0; i--) {
      if (((Order)this.ask.elementAt(i)).getEmitter().equals(playerName))
        this.ask.removeElementAt(i); 
    } 
    for (i = this.bid.size() - 1; i >= 0; i--) {
      if (((Order)this.bid.elementAt(i)).getEmitter().equals(playerName))
        this.bid.removeElementAt(i); 
    } 
  }
}
