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

public class MarketActivity extends JPanel implements AnalysisTool
{
    private Document doc;
    private XYSeriesCollection dataset;
    private HashMap ActivityXYSeries;
    
    static {
        try {
            AnalysisToolCreator.analyseFactories.put("MarketActivity", Class.forName("jessx.analysis.tools.MarketActivity"));
            System.out.println("Market Activity Analysis registered");
        }
        catch (final Exception ex) {
            System.out.println("Problem registering MarketActivity tool: " + ex.toString());
        }
    }
    
    public MarketActivity() {
        this.ActivityXYSeries = new HashMap();
    }
    
    public String getToolName() {
        return "Market activity analysis";
    }
    
    public String getToolAuthor() {
        return "EC-Lille, USTL - 2005 - Clement Plaignaud, Christophe Grosjean.";
    }
    
    public String getToolDescription() {
        return "An analysis that plots exchanged volume on the market.";
    }
    
    public void setDocument(final Document xmlDoc) {
        this.doc = xmlDoc;
    }
    
    public JPanel drawGraph() {
        this.initDataSet();
        final JFreeChart chart = ChartFactory.createXYLineChart("Exchanged Cash", "Time (s)", "Sum of Cash ($)", this.dataset, PlotOrientation.VERTICAL, true, true, false);
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
            this.ActivityXYSeries.put(InstitutionName, new XYSeries(InstitutionName));
            this.dataset.addSeries((XYSeries) this.ActivityXYSeries.get(InstitutionName));
            ((XYSeries) this.ActivityXYSeries.get(InstitutionName)).add(0.0, 0.0);
            AnalysisToolsCore.logger.debug(InstitutionName);
        }
        long periodDuration = 0L;
        final List periods = experimentNode.getChildren("Period");
        final Iterator periodsIter = periods.iterator();
        float cashexchange = 0.0f;
        while (periodsIter.hasNext()) {
            final Element period = (Element) periodsIter.next();
            final List deals = period.getChildren("Deal");
            Iterator dealIter = deals.iterator();
            while (dealIter.hasNext()) {
              Element deal = (Element)dealIter.next();
                cashexchange += Float.parseFloat(deal.getChild("Deal").getAttributeValue("price")) * Float.parseFloat(deal.getChild("Deal").getAttributeValue("quantity"));
                ((XYSeries) this.ActivityXYSeries.get(deal.getChild("Deal").getAttributeValue("institution"))).add(Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0f + periodDuration, cashexchange);
            }
            periodDuration += (long)duration;
        }
    }
}
