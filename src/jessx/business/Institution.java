package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JPanel;
import jessx.business.event.OperatorEvent;
import jessx.business.event.OperatorListener;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.server.net.NetworkCore;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public abstract class Institution implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable {
  private String name;
  
  private String quotedAsset;
  
  private OrderBook orderBook = new OrderBook();
  
  private Vector supportedOperation = new Vector();
  
  private HashMap operators = new HashMap<Object, Object>();
  
  private HashMap operationMinimalCosts = new HashMap<Object, Object>();
  
  private HashMap operationPercentageCosts = new HashMap<Object, Object>();
  
  private Vector operatorListeners = new Vector();
  
  private boolean keepingOrderBook;
  
  public Vector getSupportedOperation() {
    return this.supportedOperation;
  }
  
  public Institution() {
    initSupportedOperation();
  }
  
  public void setName(String institutionName) {
    this.name = institutionName;
    this.orderBook.setInstitution(this.name);
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setAsset(Asset asset) {
    if (this.quotedAsset == null)
      this.quotedAsset = asset.getAssetName(); 
  }
  
  public String getAssetName() {
    return this.quotedAsset;
  }
  
  public OrderBook getOrderBook() {
    return this.orderBook;
  }
  
  public JPanel getServerPanel() {
    return getInstitutionSetupGui();
  }
  
  public abstract JPanel getClientPanel(Operator paramOperator);
  
  public String getInstitutionType() {
    return getClass().toString().substring(getClass().toString().lastIndexOf(".") + 1);
  }
  
  public abstract JPanel getInstitutionSetupGui();
  
  public abstract void desactivePanel();
  
  public abstract void activePanel();
  
  /**
   * treat the operation in this method.
   * The Institution.treatOperation just decrease the operator cash due to the
   * operation cost, so do not forget to implement the operation effects by
   * overloading this metods.
   * @param op Operation
   */
  public void treatOperation(Operation op) {
    if (this.isOperationSupported(op)) {

      // getting the operation cost.
      Portfolio playerPortfolio = NetworkCore.getPlayer(op.getEmitter()).getPortfolio();
      //playerPortfolio.setCash(playerPortfolio.getCash() - this.getOperationCost(op));
      //because of the TRANSACTION costs instead of OPERATION costs

    }
  }

  private void initSupportedOperation() {
    Iterator<String> operationIterator = OperationCreator.operationFactories.keySet().iterator();
    while (operationIterator.hasNext()) {
      String opKey = operationIterator.next();
      try {
        if (isOperationSupported(OperationCreator.createOperation(opKey))) {
          this.supportedOperation.add(opKey);
          Utils.logger.info("New operation authorized: " + opKey);
        } 
      } catch (OperationNotCreatedException ex) {
        Utils.logger.warn("Unable to instantiate the following operation: " + opKey);
      } 
    } 
  }
  
  public void emptyOrderBook() {
    this.orderBook.getAsk().removeAllElements();
    this.orderBook.getBid().removeAllElements();
  }
  
  public void addOperator(Operator operator) {
    if (operator != null) {
      this.operators.put(operator.getName(), operator);
      fireOperatorAdded(operator.getName());
    } 
  }
  
  public void removeOperator(String operatorName) {
    if (operatorName != null && 
      this.operators.containsKey(operatorName)) {
      this.operators.remove(operatorName);
      fireOperatorRemoved(operatorName);
    } 
  }
  
  public void removeAllOperators() {
    Vector<String> operKeys = new Vector(getOperators().keySet());
    for (int i = 0; i < operKeys.size(); i++)
      removeOperator(operKeys.elementAt(i)); 
  }
  
  public HashMap getOperators() {
    HashMap ops = this.operators;
    return ops;
  }
  
  public Operator getOperator(String name) {
    return (Operator)this.operators.get(name);
  }
  
  public void addNewOperatorListener(OperatorListener listener) {
    this.operatorListeners.add(listener);
  }
  
  public void removeOperatorListener(OperatorListener listener) {
    this.operatorListeners.remove(listener);
  }
  
  public void fireOperatorAdded(String operName) {
    for (int i = 0; i < this.operatorListeners.size(); i++)
      ((OperatorListener)this.operatorListeners.elementAt(i)).operatorsModified(new OperatorEvent(operName, getName(), 1)); 
  }
  
  public void fireOperatorRemoved(String operName) {
    for (int i = 0; i < this.operatorListeners.size(); i++)
      ((OperatorListener)this.operatorListeners.elementAt(i)).operatorsModified(new OperatorEvent(operName, getName(), 0)); 
  }
  
  public void setMinimalCost(String operation, Float minCost) {
    this.operationMinimalCosts.put(operation, minCost);
  }
  
  public void setPercentageCost(String operation, Float percentageCost) {
    this.operationPercentageCosts.put(operation, percentageCost);
  }
  
  public void setKeepingOrderBook(boolean possible) {
    this.keepingOrderBook = possible;
  }
  
  public boolean getKeepingOrderBook() {
    return this.keepingOrderBook;
  }
  
  public float getMinimalCost(String operation) {
    if (this.operationMinimalCosts.containsKey(operation))
      return ((Float)this.operationMinimalCosts.get(operation)).floatValue(); 
    return 0.0F;
  }
  
  public float getPercentageCost(String operation) {
    if (this.operationPercentageCosts.containsKey(operation))
      return ((Float)this.operationPercentageCosts.get(operation)).floatValue(); 
    return 0.0F;
  }
  
  public float getOperationCost(Operation op) {
    return op.getOperationCost(getPercentageCost(op.getOperationName()), 
        getMinimalCost(op.getOperationName()));
  }
  
  public abstract boolean isOperationSupported(Operation paramOperation);
  
  public abstract boolean isOperationValid(Operation paramOperation);
  
  public abstract void saveToXml(Element paramElement);
  
  public abstract void loadFromXml(Element paramElement);
  
  public static void saveInstitutionToXml(Element node, Institution institutionToSave) {
    Utils.logger.debug("Saving institution " + institutionToSave.getName() + " to xml...");
    node.setAttribute("type", institutionToSave.getInstitutionType()).setAttribute("name", institutionToSave.getName());
    node.setAttribute("quotedAsset", institutionToSave.getAssetName());
    Element keepOrderBook = new Element("KeepOrderBook");
    keepOrderBook.setAttribute("allow", String.valueOf(institutionToSave.keepingOrderBook));
    node.addContent((Content)keepOrderBook);
    Utils.logger.debug("Saving operations costs...");
    Element operationsCost = new Element("OperationsCost");
    for (int i = 0; i < institutionToSave.getSupportedOperation().size(); i++) {
      Element operCost = new Element("Operation");
      String operName = (String) institutionToSave.getSupportedOperation().elementAt(i);
      operCost.setAttribute("name", operName);
      operCost.setAttribute("percentageCost", (new Float(institutionToSave.getPercentageCost(operName))).toString());
      operCost.setAttribute("minimalCost", (new Float(institutionToSave.getMinimalCost(operName))).toString());
      operationsCost.addContent((Content)operCost);
    } 
    node.addContent((Content)operationsCost);
    Utils.logger.debug("Saving operators...");
    Element operators = new Element("Operators");
    Iterator<String> operatorIter = institutionToSave.getOperators().keySet().iterator();
    while (operatorIter.hasNext()) {
      String key = operatorIter.next();
      Element op = new Element("Operator");
      institutionToSave.getOperator(key).saveToXml(op);
      operators.addContent((Content)op);
    } 
    node.addContent((Content)operators);
    Utils.logger.debug("Saving specific parameters...");
    institutionToSave.saveToXml(node);
  }
  
  public static Institution loadInstitutionFromXml(Element node) {
    Institution institution;
    Utils.logger.debug("Loading an institution...");
    Utils.logger.debug("Getting institution type...");
    String institutionType = node.getAttributeValue("type");
    if (institutionType == null) {
      Utils.logger.error("Invalid institution node: the attribute type is not defined.");
      return null;
    } 
    Utils.logger.info("Type found: " + institutionType);
    Utils.logger.debug("Getting institution name...");
    String institutionName = node.getAttributeValue("name");
    if (institutionName == null) {
      Utils.logger.error("invalid institution node: the attribute name is not defined.");
      return null;
    } 
    Utils.logger.info("Insitution name: " + institutionName);
    try {
      Utils.logger.debug("Creating institution...");
      institution = InstitutionCreator.createInstitution(institutionType);
    } catch (InstitutionNotCreatedException ex) {
      Utils.logger.error("Institution could not be created. " + ex.toString());
      return null;
    } 
    institution.setName(institutionName);
    Utils.logger.debug("Loading quotedAsset...");
    String quotedAsset = node.getAttributeValue("quotedAsset");
    if (quotedAsset == null) {
      Utils.logger.error("invlid xml file: undefined quotedAsset in the institution node.");
      return null;
    } 
    institution.quotedAsset = quotedAsset;
    Element keepOrderBook = node.getChild("KeepOrderBook");
    institution.setKeepingOrderBook(keepOrderBook.getAttributeValue("allow").equals("true"));
    Utils.logger.debug("Loading operations cost...");
    Element operationCostNode = node.getChild("OperationsCost");
    Iterator<Element> costIter = operationCostNode.getChildren("Operation").iterator();
    while (costIter.hasNext()) {
      Element cost = costIter.next();
      String operationName = cost.getAttributeValue("name");
      Utils.logger.debug("loading costs");
      Float percentageCostValue = new Float(cost.getAttributeValue("percentageCost"));
      Float minimalCostValue = new Float(cost.getAttributeValue("minimalCost"));
      if (operationName != null && minimalCostValue != null) {
        institution.setPercentageCost(operationName, percentageCostValue);
        institution.setMinimalCost(operationName, minimalCostValue);
        Utils.logger.debug("Cost loaded: operation " + operationName + "Percentage costs" + percentageCostValue + "Minimal costs " + minimalCostValue);
        continue;
      } 
      Utils.logger.error("Error reading cost: invalid xml format.");
      return null;
    } 
    Utils.logger.debug("Loading operators...");
    Element operatorsNode = node.getChild("Operators");
    Iterator<Element> operIter = operatorsNode.getChildren("Operator").iterator();
    while (operIter.hasNext()) {
      Element oper = operIter.next();
      Operator operator = new Operator(oper, institution.getName());
      institution.addOperator(operator);
    } 
    Utils.logger.debug("Loading specific parameters...");
    institution.loadFromXml(node);
    return institution;
  }
  
  public Element prepareForNetworkOutput(String pt) {
    Element rootNode = new Element("Institution");
    saveInstitutionToXml(rootNode, this);
    return rootNode;
  }
  
  public boolean initFromNetworkInput(Element rootNode) {
    loadFromXml(rootNode);
    return true;
  }
}
