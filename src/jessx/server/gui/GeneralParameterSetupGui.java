// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import javax.swing.event.TableModelEvent;
import jessx.business.BusinessCore;
import javax.swing.event.ChangeEvent;
import jessx.business.event.ChangeGeneralParametersEvent;
import org.jdom.Content;
import org.jdom.Element;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.table.TableModel;
import java.awt.LayoutManager;
import javax.swing.event.ChangeListener;
import jessx.business.event.ChangeGeneralParametersListener;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.event.TableModelListener;
import jessx.business.GeneralParameters;
import javax.swing.JPanel;

public class GeneralParameterSetupGui extends JPanel implements DisplayableNode, GeneralParameters, TableModelListener
{
    String workingDirectory;
    String loggingFileName;
    GridBagLayout gridBagLayout1;
    JLabel jLabel2;
    JTextField jTextField2;
    JLabel jLabel5;
    JSpinner jSpinner1;
    JLabel jLabelVersion;
    JTextField jTextFieldVersion;
    JLabel jLabel7;
    JSpinner jSpinner2;
    JTable jTable1;
    TableModelInterestRate tableModelInterestRate;
    JScrollPane jScrollPane1;
    private String setupFileName;
    private String XMLVersion;
    private boolean graphicalMode;
    private int periodCount;
    private int periodDuration;
    private boolean joiningAfterStartup;
    private Vector listeners;
    
