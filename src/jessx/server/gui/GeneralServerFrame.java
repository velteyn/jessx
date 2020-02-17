// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.ProgressMonitor;
import jessx.server.net.LogGetter;
import java.awt.Point;
import java.awt.Frame;
import Trobot.Robot;
import Trobot.NotDiscreet;
import Trobot.DiscreetIT;
import Trobot.Discreet;
import jessx.net.NetworkWritable;
import jessx.net.Initialisation;
import jessx.server.Server;
import java.util.Collection;
import java.util.Vector;
import javax.swing.JFileChooser;
import org.jdom.Element;
import org.jdom.Document;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import jessx.utils.Utils;
import java.io.File;
import jessx.utils.FileChooserSave;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import javax.swing.table.TableColumn;
import java.awt.GridBagConstraints;
import java.awt.Component;
import java.awt.event.MouseListener;
import java.awt.Insets;
import javax.swing.KeyStroke;
import java.awt.event.ActionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import jessx.utils.gui.JButtonRenderer;
import javax.swing.table.TableCellRenderer;
import jessx.utils.gui.JLabelRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.ImageIcon;
import jessx.business.event.PlayerEvent;
import jessx.business.event.PlayerTypeEvent;
import java.util.Iterator;
import jessx.server.net.NetworkCore;
import jessx.business.BusinessCore;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import java.awt.GridBagLayout;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import jessx.business.event.PlayerListener;
import jessx.server.net.PlayerStateListener;
import jessx.business.event.PlayerTypeListener;
import jessx.utils.Constants;
import javax.swing.JFrame;

public class GeneralServerFrame extends JFrame implements Constants, PlayerTypeListener, PlayerStateListener, PlayerListener
{
    boolean hasAlreadySaved;
    JPanel contentPane;
    JTabbedPane jTabbedPane1;
    GridBagLayout gridBagLayout1;
    private JTree jTree1;
    private ExperimentSetupTree treeModel;
    private ServerFrame_jTree1_mouseAdapter TreemouseAdapter;
    JPanel jPanel2;
    JPanel jPanelTimer;
    JSplitPane jSplitPane1;
    JScrollPane jScrollPane2;
    GridBagLayout gridBagLayout2;
    JMenuBar menuBar;
    JMenu jMenuFile;
    JMenuItem jMenuItemLoad;
    JMenuItem jMenuItemSave;
    JMenuItem jMenuItemQuickSave;
    JMenuItem jMenuItemExit;
    JMenuItem jMenuItemClear;
    JMenu jMenuExperiment;
    JMenuItem jMenuItemOnline;
    JMenuItem jMenuItemBegin;
    JMenuItem jMenuItemNewExperiment;
    JMenuItem jMenuItemRobotConnection;
    JCheckBoxMenuItem jAutomaticallyContinue;
    JMenu jMenuHelp;
    JMenuItem jMenuHelpTutorial;
    JMenuItem jMenuHelpAbout;
    JPanel jPanel1;
    CommunicationGui jPanelCommunication;
    GridBagLayout gridBagLayout3;
    JLabel jLabel1;
    JLabel jLabel2;
    JLabel jLabel3;
    JTextField jTextField1;
    JTextField jTextField2;
    Border border1;
    Border border2;
    JScrollPane jScrollPane3;
    JTable jTable1;
    JComboBox playerTypeComboBox;
    JButton jButton1;
    JTextField jTextFieldNumberOfPlayers;
    ServerTimer timer;
    TableModelPlayersStatus tableModelPlayersStatus;
    JMenuItem jMenuItem1;
    
