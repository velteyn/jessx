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
import javax.swing.event.*;

import org.jdom.*;
import jessx.business.*;
import jessx.client.*;
import jessx.client.event.*;
import jessx.gclient.net.*;
import jessx.net.*;
import jessx.utils.*;
import jessx.utils.gui.*;

/***************************************************************/
/*              GClientFrame CLASS SECTION                     */
/***************************************************************/
/**
 * <p>Title: GClientFrame</p>
 * <p>Description: </p>
 * @author Thierry Curtil, Julien Terrier,Christophe Grosjean, Charles Montez
 * @see tutorials used:
 * @see http://java.sun.com/docs/books/tutorial/uiswing/components/table.html#data
 * @see http://oconstans.developpez.com/tutorielsjava/java_tut_01/
 */

public class GClientFrame extends JFrame
    implements Constants, OperatorPlayedListener,
    ExperimentDeveloppmentListener, NetworkListener, ConnectionListener {

  private static Hashtable evolutionGraphFactories = new Hashtable();

  JPanel contentPaneGeneral;

  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();
  JLabel statusBar = new JLabel();
  TitledBorder titledBorder1;
  JPanel jPanelManagedAssets = new JPanel();
  JPanel jPanelCommunication = new JPanel();
  Border border1;
  Border border2;
  TitledBorder titledBorder2;
  Border border3;
  Border border4;
  TitledBorder titledBorder3;
  JTextArea jTextAreaCommunication = new JTextArea();
  JPanel jPanelAssetInfo = new JPanel();
  TitledBorder titledBorder4;
  TitledBorder titledBorder20;
  Border border5;
  Border border6;
  Border border7;

  TitledBorder titledBorder5;
  Border border8;

  TitledBorder titledBorder7;
  ButtonGroup buttonGroup1 = new ButtonGroup();
  TitledBorder titledBorder8;
  Border border9;
  TitledBorder titledBorder9;
  ButtonGroup buttonGroup2 = new ButtonGroup();
  JScrollPane jScrollPaneManagedAssets = new JScrollPane();
  JPanel jPanelTransactionsProperties = new JPanel();
  TitledBorder titledBorder10;
  JTabbedPane jPaneMarketProperties = new JTabbedPane();
  TitledBorder titledBorder11;
  GridBagLayout gridBagLayoutManagedAssets = new GridBagLayout();
  GridBagLayout gridBagLayoutCommunication = new GridBagLayout();
  GridBagLayout gridBagLayoutMarketProperties = new GridBagLayout();
  GridBagLayout gridBagLayoutAssetInfo = new GridBagLayout();
  GridBagLayout gridBagLayoutGeneral = new GridBagLayout();
  PortfolioTableModel tableJTable2Model = new PortfolioTableModel(new String[] {"Asset Name"," Quantity"});
  JTable jTableManagedAssets = new JTable(tableJTable2Model);
  GridBagLayout gridBagLayoutTransactionsProperties = new GridBagLayout();
  TitledBorder titledBorder12;
  Border border10;
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenuItem jMenuItem2 = new JMenuItem();
  JScrollPane jScrollPaneCommunication = new JScrollPane();
  JSplitPane jSplitPane2 = new JSplitPane();
  JPanel jPanelWest = new JPanel();
  JPanel jPanelEast = new JPanel();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  JSplitPane jSplitPaneInfoComm = new JSplitPane();
  JSplitPane jSplitPane3 = new JSplitPane();
  TitledBorder titledBorder14;
  TitledBorder titledBorder15;
  Border border11;
  TitledBorder titledBorder16;

  JTextField jTextFieldTimer = new JTextField();
  JTextField jTextFieldConnect = new JTextField();
  JTextField jTextFieldName = new JTextField();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JTabbedPane jTabbedPane2 = new JTabbedPane();
  JLabel jLabel1 = new JLabel();
  Border border13;
  JLabel jLabel2 = new JLabel();
  Border border12;
  JTabbedPane jTabbedPane3 = new JTabbedPane();
  ClientTimer timer = new ClientTimer(jTextFieldTimer);
  private int lastIndexPanel1= -1;
  private int lastIndexPanel2= -1;
  private int lastIndexPanel3= -1;
  private int lastIndexProperties=-1;


  /** Constructor of the frame GClient
   * @version 1.0
   */
  public GClientFrame() {
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    new ServerPlainMessageListener(this);

    enableEvents(AWTEvent.WINDOW_EVENT_MASK);

    this.setDefaultLookAndFeelDecorated(false);
    try {
      jbInit();
    }
    catch(Exception e) {
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
    timer.start();

    // At the beginig the background is grey. Here we turn it into red to show the initial disconnection of the client.
    jTextFieldConnect.setBackground(new Color(220,160,155));

  }

  public void experimentBegins() {
    jTextFieldTimer.setFont(new java.awt.Font("Lucida Console", 1, 12));
    jTextFieldTimer.setMaximumSize(new Dimension(550, 30));
    jTextFieldTimer.setMinimumSize(new Dimension(550, 30));
    jTextFieldTimer.setPreferredSize(new Dimension(550, 30));
    jTextFieldTimer.setText("> Waiting for all the players to read their information <");
    this.validate();

    //Commentee... l'edition ne doit commencer que lors du debut d'une periode
    // pas d'une experience
    //this.startEdition();
  }

  public void experimentFinished() {
    // inutile: cela doit avoir ete fait plus tot.
    //this.stopEdition();
    jTextFieldTimer.setText("");
    jTextFieldTimer.setFont(new java.awt.Font("Lucida Console", 1, 12));
    jTextFieldTimer.setText("> Experiment End <");
    jTextFieldTimer.setMaximumSize(new Dimension(200, 30));
    jTextFieldTimer.setMinimumSize(new Dimension(200, 30));
    jTextFieldTimer.setPreferredSize(new Dimension(200, 30));
    this.validate();
  }

  public void periodBegins() {
    this.startEdition();
    jTextFieldTimer.setText("");
    jTextFieldTimer.validate();
    jTextFieldTimer.setFont(new java.awt.Font("Lucida Console", 1, 18));
    jTextFieldTimer.setMaximumSize(new Dimension(250, 30));
    jTextFieldTimer.setMinimumSize(new Dimension(250, 30));
    jTextFieldTimer.setPreferredSize(new Dimension(250, 30));
    this.validate();
  }

  public void periodFinished() {
    this.stopEdition();
  }

  /**
   * @param xmlDoc Document
   */
  public void objectReceived(Document xmlDoc) {
    if (xmlDoc.getRootElement().getName().equals("Initialisation")) {
      System.out.print("\nInitialisation Client\n");
      ClientCore.initializeForNewExperiment();
      evolutionGraphFactories.clear();
      jTabbedPane1.removeAll();
      jTabbedPane2.removeAll();
      jTabbedPane3.removeAll();
      jPaneMarketProperties.removeAll();
      jTextAreaCommunication.setText("");
    }
    if (xmlDoc.getRootElement().getName().equals("Deal")) {
      Deal deal = new Deal("", 0, 0, 0, "", "", 0, "", "");
      if (deal.initFromNetworkInput(xmlDoc.getRootElement())) {
        if (ClientCore.getInstitution(deal.getDealInstitution()) != null) {
          /*
          Utils.logger.debug("\n\nInitialisation du deal reussie :\n-----\nSeller : "  + "\n"
                             + deal.getSeller() + "\nBuyer : " + deal.getBuyer() + "\n"
                             + "\ninstitutionName : " + deal.getDealInstitution()  + "\n"
                             + "Deal effectue : le maxBidPrice est de : " + deal.getMaxBidPrice() + "\n"
                             + "le dealPrice est de : " + deal.getDealPrice() + "\n\n\nPour une quantite de " + deal.getQuantity()
                             + " assets");

          if (deal.getSeller().equals(ClientCore.getLogin())) {
            ClientCore.getPortfolio().soldAssetForNonInvestedPortfolio(deal.
                getDealPrice(), deal.getQuantity(), deal.getDealInstitution(), ClientCore.getInstitution(deal.getDealInstitution()).getPercentageCost(deal.getSellerOperation()),ClientCore.getInstitution(deal.getDealInstitution()).getMinimalCost(deal.getSellerOperation()));
            //The seller's NonInvestedCash is increased of the amount of the sale
          }

          if (deal.getBuyer().equals(ClientCore.getLogin())) {

            ClientCore.getPortfolio().boughtAssetForNonInvestedPortfolio(deal.getDealPrice(), deal.
                getQuantity(), deal.getMaxBidPrice(), deal.getDealInstitution(), ClientCore.getInstitution(deal.getDealInstitution()).getPercentageCost(deal.getBuyerOperation()),ClientCore.getInstitution(deal.getDealInstitution()).getMinimalCost(deal.getBuyerOperation()));

          }
          //OBJ
          */
          this.jPaneMarketProperties.invalidate();

          MarketEvolutionGraph marketEvolution = ( (MarketEvolutionGraph)
                                                  evolutionGraphFactories.get(
              deal.getDealInstitution()));
          if (marketEvolution != null) {
            marketEvolution.addDeal(deal);
          }
          else {
            Utils.logger.error("marketEvolution is empty");
          }

          this.jPaneMarketProperties.validate();
        }
      }
    }
    else if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
      ExpUpdate update = new ExpUpdate( -1, "", -1);
      update.initFromNetworkInput(xmlDoc.getRootElement());

      if (update.getUpdateType()==ExpUpdate.PERIOD_BEGINNING) {
       // jTextFieldConnect.setBackground(new Color(145,220,180));//green
      }

      if (update.getUpdateType() == ExpUpdate.CLIENT_READY||update.getUpdateType()==ExpUpdate.EXPERIMENT_FINISHING) {
        Utils.logger.debug("Displaying the ready message...");
        String mess =update.getUpdateMessage();
        int rows =mess.length()/50+1;
        int columns =(rows<2)?mess.length():50;
        JTextArea jTextArea=new JTextArea(mess, rows, columns);
        jTextArea.setEditable(false);
        jTextArea.setOpaque(false);
        jTextArea.setAutoscrolls(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setVisible(true);
        new PopupWithTimer(30,jTextArea,jTextArea.getPreferredSize(),"JessX client",this).run();
        Utils.logger.debug("sending the server the ready signal...");
        ClientCore.send(update);
      }
    }
    else if (xmlDoc.getRootElement().getName().equals("Message")) {
      this.jTextAreaCommunication.append("\n===============================\n" +
                                         xmlDoc.getRootElement().getText());
    }
    else if (xmlDoc.getRootElement().getName().equals("Warn")) {
        String mess =xmlDoc.getRootElement().getText();
        int rows =mess.length()/50+1;
        int columns =(rows<2)?mess.length():50;
        JTextArea jTextArea=new JTextArea(mess, rows, columns);
        jTextArea.setEditable(false);
        jTextArea.setOpaque(false);
        jTextArea.setAutoscrolls(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setVisible(true);
        int time=Math.min(Math.round(ClientCore.getExperimentManager().getRemainingTimeInPeriod()/1000),15);
        new PopupWithTimer(time,jTextArea,jTextArea.getPreferredSize(),"JessX client",this).start();
      }

    else if (xmlDoc.getRootElement().getName().equals("Information")) {
      int jTextAreaCommunicationHeightBeforeUpdate = jTextAreaCommunication.getHeight();
      this.jTextAreaCommunication.append("\n===============================\n" +
                                         timer.getText()+"\n"+
                                         xmlDoc.getRootElement().getText());
      jTextAreaCommunication.validate();
      NewMessageTimer timerMessage = new NewMessageTimer(jTextAreaCommunication,
          jScrollPaneCommunication, jTextAreaCommunicationHeightBeforeUpdate,
          jScrollPaneCommunication.getHeight());
      timerMessage.start();
    }
    else if (xmlDoc.getRootElement().getName().equals("Institution")) {
      Institution instit = Institution.loadInstitutionFromXml(xmlDoc.getRootElement());

      if(!this.evolutionGraphFactories.containsValue(instit.getName())){
        this.evolutionGraphFactories.put(instit.getName(), new MarketEvolutionGraph());
        this.jPaneMarketProperties.add(((MarketEvolutionGraph)evolutionGraphFactories.get(instit.getName())),instit.getName());
        ((MarketEvolutionGraph)this.evolutionGraphFactories.get(instit.getName())).addAssetEvolution(instit.getName());
        ((MarketEvolutionGraph)this.evolutionGraphFactories.get(instit.getName())).initGraphic();
      }
    }
  }

  /**
   *
   * @param newState int
   */
  public void connectionStateChanged(int newState) {
    if (newState == ClientCore.DISCONNECTED) {
      this.jMenuItem1.setEnabled(true);
      this.jMenuItem2.setEnabled(false);
      jTextFieldConnect.setText("DISCONNECTED");
      jTextFieldConnect.setBackground(new Color(220,160,155)); //red
      JOptionPane.showMessageDialog(this,"Connection to server has been closed (see the log for information)", "Network Error", JOptionPane.ERROR_MESSAGE);
    }
    else if (newState == ClientCore.CONNECTED) {
      jTextFieldConnect.setText("CONNECTED");
      jTextFieldConnect.setBackground(new Color(145,220,180)); //green
      this.jMenuItem1.setEnabled(false);
      jTextFieldTimer.setAlignmentX(CENTER_ALIGNMENT);
      jTextFieldTimer.setFont(new java.awt.Font("Lucida Console", 1, 12));
      jTextFieldTimer.setMaximumSize(new Dimension(350, 30));
      jTextFieldTimer.setMinimumSize(new Dimension(350, 30));
      jTextFieldTimer.setPreferredSize(new Dimension(350, 30));
      jTextFieldTimer.setText("> Waiting for other players to connect <");


      //this.jMenuItem2.setEnabled(true);
    }
  }

  //Component initialization
  private void jbInit() throws Exception  {

    /* get the client id */
    //this.idClient = idClient;

    this.setIconImage(new ImageIcon("./images/logo_JessX_small.GIF").getImage());
    contentPaneGeneral = (JPanel) this.getContentPane();
    titledBorder1 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Assets",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    border1 = new TitledBorder(BorderFactory.createEmptyBorder(6,6,6,6),"Managed Assets",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(border2,"Communication");
    border3 = BorderFactory.createCompoundBorder(titledBorder2,BorderFactory.createEmptyBorder(6,6,6,6));
    border4 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140));
    titledBorder3 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Market");
    titledBorder4 = new TitledBorder(BorderFactory.createEmptyBorder(6,0,0,0),"Asset information",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    border5 = new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(178, 178, 178));
    border6 = BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(124, 124, 124),new Color(178, 178, 178));
    border7 = BorderFactory.createEtchedBorder(Color.white,new Color(178, 178, 178));
    titledBorder5 = new TitledBorder("");
    border8 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));

    titledBorder7 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Action");
    titledBorder8 = new TitledBorder("");
    border9 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder9 = new TitledBorder(border9,"Order type");
    titledBorder10 = new TitledBorder(BorderFactory.createEmptyBorder(6,0,0,0),"Transaction properties",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    titledBorder11 = new TitledBorder(BorderFactory.createEmptyBorder(6,0,0,0),"Market properties",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    titledBorder12 = new TitledBorder("");
    border10 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder14 = new TitledBorder("");
    titledBorder15 = new TitledBorder("");
    border11 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder16 = new TitledBorder(BorderFactory.createEmptyBorder(6,0,0,0),"Server Communication",TitledBorder.DEFAULT_JUSTIFICATION,TitledBorder.DEFAULT_POSITION,FONT_CLIENT_TITLE_BORDER);
    border13 = BorderFactory.createEmptyBorder();

    border12 = BorderFactory.createEmptyBorder();
    contentPaneGeneral.setLayout(gridBagLayoutGeneral);
    this.setResizable(true);
    this.setSize(new Dimension(905, 737));
    this.setState(Frame.NORMAL);
    this.setTitle("JessX client "+Constants.VERSION);
    statusBar.setBackground(SystemColor.activeCaptionBorder);
    statusBar.setFont(new java.awt.Font("MS Sans Serif", 0, 11));
    statusBar.setBorder(BorderFactory.createLoweredBevelBorder());
    statusBar.setOpaque(false);
    statusBar.setText("Status Bar");
    statusBar.setVisible(false);
    jMenuFile.setText("File");
    jMenuFile.setMnemonic(KeyEvent.VK_F);
    jMenuFileExit.setText("Exit");
    jMenuFileExit.setMnemonic(KeyEvent.VK_X);
    jMenuFileExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F4,Event.ALT_MASK));
    jMenuFileExit.addActionListener(new Principale_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelp.setMnemonic(KeyEvent.VK_H);
    jMenuHelpAbout.setText("About JessX...");
    jMenuHelpAbout.setMnemonic(KeyEvent.VK_A);
    jMenuHelpAbout.addActionListener(new Principale_jMenuHelpAbout_ActionAdapter(this));
    titledBorder1.setTitle("Assets");
    jPanelManagedAssets.setEnabled(true);
    jPanelManagedAssets.setBorder(border1);
    jPanelManagedAssets.setMinimumSize(new Dimension(50, 50));
    jPanelManagedAssets.setOpaque(true);
    jPanelManagedAssets.setPreferredSize(new Dimension(200, 130));
    jPanelManagedAssets.setLayout(gridBagLayoutManagedAssets);
    jPanelCommunication.setBorder(titledBorder16);
    jPanelCommunication.setMinimumSize(new Dimension(20, 100));
    jPanelCommunication.setPreferredSize(new Dimension(150, 100));
    jPanelCommunication.setLayout(gridBagLayoutCommunication);
    contentPaneGeneral.setMaximumSize(new Dimension(2147483647, 2147483647));
    contentPaneGeneral.setMinimumSize(new Dimension(800, 600));
    contentPaneGeneral.setPreferredSize(new Dimension(800, 600));
    jTextAreaCommunication.setBackground(UIManager.getColor("Button.background"));
    jTextAreaCommunication.setEnabled(true);
    jTextAreaCommunication.setBorder(BorderFactory.createLoweredBevelBorder());
    jTextAreaCommunication.setEditable(false);
    jTextAreaCommunication.setSelectedTextColor(Color.white);
    jTextAreaCommunication.setLineWrap(true);
    jTextAreaCommunication.setWrapStyleWord(true);
    jTextAreaCommunication.setFont(FONT_CLIENT_TEXTAREA);
    jPanelAssetInfo.setAlignmentX((float) 0.5);
    jPanelAssetInfo.setBorder(BorderFactory.createEmptyBorder());
    jPanelAssetInfo.setMinimumSize(new Dimension(20, 100));
    jPanelAssetInfo.setPreferredSize(new Dimension(150, 100));
    jPanelAssetInfo.setLayout(gridBagLayoutAssetInfo);
    jScrollPaneManagedAssets.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    jScrollPaneManagedAssets.setAutoscrolls(false);
    jScrollPaneManagedAssets.setRequestFocusEnabled(true);
    jPanelTransactionsProperties.setFont(new java.awt.Font("Lucida Console", 1, 13));
    jPanelTransactionsProperties.setBorder(titledBorder10);
    jPanelTransactionsProperties.setLayout(gridBagLayoutTransactionsProperties);
    jPaneMarketProperties.setBorder(titledBorder11);
    jPaneMarketProperties.setMinimumSize(new Dimension(16, 10));
    jPaneMarketProperties.setPreferredSize(new Dimension(291, 169));
    jTextFieldTimer.setHorizontalAlignment(SwingConstants.CENTER);
    jTableManagedAssets.setRowSelectionAllowed(false);
    jSplitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    jSplitPane2.setBottomComponent(null);
    jSplitPane2.setDividerSize(5);
    jSplitPane2.setLeftComponent(jPanelWest);
    jSplitPane2.setRightComponent(jPanelEast);
    jSplitPane2.setTopComponent(null);
    jPanelWest.setLayout(gridBagLayout1);
    jPanelEast.setLayout(gridBagLayout2);
    jSplitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane3.setMinimumSize(new Dimension(86, 300));
    jSplitPane3.setDividerSize(4);
    jSplitPaneInfoComm.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPaneInfoComm.setPreferredSize(new Dimension(152, 100));
    jSplitPaneInfoComm.setBottomComponent(jPanelCommunication);
    jSplitPaneInfoComm.setDividerSize(4);
    jSplitPaneInfoComm.setLastDividerLocation(1);
    jSplitPaneInfoComm.setTopComponent(jPanelAssetInfo);
    jPanelWest.setMinimumSize(new Dimension(318, 600));
    jPanelWest.setPreferredSize(new Dimension(300, 600));
    jPanelWest.setRequestFocusEnabled(true);
    jTextFieldTimer.setEnabled(false);
    jTextFieldTimer.setFont(new java.awt.Font("Lucida Console", 1, 18));
    jTextFieldTimer.setBorder(null);
    jTextFieldTimer.setMaximumSize(new Dimension(400, 30));
    jTextFieldTimer.setMinimumSize(new Dimension(400, 30));
    jTextFieldTimer.setPreferredSize(new Dimension(400, 30));
    jTextFieldTimer.setAlignmentX(CENTER_ALIGNMENT);
    jTextFieldTimer.setCaretColor(Color.black);
    jTextFieldTimer.setDisabledTextColor(Color.black);
    jTextFieldTimer.setEditable(false);
    jTextFieldTimer.setMargin(new Insets(1, 1, 1, 1));
    jTextFieldTimer.setSelectedTextColor(Color.black);
    jTextFieldTimer.setSelectionColor(Color.lightGray);
    jTextFieldName.setText("No name");
    jTextFieldName.setEditable(false);
    jTextFieldName.setPreferredSize(new Dimension(90, 20));
    jTextFieldName.setMaximumSize(new Dimension(200, 20));
    jTextFieldName.setMinimumSize(new Dimension(90, 20));
    jTextFieldName.setHorizontalAlignment(SwingConstants.CENTER);
    jTextFieldConnect.setText("DISCONNECTED");
    jTextFieldConnect.setEditable(false);
    jTextFieldConnect.setPreferredSize(new Dimension(90, 20));
    jTextFieldConnect.setMaximumSize(new Dimension(90, 20));
    jTextFieldConnect.setMinimumSize(new Dimension(90, 20));
    jTextFieldConnect.setHorizontalAlignment(SwingConstants.CENTER);
    jMenuItem1.setText("Join a session...");
    jMenuItem2.setText("Disconnect");
    jMenuItem1.setMnemonic(KeyEvent.VK_J);
    jMenuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J,
        Event.CTRL_MASK));
    jMenuItem2.setMnemonic(KeyEvent.VK_D);
    jMenuItem2.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
        Event.CTRL_MASK));
    jMenuItem1.addActionListener(new ClientFrame_jMenuItem1_actionAdapter(this));
    jMenuItem2.addActionListener(new ClientFrame_jMenuItem2_actionAdapter(this));
    jTableManagedAssets.getColumnModel().getColumn(0).setCellRenderer(new
        JLabelRenderer());
    jTableManagedAssets.getColumnModel().getColumn(1).setCellRenderer(new
        JLabelRenderer());
    jTableManagedAssets.setRowHeight(18);
    jTabbedPane1.setTabPlacement(JTabbedPane.TOP);
    jTabbedPane1.setMinimumSize(new Dimension(100, 200));
    jTabbedPane1.addChangeListener(new
                                   GClientFrame_jTabbedPane1_changeAdapter(this));
    jLabel1.setEnabled(true);
    jLabel1.setFont(FONT_CLIENT_LABEL_UP);
    jLabel2.setHorizontalAlignment(SwingConstants.TRAILING);
    jLabel2.setHorizontalTextPosition(SwingConstants.TRAILING);
    jLabel2.setText("");
    jLabel2.setFont(FONT_CLIENT_LABEL_UP);
    jTabbedPane2.setBorder(border12);
    jTabbedPane2.addChangeListener(new GClientFrame_jTabbedPane2_changeAdapter(this));
    jTabbedPane3.addChangeListener(new GClientFrame_jTabbedPane3_changeAdapter(this));
    jPaneMarketProperties.addChangeListener(new GClientFrame_jPaneMarketProperties_changeAdapter(this));
    contentPaneGeneral.add(jSplitPane2,        new GridBagConstraints(0, 2, 3, 1, 1.0, 1.0
            ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jSplitPane2.add(jPanelWest, JSplitPane.TOP);
    jSplitPane2.add(jPanelEast, JSplitPane.BOTTOM);
    jMenuFile.add(jMenuItem1);
    jMenuFile.add(jMenuItem2);

    this.jMenuItem2.setEnabled(false);//temporarly desactivated

    jMenuFile.addSeparator();
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    contentPaneGeneral.add(statusBar, new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 686, 0));
    contentPaneGeneral.add(jTextFieldConnect, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    contentPaneGeneral.add(jTextFieldName, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    contentPaneGeneral.add(jTextFieldTimer, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
        ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
    contentPaneGeneral.add(jLabel1,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0, 10, 0, 0), 0, 0));
    contentPaneGeneral.add(jLabel2,    new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 10), 0, 0));
    jPanelCommunication.add(jScrollPaneCommunication,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(4, 4, 2, 4), 0, 0));
    jScrollPaneCommunication.getViewport().add(jTextAreaCommunication, null);
    jPanelTransactionsProperties.add(jTabbedPane3,   new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelWest.add(jPanelManagedAssets,         new GridBagConstraints(0, 1, 1, 1, 1.0, 0.1
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(10, 0, 0, 0), 0, 0));
    jPanelManagedAssets.add(jScrollPaneManagedAssets, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jScrollPaneManagedAssets.getViewport().add(jTableManagedAssets, null);
    jPanelWest.add(jSplitPaneInfoComm,        new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jPanelWest.add(jPanelTransactionsProperties,     new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
        ,GridBagConstraints.NORTHEAST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jSplitPaneInfoComm.add(jPanelAssetInfo, JSplitPane.TOP);
    jPanelAssetInfo.add(jTabbedPane2,  new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));


    jSplitPaneInfoComm.add(jPanelCommunication, JSplitPane.BOTTOM);
    jPanelEast.add(jSplitPane3,     new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0
        ,GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    jSplitPane3.add(jPaneMarketProperties, JSplitPane.BOTTOM);
    jSplitPane3.add(jTabbedPane1, JSplitPane.TOP);

    jSplitPaneInfoComm.setDividerLocation(150);
    jSplitPane2.setDividerLocation(450);
    jSplitPane3.setDividerLocation(400);
  }


  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    if (JOptionPane.showConfirmDialog(this,
        "Do you really want to quit JessX Client?", "JessX Client",
                                      JOptionPane.OK_CANCEL_OPTION,
                                      JOptionPane.OK_CANCEL_OPTION) ==
        JOptionPane.OK_OPTION)
      System.exit(0);
  }

  //Help | About action performed
  /**
   *
   * @param e ActionEvent
   */
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    ClientAboutBox dlg = new ClientAboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }

  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }

  void jMenuItem1_actionPerformed(ActionEvent e) {
    new ConnectionPopup(this);
    jTextFieldName.setText(ClientCore.getLogin());
  }

  void jMenuItem2_actionPerformed(ActionEvent e) {
   // new DisconnectionPopup(this);
    jTextFieldName.setText("");
  }

  /**
   * @param op Operator
   */

  public void newOperator(Operator op) {
    // we display in the reserved place the institution panel associated with
    // this operator.
    if (op != null) {
      Utils.logger.debug("Adding the client operator panels...");
      jTabbedPane1.add(op.getInstitution(),
                       ClientCore.getInstitution(op.getInstitution()).
                       getClientPanel(op));
      jTabbedPane3.add(op.getInstitution(), new OperationPane(op));
      jTabbedPane2.add(op.getInstitution(), new InstitutionInfoPane(op.getCompleteName()));
      Utils.logger.debug("adding panels: done.");
    }
    else {
      Utils.logger.warn("Could not add the client operator panel: null operator received.");
    }
  }


  public void setOperatorText(String opCompleteName, String text) {

  }

  public void startEdition() {
    for(int i = 0; i < jTabbedPane3.getTabCount(); i++) {
      ((OperationPane)jTabbedPane3.getComponentAt(i)).startEdition();
    }
  }

  public void stopEdition() {
    for(int i = 0; i < jTabbedPane3.getTabCount(); i++) {
      ((OperationPane)jTabbedPane3.getComponentAt(i)).stopEdition();
    }
  }

  private void selectTabsNum(int index) {
    if ((jTabbedPane1.getSelectedIndex() != index) &&
        (jTabbedPane1.getTabCount() > index)) {
      jTabbedPane1.setSelectedIndex(index);
    }
    if ((jTabbedPane2.getSelectedIndex() != index) &&
        (jTabbedPane2.getTabCount() > index)) {
     jTabbedPane2.setSelectedIndex(index);
   }
   if ((jTabbedPane3.getSelectedIndex() != index) &&
       (jTabbedPane3.getTabCount() > index)) {
     jTabbedPane3.setSelectedIndex(index);
   }
   if ((jPaneMarketProperties.getSelectedIndex() != index) &&
       (jPaneMarketProperties.getTabCount() > index)) {
     jPaneMarketProperties.setSelectedIndex(index);
   }
  }

  void jPaneMarketProperties_stateChanged(ChangeEvent e) {
    int newIndex = jPaneMarketProperties.getSelectedIndex();
    if (lastIndexProperties != newIndex && newIndex >= 0 &&
        lastIndexProperties >= 0) {
      jPaneMarketProperties.setForegroundAt(lastIndexProperties, Color.black);
      jPaneMarketProperties.setForegroundAt(newIndex, Color.red);
      this.lastIndexProperties = newIndex;
    }
    if (lastIndexProperties == -1) {
      jPaneMarketProperties.setForegroundAt(newIndex, Color.red);
      this.lastIndexProperties = newIndex;
    }
    selectTabsNum(jPaneMarketProperties.getSelectedIndex());
  }

  void jTabbedPane3_stateChanged(ChangeEvent e) {
    int newIndex = jTabbedPane3.getSelectedIndex();
    if (lastIndexPanel3 != newIndex && newIndex >= 0 && lastIndexPanel3 >= 0) {
      jTabbedPane3.setForegroundAt(lastIndexPanel3, Color.black);
      jTabbedPane3.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel3 = newIndex;
    }
    if (lastIndexPanel3 == -1) {
      jTabbedPane3.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel3 = newIndex;
    }
    selectTabsNum(jTabbedPane3.getSelectedIndex());
  }

  void jTabbedPane2_stateChanged(ChangeEvent e) {
    int newIndex = jTabbedPane2.getSelectedIndex();
    if (lastIndexPanel2 != newIndex && newIndex >= 0 && lastIndexPanel2 >= 0) {
      jTabbedPane2.setForegroundAt(lastIndexPanel2, Color.black);
      jTabbedPane2.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel2 = newIndex;
    }
    if (lastIndexPanel2 == -1) {
      jTabbedPane2.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel2 = newIndex;
    }

    selectTabsNum(jTabbedPane2.getSelectedIndex());
  }

  void jTabbedPane1_stateChanged(ChangeEvent e) {
    int newIndex = jTabbedPane1.getSelectedIndex();
    if (lastIndexPanel1 != newIndex && newIndex >= 0 && lastIndexPanel1 >= 0) {
      jTabbedPane1.setForegroundAt(lastIndexPanel1, Color.black);
      jTabbedPane1.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel1 = newIndex;
    }
    if (lastIndexPanel1 == -1) {
      jTabbedPane1.setForegroundAt(newIndex, Color.red);
      this.lastIndexPanel1 = newIndex;
    }
    selectTabsNum(jTabbedPane1.getSelectedIndex());
  }
}

