// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.institutions;

import javax.swing.JComboBox;
import jessx.utils.JessXTableModel;
import jessx.business.Operator;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import javax.swing.table.TableModel;
import jessx.business.Institution;
import jessx.business.event.InstitutionEvent;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import jessx.utils.PopupListener;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.LayoutManager;
import jessx.business.BusinessCore;
import javax.swing.JCheckBox;
import javax.swing.JMenuItem;
import java.awt.event.MouseListener;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import jessx.business.event.InstitutionListener;
import java.awt.event.ActionListener;
import javax.swing.JPanel;

public class OrderMarketSetupGui extends JPanel implements ActionListener, InstitutionListener
{
    private OrderMarket orderMarket;
    GridBagLayout gridBagLayout1;
    JPanel jPanel1;
    JPanel jPanel2;
    GridBagLayout gridBagLayout2;
    JLabel jLabel2;
    JScrollPane jScrollPane1;
    JTable jTable1;
    JScrollPane jScrollPane2;
    GridBagLayout gridBagLayout3;
    JTable jTable2;
    JPopupMenu popup;
    MouseListener popupListener;
    TableModelOperationsCost tableModelOperationsCost;
    OrderMarketOperatorsTableModel orderMarketOperatorsTableModel;
    JMenuItem menuItemAdd;
    JMenuItem menuItemDelete;
    JCheckBox keepingOrderBook;
    
