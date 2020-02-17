// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.AbstractButton;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.InputVerifier;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import jessx.utils.Utils;
import javax.swing.event.ChangeEvent;
import jessx.business.event.DividendInfoEvent;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.JSpinner;
import jessx.business.DividendLimitation;
import javax.swing.event.ChangeListener;
import jessx.business.event.DividendInfoListener;
import javax.swing.JPanel;

public class DividendInfoPanel extends JPanel implements DividendInfoListener, ChangeListener
{
    private DividendLimitation divInfo;
    private JSpinner jSpinner1;
    private GridBagLayout gridBagLayout1;
    private JLabel jLabel1;
    private JPanel jPanel1;
    private JRadioButton jRadioButton1;
    private GridBagLayout gridBagLayout2;
    private JRadioButton jRadioButton2;
    private JRadioButton jRadioButton3;
    private JRadioButton jRadioButton0;
    private int stateOfButtons0123;
    private ButtonGroup buttonGroup1;
    private JCheckBox jCheckBox1;
    private JCheckBox jCheckBox2;
    private JCheckBox jCheckBox3;
    private JCheckBox jCheckBox4;
    private JCheckBox jCheckBox5;
    private boolean first;
    private Border border1;
    private Border border2;
    
    public DividendInfoPanel(final DividendLimitation divInfo) {
        this.jSpinner1 = new JSpinner();
        this.gridBagLayout1 = new GridBagLayout();
        this.jLabel1 = new JLabel();
        this.jPanel1 = new JPanel();
        this.jRadioButton1 = new JRadioButton();
        this.gridBagLayout2 = new GridBagLayout();
        this.jRadioButton2 = new JRadioButton();
        this.jRadioButton3 = new JRadioButton();
        this.jRadioButton0 = new JRadioButton();
        this.stateOfButtons0123 = 1;
        this.buttonGroup1 = new ButtonGroup();
        this.jCheckBox1 = new JCheckBox();
        this.jCheckBox2 = new JCheckBox();
        this.jCheckBox3 = new JCheckBox();
        this.jCheckBox4 = new JCheckBox();
        this.jCheckBox5 = new JCheckBox();
        this.first = true;
        this.border1 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.border2 = new TitledBorder(this.border1, "Dividend Detailled Information");
        this.divInfo = divInfo;
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.divInfo.addListener(this);
        this.updateUIFromModel();
        this.jSpinner1.addChangeListener(this);
        this.jRadioButton1.addChangeListener(this);
        this.jRadioButton2.addChangeListener(this);
        this.jRadioButton3.addChangeListener(this);
        this.jRadioButton0.addChangeListener(this);
        this.jCheckBox1.addChangeListener(this);
        this.jCheckBox2.addChangeListener(this);
        this.jCheckBox3.addChangeListener(this);
        this.jCheckBox4.addChangeListener(this);
        this.jCheckBox5.addChangeListener(this);
    }
    
    public void dividendInfoModified(final DividendInfoEvent e) {
        if (e.getEvent() == DividendInfoEvent.DIVIDEND_INFO_UPDATED) {
            this.updateUIFromModel();
        }
    }
    
