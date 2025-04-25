// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Content;
import org.jdom.Element;
import jessx.business.event.OperatorEvent;
import jessx.business.event.OperatorListener;
import java.util.Collection;
import java.util.Iterator;
import jessx.utils.Utils;
import jessx.server.net.NetworkCore;
import javax.swing.JPanel;
import java.util.HashMap;
import java.util.Vector;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public abstract class Institution implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable
{
    private String name;
    private String quotedAsset;
    private OrderBook orderBook;
    private Vector supportedOperation;
    private HashMap operators;
    private HashMap operationMinimalCosts;
    private HashMap operationPercentageCosts;
    private Vector operatorListeners;
    private boolean keepingOrderBook;
    
    public Vector getSupportedOperation() {
        return this.supportedOperation;
    }
    
    public Institution() {
        this.orderBook = new OrderBook();
        this.supportedOperation = new Vector();
        this.operators = new HashMap();
        this.operationMinimalCosts = new HashMap();
        this.operationPercentageCosts = new HashMap();
        this.operatorListeners = new Vector();
        this.initSupportedOperation();
    }
    
    public void setName(final String institutionName) {
        this.name = institutionName;
        this.orderBook.setInstitution(this.name);
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setAsset(final Asset asset) {
        if (this.quotedAsset == null) {
            this.quotedAsset = asset.getAssetName();
        }
    }
    
    public String getAssetName() {
        return this.quotedAsset;
    }
    
    public OrderBook getOrderBook() {
        return this.orderBook;
    }
    
    public JPanel getServerPanel() {
        return this.getInstitutionSetupGui();
    }
    
    public abstract JPanel getClientPanel(final Operator p0);
    
    public String getInstitutionType() {
        return this.getClass().toString().substring(this.getClass().toString().lastIndexOf(".") + 1);
    }
    
    public abstract JPanel getInstitutionSetupGui();
    
    public abstract void desactivePanel();
    
    public abstract void activePanel();
    
    public void treatOperation(final Operation op) {
        if (this.isOperationSupported(op)) {
            NetworkCore.getPlayer(op.getEmitter()).getPortfolio();
        }
    }
    
    private void initSupportedOperation() {
    	 Iterator operationIterator = OperationCreator.operationFactories.keySet().iterator();
    	    while (operationIterator.hasNext()) {
    	      String opKey = (String)operationIterator.next();
            try {
                if (!this.isOperationSupported(OperationCreator.createOperation(opKey))) {
                    continue;
                }
                this.supportedOperation.add(opKey);
                Utils.logger.info("New operation authorized: " + opKey);
            }
            catch (final OperationNotCreatedException ex) {
                Utils.logger.warn("Unable to instantiate the following operation: " + opKey);
            }
        }
    }
    
    public void emptyOrderBook() {
        this.orderBook.getAsk().removeAllElements();
        this.orderBook.getBid().removeAllElements();
    }
    
    public void addOperator(final Operator operator) {
        if (operator != null) {
            this.operators.put(operator.getName(), operator);
            this.fireOperatorAdded(operator.getName());
        }
    }
    
    public void removeOperator(final String operatorName) {
        if (operatorName != null && this.operators.containsKey(operatorName)) {
            this.operators.remove(operatorName);
            this.fireOperatorRemoved(operatorName);
        }
    }
    
    public void removeAllOperators() {
        final Vector operKeys = new Vector(this.getOperators().keySet());
        for (int i = 0; i < operKeys.size(); ++i) {
            this.removeOperator((String) operKeys.elementAt(i));
        }
    }
    
    public HashMap getOperators() {
        final HashMap ops = this.operators;
        return ops;
    }
    
    public Operator getOperator(final String name) {
        return (Operator) this.operators.get(name);
    }
    
    public void addNewOperatorListener(final OperatorListener listener) {
        this.operatorListeners.add(listener);
    }
    
    public void removeOperatorListener(final OperatorListener listener) {
        this.operatorListeners.remove(listener);
    }
    
    public void fireOperatorAdded(final String operName) {
        for (int i = 0; i < this.operatorListeners.size(); ++i) {
            ((OperatorListener) this.operatorListeners.elementAt(i)).operatorsModified(new OperatorEvent(operName, this.getName(), 1));
        }
    }
    
    public void fireOperatorRemoved(final String operName) {
        for (int i = 0; i < this.operatorListeners.size(); ++i) {
            ((OperatorListener) this.operatorListeners.elementAt(i)).operatorsModified(new OperatorEvent(operName, this.getName(), 0));
        }
    }
    
    public void setMinimalCost(final String operation, final Float minCost) {
        this.operationMinimalCosts.put(operation, minCost);
    }
    
    public void setPercentageCost(final String operation, final Float percentageCost) {
        this.operationPercentageCosts.put(operation, percentageCost);
    }
    
    public void setKeepingOrderBook(final boolean possible) {
        this.keepingOrderBook = possible;
    }
    
    public boolean getKeepingOrderBook() {
        return this.keepingOrderBook;
    }
    
    public float getMinimalCost(final String operation) {
        if (this.operationMinimalCosts.containsKey(operation)) {
            return (float) this.operationMinimalCosts.get(operation);
        }
        return 0.0f;
    }
    
    public float getPercentageCost(final String operation) {
        if (this.operationPercentageCosts.containsKey(operation)) {
            return (float) this.operationPercentageCosts.get(operation);
        }
        return 0.0f;
    }
    
    public float getOperationCost(final Operation op) {
        return op.getOperationCost(this.getPercentageCost(op.getOperationName()), this.getMinimalCost(op.getOperationName()));
    }
    
    public abstract boolean isOperationSupported(final Operation p0);
    
    public abstract boolean isOperationValid(final Operation p0);
    
    public abstract void saveToXml(final Element p0);
    
    public abstract void loadFromXml(final Element p0);
    
    public static void saveInstitutionToXml(final Element node, final Institution institutionToSave) {
        Utils.logger.debug("Saving institution " + institutionToSave.getName() + " to xml...");
        node.setAttribute("type", institutionToSave.getInstitutionType()).setAttribute("name", institutionToSave.getName());
        node.setAttribute("quotedAsset", institutionToSave.getAssetName());
        final Element keepOrderBook = new Element("KeepOrderBook");
        keepOrderBook.setAttribute("allow", String.valueOf(institutionToSave.keepingOrderBook));
        node.addContent(keepOrderBook);
        Utils.logger.debug("Saving operations costs...");
        final Element operationsCost = new Element("OperationsCost");
        for (int i = 0; i < institutionToSave.getSupportedOperation().size(); ++i) {
            final Element operCost = new Element("Operation");
            final String operName = (String) institutionToSave.getSupportedOperation().elementAt(i);
            operCost.setAttribute("name", operName);
            operCost.setAttribute("percentageCost", new Float(institutionToSave.getPercentageCost(operName)).toString());
            operCost.setAttribute("minimalCost", new Float(institutionToSave.getMinimalCost(operName)).toString());
            operationsCost.addContent(operCost);
        }
        node.addContent(operationsCost);
        Utils.logger.debug("Saving operators...");
        final Element operators = new Element("Operators");
        Iterator operatorIter = institutionToSave.getOperators().keySet().iterator();
        while(operatorIter.hasNext()) {
          String key = (String) operatorIter.next();
            final Element op = new Element("Operator");
            institutionToSave.getOperator(key).saveToXml(op);
            operators.addContent(op);
        }
        node.addContent(operators);
        Utils.logger.debug("Saving specific parameters...");
        institutionToSave.saveToXml(node);
    }
    
    public static Institution loadInstitutionFromXml(final Element node) {
        Utils.logger.debug("Loading an institution...");
        Utils.logger.debug("Getting institution type...");
        final String institutionType = node.getAttributeValue("type");
        if (institutionType == null) {
            Utils.logger.error("Invalid institution node: the attribute type is not defined.");
            return null;
        }
        Utils.logger.info("Type found: " + institutionType);
        Utils.logger.debug("Getting institution name...");
        final String institutionName = node.getAttributeValue("name");
        if (institutionName == null) {
            Utils.logger.error("invalid institution node: the attribute name is not defined.");
            return null;
        }
        Utils.logger.info("Insitution name: " + institutionName);
        Institution institution;
        try {
            Utils.logger.debug("Creating institution...");
            institution = InstitutionCreator.createInstitution(institutionType);
        }
        catch (final InstitutionNotCreatedException ex) {
            Utils.logger.error("Institution could not be created. " + ex.toString());
            return null;
        }
        institution.setName(institutionName);
        Utils.logger.debug("Loading quotedAsset...");
        final String quotedAsset = node.getAttributeValue("quotedAsset");
        if (quotedAsset == null) {
            Utils.logger.error("invlid xml file: undefined quotedAsset in the institution node.");
            return null;
        }
        institution.quotedAsset = quotedAsset;
        final Element keepOrderBook = node.getChild("KeepOrderBook");
        institution.setKeepingOrderBook(keepOrderBook.getAttributeValue("allow").equals("true"));
        Utils.logger.debug("Loading operations cost...");
        final Element operationCostNode = node.getChild("OperationsCost");
        Iterator costIter = operationCostNode.getChildren("Operation").iterator();
        while(costIter.hasNext()) {
          Element cost = (Element)costIter.next();
            final String operationName = cost.getAttributeValue("name");
            Utils.logger.debug("loading costs");
            final Float percentageCostValue = new Float(cost.getAttributeValue("percentageCost"));
            final Float minimalCostValue = new Float(cost.getAttributeValue("minimalCost"));
            if (operationName == null || minimalCostValue == null) {
                Utils.logger.error("Error reading cost: invalid xml format.");
                return null;
            }
            institution.setPercentageCost(operationName, percentageCostValue);
            institution.setMinimalCost(operationName, minimalCostValue);
            Utils.logger.debug("Cost loaded: operation " + operationName + "Percentage costs" + percentageCostValue + "Minimal costs " + minimalCostValue);
        }
        Utils.logger.debug("Loading operators...");
        final Element operatorsNode = node.getChild("Operators");
        Iterator operIter = operatorsNode.getChildren("Operator").iterator();
        while(operIter.hasNext()) {
          Element oper = (Element)operIter.next();
            final Operator operator = new Operator(oper, institution.getName());
            institution.addOperator(operator);
        }
        Utils.logger.debug("Loading specific parameters...");
        institution.loadFromXml(node);
        return institution;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element rootNode = new Element("Institution");
        saveInstitutionToXml(rootNode, this);
        return rootNode;
    }
    
    public boolean initFromNetworkInput(final Element rootNode) {
        this.loadFromXml(rootNode);
        return true;
    }
}
