// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.analysis.gui;

import jessx.analysis.tools.XMLExportation;
import org.jdom.Document;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.IOException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import jessx.utils.Utils;
import java.awt.event.WindowEvent;
import java.awt.Point;
import java.awt.Frame;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import jessx.analysis.AnalysisTool;
import java.util.Enumeration;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import jessx.analysis.AnalysisToolNotCreatedException;
import javax.swing.JCheckBox;
import jessx.analysis.AnalysisToolCreator;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import jessx.analysis.AnalysisToolsCore;
import java.util.HashMap;
import java.awt.GridBagLayout;

import javax.swing.AbstractButton;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import jessx.utils.Constants;
import javax.swing.JFrame;

public class CoreFrame extends JFrame implements Constants
{
    JPanel contentPane;
    JMenuBar jMenuBar1;
    JMenu jMenuFile;
    JMenuItem jMenuFileExit;
    JMenu jMenuHelp;
    JMenuItem jMenuHelpAbout;
    JLabel statusBar;
    JTabbedPane jTabbedPane;
    GridBagLayout gridBagLayout1;
    JMenuItem jMenuItemExport;
    JMenuItem jMenuItemAnalyse;
    JPanel jPanel2;
    GridBagLayout gridBagLayout2;
    private HashMap checkboxList;
    
