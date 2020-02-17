package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.PlayerTypeListener;
import jessx.business.event.ProgrammedInfoEvent;
import jessx.business.event.ProgrammedInfoListener;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public class Scenario implements XmlExportable, XmlLoadable {
	private HashMap playerTypes = new HashMap<Object, Object>();

	private Vector playerTypeListeners = new Vector();

	private Vector vectorInformation = new Vector();

	private Vector infoListners = new Vector();

	private String password = "";

	private boolean passwordUsed = false;

	private boolean listOfParticipantsUsed = false;

	private Vector listOfParticipants = new Vector();

	public DividendLimitation getDividendInfo(String assetName, String playerType) {
		Utils.logger.debug("Getting dividend info for asset : " + assetName + " and playerType: " + playerType);
		return ((PlayerType) this.playerTypes.get(playerType)).getDividendInfo(assetName);
	}

	public void setDividendInfo(String assetName, String playerType, DividendLimitation value) {
		((PlayerType) this.playerTypes.get(playerType)).setDividendInfo(assetName, value);
	}

	public PlayerType getPlayerType(String name) {
		return (PlayerType) this.playerTypes.get(name);
	}

	public HashMap getPlayerTypes() {
		return this.playerTypes;
	}

	public void addPlayerType(PlayerType playerType) {
		if (playerType != null) {
			this.playerTypes.put(playerType.getPlayerTypeName(), playerType);
			firePlayerTypeAdded(playerType);
		}
	}

	public void removePlayerType(PlayerType playerType) {
		if (playerType != null && this.playerTypes.containsValue(playerType)) {
			this.playerTypes.remove(playerType.getPlayerTypeName());
			firePlayerTypeRemoved(playerType);
		}
	}

	protected void firePlayerTypeAdded(PlayerType playerType) {
		for (int i = 0; i < this.playerTypeListeners.size(); i++)
			((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(playerType, 1, null));
	}

	protected void firePlayerTypeRemoved(PlayerType playerType) {
		for (int i = 0; i < this.playerTypeListeners.size(); i++)
			((PlayerTypeListener) this.playerTypeListeners.elementAt(i)).playerTypeModified(new PlayerTypeEvent(playerType, 0, null));
	}

	public void addPlayerTypeListener(PlayerTypeListener listener) {
		this.playerTypeListeners.add(listener);
	}

	public void removePlayerTypeListener(PlayerTypeListener listener) {
		this.playerTypeListeners.remove(listener);
	}

	public Vector getListInformation() {
		return this.vectorInformation;
	}

	public void addInformation(String[] info) {
		this.vectorInformation.add(info);
		fireInfoAdded(info);
		Utils.logger.debug("add " + info);
	}

	public void removeInformation(int i) {
		this.vectorInformation.remove(i);
		fireInfoRemoved(i);
		Utils.logger.debug("remove info number " + i);
	}

	public void removeAllInformation() {
		this.vectorInformation.clear();
		fireInfoClear();
		Utils.logger.debug("remove all info");
	}

	public void changeInformation(int i, String[] message) {
		this.vectorInformation.set(i, message);
		Utils.logger.debug("change " + i);
	}

	protected void fireInfoAdded(String[] info) {
		for (int i = 0; i < this.infoListners.size(); i++) {
			((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(info, 2));
			Utils.logger.debug("fireadd" + info);
		}
	}

	protected void fireInfoRemoved(int num) {
		for (int i = 0; i < this.infoListners.size(); i++) {
			((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(new Integer(num), 0));
			Utils.logger.debug("fireremov" + num);
		}
	}

	protected void fireInfoClear() {
		for (int i = 0; i < this.infoListners.size(); i++)
			((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(new Integer(-1), 1));
		Utils.logger.debug("fireremovall-1");
	}

	protected void fireProgammedInfoLoad() {
		for (int i = 0; i < this.infoListners.size(); i++)
			((ProgrammedInfoListener) this.infoListners.elementAt(i)).programmedInfoModified(new ProgrammedInfoEvent(this.vectorInformation, 3));
	}

	public void addProgrammedInfoListener(ProgrammedInfoListener listener) {
		this.infoListners.add(listener);
	}

	public void removedInfoListener(ProgrammedInfoListener listener) {
		this.infoListners.remove(listener);
	}

	public void saveToXml(Element node) {
		Utils.logger.debug("Saving scenario...");
		Vector<String> keys = Utils.convertAndSortMapToVector(getPlayerTypes());
		int keysCount = keys.size();
		for (int i = 0; i < keysCount; i++) {
			Element ptNode = new Element("PlayerType");
			getPlayerType(keys.get(i)).saveToXml(ptNode);
			node.addContent((Content) ptNode);
		}
		int size = this.vectorInformation.size();
		Element informationNode = new Element("Information");
		for (int j = 0; j < size; j++) {
			Element infoNode = new Element("Information");
			infoNode.setAttribute("Content", ((String[]) this.vectorInformation.get(j))[0]);
			infoNode.setAttribute("Category", ((String[]) this.vectorInformation.get(j))[1]);
			infoNode.setAttribute("Period", ((String[]) this.vectorInformation.get(j))[2]);
			infoNode.setAttribute("Time", ((String[]) this.vectorInformation.get(j))[3]);
			informationNode.addContent((Content) infoNode);
		}
		node.addContent((Content) informationNode);
	}

	public void loadFromXml(Element node) {
		Utils.logger.debug("Loading scenario...");
		Iterator<Element> ptIter = node.getChildren("PlayerType").iterator();
		while (ptIter.hasNext()) {
			Element ptElem = ptIter.next();
			PlayerType pt = new PlayerType(ptElem);
			addPlayerType(pt);
		}
		Iterator<Element> infoIter = node.getChild("Information").getChildren("Information").iterator();
		Utils.logger.debug(infoIter);
		if (this.vectorInformation.size() > 0)
			removeAllInformation();
		while (infoIter.hasNext()) {
			Element infoElem = infoIter.next();
			Utils.logger.debug("crea infoElem =" + infoElem);
			String infoContent = new String(infoElem.getAttributeValue("Content"));
			Utils.logger.debug("------" + infoContent);
			String infoReceivers = new String(infoElem.getAttributeValue("Category"));
			String infoPeriode = new String(infoElem.getAttributeValue("Period"));
			String infoTime = new String(infoElem.getAttributeValue("Time"));
			this.vectorInformation.add(new String[] { infoContent, infoReceivers, infoPeriode, infoTime });
			Utils.logger.debug(this.vectorInformation);
		}
		fireProgammedInfoLoad();
	}

	public void setPasswordUsed(boolean b) {
		this.passwordUsed = b;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return this.password;
	}

	public boolean isPasswordUsed() {
		return this.passwordUsed;
	}

	public void setlistOfParticipantsUsed(boolean b) {
		this.listOfParticipantsUsed = b;
	}

	public void setlistOfParticipants(Vector listOfParticipants) {
		this.listOfParticipants = listOfParticipants;
	}

	public Vector getlistOfParticipants() {
		return this.listOfParticipants;
	}

	public boolean islistOfParticipantsUsed() {
		return this.listOfParticipantsUsed;
	}
}
