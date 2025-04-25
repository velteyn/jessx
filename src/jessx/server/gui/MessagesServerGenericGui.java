// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import jessx.business.event.ProgrammedInfoEvent;
import jessx.utils.Utils;
import java.awt.event.MouseEvent;
import java.util.Vector;
import java.awt.GridBagConstraints;
import javax.swing.table.TableCellRenderer;
import jessx.utils.gui.JButtonRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import jessx.business.event.ChangeGeneralParametersEvent;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.BusinessCore;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.table.TableModel;
import javax.swing.border.Border;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import jessx.business.event.ChangeGeneralParametersListener;
import jessx.business.event.ProgrammedInfoListener;
import jessx.business.event.PlayerTypeListener;
import javax.swing.JPanel;

public class MessagesServerGenericGui extends JPanel implements DisplayableNode, PlayerTypeListener, ProgrammedInfoListener, ChangeGeneralParametersListener
{
    String[] message;
    JPanel jPanel1;
    JPanel jPanel2;
    JPanel jPanel3;
    GridBagLayout gridBagLayout0;
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    GridBagLayout gridBagLayout3;
    JScrollPane jScrollPane1;
    JTextArea jTextArea11Message;
    JSpinner jSpinner2Time;
    JSpinner jSpinner2Period;
    JSpinnerEditor jSpinnerEditorPeriod;
    JComboBox jComboBox2Receivers;
    JComboBox jComboBox2inTable;
    JLabel jLabel2To;
    JLabel jLabel2Time;
    JLabel jLabel2Period;
    JButton jButton2Validate;
    JButton jButtonSort;
    JScrollPane jScrollPane3;
    TableModelMessages tableModelMessages;
    JTable jTable3Receivers;
    Border border1;
    Border border2;
    Border border3;
    MessagesServerGenericGui_jButton2Validate_actionAdapter messagesServerGenericGui_jButton2Validate_actionAdapter;
    MessagesServerGenericGui_jButtonSort_actionAdapter messagesServerGenericGui_jButtonSort_actionAdapter;
    
    public MessagesServerGenericGui() {
        this.message = new String[] { "", "", "", "" };
        this.jPanel1 = new JPanel();
        this.jPanel2 = new JPanel();
        this.jPanel3 = new JPanel();
        this.gridBagLayout0 = new GridBagLayout();
        this.gridBagLayout1 = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.gridBagLayout3 = new GridBagLayout();
        this.jScrollPane1 = new JScrollPane();
        this.jTextArea11Message = new JTextArea();
        this.jSpinner2Time = new JSpinner();
        this.jSpinner2Period = new JSpinner();
        this.jSpinnerEditorPeriod = new JSpinnerEditor("period");
        this.jComboBox2Receivers = new JComboBox();
        this.jComboBox2inTable = new JComboBox();
        this.jLabel2To = new JLabel();
        this.jLabel2Time = new JLabel();
        this.jLabel2Period = new JLabel();
        this.jButton2Validate = new JButton();
        this.jButtonSort = new JButton();
        this.jScrollPane3 = new JScrollPane();
        this.tableModelMessages = new TableModelMessages(5, this);
        this.jTable3Receivers = new JTable(this.tableModelMessages);
        this.border1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Message");
        this.border2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Properties");
        this.border3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Summary");
        this.messagesServerGenericGui_jButton2Validate_actionAdapter = new MessagesServerGenericGui_jButton2Validate_actionAdapter(this);
        this.messagesServerGenericGui_jButtonSort_actionAdapter = new MessagesServerGenericGui_jButtonSort_actionAdapter(this);
        try {
            this.jbInit();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
        BusinessCore.getScenario().addPlayerTypeListener(this);
        BusinessCore.getScenario().addProgrammedInfoListener(this);
        ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters().getGeneralParameterSetupGui()).addChangeGeneralParametersListener(this);
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (e.getEvent() == 1) {
            this.jComboBox2Receivers.addItem(e.getPlayerType().getPlayerTypeName());
            this.jComboBox2inTable.addItem(e.getPlayerType().getPlayerTypeName());
        }
        else if (e.getEvent() == 0) {
            this.tableModelMessages.deletePlayerType(e.getPlayerType().getPlayerTypeName());
            this.jComboBox2Receivers.removeItem(e.getPlayerType().getPlayerTypeName());
            this.jComboBox2inTable.removeItem(e.getPlayerType().getPlayerTypeName());
        }
    }
    
