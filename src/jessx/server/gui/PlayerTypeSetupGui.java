// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import java.awt.event.MouseListener;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jessx.utils.PopupListener;
import jessx.business.Institution;
import javax.swing.JOptionPane;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.GridLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import jessx.business.Operator;
import java.util.Vector;
import jessx.business.event.InstitutionEvent;
import java.awt.event.ActionEvent;
import jessx.utils.Utils;
import jessx.business.event.PlayerTypeEvent;
import java.util.Iterator;
import jessx.business.BusinessCore;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import jessx.utils.JessXTableModel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import jessx.business.PlayerType;
import jessx.business.event.InstitutionListener;
import java.awt.event.ActionListener;
import jessx.business.event.PlayerTypeListener;
import javax.swing.JPanel;

public class PlayerTypeSetupGui extends JPanel implements DisplayableNode, PlayerTypeListener, ActionListener, InstitutionListener
{
    PlayerType playerType;
    JScrollPane jScrollPane1;
    JScrollPane jScrollPane2;
    GridBagLayout gridBagLayout1;
    JTable jTable1;
    JTable jTable2;
    JPopupMenu popup;
    private JMenuItem menuItem1;
    private JMenuItem menuItem2;
    private TableModelPortfolioSetup tableModel;
    
    public PlayerTypeSetupGui(final PlayerType pt) {
        this.jScrollPane1 = new JScrollPane();
        this.jScrollPane2 = new JScrollPane();
        this.gridBagLayout1 = new GridBagLayout();
        this.jTable2 = new JTable(new JessXTableModel(new Object[] { "Operators played" }, 0));
        this.playerType = pt;
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        final Iterator opIter = this.playerType.getOperatorsPlayed().keySet().iterator();
        while (opIter.hasNext()) {
            ((DefaultTableModel)this.jTable2.getModel()).addRow(new Object[] { opIter.next().toString() });
        }
        pt.addPlayerTypeListener(this);
        BusinessCore.addInstitutionListener(this);
    }
    
    public void setEditable() {
        this.menuItem1.setEnabled(true);
        this.menuItem2.setEnabled(true);
        this.tableModel.setCellEditable();
    }
    
