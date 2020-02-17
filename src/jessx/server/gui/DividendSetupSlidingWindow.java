package jessx.server.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jessx.business.BusinessCore;
import jessx.business.DividendLimitation;
import jessx.business.DividendModel;
import jessx.business.NormalDividend;
import jessx.business.event.DividendInfoEvent;
import jessx.business.event.DividendInfoListener;
import jessx.business.event.PlayerTypeEvent;
import jessx.business.event.PlayerTypeListener;
import jessx.utils.Utils;

public class DividendSetupSlidingWindow extends JPanel implements DisplayableNode, ChangeListener, PlayerTypeListener, DividendInfoListener {
  private DividendModel dividendModel;
  
  private String assetName;
  
  JPanel jPanel1 = new JPanel();
  
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  JScrollPane jScrollPane1 = new JScrollPane();
  
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  
  JTable jTable1;
  
  ButtonGroup buttonGroup1 = new ButtonGroup();
  
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  
  JPanel jpanelNoPlayer = new JPanel();
  
  private HashMap jPanelDivinfoList = new HashMap<Object, Object>();
  
  TableModelDividendSetup tableModelDividendSetup;
  
  public DividendSetupSlidingWindow(String assetName, DividendModel divModel) {
    this.dividendModel = divModel;
    this.assetName = assetName;
    JTextArea jTextArea = new JTextArea(
        "You have to add a category of players\nbefore completing this section!");
    jTextArea.setEditable(false);
    jTextArea.setBackground(this.jpanelNoPlayer.getBackground());
    jTextArea.setFont(this.jpanelNoPlayer.getFont());
    this.jpanelNoPlayer.add(jTextArea);
    try {
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    } 
    Iterator<String> ptIter = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
    if (!BusinessCore.getScenario().getPlayerTypes().isEmpty()) {
      while (ptIter.hasNext()) {
        String key = ptIter.next();
        this.jPanelDivinfoList.put(key, 
            new DividendInfoPanel(
              getDividendInfo(key)));
        this.jTabbedPane1.add(key, (JPanel)this.jPanelDivinfoList.get(key));
      } 
    } else {
      this.jTabbedPane1.add("No player", this.jpanelNoPlayer);
    } 
    BusinessCore.getGeneralParameters().addPeriodCountChangeListener(this);
    BusinessCore.getScenario().addPlayerTypeListener(this);
  }
  
  public void setEditable() {
    Iterator<String> iter = this.jPanelDivinfoList.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      ((DividendInfoPanel)this.jPanelDivinfoList.get(key)).active();
    } 
    this.tableModelDividendSetup.setCellEditable();
  }
  
  public void setUneditable() {
    Iterator<String> iter = this.jPanelDivinfoList.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      ((DividendInfoPanel)this.jPanelDivinfoList.get(key)).desactive();
    } 
    this.tableModelDividendSetup.setCellUneditable();
  }
  
  public String toString() {
    return this.assetName;
  }
  
  public JPanel getPanel() {
    return this;
  }
  
  public void stateChanged(ChangeEvent e) {
    if (e.getSource() instanceof JSpinner) {
      int periodNum = (new Integer(((JSpinner)e.getSource()).getValue().toString())).intValue();
      ((TableModelDividendSetup)this.jTable1.getModel()).setPeriodCount(periodNum);
    } 
  }
  
  public void playerTypeModified(PlayerTypeEvent e) {
    if (BusinessCore.getAssets().containsKey(this.assetName))
      if (e.getEvent() == 1) {
        this.jTabbedPane1.remove(this.jpanelNoPlayer);
        String key = e.getPlayerType().getPlayerTypeName();
        this.jPanelDivinfoList.put(key, 
            new DividendInfoPanel(
              getDividendInfo(key)));
        this.jTabbedPane1.add(key, (JPanel)this.jPanelDivinfoList.get(key));
      } else if (e.getEvent() == 0) {
        this.jTabbedPane1.remove((JPanel)this.jPanelDivinfoList.get(e
              .getPlayerType().getPlayerTypeName()));
        this.jPanelDivinfoList.remove(e.getPlayerType().getPlayerTypeName());
        if (BusinessCore.getScenario().getPlayerTypes().isEmpty())
          this.jTabbedPane1.add("No player", this.jpanelNoPlayer); 
      }  
  }
  
  public void dividendInfoModified(DividendInfoEvent e) {}
  
  private DividendLimitation getDividendInfo(String playerType) {
    Utils.logger.debug("Getting dividend info...");
    DividendLimitation divInf = BusinessCore.getScenario().getDividendInfo(this.assetName, playerType);
    if (divInf == null) {
      Utils.logger.debug("No dividend info found for (" + playerType + ", " + this.assetName + ")");
      divInf = new DividendLimitation(playerType, this.assetName);
      Utils.logger.debug("Creating one...");
      BusinessCore.getScenario().setDividendInfo(this.assetName, playerType, divInf);
      divInf.addListener(this);
    } 
    return divInf;
  }
  
  private void jbInit() throws Exception {
    setLayout(this.gridBagLayout1);
    this.tableModelDividendSetup = new TableModelDividendSetup(NormalDividend.class, this.dividendModel);
    this.jTable1 = new JTable(this.tableModelDividendSetup);
    this.jPanel1.setDebugGraphicsOptions(0);
    this.jPanel1.setLayout(this.gridBagLayout2);
    this.jScrollPane1.setMinimumSize(new Dimension(50, 50));
    add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0D, 1.0D, 
          10, 1, new Insets(0, 0, 0, 0), 0, 0));
    this.jScrollPane1.getViewport().add(this.jTable1, (Object)null);
    this.jPanel1.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.5D, 1.0D, 
          10, 1, 
          new Insets(0, 0, 0, 0), 0, 0));
    this.jPanel1.add(this.jTabbedPane1, new GridBagConstraints(1, 0, 1, 1, 0.5D, 1.0D, 
          10, 1, 
          new Insets(0, 0, 0, 0), 0, 0));
  }
}
