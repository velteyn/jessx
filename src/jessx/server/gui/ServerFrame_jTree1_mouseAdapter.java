// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.utils.Utils;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseEvent;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import javax.swing.JPanel;
import javax.swing.JTree;
import java.awt.event.MouseAdapter;

class ServerFrame_jTree1_mouseAdapter extends MouseAdapter
{
    private JTree tree;
    public JPanel displayPanel;
    
    public ServerFrame_jTree1_mouseAdapter(final JTree tree, final JPanel displayPanel) {
        this.tree = tree;
        (this.displayPanel = displayPanel).setLayout(new GridBagLayout());
    }
    
    void resetDisplay() {
        final ExperimentSetupTree experimentSetupTree = (ExperimentSetupTree)this.tree.getModel();
        this.displayNode((DisplayableNode)ExperimentSetupTree.getExperimentRootNode().getUserObject());
    }
    
    void displayNode(final DisplayableNode node) {
        this.displayPanel.removeAll();
        this.displayPanel.add(node.getPanel(), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.displayPanel.revalidate();
        this.displayPanel.repaint();
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node.getUserObject() instanceof DisplayableNode) {
            this.displayNode((DisplayableNode)node.getUserObject());
        }
        else {
            Utils.logger.warn("Tree node is not a DisplayableNode. Don't know what to do with him...");
        }
    }
    
    public void refreshDisplay() {
        final DefaultMutableTreeNode node = (DefaultMutableTreeNode)this.tree.getLastSelectedPathComponent();
        if (node == null) {
            return;
        }
        if (node.getUserObject() instanceof DisplayableNode) {
            this.displayNode((DisplayableNode)node.getUserObject());
        }
        else {
            Utils.logger.warn("Tree node is not a DisplayableNode. Don't know what to do with him...");
        }
    }
}
