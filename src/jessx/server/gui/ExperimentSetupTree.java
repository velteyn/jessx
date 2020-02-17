package jessx.server.gui;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import jessx.business.Asset;
import jessx.business.BusinessCore;
import jessx.business.Institution;
import jessx.business.PlayerType;
import jessx.business.event.AssetEvent;
import jessx.business.event.AssetListener;
import jessx.business.event.InstitutionEvent;
import jessx.business.event.InstitutionListener;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.PlayerTypeListener;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;

public class ExperimentSetupTree extends DefaultTreeModel implements AssetListener, InstitutionListener, PlayerTypeListener {
  private static DefaultMutableTreeNode experimentRootNode;
  
  private static DefaultMutableTreeNode assetsNode;
  
  private static DefaultMutableTreeNode institutionsNode;
  
  private static DefaultMutableTreeNode scenarioNode;
  
  private static DefaultMutableTreeNode currentPlayersNode;
  
  private static DefaultMutableTreeNode playersType;
  
  private static DefaultMutableTreeNode dividends;
  
  private static DefaultMutableTreeNode messages;
  
  private static DefaultMutableTreeNode robotsNode;
  
  private static DefaultMutableTreeNode ZIT1;
  
  private static DefaultMutableTreeNode ZIT2;
  
  private static DefaultMutableTreeNode ZIT3;
  
  RobotsNode robotNode = new RobotsNode(this);
  
  ScenarioServerGenericGui scenarioServerGenericGui = new ScenarioServerGenericGui(this);
  
  InstitutionServerGenericGui institutionServerGenericGui = new InstitutionServerGenericGui();
  
  PlayersTypeServerGenericGui playersTypeServerGenericGui = new PlayersTypeServerGenericGui(this);
  
  DividendsServerGenericGui dividendsServerGenericGui = new DividendsServerGenericGui();
  
  MessagesServerGenericGui messagesServerGenericGui = new MessagesServerGenericGui();
  
  ConnectionsPlayersServerGenericGui currentPlayersServerGenericGui = new ConnectionsPlayersServerGenericGui();
  
  ZitNotDiscreetServerGui zitNotDiscreetServeurGui = new ZitNotDiscreetServerGui();
  
  ZitDiscreetServerGui zitDiscreetServerGui = new ZitDiscreetServerGui();
  
  ZitDiscreetITServerGui zitDiscreetITServerGui = new ZitDiscreetITServerGui();
  
  private HashMap hashMapDividendSetup = new HashMap<Object, Object>();
  
  private HashMap hashMapplayerTypeSetup = new HashMap<Object, Object>();
  
  public static DefaultMutableTreeNode getScenarioNode() {
    return scenarioNode;
  }
  
  public static DefaultMutableTreeNode getExperimentRootNode() {
    return experimentRootNode;
  }
  
  public ExperimentSetupTree(DefaultMutableTreeNode root) {
    super(root);
    experimentRootNode = root;
    experimentRootNode.setUserObject(BusinessCore.getGeneralParameters());
    assetsNode = new DefaultMutableTreeNode(Asset.getAssetServerGenericGui());
    experimentRootNode.add(assetsNode);
    institutionsNode = new DefaultMutableTreeNode(this.institutionServerGenericGui);
    experimentRootNode.add(institutionsNode);
    scenarioNode = new DefaultMutableTreeNode(this.scenarioServerGenericGui);
    playersType = new DefaultMutableTreeNode(this.playersTypeServerGenericGui);
    robotsNode = new DefaultMutableTreeNode(this.robotNode);
    dividends = new DefaultMutableTreeNode(this.dividendsServerGenericGui);
    messages = new DefaultMutableTreeNode(this.messagesServerGenericGui);
    ZIT1 = new DefaultMutableTreeNode(this.zitNotDiscreetServeurGui);
    ZIT2 = new DefaultMutableTreeNode(this.zitDiscreetServerGui);
    ZIT3 = new DefaultMutableTreeNode(this.zitDiscreetITServerGui);
    robotsNode.add(ZIT1);
    robotsNode.add(ZIT2);
    robotsNode.add(ZIT3);
    scenarioNode.add(playersType);
    scenarioNode.add(dividends);
    scenarioNode.add(messages);
    scenarioNode.add(robotsNode);
    experimentRootNode.add(scenarioNode);
    currentPlayersNode = new DefaultMutableTreeNode(
        this.currentPlayersServerGenericGui);
    experimentRootNode.add(currentPlayersNode);
    BusinessCore.addInstitutionListener(this);
    BusinessCore.addAssetListener(this);
    BusinessCore.getScenario().addPlayerTypeListener(this);
  }
  