    public void stateChanged(final ChangeEvent e) {
        if (e.getSource() instanceof JSpinner) {
            Utils.logger.debug("jSpinner state changed. new value entered: " + (int)this.jSpinner1.getValue() + ". Updating model.");
            this.divInfo.setWindowSize((int)this.jSpinner1.getValue());
        }
        else if (e.getSource() instanceof JCheckBox) {
            if (((JCheckBox)e.getSource()).getName().equalsIgnoreCase(this.jCheckBox1.getName())) {
                if (this.jCheckBox1.isSelected() != this.divInfo.isDisplayingSessionLength()) {
                    Utils.logger.debug("jCheckBox1 state changed. new value: " + this.jCheckBox1.isSelected() + ". Updating model.");
                    this.divInfo.setDisplaySessionLength(this.jCheckBox1.isSelected());
                }
            }
            else if (((JCheckBox)e.getSource()).getName().equalsIgnoreCase(this.jCheckBox2.getName())) {
                if (this.jCheckBox2.isSelected() != this.divInfo.isDisplayingWindowSize()) {
                    Utils.logger.debug("jCheckBox2 state changed. new value: " + this.jCheckBox2.isSelected() + ". Updating model.");
                    this.divInfo.setDisplayWindowSize(this.jCheckBox2.isSelected());
                }
            }
            else if (((JCheckBox)e.getSource()).getName().equalsIgnoreCase(this.jCheckBox3.getName())) {
                if (this.jCheckBox3.isSelected() != this.divInfo.isDisplayingHoldingValueForExperiment()) {
                    Utils.logger.debug("jCheckBox3 state changed. new value: " + this.jCheckBox3.isSelected() + ". Updating model.");
                    this.divInfo.setDisplayHoldingValueForExperiment(this.jCheckBox3.isSelected());
                }
            }
            else if (((JCheckBox)e.getSource()).getName().equalsIgnoreCase(this.jCheckBox4.getName())) {
                if (this.jCheckBox4.isSelected() != this.divInfo.isDisplayHoldingValueForWindow()) {
                    Utils.logger.debug("jCheckBox4 state changed. new value: " + this.jCheckBox4.isSelected() + ". Updating model.");
                    this.divInfo.setDisplayHoldingValueForWindow(this.jCheckBox4.isSelected());
                }
            }
            else if (((JCheckBox)e.getSource()).getName().equalsIgnoreCase(this.jCheckBox5.getName()) && this.jCheckBox5.isSelected() != this.divInfo.isDisplayingOperationsCosts()) {
                Utils.logger.debug("jCheckBox5 state changed. new value: " + this.jCheckBox5.isSelected() + ". Updating model.");
                this.divInfo.setDisplayOperationsCosts(this.jCheckBox5.isSelected());
                Utils.logger.info("couts des operations " + this.jCheckBox5.isSelected());
            }
        }
        else if (e.getSource() instanceof JRadioButton) {
            Utils.logger.debug("jRadioButton state changed.");
            final int previousState = this.stateOfButtons0123;
            if (this.jRadioButton1.isSelected()) {
                this.stateOfButtons0123 = 1;
            }
            else if (this.jRadioButton2.isSelected()) {
                this.stateOfButtons0123 = 2;
            }
            else if (this.jRadioButton3.isSelected()) {
                this.stateOfButtons0123 = 3;
            }
            else if (this.jRadioButton0.isSelected()) {
                this.stateOfButtons0123 = 0;
            }
            if (previousState == this.stateOfButtons0123) {
                this.divInfo.setDividendDetailledproperties(this.stateOfButtons0123);
            }
        }
    }
    
    public void desactive() {
        this.jSpinner1.setEnabled(false);
        this.jRadioButton0.setEnabled(false);
        this.jRadioButton1.setEnabled(false);
        this.jRadioButton2.setEnabled(false);
        this.jRadioButton3.setEnabled(false);
        this.jCheckBox1.setEnabled(false);
        this.jCheckBox2.setEnabled(false);
        this.jCheckBox3.setEnabled(false);
        this.jCheckBox4.setEnabled(false);
        this.jCheckBox5.setEnabled(false);
    }
    
    public void active() {
        this.jSpinner1.setEnabled(true);
        this.jRadioButton0.setEnabled(true);
        this.jRadioButton1.setEnabled(true);
        this.jRadioButton2.setEnabled(true);
        this.jRadioButton3.setEnabled(true);
        this.jCheckBox1.setEnabled(true);
        this.jCheckBox2.setEnabled(true);
        this.jCheckBox3.setEnabled(true);
        this.jCheckBox4.setEnabled(true);
        this.jCheckBox5.setEnabled(true);
    }
    
