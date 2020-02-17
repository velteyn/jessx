package jessx.business;

import java.util.Iterator;
import java.util.Vector;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public class Operator implements XmlExportable, XmlLoadable, NetworkWritable, NetworkReadable {
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
		return String.valueOf(getName()) + " on " + getInstitution();
	}

	public int getOrderBookVisibility() {
		return this.orderBookVisibility;
	}

	public String getInstitution() {
		return this.institution;
	}

	public Operator(String institution, String operatorName, Vector grantedOps, int orderBookVisibility) {
		this.name = operatorName;
		this.grantedOperations = grantedOps;
		this.orderBookVisibility = orderBookVisibility;
		this.institution = institution;
	}

	public Operator(Element xmlOper, String institutionName) {
		this.institution = institutionName;
		this.grantedOperations = new Vector();
		loadFromXml(xmlOper);
	}

	public boolean isGrantedTo(String operationName) {
		return this.grantedOperations.contains(operationName);
	}

	public void grant(String operationName) {
		if (!this.grantedOperations.contains(operationName))
			this.grantedOperations.add(operationName);
	}

	public void deny(String operationName) {
		if (this.grantedOperations.contains(operationName))
			this.grantedOperations.remove(operationName);
	}

	public void setOrderbookVisibility(int depth) {
		this.orderBookVisibility = depth;
	}

	public void setName(String operName) {
		this.name = operName;
	}

	public String toString() {
		return String.valueOf(getName()) + " on " + getInstitution();
	}

	public void loadFromXml(Element node) {
		Utils.logger.debug("Loading operator from xml...");
		Utils.logger.debug("loading name...");
		String operName = node.getAttributeValue("name");
		if (operName == null) {
			Utils.logger.error("Invalid xml operator node: attribute name not found.");
			System.exit(1);
		}
		Utils.logger.info("Operator name: " + operName);
		setName(operName);
		Utils.logger.debug("loading orderbook visibility...");
		String obVisibility = node.getAttributeValue("orderbookVisibility");
		if (obVisibility == null) {
			Utils.logger.error("Invalid xml operator node: attribute orderbookVisibility not found.");
			System.exit(1);
		}
		setOrderbookVisibility(Integer.parseInt(obVisibility));
		Utils.logger.info("the operator can see " + obVisibility + " deep in the orderbook.");
		Utils.logger.debug("Loading operations granted...");
		Iterator<Element> grantedOpIter = node.getChildren("GrantedOperation").iterator();
		while (grantedOpIter.hasNext()) {
			Element oper = grantedOpIter.next();
			String opName = oper.getAttributeValue("name");
			if (opName == null) {
				Utils.logger.error("Invalid xml GrantedOperation node: attribute name not found.");
				System.exit(1);
			}
			Utils.logger.info("Granting the operation " + opName);
			grant(opName);
		}
	}

	public void saveToXml(Element parentNode) {
		Utils.logger.debug("Saving operator " + getName() + "to xml...");
		parentNode.setAttribute("name", getName());
		parentNode.setAttribute("orderbookVisibility", Integer.toString(getOrderBookVisibility()));
		Utils.logger.debug("Saving granted operations...");
		for (int i = 0; i < getGrantedOperations().size(); i++) {
			Element grantedOp = new Element("GrantedOperation");
			grantedOp.setAttribute("name", getGrantedOperations().elementAt(i).toString());
			parentNode.addContent((Content) grantedOp);
		}
	}

	public Element prepareForNetworkOutput(String pt) {
		Element rootOperator = new Element("Operator");
		saveToXml(rootOperator);
		return rootOperator;
	}

	public boolean initFromNetworkInput(Element rootNode) {
		loadFromXml(rootNode);
		return true;
	}
}
