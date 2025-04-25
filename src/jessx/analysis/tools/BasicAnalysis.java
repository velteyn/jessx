// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

import jessx.analysis.AnalysisToolsCore;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.JFreeChart;
import java.util.Iterator;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import java.awt.Component;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jdom.Element;
import javax.swing.JTabbedPane;
import jessx.analysis.AnalysisToolCreator;
import java.util.List;
import java.util.HashMap;
import org.jdom.Document;
import jessx.analysis.AnalysisTool;
import javax.swing.JPanel;

public class BasicAnalysis extends JPanel implements AnalysisTool
{
    private Document doc;
    private long sessionDuration;
    private HashMap datasetInstitution;
    private List institutions;
    
    static {
        try {
            AnalysisToolCreator.analyseFactories.put("BasicAnalysis", Class.forName("jessx.analysis.tools.BasicAnalysis"));
            System.out.println("Basic analysis registered");
        }
        catch (final Exception ex) {
            System.out.println("Problem registering BasicAnalysis tool: " + ex.toString());
        }
    }
    
    public BasicAnalysis() {
        this.sessionDuration = 0L;
        this.datasetInstitution = new HashMap();
    }
    
    public String getToolName() {
        return "Basic analysis";
    }
    
    public String getToolAuthor() {
        return "EC-Lille, USTL - 2005 - Thierry Curtil, Julien Terrier, Christophe Grosjean.";
    }
    
    public String getToolDescription() {
        return "An analysis that plots price evolution on the market.";
    }
    
    public void setDocument(final Document xmlDoc) {
        this.doc = xmlDoc;
    }
    
    public JPanel drawGraph() {
        final JPanel returnPanel = new JPanel();
        this.initDataSet();
        final Iterator institutionsIter = this.institutions.iterator();
        final JTabbedPane tempTabbedPane = new JTabbedPane();
        while (institutionsIter.hasNext()) {
            final Element instit = (Element) institutionsIter.next();
            final String institutionName = instit.getAttributeValue("name");
            final XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(institutionName) + "ask"));
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(institutionName) + "bid"));
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(institutionName) + "price"));
            final JPanel institution = new ChartPanel(this.CreateChart(dataset));
            tempTabbedPane.add(institution, institutionName);
        }
        final GridBagLayout gridBagLayout1 = new GridBagLayout();
        returnPanel.setLayout(gridBagLayout1);
        returnPanel.add(tempTabbedPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        return returnPanel;
    }
    
    private JFreeChart CreateChart(final XYSeriesCollection dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart("Best limits", "Time (s)", "Price ($)", dataset, PlotOrientation.VERTICAL, true, true, false);
        return chart;
    }
    
    private void initDataSet() {
        AnalysisToolsCore.logger.debug("Open XML");
        final Element experimentNode = this.doc.getRootElement();
        institutions = experimentNode.getChild("Setup").getChildren("Institution");
        Iterator institutionsIter = institutions.iterator();
        while(institutionsIter.hasNext()) {
          Element institution = (Element)institutionsIter.next();
          String institutionName =institution.getAttributeValue("name");
          datasetInstitution.put(institutionName+"ask",new XYSeries("Best Ask Limit"));
          datasetInstitution.put(institutionName+"bid",new XYSeries("Best Bid Limit"));
          datasetInstitution.put(institutionName+"price",new XYSeries("Price"));
          AnalysisToolsCore.logger.debug("New Institution"+institutionName);

        }
        List periods = experimentNode.getChildren("Period");
        AnalysisToolsCore.logger.debug("List of periods created");
        Iterator periodsIter = periods.iterator();
        while(periodsIter.hasNext()) {
        	Element period = (Element)periodsIter.next();
            List orderBooks = period.getChildren("OrderBook");
            AnalysisToolsCore.logger.debug("List of OrderBooks created");
            Iterator orderBooksIter = orderBooks.iterator();

            while(orderBooksIter.hasNext()) {
                Element orderBook = (Element)orderBooksIter.next();
                float price = this.getBestBidFromOrderBook(orderBook);
                if (price >= 0) {
                 ((XYSeries) datasetInstitution.get(orderBook.getChild("Bid").getChild("Operation").getAttributeValue("institution")+"bid")).
                     add(Float.parseFloat(orderBook.getAttributeValue("timestamp"))/1000 + this.sessionDuration, price);
                }

                price = this.getBestAskFromOrderBook(orderBook);
                if (price >= 0) {
                  ((XYSeries) datasetInstitution.get(orderBook.getChild("Ask").getChild("Operation").getAttributeValue("institution")+"ask")).
                      add(Float.parseFloat(orderBook.getAttributeValue("timestamp"))/1000 + this.sessionDuration, price);
                }

              }
            final List deals = period.getChildren("Deal");
            AnalysisToolsCore.logger.debug("List of deals created");
            Iterator dealIter = deals.iterator();
            float time;
            float price;
            while (dealIter.hasNext()) {
              Element deal = (Element)dealIter.next();
              time = Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp"))/1000 + this.sessionDuration;
              price = Float.parseFloat(deal.getChild("Deal").getAttributeValue("price"));
              ((XYSeries) datasetInstitution.get(deal.getChild("Deal").getAttributeValue("institution")+"price")).
                  add(time, price);
              ((XYSeries) datasetInstitution.get(deal.getChild("Deal").getAttributeValue("institution")+"ask")).
                  add(time, price);
              ((XYSeries) datasetInstitution.get(deal.getChild("Deal").getAttributeValue("institution")+"bid")).
                  add(time, price);
            }
            this.sessionDuration += (long)Float.parseFloat(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodDuration"));
        }
    }
    
    private float getBestBidFromOrderBook(final Element orderBook) {
        if (orderBook.getChild("Bid").getChild("Operation") == null || orderBook.getChild("Ask").getChild("Operation") == null) {
            return -1.0f;
        }
        if (orderBook.getChild("Bid").getChild("Operation").getChild("LimitOrder") != null) {
            return Float.parseFloat(orderBook.getChild("Bid").getChild("Operation").getChild("LimitOrder").getAttributeValue("price"));
        }
        return Float.parseFloat(orderBook.getChild("Bid").getChild("Operation").getChild("BestLimitOrder").getAttributeValue("price"));
    }
    
    private float getBestAskFromOrderBook(final Element orderBook) {
        if (orderBook.getChild("Ask").getChild("Operation") == null || orderBook.getChild("Bid").getChild("Operation") == null) {
            return -1.0f;
        }
        if (orderBook.getChild("Ask").getChild("Operation").getChild("LimitOrder") != null) {
            return Float.parseFloat(orderBook.getChild("Ask").getChild("Operation").getChild("LimitOrder").getAttributeValue("price"));
        }
        return Float.parseFloat(orderBook.getChild("Ask").getChild("Operation").getChild("BestLimitOrder").getAttributeValue("price"));
    }
}