    public GeneralServerFrame() {
        this.hasAlreadySaved = false;
        this.jTabbedPane1 = new JTabbedPane();
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel2 = new JPanel();
        this.jPanelTimer = new JPanel();
        this.jSplitPane1 = new JSplitPane();
        this.jScrollPane2 = new JScrollPane();
        this.gridBagLayout2 = new GridBagLayout();
        this.menuBar = new JMenuBar();
        this.jMenuFile = new JMenu();
        this.jMenuItemLoad = new JMenuItem();
        this.jMenuItemSave = new JMenuItem();
        this.jMenuItemQuickSave = new JMenuItem();
        this.jMenuItemExit = new JMenuItem();
        this.jMenuItemClear = new JMenuItem();
        this.jMenuExperiment = new JMenu();
        this.jMenuItemOnline = new JMenuItem();
        this.jMenuItemBegin = new JMenuItem();
        this.jMenuItemNewExperiment = new JMenuItem();
        this.jMenuItemRobotConnection = new JMenuItem();
        this.jAutomaticallyContinue = new JCheckBoxMenuItem();
        this.jMenuHelp = new JMenu();
        this.jMenuHelpTutorial = new JMenuItem();
        this.jMenuHelpAbout = new JMenuItem();
        this.jPanel1 = new JPanel();
        this.jPanelCommunication = new CommunicationGui();
        this.gridBagLayout3 = new GridBagLayout();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jLabel3 = new JLabel();
        this.jTextField1 = new JTextField();
        this.jTextField2 = new JTextField();
        this.jScrollPane3 = new JScrollPane();
        this.jTable1 = new JTable();
        this.playerTypeComboBox = new JComboBox();
        this.jButton1 = new JButton();
        this.jTextFieldNumberOfPlayers = new JTextField("0");
        this.timer = new ServerTimer(this.jButton1, this.jTextField2);
        this.tableModelPlayersStatus = new TableModelPlayersStatus();
        this.jMenuItem1 = new JMenuItem();
        this.timer.start();
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        final Iterator iter = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
        while (iter.hasNext()) {
            this.playerTypeComboBox.addItem(iter.next());
        }
        BusinessCore.getScenario().addPlayerTypeListener(this);
        NetworkCore.addListener(this);
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (e.getEvent() == 1) {
            this.playerTypeComboBox.addItem(e.getPlayerType().getPlayerTypeName());
        }
        else if (e.getEvent() == 0) {
            this.playerTypeComboBox.removeItem(e.getPlayerType().getPlayerTypeName());
        }
        if (this.playerTypeComboBox.getItemCount() == 1) {
            this.tableModelPlayersStatus.setDefaultPlayerCategory(e.getPlayerType().getPlayerTypeName());
        }
    }
    
    public void playerListModified(final PlayerEvent e) {
        if (e.getEvent() == PlayerEvent.PLAYER_ADDED) {
            NetworkCore.getPlayer(e.getPlayerName()).addListener(this);
        }
        this.jTextFieldNumberOfPlayers.setText(new StringBuilder().append(NetworkCore.getPlayerList().size()).toString());
    }
    
    public ExperimentSetupTree getExperimentSetupTree() {
        return this.treeModel;
    }
    
