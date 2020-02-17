// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.JComboBox;
import jessx.business.PlayerType;
import javax.swing.JOptionPane;
import java.awt.GridLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import jessx.business.event.PlayerTypeEvent;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import jessx.utils.PopupListener;
import javax.swing.JMenuItem;
import javax.swing.table.TableModel;
import jessx.utils.JessXTableModel;
import jessx.business.BusinessCore;
import javax.swing.JPopupMenu;
import java.awt.GridBagLayout;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import jessx.business.event.PlayerTypeListener;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class PlayersTypeServerGenericGui extends JPanel implements DisplayableNode, ActionListener, PlayerTypeListener
{
    ExperimentSetupTree treeModel;
    JScrollPane jScrollPane1;
    JTable jTable1;
    GridBagLayout gridBagLayout1;
    JPopupMenu popup;
    
    public PlayersTypeServerGenericGui(final ExperimentSetupTree treeModel) {
        this.jScrollPane1 = new JScrollPane();
        this.jTable1 = new JTable();
        this.gridBagLayout1 = new GridBagLayout();
        this.treeModel = treeModel;
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        BusinessCore.getScenario().addPlayerTypeListener(this);
    }
    
    public void setEditable() {
        this.jTable1.setEnabled(true);
        this.popup.getComponent(0).setEnabled(true);
        this.popup.getComponent(1).setEnabled(true);
    }
    
    public void setUneditable() {
        this.jTable1.setEnabled(false);
        this.popup.getComponent(0).setEnabled(false);
        this.popup.getComponent(1).setEnabled(false);
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return "Player's Category";
    }
    
    private void jbInit() throws Exception {
        this.jTable1 = new JTable(new JessXTableModel(new Object[] { "Player type" }, 0));
        this.popup = new JPopupMenu();
        JMenuItem menuItem = new JMenuItem("Add a new player type");
        menuItem.setActionCommand("addPlayerType");
        menuItem.addActionListener(this);
        this.jTable1.setRowSelectionAllowed(false);
        this.popup.add(menuItem);
        menuItem = new JMenuItem("Remove a player type");
        menuItem.setActionCommand("removePlayerType");
        menuItem.addActionListener(this);
        this.popup.add(menuItem);
        final MouseListener popupListener = new PopupListener(this.popup);
        this.jScrollPane1.addMouseListener(popupListener);
        this.jTable1.addMouseListener(popupListener);
        this.setLayout(this.gridBagLayout1);
        this.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("addPlayerType")) {
                this.addPlayerTypeWithPopup();
            }
            else if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("removePlayerType")) {
                this.removePlayerTypeWithPopup();
            }
        }
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (e.getEvent() == 1) {
            ((JessXTableModel)this.jTable1.getModel()).addRow(new Object[] { e.getPlayerType().getPlayerTypeName() });
        }
        else if (e.getEvent() == 0) {
            final int row = 0;
            for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
                if (this.jTable1.getValueAt(i, 0).toString().equalsIgnoreCase(e.getPlayerType().getPlayerTypeName())) {
                    ((JessXTableModel)this.jTable1.getModel()).removeRow(i);
                }
            }
        }
    }
    
    private void addPlayerTypeWithPopup() {
        final JLabel jLabelPlayerTypeName = new JLabel("Player's type :");
        final JTextField jTextFieldPlayerTypeName = new JTextField("");
        final JPanel jPanel = new JPanel(new GridLayout(1, 2));
        jPanel.add(jLabelPlayerTypeName);
        jPanel.add(jTextFieldPlayerTypeName);
        int answer;
        boolean wrongName;
        do {
            wrongName = false;
            answer = JOptionPane.showConfirmDialog(this.jScrollPane1, jPanel, "Add a player type.", 2, 1);
            if (answer == 0) {
                wrongName = (jTextFieldPlayerTypeName.getText().equals("") || BusinessCore.getScenario().getPlayerTypes().keySet().contains(jTextFieldPlayerTypeName.getText()));
                if (!wrongName) {
                    continue;
                }
                final String mess = new String("The name '" + jTextFieldPlayerTypeName.getText() + "' is already used. Please choose an other name.");
                if (jTextFieldPlayerTypeName.getText().equals("")) {
                    continue;
                }
                JOptionPane.showMessageDialog(this, mess, "Name conflict", 2);
            }
        } while (answer == 0 && wrongName);
        if (answer == 0) {
            BusinessCore.getScenario().addPlayerType(new PlayerType(jTextFieldPlayerTypeName.getText()));
        }
    }
    
    private void removePlayerTypeWithPopup() {
        final JLabel jLabel = new JLabel("Which operator do you wish to remove ?");
        final JComboBox jComboBox = new JComboBox();
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        for (int i = 0; i < this.jTable1.getRowCount(); ++i) {
            jComboBox.addItem(this.jTable1.getValueAt(i, 0).toString());
        }
        if (jComboBox.getItemCount() == 0) {
            JOptionPane.showConfirmDialog(this.jScrollPane1, "There is no player type in this experiment for now.", "Error: no player type", 0, 2);
            return;
        }
        panel.add(jLabel);
        panel.add(jComboBox);
        final int response = JOptionPane.showConfirmDialog(this.jScrollPane1, panel, "Removing a player type.", 2, 1);
        if (response == 0) {
            BusinessCore.getScenario().removePlayerType(BusinessCore.getScenario().getPlayerType(jComboBox.getSelectedItem().toString()));
        }
    }
}
