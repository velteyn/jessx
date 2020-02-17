// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import javax.swing.event.ChangeEvent;
import jessx.business.Operator;
import java.awt.event.WindowEvent;
import java.awt.Point;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionListener;
import javax.swing.KeyStroke;
import java.awt.SystemColor;
import java.awt.LayoutManager;
import javax.swing.BorderFactory;
import javax.swing.border.EtchedBorder;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.util.Iterator;
import jessx.net.NetworkWritable;
import java.awt.Component;
import jessx.utils.PopupWithTimer;
import jessx.utils.Utils;
import jessx.net.ExpUpdate;
import jessx.business.Deal;
import org.jdom.Document;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import jessx.client.ClientCore;
import jessx.gclient.net.ServerPlainMessageListener;
import java.util.HashMap;
import java.util.ArrayList;
import javax.swing.table.TableModel;
import java.util.Map;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import java.util.Hashtable;
import jessx.client.event.ConnectionListener;
import jessx.client.event.NetworkListener;
import jessx.client.event.ExperimentDeveloppmentListener;
import jessx.client.event.OperatorPlayedListener;
import jessx.utils.Constants;
import javax.swing.JFrame;

public class GClientFrame extends JFrame implements Constants, OperatorPlayedListener, ExperimentDeveloppmentListener, NetworkListener, ConnectionListener
{
    private static Hashtable evolutionGraphFactories;
    private int indexcomm1;
    private int indexcomm2;
    JPanel contentPaneGeneral;
    JTabbedPane jTabbedPaneGeneral;
    JMenuBar jMenuBar1;
    JMenu jMenuFile;
    JMenuItem jMenuFileExit;
    JMenu jMenuHelp;
    JMenuItem jMenuHelpAbout;
    JLabel statusBar;
    TitledBorder titledBorder1;
    JPanel jPanelManagedAssets;
    JPanel jPanelCommunication;
    Border border1;
    Border border2;
    TitledBorder titledBorder2;
    Border border3;
    Border border4;
    TitledBorder titledBorder3;
    JTextArea jTextAreaCommunication;
    JTextArea jTextAreaCommunication2;
    JPanel jPanelAssetInfo;
    TitledBorder titledBorder4;
    TitledBorder titledBorder20;
    Border border5;
    Border border6;
    Border border7;
    TitledBorder titledBorder5;
    Border border8;
    TitledBorder titledBorder7;
    ButtonGroup buttonGroup1;
    TitledBorder titledBorder8;
    Border border9;
    TitledBorder titledBorder9;
    ButtonGroup buttonGroup2;
    JScrollPane jScrollPaneManagedAssets;
    JPanel jPanelTransactionsProperties;
    TitledBorder titledBorder10;
    JTabbedPane jPaneMarketProperties;
    TitledBorder titledBorder11;
    GridBagLayout gridBagLayout;
    GridBagLayout gridBagLayoutCommunication;
    GridBagLayout gridBagLayoutMarketProperties;
    GridBagLayout gridBagLayoutAssetInfo;
    GridBagLayout gridBagLayoutGeneral;
    PortfolioTableModel tableJTable2Model;
    JTable jTableManagedAssets;
    GridBagLayout gridBagLayoutManagedAssets;
    GridBagLayout gridBagLayoutTransactionsProperties;
    TitledBorder titledBorder12;
    Border border10;
    JMenuItem jMenuItem1;
    JMenuItem jMenuItem2;
    JScrollPane jScrollPaneCommunication;
    JScrollPane jScrollPaneCommunication2;
    JSplitPane jSplitPane2;
    JPanel jPanelWest;
    JPanel jPanelEast;
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    JSplitPane jSplitPaneInfoComm;
    JSplitPane jSplitPane3;
    TitledBorder titledBorder14;
    TitledBorder titledBorder15;
    Border border11;
    TitledBorder titledBorder16;
    TitledBorder titledBorder17;
    JTabbedPane jTabbedPaneServerComm;
    JPanel jPanelCommunication2;
    JLabel jTextFieldTimer;
    JTextField jTextFieldConnect;
    JTextField jTextFieldName;
    JTabbedPane jTabbedPane1;
    JPanel jTabbedPane2;
    JLabel jLabel1;
    Border border13;
    JLabel jLabel2;
    Border border12;
    JTabbedPane jTabbedPane3;
    ClientTimer timer;
    private int lastIndexPanel1;
    private int lastIndexPanel2;
    private int lastIndexPanel3;
    private int lastIndexPanel4;
    private int lastIndexPanelComm;
    private int lastIndexProperties;
    private List operationPaneList;
    private List serverCommList;
    private List managedAssetsList;
    private Map marketMap;
    
    static {
        GClientFrame.evolutionGraphFactories = new Hashtable();
    }
    