/***************************************************************/
/*                   EVENT CLASSES SECTION                     */
/***************************************************************/
class Principale_jMenuFileExit_ActionAdapter implements ActionListener {
  GClientFrame adaptee;

  Principale_jMenuFileExit_ActionAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Principale_jMenuHelpAbout_ActionAdapter implements ActionListener {
  GClientFrame adaptee;

  Principale_jMenuHelpAbout_ActionAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}

class ClientFrame_jMenuItem1_actionAdapter implements java.awt.event.ActionListener {
  GClientFrame adaptee;

  ClientFrame_jMenuItem1_actionAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem1_actionPerformed(e);
  }
}

class ClientFrame_jMenuItem2_actionAdapter implements java.awt.event.ActionListener {
  GClientFrame adaptee;

  ClientFrame_jMenuItem2_actionAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuItem2_actionPerformed(e);
  }
}

class GClientFrame_jPaneMarketProperties_changeAdapter implements javax.swing.event.ChangeListener {
  GClientFrame adaptee;

  GClientFrame_jPaneMarketProperties_changeAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.jPaneMarketProperties_stateChanged(e);
  }
}

class GClientFrame_jTabbedPane3_changeAdapter implements javax.swing.event.ChangeListener {
  GClientFrame adaptee;

  GClientFrame_jTabbedPane3_changeAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.jTabbedPane3_stateChanged(e);
  }
}

class GClientFrame_jTabbedPane2_changeAdapter implements javax.swing.event.ChangeListener {
  GClientFrame adaptee;

  GClientFrame_jTabbedPane2_changeAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.jTabbedPane2_stateChanged(e);
  }
}

class GClientFrame_jTabbedPane1_changeAdapter implements javax.swing.event.ChangeListener {
  GClientFrame adaptee;

  GClientFrame_jTabbedPane1_changeAdapter(GClientFrame adaptee) {
    this.adaptee = adaptee;
  }
  public void stateChanged(ChangeEvent e) {
    adaptee.jTabbedPane1_stateChanged(e);
  }
}
