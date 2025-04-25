// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import jessx.business.Operation;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Component;
import jessx.business.OperationNotCreatedException;
import jessx.utils.Utils;
import javax.swing.AbstractButton;
import javax.swing.JRadioButton;
import jessx.business.OperationCreator;
import jessx.client.ClientCore;
import java.awt.LayoutManager;
import java.util.Iterator;
import jessx.business.ClientInputPanel;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.border.Border;
import java.awt.GridBagLayout;
import java.util.Vector;
import javax.swing.ButtonGroup;
import java.util.Hashtable;
import jessx.business.Operator;
import javax.swing.JPanel;

public class OperationPane extends JPanel
{
    private Operator operator;
    private Hashtable operationsPanels;
    private ButtonGroup operationsButtonsGroup;
    private Vector operationsButtons;
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    JPanel jPanel1;
    JPanel JOpPanel;
    private Border border;
    private Border operationsBorder;
    
    public OperationPane(final Operator op) {
        this.operationsPanels = new Hashtable();
        this.operationsButtonsGroup = new ButtonGroup();
        this.operationsButtons = new Vector();
        this.gridBagLayout1 = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.JOpPanel = new JPanel();
        this.border = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.operationsBorder = new TitledBorder(this.border, "Operation type");
        this.operator = op;
        this.jbInit();
    }
    
    public void startEdition() {
    	Iterator iter = operationsPanels.keySet().iterator();
        while(iter.hasNext()) {
          String key = (String)iter.next();
            ((ClientInputPanel) this.operationsPanels.get(key)).startEdition();
        }
    }
    
    public void stopEdition() {
    	  Iterator iter = operationsPanels.keySet().iterator();
    	    while(iter.hasNext()) {
    	      String key = (String)iter.next();
            ((ClientInputPanel) this.operationsPanels.get(key)).stopEdition();
        }
    }
    
    private void jbInit() {
        this.setLayout(this.gridBagLayout1);
        final Vector ops = ClientCore.getOperator(this.operator.getCompleteName()).getGrantedOperations();
        for (int i = 0; i < ops.size(); ++i) {
            try {
                final Operation op = OperationCreator.createOperation((String) ops.elementAt(i));
                if (op.isVisibleInTheClientPanel()) {
                    final ClientInputPanel pane = op.getClientPanel(this.operator.getInstitution());
                    final JRadioButton button = new JRadioButton();
                    button.setActionCommand(pane.toString());
                    button.setText((String) ops.elementAt(i));
                    this.operationsButtonsGroup.add(button);
                    this.operationsButtons.add(button);
                    this.operationsPanels.put(pane.toString(), pane);
                }
            }
            catch (final OperationNotCreatedException ex) {
                Utils.logger.error("Could not create operation. Could not get panel associated. [IGNORED]");
            }
        }
        if (this.operationsButtons.size() > 0) {
            ((AbstractButton) this.operationsButtons.get(0)).setSelected(true);
            this.jPanel1.add(((ClientInputPanel) this.operationsPanels.get(this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());
            this.jPanel1.repaint();
            this.jPanel1.setEnabled(true);
            this.JOpPanel.setBorder(this.operationsBorder);
            this.JOpPanel.setPreferredSize(new Dimension(105, 100));
            this.JOpPanel.setLayout(new GridBagLayout());
            for (int i = 0; i < this.operationsButtons.size(); ++i) {
                ((JRadioButton) this.operationsButtons.get(i)).addActionListener(new OperationPane_operationsButtons_actionAdapter(this));
            }
            for (int i = 0; i < this.operationsButtons.size(); ++i) {
                this.JOpPanel.add((Component) this.operationsButtons.get(i), new GridBagConstraints(0, i, 1, 1, 0.0, 1.0, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
            }
            this.add(this.JOpPanel, new GridBagConstraints(0, 0, 1, 1, 0.5, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            this.add(this.jPanel1, new GridBagConstraints(1, 0, 1, 1, 0.5, 0.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        }
        else {
            final JTextField jtextField = new JTextField();
            jtextField.setText(" You can't exchange on this institution ");
            jtextField.setEditable(false);
            this.add(jtextField, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        }
    }
    
    void operationsButtons_actionPerformed(final ActionEvent e) {
        this.jPanel1.removeAll();
        this.jPanel1.add(((ClientInputPanel) this.operationsPanels.get(this.operationsButtonsGroup.getSelection().getActionCommand().toString())).getPanel());
        this.jPanel1.repaint();
        this.jPanel1.validate();
    }
}
