// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Dimension;
import javax.swing.table.TableModel;
import jessx.business.NormalDividend;
import java.awt.LayoutManager;
import jessx.utils.Utils;
import jessx.business.DividendLimitation;
import jessx.business.event.DividendInfoEvent;
import jessx.business.event.PlayerTypeEvent;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import java.util.Iterator;
import jessx.business.BusinessCore;
import java.awt.Component;
import javax.swing.JTextArea;
import java.util.HashMap;
import javax.swing.JTabbedPane;
import javax.swing.ButtonGroup;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import jessx.business.DividendModel;
import jessx.business.event.DividendInfoListener;
import jessx.business.event.PlayerTypeListener;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;

public class DividendSetupSlidingWindow extends JPanel implements DisplayableNode, ChangeListener, PlayerTypeListener, DividendInfoListener
{
    private DividendModel dividendModel;
    private String assetName;
    JPanel jPanel1;
    GridBagLayout gridBagLayout1;
    JScrollPane jScrollPane1;
    GridBagLayout gridBagLayout2;
    JTable jTable1;
    ButtonGroup buttonGroup1;
    JTabbedPane jTabbedPane1;
    JPanel jpanelNoPlayer;
    private HashMap jPanelDivinfoList;
    TableModelDividendSetup tableModelDividendSetup;
    
    public DividendSetupSlidingWindow(final String assetName, final DividendModel divModel) {
        this.jPanel1 = new JPanel();
        this.gridBagLayout1 = new GridBagLayout();
        this.jScrollPane1 = new JScrollPane();
        this.gridBagLayout2 = new GridBagLayout();
        this.buttonGroup1 = new ButtonGroup();
        this.jTabbedPane1 = new JTabbedPane();
        this.jpanelNoPlayer = new JPanel();
        this.jPanelDivinfoList = new HashMap();
        this.dividendModel = divModel;
        this.assetName = assetName;
        final JTextArea jTextArea = new JTextArea("You have to add a category of players\nbefore completing this section!");
        jTextArea.setEditable(false);
        jTextArea.setBackground(this.jpanelNoPlayer.getBackground());
        jTextArea.setFont(this.jpanelNoPlayer.getFont());
        this.jpanelNoPlayer.add(jTextArea);
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        final Iterator ptIter = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
        if (!BusinessCore.getScenario().getPlayerTypes().isEmpty()) {
            while (ptIter.hasNext()) {
                final String key = (String) ptIter.next();
                this.jPanelDivinfoList.put(key, new DividendInfoPanel(this.getDividendInfo(key)));
                this.jTabbedPane1.add(key, (Component) this.jPanelDivinfoList.get(key));
            }
        }
        else {
            this.jTabbedPane1.add("No player", this.jpanelNoPlayer);
        }
        BusinessCore.getGeneralParameters().addPeriodCountChangeListener(this);
        BusinessCore.getScenario().addPlayerTypeListener(this);
    }
    
    public void setEditable(){
        Iterator iter = this.jPanelDivinfoList.keySet().iterator();
      while (iter.hasNext()){
        String key = (String) iter.next();
        ((DividendInfoPanel) this.jPanelDivinfoList.get(key)).active();
      }
      tableModelDividendSetup.setCellEditable();
      }
    
    public void setUneditable(){
    	  Iterator iter = this.jPanelDivinfoList.keySet().iterator();
    	  while (iter.hasNext()){
    	    String key = (String) iter.next();
    	    ((DividendInfoPanel) this.jPanelDivinfoList.get(key)).desactive();
    	  }
    	  tableModelDividendSetup.setCellUneditable();
    	}
    
    @Override
    public String toString() {
        return this.assetName;
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void stateChanged(final ChangeEvent e) {
        if (e.getSource() instanceof JSpinner) {
            final int periodNum = new Integer(((JSpinner)e.getSource()).getValue().toString());
            ((TableModelDividendSetup)this.jTable1.getModel()).setPeriodCount(periodNum);
        }
    }
    
    public void playerTypeModified(final PlayerTypeEvent e) {
        if (BusinessCore.getAssets().containsKey(this.assetName)) {
            if (e.getEvent() == 1) {
                this.jTabbedPane1.remove(this.jpanelNoPlayer);
                final String key = e.getPlayerType().getPlayerTypeName();
                this.jPanelDivinfoList.put(key, new DividendInfoPanel(this.getDividendInfo(key)));
                this.jTabbedPane1.add(key, (Component) this.jPanelDivinfoList.get(key));
            }
            else if (e.getEvent() == 0) {
                this.jTabbedPane1.remove((Component) this.jPanelDivinfoList.get(e.getPlayerType().getPlayerTypeName()));
                this.jPanelDivinfoList.remove(e.getPlayerType().getPlayerTypeName());
                if (BusinessCore.getScenario().getPlayerTypes().isEmpty()) {
                    this.jTabbedPane1.add("No player", this.jpanelNoPlayer);
                }
            }
        }
    }
    
    public void dividendInfoModified(final DividendInfoEvent e) {
    }
    
    private DividendLimitation getDividendInfo(final String playerType) {
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
        this.setLayout(this.gridBagLayout1);
        this.tableModelDividendSetup = new TableModelDividendSetup(NormalDividend.class, this.dividendModel);
        this.jTable1 = new JTable(this.tableModelDividendSetup);
        this.jPanel1.setDebugGraphicsOptions(0);
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.jScrollPane1.setMinimumSize(new Dimension(50, 50));
        this.add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1, null);
        this.jPanel1.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 0.5, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jTabbedPane1, new GridBagConstraints(1, 0, 1, 1, 0.5, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
    }
}
