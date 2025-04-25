// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.business.PlayerType;
import java.util.Enumeration;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.InstitutionEvent;
import jessx.business.event.AssetEvent;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;
import jessx.business.Institution;
import java.util.Iterator;
import javax.swing.tree.MutableTreeNode;
import jessx.business.Asset;
import jessx.business.BusinessCore;
import javax.swing.tree.TreeNode;
import java.util.HashMap;
import javax.swing.tree.DefaultMutableTreeNode;
import jessx.business.event.PlayerTypeListener;
import jessx.business.event.InstitutionListener;
import jessx.business.event.AssetListener;
import javax.swing.tree.DefaultTreeModel;

public class ExperimentSetupTree extends DefaultTreeModel implements AssetListener, InstitutionListener, PlayerTypeListener
{
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
    RobotsNode robotNode;
    ScenarioServerGenericGui scenarioServerGenericGui;
    InstitutionServerGenericGui institutionServerGenericGui;
    PlayersTypeServerGenericGui playersTypeServerGenericGui;
    DividendsServerGenericGui dividendsServerGenericGui;
    MessagesServerGenericGui messagesServerGenericGui;
    ConnectionsPlayersServerGenericGui currentPlayersServerGenericGui;
    ZitNotDiscreetServerGui zitNotDiscreetServeurGui;
    ZitDiscreetServerGui zitDiscreetServerGui;
    ZitDiscreetITServerGui zitDiscreetITServerGui;
    private HashMap hashMapDividendSetup;
    private HashMap hashMapplayerTypeSetup;
    
    public static DefaultMutableTreeNode getScenarioNode() {
        return ExperimentSetupTree.scenarioNode;
    }
    
    public static DefaultMutableTreeNode getExperimentRootNode() {
        return ExperimentSetupTree.experimentRootNode;
    }
    
    public ExperimentSetupTree(final DefaultMutableTreeNode root) {
        super(root);
        this.robotNode = new RobotsNode(this);
        this.scenarioServerGenericGui = new ScenarioServerGenericGui(this);
        this.institutionServerGenericGui = new InstitutionServerGenericGui();
        this.playersTypeServerGenericGui = new PlayersTypeServerGenericGui(this);
        this.dividendsServerGenericGui = new DividendsServerGenericGui();
        this.messagesServerGenericGui = new MessagesServerGenericGui();
        this.currentPlayersServerGenericGui = new ConnectionsPlayersServerGenericGui();
        this.zitNotDiscreetServeurGui = new ZitNotDiscreetServerGui();
        this.zitDiscreetServerGui = new ZitDiscreetServerGui();
        this.zitDiscreetITServerGui = new ZitDiscreetITServerGui();
        this.hashMapDividendSetup = new HashMap();
        this.hashMapplayerTypeSetup = new HashMap();
        (ExperimentSetupTree.experimentRootNode = root).setUserObject(BusinessCore.getGeneralParameters());
        ExperimentSetupTree.assetsNode = new DefaultMutableTreeNode(Asset.getAssetServerGenericGui());
        ExperimentSetupTree.experimentRootNode.add(ExperimentSetupTree.assetsNode);
        ExperimentSetupTree.institutionsNode = new DefaultMutableTreeNode(this.institutionServerGenericGui);
        ExperimentSetupTree.experimentRootNode.add(ExperimentSetupTree.institutionsNode);
        ExperimentSetupTree.scenarioNode = new DefaultMutableTreeNode(this.scenarioServerGenericGui);
        ExperimentSetupTree.playersType = new DefaultMutableTreeNode(this.playersTypeServerGenericGui);
        ExperimentSetupTree.robotsNode = new DefaultMutableTreeNode(this.robotNode);
        ExperimentSetupTree.dividends = new DefaultMutableTreeNode(this.dividendsServerGenericGui);
        ExperimentSetupTree.messages = new DefaultMutableTreeNode(this.messagesServerGenericGui);
        ExperimentSetupTree.ZIT1 = new DefaultMutableTreeNode(this.zitNotDiscreetServeurGui);
        ExperimentSetupTree.ZIT2 = new DefaultMutableTreeNode(this.zitDiscreetServerGui);
        ExperimentSetupTree.ZIT3 = new DefaultMutableTreeNode(this.zitDiscreetITServerGui);
        ExperimentSetupTree.robotsNode.add(ExperimentSetupTree.ZIT1);
        ExperimentSetupTree.robotsNode.add(ExperimentSetupTree.ZIT2);
        ExperimentSetupTree.robotsNode.add(ExperimentSetupTree.ZIT3);
        ExperimentSetupTree.scenarioNode.add(ExperimentSetupTree.playersType);
        ExperimentSetupTree.scenarioNode.add(ExperimentSetupTree.dividends);
        ExperimentSetupTree.scenarioNode.add(ExperimentSetupTree.messages);
        ExperimentSetupTree.scenarioNode.add(ExperimentSetupTree.robotsNode);
        ExperimentSetupTree.experimentRootNode.add(ExperimentSetupTree.scenarioNode);
        ExperimentSetupTree.currentPlayersNode = new DefaultMutableTreeNode(this.currentPlayersServerGenericGui);
        ExperimentSetupTree.experimentRootNode.add(ExperimentSetupTree.currentPlayersNode);
        BusinessCore.addInstitutionListener(this);
        BusinessCore.addAssetListener(this);
        BusinessCore.getScenario().addPlayerTypeListener(this);
    }
    