    public void setUneditable() {
        this.menuItem1.setEnabled(false);
        this.menuItem2.setEnabled(false);
        this.tableModel.setCellUneditable();
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return this.playerType.getPlayerTypeName();
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (e.getEvent() == 2) {
            ((DefaultTableModel)this.jTable2.getModel()).addRow(new Object[] { e.getOperator().toString() });
        }
        else if (e.getEvent() == 3) {
            Utils.logger.debug("Following operator right denied to " + this.playerType.getPlayerTypeName() + " : " + e.getOperator().toString());
            for (int i = this.jTable2.getRowCount() - 1; i >= 0; --i) {
                if (this.jTable2.getValueAt(i, 0).toString().equalsIgnoreCase(e.getOperator().toString())) {
                    ((DefaultTableModel)this.jTable2.getModel()).removeRow(i);
                }
            }
        }
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getActionCommand().equalsIgnoreCase("addOperator")) {
            this.askUserAnOperator();
        }
        else if (e.getActionCommand().equalsIgnoreCase("removeOperator")) {
            this.askUserOperatorToRemove();
        }
    }
    
    public void institutionsModified(final InstitutionEvent e) {
        if (e.getEvent() == 0) {
            final Iterator operIter = this.playerType.getOperatorsPlayed().keySet().iterator();
            final Vector operToDelete = new Vector();
            while (operIter.hasNext()) {
                final String key = (String) operIter.next();
                if (this.playerType.getOperatorPlayed(key).getInstitution().equalsIgnoreCase(e.getInstitutionName())) {
                    operToDelete.add(this.playerType.getOperatorPlayed(key));
                }
            }
            for (int i = 0; i < operToDelete.size(); ++i) {
                this.playerType.removeOperatorPlayed((Operator) operToDelete.elementAt(i));
            }
        }
    }
    
    private void askUserAnOperator() {
        JLabel jLabel = new JLabel("You are going to grant this player type with the right to play an operator.");

        Vector operatorList = new Vector();
        Iterator institIter = BusinessCore.getInstitutions().keySet().iterator();
        while(institIter.hasNext()) {
          String institKey = (String)institIter.next();
          Institution tempInstit = BusinessCore.getInstitution(institKey);

          if (!this.isAlreadyPresent(institKey)) {
            Iterator operIter = ((Institution)BusinessCore.getInstitution(institKey)).getOperators().keySet().iterator();
            while (operIter.hasNext()) {
              String operKey = (String) operIter.next();
              if (!isOperatorPlayed(tempInstit.getOperator(operKey).getCompleteName()))
                operatorList.add(tempInstit.getOperator(operKey));
            }
          }
        }

        JComboBox jComboBox = new JComboBox(operatorList);

        JPanel panel = new JPanel(new GridLayout(2,1));

        panel.add(jLabel);
        panel.add(jComboBox);

        int response = JOptionPane.showConfirmDialog(jScrollPane2,panel,"Grant an operator right.", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

        if (response == JOptionPane.OK_OPTION) {
          Utils.logger.debug("Operator selected : " + jComboBox.getSelectedIndex());
          if (jComboBox.getSelectedIndex()!=-1)
            this.playerType.addOperatorPlayed((Operator)jComboBox.getSelectedItem());
        }
      }
    private boolean isAlreadyPresent(final String institutionName) {
        for (int i = 0; i < this.jTable2.getRowCount(); ++i) {
            final String completeName = this.jTable2.getValueAt(i, 0).toString();
            if (completeName.substring(completeName.lastIndexOf(" on ") + 4).equals(institutionName)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isOperatorPlayed(final String operCompleteName) {
        for (int i = 0; i < this.jTable2.getRowCount(); ++i) {
            if (this.jTable2.getValueAt(i, 0).toString().equals(operCompleteName)) {
                return true;
            }
        }
        return false;
    }
    
    private void askUserOperatorToRemove() {
        final JLabel jLabel = new JLabel("Which operator do you wish to remove ?");
        final JComboBox jComboBox = new JComboBox();
        final JPanel panel = new JPanel(new GridLayout(2, 1));
        for (int i = 0; i < this.jTable2.getRowCount(); ++i) {
            jComboBox.addItem(this.jTable2.getValueAt(i, 0).toString());
        }
        if (jComboBox.getItemCount() == 0) {
            JOptionPane.showConfirmDialog(this.jScrollPane2, "This player type hasn't yet been granted with any operator right.\nYou can not deny him any.", "Error: no right to deny.", 0, 2);
            return;
        }
        panel.add(jLabel);
        panel.add(jComboBox);
        final int response = JOptionPane.showConfirmDialog(this.jScrollPane2, panel, "Removing an operator.", 2, 1);
        if (response == 0) {
            this.playerType.removeOperatorPlayed(this.playerType.getOperatorPlayed(jComboBox.getSelectedItem().toString()));
        }
    }
    
    private void jbInit() throws Exception {
        this.tableModel = new TableModelPortfolioSetup(this.playerType.getPortfolio());
        this.jTable1 = new JTable(this.tableModel);
        this.popup = new JPopupMenu();
        (this.menuItem1 = new JMenuItem("Grant this player type an operator right")).setActionCommand("addOperator");
        this.menuItem1.addActionListener(this);
        this.jTable1.setRowSelectionAllowed(false);
        this.popup.add(this.menuItem1);
        (this.menuItem2 = new JMenuItem("Deny this player type an operator right")).setActionCommand("removeOperator");
        this.menuItem2.addActionListener(this);
        this.popup.add(this.menuItem2);
        final MouseListener popupListener = new PopupListener(this.popup);
        this.jScrollPane2.addMouseListener(popupListener);
        this.jTable2.addMouseListener(popupListener);
        this.setLayout(this.gridBagLayout1);
        this.jTable1.setRowSelectionAllowed(false);
        this.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
        this.add(this.jScrollPane2, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jScrollPane2.getViewport().add(this.jTable2, null);
    }
}
