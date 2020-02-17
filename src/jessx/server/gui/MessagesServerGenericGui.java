package jessx.server.gui;

/***************************************************************/
/*                     SOFTWARE SECTION                        */
/***************************************************************/
/*
 * <p>Name: Jessx</p>
 * <p>Description: Financial Market Simulation Software</p>
 * <p>Licence: GNU General Public License</p>
 * <p>Organisation: EC Lille / USTL</p>
 * <p>Persons involved in the project : group T.E.A.M.</p>
 * <p>More details about this source code at :
 *    http://eleves.ec-lille.fr/~ecoxp03  </p>
 * <p>Current version: 1.0</p>
 */

/***************************************************************/
/*                      LICENCE SECTION                        */
/***************************************************************/
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/***************************************************************/
/*                       IMPORT SECTION                        */
/***************************************************************/

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import jessx.business.*;
import jessx.business.event.*;
import jessx.utils.*;
import jessx.utils.gui.*;


/***************************************************************/
/*         MessagesServerGenericGui CLASS SECTION              */
/***************************************************************/
/**
 * <p>Title : MessagesServerGenericGui</p>
 * <p>Description : </p>
 * @authors: Grosjean Christophe , Charles Montez
 * @version 1.0
 */

public class MessagesServerGenericGui
    extends JPanel implements DisplayableNode, PlayerTypeListener, ProgrammedInfoListener, ChangeGeneralParametersListener {

// Real content
  String[] message = new String[] {"", "", "", ""};

// Graphical objects
  // 3 panels principaux :(les indices marquent l'appartenance des objets aux panels)
  JPanel jPanel1 = new JPanel(); // "Saisie du message"
  JPanel jPanel2 = new JPanel(); // "Parametres d'envoi"
  JPanel jPanel3 = new JPanel(); // "Historique des message crees"

  GridBagLayout gridBagLayout0 = new GridBagLayout(); // celui du panel this
  GridBagLayout gridBagLayout1 = new GridBagLayout(); // celui du panel 1
  GridBagLayout gridBagLayout2 = new GridBagLayout(); // celui du panel 2
  GridBagLayout gridBagLayout3 = new GridBagLayout(); // panel 3

  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea11Message = new JTextArea();

  JSpinner jSpinner2Time = new JSpinner();
  JSpinner jSpinner2Period = new JSpinner();
  JSpinnerEditor jSpinnerEditorPeriod=new JSpinnerEditor("period");

  JComboBox jComboBox2Receivers = new JComboBox();
  JComboBox jComboBox2inTable = new JComboBox();
  JLabel jLabel2To = new JLabel();
  JLabel jLabel2Time = new JLabel();
  JLabel jLabel2Period = new JLabel();
  JButton jButton2Validate = new JButton();
  JButton jButtonSort = new JButton();

  JScrollPane jScrollPane3 = new JScrollPane();
  TableModelMessages tableModelMessages = new TableModelMessages(5, (JPanel) this); //Five columns
  JTable jTable3Receivers = new JTable(tableModelMessages);

  //borders
  Border border1 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
      white,
      new Color(156, 156, 158)), "Message");
  Border border2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
      white,
      new Color(156, 156, 158)), "Properties");
  Border border3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.
      white,
      new Color(156, 156, 158)), "Summary");

  MessagesServerGenericGui_jButton2Validate_actionAdapter
      messagesServerGenericGui_jButton2Validate_actionAdapter =
      new MessagesServerGenericGui_jButton2Validate_actionAdapter(this);

  MessagesServerGenericGui_jButtonSort_actionAdapter
    messagesServerGenericGui_jButtonSort_actionAdapter =
    new MessagesServerGenericGui_jButtonSort_actionAdapter(this);


  /**
   * Allow this panel to be displayed and warn the user if it can't.
   */
  public MessagesServerGenericGui() {

    // Graphic initialization
    try {
      jbInit();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    BusinessCore.getScenario().addPlayerTypeListener(this);
    BusinessCore.getScenario().addProgrammedInfoListener(this);
    ((GeneralParameterSetupGui)BusinessCore.getGeneralParameters().getGeneralParameterSetupGui()).addChangeGeneralParametersListener(this);
  }

  /**
   * Changes the jComboBox2Receivers if their is a new PlayerTypeEvent
   * @param e PlayerTypeEvent (PLAYER_ADDED or PLAYER_REMOVED) which indicates
   * whether a new player type has been created or removed
   * @see PlayerTypeListener
   */
  public void playerTypeModified(PlayerTypeEvent e) {
    if (e.getEvent() == PlayerTypeEvent.PLAYER_ADDED) {
      this.jComboBox2Receivers.addItem(e.getPlayerType().getPlayerTypeName());
      this.jComboBox2inTable.addItem(e.getPlayerType().getPlayerTypeName());
    }
    else if (e.getEvent() == PlayerTypeEvent.PLAYER_REMOVED) {
      tableModelMessages.deletePlayerType(e.getPlayerType().getPlayerTypeName());
      this.jComboBox2Receivers.removeItem(e.getPlayerType().getPlayerTypeName());
      this.jComboBox2inTable.removeItem(e.getPlayerType().getPlayerTypeName());
    }
  }

  /**
   * Remove each piece of info from the scenario and the table.
   */
  public void removeAllInfo() {
    tableModelMessages.removeAll();
    jessx.business.BusinessCore.getScenario().removeAllInformation();
  }

  /**
   * Return the created panel.
   * @return The panel created
   * @see DisplayableNode
   */
  public JPanel getPanel() {
    return this;
  }

  /**
   * Return the name of the created panel.
   * @return The name of the panel created
   * @see DisplayableNode
   */
  public String toString() {
    return "Communication";
  }

  public void generalParametersChanged(ChangeGeneralParametersEvent e) {
    if (e.getSource() instanceof JSpinner) {
      if ( ( (JSpinner) e.getSource()).getName().equalsIgnoreCase(BusinessCore.
          getGeneralParameters().getPeriodCountSpinner().getName())) {
        if (new Integer(this.jSpinner2Period.getValue().toString()).intValue() > BusinessCore.getGeneralParameters().getPeriodCount()) {
          this.jSpinner2Period.setModel(new SpinnerNumberModel(BusinessCore.getGeneralParameters().getPeriodCount(),1,BusinessCore.getGeneralParameters().getPeriodCount(), 1));
        }
        else {
          this.jSpinner2Period.setModel(new SpinnerNumberModel(new Integer(this.jSpinner2Period.getValue().toString()).intValue(), 1,BusinessCore.getGeneralParameters().getPeriodCount(), 1));
        }
        for (int i = 0; i < tableModelMessages.getRowCount(); i++) {
          if (Integer.parseInt(tableModelMessages.getValueAt(i, 2).toString()) >
              BusinessCore.getGeneralParameters().getPeriodCount()) {
            tableModelMessages.deleteRow(i);
            i--;
          }
        }
      }
      if (((JSpinner)e.getSource()).getName().equalsIgnoreCase(BusinessCore.getGeneralParameters().getPeriodDurationSpinner().getName())) {
        if (new Integer (this.jSpinner2Time.getValue().toString()).intValue() > BusinessCore.getGeneralParameters().getPeriodDuration()) {
          this.jSpinner2Time.setModel(new SpinnerNumberModel(BusinessCore.getGeneralParameters().getPeriodDuration(), 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
        }
        else {
          this.jSpinner2Time.setModel(new SpinnerNumberModel(new Integer (this.jSpinner2Time.getValue().toString()).intValue(), 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
        }
        for (int i = 0; i < tableModelMessages.getRowCount(); i++) {
          if (Integer.parseInt(tableModelMessages.getValueAt(i, 3).toString()) >
              BusinessCore.getGeneralParameters().getPeriodDuration()) {
            tableModelMessages.deleteRow(i);
            i--;
          }
        }
      }
    }
  }

  // Initialization
  /**
   * Constructs the appearance of the panel.
   * @throws Exception
   */
  private void jbInit() throws Exception {
    this.setLayout(gridBagLayout0);

    jPanel1.setBorder(border1);
    jPanel1.setLayout(gridBagLayout1);

    jTextArea11Message.setBorder(BorderFactory.createEtchedBorder());
    jTextArea11Message.setMargin(new Insets(2, 2, 2, 2));
    jTextArea11Message.setText("Write here your message");
    jTextArea11Message.setRows(3);

    jTable3Receivers.addMouseListener(new MessagesServerGenericGui_jTable3Receivers_mouseAdapter(this));
    jComboBox2inTable.addMouseListener(new MessagesServerGenericGui_jComboBox2inTable_mouseAdapter(this));
    jButton2Validate.addActionListener(
        messagesServerGenericGui_jButton2Validate_actionAdapter);
    jButtonSort.addActionListener(
        messagesServerGenericGui_jButtonSort_actionAdapter);
    jButtonSort.setMaximumSize(new Dimension(33, 9));
    jButtonSort.setMinimumSize(new Dimension(33, 9));
    jButtonSort.setPreferredSize(new Dimension(33, 9));
    jButtonSort.setMargin(new Insets(2, 4, 2, 4));
    jButtonSort.setText("Sort");
    jButton2Validate.setMargin(new Insets(2, 4, 2, 4));
    jScrollPane1.getViewport().add(jTextArea11Message);
    jPanel2.setLayout(gridBagLayout2);
    jPanel2.setBorder(border2);

    jLabel2To.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2To.setText("To :");

    jLabel2Time.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2Time.setText("Time :");
    jLabel2Period.setHorizontalAlignment(SwingConstants.RIGHT);
    jLabel2Period.setText("Period :");

    jSpinner2Time.setModel(new SpinnerNumberModel(0, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
    jSpinner2Period.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));

    jButton2Validate.setText("Validate");


    jComboBox2Receivers.setMaximumRowCount(5);
    jComboBox2inTable.setMaximumRowCount(5);
    jComboBox2Receivers.addItem("All players");
    jComboBox2inTable.addItem("All players");

    jPanel3.setLayout(gridBagLayout3);
    jPanel3.setBorder(border3);

    jScrollPane3.getViewport().add(jTable3Receivers);
    jTable3Receivers.getColumnModel().getColumn(1).setCellEditor(new
        DefaultCellEditor(jComboBox2inTable));
    jTable3Receivers.getColumnModel().getColumn(2).setCellEditor(jSpinnerEditorPeriod);
    jTable3Receivers.getColumnModel().getColumn(3).setCellEditor(new
        JSpinnerEditor("time"));
    jTable3Receivers.getColumnModel().getColumn(4).setCellRenderer(new
        JButtonRenderer());
    jTable3Receivers.getColumnModel().getColumn(4).setCellEditor(new
        JButtonEditor(tableModelMessages));

    this.add(jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.3, 20.0
                                             , GridBagConstraints.NORTH,
                                             GridBagConstraints.BOTH,
                                             new Insets(5, 5, 0, 5), 1, 1));
    this.add(jPanel3, new GridBagConstraints(0, 2, 1, 1, 0.3, 60.0
                                             , GridBagConstraints.SOUTH,
                                             GridBagConstraints.BOTH,
                                             new Insets(0, 5, 5, 5), 1, 1));

    jPanel1.add(jScrollPane1, new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0
        , GridBagConstraints.NORTH, GridBagConstraints.BOTH,
        new Insets(5, 5, 5, 5), 1, 1));

    jPanel3.add(jScrollPane3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 0, 0, 0), 582, 56));
    this.add(jPanel2, new GridBagConstraints(0, 1, 1, 1, 0.3, 0.0
                                             , GridBagConstraints.CENTER,
                                             GridBagConstraints.BOTH,
                                             new Insets(0, 5, 0, 5), 1, 1));
    jPanel2.add(jButton2Validate, new GridBagConstraints(7, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jSpinner2Period, new GridBagConstraints(4, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 3), 0, 0));
    jPanel2.add(jLabel2To, new GridBagConstraints(1, 0, 1, 1, 4.0, 0.0
                                                  , GridBagConstraints.CENTER,
                                                  GridBagConstraints.BOTH,
                                                  new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jComboBox2Receivers,
                new GridBagConstraints(2, 0, 1, 1, 30.0, 0.0
                                       , GridBagConstraints.CENTER,
                                       GridBagConstraints.BOTH,
                                       new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jLabel2Period, new GridBagConstraints(3, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jLabel2Time, new GridBagConstraints(5, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jSpinner2Time, new GridBagConstraints(6, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 0), 0, 0));
    jPanel2.add(jButtonSort, new GridBagConstraints(8, 0, 1, 1, 10.0, 0.0
        , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
        new Insets(0, 5, 5, 3), 0, 0));
  }

  /**
   * Return the list of messages that must be sent during the session.
   * @return Vector
   */
  public Vector getListMessages() {
    return
     jessx.business.BusinessCore.getScenario().getListInformation();
  }

  /**
   * Prevent the panel from being edited.
   */
  public void setUneditable() {
    jTextArea11Message.setEditable(false);
    jComboBox2Receivers.setEnabled(false);
    jSpinner2Time.setEnabled(false);
    jSpinner2Period.setEnabled(false);
    jButton2Validate.setEnabled(false);
    jButtonSort.setEnabled(false);
    jButton2Validate.removeActionListener(
        messagesServerGenericGui_jButton2Validate_actionAdapter);
    jButtonSort.removeActionListener(
        messagesServerGenericGui_jButtonSort_actionAdapter);
    tableModelMessages.setEditableCellsPossible(false);
  }

  /**
   * Allow the panel to be editable.
   */
  public void setEditable() {
    jTextArea11Message.setEditable(true);
    jComboBox2Receivers.setEnabled(true);
    jSpinner2Time.setEnabled(true);
    jSpinner2Period.setEnabled(true);
    jButton2Validate.setEnabled(true);
    jButtonSort.setEnabled(true);
    jButton2Validate.addActionListener(
        messagesServerGenericGui_jButton2Validate_actionAdapter);
    jButtonSort.addActionListener(
        messagesServerGenericGui_jButtonSort_actionAdapter);
    tableModelMessages.setEditableCellsPossible(true);
  }

  /**
   * Verify if the user has clicked on the table.
   * @param e MouseEvent
   */
  void jTable3Receivers_mouseClicked(MouseEvent e) {
    this.updateJpanel();
  }

  /**
   */
  public void updateJpanel() {
    Utils.logger.info("Update messageServerGui");
    int row = this.jTable3Receivers.getSelectedRow();
    this.jTextArea11Message.setText(jTable3Receivers.getValueAt(row, 0).
                                    toString());
    this.jComboBox2Receivers.setSelectedItem(jTable3Receivers.getValueAt(row, 1).
                                             toString());
    this.jSpinner2Period.setValue( (Number) ( (Integer.valueOf( (String)this.
        jTable3Receivers.getValueAt(row, 2)))));
    this.jSpinner2Time.setValue( (Number) ( (Integer.valueOf( (String)this.
        jTable3Receivers.getValueAt(row, 3)))));
  }

  /**
   * @param e MouseEvent
   */
  public void jComboBox2inTable_mouseEntered(MouseEvent e) {
    this.updateJpanel();
  }

  /**
   * @param e ProgrammedInfoEvent
   */
  public void programmedInfoModified(ProgrammedInfoEvent e) {

    if (e.getEvent() == ProgrammedInfoEvent.PROGRAMMEDINFO_ADDED) {
        tableModelMessages.addRow( (String[]) (e.getProgrammedInfoObject()));
    }
    if (e.getEvent() == ProgrammedInfoEvent.PROGRAMMEDINFO_REMOVED) {
        tableModelMessages.deleteRow( ( (Integer) e.getProgrammedInfoObject()).intValue() );
    }
    if (e.getEvent() == ProgrammedInfoEvent.PROGRAMMEDINFO_LISTLOADED) {
      tableModelMessages.removeAll();
      for (int i = 0; i < ( (Vector) e.getProgrammedInfoObject()).size(); i++) {
        tableModelMessages.addRow( (String[]) ( (Vector) (e.getProgrammedInfoObject())).get(i));
      }
    }
    if (e.getEvent() == ProgrammedInfoEvent.PROGRAMMEDINFO_ALLREMOVED) {
        tableModelMessages.removeAll();  }
    }
  /**
   * The message saved in jButton2Validate_actionPerformed is added to the TableModel
   * @param e ActionEvent when you click in the button to save message
   * @see jButton2Validate_actionPerformed
   */

  public void jButton2Validate_actionPerformed(ActionEvent e) {

    message = new String[] {
            jTextArea11Message.getText(),
            jComboBox2Receivers.getSelectedItem().toString(),
            jSpinner2Period.getValue().toString(),
         jSpinner2Time.getValue().toString() };

    if ( ( (Vector) jessx.business.BusinessCore.getScenario().
          getListInformation()).size() == 0) {
      validateMessage(message);
    }
    else if (checkMessage(message,-1)) {
      validateMessage(message);}

    }

    /**
     * This method is called whe you clik on the "sort" button. It enables to sort
     * the list of messages in the tablemodel.
     * @param e ActionEvent
     */
    public void jButtonSort_actionPerformed(ActionEvent e) {
      Utils.logger.info("Sort the list of messages");
      Vector listSorted = new Vector();
      Vector listInformation =(Vector) BusinessCore.getScenario().getListInformation().clone();

      int size = listInformation.size();
      for (int i = size - 1; i >= 0; i--) {
        String[] temp = (String[]) listInformation.get(i);
        int index = i;
        for (int j = i; j >= 0; j--) {
          boolean ComparePeriod = (Integer.parseInt( ( (String[]) listInformation.
              get(j))[2]) < Integer.parseInt(temp[2]));
          if (ComparePeriod ||
              ( (Integer.parseInt( ( (String[]) listInformation.get(j))[2]) ==
                 Integer.parseInt(temp[2])) &
               (Integer.parseInt( ( (String[]) listInformation.get(j))[3]) <
                Integer.parseInt(temp[3])))) {
            temp = (String[]) listInformation.get(j);
            index = j;
          }
        }
        listSorted.add(new String[] {temp[0], temp[1], temp[2], temp[3]});
        listInformation.remove(index);
      }
        BusinessCore.getScenario().removeAllInformation();
        int sizeList = listSorted.size();
        for (int k = 0; k < sizeList; k++) {
          BusinessCore.getScenario().addInformation( (String[])
              listSorted.get(k));
        }

    }


  //test to prevent the use from programming a message sent at same time and period than another one
  public boolean checkMessage(String[] newMessage,int row)  {
    Vector listCheckInfo = new Vector();
    listCheckInfo = (Vector) jessx.business.BusinessCore.getScenario().getListInformation().clone();
    boolean IsMessageGood = true;
    int i = 0;

    while ((i < listCheckInfo.size()) && (IsMessageGood)) {
      if (row != i) {
        if (
            (
                (( ( (String[]) listCheckInfo.get(i))[1]).equals(newMessage[1])) //same receiver
                ||
                (( ( (String[]) listCheckInfo.get(i))[1]).equals("All players")) //same receiver
                ||
                ((newMessage[1]).equals("All players"))//same receiver
            )
            &&
            (( ( (String[]) listCheckInfo.get(i))[2]).equals(newMessage[2])) //same period
            &&
            (( ( (String[]) listCheckInfo.get(i))[3]).equals(newMessage[3])) //same time in period
            ) {
          JOptionPane.showMessageDialog(this,
                                        "You cannot create two messages which \nwill be sent at the same category \nof player at the same time. Please, \nmodify your message.",
                                        "Message Warning",
                                        JOptionPane.WARNING_MESSAGE);
          IsMessageGood = false;
        }
      }

      i++;
    }
       return IsMessageGood;

    }



  private void validateMessage(String[] newMessage) {
    jessx.business.BusinessCore.getScenario().addInformation(newMessage); // It is said to the instance of the scenario

  }
}



/***************************************************************/
/*                   Event CLASSES SECTION                     */
/***************************************************************/
class MessagesServerGenericGui_jComboBox2inTable_mouseAdapter
    extends MouseAdapter {
  private MessagesServerGenericGui adaptee;
  MessagesServerGenericGui_jComboBox2inTable_mouseAdapter(
      MessagesServerGenericGui adaptee) {
    this.adaptee = adaptee;
  }

  public void mouseEntered(MouseEvent e) {
    adaptee.jComboBox2inTable_mouseEntered(e);
  }
}

class MessagesServerGenericGui_jButton2Validate_actionAdapter
    implements ActionListener {
  private MessagesServerGenericGui adaptee;
  MessagesServerGenericGui_jButton2Validate_actionAdapter(
      MessagesServerGenericGui adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2Validate_actionPerformed(e);
  }
}

class MessagesServerGenericGui_jButtonSort_actionAdapter
    implements ActionListener {
  private MessagesServerGenericGui adaptee;
  MessagesServerGenericGui_jButtonSort_actionAdapter(
      MessagesServerGenericGui adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonSort_actionPerformed(e);
  }
}

class MessagesServerGenericGui_jTable3Receivers_mouseAdapter
    extends java.awt.event.MouseAdapter {
  MessagesServerGenericGui adaptee;

  /**
   * Replace the value of adaptee by one specified by the user.
   * @param adaptee MessagesServerGenericGui
   */
  MessagesServerGenericGui_jTable3Receivers_mouseAdapter(
      MessagesServerGenericGui adaptee) {
    this.adaptee = adaptee;
  }

  /**
   * Verify if the user has clicked on the table
   * @param e MouseEvent
   */
  public void mouseClicked(MouseEvent e) {
    adaptee.jTable3Receivers_mouseClicked(e);
  }
}

/***************************************************************/
/*               JButtonEditor CLASS SECTION                  */
/***************************************************************/

class JButtonEditor
    extends AbstractCellEditor implements TableCellEditor, ActionListener {
  JButton button;
  TableModelMessages tableModelMessages1;

  /**
   * Add a button to the table.
   * @param tableModelMessages TableModelMessages
   */
  public JButtonEditor(TableModelMessages tableModelMessages) {
    button = new JButton();
    tableModelMessages1 = tableModelMessages;

  }

  /**
   * Execute the method which
   * allow to remove a row from the table.
   * @param e ActionEvent
   */
  public void actionPerformed(ActionEvent e) {
    //"e.getActionCommand()" contains the row where the button "delete" was clicked
    BusinessCore.getScenario().removeInformation( Integer.parseInt(e.getActionCommand().
        toString()) );
    }

  /**
   * Return the button.
   * @return Object
   */
  public Object getCellEditorValue() {
    return button;
  }

  /**
   * Return the button.
   * @param table JTable
   * @param jbutton Object
   * @param isSelected boolean
   * @param row int
   * @param column int
   * @return Component
   */
  public Component getTableCellEditorComponent(JTable table, Object jbutton,
                                               boolean isSelected, int row,
                                               int column) {
    button = (JButton) jbutton;
    if (button != null) {
      button.setActionCommand(Integer.toString(row));
      button.addActionListener(this);
    }
    return button;
  }
}



/***************************************************************/
/*               JSpinnerEditor Classe SECTION                  */
/***************************************************************/

class JSpinnerEditor extends AbstractCellEditor implements TableCellEditor {
  JSpinner jspinner;

  /**
   * Add a jspinner to the table.
   * @param type String
   */
  public JSpinnerEditor(String type) {
    jspinner = new JSpinner();
    if (type.equals("time")) {
      jspinner.setModel(new SpinnerNumberModel(1, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
    }
    else {
      jspinner.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
    }
  }


  /**
   * Return the jspinner.
   * @return Object
   */
  public Object getCellEditorValue() {
    return jspinner;
  }

  /**
   * Return the jspinner.
   * @param table JTable
   * @param jbutton Object
   * @param isSelected boolean
   * @param row int
   * @param column int
   * @return Component
   */
  public Component getTableCellEditorComponent(JTable table, Object jbutton,
                                               boolean isSelected, int row,
                                               int column) {
    if (column==2){
      jspinner.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
      jspinner.setValue( (Number) ( (Integer.valueOf((String)table.getValueAt(row, column)))));
    }
    if (column == 3) {
      jspinner.setModel(new SpinnerNumberModel(1, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
      jspinner.setValue( (Number) ( (Integer.valueOf((String)table.getValueAt(row, column)))));
    }
    return jspinner;
  }
}

