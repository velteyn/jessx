// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.net.NetworkWritable;
import jessx.net.Information;
import java.awt.event.MouseEvent;
import jessx.business.event.PlayerTypeEvent;
import org.jdom.Content;
import org.jdom.Element;
import java.awt.GridBagConstraints;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import java.awt.event.MouseListener;
import java.awt.Component;
import java.awt.Insets;
import java.awt.LayoutManager;
import jessx.server.net.NetworkCore;
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
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import jessx.server.net.event.ExperimentStateListener;
import jessx.utils.XmlExportable;
import jessx.business.event.PlayerTypeListener;
import javax.swing.JPanel;

public class CommunicationGui extends JPanel implements PlayerTypeListener, XmlExportable, ExperimentStateListener
{
    String[] chatToBeSaved;
    JPanel jPanel1;
    JPanel jPanel2;
    JPanel jPanel3;
    GridBagLayout gridBagLayout0;
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    GridBagLayout gridBagLayout3;
    JScrollPane jScrollPane1;
    JTextArea jTextArea11Message;
    JComboBox jComboBox2Receivers;
    JComboBox jComboBox2inTable;
    JLabel jLabel2To;
    JButton jButton2Send;
    JLabel jLabelSpace;
    JScrollPane jScrollPane3;
    TableModelMessages tableModelMessages;
    JTable jTable3Receivers;
    CommuncationGui_jTable3Receivers_mouseAdapter communcationGui_jTable3Receivers_mouseAdapter;
    Border border1;
    Border border2;
    Border border3;
    String[] message;
    CommunicationGui_jButton2Send_mouseAdapter1 communicationGui_jButton2Send_mouseAdapter;
    