    public GeneralParameterSetupGui(final boolean graphical) {
        this.workingDirectory = new String(String.valueOf(System.getProperty("user.dir")) + "\\");
        this.loggingFileName = new String("New Experiment Log");
        this.gridBagLayout1 = null;
        this.jLabel2 = null;
        this.jTextField2 = null;
        this.jLabel5 = null;
        this.jSpinner1 = null;
        this.jLabelVersion = null;
        this.jTextFieldVersion = null;
        this.jLabel7 = null;
        this.jSpinner2 = null;
        this.jScrollPane1 = new JScrollPane();
        this.setupFileName = new String("Setup File Name");
        this.XMLVersion = "";
        this.graphicalMode = true;
        this.listeners = new Vector();
        this.graphicalMode = graphical;
        if (this.graphicalMode) {
            this.gridBagLayout1 = new GridBagLayout();
            this.jLabel2 = new JLabel();
            this.jTextField2 = new JTextField();
            this.jLabel5 = new JLabel();
            this.jSpinner1 = new JSpinner();
            this.jLabelVersion = new JLabel("Version : ");
            this.jTextFieldVersion = new JTextField();
            this.jLabel7 = new JLabel();
            this.jSpinner2 = new JSpinner();
            try {
                this.jbInit();
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void addChangeGeneralParametersListener(final ChangeGeneralParametersListener listener) {
        this.listeners.add(listener);
    }
    
    public JPanel getGeneralParameterSetupGui() {
        return this;
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return "General Parameters";
    }
    
    public String getWorkingDirectory() {
        return this.workingDirectory;
    }
    
    public void setWorkingDirectory(final String cwd) {
        this.workingDirectory = cwd;
    }
    
    public String getSetupFileName() {
        if (this.graphicalMode) {
            return this.jTextField2.getText();
        }
        return this.setupFileName;
    }
    
    public void setSetupFileName(final String _setupFileName) {
        if (this.graphicalMode) {
            this.jTextField2.setText(_setupFileName);
        }
        else {
            this.setupFileName = _setupFileName;
        }
    }
    
    public String getXMLVersion() {
        if (this.graphicalMode) {
            return this.jTextFieldVersion.getText();
        }
        return this.XMLVersion;
    }
    
    public void setXMLVersion(final String xmlVersion) {
        if (this.graphicalMode) {
            this.jTextFieldVersion.setText(xmlVersion);
        }
        else {
            this.XMLVersion = xmlVersion;
        }
    }
    
    public String getLoggingFileName() {
        return this.loggingFileName;
    }
    
    public void setLoggingFileName(final String name) {
        this.loggingFileName = name;
    }
    
    public int getPeriodCount() {
        if (this.graphicalMode) {
            return new Integer(this.jSpinner1.getValue().toString());
        }
        return this.periodCount;
    }
    
    public void setPeriodCount(final int _periodCount) {
        if (this.graphicalMode) {
            this.jSpinner1.setValue(new Integer(_periodCount));
        }
        else {
            this.periodCount = _periodCount;
            this.tableModelInterestRate.setInterestSize(this.periodCount);
        }
    }
    
    public boolean getAfterSetupJoiningAllowed() {
        return false;
    }
    
    public void setAfterSetupJoiningAllowed(final boolean allowed) {
    }
    
    public float getInterestRate(final int index) {
        return (float)this.tableModelInterestRate.getValueAt(index, 2);
    }
    
    public void setInterestRate(final int index, final float rate) {
        this.tableModelInterestRate.getVectorVal().setElementAt(rate, index);
    }
    
    public int getPeriodDuration() {
        if (this.graphicalMode) {
            return new Integer(this.jSpinner2.getValue().toString());
        }
        return this.periodDuration;
    }
    
    public void setPeriodDuration(final int duration) {
        if (this.graphicalMode) {
            this.jSpinner2.setValue(new Integer(duration));
        }
        else {
            this.periodDuration = duration;
        }
    }
    
    public void addPeriodCountChangeListener(final ChangeListener listener) {
        if (this.graphicalMode) {
            this.jSpinner1.addChangeListener(listener);
        }
    }
    
    public void addPeriodDurationChangeListener(final ChangeListener listener) {
        if (this.graphicalMode) {
            this.jSpinner2.addChangeListener(listener);
        }
    }
    
    public void removePeriodCountChangeListener(final ChangeListener listener) {
        if (this.graphicalMode) {
            this.jSpinner1.removeChangeListener(listener);
        }
    }
    
    public void removePeriodDurationChangeListener(final ChangeListener listener) {
        if (this.graphicalMode) {
            this.jSpinner2.removeChangeListener(listener);
        }
    }
    
    public void initializeGeneralParameters() {
        this.setSetupFileName("Title of the Experiment");
        this.setPeriodCount(1);
        if (this.graphicalMode) {
            this.jTextField2.setEditable(true);
            this.tableModelInterestRate.setCellEditable();
            this.jSpinner1.setEnabled(true);
            this.jSpinner1.setName("PeriodCountSpinner");
            this.jSpinner2.setName("PeriodDurationSpinner");
        }
    }
    
    public void setUneditable() {
        this.jTextField2.setEditable(false);
        this.tableModelInterestRate.setCellUneditable();
        this.jSpinner1.setEnabled(false);
        this.jSpinner2.setEnabled(false);
    }
    
    public void setEditable() {
        this.jTextField2.setEditable(true);
        this.tableModelInterestRate.setCellEditable();
        this.jSpinner1.setEnabled(true);
        this.jSpinner2.setEnabled(true);
    }
    
    public JSpinner getPeriodCountSpinner() {
        return this.jSpinner1;
    }
    
    public JSpinner getPeriodDurationSpinner() {
        return this.jSpinner2;
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout1);
        this.tableModelInterestRate = new TableModelInterestRate();
        this.jTable1 = new JTable(this.tableModelInterestRate);
        this.jScrollPane1.setMinimumSize(new Dimension(50, 50));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
        this.jLabel2.setText("Experiment Title : ");
        this.jLabel5.setText("Number of Periods : ");
        this.jSpinner1.setModel(new SpinnerNumberModel(1, 1, 100, 1));
        this.jLabel7.setText("Period length (sec) :");
        this.jSpinner2.setValue(new Integer(1));
        this.jSpinner2.setModel(new SpinnerNumberModel(1, 1, 3600, 1));
        this.initializeGeneralParameters();
        this.add(this.jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(3, 6, 3, 3), 0, 0));
        this.add(this.jTextField2, new GridBagConstraints(1, 0, 2, 1, 1.0, 0.0, 10, 2, new Insets(3, 3, 3, 6), 0, 0));
        this.add(this.jLabel5, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 13, 0, new Insets(6, 6, 3, 3), 0, 0));
        this.add(this.jSpinner1, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, 17, 0, new Insets(6, 3, 3, 6), 50, 0));
        this.add(this.jLabel7, new GridBagConstraints(0, 5, 1, 1, 0.0, 0.0, 13, 0, new Insets(3, 3, 3, 6), 0, 0));
        this.add(this.jSpinner2, new GridBagConstraints(1, 5, 1, 1, 1.0, 0.0, 17, 0, new Insets(3, 3, 3, 6), 34, 0));
        this.add(this.jLabelVersion, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 13, 0, new Insets(3, 6, 3, 3), 0, 0));
        this.add(this.jScrollPane1, new GridBagConstraints(1, 9, 1, 2, 0.0, 0.0, 10, 2, new Insets(3, 3, 3, 50), 0, 200));
        this.jTextFieldVersion.setEditable(false);
        this.jTextFieldVersion.setText("1.6");
        this.add(this.jTextFieldVersion, new GridBagConstraints(1, 1, 2, 1, 1.0, 0.0, 10, 2, new Insets(3, 3, 3, 3), 0, 0));
        this.jSpinner1.addChangeListener(new GeneralParameterSetupGui_jSpinner1_changeAdapter(this));
        this.jSpinner2.addChangeListener(new GeneralParameterSetupGui_jSpinner2_changeAdapter(this));
    }
    
    public void saveToXml(final Element node) {
        final Element interestRateNode = new Element("InterestRate");
        this.tableModelInterestRate.saveToXml(interestRateNode);
        node.addContent(new Element("WorkingDirectory").setText(this.getWorkingDirectory())).addContent(new Element("SetupFileName").setText(this.getSetupFileName())).addContent(new Element("XMLVersion").setText("1.6")).addContent(new Element("LoggingFileName").setText(this.getLoggingFileName())).addContent(new Element("PeriodNumber").setText(Integer.toString(this.getPeriodCount()))).addContent(new Element("PeriodDuration").setText(Integer.toString(this.getPeriodDuration()))).addContent(interestRateNode).addContent(new Element("JoiningAfterStartup").setText(Boolean.toString(this.getAfterSetupJoiningAllowed())));
        this.setXMLVersion("1.6");
    }
    
    public void loadFromXml(final Element node) {
        this.setWorkingDirectory(node.getChild("WorkingDirectory").getText());
        this.setSetupFileName(node.getChild("SetupFileName").getText());
        if (node.getChild("XMLVersion") != null) {
            this.setXMLVersion(node.getChild("XMLVersion").getText());
        }
        else {
            this.setXMLVersion("This XML file has no version");
        }
        this.setLoggingFileName(node.getChild("LoggingFileName").getText());
        this.setPeriodCount(Integer.parseInt(node.getChild("PeriodNumber").getText()));
        this.setPeriodDuration(Integer.parseInt(node.getChild("PeriodDuration").getText()));
        this.tableModelInterestRate.loadFromXml(node.getChild("InterestRate"));
        this.setAfterSetupJoiningAllowed(Boolean.getBoolean(node.getChild("JoiningAfterStartup").getText()));
    }
    
    private void firejSpinner1StateChanged() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ChangeGeneralParametersListener) this.listeners.elementAt(i)).generalParametersChanged(new ChangeGeneralParametersEvent(this.jSpinner1));
        }
    }
    
    private void firejSpinner2StateChanged() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ChangeGeneralParametersListener) this.listeners.elementAt(i)).generalParametersChanged(new ChangeGeneralParametersEvent(this.jSpinner2));
        }
    }
    
    public void jSpinner1_stateChanged(final ChangeEvent e) {
        this.firejSpinner1StateChanged();
        this.tableModelInterestRate.setInterestSize(BusinessCore.getGeneralParameters().getPeriodCount());
        this.tableModelInterestRate.fireTableDataChanged();
    }
    
    public void jSpinner2_stateChanged(final ChangeEvent e) {
        this.firejSpinner2StateChanged();
    }
    
    public void tableChanged(final TableModelEvent arg0) {
    }
}
