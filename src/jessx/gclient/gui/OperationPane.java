package jessx.gclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import jessx.business.ClientInputPanel;
import jessx.business.Operation;
import jessx.business.OperationCreator;
import jessx.business.OperationNotCreatedException;
import jessx.business.Operator;
import jessx.client.ClientCore;
import jessx.utils.Utils;

public class OperationPane extends JPanel {
  private Operator operator;
  
  private Hashtable operationsPanels = new Hashtable<Object, Object>();
  
  private ButtonGroup operationsButtonsGroup = new ButtonGroup();
  
  private Vector operationsButtons = new Vector();
  
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  
  JPanel jPanel1 = new JPanel();
  
  JPanel JOpPanel = new JPanel();
  
  private Border border = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
  
  private Border operationsBorder = new TitledBorder(this.border, "Operation type");
  
  public OperationPane(Operator op) {
    this.operator = op;
    jbInit();
  }
  
  public void startEdition() {
    Iterator<String> iter = this.operationsPanels.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      ((ClientInputPanel)this.operationsPanels.get(key)).startEdition();
    } 
  }
  
  public void stopEdition() {
    Iterator<String> iter = this.operationsPanels.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      ((ClientInputPanel)this.operationsPanels.get(key)).stopEdition();
    } 
  }
  
  private void jbInit() {
    setLayout(this.gridBagLayout1);
    Vector<String> ops = ClientCore.getOperator(this.operator.getCompleteName())
      .getGrantedOperations();
    int i;
    for (i = 0; i < ops.size(); i++) {
      try {
        Operation op = OperationCreator.createOperation(ops.elementAt(i));
        if (op.isVisibleInTheClientPanel()) {
          ClientInputPanel pane = op.getClientPanel(this.operator.getInstitution());
          JRadioButton button = new JRadioButton();
          button.setActionCommand(pane.toString());
          button.setText(ops.elementAt(i));
          this.operationsButtonsGroup.add(button);
          this.operationsButtons.add(button);
          this.operationsPanels.put(pane.toString(), pane);
        } 
      } catch (OperationNotCreatedException ex) {
        Utils.logger.error(
            "Could not create operation. Could not get panel associated. [IGNORED]");
      } 
    } 
    if (this.operationsButtons.size() > 0) {
      ((JRadioButton)this.operationsButtons.get(0)).setSelected(true);
      this.jPanel1.add(((ClientInputPanel)this.operationsPanels.get(
            this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());
      this.jPanel1.repaint();
      this.jPanel1.setEnabled(true);
      this.JOpPanel.setBorder(this.operationsBorder);
      this.JOpPanel.setPreferredSize(new Dimension(105, 100));
      this.JOpPanel.setLayout(new GridBagLayout());
      for (i = 0; i < this.operationsButtons.size(); i++)
        ((JRadioButton)this.operationsButtons.get(i)).addActionListener(
            new OperationPane_operationsButtons_actionAdapter(this)); 
      for (i = 0; i < this.operationsButtons.size(); i++)
        this.JOpPanel.add((Component) this.operationsButtons.get(i), new GridBagConstraints(0, i, 1, 1, 0.0D, 1.0D, 
              17, 
              0, 
              new Insets(0, 0, 0, 0), 0, 0)); 
      add(this.JOpPanel, new GridBagConstraints(0, 0, 1, 1, 0.5D, 0.0D, 
            10, 
            1, 
            new Insets(0, 0, 0, 0), 0, 0));
      add(this.jPanel1, new GridBagConstraints(1, 0, 1, 1, 0.5D, 0.0D, 
            10, 
            1, 
            new Insets(0, 0, 0, 0), 0, 0));
    } else {
      JTextField jtextField = new JTextField();
      jtextField.setText(" You can't exchange on this institution ");
      jtextField.setEditable(false);
      add(jtextField, new GridBagConstraints(0, 1, 1, 1, 0.0D, 0.0D, 
            10, 
            0, 
            new Insets(0, 0, 0, 0), 0, 0));
    } 
  }
  
  void operationsButtons_actionPerformed(ActionEvent e) {
    this.jPanel1.removeAll();
    this.jPanel1.add(((ClientInputPanel)this.operationsPanels.get(
          this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());
    this.jPanel1.repaint();
    this.jPanel1.validate();
  }
}
