// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.util.Iterator;
import jessx.business.AssetNotCreatedException;
import jessx.business.AssetCreator;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import jessx.business.Asset;
import jessx.business.event.AssetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jessx.utils.PopupListener;
import javax.swing.JMenuItem;
import javax.swing.table.TableModel;
import jessx.utils.JessXTableModel;
import java.awt.LayoutManager;
import jessx.utils.Utils;
import jessx.business.BusinessCore;
import javax.swing.JComboBox;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import jessx.business.event.AssetListener;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class AssetServerGenericGui extends JPanel implements DisplayableNode, ActionListener, AssetListener
{
    GridBagLayout gridBagLayout1;
    JScrollPane jScrollPane2;
    JTable jTable1;
    JPopupMenu popup;
    JComboBox jComboBox1;
    private static boolean editable;
    
    static {
        AssetServerGenericGui.editable = true;
    }
    
    public AssetServerGenericGui() {
        this.gridBagLayout1 = new GridBagLayout();
        this.jScrollPane2 = new JScrollPane();
        this.jComboBox1 = new JComboBox();
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        BusinessCore.addAssetListener(this);
    }
    
    public void setEditable() {
        AssetServerGenericGui.editable = true;
        this.activeButtonsRemoveIfNecessary();
    }
    
    public void setUneditable() {
        AssetServerGenericGui.editable = false;
        this.activeButtonsRemoveIfNecessary();
    }
    
    @Override
    public String toString() {
        return "Assets";
    }
    
    public JPanel getPanel() {
        this.activeButtonsRemoveIfNecessary();
        return this;
    }
    
    private void jbInit() throws Exception {
        Utils.logger.debug("AssetServerGenericGui is initializing;");
        this.setLayout(this.gridBagLayout1);
        this.jTable1 = new JTable(new JessXTableModel(new String[] { "Asset name", "Asset type" }, 0));
        this.popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add an asset");
        menuItem.setActionCommand("addAsset");
        menuItem.addActionListener(this);
        this.popup.add(menuItem);
        menuItem = new JMenuItem("Remove an asset");
        menuItem.setActionCommand("deleteAsset");
        menuItem.addActionListener(this);
        this.popup.add(menuItem);
        menuItem = new JMenuItem("Remove all assets");
        menuItem.setActionCommand("deleteAllAssets");
        menuItem.addActionListener(this);
        this.popup.add(menuItem);
        this.activeButtonsRemoveIfNecessary();
        final MouseListener popupListener = new PopupListener(this.popup);
        this.jScrollPane2.addMouseListener(popupListener);
        this.jTable1.addMouseListener(popupListener);
        this.jScrollPane2.setToolTipText("Right-click here to add or remove assets.");
        this.jTable1.setToolTipText("Right-click here to add or remove assets.");
        this.jTable1.setRowSelectionAllowed(false);
        this.add(this.jScrollPane2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(3, 6, 6, 6), 0, 0));
        this.jScrollPane2.getViewport().add(this.jTable1, null);
        this.initAssetTypeComboBox();
    }
    
    public void actionPerformed(final ActionEvent e) {
        Utils.logger.debug("AssetServerGenericGui actionperformed(e) triggers");
        if (!(e.getSource() instanceof JMenuItem)) {
            return;
        }
        if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("addAsset")) {
            this.addAssetWithPopup();
        }
        else if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("deleteAsset")) {
            this.removeAssetWithPopup();
            this.activeButtonsRemoveIfNecessary();
        }
        else if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("deleteAllAssets")) {
            this.removeAllAssetsWithPopup();
            this.activeButtonsRemoveIfNecessary();
        }
    }
    
    public void assetsModified(final AssetEvent e) {
        Utils.logger.debug("AssetServerGenericGui assetsModified");
        if (e.getEvent() == 1) {
            this.addAssetToTable(BusinessCore.getAsset(e.getAssetName()));
        }
        else if (e.getEvent() == 0) {
            this.removeAssetFromTable(e.getAssetName());
        }
    }
    
    private void addAssetToTable(final Asset asset) {
        ((JessXTableModel)this.jTable1.getModel()).addRow(new Object[] { asset.getAssetName(), asset.getAssetType() });
    }
    
    private void removeAssetFromTable(final String assetName) {
        final int row = this.locateAssetInTable(assetName);
        if (row >= 0) {
            ((JessXTableModel)this.jTable1.getModel()).removeRow(row);
        }
    }
    
    private int locateAssetInTable(final String assetName) {
        for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
            if (this.jTable1.getValueAt(i, 0).toString().equalsIgnoreCase(assetName)) {
                return i;
            }
        }
        return -1;
    }
    
    private void addAssetWithPopup() {
        final JLabel jLabelAssetName = new JLabel("Asset name: ");
        final JLabel jLabelAssetType = new JLabel("Asset type: ");
        final JTextField jTextFieldAssetName = new JTextField();
        final JPanel jPanel = new JPanel(new GridLayout(2, 2));
        jPanel.add(jLabelAssetName);
        jPanel.add(jTextFieldAssetName);
        jPanel.add(jLabelAssetType);
        jPanel.add(this.jComboBox1);
        boolean assetAddedIsNew = false;
        while (!assetAddedIsNew) {
            final int response = JOptionPane.showConfirmDialog(this.jScrollPane2, jPanel, "Add a new asset.", 2, 1);
            jTextFieldAssetName.selectAll();
            if (response == 2) {
                return;
            }
            if (jTextFieldAssetName.getText().equalsIgnoreCase("") || this.jComboBox1.getSelectedIndex() == -1) {
                continue;
            }
            String nameNewAsset = jTextFieldAssetName.getText();
            if (nameNewAsset.equals("Cash")) {
                nameNewAsset = String.valueOf(nameNewAsset) + " ";
            }
            if (this.jTable1.getRowCount() == 0) {
                assetAddedIsNew = true;
                this.addAsset(nameNewAsset, this.jComboBox1.getSelectedItem().toString());
                this.activeButtonsRemoveIfNecessary();
                return;
            }
            int numOfAssets = this.jTable1.getRowCount();
            assetAddedIsNew = true;
            do {
                Utils.logger.debug(String.valueOf(numOfAssets) + ";" + nameNewAsset + "; assetAddedIsNew is" + assetAddedIsNew + this.jTable1.getValueAt(numOfAssets - 1, 0).toString());
                if (this.jTable1.getValueAt(numOfAssets - 1, 0).toString().equalsIgnoreCase(nameNewAsset)) {
                    assetAddedIsNew = false;
                }
            } while (--numOfAssets > 0 & assetAddedIsNew);
            if (assetAddedIsNew) {
                this.addAsset(nameNewAsset, this.jComboBox1.getSelectedItem().toString());
                this.activeButtonsRemoveIfNecessary();
                return;
            }
            final String mess = new String("The name '" + nameNewAsset + "' is already used by an asset. Please choose an other name.");
            JOptionPane.showMessageDialog(this, mess, "Name conflict", 2);
        }
    }
    
    private void addAsset(final String name, final String type) {
        try {
            final Asset newAsset = AssetCreator.createAsset(type);
            newAsset.setAssetName(name);
            BusinessCore.addAsset(newAsset);
        }
        catch (final AssetNotCreatedException ex) {
            Utils.logger.error("Unable to add an asset: " + ex.toString());
            JOptionPane.showMessageDialog(null, "Unable to add the asset: " + name + "'.", "Exception during the addition of the asset", 2);
            ex.printStackTrace();
        }
    }
    
    private void removeAssetWithPopup() {
        Utils.logger.debug("removeAssetWithPopup");
        final JPanel dialogPane = new JPanel(new GridLayout(2, 1));
        final JLabel question = new JLabel("Which asset do you want to remove ?");
        final JComboBox jComboBox = new JComboBox();
        for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
            jComboBox.addItem(this.jTable1.getValueAt(i, 0));
        }
        dialogPane.add(question);
        dialogPane.add(jComboBox);
        final int chosenOption = JOptionPane.showConfirmDialog(null, dialogPane, "Delete an asset", 2);
        if (chosenOption == 0) {
            this.removeAssetAtLine(jComboBox.getSelectedIndex());
        }
    }
    
    private void removeAllAssetsWithPopup() {
        if (this.jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "There is no asset to remove.", "No asset", 1);
            return;
        }
        final int chosenOption = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete all the assets ?", "Delete all the assets", 0);
        if (chosenOption == 0) {
            for (int numberOfAssets = this.jTable1.getRowCount(); numberOfAssets > 0; --numberOfAssets) {
                this.removeAssetAtLine(numberOfAssets - 1);
            }
        }
    }
    
    private void removeAssetAtLine(final int row) {
        BusinessCore.removeAsset(BusinessCore.getAsset(this.jTable1.getValueAt(row, 0).toString()));
    }
    
    private void activeButtonsRemoveIfNecessary() {
        if (AssetServerGenericGui.editable) {
            if (this.jTable1.getRowCount() == 0) {
                this.popup.getComponent(0).setEnabled(true);
                this.popup.getComponent(1).setEnabled(false);
                this.popup.getComponent(2).setEnabled(false);
            }
            else {
                this.popup.getComponent(0).setEnabled(true);
                this.popup.getComponent(1).setEnabled(true);
                this.popup.getComponent(2).setEnabled(true);
            }
        }
        else {
            this.popup.getComponent(0).setEnabled(false);
            this.popup.getComponent(1).setEnabled(false);
            this.popup.getComponent(2).setEnabled(false);
        }
    }
    
    /**
     * Put the names of all assets in a combo box.
     */
    private void initAssetTypeComboBox() {
      Utils.logger.debug("initAssetTypeComboBox");
      Iterator keyIterator = AssetCreator.assetFactories.keySet().iterator();
      while (keyIterator.hasNext()) {
        String key = (String) keyIterator.next();
        this.jComboBox1.addItem(key);
      }
    }
}