    public CommunicationGui() {
        this.jPanel1 = new JPanel();
        this.jPanel2 = new JPanel();
        this.jPanel3 = new JPanel();
        this.gridBagLayout0 = new GridBagLayout();
        this.gridBagLayout1 = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.gridBagLayout3 = new GridBagLayout();
        this.jScrollPane1 = new JScrollPane();
        this.jTextArea11Message = new JTextArea();
        this.jComboBox2Receivers = new JComboBox();
        this.jComboBox2inTable = new JComboBox();
        this.jLabel2To = new JLabel();
        this.jButton2Send = new JButton();
        this.jLabelSpace = new JLabel();
        this.jScrollPane3 = new JScrollPane();
        this.tableModelMessages = new TableModelMessages(4, this);
        this.jTable3Receivers = new JTable(this.tableModelMessages);
        this.communcationGui_jTable3Receivers_mouseAdapter = new CommuncationGui_jTable3Receivers_mouseAdapter(this);
        this.border1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Message");
        this.border2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Properties");
        this.border3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Summary");
        this.message = new String[] { "", "", "", "" };
        this.communicationGui_jButton2Send_mouseAdapter = new CommunicationGui_jButton2Send_mouseAdapter1(this);
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        BusinessCore.getScenario().addPlayerTypeListener(this);
        NetworkCore.getExperimentManager().addExperimentStateListener(this);
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout0);
        this.jPanel1.setBorder(this.border1);
        this.jPanel1.setLayout(this.gridBagLayout1);
        this.jTextArea11Message.setBorder(BorderFactory.createEtchedBorder());
        this.jTextArea11Message.setMargin(new Insets(2, 2, 2, 2));
        this.jTextArea11Message.setText("Write here your message");
        this.jTextArea11Message.setRows(3);
        this.jLabelSpace.setText(" ");
        this.jScrollPane1.getViewport().add(this.jTextArea11Message);
        this.jPanel2.setLayout(this.gridBagLayout2);
        this.jPanel2.setBorder(this.border2);
        this.jLabel2To.setHorizontalAlignment(4);
        this.jLabel2To.setText("To :");
        this.jTable3Receivers.addMouseListener(this.communcationGui_jTable3Receivers_mouseAdapter);
        this.tableModelMessages.setEditableCellsPossible(false);
        this.jButton2Send.setEnabled(false);
        this.jButton2Send.setText("Send");
        this.jComboBox2Receivers.setEnabled(false);
        this.jComboBox2Receivers.setMaximumRowCount(5);
        this.jComboBox2Receivers.addItem("All players");
        this.jComboBox2inTable.setMaximumRowCount(5);
        this.jComboBox2inTable.addItem("All players");
        this.jPanel3.setLayout(this.gridBagLayout3);
        this.jPanel3.setBorder(this.border3);
        this.jScrollPane3.getViewport().add(this.jTable3Receivers);
        this.jTable3Receivers.getColumnModel().getColumn(1).setCellEditor(new DefaultCellEditor(this.jComboBox2inTable));
        this.add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.3, 20.0, 11, 1, new Insets(5, 5, 0, 5), 1, 1));
        this.add(this.jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.3, 20.0, 10, 1, new Insets(0, 5, 0, 5), 1, 1));
        this.add(this.jPanel3, new GridBagConstraints(0, 2, 1, 1, 0.3, 60.0, 15, 1, new Insets(0, 5, 5, 5), 1, 1));
        this.jPanel1.add(this.jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 11, 1, new Insets(5, 5, 5, 5), 1, 1));
        this.jPanel3.add(this.jScrollPane3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 582, 56));
        this.jPanel2.add(this.jLabel2To, new GridBagConstraints(1, 0, 1, 1, 5.0, 0.0, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
        this.jPanel2.add(this.jButton2Send, new GridBagConstraints(4, 0, 1, 1, 15.0, 0.0, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
        this.jPanel2.add(this.jComboBox2Receivers, new GridBagConstraints(2, 0, 1, 1, 40.0, 0.0, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
        this.jPanel2.add(this.jLabelSpace, new GridBagConstraints(3, 0, 1, 1, 40.0, 0.0, 10, 1, new Insets(0, 5, 5, 5), 0, 0));
    }
    
    public void saveToXml(final Element parentNode) {
        final Element messageNode = new Element("ChatMessage");
        messageNode.setAttribute("Subject", this.chatToBeSaved[0]);
        messageNode.setAttribute("Receivers", this.chatToBeSaved[1]);
        messageNode.setAttribute("Period", this.chatToBeSaved[2]);
        messageNode.setAttribute("Time", this.chatToBeSaved[3]);
        parentNode.addContent(messageNode);
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
    
    public void experimentStateChanged(final int newState) {
        if (newState == 0) {
            this.setCommunicationGuiAllUneditable();
        }
        else {
            this.setCommunicationGuiAllEditable();
        }
    }
    
    public void jButton2Send_mouseClicked(final MouseEvent e) {
        final int experimentState = NetworkCore.getExperimentManager().getExperimentState();
        NetworkCore.getExperimentManager();
        if (experimentState != 0) {
            final int experimentState2 = NetworkCore.getExperimentManager().getExperimentState();
            NetworkCore.getExperimentManager();
            if (experimentState2 == 2) {
                this.chatToBeSaved = new String[] { this.message[0], this.message[1], Integer.toString(NetworkCore.getExperimentManager().getPeriodNum() + 1), Float.toString((float)(Math.round((float)(NetworkCore.getExperimentManager().getTimeInPeriod() / 100L)) / 10)) };
                this.tableModelMessages.addRow(this.chatToBeSaved);
                this.saveToXml(BusinessCore.getElementToSaveChat());
            }
            else {
                this.chatToBeSaved = new String[] { this.message[0], this.message[1], "Period off", "No time" };
                this.tableModelMessages.addRow(this.chatToBeSaved);
                this.saveToXml(BusinessCore.getElementToSaveChat());
            }
        }
        else {
            this.chatToBeSaved = new String[] { this.message[0], this.message[1], "Experiment off", "No time" };
            this.tableModelMessages.addRow(this.chatToBeSaved);
        }
        if ("All players".equals(this.message[1])) {
            NetworkCore.sendToAllPlayers(new Information(this.message[0]));
        }
        else {
            NetworkCore.sendToPlayerCategory(new Information(this.message[0]), this.message[1]);
        }
    }
    
    public void jButton2Send_mouseEntered(final MouseEvent e) {
        this.message = new String[] { this.jTextArea11Message.getText(), this.jComboBox2Receivers.getSelectedItem().toString(), "", "" };
    }
    
    private void setCommunicationGuiAllUneditable() {
        this.jComboBox2Receivers.setEnabled(false);
        this.jButton2Send.setEnabled(false);
        this.jButton2Send.removeMouseListener(this.communicationGui_jButton2Send_mouseAdapter);
        this.tableModelMessages.setEditableCellsPossible(false);
    }
    
    private void setCommunicationGuiAllEditable() {
        this.jComboBox2Receivers.setEnabled(true);
        this.jButton2Send.setEnabled(true);
        if (this.jButton2Send.getMouseListeners().length == 1) {
            this.jButton2Send.addMouseListener(this.communicationGui_jButton2Send_mouseAdapter);
        }
        this.tableModelMessages.setEditableCellsPossible(true);
    }
    
    void jTable3Receivers_mouseClicked(final MouseEvent e) {
        final int row = this.jTable3Receivers.getSelectedRow();
        this.jTextArea11Message.setText(this.jTable3Receivers.getValueAt(row, 0).toString());
        this.jComboBox2Receivers.setSelectedItem(this.jTable3Receivers.getValueAt(row, 1).toString());
    }
}
