package jessx.gclient.gui;

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

import jessx.business.*;
import jessx.client.*;
import jessx.utils.*;


/***************************************************************/
/*            OperationPane CLASS SECTION                      */
/***************************************************************/
/**
 * <p>Title : OperationPane</p>
 * <p>Description :</p>
 * @author Thierry Curtil
 * @version 1.0
 */

public class OperationPane extends JPanel {

  private Operator operator;
  private Hashtable operationsPanels = new Hashtable(); // key: operations name, value: JPanel associated

  private ButtonGroup operationsButtonsGroup = new ButtonGroup();

  private Vector operationsButtons = new Vector();


  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  JPanel JOpPanel = new JPanel();


  private Border border = BorderFactory.createEtchedBorder(Color.white,
      new Color(148, 145, 140));
  private Border operationsBorder = new TitledBorder(border, "Operation type");
  //set the graphic properties


  public OperationPane(Operator op) {
    this.operator = op;
    //operator gets an assignment and then it starts
    jbInit();
  }

  public void startEdition() {
    //the working time (between start of stop) of operation is assciated with the experiment duration.
    Iterator iter = operationsPanels.keySet().iterator();
    while(iter.hasNext()) {
      String key = (String)iter.next();
      ((ClientInputPanel)operationsPanels.get(key)).startEdition();
    }
  }

  public void stopEdition() {
    Iterator iter = operationsPanels.keySet().iterator();
    while(iter.hasNext()) {
      String key = (String)iter.next();
      ((ClientInputPanel)operationsPanels.get(key)).stopEdition();
    }
  }

  /**
   * method jbInit
   * it shows the way how opeartionpane gets information and the graphic properties.
   */
  private void jbInit() {
    this.setLayout(gridBagLayout1);
    Vector ops = ClientCore.getOperator(operator.getCompleteName()).
        getGrantedOperations();
    //define a vector which is associated with with the operations

    for (int i = 0; i < ops.size(); i++) {
      try {
        Operation op = OperationCreator.createOperation( (String) ops.elementAt(i));
        if (op.isVisibleInTheClientPanel()) {
          ClientInputPanel pane = op.getClientPanel(operator.getInstitution());

          JRadioButton button = new JRadioButton();
          button.setActionCommand(pane.toString());
          button.setText((String) ops.elementAt(i));
          operationsButtonsGroup.add(button);
          this.operationsButtons.add(button);


          this.operationsPanels.put(pane.toString(), pane);


        }
      }
      //if operation appears in the client panel, we can add and put things in the ComboBox.
      catch (OperationNotCreatedException ex) {
        Utils.logger.error(
            "Could not create operation. Could not get panel associated. [IGNORED]");
        //if operation doesn't appear, we send a warning
      }
    }




    if (operationsButtons.size() > 0) {
      //if the price and quantity in the ComboBox are > 0, you can exchange on this institution.


      ((JRadioButton)operationsButtons.get(0)).setSelected(true);


      jPanel1.add( ( (ClientInputPanel) operationsPanels.get(
          this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());
          //ComboBox and Panel get actions

      jPanel1.repaint();
      jPanel1.setEnabled(true);
      JOpPanel.setBorder(operationsBorder);
      JOpPanel.setPreferredSize(new Dimension(105, 100));
      JOpPanel.setLayout(new GridBagLayout());

      for(int i = 0; i < this.operationsButtons.size(); i++) {
        ( (JRadioButton) operationsButtons.get(i)).addActionListener(
            new OperationPane_operationsButtons_actionAdapter(this));
      }

      for(int i = 0; i < this.operationsButtons.size(); i++) {

        JOpPanel.add(((JRadioButton) operationsButtons.get(i)), new GridBagConstraints(0, i, 1, 1, 0.0, 1.0
                                                       , GridBagConstraints.WEST,
                                                       GridBagConstraints.NONE,
                                                       new Insets(0, 0, 0, 0), 0, 0));
                                                   //set the position and layout of ComboBox and Panell.
      }





      this.add(JOpPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));


      this.add(jPanel1, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0
                                               , GridBagConstraints.CENTER,
                                               GridBagConstraints.BOTH,
                                               new Insets(0, 0, 0, 0), 0, 0));


    }
    else {
      JTextField jtextField = new JTextField();
      jtextField.setText(" You can't exchange on this institution ");
      //if the price and quantity are negative, we send a warning.
      jtextField.setEditable(false);
      this.add(jtextField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                                         , GridBagConstraints.CENTER,
                                         GridBagConstraints.NONE,
                                         new Insets(0, 0, 0, 0), 0, 0)
          );
    }


  }



  void operationsButtons_actionPerformed(ActionEvent e) {
    jPanel1.removeAll();
    jPanel1.add( ( (ClientInputPanel) operationsPanels.get(
          this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());

    jPanel1.repaint();
    jPanel1.validate();
    //when action occurs in the ComboBox, Panel will know it.

    }


}


  /***************************************************************/
  /*                   EVENT CLASSES SECTION                     */
  /***************************************************************/
  class OperationPane_operationsButtons_actionAdapter
      implements java.awt.event.ActionListener {
    OperationPane adaptee;

    OperationPane_operationsButtons_actionAdapter(OperationPane adaptee) {
      this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
      adaptee.operationsButtons_actionPerformed(e);
    }

}


