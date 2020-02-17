package jessx.gclient.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import jessx.business.Deal;
import jessx.client.ClientCore;
import jessx.client.event.NetworkListener;
import jessx.net.ExpUpdate;
import jessx.utils.Utils;
import org.jdom.Document;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class MarketEvolutionGraph extends JPanel implements NetworkListener {
  private HashMap dealXYSeries = new HashMap<Object, Object>();
  
  private XYSeriesCollection dataset = new XYSeriesCollection();
  
  private float timeSinceBeginning = 0.0F;
  
  private JPanel priceBar = new JPanel();
  
  private GridBagLayout gridBagLayout1 = new GridBagLayout();
  
  private JLabel priceBarTitle = new JLabel();
  
  private JList listPriceAndQty = new JList();
  
  private void increaseTimeFromBeginning(long time) {
    this.timeSinceBeginning += (float)time;
  }
  
  public MarketEvolutionGraph() {
    ClientCore.addNetworkListener(this, "ExperimentUpdate");
  }
  
  public void objectReceived(Document xmlDoc) {
    if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
      ExpUpdate update = new ExpUpdate(0, "", 0);
      if (update.initFromNetworkInput(xmlDoc.getRootElement()) && 
        update.getUpdateType() == 2 && 
        update.getCurrentPeriod() != 0)
        increaseTimeFromBeginning(Long.parseLong(update
              .getUpdateMessage())); 
    } 
  }
  
  public void addAssetEvolution(String institutionName) {
    this.dealXYSeries.put(institutionName, new XYSeries(institutionName));
    this.dataset.addSeries((XYSeries)this.dealXYSeries.get(institutionName));
  }
  
  public void addDeal(Deal deal) {
    if (50 < ((XYSeries)this.dealXYSeries.get(deal.getDealInstitution())).getItemCount())
      ((XYSeries)this.dealXYSeries.get(deal.getDealInstitution())).remove(0); 
    ((XYSeries)this.dealXYSeries.get(deal.getDealInstitution())).add(((
        (float)deal.getTimestamp() + this.timeSinceBeginning) / 1000.0F), 
        deal.getDealPrice());
    Utils.logger.debug("deal price: " + deal.getDealPrice());
    Utils.logger.debug("deal timestamp: " + deal.getTimestamp());
    Utils.logger.debug("deal time since beginning: " + this.timeSinceBeginning);
    DefaultListModel<String> model = (DefaultListModel)this.listPriceAndQty.getModel();
    if (model.getSize() > 4)
      model.removeElementAt(0); 
    model.addElement(new Float(deal.getDealPrice()) + " / " + deal.getQuantity() + "  ");
    this.listPriceAndQty.setModel(model);
  }
  
  public void initGraphic() {
    JFreeChart chart = ChartFactory.createXYLineChart(
        null, 
        "Time (s)", 
        "Price ($)", 
        (XYDataset)this.dataset, 
        PlotOrientation.VERTICAL, 
        true, 
        false, 
        false);
    setLayout(new GridBagLayout());
    add((Component)new ChartPanel(chart), new GridBagConstraints(0, 1, 1, 1, 1.0D, 1.0D, 
          10, 1, 
          new Insets(4, 4, 4, 4), 0, 0));
    this.priceBar.setLayout(this.gridBagLayout1);
    this.priceBarTitle.setText("Last Deals : Price/Qty");
    this.priceBarTitle.setFont(new Font(this.priceBarTitle.getFont().getName(), 1, this.priceBarTitle.getFont().getSize()));
    this.listPriceAndQty.setLayoutOrientation(2);
    this.listPriceAndQty.setVisibleRowCount(-1);
    DefaultListModel model = new DefaultListModel();
    this.listPriceAndQty.setBackground(new Color(224, 223, 227));
    this.listPriceAndQty.setOpaque(false);
    this.listPriceAndQty.setRequestFocusEnabled(false);
    this.listPriceAndQty.setModel(model);
    this.priceBar.add(this.priceBarTitle, new GridBagConstraints(0, 0, 1, 1, 1.0D, 0.0D, 
          17, 
          1, 
          new Insets(0, 0, 0, 0), 1, 1));
    this.priceBar.add(this.listPriceAndQty, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 
          17, 
          1, 
          new Insets(0, 0, 0, 0), 1, 1));
    add(this.priceBar, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 
          10, 
          1, 
          new Insets(4, 4, 4, 4), 0, 0));
  }
}