    private void updateUIFromModel() {
        this.jSpinner1.setValue(new Integer(this.divInfo.getWindowSize()));
        if (this.first) {
            this.first = false;
            if (1 == this.divInfo.getDividendDetailledproperties()) {
                this.jRadioButton1.setSelected(true);
            }
            else if (2 == this.divInfo.getDividendDetailledproperties()) {
                this.jRadioButton2.setSelected(true);
            }
            else if (3 == this.divInfo.getDividendDetailledproperties()) {
                this.jRadioButton3.setSelected(true);
            }
            else {
                this.jRadioButton0.setSelected(true);
            }
        }
        this.jCheckBox1.setSelected(this.divInfo.isDisplayingSessionLength());
        this.jCheckBox2.setSelected(this.divInfo.isDisplayingWindowSize());
        this.jCheckBox3.setSelected(this.divInfo.isDisplayingHoldingValueForExperiment());
        this.jCheckBox4.setSelected(this.divInfo.isDisplayHoldingValueForWindow());
        this.jCheckBox5.setSelected(this.divInfo.isDisplayingOperationsCosts());
    }
    
    private void jbInit() throws Exception {
        this.jSpinner1.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        this.setLayout(this.gridBagLayout1);
        this.jLabel1.setText("Window size : ");
        this.jPanel1.setBorder(this.border2);
        this.jPanel1.setPreferredSize(new Dimension(200, 73));
        this.jPanel1.setInputVerifier(null);
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.jRadioButton0.setMinimumSize(new Dimension(5, 23));
        this.jRadioButton0.setText("Show them for each period of the experiment");
        this.jRadioButton1.setMinimumSize(new Dimension(5, 23));
        this.jRadioButton1.setText("Show them only for the next period");
        this.jRadioButton2.setMinimumSize(new Dimension(5, 23));
        this.jRadioButton2.setText("Show them for each period of the window");
        this.jRadioButton3.setMinimumSize(new Dimension(5, 23));
        this.jRadioButton3.setText("Do not show anything");
        this.jCheckBox1.setText("Show the number of periods in the experiment");
        this.jCheckBox2.setText("Show the number of periods in the window");
        this.jCheckBox3.setText("Show the holding value for the experiment");
        this.jCheckBox4.setText("Show the holding value for the window");
        this.jCheckBox5.setText("Show the costs of the operations");
        this.jCheckBox1.setName("jCheckBox1");
        this.jCheckBox1.setMinimumSize(new Dimension(243, 23));
        this.jCheckBox2.setName("jCheckBox2");
        this.jCheckBox2.setMinimumSize(new Dimension(5, 23));
        this.jCheckBox3.setName("jCheckBox3");
        this.jCheckBox3.setMinimumSize(new Dimension(5, 23));
        this.jCheckBox4.setName("jCheckBox4");
        this.jCheckBox4.setMinimumSize(new Dimension(5, 23));
        this.jCheckBox5.setName("jCheckBox5");
        this.jCheckBox5.setMinimumSize(new Dimension(5, 23));
        this.jPanel1.add(this.jRadioButton0, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jRadioButton1, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 17, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jRadioButton2, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jRadioButton3, new GridBagConstraints(0, 3, 1, 1, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.buttonGroup1.add(this.jRadioButton0);
        this.buttonGroup1.add(this.jRadioButton1);
        this.buttonGroup1.add(this.jRadioButton2);
        this.buttonGroup1.add(this.jRadioButton3);
        this.add(this.jSpinner1, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(6, 3, 3, 6), 40, 0));
        this.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(6, 6, 3, 3), 0, 0));
        this.add(this.jCheckBox1, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 17, 0, new Insets(3, 6, 3, 6), 0, 0));
        this.add(this.jCheckBox2, new GridBagConstraints(0, 2, 2, 1, 1.0, 0.0, 17, 2, new Insets(3, 6, 3, 6), 0, 0));
        this.add(this.jCheckBox3, new GridBagConstraints(0, 4, 2, 1, 1.0, 0.0, 17, 2, new Insets(3, 6, 3, 6), 0, 0));
        this.add(this.jCheckBox4, new GridBagConstraints(0, 5, 2, 1, 1.0, 0.0, 17, 2, new Insets(3, 6, 3, 6), 0, 0));
        this.add(this.jCheckBox5, new GridBagConstraints(0, 6, 3, 1, 0.0, 0.0, 10, 2, new Insets(3, 6, 3, 6), 185, 0));
        this.add(this.jPanel1, new GridBagConstraints(0, 3, 2, 1, 1.0, 0.0, 10, 2, new Insets(3, 6, 3, 6), 0, 47));
    }
}
