// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.business.InstitutionNotCreatedException;
import jessx.utils.Utils;
import java.util.Iterator;
import jessx.business.InstitutionCreator;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import jessx.business.Institution;
import jessx.business.event.InstitutionEvent;
import jessx.business.event.AssetEvent;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jessx.utils.PopupListener;
import javax.swing.JMenuItem;
import javax.swing.table.TableModel;
import jessx.utils.JessXTableModel;
import java.awt.LayoutManager;
import jessx.business.BusinessCore;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import jessx.business.event.AssetListener;
import jessx.business.event.InstitutionListener;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class InstitutionServerGenericGui extends JPanel implements DisplayableNode, ActionListener, InstitutionListener, AssetListener
{
    GridBagLayout gridBagLayout1;
    JScrollPane jScrollPane1;
    JTable jTable1;
    JPopupMenu popup;
    MouseListener popupListener;
    
    @Override
    public String toString() {
        return "Institutions";
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public InstitutionServerGenericGui() {
        this.gridBagLayout1 = new GridBagLayout();
        this.jScrollPane1 = new JScrollPane();
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        BusinessCore.addAssetListener(this);
        BusinessCore.addInstitutionListener(this);
    }
    
    public void setEditable() {
        this.jScrollPane1.addMouseListener(this.popupListener);
        this.jTable1.addMouseListener(this.popupListener);
        this.popup.getComponent(0).setEnabled(true);
        this.popup.getComponent(1).setEnabled(true);
    }
    
    public void setUneditable() {
        this.jScrollPane1.addMouseListener(this.popupListener);
        this.jTable1.addMouseListener(this.popupListener);
        this.popup.getComponent(0).setEnabled(false);
        this.popup.getComponent(1).setEnabled(false);
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout1);
        this.jTable1 = new JTable(new JessXTableModel(new String[] { "Institution name", "Institution Type", "Quoted Asset" }, 0));
        this.popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add an institution");
        menuItem.setActionCommand("addInstitution");
        menuItem.addActionListener(this);
        this.jScrollPane1.setToolTipText("Right-click here to add or delete an institution");
        this.jTable1.setToolTipText("Right-click here to add or delete an institution");
        this.popup.add(menuItem);
        menuItem = new JMenuItem("Remove an institution");
        menuItem.setActionCommand("deleteInstitution");
        menuItem.addActionListener(this);
        this.popup.add(menuItem);
        this.popupListener = new PopupListener(this.popup);
        this.jScrollPane1.addMouseListener(this.popupListener);
        this.jTable1.addMouseListener(this.popupListener);
        this.jTable1.setRowSelectionAllowed(false);
        this.add(this.jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(3, 6, 6, 6), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("addInstitution")) {
                this.addInstitutionWithPopup();
            }
            else if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("deleteInstitution")) {
                this.deleteInsitutionWithPopup();
            }
        }
    }
    
    public void assetsModified(final AssetEvent e) {
        if (e.getEvent() != 1 && e.getEvent() == 0) {
            this.removeInstitutionRelatedTo(e.getAssetName());
        }
    }
    
    public void institutionsModified(final InstitutionEvent e) {
        if (e.getEvent() == 1) {
            this.addInstitutionToTable(BusinessCore.getInstitution(e.getInstitutionName()));
        }
        else if (e.getEvent() == 0) {
            this.removeInstitutionFromTable(e.getInstitutionName());
        }
    }
    
    private void addInstitutionToTable(final Institution institution) {
        ((DefaultTableModel)this.jTable1.getModel()).addRow(new String[] { institution.getName(), institution.getInstitutionType(), institution.getAssetName() });
    }
    
    private void removeInstitutionFromTable(final String institutionName) {
        final int row = this.locateInsitutionInTable(institutionName);
        ((JessXTableModel)this.jTable1.getModel()).removeRow(row);
    }
    
    private void removeInstitutionRelatedTo(final String assetName) {
        for (int i = this.jTable1.getRowCount() - 1; i >= 0; --i) {
            if (BusinessCore.getInstitution(this.jTable1.getValueAt(i, 0).toString()).getAssetName().equalsIgnoreCase(assetName)) {
                BusinessCore.removeInstitution(BusinessCore.getInstitution(this.jTable1.getValueAt(i, 0).toString()));
            }
        }
    }
    
    private void addInstitutionWithPopup() {
        final JLabel jLabelInstitutionName = new JLabel("Insitution name : ");
        final JTextField jTextFieldInstitutionName = new JTextField("");
        final JLabel jLabelInstitutionType = new JLabel("Institution type : ");
        final JComboBox jComboBoxInstitutionType = new JComboBox();
        final JLabel jLabelAssetQuoted = new JLabel("Asset quoted : ");
        final JComboBox jComboBoxAssetQuoted = new JComboBox();
        final JPanel jPanel = new JPanel(new GridLayout(3, 2));
        final Iterator assetsIterator = BusinessCore.getAssets().keySet().iterator();
        if (!assetsIterator.hasNext()) {
            JOptionPane.showConfirmDialog(this.jScrollPane1, "There no asset to be quoted. You should define an asset first.", "error: no defined asset", 0, 0);
            return;
        }
        while (assetsIterator.hasNext()) {
            jComboBoxAssetQuoted.addItem(assetsIterator.next());
        }
        final Iterator institIterator = InstitutionCreator.institutionFactories.keySet().iterator();
        while (institIterator.hasNext()) {
            jComboBoxInstitutionType.addItem(institIterator.next());
        }
        jPanel.add(jLabelInstitutionName);
        jPanel.add(jTextFieldInstitutionName);
        jPanel.add(jLabelInstitutionType);
        jPanel.add(jComboBoxInstitutionType);
        jPanel.add(jLabelAssetQuoted);
        jPanel.add(jComboBoxAssetQuoted);
        boolean wrongName = false;
        int answer;
        do {
            answer = JOptionPane.showConfirmDialog(this, jPanel, "New Institution", 2, 1);
            if (answer == 0) {
                wrongName = (jTextFieldInstitutionName.getText().equals("") || BusinessCore.getInstitutions().keySet().contains(jTextFieldInstitutionName.getText()));
                if (wrongName) {
                    final String mess = new String("The name '" + jTextFieldInstitutionName.getText() + "' is already used. Please choose an other name.");
                    if (!jTextFieldInstitutionName.getText().equals("")) {
                        JOptionPane.showMessageDialog(this, mess, "Name conflict", 2);
                    }
                }
                final String assetName = jComboBoxAssetQuoted.getSelectedItem().toString();
                Iterator instit;
                boolean sameAssetNeverUsed;
                String instName;
                for (instit = BusinessCore.getInstitutions().keySet().iterator(), sameAssetNeverUsed = true; instit.hasNext() && sameAssetNeverUsed; sameAssetNeverUsed = false) {
                    instName = (String) instit.next();
                    if (((Institution) BusinessCore.getInstitutions().get(instName)).getAssetName().equals(assetName)) {}
                }
                if (sameAssetNeverUsed) {
                    continue;
                }
                final int ans = JOptionPane.showConfirmDialog(this, "An institution with the same asset already exists.\nDo you want to correct this choice?", "Warning", 0, 2);
                wrongName = (ans == 0);
            }
        } while (answer == 0 && wrongName);
        if (answer == 0 && jTextFieldInstitutionName.getText().length() != 0 && jComboBoxInstitutionType.getSelectedIndex() != -1 && jComboBoxAssetQuoted.getSelectedIndex() != -1) {
            this.addInstitution(jTextFieldInstitutionName.getText(), jComboBoxInstitutionType.getSelectedItem().toString(), jComboBoxAssetQuoted.getSelectedItem().toString());
        }
    }
    
    private void deleteInsitutionWithPopup() {
        if (this.jTable1.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "There is no institution to delete.", "No institution.", 1);
            return;
        }
        final JPanel dialogPane = new JPanel(new GridLayout(2, 1));
        final JLabel question = new JLabel("Which institution do you want to delete ?");
        final JComboBox jComboBox = new JComboBox();
        for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
            jComboBox.addItem(this.jTable1.getValueAt(i, 0));
        }
        dialogPane.add(question);
        dialogPane.add(jComboBox);
        final int chosenOption = JOptionPane.showConfirmDialog(null, dialogPane, "Delete an institution", 2);
        if (chosenOption == 0) {
            BusinessCore.removeInstitution(BusinessCore.getInstitution(this.jTable1.getValueAt(jComboBox.getSelectedIndex(), 0).toString()));
        }
    }
    
    private int locateInsitutionInTable(final String name) {
        for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
            if (this.jTable1.getValueAt(i, 0).toString().equalsIgnoreCase(name)) {
                return i;
            }
        }
        return -1;
    }
    
    private void addInstitution(final String institutionName, final String institutionType, final String quotedAsset) {
        try {
            final Institution newInstitution = InstitutionCreator.createInstitution(institutionType);
            newInstitution.setName(institutionName);
            newInstitution.setAsset(BusinessCore.getAsset(quotedAsset));
            BusinessCore.addInstitution(newInstitution);
        }
        catch (final InstitutionNotCreatedException ex) {
            Utils.logger.warn(String.valueOf(ex.toString()) + ". Error creating an institution whose type was in combobox.");
            ex.printStackTrace();
        }
    }
}