  public void setAllUneditable() {
    ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters())
      .setUneditable();
    ((AssetServerGenericGui)Asset.getAssetServerGenericGui()).setUneditable();
    this.institutionServerGenericGui.setUneditable();
    this.scenarioServerGenericGui.setUneditable();
    this.currentPlayersServerGenericGui.setUneditable();
    this.playersTypeServerGenericGui.setUneditable();
    this.dividendsServerGenericGui.setUneditable();
    this.messagesServerGenericGui.setUneditable();
    this.zitNotDiscreetServeurGui.setUneditable();
    this.zitDiscreetServerGui.setUneditable();
    this.zitDiscreetITServerGui.setUneditable();
    HashMap institutions = BusinessCore.getInstitutions();
    Iterator<String> iterator1 = institutions.keySet().iterator();
    while (iterator1.hasNext()) {
      Institution institution = BusinessCore.getInstitution(
          iterator1.next());
      institution.desactivePanel();
    } 
    Iterator<String> iterator2 = this.hashMapDividendSetup.keySet().iterator();
    while (iterator2.hasNext()) {
      DividendSetupSlidingWindow dividendSetupSlidingWindow = 
        (DividendSetupSlidingWindow)this.hashMapDividendSetup.get(
          iterator2.next());
      dividendSetupSlidingWindow.setUneditable();
    } 
    Iterator<String> iterator3 = this.hashMapplayerTypeSetup.keySet().iterator();
    while (iterator3.hasNext()) {
      PlayerTypeSetupGui playerTypeSetupGui = 
        (PlayerTypeSetupGui)this.hashMapplayerTypeSetup.get(
          iterator3.next());
      playerTypeSetupGui.setUneditable();
    } 
  }
  
  public void setAllEditable() {
    ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters())
      .setEditable();
    ((AssetServerGenericGui)Asset.getAssetServerGenericGui()).setEditable();
    this.institutionServerGenericGui.setEditable();
    this.scenarioServerGenericGui.setEditable();
    this.currentPlayersServerGenericGui.setEditable();
    this.playersTypeServerGenericGui.setEditable();
    this.dividendsServerGenericGui.setEditable();
    this.messagesServerGenericGui.setEditable();
    this.zitNotDiscreetServeurGui.setEditable();
    this.zitDiscreetServerGui.setEditable();
    this.zitDiscreetITServerGui.setEditable();
    HashMap institutions = BusinessCore.getInstitutions();
    Iterator<String> iterator = institutions.keySet().iterator();
    while (iterator.hasNext()) {
      Institution institution = BusinessCore.getInstitution(
          iterator.next());
      institution.activePanel();
    } 
    Iterator<String> iterator2 = this.hashMapDividendSetup.keySet().iterator();
    while (iterator2.hasNext()) {
      DividendSetupSlidingWindow dividendSetupSlidingWindow = 
        (DividendSetupSlidingWindow)this.hashMapDividendSetup.get(
          iterator2.next());
      dividendSetupSlidingWindow.setEditable();
    } 
    Iterator<String> iterator3 = this.hashMapplayerTypeSetup.keySet().iterator();
    while (iterator3.hasNext()) {
      PlayerTypeSetupGui playerTypeSetupGui = 
        (PlayerTypeSetupGui)this.hashMapplayerTypeSetup.get(
          iterator3.next());
      playerTypeSetupGui.setEditable();
    } 
  }
  
  public void saveRobotConfiguration(Element node) {
    Element robots = new Element("Robots");
    node.addContent((Content)robots);
    this.zitNotDiscreetServeurGui.saveToXml(robots);
    this.zitDiscreetServerGui.saveToXml(robots);
    this.zitDiscreetITServerGui.saveToXml(robots);
  }
  
  public void loadRobotConfigurationFromXml(Element root) {
    Element robots = root.getChild("Robots");
    if (robots == null) {
      Utils.logger.error("this version of XML was made before jessX-version 1.5 (no robots)");
      return;
    } 
    this.zitNotDiscreetServeurGui.loadFromXml(robots);
    this.zitDiscreetServerGui.loadFromXml(robots);
    this.zitDiscreetITServerGui.loadFromXml(robots);
  }
  
  public void initializeRobotPanel() {
    this.zitNotDiscreetServeurGui.initializePanel();
    this.zitDiscreetServerGui.initializePanel();
    this.zitDiscreetITServerGui.initializePanel();
  }
  
  public void assetsModified(AssetEvent e) {
    if (e.getEvent() == 1) {
      addAssetToTree(BusinessCore.getAsset(e.getAssetName()));
      addDividendNodeToTree(BusinessCore.getAsset(e.getAssetName()));
    } else if (e.getEvent() == 0) {
      removeAssetFromTree(e.getAssetName());
      removeDividendNodeFromTree(e.getAssetName());
    } 
  }
  
  public void institutionsModified(InstitutionEvent e) {
    if (e.getEvent() == 1) {
      addInstitutionToTree(BusinessCore.getInstitution(e
            .getInstitutionName()));
    } else if (e.getEvent() == 0) {
      removeInstitutionFromTree(e.getInstitutionName());
    } 
  }
  
  public void playerTypeModified(PlayerTypeEvent e) {
    if (e.getEvent() == 1) {
      addPlayerTypeToTree(e.getPlayerType());
    } else if (e.getEvent() == 0) {
      removePlayerTypeFromTree(e.getPlayerType().toString());
    } 
  }
  
  private void addDividendNodeToTree(Asset asset) {
    DividendSetupSlidingWindow dividendSetupSlidingWindow = 
      new DividendSetupSlidingWindow(asset.getAssetName(), asset.getDividendModel());
    this.hashMapDividendSetup.put(asset.getAssetName(), dividendSetupSlidingWindow);
    dividends.add(new DefaultMutableTreeNode(dividendSetupSlidingWindow));
    nodesWereInserted(dividends, 
        new int[] { dividends.getChildCount() - 1 });
  }
  
  private void removeDividendNodeFromTree(String assetName) {
    this.hashMapDividendSetup.remove(assetName);
    Enumeration<DefaultMutableTreeNode> iter = dividends.children();
    while (iter.hasMoreElements()) {
      DefaultMutableTreeNode child = iter.nextElement();
      if (child.getUserObject().toString().equalsIgnoreCase(assetName)) {
        int index = dividends.getIndex(child);
        dividends.remove(index);
        nodesWereRemoved(dividends, new int[] { index }, new Object[] { child });
      } 
    } 
  }
  
  private void addAssetToTree(Asset asset) {
    assetsNode.add(new DefaultMutableTreeNode(new AssetNode(asset)));
    nodesWereInserted(assetsNode, 
        new int[] { assetsNode.getChildCount() - 1 });
  }
  
  private void removeAssetFromTree(String assetName) {
    Enumeration<DefaultMutableTreeNode> iter = assetsNode.children();
    while (iter.hasMoreElements()) {
      DefaultMutableTreeNode child = iter.nextElement();
      if (child.getUserObject().toString().equalsIgnoreCase(assetName)) {
        int index = assetsNode.getIndex(child);
        assetsNode.remove(index);
        nodesWereRemoved(assetsNode, new int[] { index }, new Object[] { child });
      } 
    } 
  }
  
  private void addInstitutionToTree(Institution institution) {
    institutionsNode.add(new DefaultMutableTreeNode(new InstitutionNode(
            institution)));
    nodesWereInserted(institutionsNode, 
        new int[] { institutionsNode.getChildCount() - 1 });
  }
  
  private void removeInstitutionFromTree(String institutionName) {
    Enumeration<DefaultMutableTreeNode> iter = institutionsNode.children();
    while (iter.hasMoreElements()) {
      DefaultMutableTreeNode child = iter.nextElement();
      if (child.getUserObject().toString().equalsIgnoreCase(institutionName)) {
        int index = institutionsNode.getIndex(child);
        institutionsNode.remove(index);
        nodesWereRemoved(institutionsNode, new int[] { index }, new Object[] { child });
      } 
    } 
  }
  
  private void addPlayerTypeToTree(PlayerType pt) {
    PlayerTypeSetupGui playerTypeSetupGui = new PlayerTypeSetupGui(pt);
    this.hashMapplayerTypeSetup.put(playerTypeSetupGui.toString(), playerTypeSetupGui);
    playersType.add(new DefaultMutableTreeNode(playerTypeSetupGui));
    nodesWereInserted(playersType, 
        new int[] { playersType.getChildCount() - 1 });
  }
  
  private void removePlayerTypeFromTree(String playerType) {
    this.hashMapplayerTypeSetup.remove(playerType);
    Utils.logger.debug("Removing a player type : \"" + playerType + 
        "\" from tree...");
    Enumeration<DefaultMutableTreeNode> iter = playersType.children();
    while (iter.hasMoreElements()) {
      DefaultMutableTreeNode child = iter.nextElement();
      Utils.logger.debug("Scanning player type node named " + 
          child.getUserObject().toString());
      if (child.getUserObject().toString().equalsIgnoreCase(playerType)) {
        int index = playersType.getIndex(child);
        playersType.remove(index);
        nodesWereRemoved(playersType, new int[] { index }, new Object[] { child });
      } 
    } 
  }
}