    void jbInit() throws Exception {
        this.setDefaultCloseOperation(0);
        this.setIconImage(new ImageIcon("./images/logo_JessX_small.GIF").getImage());
        this.border1 = BorderFactory.createLineBorder(Color.black, 1);
        this.border2 = BorderFactory.createLineBorder(Color.black, 1);
        this.setResizable(true);
        this.setTitle("JessX server 1.6 - New Experiment");
        (this.contentPane = (JPanel)this.getContentPane()).setLayout(this.gridBagLayout2);
        this.contentPane.setBorder(null);
        this.contentPane.setEnabled(true);
        this.contentPane.setMinimumSize(new Dimension(640, 480));
        this.contentPane.setPreferredSize(new Dimension(880, 700));
        this.jTable1 = new JTable(this.tableModelPlayersStatus);
        final TableColumn playerTypeColumn = this.jTable1.getColumnModel().getColumn(1);
        playerTypeColumn.setCellEditor(new DefaultCellEditor(this.playerTypeComboBox));
        this.jTable1.getColumnModel().getColumn(3).setCellRenderer(new JLabelRenderer());
        this.jTable1.getColumnModel().getColumn(4).setCellRenderer(new JLabelRenderer());
        this.jTable1.getColumnModel().getColumn(5).setCellRenderer(new JButtonRenderer());
        this.jTable1.getColumnModel().getColumn(5).setCellEditor(new JButtonEditor2(this.tableModelPlayersStatus, this));
        this.jTable1.setCellSelectionEnabled(false);
        this.jTable1.setRowSelectionAllowed(false);
        this.jTable1.setColumnSelectionAllowed(false);
        this.treeModel = new ExperimentSetupTree(new DefaultMutableTreeNode("Experiment"));
        this.jTree1 = new JTree(this.treeModel);
        this.jTree1.getSelectionModel().setSelectionMode(1);
        this.jMenuFile.setText("File");
        this.jMenuFile.setMnemonic(70);
        this.jMenuItemLoad.setText("Open an experiment...");
        this.jMenuItemLoad.setMnemonic(79);
        this.jMenuItemLoad.addActionListener(new ServerFrame_jMenuItemLoad_actionAdapter(this));
        this.jMenuItemLoad.setAccelerator(KeyStroke.getKeyStroke(79, 2));
        this.jMenuItemSave.setText("Save the experiment As...");
        this.jMenuItemSave.setMnemonic(65);
        this.jMenuItemSave.addActionListener(new ServerFrame_jMenuItemSave_actionAdapter(this));
        this.jMenuItemQuickSave.setText("Save the experiment");
        this.jMenuItemQuickSave.setMnemonic(83);
        this.jMenuItemQuickSave.addActionListener(new ServerFrame_jMenuItemQuickSave_actionAdapter(this));
        this.jMenuItemQuickSave.setAccelerator(KeyStroke.getKeyStroke(83, 2));
        this.jMenuItemExit.setText("Exit");
        this.jMenuItemExit.setMnemonic(88);
        this.jMenuItemExit.addActionListener(new ServerFrame_jMenuItemExit_actionAdapter(this));
        this.jMenuItemExit.setAccelerator(KeyStroke.getKeyStroke(115, 8));
        this.jMenuItemClear.setText("New experiment");
        this.jMenuItemClear.setMnemonic(78);
        this.jMenuItemClear.addActionListener(new ServerFrame_jMenuItemClear_actionAdapter(this));
        this.jMenuItemClear.setAccelerator(KeyStroke.getKeyStroke(78, 2));
        this.menuBar.setMargin(new Insets(3, 3, 3, 3));
        this.jMenuExperiment.setText("Experiment");
        this.jMenuExperiment.setMnemonic(69);
        this.jMenuItemOnline.setText("Host a session");
        this.jMenuItemOnline.setAccelerator(KeyStroke.getKeyStroke(72, 2));
        this.jMenuItemOnline.setMnemonic(72);
        this.jMenuItemOnline.addActionListener(new ServerFrame_jMenuItemOnline_actionAdapter(this));
        this.jMenuItemBegin.setText("Begin the session");
        this.jMenuItemBegin.setAccelerator(KeyStroke.getKeyStroke(66, 2));
        this.jMenuItemBegin.setMnemonic(66);
        this.jMenuItemBegin.addActionListener(new ServerFrame_jMenuItemBegin_actionAdapter(this));
        this.jMenuItemBegin.setEnabled(false);
        this.jMenuItemRobotConnection.setText("Connect the robots");
        this.jMenuItemRobotConnection.setAccelerator(KeyStroke.getKeyStroke(67, 2));
        this.jMenuItemRobotConnection.setMnemonic(67);
        this.jMenuItemRobotConnection.addActionListener(new ServerFrame_jMenuItemRobotConnection_actionAdapter(this));
        this.jAutomaticallyContinue.setText("Start next period automatically");
        this.jAutomaticallyContinue.setMnemonic(83);
        this.jAutomaticallyContinue.setSelected(true);
        this.jMenuItemNewExperiment.setText("Initialize Clients");
        this.jMenuItemNewExperiment.setMnemonic(73);
        this.jMenuItemNewExperiment.addActionListener(new ServerFrame_jMenuItemNewExperiment_actionAdapter(this));
        this.jMenuHelp.setText("Help");
        this.jMenuHelp.setMnemonic(72);
        this.jMenuHelpAbout.setText("About JessX...");
        this.jMenuHelpTutorial.setText("Tutorial");
        this.jMenuHelpAbout.setMnemonic(65);
        this.jMenuHelpTutorial.setMnemonic(84);
        this.jMenuHelpAbout.addActionListener(new ServerFrame_jMenuHelpAbout_actionAdapter(this));
        this.jMenuHelpTutorial.addActionListener(new ServerFrame_jMenuHelpTutorial_actionAdapter(this));
        this.jPanel1.setLayout(this.gridBagLayout3);
        this.jLabel1.setText("Server ip / Version java :");
        this.jTextField1.setBorder(this.border2);
        this.jTextField1.setDoubleBuffered(false);
        this.jTextField1.setPreferredSize(new Dimension(2, 20));
        this.jTextField1.setEditable(false);
        this.jTextField1.setMargin(new Insets(2, 10, 2, 10));
        this.jTextField1.setText(NetworkCore.getConnectionPoint().getIpAddressAndJavaVersion());
        this.jLabel2.setText("Experiment Status : ");
        this.jLabel3.setText("Connected players :");
        this.jTextField2.setEnabled(true);
        this.jTextField2.setText("Experiment OFF");
        this.jTextField2.setBorder(this.border2);
        this.jTextField2.setPreferredSize(new Dimension(74, 20));
        this.jTextField2.setEditable(false);
        this.jTextField2.setMargin(new Insets(2, 10, 2, 10));
        this.jButton1.setEnabled(false);
        this.jButton1.addActionListener(new ServerFrame_jButton1_actionAdapter(this));
        this.jButton1.setText("Experiment off");
        this.jTextFieldNumberOfPlayers.setEnabled(true);
        this.jTextFieldNumberOfPlayers.setBorder(this.border2);
        this.jTextFieldNumberOfPlayers.setMargin(new Insets(2, 10, 2, 10));
        this.jTextFieldNumberOfPlayers.setPreferredSize(new Dimension(74, 20));
        this.jTextFieldNumberOfPlayers.setEditable(false);
        this.TreemouseAdapter = new ServerFrame_jTree1_mouseAdapter(this.jTree1, this.jPanel2);
        this.jTree1.addMouseListener(this.TreemouseAdapter);
        this.jMenuItem1.setText("Get client logs");
        this.jMenuItem1.setMnemonic(71);
        this.jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(71, 2));
        this.jMenuItem1.addActionListener(new ServerFrame_jMenuItem1_actionAdapter(this));
        this.jSplitPane1.add(this.jScrollPane2, "left");
        this.jSplitPane1.add(this.jPanel2, "right");
        this.jSplitPane1.setDividerLocation(270);
        this.jTabbedPane1.add(this.jSplitPane1, "Settings");
        this.jTabbedPane1.add(this.jPanelCommunication, "Chat");
        this.jTabbedPane1.add(this.jPanel1, "Server Status");
        this.jPanel1.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(6, 6, 3, 3), 0, 0));
        this.jPanel1.add(this.jTextField1, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 10, 2, new Insets(6, 3, 3, 6), 0, 0));
        this.jPanel1.add(this.jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 3, 3), 0, 0));
        this.jPanel1.add(this.jTextField2, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, 10, 2, new Insets(3, 3, 3, 6), 0, 0));
        this.jPanel1.add(this.jLabel3, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 3, 3), 0, 0));
        this.jPanel1.add(this.jTextFieldNumberOfPlayers, new GridBagConstraints(1, 2, 1, 1, 1.0, 0.0, 10, 2, new Insets(3, 3, 3, 6), 0, 0));
        this.jPanel1.add(this.jScrollPane3, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, 10, 1, new Insets(3, 6, 3, 6), 0, 0));
        this.jScrollPane3.getViewport().add(this.jTable1, null);
        this.jScrollPane2.getViewport().add(this.jTree1, null);
        this.contentPane.add(this.jTabbedPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelTimer.add(this.jButton1, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 6, 6), 0, 0));
        this.contentPane.add(this.jPanelTimer, new GridBagConstraints(0, 1, 0, 0, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.menuBar.add(this.jMenuFile);
        this.menuBar.add(this.jMenuExperiment);
        this.menuBar.add(this.jMenuHelp);
        this.jMenuFile.add(this.jMenuItemClear);
        this.jMenuFile.add(this.jMenuItemLoad);
        this.jMenuFile.addSeparator();
        this.jMenuFile.add(this.jMenuItemQuickSave);
        this.jMenuFile.add(this.jMenuItemSave);
        this.jMenuFile.addSeparator();
        this.jMenuFile.add(this.jMenuItemExit);
        this.jMenuExperiment.add(this.jMenuItemOnline);
        this.jMenuExperiment.add(this.jMenuItemBegin);
        this.jMenuExperiment.addSeparator();
        this.jMenuExperiment.add(this.jMenuItemRobotConnection);
        this.jMenuExperiment.addSeparator();
        this.jMenuExperiment.add(this.jMenuItem1);
        this.jMenuExperiment.addSeparator();
        this.jMenuExperiment.add(this.jAutomaticallyContinue);
        this.jMenuHelp.add(this.jMenuHelpTutorial);
        this.jMenuHelp.addSeparator();
        this.jMenuHelp.add(this.jMenuHelpAbout);
        this.setJMenuBar(this.menuBar);
    }
    
    public void playerStateChanged(final String login) {
        if (NetworkCore.getPlayer(login).getPlayerStatus() == 1 || NetworkCore.getPlayerList().size() != 1) {
            Iterator iter;
            boolean allPlayerReady;
            for (iter = NetworkCore.getPlayerList().keySet().iterator(), allPlayerReady = true; iter.hasNext() && allPlayerReady; allPlayerReady = (allPlayerReady && NetworkCore.getPlayer((String) iter.next()).getPlayerState() == 0)) {}
            if (allPlayerReady && NetworkCore.getExperimentManager().getPeriodNum() == -1) {
                NetworkCore.getExperimentManager().beginNewPeriod();
            }
            if (allPlayerReady && NetworkCore.getExperimentManager().getExperimentState() == 1) {
                this.jButton1.setText("Begin next period");
                this.jButton1.setEnabled(true);
                if (this.jAutomaticallyContinue.isSelected()) {
                    this.jButton1.doClick(0);
                }
            }
        }
    }
    
    public void displayInfoMessage(final String message) {
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == 201) {
            this.Quit_actionPerformed(null);
        }
    }
    
    void Quit_actionPerformed(final ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this, "Do you really want to quit JessX Server?", "JessX Server", 2, 2) == 0) {
            System.exit(0);
        }
    }
    
    void jMenuItemSave_actionPerformed(final ActionEvent e) {
        try {
            final FileChooserSave fileChooserSave = new FileChooserSave(this.createExperimentSetupXmlDocument(), this, "JessX Server", "xml");
            if (fileChooserSave.getAnswer() == 0) {
                JOptionPane.showMessageDialog(this, "The file has been saved correctly.", "Save", 1);
                BusinessCore.getGeneralParameters().setWorkingDirectory(fileChooserSave.getDirectoryName());
                int i;
                String name;
                for (i = 1, name = String.valueOf(fileChooserSave.getFileName()) + " Log " + i + ".xml"; new File(name).exists(); name = String.valueOf(fileChooserSave.getFileName()) + " Log " + i + ".xml") {
                    ++i;
                }
                BusinessCore.getGeneralParameters().setLoggingFileName(name);
                this.setTitle("JessX server 1.6 - " + fileChooserSave.getDirectoryName() + "\\" + fileChooserSave.getFileName() + ".xml");
                this.hasAlreadySaved = true;
            }
        }
        catch (Exception ex) {
            Utils.logger.error("Error while saving xml document. " + ex.toString());
            JOptionPane.showConfirmDialog(this, "Error occured during writing of the xml document:\n" + ex.toString(), "Error: unable to write xml document", 2, 0);
        }
    }
    
    void jMenuItemQuickSave_actionPerformed(final ActionEvent e) {
        if (this.hasAlreadySaved) {
            final XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
            try {
                sortie.output(this.createExperimentSetupXmlDocument(), new FileOutputStream(new File(String.valueOf(BusinessCore.getGeneralParameters().getWorkingDirectory()) + "\\" + BusinessCore.getGeneralParameters().getLoggingFileName().substring(0, BusinessCore.getGeneralParameters().getLoggingFileName().lastIndexOf(" Log")) + ".xml")));
                JOptionPane.showMessageDialog(this, "The file has been saved correctly.", "Save", 1);
            }
            catch (IOException ex) {
                System.out.print("Error when the file is created...\n" + ex.toString());
            }
        }
        else {
            this.jMenuItemSave_actionPerformed(null);
        }
    }
    
    private void loadXmlDocument(final Document xmlDoc) {
        final Element experiment = xmlDoc.getRootElement();
        BusinessCore.loadFromXml(experiment, this);
        this.treeModel.loadRobotConfigurationFromXml(experiment);
    }
    
    private Document createExperimentSetupXmlDocument() {
        final Document doc = new Document();
        final Element experiment = new Element("JessXSetup");
        BusinessCore.saveToXml(experiment);
        this.treeModel.saveRobotConfiguration(experiment);
        doc.setRootElement(experiment);
        return doc;
    }
    
    void jMenuItemLoad_actionPerformed(final ActionEvent e) {
        final JFileChooser chooser = Utils.newFileChooser(null, "", "xml files", "xml");
        final int option = chooser.showOpenDialog(this);
        if (option == 1) {
            return;
        }
        this.jMenuItemClear_actionPerformed(e);
        try {
            this.jMenuItemClear_actionPerformed(e);
            final Document xmlDoc = Utils.readXmlFile(chooser.getSelectedFile().getAbsolutePath());
            System.out.println(chooser.getSelectedFile().getAbsolutePath());
            this.loadXmlDocument(xmlDoc);
            int i;
            String name;
            for (i = 1, name = String.valueOf(chooser.getSelectedFile().getName().substring(0, chooser.getSelectedFile().getName().length() - 4)) + " Log " + i + ".xml"; new File(name).exists(); name = String.valueOf(chooser.getSelectedFile().getName().substring(0, chooser.getSelectedFile().getName().length() - 4)) + " Log " + i + ".xml") {
                ++i;
            }
            BusinessCore.getGeneralParameters().setLoggingFileName(name);
            BusinessCore.getGeneralParameters().setWorkingDirectory(chooser.getSelectedFile().getParent());
            this.setTitle("JessX server 1.6 - " + chooser.getSelectedFile().getAbsolutePath());
            this.hasAlreadySaved = true;
        }
        catch (Exception ex) {
            Utils.logger.error("Error reading the xml file: " + ex.toString());
            JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            return;
        }
        this.TreemouseAdapter.resetDisplay();
    }
    
    void jMenuItemClear_actionPerformed(final ActionEvent e) {
        Utils.logger.debug("Removing assets...");
        this.setTitle("JessX server 1.6 - New Experiment");
        this.hasAlreadySaved = false;
        final Vector iterAsset = new Vector(BusinessCore.getAssets().keySet());
        for (int i = 0; i < iterAsset.size(); ++i) {
            BusinessCore.removeAsset(BusinessCore.getAsset(iterAsset.elementAt(i).toString()));
        }
        Utils.logger.debug("Removing assets...");
        final Vector iterPT = new Vector(BusinessCore.getScenario().getPlayerTypes().keySet());
        for (int j = 0; j < iterPT.size(); ++j) {
            BusinessCore.getScenario().removePlayerType(BusinessCore.getScenario().getPlayerType(iterPT.elementAt(j).toString()));
        }
        BusinessCore.getGeneralParameters().initializeGeneralParameters();
        this.treeModel.messagesServerGenericGui.removeAllInfo();
        this.TreemouseAdapter.resetDisplay();
        this.treeModel.initializeRobotPanel();
    }
    
    void jMenuItemOnline_actionPerformed(final ActionEvent e) {
        Server.setServerState(Server.SERVER_STATE_ONLINE);
        this.jMenuItemOnline.setEnabled(false);
        this.jMenuItemBegin.setEnabled(true);
    }
    
    void jMenuItemNewExperiment_actionPerformed(final ActionEvent e) {
        final Iterator iterPlayer = NetworkCore.getPlayerList().keySet().iterator();
        while (iterPlayer.hasNext()) {
            NetworkCore.getPlayer((String) iterPlayer.next()).send(new Initialisation());
        }
    }
    
    void jMenuItemBegin_actionPerformed(final ActionEvent e) {
        System.out.println("jMenuItemBegin_actionPerformed d\u00e9but");
        if (NetworkCore.getExperimentManager().beginExperiment(this)) {
            new MessageTimer((Vector)BusinessCore.getScenario().getListInformation().clone()).start();
            this.jMenuItemBegin.setEnabled(false);
            this.jMenuItemSave.setEnabled(false);
            this.jMenuItemQuickSave.setEnabled(false);
            this.jMenuItemLoad.setEnabled(false);
            this.jMenuItemClear.setEnabled(false);
            this.jMenuItemRobotConnection.setEnabled(false);
            this.treeModel.setAllUneditable();
            this.TreemouseAdapter.refreshDisplay();
        }
    }
    
    void jMenuItemRobotConnection_actionPerformed(final ActionEvent e) {
        final int temp = this.treeModel.zitDiscreetServerGui.getJSliderFrequency();
        final int tempIT = this.treeModel.zitDiscreetITServerGui.getJSliderFrequency();
        for (int i = 0; i < this.treeModel.zitDiscreetServerGui.getJSpinnerNumberOfRobots(); ++i) {
            final Robot zitDiscreet = new Discreet(i, temp);
            System.out.println("dans for, apr\u00e8s cr\u00e9ation du discreet " + i + " et avant start");
            zitDiscreet.start();
            System.out.println("apr\u00e8s start du discreet " + i);
        }
        for (int i = 0; i < this.treeModel.zitDiscreetITServerGui.getJSpinnerNumberOfRobots(); ++i) {
            final Robot zitDiscreetIT = new DiscreetIT(i, tempIT);
            System.out.println("dans for, apr\u00e8s cr\u00e9ation du discreet " + i + " et avant start");
            zitDiscreetIT.start();
            System.out.println("apr\u00e8s start du discreetIT " + i);
        }
        for (int i = 0; i < this.treeModel.zitNotDiscreetServeurGui.getJSpinnerNumberOfRobots(); ++i) {
            final Robot zitNotDiscreet = new NotDiscreet(i, this.treeModel.zitNotDiscreetServeurGui.getJSliderFrequency(), this.treeModel.zitNotDiscreetServeurGui.getJSpinnerLowLimit(), this.treeModel.zitNotDiscreetServeurGui.getJSpinnerHighLimit());
            zitNotDiscreet.start();
        }
    }
    
    public void jMenuHelpAbout_actionPerformed(final ActionEvent e) {
        final ServerAboutBox dlg = new ServerAboutBox(this);
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = this.getSize();
        final Point loc = this.getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }
    
    public void jMenuHelpTutorial_actionPerformed(final ActionEvent e) {
        final String directory = System.getProperty("user.dir");
        final File file = new File(String.valueOf(directory) + System.getProperty("file.separator") + "tutoriel" + System.getProperty("file.separator") + "help.html");
        try {
            final Process p = Runtime.getRuntime().exec("rundll32 SHELL32.DLL,ShellExec_RunDLL " + file.getAbsolutePath());
            p.waitFor();
        }
        catch (IOException ex1) {
            JOptionPane.showConfirmDialog(this, "An error occured with the tutorial.\nGo to our web site to see it.", "JessX Server", 0, 0);
            Utils.logger.error("ERROR with File help");
        }
        catch (InterruptedException ex2) {
            JOptionPane.showConfirmDialog(this, "An error occured with the tutorial.\nGo to our web site to see it.", "JessX Server", 0, 0);
            Utils.logger.error("ERROR with File help");
        }
    }
    
    void jButton1_actionPerformed(final ActionEvent e) {
        this.jButton1.setEnabled(false);
        if (NetworkCore.getExperimentManager().getExperimentState() == 1) {
            NetworkCore.getExperimentManager().beginNewPeriod();
        }
        else {
            this.jButton1.setEnabled(false);
            this.jButton1.setText("Experiment off");
            this.jMenuItemBegin.setEnabled(true);
            this.jMenuItemSave.setEnabled(true);
            this.jMenuItemQuickSave.setEnabled(true);
            this.jMenuItemLoad.setEnabled(true);
            this.jMenuItemRobotConnection.setEnabled(true);
            this.jMenuItemClear.setEnabled(true);
            this.treeModel.setAllEditable();
            this.TreemouseAdapter.refreshDisplay();
        }
    }
    
    void jMenuItem1_actionPerformed(final ActionEvent e) {
        new Thread(new Runnable() {
            public void run() {
                new LogGetter(24456).getLogs(new ProgressMonitor(null, "Chargement des logs ...", "Demarrage", 1, NetworkCore.getPlayerList().size()));
            }
        }).start();
    }
}
