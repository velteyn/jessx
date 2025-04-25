// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Content;
import java.util.Iterator;
import jessx.utils.Utils;
import org.jdom.Element;
import java.util.Vector;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public class Operator implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable
{
    private Vector grantedOperations;
    private String name;
    private int orderBookVisibility;
    private String institution;
    
    public Vector getGrantedOperations() {
        return this.grantedOperations;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getCompleteName() {
        return String.valueOf(this.getName()) + " on " + this.getInstitution();
    }
    
    public int getOrderBookVisibility() {
        return this.orderBookVisibility;
    }
    
    public String getInstitution() {
        return this.institution;
    }
    
    public Operator(final String institution, final String operatorName, final Vector grantedOps, final int orderBookVisibility) {
        this.name = operatorName;
        this.grantedOperations = grantedOps;
        this.orderBookVisibility = orderBookVisibility;
        this.institution = institution;
    }
    
    public Operator(final Element xmlOper, final String institutionName) {
        this.institution = institutionName;
        this.grantedOperations = new Vector();
        this.loadFromXml(xmlOper);
    }
    
    public boolean isGrantedTo(final String operationName) {
        return this.grantedOperations.contains(operationName);
    }
    
    public void grant(final String operationName) {
        if (!this.grantedOperations.contains(operationName)) {
            this.grantedOperations.add(operationName);
        }
    }
    
    public void deny(final String operationName) {
        if (this.grantedOperations.contains(operationName)) {
            this.grantedOperations.remove(operationName);
        }
    }
    
    public void setOrderbookVisibility(final int depth) {
        this.orderBookVisibility = depth;
    }
    
    public void setName(final String operName) {
        this.name = operName;
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getName()) + " on " + this.getInstitution();
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading operator from xml...");
        Utils.logger.debug("loading name...");
        final String operName = node.getAttributeValue("name");
        if (operName == null) {
            Utils.logger.error("Invalid xml operator node: attribute name not found.");
            System.exit(1);
        }
        Utils.logger.info("Operator name: " + operName);
        this.setName(operName);
        Utils.logger.debug("loading orderbook visibility...");
        final String obVisibility = node.getAttributeValue("orderbookVisibility");
        if (obVisibility == null) {
            Utils.logger.error("Invalid xml operator node: attribute orderbookVisibility not found.");
            System.exit(1);
        }
        this.setOrderbookVisibility(Integer.parseInt(obVisibility));
        Utils.logger.info("the operator can see " + obVisibility + " deep in the orderbook.");
        Utils.logger.debug("Loading operations granted...");
        Iterator grantedOpIter = node.getChildren("GrantedOperation").iterator();
        while(grantedOpIter.hasNext()) {
          Element oper = (Element)grantedOpIter.next();
            final String opName = oper.getAttributeValue("name");
            if (opName == null) {
                Utils.logger.error("Invalid xml GrantedOperation node: attribute name not found.");
                System.exit(1);
            }
            Utils.logger.info("Granting the operation " + opName);
            this.grant(opName);
        }
    }
    
    public void saveToXml(final Element parentNode) {
        Utils.logger.debug("Saving operator " + this.getName() + "to xml...");
        parentNode.setAttribute("name", this.getName());
        parentNode.setAttribute("orderbookVisibility", Integer.toString(this.getOrderBookVisibility()));
        Utils.logger.debug("Saving granted operations...");
        for (int i = 0; i < this.getGrantedOperations().size(); ++i) {
            final Element grantedOp = new Element("GrantedOperation");
            grantedOp.setAttribute("name", this.getGrantedOperations().elementAt(i).toString());
            parentNode.addContent(grantedOp);
        }
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element rootOperator = new Element("Operator");
        this.saveToXml(rootOperator);
        return rootOperator;
    }
    
    public boolean initFromNetworkInput(final Element rootNode) {
        this.loadFromXml(rootNode);
        return true;
    }
}