    public GClientFrame() {
        this.indexcomm1 = 0;
        this.indexcomm2 = 1;
        this.jTabbedPaneGeneral = new JTabbedPane();
        this.jMenuBar1 = new JMenuBar();
        this.jMenuFile = new JMenu();
        this.jMenuFileExit = new JMenuItem();
        this.jMenuHelp = new JMenu();
        this.jMenuHelpAbout = new JMenuItem();
        this.statusBar = new JLabel();
        this.jPanelManagedAssets = new JPanel();
        this.jPanelCommunication = new JPanel();
        this.jTextAreaCommunication = new JTextArea();
        this.jTextAreaCommunication2 = new JTextArea();
        this.jPanelAssetInfo = new JPanel();
        this.buttonGroup1 = new ButtonGroup();
        this.buttonGroup2 = new ButtonGroup();
        this.jScrollPaneManagedAssets = new JScrollPane();
        this.jPanelTransactionsProperties = new JPanel();
        this.jPaneMarketProperties = new JTabbedPane();
        this.gridBagLayout = new GridBagLayout();
        this.gridBagLayoutCommunication = new GridBagLayout();
        this.gridBagLayoutMarketProperties = new GridBagLayout();
        this.gridBagLayoutAssetInfo = new GridBagLayout();
        this.gridBagLayoutGeneral = new GridBagLayout();
        this.tableJTable2Model = new PortfolioTableModel(new String[] { "Asset Name", " Quantity" });
        this.jTableManagedAssets = new JTable(this.tableJTable2Model);
        this.gridBagLayoutManagedAssets = new GridBagLayout();
        this.gridBagLayoutTransactionsProperties = new GridBagLayout();
        this.jMenuItem1 = new JMenuItem();
        this.jMenuItem2 = new JMenuItem();
        this.jScrollPaneCommunication = new JScrollPane();
        this.jScrollPaneCommunication2 = new JScrollPane();
        this.jSplitPane2 = new JSplitPane();
        this.jPanelWest = new JPanel();
        this.jPanelEast = new JPanel();
        this.gridBagLayout1 = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.jSplitPaneInfoComm = new JSplitPane();
        this.jSplitPane3 = new JSplitPane();
        this.jTabbedPaneServerComm = new JTabbedPane();
        this.jPanelCommunication2 = new JPanel();
        this.jTextFieldTimer = new JLabel();
        this.jTextFieldConnect = new JTextField();
        this.jTextFieldName = new JTextField();
        this.jTabbedPane1 = new JTabbedPane();
        this.jTabbedPane2 = new JPanel();
        this.jLabel1 = new JLabel();
        this.jLabel2 = new JLabel();
        this.jTabbedPane3 = new JTabbedPane();
        this.timer = new ClientTimer(this.jTextFieldTimer);
        this.lastIndexPanel1 = -1;
        this.lastIndexPanel2 = -1;
        this.lastIndexPanel3 = -1;
        this.lastIndexPanel4 = -1;
        this.lastIndexPanelComm = -1;
        this.lastIndexProperties = -1;
        this.operationPaneList = new ArrayList();
        this.serverCommList = new ArrayList();
        this.managedAssetsList = new ArrayList();
        this.marketMap = new HashMap();
        this.setDefaultCloseOperation(0);
        new ServerPlainMessageListener(this);
        this.enableEvents(64L);
        JFrame.setDefaultLookAndFeelDecorated(false);
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ClientCore.addOperatorPLayedListener(this);
        ClientCore.getExperimentManager().addListener(this);
        ClientCore.addNetworkListener(this, "ExperimentUpdate");
        ClientCore.addNetworkListener(this, "Initialisation");
        ClientCore.addNetworkListener(this, "Message");
        ClientCore.addNetworkListener(this, "Deal");
        ClientCore.addNetworkListener(this, "Institution");
        ClientCore.addNetworkListener(this, "Information");
        ClientCore.addNetworkListener(this, "Warn");
        ClientCore.addConnectionListener(this);
        this.timer.start();
        this.jTextFieldConnect.setBackground(new Color(220, 160, 155));
    }
    
    public void experimentBegins() {
        this.jTextFieldTimer.setFont(new Font("Verdana", 1, 12));
        this.jTextFieldTimer.setMaximumSize(new Dimension(550, 30));
        this.jTextFieldTimer.setMinimumSize(new Dimension(550, 30));
        this.jTextFieldTimer.setPreferredSize(new Dimension(550, 30));
        this.jTextFieldTimer.setText("> Waiting for all the players to read their information <");
        this.validate();
    }
    
    public void experimentFinished() {
        System.exit(0);
    }
    
    public void periodBegins() {
        this.startEdition();
        this.jTextFieldTimer.setText("");
        this.jTextFieldTimer.validate();
        this.jTextFieldTimer.setFont(new Font("Verdana", 1, 18));
        this.jTextFieldTimer.setMaximumSize(new Dimension(250, 30));
        this.jTextFieldTimer.setMinimumSize(new Dimension(250, 30));
        this.jTextFieldTimer.setPreferredSize(new Dimension(250, 30));
        this.validate();
    }
    