    public OrderMarketSetupGui(final OrderMarket anOrderMarket) {
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.jPanel2 = new JPanel();
        this.gridBagLayout2 = new GridBagLayout();
        this.jLabel2 = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.jScrollPane2 = new JScrollPane();
        this.gridBagLayout3 = new GridBagLayout();
        this.jTable2 = new JTable();
        this.keepingOrderBook = new JCheckBox("Check here if you want to keep the order book between two periods");
        this.orderMarket = anOrderMarket;
        this.initOperationsTables();
        this.keepingOrderBook.setSelected(this.orderMarket.getKeepingOrderBook());
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        BusinessCore.addInstitutionListener(this);
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void desactive() {
        this.tableModelOperationsCost.setUneditable();
        this.orderMarketOperatorsTableModel.setUneditable();
        this.menuItemAdd.setEnabled(false);
        this.menuItemDelete.setEnabled(false);
        this.keepingOrderBook.setEnabled(false);
    }
    
    public void active() {
        this.tableModelOperationsCost.setEditable();
        this.orderMarketOperatorsTableModel.setEditable();
        this.menuItemAdd.setEnabled(true);
        this.menuItemDelete.setEnabled(true);
        this.keepingOrderBook.setEnabled(true);
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout1);
        this.jPanel1.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "General Parameters"));
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.jPanel2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Add operators"));
        this.jPanel2.setLayout(this.gridBagLayout3);
        this.jLabel2.setText("Cost of the operations:");
        this.keepingOrderBook.addActionListener(new OrderMarketSetupGui_KeepingOrderBook_actionAdapter(this));
        this.popup = new JPopupMenu();
        (this.menuItemAdd = new JMenuItem("Add an operator")).setActionCommand("addOperator");
        this.menuItemAdd.addActionListener(this);
        this.jTable1.setToolTipText("Specify here the cost of each operation");
        this.jScrollPane1.setToolTipText("Specify here the cost of each operation");
        this.jScrollPane2.setToolTipText("Right-click here to add or remove an operator");
        this.jTable2.setToolTipText("Right-click here to add or remove an operator");
        this.popup.add(this.menuItemAdd);
        (this.menuItemDelete = new JMenuItem("Delete an operator")).setActionCommand("deleteOperator");
        this.menuItemDelete.addActionListener(this);
        this.popup.add(this.menuItemDelete);
        this.popupListener = new PopupListener(this.popup);
        this.jScrollPane2.addMouseListener(this.popupListener);
        this.jTable2.addMouseListener(this.popupListener);
        this.jTable1.setRowSelectionAllowed(false);
        this.jTable2.setRowSelectionAllowed(false);
        this.jScrollPane1.setMinimumSize(new Dimension(23, 64));
        this.jPanel1.add(this.keepingOrderBook, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(10, 6, 3, 6), 0, 0));
        this.jPanel1.add(this.jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(10, 6, 3, 6), 0, 0));
        this.jPanel1.add(this.jScrollPane1, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, 10, 1, new Insets(3, 6, 6, 6), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
        this.jPanel2.add(this.jScrollPane2, new GridBagConstraints(0, 0, 0, 0, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jScrollPane2.getViewport().add(this.jTable2, null);
        this.add(this.jPanel2, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.7, 10, 1, new Insets(3, 6, 3, 6), 0, 0));
        this.add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.3, 11, 1, new Insets(6, 6, 3, 6), 0, 0));
    }
    
    public void institutionsModified(final InstitutionEvent e) {
        if (e.getEvent() != 1 && e.getEvent() == 0 && e.getInstitutionName().equalsIgnoreCase(this.orderMarket.getName())) {
            this.orderMarket.removeAllOperators();
        }
    }
    
    private void initOperationsTables() {
        this.tableModelOperationsCost = new TableModelOperationsCost(this.orderMarket);
        this.jTable1 = new JTable(this.tableModelOperationsCost);
        this.orderMarketOperatorsTableModel = new OrderMarketOperatorsTableModel(this.orderMarket);
        this.jTable2 = new JTable(this.orderMarketOperatorsTableModel);
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() instanceof JMenuItem) {
            if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("addOperator")) {
                final JPanel jPanel = new JPanel(new GridLayout(3, 2));
                final JTextField jTextField = new JTextField("");
                final JLabel jLabelOperatorName = new JLabel("Enter a new operator title here :");
                jPanel.add(jLabelOperatorName);
                jPanel.add(jTextField);
                int answer;
                boolean wrongName;
                do {
                    wrongName = false;
                    answer = JOptionPane.showConfirmDialog(this, jPanel, "New Operator", 2, 1);
                    if (answer == 0) {
                        wrongName = (jTextField.getText().equals("") || this.orderMarket.getOperators().containsKey(jTextField.getText()));
                        if (!wrongName) {
                            continue;
                        }
                        final String mess = new String("The name '" + jTextField.getText() + "' is already used. Please choose an other name.");
                        if (jTextField.getText().equals("")) {
                            continue;
                        }
                        JOptionPane.showMessageDialog(this, mess, "Name conflict", 2);
                    }
                } while (answer == 0 && wrongName);
                if (answer == 0) {
                    this.orderMarket.addOperator(new Operator(this.orderMarket.getName(), jTextField.getText(), new Vector(), 5));
                }
            }
            else if (((JMenuItem)e.getSource()).getActionCommand().equalsIgnoreCase("deleteOperator")) {
                final int rowToBeRemoved = this.askUserOperatorToBeDeleted();
                if (rowToBeRemoved != -1) {
                    this.orderMarket.removeOperator(this.jTable2.getValueAt(rowToBeRemoved, 0).toString());
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return this.orderMarket.getName();
    }
    
    public void stopEditingInsitutionPanel() {
        for (int i = 0; i < this.jTable1.getColumnCount(); ++i) {
            ((JessXTableModel)this.jTable1.getModel()).setEditable(i, false);
        }
        for (int i = 0; i < this.jTable2.getColumnCount(); ++i) {
            ((JessXTableModel)this.jTable2.getModel()).setEditable(i, false);
        }
        this.jScrollPane2.removeMouseListener(this.popupListener);
        this.jTable2.removeMouseListener(this.popupListener);
    }
    
    private int askUserOperatorToBeDeleted() {
        if (this.jTable2.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "There is no operator to delete.", "No operator.", 1);
            return -1;
        }
        final JPanel dialogPane = new JPanel(new GridLayout(2, 1));
        final JLabel question = new JLabel("Which operator do you want to delete ?");
        final JComboBox jComboBox = new JComboBox();
        for (int i = 0; i < this.jTable2.getRowCount(); ++i) {
            jComboBox.addItem(this.jTable2.getValueAt(i, 0));
        }
        dialogPane.add(question);
        dialogPane.add(jComboBox);
        final int chosenOption = JOptionPane.showConfirmDialog(null, dialogPane, "Delete an operator", 2);
        if (chosenOption == 0) {
            return jComboBox.getSelectedIndex();
        }
        return -1;
    }
    
    public void KeepingOrderBook_actionPerformed(final ActionEvent e) {
        this.orderMarket.setKeepingOrderBook(this.keepingOrderBook.isSelected());
    }
}
