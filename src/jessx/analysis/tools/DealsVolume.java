// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

import java.util.Iterator;
import java.util.List;
import org.jfree.data.xy.XYSeries;
import org.jdom.Element;
import jessx.analysis.AnalysisToolsCore;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartPanel;
import org.jfree.data.xy.XYDataset;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.plot.PlotOrientation;
import jessx.analysis.AnalysisToolCreator;
import java.util.HashMap;
import org.jfree.data.xy.XYSeriesCollection;
import org.jdom.Document;
import jessx.analysis.AnalysisTool;
import javax.swing.JPanel;

public class DealsVolume extends JPanel implements AnalysisTool
{
    private Document doc;
    private XYSeriesCollection dataset;
    private HashMap dealsXYSeries;
    
    static {
        try {
            AnalysisToolCreator.analyseFactories.put("DealsVolume", Class.forName("jessx.analysis.tools.DealsVolume"));
            System.out.println("Deals Volume Analysis registered");
        }
        catch (final Exception ex) {
            System.out.println("Problem registering DealsVolume tool: " + ex.toString());
        }
    }
    
    public DealsVolume() {
        this.dealsXYSeries = new HashMap();
    }
    
    public String getToolName() {
        return "Deals volume analysis";
    }
    
    public String getToolAuthor() {
        return "EC-Lille, USTL - 2005 - Mohamed Amine Hamamouchi, Christophe Grosjean.";
    }
    
    public String getToolDescription() {
        return "An analysis that plots exchanged volume on the market.";
    }
    
    public void setDocument(final Document xmlDoc) {
        this.doc = xmlDoc;
    }
    
    public JPanel drawGraph() {
        this.initDataSet();
        final JFreeChart chart = ChartFactory.createXYLineChart("Exchanged Volume", "Time (s)", "Volume (quantity of assets)", this.dataset, PlotOrientation.VERTICAL, true, true, false);
        return new ChartPanel(chart);
    }
    
    private void initDataSet() {
        AnalysisToolsCore.logger.debug("Open XML");
        final Element experimentNode = this.doc.getRootElement();
        final float duration = Float.parseFloat(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodDuration"));
        this.dataset = new XYSeriesCollection();
        final List institutions = experimentNode.getChild("Setup").getChildren("Institution");
        final Iterator institutionsIter = institutions.iterator();
        while (institutionsIter.hasNext()) {
            AnalysisToolsCore.logger.debug("New Institution");
            final Element institution = (Element) institutionsIter.next();
            final String InstitutionName = institution.getAttributeValue("name");
            this.dealsXYSeries.put(InstitutionName, new XYSeries(InstitutionName));
            this.dataset.addSeries((XYSeries) this.dealsXYSeries.get(InstitutionName));
            ((XYSeries) this.dealsXYSeries.get(InstitutionName)).add(0.0, 0.0);
            AnalysisToolsCore.logger.debug(InstitutionName);
        }
        long periodDuration = 0;

        List periods = experimentNode.getChildren("Period");
        Iterator periodsIter = periods.iterator();

        float volumexchange = 0;

        while(periodsIter.hasNext()) {
          Element period = (Element)periodsIter.next();
          List deals = period.getChildren("Deal");
          Iterator dealIter = deals.iterator();

          while (dealIter.hasNext()) {
            Element deal = (Element)dealIter.next();
            {
              volumexchange = (Float.parseFloat(deal.getChild("Deal").getAttributeValue(
                  "quantity")));
              ( (XYSeries) dealsXYSeries.get(deal.getChild("Deal").getAttributeValue(
                  "institution"))).add(Float.parseFloat(deal.getChild("Deal").
                                                        getAttributeValue("timestamp")) /
                                       1000 + periodDuration, 0);
              ( (XYSeries) dealsXYSeries.get(deal.getChild("Deal").getAttributeValue(
                  "institution"))).add(Float.parseFloat(deal.getChild("Deal").
                                                        getAttributeValue("timestamp")) /
                                       1000 + periodDuration, volumexchange);
              ( (XYSeries) dealsXYSeries.get(deal.getChild("Deal").getAttributeValue(
                  "institution"))).add(Float.parseFloat(deal.getChild("Deal").
                                                        getAttributeValue("timestamp")) /
                                       1000 + periodDuration, 0);
            }
          }
          periodDuration += duration;
        }
    }
}
