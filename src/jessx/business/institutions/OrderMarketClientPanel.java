// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.institutions;

import org.jdom.Document;

import jessx.business.Operation;
import jessx.business.OrderBook;
import javax.swing.event.TableModelEvent;
import jessx.business.Order;
import java.awt.Color;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.table.TableCellEditor;
import jessx.utils.gui.JButtonRenderer;
import jessx.business.operations.DeleteOrder;
import javax.swing.table.TableCellRenderer;
import jessx.utils.gui.JLabelRenderer;
import java.awt.SystemColor;
import java.awt.Dimension;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import jessx.utils.Constants;
import javax.swing.BorderFactory;
import javax.swing.table.TableModel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import java.awt.GridBagLayout;
import jessx.client.ClientCore;
import jessx.utils.Utils;
import jessx.business.Operator;
import javax.swing.JTable;
import jessx.client.event.NetworkListener;
import javax.swing.JPanel;

public class OrderMarketClientPanel extends JPanel implements NetworkListener
{
    private JTable jTableWaitingOrders;
    private JTable jTableBid;
    private JTable jTableAsk;
    private CellFading cellFading;
    private Operator operator;
    private long lastTimeStamp;
    
    public OrderMarketClientPanel() {
        try {
            this.jbInit();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public OrderMarketClientPanel(final Operator operator) {
        this.operator = operator;
        this.panelInit();
        Utils.logger.debug("Launching cell fading capabilities...");
        (this.cellFading = new CellFading(this.jTableAsk, this.jTableBid)).start();
        Utils.logger.debug("Registering the panel as an OrderBook object listener...");
        ClientCore.addNetworkListener(this, "OrderBook");
        Utils.logger.debug("Ordermarket panel init finished successfully.");
    }
    
    private void panelInit() {
        Utils.logger.debug("Initing the orderbook panel...");
        final GridBagLayout gridBagLayoutWaitingOrders = new GridBagLayout();
        final JSplitPane jSplitPane1 = new JSplitPane();
        final JPanel jPanelWaitingOrders = new JPanel();
        final JScrollPane jScrollPaneWaitingOrders = new JScrollPane();
        final JPanel jPanelPortfolio = new JPanel();
        final GridBagLayout gridBagLayout4 = new GridBagLayout();
        final JScrollPane jScrollPaneBid = new JScrollPane();
        final JScrollPane jScrollPaneAsk = new JScrollPane();
        final TWaitingOrdersModel waitingOrdersTableModel = new TWaitingOrdersModel(0, this.operator);
        this.jTableWaitingOrders = new JTable(waitingOrdersTableModel);
        final String[] enteteBidTable = { "Bid Price", "Bid Quantity" };
        final DefaultTableModel tableBidTableModel = new TOrderbookTableModel(enteteBidTable, this.operator.getOrderBookVisibility());
        this.jTableBid = new JTable(tableBidTableModel);
        final String[] enteteAskTable1Table = { "Ask Price", "Ask Quantity" };
        final DefaultTableModel tableAskTableModel = new TOrderbookTableModel(enteteAskTable1Table, this.operator.getOrderBookVisibility());
        this.jTableAsk = new JTable(tableAskTableModel);
        final TitledBorder titledBorder6 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "My Pending Orders", 0, 0, Constants.FONT_CLIENT_TITLE_BORDER);
        final TitledBorder titledBorder7 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "Market Orderbook", 0, 0, Constants.FONT_CLIENT_TITLE_BORDER);
        jPanelPortfolio.setBorder(titledBorder7);
        jPanelPortfolio.setLayout(gridBagLayout4);
        jPanelPortfolio.add(jScrollPaneBid, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 17, 1, new Insets(0, 0, 0, 0), 0, 0));
        jPanelPortfolio.add(jScrollPaneAsk, new GridBagConstraints(1, 0, 1, 1, 1.0, 1.0, 13, 1, new Insets(0, 0, 0, 0), 0, 0));
        jPanelWaitingOrders.setLayout(gridBagLayoutWaitingOrders);
        jSplitPane1.setOrientation(0);
        jSplitPane1.setPreferredSize(new Dimension(500, 300));
        jSplitPane1.setBottomComponent(jPanelWaitingOrders);
        jSplitPane1.add(jPanelWaitingOrders, "bottom");
        jPanelWaitingOrders.add(jScrollPaneWaitingOrders, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), -295, -391));
        jSplitPane1.add(jPanelPortfolio, "top");
        jSplitPane1.setDividerLocation(200);
        jPanelWaitingOrders.setBorder(titledBorder6);
        jPanelWaitingOrders.setMaximumSize(new Dimension(2048, 1536));
        jPanelWaitingOrders.setMinimumSize(new Dimension(100, 100));
        jPanelWaitingOrders.setPreferredSize(new Dimension(100, 150));
        jScrollPaneWaitingOrders.setVerticalScrollBarPolicy(20);
        jScrollPaneWaitingOrders.setMinimumSize(new Dimension(24, 90));
        jScrollPaneWaitingOrders.setPreferredSize(new Dimension(454, 150));
        jScrollPaneWaitingOrders.getViewport().add(this.jTableWaitingOrders, null);
        this.jTableWaitingOrders.setMinimumSize(new Dimension(0, 0));
        this.jTableWaitingOrders.setCellSelectionEnabled(true);
        jScrollPaneAsk.setVerticalScrollBarPolicy(20);
        jScrollPaneBid.setVerticalScrollBarPolicy(20);
        jScrollPaneBid.setPreferredSize(new Dimension(454, 400));
        jScrollPaneAsk.getViewport().add(this.jTableAsk, null);
        jScrollPaneBid.getViewport().add(this.jTableBid, null);
        this.jTableBid.setBackground(Constants.CLIENT_BUY_INACTIVE);
        this.jTableBid.setForeground(SystemColor.BLACK);
        this.jTableBid.setGridColor(SystemColor.BLACK);
        this.jTableBid.setMinimumSize(new Dimension(30, 80));
        this.jTableBid.setCellSelectionEnabled(false);
        this.jTableAsk.setBackground(Constants.CLIENT_SELL_INACTIVE);
        this.jTableAsk.setForeground(SystemColor.black);
        this.jTableAsk.setGridColor(SystemColor.black);
        this.jTableAsk.setColumnSelectionAllowed(false);
        this.jTableAsk.setRowSelectionAllowed(false);
        this.jTableAsk.getColumnModel().getColumn(0).setCellRenderer(new JLabelRenderer());
        this.jTableAsk.getColumnModel().getColumn(1).setCellRenderer(new JLabelRenderer());
        this.jTableBid.getColumnModel().getColumn(0).setCellRenderer(new JLabelRenderer());
        this.jTableBid.getColumnModel().getColumn(1).setCellRenderer(new JLabelRenderer());
        this.jTableWaitingOrders.getColumnModel().getColumn(0).setCellRenderer(new JLabelRenderer());
        this.jTableWaitingOrders.getColumnModel().getColumn(1).setCellRenderer(new JLabelRenderer());
        this.jTableWaitingOrders.getColumnModel().getColumn(2).setCellRenderer(new JLabelRenderer());
        if (this.operator.isGrantedTo(new DeleteOrder().getOperationName())) {
            this.jTableWaitingOrders.getColumnModel().getColumn(3).setCellRenderer(new JButtonRenderer());
            this.jTableWaitingOrders.getColumnModel().getColumn(3).setCellEditor(new JButtonEditor());
        }
        this.jTableBid.setRowHeight(20);
        this.jTableAsk.setRowHeight(this.jTableBid.getRowHeight());
        this.jTableWaitingOrders.setRowHeight(18);
        this.jTableBid.setBackground(UIManager.getColor("Button.background"));
        this.jTableAsk.setBackground(UIManager.getColor("Button.background"));
        this.setLayout(new GridBagLayout());
        this.add(jSplitPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        Utils.logger.debug("Init finished.");
    }
    
    private long updateObTable(final Vector sideOrders, final JTable table, final Color iColor, final Color interColor, final Color fColor, final int side, final long tempLastTS) {
        int indiceOb = 0;
        int indiceTable = 0;
        boolean isOrderOurs = false;
        int qttySum = 0;
        long tempLastTimeStamp = tempLastTS;
        Utils.logger.debug("Updating " + ((side == 0) ? "ask" : "bid") + " side...");
        while (indiceOb < sideOrders.size() && indiceTable < this.operator.getOrderBookVisibility()) {
            if (indiceOb < sideOrders.size() - 1 && ((Order) sideOrders.elementAt(indiceOb)).getOrderPrice(side) == ((Order) sideOrders.elementAt(indiceOb + 1)).getOrderPrice(side)) {
                qttySum += ((Order) sideOrders.elementAt(indiceOb)).getMaxQtty();
                if (((Operation) sideOrders.elementAt(indiceOb)).getEmitter().equals(ClientCore.getLogin())) {
                    isOrderOurs = true;
                }
            }
            else {
                if (((Operation) sideOrders.elementAt(indiceOb)).getEmitter().equals(ClientCore.getLogin())) {
                    isOrderOurs = true;
                }
                if (((Order) sideOrders.elementAt(indiceOb)).getTimestamp() > tempLastTimeStamp) {
                    tempLastTimeStamp = ((Order) sideOrders.elementAt(indiceOb)).getTimestamp();
                }
                if (((Order) sideOrders.elementAt(indiceOb)).getTimestamp() > this.lastTimeStamp) {
                    table.setValueAt(Utils.createColoredLabel(String.valueOf(isOrderOurs ? "" : "") + new Float(((Order) sideOrders.elementAt(indiceOb)).getOrderPrice(side)).toString() + (isOrderOurs ? "" : ""), Constants.FONT_DEFAULT_LABEL, iColor), indiceTable, 0);
                    table.setValueAt(Utils.createColoredLabel(String.valueOf(isOrderOurs ? "" : "") + new Integer(((Order) sideOrders.elementAt(indiceOb)).getMaxQtty() + qttySum).toString() + (isOrderOurs ? "" : ""), Constants.FONT_DEFAULT_LABEL, iColor), indiceTable, 1);
                    this.cellFading.registerCell(side, 0, indiceTable, iColor, isOrderOurs ? interColor : fColor);
                    this.cellFading.registerCell(side, 1, indiceTable, iColor, isOrderOurs ? interColor : fColor);
                }
                else {
                    Utils.logger.debug("displaying value: qtty=" + qttySum);
                    table.setValueAt(Utils.createColoredLabel(String.valueOf(isOrderOurs ? "" : "") + new Float(((Order) sideOrders.elementAt(indiceOb)).getOrderPrice(side)).toString() + (isOrderOurs ? "" : ""), Constants.FONT_DEFAULT_LABEL, isOrderOurs ? interColor : fColor), indiceTable, 0);
                    table.setValueAt(Utils.createColoredLabel(String.valueOf(isOrderOurs ? "" : "") + new Integer(((Order) sideOrders.elementAt(indiceOb)).getMaxQtty() + qttySum).toString() + (isOrderOurs ? "" : ""), Constants.FONT_DEFAULT_LABEL, isOrderOurs ? interColor : fColor), indiceTable, 1);
                }
                isOrderOurs = false;
                ++indiceTable;
                qttySum = 0;
            }
            ++indiceOb;
        }
        for (int i = indiceTable; i < table.getRowCount(); ++i) {
            table.setValueAt(Utils.createColoredLabel("", Constants.FONT_DEFAULT_LABEL, fColor), i, 0);
            table.setValueAt(Utils.createColoredLabel("", Constants.FONT_DEFAULT_LABEL, fColor), i, 1);
        }
        table.tableChanged(new TableModelEvent(table.getModel()));
        return tempLastTimeStamp;
    }
    
    public void updateClientInterface(final OrderBook orderBook) {
        long tempLastTimeStamp = this.updateObTable(orderBook.getBid(), this.jTableBid, Constants.CLIENT_BUY_HL, Constants.CLIENT_BUY_INTERMEDIARY, Constants.CLIENT_BUY_INACTIVE, 1, 0L);
        tempLastTimeStamp = this.updateObTable(orderBook.getAsk(), this.jTableAsk, Constants.CLIENT_SELL_HL, Constants.CLIENT_SELL_INTERMEDIARY, Constants.CLIENT_SELL_INACTIVE, 0, tempLastTimeStamp);
        this.lastTimeStamp = tempLastTimeStamp;
        int i = 0;
        for (int j = 0; j < orderBook.getAsk().size(); ++j) {
            if (((Operation) orderBook.getAsk().elementAt(j)).getEmitter().equals(ClientCore.getLogin())) {
                final Order tempOrder = (Order) orderBook.getAsk().elementAt(j);
                this.jTableWaitingOrders.setValueAt("Ask", i, 0);
                this.jTableWaitingOrders.setValueAt(new Float(tempOrder.getOrderPrice(0)), i, 1);
                this.jTableWaitingOrders.setValueAt(new Integer(tempOrder.getMaxQtty()), i, 2);
                if (this.operator.isGrantedTo(new DeleteOrder().getOperationName())) {
                    this.jTableWaitingOrders.setValueAt(new JOrderButton(tempOrder), i, 3);
                }
                ++i;
            }
        }
        for (int j = 0; j < orderBook.getBid().size(); ++j) {
            if (((Operation) orderBook.getBid().elementAt(j)).getEmitter().equals(ClientCore.getLogin())) {
                final Order tempOrder = (Order) orderBook.getBid().elementAt(j);
                this.jTableWaitingOrders.setValueAt("Bid", i, 0);
                this.jTableWaitingOrders.setValueAt(new Float(tempOrder.getOrderPrice(1)), i, 1);
                this.jTableWaitingOrders.setValueAt(new Integer(tempOrder.getMaxQtty()), i, 2);
                if (this.operator.isGrantedTo(new DeleteOrder().getOperationName())) {
                    this.jTableWaitingOrders.setValueAt(new JOrderButton(tempOrder), i, 3);
                }
                ++i;
            }
        }
        for (int k = this.jTableWaitingOrders.getRowCount() - 1; k >= i; --k) {
            this.jTableWaitingOrders.setValueAt(null, k, 0);
        }
        this.jTableWaitingOrders.tableChanged(new TableModelEvent(this.jTableWaitingOrders.getModel()));
    }
    
    public void objectReceived(final Document root) {
        if (root.getRootElement().getName().equals("OrderBook")) {
            final OrderBook ob = new OrderBook();
            Utils.logger.debug("Reading orderbook...");
            ob.initFromNetworkInput(root.getRootElement());
            Utils.logger.debug("This is the orderbook of the institution: " + ob.getInstitution());
            Utils.logger.debug("This panel is displaying the orderbook of the institution: " + this.operator.getInstitution());
            if (ob.getInstitution().equals(this.operator.getInstitution())) {
                Utils.logger.debug("Updating panels...");
                this.updateClientInterface(ob);
            }
        }
    }
    
    private void jbInit() throws Exception {
    }
}