    public void removeAllInfo() {
        this.tableModelMessages.removeAll();
        BusinessCore.getScenario().removeAllInformation();
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return "Communication";
    }
    
    public void generalParametersChanged(final ChangeGeneralParametersEvent e) {
        if (e.getSource() instanceof JSpinner) {
            if (((JSpinner)e.getSource()).getName().equalsIgnoreCase(BusinessCore.getGeneralParameters().getPeriodCountSpinner().getName())) {
                if (new Integer(this.jSpinner2Period.getValue().toString()) > BusinessCore.getGeneralParameters().getPeriodCount()) {
                    this.jSpinner2Period.setModel(new SpinnerNumberModel(BusinessCore.getGeneralParameters().getPeriodCount(), 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
                }
                else {
                    this.jSpinner2Period.setModel(new SpinnerNumberModel(new Integer(this.jSpinner2Period.getValue().toString()).intValue(), 1,BusinessCore.getGeneralParameters().getPeriodCount(), 1));
                }
                for (int i = 0; i < this.tableModelMessages.getRowCount(); ++i) {
                    if (Integer.parseInt(this.tableModelMessages.getValueAt(i, 2).toString()) > BusinessCore.getGeneralParameters().getPeriodCount()) {
                        this.tableModelMessages.deleteRow(i);
                        --i;
                    }
                }
            }
            if (((JSpinner)e.getSource()).getName().equalsIgnoreCase(BusinessCore.getGeneralParameters().getPeriodDurationSpinner().getName())) {
                if (new Integer(this.jSpinner2Time.getValue().toString()) > BusinessCore.getGeneralParameters().getPeriodDuration()) {
                    this.jSpinner2Time.setModel(new SpinnerNumberModel(BusinessCore.getGeneralParameters().getPeriodDuration(), 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
                }
                else {
                  
                    this.jSpinner2Time.setModel(new SpinnerNumberModel(new Integer (this.jSpinner2Time.getValue().toString()).intValue(), 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
                }
                for (int i = 0; i < this.tableModelMessages.getRowCount(); ++i) {
                    if (Integer.parseInt(this.tableModelMessages.getValueAt(i, 3).toString()) > BusinessCore.getGeneralParameters().getPeriodDuration()) {
                        this.tableModelMessages.deleteRow(i);
                        --i;
                    }
                }
            }
        }
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout0);
        this.jPanel1.setBorder(this.border1);
        this.jPanel1.setLayout(this.gridBagLayout1);
        this.jTextArea11Message.setBorder(BorderFactory.createEtchedBorder());
        this.jTextArea11Message.setMargin(new Insets(2, 2, 2, 2));
        this.jTextArea11Message.setText("Write here your message");
        this.jTextArea11Message.setRows(3);
        this.jTable3Receivers.addMouseListener(new MessagesServerGenericGui_jTable3Receivers_mouseAdapter(this));
        this.jComboBox2inTable.addMouseListener(new MessagesServerGenericGui_jComboBox2inTable_mouseAdapter(this));
        this.jButton2Validate.addActionListener(this.messagesServerGenericGui_jButton2Validate_actionAdapter);
        this.jButtonSort.addActionListener(this.messagesServerGenericGui_jButtonSort_actionAdapter);
        this.jButtonSort.setMaximumSize(new Dimension(33, 9));
        this.jButtonSort.setMinimumSize(new Dimension(33, 9));
        this.jButtonSort.setPreferredSize(new Dimension(33, 9));
        this.jButtonSort.setMargin(new Insets(2, 4, 2, 4));
        this.jButtonSort.setText("Sort");
        this.jButton2Validate.setMargin(new Insets(2, 4, 2, 4));
        this.jScrollPane1.getViewport().add(this.jTextArea11Message);
        this.jPanel2.setLayout(this.gridBagLayout2);
        this.jPanel2.setBorder(this.border2);
        this.jLabel2To.setHorizontalAlignment(4);
        this.jLabel2To.setText("To :");
        this.jLabel2Time.setHorizontalAlignment(4);
        this.jLabel2Time.setText("Time :");
        this.jLabel2Period.setHorizontalAlignment(4);
        this.jLabel2Period.setText("Period :");
        this.jSpinner2Time.setModel(new SpinnerNumberModel(0, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
        this.jSpinner2Period.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
        this.jButton2Validate.setText("Validate");
        this.jComboBox2Receivers.setMaximumRowCount(5);
        this.jComboBox2inTable.setMaximumRowCount(5);
        this.jComboBox2Receivers.addItem("All players");
        this.jComboBox2inTable.addItem("All players");
        this.jPanel3.setLayout(this.gridBagLayout3);
        this.jPanel3.setBorder(this.border3);
        this.jScrollPane3.getViewport().add(this.jTable3Receivers);
        this.jTable3Receivers.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.jComboBox2inTable));
        this.jTable3Receivers.getColumnModel().getColumn(2).setCellEditor(this.jSpinnerEditorPeriod);
        this.jTable3Receivers.getColumnModel().getColumn(3).setCellEditor(new JSpinnerEditor("time"));
        this.jTable3Receivers.getColumnModel().getColumn(4).setCellRenderer(new JButtonRenderer());
        this.jTable3Receivers.getColumnModel().getColumn(4).setCellEditor(new JButtonEditor(this.tableModelMessages));
        this.add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.3, 20.0, 11, 1, new Insets(5, 5, 0, 5), 1, 1));
        this.add(this.jPanel3, new GridBagConstraints(0, 2, 1, 1, 0.3, 60.0, 15, 1, new Insets(0, 5, 5, 5), 1, 1));
        this.jPanel1.add(this.jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 11, 1, new Insets(5, 5, 5, 5), 1, 1));
        this.jPanel3.add(this.jScrollPane3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 582, 56));
        this.add(this.jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.3, 0.0, 10, 1, new Insets(0, 5, 0, 5), 1, 1));
        this.jPanel2.add(this.jButton2Validate, new GridBagConstraints(7, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jSpinner2Period, new GridBagConstraints(4, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 3), 0, 0));
        this.jPanel2.add(this.jLabel2To, new GridBagConstraints(1, 0, 1, 1, 4.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jComboBox2Receivers, new GridBagConstraints(2, 0, 1, 1, 30.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jLabel2Period, new GridBagConstraints(3, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jLabel2Time, new GridBagConstraints(5, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jSpinner2Time, new GridBagConstraints(6, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 0), 0, 0));
        this.jPanel2.add(this.jButtonSort, new GridBagConstraints(8, 0, 1, 1, 10.0, 0.0, 10, 1, new Insets(0, 5, 5, 3), 0, 0));
    }
    
    public Vector getListMessages() {
        return BusinessCore.getScenario().getListInformation();
    }
    
    public void setUneditable() {
        this.jTextArea11Message.setEditable(false);
        this.jComboBox2Receivers.setEnabled(false);
        this.jSpinner2Time.setEnabled(false);
        this.jSpinner2Period.setEnabled(false);
        this.jButton2Validate.setEnabled(false);
        this.jButtonSort.setEnabled(false);
        this.jButton2Validate.removeActionListener(this.messagesServerGenericGui_jButton2Validate_actionAdapter);
        this.jButtonSort.removeActionListener(this.messagesServerGenericGui_jButtonSort_actionAdapter);
        this.tableModelMessages.setEditableCellsPossible(false);
    }
    
    public void setEditable() {
        this.jTextArea11Message.setEditable(true);
        this.jComboBox2Receivers.setEnabled(true);
        this.jSpinner2Time.setEnabled(true);
        this.jSpinner2Period.setEnabled(true);
        this.jButton2Validate.setEnabled(true);
        this.jButtonSort.setEnabled(true);
        this.jButton2Validate.addActionListener(this.messagesServerGenericGui_jButton2Validate_actionAdapter);
        this.jButtonSort.addActionListener(this.messagesServerGenericGui_jButtonSort_actionAdapter);
        this.tableModelMessages.setEditableCellsPossible(true);
    }
    
    void jTable3Receivers_mouseClicked(final MouseEvent e) {
        this.updateJpanel();
    }
    
    public void updateJpanel() {
        Utils.logger.info("Update messageServerGui");
        final int row = this.jTable3Receivers.getSelectedRow();
        this.jTextArea11Message.setText(this.jTable3Receivers.getValueAt(row, 0).toString());
        this.jComboBox2Receivers.setSelectedItem(this.jTable3Receivers.getValueAt(row, 1).toString());
        this.jSpinner2Period.setValue(Integer.valueOf((String)this.jTable3Receivers.getValueAt(row, 2)));
        this.jSpinner2Time.setValue(Integer.valueOf((String)this.jTable3Receivers.getValueAt(row, 3)));
    }
    
    public void jComboBox2inTable_mouseEntered(final MouseEvent e) {
        this.updateJpanel();
    }
    
    public void programmedInfoModified(final ProgrammedInfoEvent e) {
        if (e.getEvent() == 2) {
            this.tableModelMessages.addRow((String[])e.getProgrammedInfoObject());
        }
        if (e.getEvent() == 0) {
            this.tableModelMessages.deleteRow((int)e.getProgrammedInfoObject());
        }
        if (e.getEvent() == 3) {
            this.tableModelMessages.removeAll();
            for (int i = 0; i < ((Vector)e.getProgrammedInfoObject()).size(); ++i) {
                this.tableModelMessages.addRow((String[]) ((Vector)e.getProgrammedInfoObject()).get(i));
            }
        }
        if (e.getEvent() == 1) {
            this.tableModelMessages.removeAll();
        }
    }
    
    public void jButton2Validate_actionPerformed(final ActionEvent e) {
        this.message = new String[] { this.jTextArea11Message.getText(), this.jComboBox2Receivers.getSelectedItem().toString(), this.jSpinner2Period.getValue().toString(), this.jSpinner2Time.getValue().toString() };
        if (BusinessCore.getScenario().getListInformation().size() == 0) {
            this.validateMessage(this.message);
        }
        else if (this.checkMessage(this.message, -1)) {
            this.validateMessage(this.message);
        }
    }
    
    public void jButtonSort_actionPerformed(final ActionEvent e) {
        Utils.logger.info("Sort the list of messages");
        final Vector listSorted = new Vector();
        final Vector listInformation = (Vector)BusinessCore.getScenario().getListInformation().clone();
        final int size = listInformation.size();
        for (int i = size - 1; i >= 0; --i) {
            String[] temp = (String[]) listInformation.get(i);
            int index = i;
            for (int j = i; j >= 0; --j) {
                final boolean ComparePeriod = Integer.parseInt(((String[])listInformation.get(j))[2]) < Integer.parseInt(temp[2]);
                if (ComparePeriod || (Integer.parseInt(((String[])listInformation.get(j))[2]) == Integer.parseInt(temp[2]) & Integer.parseInt(((String[])listInformation.get(j))[3]) < Integer.parseInt(temp[3]))) {
                    temp = (String[]) listInformation.get(j);
                    index = j;
                }
            }
            listSorted.add(new String[] { temp[0], temp[1], temp[2], temp[3] });
            listInformation.remove(index);
        }
        BusinessCore.getScenario().removeAllInformation();
        for (int sizeList = listSorted.size(), k = 0; k < sizeList; ++k) {
            BusinessCore.getScenario().addInformation((String[]) listSorted.get(k));
        }
    }
    
    public boolean checkMessage(final String[] newMessage, final int row) {
        Vector listCheckInfo = new Vector();
        listCheckInfo = (Vector)BusinessCore.getScenario().getListInformation().clone();
        boolean IsMessageGood = true;
        for (int i = 0; i < listCheckInfo.size() && IsMessageGood; ++i) {
            if (row != i && (((String[])listCheckInfo.get(i))[1].equals(newMessage[1]) || ((String[])listCheckInfo.get(i))[1].equals("All players") || newMessage[1].equals("All players")) && ((String[])listCheckInfo.get(i))[2].equals(newMessage[2]) && ((String[])listCheckInfo.get(i))[3].equals(newMessage[3])) {
                JOptionPane.showMessageDialog(this, "You cannot create two messages which \nwill be sent at the same category \nof player at the same time. Please, \nmodify your message.", "Message Warning", 2);
                IsMessageGood = false;
            }
        }
        return IsMessageGood;
    }
    
    private void validateMessage(final String[] newMessage) {
        BusinessCore.getScenario().addInformation(newMessage);
    }
}
