package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import jessx.business.event.AssetEvent;
import jessx.business.event.AssetListener;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.PlayerTypeListener;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public class PlayerType implements AssetListener, XmlLoadable, XmlExportable {
	private HashMap operatorList = new HashMap<Object, Object>();

	private Vector institutionsVect = new Vector();

	private Portfolio initialPortfolio;

	private Vector playerTypeListeners = new Vector();

	private String playerTypeName;

	private HashMap dividendInfo = new HashMap<Object, Object>();

	public String getPlayerTypeName() {
		return this.playerTypeName;
	}

	public HashMap getOperatorsPlayed() {
		return this.operatorList;
	}

	public Operator getOperatorPlayed(String operatorCompleteName) {
		return (Operator) this.operatorList.get(operatorCompleteName);
	}

	public Vector getInstitutionsWherePlaying() {
		return this.institutionsVect;
	}

	public Portfolio getPortfolio() {
		return this.initialPortfolio;
	}

	public void setPortfolio(Portfolio portfolio) {
		this.initialPortfolio = portfolio;
	}

	public void addOperatorPlayed(Operator oper) {
		this.operatorList.put(oper.toString(), oper);
		fireOperatorPlayedAdded(oper);
	}

	public void addInstitution(String institution) {
		this.institutionsVect.add(institution);
	}

	public void removeOperatorPlayed(Operator oper) {
		this.operatorList.remove(oper.toString());
		fireOperatorPlayedRemoved(oper);
	}

	public DividendLimitation getDividendInfo(String assetName) {
		return (DividendLimitation) this.dividendInfo.get(assetName);
	}

	public void setDividendInfo(String assetName, DividendLimitation divInfo) {
		this.dividendInfo.put(assetName, divInfo);
	}

	public void addPlayerTypeListener(PlayerTypeListener listener) {
		this.playerTypeListeners.add(listener);
	}

	public void removePlayerTypListener(PlayerTypeListener listener) {
		this.playerTypeListeners.remove(listener);
	}

	public PlayerType(String name) {
		this.playerTypeName = name;
		this.initialPortfolio = new Portfolio(0.0F, new HashMap<Object, Object>());
		Iterator<String> assetIter = BusinessCore.getAssets().keySet().iterator();
		while (assetIter.hasNext()) {
			String key = assetIter.next();
			setDividendInfo(key, new DividendLimitation(getPlayerTypeName(), key));
		}
		BusinessCore.addAssetListener(this);
	}

	public PlayerType(Element xmlPTNode) {
		this("");
		loadFromXml(xmlPTNode);
	}

	public String toString() {
		return getPlayerTypeName();
	}

	public void assetsModified(AssetEvent e) {
		if (e.getEvent() == 1) {
			if (this.dividendInfo.get(e.getAssetName()) == null)
				this.dividendInfo.put(e.getAssetName(), new DividendLimitation(getPlayerTypeName(), e.getAssetName()));
		} else if (e.getEvent() == 0) {
			this.dividendInfo.remove(e.getAssetName());
		}
	}

	public void saveToXml(Element node) {
		Utils.logger.debug("Saving playerType " + getPlayerTypeName() + "...");
		Utils.logger.debug("Saving playerType name...");
		node.setAttribute("name", getPlayerTypeName());
		Vector keys = Utils.convertAndSortMapToVector(getOperatorsPlayed());

		int keysCount = keys.size();
		Utils.logger.debug("Saving operator list...");
		Element opPlayedNode = new Element("OperatorsPlayed");
		for (int i = 0; i < keysCount; i++) {
			Element op = new Element("Operator");
			op.setAttribute("name", keys.get(i).toString());
			opPlayedNode.addContent((Content) op);
			Utils.logger.info("\n" + keys.get(i).toString() + "\n");
		}
		node.addContent((Content) opPlayedNode);
		Utils.logger.debug("Saving initial portfolio...");
		Element portfolioNode = new Element("Portfolio");
		getPortfolio().saveToXml(portfolioNode);
		node.addContent((Content) portfolioNode);
		Utils.logger.debug("Saving dividends Info...");
		Iterator<String> divInfosIter = this.dividendInfo.keySet().iterator();
		while (divInfosIter.hasNext()) {
			String key = divInfosIter.next();
			Utils.logger.debug("Saving dividend info on the asset: " + key);
			Element divInfoNode = new Element("DividendInfo");
			getDividendInfo(key).saveToXml(divInfoNode);
			node.addContent((Content) divInfoNode);
		}
	}

	public void loadFromXml(Element node) {
		Utils.logger.debug("Loading playerType...");
		Utils.logger.debug("Loading name...");
		String ptName = node.getAttributeValue("name");
		if (ptName == null) {
			Utils.logger.error("Invalid xml playertype node: attribute name not found.");
			return;
		}
		this.playerTypeName = ptName;
		Utils.logger.info("playertype name : " + ptName);
		Utils.logger.debug("operator played...");
		Iterator<Element> opPlayedIter = node.getChild("OperatorsPlayed").getChildren("Operator").iterator();
		while (opPlayedIter.hasNext()) {
			Element op = opPlayedIter.next();
			String opCompleteName = op.getAttributeValue("name");
			if (opCompleteName == null) {
				Utils.logger.error("Invalid operatorPlayed xml node : attribue name not found.");
				return;
			}
			Utils.logger.debug("Operator found. Complete name: " + opCompleteName);
			int index = opCompleteName.lastIndexOf(" on ");
			String opName = opCompleteName.substring(0, index);
			String institutionName = opCompleteName.substring(index + 4);
			Utils.logger.debug("Operator name: >-" + opName + "-< institution name: >-" + institutionName + "-<");
			addOperatorPlayed(BusinessCore.getInstitution(institutionName).getOperator(opName));
			addInstitution(institutionName);
		}
		Utils.logger.debug("Loading portfolio...");
		Portfolio portfolio = new Portfolio(0.0F, new HashMap<Object, Object>());
		Utils.logger.debug("\nPlayerType avant " + this.initialPortfolio.getNonInvestedCash() + "\n");
		portfolio.loadFromXml(node.getChild("Portfolio"));
		setPortfolio(portfolio);
		Utils.logger.debug("\nPlayerType apres " + this.initialPortfolio.getNonInvestedCash() + "\n");
		Utils.logger.debug("Loading dividend Infos...");
		Iterator<Element> divInfoNodes = node.getChildren("DividendInfo").iterator();
		while (divInfoNodes.hasNext()) {
			DividendLimitation divInfo = new DividendLimitation(getPlayerTypeName(), divInfoNodes.next());
			setDividendInfo(divInfo.getAssetName(), divInfo);
		}
	}

	private void fireOperatorPlayedAdded(Operator oper) {
		for (int i = 0; i < this.playerTypeListeners.size(); i++)
			((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(this, 2, oper));
	}

	private void fireOperatorPlayedRemoved(Operator oper) {
		for (int i = 0; i < this.playerTypeListeners.size(); i++)
			((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(this, 3, oper));
	}
}
