// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import org.jfree.chart.JFreeChart;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import org.jfree.chart.ChartPanel;
import java.awt.LayoutManager;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import javax.swing.ListModel;
import javax.swing.DefaultListModel;
import jessx.utils.Utils;
import jessx.business.Deal;
import org.jfree.data.xy.XYSeries;
import jessx.net.ExpUpdate;
import org.jdom.Document;
import jessx.client.ClientCore;
import javax.swing.JList;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import org.jfree.data.xy.XYSeriesCollection;
import java.util.HashMap;
import jessx.client.event.NetworkListener;
import javax.swing.JPanel;

public class MarketEvolutionGraph extends JPanel implements NetworkListener
{
    private HashMap dealXYSeries;
    private XYSeriesCollection dataset;
    private float timeSinceBeginning;
    private JPanel priceBar;
    private GridBagLayout gridBagLayout1;
    private JLabel priceBarTitle;
    private JList listPriceAndQty;
    
    private void increaseTimeFromBeginning(final long time) {
        this.timeSinceBeginning += time;
    }
    
    public MarketEvolutionGraph() {
        this.dealXYSeries = new HashMap();
        this.dataset = new XYSeriesCollection();
        this.timeSinceBeginning = 0.0f;
        this.priceBar = new JPanel();
        this.gridBagLayout1 = new GridBagLayout();
        this.priceBarTitle = new JLabel();
        this.listPriceAndQty = new JList();
        ClientCore.addNetworkListener(this, "ExperimentUpdate");
    }
    
    public void objectReceived(final Document xmlDoc) {
        if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
            final ExpUpdate update = new ExpUpdate(0, "", 0);
            if (update.initFromNetworkInput(xmlDoc.getRootElement()) && update.getUpdateType() == 2 && update.getCurrentPeriod() != 0) {
                this.increaseTimeFromBeginning(Long.parseLong(update.getUpdateMessage()));
            }
        }
    }
    
    public void addAssetEvolution(final String institutionName) {
        this.dealXYSeries.put(institutionName, new XYSeries(institutionName));
        this.dataset.addSeries((XYSeries) this.dealXYSeries.get(institutionName));
    }
    
    public void addDeal(final Deal deal) {
        if (50 < ((XYSeries) this.dealXYSeries.get(deal.getDealInstitution())).getItemCount()) {
            ((XYSeries) this.dealXYSeries.get(deal.getDealInstitution())).remove(0);
        }
        ((XYSeries) this.dealXYSeries.get(deal.getDealInstitution())).add((deal.getTimestamp() + this.timeSinceBeginning) / 1000.0f, deal.getDealPrice());
        Utils.logger.debug("deal price: " + deal.getDealPrice());
        Utils.logger.debug("deal timestamp: " + deal.getTimestamp());
        Utils.logger.debug("deal time since beginning: " + this.timeSinceBeginning);
        final DefaultListModel model = (DefaultListModel)this.listPriceAndQty.getModel();
        if (model.getSize() > 4) {
            model.removeElementAt(0);
        }
        model.addElement(new Float(deal.getDealPrice()) + " / " + deal.getQuantity() + "  ");
        this.listPriceAndQty.setModel(model);
    }
    
    public void initGraphic() {
        final JFreeChart chart = ChartFactory.createXYLineChart(null, "Time (s)", "Price ($)", this.dataset, PlotOrientation.VERTICAL, true, false, false);
        this.setLayout(new GridBagLayout());
        this.add(new ChartPanel(chart), new GridBagConstraints(0, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        this.priceBar.setLayout(this.gridBagLayout1);
        this.priceBarTitle.setText("Last Deals : Price/Qty");
        this.priceBarTitle.setFont(new Font(this.priceBarTitle.getFont().getName(), 1, this.priceBarTitle.getFont().getSize()));
        this.listPriceAndQty.setLayoutOrientation(2);
        this.listPriceAndQty.setVisibleRowCount(-1);
        final DefaultListModel model = new DefaultListModel();
        this.listPriceAndQty.setBackground(new Color(224, 223, 227));
        this.listPriceAndQty.setOpaque(false);
        this.listPriceAndQty.setRequestFocusEnabled(false);
        this.listPriceAndQty.setModel(model);
        this.priceBar.add(this.priceBarTitle, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 17, 1, new Insets(0, 0, 0, 0), 1, 1));
        this.priceBar.add(this.listPriceAndQty, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 17, 1, new Insets(0, 0, 0, 0), 1, 1));
        this.add(this.priceBar, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
    }
}