    public void setAllUneditable() {
        ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters()).setUneditable();
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
        final HashMap institutions = BusinessCore.getInstitutions();
        final Iterator iterator1 = institutions.keySet().iterator();
        while (iterator1.hasNext()) {
            final Institution institution = BusinessCore.getInstitution((String) iterator1.next());
            institution.desactivePanel();
        }
        final Iterator iterator2 = this.hashMapDividendSetup.keySet().iterator();
        while (iterator2.hasNext()) {
            final DividendSetupSlidingWindow dividendSetupSlidingWindow = (DividendSetupSlidingWindow) this.hashMapDividendSetup.get(iterator2.next());
            dividendSetupSlidingWindow.setUneditable();
        }
        final Iterator iterator3 = this.hashMapplayerTypeSetup.keySet().iterator();
        while (iterator3.hasNext()) {
            final PlayerTypeSetupGui playerTypeSetupGui = (PlayerTypeSetupGui) this.hashMapplayerTypeSetup.get(iterator3.next());
            playerTypeSetupGui.setUneditable();
        }
    }
    
    public void setAllEditable() {
        ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters()).setEditable();
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
        final HashMap institutions = BusinessCore.getInstitutions();
        final Iterator iterator = institutions.keySet().iterator();
        while (iterator.hasNext()) {
            final Institution institution = BusinessCore.getInstitution((String) iterator.next());
            institution.activePanel();
        }
        final Iterator iterator2 = this.hashMapDividendSetup.keySet().iterator();
        while (iterator2.hasNext()) {
            final DividendSetupSlidingWindow dividendSetupSlidingWindow = (DividendSetupSlidingWindow) this.hashMapDividendSetup.get(iterator2.next());
            dividendSetupSlidingWindow.setEditable();
        }
        final Iterator iterator3 = this.hashMapplayerTypeSetup.keySet().iterator();
        while (iterator3.hasNext()) {
            final PlayerTypeSetupGui playerTypeSetupGui = (PlayerTypeSetupGui) this.hashMapplayerTypeSetup.get(iterator3.next());
            playerTypeSetupGui.setEditable();
        }
    }
    
    public void saveRobotConfiguration(final Element node) {
        final Element robots = new Element("Robots");
        node.addContent(robots);
        this.zitNotDiscreetServeurGui.saveToXml(robots);
        this.zitDiscreetServerGui.saveToXml(robots);
        this.zitDiscreetITServerGui.saveToXml(robots);
    }
    
    public void loadRobotConfigurationFromXml(final Element root) {
        final Element robots = root.getChild("Robots");
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
    
    public void assetsModified(final AssetEvent e) {
        if (e.getEvent() == 1) {
            this.addAssetToTree(BusinessCore.getAsset(e.getAssetName()));
            this.addDividendNodeToTree(BusinessCore.getAsset(e.getAssetName()));
        }
        else if (e.getEvent() == 0) {
            this.removeAssetFromTree(e.getAssetName());
            this.removeDividendNodeFromTree(e.getAssetName());
        }
    }
    
    public void institutionsModified(final InstitutionEvent e) {
        if (e.getEvent() == 1) {
            this.addInstitutionToTree(BusinessCore.getInstitution(e.getInstitutionName()));
        }
        else if (e.getEvent() == 0) {
            this.removeInstitutionFromTree(e.getInstitutionName());
        }
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (e.getEvent() == 1) {
            this.addPlayerTypeToTree(e.getPlayerType());
        }
        else if (e.getEvent() == 0) {
            this.removePlayerTypeFromTree(e.getPlayerType().toString());
        }
    }
    
    private void addDividendNodeToTree(final Asset asset) {
        final DividendSetupSlidingWindow dividendSetupSlidingWindow = new DividendSetupSlidingWindow(asset.getAssetName(), asset.getDividendModel());
        this.hashMapDividendSetup.put(asset.getAssetName(), dividendSetupSlidingWindow);
        ExperimentSetupTree.dividends.add(new DefaultMutableTreeNode(dividendSetupSlidingWindow));
        this.nodesWereInserted(ExperimentSetupTree.dividends, new int[] { ExperimentSetupTree.dividends.getChildCount() - 1 });
    }
    
    private void removeDividendNodeFromTree(final String assetName) {
        this.hashMapDividendSetup.remove(assetName);
        final Enumeration iter = ExperimentSetupTree.dividends.children();
        while (iter.hasMoreElements()) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) iter.nextElement();
            if (child.getUserObject().toString().equalsIgnoreCase(assetName)) {
                final int index = ExperimentSetupTree.dividends.getIndex(child);
                ExperimentSetupTree.dividends.remove(index);
                this.nodesWereRemoved(ExperimentSetupTree.dividends, new int[] { index }, new Object[] { child });
            }
        }
    }
    
    private void addAssetToTree(final Asset asset) {
        ExperimentSetupTree.assetsNode.add(new DefaultMutableTreeNode(new AssetNode(asset)));
        this.nodesWereInserted(ExperimentSetupTree.assetsNode, new int[] { ExperimentSetupTree.assetsNode.getChildCount() - 1 });
    }
    
    private void removeAssetFromTree(final String assetName) {
        final Enumeration iter = ExperimentSetupTree.assetsNode.children();
        while (iter.hasMoreElements()) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) iter.nextElement();
            if (child.getUserObject().toString().equalsIgnoreCase(assetName)) {
                final int index = ExperimentSetupTree.assetsNode.getIndex(child);
                ExperimentSetupTree.assetsNode.remove(index);
                this.nodesWereRemoved(ExperimentSetupTree.assetsNode, new int[] { index }, new Object[] { child });
            }
        }
    }
    
    private void addInstitutionToTree(final Institution institution) {
        ExperimentSetupTree.institutionsNode.add(new DefaultMutableTreeNode(new InstitutionNode(institution)));
        this.nodesWereInserted(ExperimentSetupTree.institutionsNode, new int[] { ExperimentSetupTree.institutionsNode.getChildCount() - 1 });
    }
    
    private void removeInstitutionFromTree(final String institutionName) {
        final Enumeration iter = ExperimentSetupTree.institutionsNode.children();
        while (iter.hasMoreElements()) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) iter.nextElement();
            if (child.getUserObject().toString().equalsIgnoreCase(institutionName)) {
                final int index = ExperimentSetupTree.institutionsNode.getIndex(child);
                ExperimentSetupTree.institutionsNode.remove(index);
                this.nodesWereRemoved(ExperimentSetupTree.institutionsNode, new int[] { index }, new Object[] { child });
            }
        }
    }
    
    private void addPlayerTypeToTree(final PlayerType pt) {
        final PlayerTypeSetupGui playerTypeSetupGui = new PlayerTypeSetupGui(pt);
        this.hashMapplayerTypeSetup.put(playerTypeSetupGui.toString(), playerTypeSetupGui);
        ExperimentSetupTree.playersType.add(new DefaultMutableTreeNode(playerTypeSetupGui));
        this.nodesWereInserted(ExperimentSetupTree.playersType, new int[] { ExperimentSetupTree.playersType.getChildCount() - 1 });
    }
    
    private void removePlayerTypeFromTree(final String playerType) {
        this.hashMapplayerTypeSetup.remove(playerType);
        Utils.logger.debug("Removing a player type : \"" + playerType + "\" from tree...");
        final Enumeration iter = ExperimentSetupTree.playersType.children();
        while (iter.hasMoreElements()) {
            final DefaultMutableTreeNode child = (DefaultMutableTreeNode) iter.nextElement();
            Utils.logger.debug("Scanning player type node named " + child.getUserObject().toString());
            if (child.getUserObject().toString().equalsIgnoreCase(playerType)) {
                final int index = ExperimentSetupTree.playersType.getIndex(child);
                ExperimentSetupTree.playersType.remove(index);
                this.nodesWereRemoved(ExperimentSetupTree.playersType, new int[] { index }, new Object[] { child });
            }
        }
    }
}