    public CoreFrame() {
        this.jMenuBar1 = new JMenuBar();
        this.jMenuFile = new JMenu();
        this.jMenuFileExit = new JMenuItem();
        this.jMenuHelp = new JMenu();
        this.jMenuHelpAbout = new JMenuItem();
        this.statusBar = new JLabel();
        this.jTabbedPane = new JTabbedPane();
        this.gridBagLayout1 = new GridBagLayout();
        this.jMenuItemExport = new JMenuItem();
        this.jMenuItemAnalyse = new JMenuItem();
        this.jPanel2 = new JPanel();
        this.gridBagLayout2 = new GridBagLayout();
        this.checkboxList = new HashMap();
        this.setDefaultCloseOperation(0);
        this.enableEvents(64L);
        try {
            AnalysisToolsCore.logger.debug("Frame initialization...");
            this.jbInit();
        }
        catch (Exception e) {
            AnalysisToolsCore.logger.fatal("Something went wrong during frame initialisation: " + e.toString());
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.setIconImage(new ImageIcon("./images/logo_JessX_small.GIF").getImage());
        AnalysisToolsCore.logger.debug("Content pane initialisation...");
        (this.contentPane = (JPanel)this.getContentPane()).setLayout(this.gridBagLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("JessX Analyzer 1.6");
        this.statusBar.setText(" ");
        AnalysisToolsCore.logger.debug("Menu items initialisation");
        this.jMenuFile.setMnemonic(70);
        this.jMenuFile.setText("File");
        this.jMenuFileExit.setMnemonic(88);
        this.jMenuFileExit.setText("Exit");
        this.jMenuFileExit.setAccelerator(KeyStroke.getKeyStroke(115, 8));
        this.jMenuFileExit.addActionListener(new CoreFrame_jMenuFileExit_ActionAdapter(this));
        this.jMenuHelp.setMnemonic(72);
        this.jMenuHelp.setText("Help");
        this.jMenuHelpAbout.setMnemonic(65);
        this.jMenuHelpAbout.setText("About JessX...");
        this.jMenuHelpAbout.addActionListener(new CoreFrame_jMenuHelpAbout_ActionAdapter(this));
        this.jMenuItemAnalyse.setMnemonic(79);
        this.jMenuItemAnalyse.setText("Open results...");
        this.jMenuItemAnalyse.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        this.jMenuItemAnalyse.setToolTipText("Click here to analyse the results of the experiment.");
        this.jMenuItemAnalyse.addActionListener(new CoreFrame_jMenuItemAnalyse_actionAdapter(this));
        this.jMenuItemExport.setMnemonic(69);
        this.jMenuItemExport.setAccelerator(KeyStroke.getKeyStroke(69, 2));
        this.jMenuItemExport.setText("Export results...");
        this.jMenuItemExport.setToolTipText("Click here to export the results of the experiment.");
        this.jMenuItemExport.addActionListener(new CoreFrame_jMenuItemExport_actionAdapter(this));
        this.jPanel2.setLayout(this.gridBagLayout2);
        this.jPanel2.setMinimumSize(new Dimension(100, 0));
        this.jPanel2.setToolTipText("Choose an option.");
        this.jTabbedPane.setTabPlacement(2);
        this.jMenuFile.add(this.jMenuItemAnalyse);
        this.jMenuFile.addSeparator();
        this.jMenuFile.add(this.jMenuItemExport);
        this.jMenuFile.addSeparator();
        this.jMenuFile.add(this.jMenuFileExit);
        this.jMenuHelp.add(this.jMenuHelpAbout);
        this.jMenuBar1.add(this.jMenuFile);
        this.jMenuBar1.add(this.jMenuHelp);
        this.setJMenuBar(this.jMenuBar1);
        this.contentPane.add(this.statusBar, new GridBagConstraints(0, 2, 2, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.contentPane.add(this.jTabbedPane, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 3, 3, 3), 0, 0));
        this.contentPane.add(this.jPanel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 1.0, 17, 3, new Insets(0, 0, 0, 0), 0, 0));
        AnalysisToolsCore.logger.debug("Initiating about box with available modules...");
        final Enumeration analysisToolsIterator = AnalysisToolCreator.analyseFactories.keys();
        int toolNum = 0;
        while (analysisToolsIterator.hasMoreElements()) {
            final String key = (String) analysisToolsIterator.nextElement();
            AnalysisTool tempTool = null;
            try {
                tempTool = AnalysisToolCreator.createTool(key);
                final JCheckBox jCheckBox = new JCheckBox(tempTool.getToolName());
                jCheckBox.setToolTipText(tempTool.getToolDescription());
                AnalysisToolsCore.logger.debug("Adding " + tempTool.getToolName() + " tool to interface.");
                jCheckBox.setSelected(true);
                this.checkboxList.put(tempTool.getToolName(), jCheckBox);
                this.jPanel2.add(jCheckBox, new GridBagConstraints(0, toolNum, 1, 1, 0.0, 0.0, 18, 0, new Insets(3, 3, 3, 3), 0, 0));
                AnalysisToolsCore.logger.debug("following tool added to interface: " + tempTool.getToolName());
                ++toolNum;
            }
            catch (AnalysisToolNotCreatedException ex1) {
                ex1.printStackTrace();
            }
        }
        this.jPanel2.setBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158)), "Available modules"));
    }
    
    public void jMenuFileExit_actionPerformed(final ActionEvent e) {
        AnalysisToolsCore.logger.debug("Exiting program on user prompt. (menu|exit)");
        if (JOptionPane.showConfirmDialog(this, "Do you really want to quit JessX Analyzer?", "JessX Analyzer", 2, 2) == 0) {
            System.exit(0);
        }
    }
    
    public void jMenuHelpAbout_actionPerformed(final ActionEvent e) {
        final AnalysisAboutBox dlg = new AnalysisAboutBox(this);
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = this.getSize();
        final Point loc = this.getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == 201) {
            this.jMenuFileExit_actionPerformed(null);
        }
    }
    
    void jMenuItemAnalyse_actionPerformed(final ActionEvent e) {
        AnalysisToolsCore.logger.info("Beginning analyse process.");
        AnalysisToolsCore.logger.debug("Asking user a file to analyse.");
        final JFileChooser chooser = Utils.newFileChooser(new String("."), new String(""), "JessX XML log files (*.xml)", ".xml");
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal != 0) {
            AnalysisToolsCore.logger.info("No file selected. Stopping analyse process.");
            return;
        }
        AnalysisToolsCore.logger.info("A file was selected: " + chooser.getSelectedFile());
        final File file = chooser.getSelectedFile();
        if (!file.exists()) {
            AnalysisToolsCore.logger.info("File selected doesn't exist. Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", 2);
            return;
        }
        if (!file.getName().endsWith("xml")) {
            AnalysisToolsCore.logger.info("File choosen is incorrect(" + file.getName() + "). Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            return;
        }
        final JTabbedPane tempTabbedPane = new JTabbedPane(1);
        final SAXBuilder sax = new SAXBuilder();
        Document xmlLog;
        try {
            AnalysisToolsCore.logger.debug("Reading the xml file...");
            xmlLog = sax.build(file);
        }
        catch (JDOMException ex) {
            AnalysisToolsCore.logger.fatal("Something went wrong during reading: " + ex.toString() + ". Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            return;
        }
        catch (IOException ex2) {
            AnalysisToolsCore.logger.fatal("Something went wrong during reading: " + ex2.toString() + ". Stopping analyse process.");
            ex2.printStackTrace();
            return;
        }
        AnalysisToolsCore.logger.debug("Got the xml document.");
        AnalysisToolsCore.logger.debug("Preparing analysis tools...");
        final Enumeration analysisToolsIterator = AnalysisToolCreator.analyseFactories.keys();
        try {
            while (analysisToolsIterator.hasMoreElements()) {
                final String key = (String) analysisToolsIterator.nextElement();
                AnalysisTool tempTool = null;
                try {
                    tempTool = AnalysisToolCreator.createTool(key);
                    if (!((AbstractButton) this.checkboxList.get(tempTool.getToolName())).isSelected()) {
                        continue;
                    }
                    tempTool.setDocument(xmlLog);
                    AnalysisToolsCore.logger.debug("Analysing the xml with the following tool: " + tempTool.getToolName());
                    tempTabbedPane.add(tempTool.drawGraph(), tempTool.getToolName());
                }
                catch (AnalysisToolNotCreatedException ex3) {
                    AnalysisToolsCore.logger.fatal("Something went wrong when trying to use to following tool: " + key + ": " + ex3.toString());
                    ex3.printStackTrace();
                }
            }
            this.jTabbedPane.add(tempTabbedPane, "Analysis " + (this.jTabbedPane.getTabCount() + 1));
            this.jTabbedPane.setSelectedIndex(this.jTabbedPane.getTabCount() - 1);
        }
        catch (Exception ex4) {
            JOptionPane.showMessageDialog(this, "File incorrect for those analysis : " + ex4.toString() + " " + ex4.getCause(), "error", 2);
        }
    }
    
    void jMenuItemExport_actionPerformed(final ActionEvent e) {
        AnalysisToolsCore.logger.info("Beginning exportation");
        AnalysisToolsCore.logger.debug("Asking user a file to export");
        final JFileChooser chooser = Utils.newFileChooser(new String("."), new String(""), "JessX XML log files (*.xml)", ".xml");
        final int returnVal = chooser.showOpenDialog(this);
        if (returnVal != 0) {
            AnalysisToolsCore.logger.info("No file selected. Stopping analyse process.");
            return;
        }
        AnalysisToolsCore.logger.info("A file was selected: " + chooser.getSelectedFile());
        final File file = chooser.getSelectedFile();
        if (!file.exists()) {
            AnalysisToolsCore.logger.info("File selected doesn't exist. Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "No file selected.", "Error", 2);
            return;
        }
        if (!file.getName().endsWith("xml")) {
            AnalysisToolsCore.logger.info("File choosen is incorrect(" + file.getName() + "). Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            return;
        }
        final SAXBuilder sax = new SAXBuilder();
        try {
            AnalysisToolsCore.logger.debug("Reading the xml file...");
            final Document xmlLog = sax.build(file);
            AnalysisToolsCore.logger.debug("Got the xml document.");
            new XMLExportation(xmlLog, this);
        }
        catch (JDOMException ex) {
            AnalysisToolsCore.logger.error("Something went wrong during reading: " + ex.toString() + ". Stopping analyse process.");
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
        }
        catch (IOException ex2) {
            AnalysisToolsCore.logger.error("Something went wrong during reading: " + ex2.toString() + ". Stopping analyse process.");
            ex2.printStackTrace();
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
        }
    }
}