    public void periodFinished() {
        this.stopEdition();
    }
    
    public void objectReceived(final Document xmlDoc) {
        if (xmlDoc.getRootElement().getName().equals("Initialisation")) {
            System.out.print("\nInitialisation Client\n");
            ClientCore.initializeForNewExperiment();
            GClientFrame.evolutionGraphFactories.clear();
            this.jTabbedPane1.removeAll();
            this.jTabbedPane2.removeAll();
            this.jTabbedPane3.removeAll();
            this.jPaneMarketProperties.removeAll();
            this.jTextAreaCommunication.setText("");
            this.jTextAreaCommunication2.setText("");
        }
        if (xmlDoc.getRootElement().getName().equals("Deal")) {
            final Deal deal = new Deal("", 0.0f, 0, 0L, "", "", 0.0f, "", "");
            if (deal.initFromNetworkInput(xmlDoc.getRootElement()) && ClientCore.getInstitution(deal.getDealInstitution()) != null) {
                final MarketEvolutionGraph marketEvolution = (MarketEvolutionGraph) this.marketMap.get(deal.getDealInstitution());
                marketEvolution.addDeal(deal);
            }
        }
        else if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
            final ExpUpdate update = new ExpUpdate(-1, "", -1);
            update.initFromNetworkInput(xmlDoc.getRootElement());
            update.getUpdateType();
            if (update.getUpdateType() == 7 || update.getUpdateType() == 1) {
                Utils.logger.debug("Displaying the ready message...");
                final String mess = update.getUpdateMessage();
                final int rows = mess.length() / 50 + 1;
                final int columns = (rows < 2) ? mess.length() : 50;
                final JTextArea jTextArea = new JTextArea(mess, rows, columns);
                jTextArea.setEditable(false);
                jTextArea.setOpaque(false);
                jTextArea.setAutoscrolls(true);
                jTextArea.setWrapStyleWord(true);
                jTextArea.setVisible(true);
                jTextArea.setBackground(new Color(203, 230, 211));
                new PopupWithTimer(30, jTextArea, jTextArea.getPreferredSize(), "JessX client", this).run();
                Utils.logger.debug("sending the server the ready signal...");
                ClientCore.send(update);
            }
        }
        else if (xmlDoc.getRootElement().getName().equals("Message")) {
            final Iterator it_pane = this.serverCommList.iterator();
            while (it_pane.hasNext()) {
                ((PanelServerComm) it_pane.next()).messageReceived(xmlDoc.getRootElement().getText());
            }
        }
        else if (xmlDoc.getRootElement().getName().equals("Warn")) {
            final String mess2 = xmlDoc.getRootElement().getText();
            final int rows2 = mess2.length() / 50 + 1;
            final int columns2 = (rows2 < 2) ? mess2.length() : 50;
            final JTextArea jTextArea2 = new JTextArea(mess2, rows2, columns2);
            jTextArea2.setEditable(false);
            jTextArea2.setOpaque(false);
            jTextArea2.setAutoscrolls(true);
            jTextArea2.setWrapStyleWord(true);
            jTextArea2.setVisible(true);
            jTextArea2.setBackground(new Color(203, 230, 211));
            final int time = Math.min(Math.round((float)(ClientCore.getExperimentManager().getRemainingTimeInPeriod() / 1000L)), 15);
            new PopupWithTimer(time, jTextArea2, jTextArea2.getPreferredSize(), "JessX client", this).start();
        }
        else if (xmlDoc.getRootElement().getName().equals("Information")) {
            final Iterator it_pane = this.serverCommList.iterator();
            while (it_pane.hasNext()) {
                ((PanelServerComm) it_pane.next()).informationReceived(xmlDoc.getRootElement().getText());
            }
        }
    }
    
    public void connectionStateChanged(final int newState) {
        if (newState == 0) {
            this.jMenuItem1.setEnabled(true);
            this.jMenuItem2.setEnabled(false);
            this.jTextFieldConnect.setText("DISCONNECTED");
            this.jTextFieldConnect.setBackground(new Color(220, 160, 155));
            JOptionPane.showMessageDialog(this, "Connection to server has been closed (see the log for information)", "Network Error", 0);
        }
        else if (newState == 1) {
            this.jTextFieldConnect.setText("CONNECTED");
            this.jTextFieldConnect.setBackground(new Color(145, 220, 180));
            this.jMenuItem1.setEnabled(false);
            this.jTextFieldTimer.setAlignmentX(0.5f);
            this.jTextFieldTimer.setFont(new Font("Verdana", 1, 12));
            this.jTextFieldTimer.setMaximumSize(new Dimension(350, 30));
            this.jTextFieldTimer.setMinimumSize(new Dimension(350, 30));
            this.jTextFieldTimer.setPreferredSize(new Dimension(350, 30));
            this.jTextFieldTimer.setText("> Waiting for other players to connect <");
        }
    }
    
    public void jbInit() throws Exception {
        this.setIconImage(new ImageIcon("./images/logo_JessX_small.GIF").getImage());
        this.contentPaneGeneral = (JPanel)this.getContentPane();
        this.titledBorder1 = new TitledBorder(new EtchedBorder(0, Color.white, new Color(148, 145, 140)), "Assets", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.border1 = new TitledBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6), "My Portfolio", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.border2 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.titledBorder2 = new TitledBorder(this.border2, "Communication");
        this.border3 = BorderFactory.createCompoundBorder(this.titledBorder2, BorderFactory.createEmptyBorder(6, 6, 6, 6));
        this.border4 = new EtchedBorder(0, Color.white, new Color(148, 145, 140));
        this.titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Market");
        this.titledBorder4 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "Asset information", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.border5 = new EtchedBorder(0, Color.white, new Color(178, 178, 178));
        this.border6 = BorderFactory.createBevelBorder(1, Color.white, Color.white, new Color(124, 124, 124), new Color(178, 178, 178));
        this.border7 = BorderFactory.createEtchedBorder(Color.white, new Color(178, 178, 178));
        this.titledBorder5 = new TitledBorder("");
        this.border8 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140)), "Action");
        this.titledBorder8 = new TitledBorder("");
        this.border9 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.titledBorder9 = new TitledBorder(this.border9, "Order type");
        this.titledBorder10 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "Transaction properties", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.titledBorder11 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "Market properties", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.titledBorder12 = new TitledBorder("");
        this.border10 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.titledBorder14 = new TitledBorder("");
        this.titledBorder15 = new TitledBorder("");
        this.border11 = BorderFactory.createEtchedBorder(Color.white, new Color(148, 145, 140));
        this.titledBorder16 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.border13 = BorderFactory.createEmptyBorder();
        this.titledBorder17 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "", 0, 0, GClientFrame.FONT_CLIENT_TITLE_BORDER);
        this.border12 = BorderFactory.createEmptyBorder();
        this.contentPaneGeneral.setLayout(this.gridBagLayoutGeneral);
        this.setResizable(true);
        this.setSize(new Dimension(905, 737));
        this.setState(0);
        this.setTitle("JessX client 1.6");
        this.statusBar.setBackground(SystemColor.activeCaptionBorder);
        this.statusBar.setFont(new Font("MS Sans Serif", 0, 11));
        this.statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
        this.statusBar.setOpaque(false);
        this.statusBar.setText("Status Bar");
        this.statusBar.setVisible(false);
        this.jMenuFile.setText("File");
        this.jMenuFile.setMnemonic(70);
        this.jMenuFileExit.setText("Exit");
        this.jMenuFileExit.setMnemonic(88);
        this.jMenuFileExit.setAccelerator(KeyStroke.getKeyStroke(115, 8));
        this.jMenuFileExit.addActionListener(new Principale_jMenuFileExit_ActionAdapter(this));
        this.jMenuHelp.setText("Help");
        this.jMenuHelp.setMnemonic(72);
        this.jMenuHelpAbout.setText("About JessX...");
        this.jMenuHelpAbout.setMnemonic(65);
        this.jMenuHelpAbout.addActionListener(new Principale_jMenuHelpAbout_ActionAdapter(this));
        this.titledBorder1.setTitle("Assets");
        this.jPanelManagedAssets.setEnabled(true);
        this.jPanelManagedAssets.setBorder(this.border1);
        this.jPanelManagedAssets.setMinimumSize(new Dimension(50, 50));
        this.jPanelManagedAssets.setOpaque(true);
        this.jPanelManagedAssets.setPreferredSize(new Dimension(200, 130));
        this.jPanelManagedAssets.setLayout(this.gridBagLayoutManagedAssets);
        this.jPanelCommunication.setBorder(this.titledBorder16);
        this.jPanelCommunication.setMinimumSize(new Dimension(20, 100));
        this.jPanelCommunication.setPreferredSize(new Dimension(150, 100));
        this.jPanelCommunication.setLayout(this.gridBagLayoutCommunication);
        this.jPanelCommunication2.setBorder(this.titledBorder17);
        this.jPanelCommunication2.setMinimumSize(new Dimension(20, 100));
        this.jPanelCommunication2.setPreferredSize(new Dimension(150, 100));
        this.jPanelCommunication2.setLayout(this.gridBagLayoutCommunication);
        this.contentPaneGeneral.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        this.contentPaneGeneral.setMinimumSize(new Dimension(800, 600));
        this.contentPaneGeneral.setPreferredSize(new Dimension(800, 600));
        this.jTextAreaCommunication.setBackground(new Color(203, 230, 211));
        this.jTextAreaCommunication.setEnabled(true);
        this.jTextAreaCommunication.setBorder(BorderFactory.createLoweredBevelBorder());
        this.jTextAreaCommunication.setEditable(false);
        this.jTextAreaCommunication.setSelectedTextColor(Color.white);
        this.jTextAreaCommunication.setLineWrap(true);
        this.jTextAreaCommunication.setWrapStyleWord(true);
        this.jTextAreaCommunication.setFont(GClientFrame.FONT_CLIENT_TEXTAREA);
        this.jTextAreaCommunication2.setBackground(new Color(203, 230, 211));
        this.jTextAreaCommunication2.setEnabled(true);
        this.jTextAreaCommunication2.setBorder(BorderFactory.createLoweredBevelBorder());
        this.jTextAreaCommunication2.setEditable(false);
        this.jTextAreaCommunication2.setSelectedTextColor(Color.white);
        this.jTextAreaCommunication2.setLineWrap(true);
        this.jTextAreaCommunication2.setWrapStyleWord(true);
        this.jTextAreaCommunication2.setFont(GClientFrame.FONT_CLIENT_TEXTAREA);
        this.jPanelAssetInfo.setAlignmentX(0.5f);
        this.jPanelAssetInfo.setBorder(BorderFactory.createEmptyBorder());
        this.jPanelAssetInfo.setMinimumSize(new Dimension(20, 100));
        this.jPanelAssetInfo.setPreferredSize(new Dimension(150, 100));
        this.jPanelAssetInfo.setLayout(this.gridBagLayoutAssetInfo);
        this.jScrollPaneManagedAssets.setHorizontalScrollBarPolicy(30);
        this.jScrollPaneManagedAssets.setAutoscrolls(false);
        this.jScrollPaneManagedAssets.setRequestFocusEnabled(true);
        this.jPanelTransactionsProperties.setFont(new Font("Verdana", 1, 13));
        this.jPanelTransactionsProperties.setBorder(this.titledBorder10);
        this.jPanelTransactionsProperties.setLayout(this.gridBagLayoutTransactionsProperties);
        this.jPaneMarketProperties.setBorder(this.titledBorder11);
        this.jPaneMarketProperties.setMinimumSize(new Dimension(16, 10));
        this.jPaneMarketProperties.setPreferredSize(new Dimension(291, 169));
        this.jTextFieldTimer.setHorizontalAlignment(0);
        this.jSplitPane2.setOrientation(1);
        this.jSplitPane2.setBottomComponent(null);
        this.jSplitPane2.setDividerSize(5);
        this.jSplitPane2.setLeftComponent(this.jPanelWest);
        this.jSplitPane2.setRightComponent(this.jPanelEast);
        this.jSplitPane2.setTopComponent(null);
        this.jPanelWest.setLayout(this.gridBagLayout1);
        this.jPanelEast.setLayout(this.gridBagLayout2);
        this.jSplitPane3.setOrientation(0);
        this.jSplitPane3.setMinimumSize(new Dimension(86, 300));
        this.jSplitPane3.setDividerSize(4);
        this.jSplitPaneInfoComm.setOrientation(0);
        this.jSplitPaneInfoComm.setPreferredSize(new Dimension(152, 100));
        this.jSplitPaneInfoComm.setBottomComponent(this.jTabbedPaneServerComm);
        this.jSplitPaneInfoComm.setDividerSize(4);
        this.jSplitPaneInfoComm.setLastDividerLocation(1);
        this.jSplitPaneInfoComm.setTopComponent(this.jPanelAssetInfo);
        this.jPanelWest.setMinimumSize(new Dimension(318, 600));
        this.jPanelWest.setPreferredSize(new Dimension(300, 600));
        this.jPanelWest.setRequestFocusEnabled(true);
        this.jTextFieldTimer.setEnabled(true);
        this.jTextFieldTimer.setFont(new Font("Verdana", 1, 18));
        this.jTextFieldTimer.setBorder(null);
        this.jTextFieldTimer.setMaximumSize(new Dimension(400, 30));
        this.jTextFieldTimer.setMinimumSize(new Dimension(400, 30));
        this.jTextFieldTimer.setPreferredSize(new Dimension(400, 30));
        this.jTextFieldTimer.setAlignmentX(0.5f);
        this.jTextFieldName.setText("No name");
        this.jTextFieldName.setEditable(false);
        this.jTextFieldName.setPreferredSize(new Dimension(90, 20));
        this.jTextFieldName.setMaximumSize(new Dimension(200, 20));
        this.jTextFieldName.setMinimumSize(new Dimension(90, 20));
        this.jTextFieldName.setHorizontalAlignment(0);
        this.jTextFieldConnect.setText("DISCONNECTED");
        this.jTextFieldConnect.setEditable(false);
        this.jTextFieldConnect.setPreferredSize(new Dimension(110, 20));
        this.jTextFieldConnect.setMaximumSize(new Dimension(110, 20));
        this.jTextFieldConnect.setMinimumSize(new Dimension(110, 20));
        this.jTextFieldConnect.setHorizontalAlignment(0);
        this.jMenuItem1.setText("Join a session...");
        this.jMenuItem2.setText("Disconnect");
        this.jMenuItem1.setMnemonic(74);
        this.jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(74, 2));
        this.jMenuItem2.setMnemonic(68);
        this.jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(68, 2));
        this.jMenuItem1.addActionListener(new ClientFrame_jMenuItem1_actionAdapter(this));
        this.jMenuItem2.addActionListener(new ClientFrame_jMenuItem2_actionAdapter(this));
        this.jTabbedPane1.setTabPlacement(1);
        this.jTabbedPane1.setMinimumSize(new Dimension(100, 200));
        this.jTabbedPane1.addChangeListener(new GClientFrame_jTabbedPane1_changeAdapter(this));
        this.jLabel1.setEnabled(true);
        this.jLabel1.setFont(GClientFrame.FONT_CLIENT_LABEL_UP);
        this.jLabel2.setHorizontalAlignment(11);
        this.jLabel2.setHorizontalTextPosition(11);
        this.jLabel2.setText("");
        this.jLabel2.setFont(GClientFrame.FONT_CLIENT_LABEL_UP);
        this.jTabbedPane2.setBorder(this.border12);
        this.jTabbedPane3.addChangeListener(new GClientFrame_jTabbedPane3_changeAdapter(this));
        this.jTabbedPaneServerComm.addChangeListener(new GClientFrame_jTabbedPaneServerComm_changeAdapter(this));
        this.jTabbedPaneGeneral.addChangeListener(new GClientFrame_jTabbedPaneGeneral_changeAdapter(this));
        this.jPaneMarketProperties.addChangeListener(new GClientFrame_jPaneMarketProperties_changeAdapter(this));
        this.contentPaneGeneral.add(this.jTabbedPaneGeneral, new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jSplitPane2.add(this.jPanelWest, "top");
        this.jSplitPane2.add(this.jPanelEast, "bottom");
        this.jMenuFile.add(this.jMenuItem1);
        this.jMenuFile.add(this.jMenuItem2);
        this.jTabbedPaneGeneral.add("General", this.jSplitPane2);
        this.jMenuItem2.setEnabled(false);
        this.jMenuFile.addSeparator();
        this.jMenuFile.add(this.jMenuFileExit);
        this.jMenuHelp.add(this.jMenuHelpAbout);
        this.jMenuBar1.add(this.jMenuFile);
        this.jMenuBar1.add(this.jMenuHelp);
        this.setJMenuBar(this.jMenuBar1);
        this.contentPaneGeneral.add(this.statusBar, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0, 10, 1, new Insets(0, 0, 0, 0), 686, 0));
        this.contentPaneGeneral.add(this.jTextFieldConnect, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.contentPaneGeneral.add(this.jTextFieldName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 13, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.contentPaneGeneral.add(this.jTextFieldTimer, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.contentPaneGeneral.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 10, 0, 0), 0, 0));
        this.contentPaneGeneral.add(this.jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 13, 0, new Insets(0, 0, 0, 10), 0, 0));
        this.jPanelCommunication.add(this.jScrollPaneCommunication, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 2, 4), 0, 0));
        this.jScrollPaneCommunication.getViewport().add(this.jTextAreaCommunication, null);
        this.jPanelCommunication2.add(this.jScrollPaneCommunication2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 2, 4), 0, 0));
        this.jScrollPaneCommunication2.getViewport().add(this.jTextAreaCommunication2, null);
        this.jPanelTransactionsProperties.add(this.jTabbedPane3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelWest.add(this.jPanelManagedAssets, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1, 10, 1, new Insets(10, 0, 0, 0), 0, 0));
        this.jPanelManagedAssets.add(this.jScrollPaneManagedAssets, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelWest.add(this.jSplitPaneInfoComm, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelWest.add(this.jPanelTransactionsProperties, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 12, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jSplitPaneInfoComm.add(this.jPanelAssetInfo, "top");
        this.jPanelAssetInfo.add(this.jTabbedPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jTabbedPaneServerComm.add(this.jPanelCommunication, "Financial Information", this.indexcomm1);
        this.jTabbedPaneServerComm.add(this.jPanelCommunication2, "Press Review", this.indexcomm2);
        this.jSplitPaneInfoComm.add(this.jTabbedPaneServerComm, "bottom");
        this.jPanelEast.add(this.jSplitPane3, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jSplitPane3.add(this.jPaneMarketProperties, "bottom");
        this.jSplitPane3.add(this.jTabbedPane1, "top");
        this.jSplitPaneInfoComm.setDividerLocation(150);
        this.jSplitPane2.setDividerLocation(450);
        this.jSplitPane3.setDividerLocation(400);
    }
    
    public void jMenuFileExit_actionPerformed(final ActionEvent e) {
        if (JOptionPane.showConfirmDialog(this, "Do you really want to quit JessX Client?", "JessX Client", 2, 2) == 0) {
            System.exit(0);
        }
    }
    
    public void jMenuHelpAbout_actionPerformed(final ActionEvent e) {
        final ClientAboutBox dlg = new ClientAboutBox(this);
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
    
    void jMenuItem1_actionPerformed(final ActionEvent e) {
        new ConnectionPopup(this);
        this.jTextFieldName.setText(ClientCore.getLogin());
    }
    
    void jMenuItem2_actionPerformed(final ActionEvent e) {
        this.jTextFieldName.setText("");
    }
    
    public void newOperator(final Operator op) {
        if (op != null) {
            Utils.logger.debug("Adding the client operator panels...");
            final JPanel jPanelAssetInfo2 = new JPanel();
            final JPanel jPanelTransactionsProperties2 = new JPanel();
            final JSplitPane NewPane = new JSplitPane();
            final JPanel jPanelWest2 = new JPanel();
            final JPanel jPanelEast2 = new JPanel();
            final JSplitPane jSplitPane32 = new JSplitPane();
            final JSplitPane jSplitPaneInfoComm2 = new JSplitPane();
            jPanelAssetInfo2.setAlignmentX(0.5f);
            jPanelAssetInfo2.setBorder(BorderFactory.createEmptyBorder());
            jPanelAssetInfo2.setMinimumSize(new Dimension(20, 100));
            jPanelAssetInfo2.setPreferredSize(new Dimension(150, 100));
            jPanelAssetInfo2.setLayout(this.gridBagLayoutAssetInfo);
            jPanelTransactionsProperties2.setFont(new Font("Verdana", 1, 13));
            jPanelTransactionsProperties2.setBorder(this.titledBorder10);
            jPanelTransactionsProperties2.setLayout(this.gridBagLayoutTransactionsProperties);
            NewPane.setOrientation(1);
            NewPane.setBottomComponent(null);
            NewPane.setDividerSize(5);
            NewPane.setLeftComponent(jPanelWest2);
            NewPane.setRightComponent(jPanelEast2);
            NewPane.setTopComponent(null);
            jPanelWest2.setLayout(this.gridBagLayout1);
            jPanelEast2.setLayout(this.gridBagLayout2);
            jSplitPane32.setOrientation(0);
            jSplitPane32.setMinimumSize(new Dimension(86, 300));
            jSplitPane32.setDividerSize(4);
            jSplitPaneInfoComm2.setOrientation(0);
            jSplitPaneInfoComm2.setPreferredSize(new Dimension(152, 100));
            final PanelServerComm serverCommPane = new PanelServerComm();
            this.serverCommList.add(serverCommPane);
            jSplitPaneInfoComm2.setBottomComponent(serverCommPane);
            jSplitPaneInfoComm2.setDividerSize(4);
            jSplitPaneInfoComm2.setLastDividerLocation(1);
            jSplitPaneInfoComm2.setTopComponent(jPanelAssetInfo2);
            jPanelWest2.setMinimumSize(new Dimension(318, 600));
            jPanelWest2.setPreferredSize(new Dimension(300, 600));
            jPanelWest2.setRequestFocusEnabled(true);
            NewPane.add(jPanelWest2, "top");
            NewPane.add(jPanelEast2, "bottom");
            final OperationPane opPane = new OperationPane(op);
            this.operationPaneList.add(opPane);
            final MarketEvolutionGraph paneEvolutionGraph = new MarketEvolutionGraph();
            this.marketMap.put(op.getInstitution(), paneEvolutionGraph);
            paneEvolutionGraph.addAssetEvolution(op.getInstitution());
            paneEvolutionGraph.initGraphic();
            final ManagedAssets PanelManagedAssets = new ManagedAssets();
            jPanelTransactionsProperties2.add(opPane, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            jPanelWest2.add(PanelManagedAssets, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1, 10, 1, new Insets(10, 0, 0, 0), 0, 0));
            jPanelWest2.add(jSplitPaneInfoComm2, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            jPanelWest2.add(jPanelTransactionsProperties2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 12, 2, new Insets(0, 0, 0, 0), 0, 0));
            jSplitPaneInfoComm2.add(jPanelAssetInfo2, "top");
            jPanelAssetInfo2.add(new InstitutionInfoPane(op.getCompleteName()), new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            jSplitPaneInfoComm2.add(serverCommPane, "bottom");
            jPanelEast2.add(jSplitPane32, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
            jSplitPane32.add(paneEvolutionGraph, "bottom");
            jSplitPane32.add(ClientCore.getInstitution(op.getInstitution()).getClientPanel(op), "top");
            jSplitPaneInfoComm2.setDividerLocation(150);
            NewPane.setDividerLocation(450);
            jSplitPane32.setDividerLocation(400);
            this.jTabbedPaneGeneral.remove(this.jSplitPane2);
            this.jTabbedPaneGeneral.add(op.getInstitution(), NewPane);
            Utils.logger.debug("adding panels: done.");
        }
        else {
            Utils.logger.warn("Could not add the client operator panel: null operator received.");
        }
    }
    
    public void setOperatorText(final String opCompleteName, final String text) {
    }
    
    public void startEdition() {
        final Iterator it_pane = this.operationPaneList.iterator();
        while (it_pane.hasNext()) {
            ((GClientFrame) it_pane.next()).startEdition();
        }
    }
    
    public void stopEdition() {
        final Iterator it_pane = this.operationPaneList.iterator();
        while (it_pane.hasNext()) {
            ((GClientFrame) it_pane.next()).stopEdition();
        }
    }
    
    private void selectTabsNum(final int index) {
        if (this.jTabbedPane1.getSelectedIndex() != index && this.jTabbedPane1.getTabCount() > index) {
            this.jTabbedPane1.setSelectedIndex(index);
        }
        if (this.jTabbedPane3.getSelectedIndex() != index && this.jTabbedPane3.getTabCount() > index) {
            this.jTabbedPane3.setSelectedIndex(index);
        }
        if (this.jPaneMarketProperties.getSelectedIndex() != index && this.jPaneMarketProperties.getTabCount() > index) {
            this.jPaneMarketProperties.setSelectedIndex(index);
        }
    }
    
    void jPaneMarketProperties_stateChanged(final ChangeEvent e) {
        final int newIndex = this.jPaneMarketProperties.getSelectedIndex();
        if (this.lastIndexProperties != newIndex && newIndex >= 0 && this.lastIndexProperties >= 0) {
            this.jPaneMarketProperties.setForegroundAt(this.lastIndexProperties, Color.black);
            this.jPaneMarketProperties.setForegroundAt(newIndex, Color.red);
            this.lastIndexProperties = newIndex;
        }
        if (this.lastIndexProperties == -1) {
            this.jPaneMarketProperties.setForegroundAt(newIndex, Color.red);
            this.lastIndexProperties = newIndex;
        }
        this.selectTabsNum(this.jPaneMarketProperties.getSelectedIndex());
    }
    
    void jTabbedPane3_stateChanged(final ChangeEvent e) {
        final int newIndex = this.jTabbedPane3.getSelectedIndex();
        if (this.lastIndexPanel3 != newIndex && newIndex >= 0 && this.lastIndexPanel3 >= 0) {
            this.jTabbedPane3.setForegroundAt(this.lastIndexPanel3, Color.black);
            this.jTabbedPane3.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel3 = newIndex;
        }
        if (this.lastIndexPanel3 == -1) {
            this.jTabbedPane3.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel3 = newIndex;
        }
        this.selectTabsNum(this.jTabbedPane3.getSelectedIndex());
    }
    
    void jTabbedPane1_stateChanged(final ChangeEvent e) {
        final int newIndex = this.jTabbedPane1.getSelectedIndex();
        if (this.lastIndexPanel1 != newIndex && newIndex >= 0 && this.lastIndexPanel1 >= 0) {
            this.jTabbedPane1.setForegroundAt(this.lastIndexPanel1, Color.black);
            this.jTabbedPane1.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel1 = newIndex;
        }
        if (this.lastIndexPanel1 == -1) {
            this.jTabbedPane1.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel1 = newIndex;
        }
        this.selectTabsNum(this.jTabbedPane1.getSelectedIndex());
    }
    
    void jTabbedPaneGeneral_stateChanged(final ChangeEvent e) {
        final int newIndex = this.jTabbedPaneGeneral.getSelectedIndex();
        if (this.lastIndexPanel4 != newIndex && newIndex >= 0 && this.lastIndexPanel4 >= 0) {
            this.jTabbedPaneGeneral.setForegroundAt(this.lastIndexPanel4, Color.black);
            this.jTabbedPaneGeneral.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel4 = newIndex;
        }
        if (this.lastIndexPanel4 == -1) {
            this.jTabbedPaneGeneral.setForegroundAt(newIndex, Color.red);
            this.lastIndexPanel4 = newIndex;
        }
    }
    
    void jTabbedPaneServerComm_stateChanged(final ChangeEvent e) {
        final int newIndex = this.jTabbedPaneServerComm.getSelectedIndex();
        this.jTabbedPaneServerComm.setForegroundAt(newIndex, Color.black);
    }
}
