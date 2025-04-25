// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

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
import jessx.utils.Utils;
import jessx.analysis.AnalysisToolsCore;
import jessx.analysis.AnalysisToolCreator;
import java.util.List;
import java.util.HashMap;
import java.util.Vector;
import org.jdom.Document;
import jessx.analysis.AnalysisTool;
import javax.swing.JPanel;

public class HoldingValue extends JPanel implements AnalysisTool
{
    private Document doc;
    private long sessionDuration;
    private float periodDuration;
    private Vector<Float> interestRate;
    private HashMap datasetInstitution;
    private List institutions;
    
    static {
        try {
            AnalysisToolCreator.analyseFactories.put("HoldingValue", Class.forName("jessx.analysis.tools.HoldingValue"));
            System.out.println("Holding Value registered");
        }
        catch (final Exception ex) {
            System.out.println("Problem registering HoldingValue tool: " + ex.toString());
        }
    }
    
    public HoldingValue() {
        this.sessionDuration = 0L;
        this.interestRate = new Vector<Float>();
        this.datasetInstitution = new HashMap();
    }
    
    public String getToolName() {
        return "Holding Value";
    }
    
    public String getToolAuthor() {
        return "EC-Lille, USTL - 2005 - Mohamed Hamamouchi.";
    }
    
    public String getToolDescription() {
        return "An analysis that plots price evolution on the market and the holding value.";
    }
    
    public void setDocument(final Document xmlDoc) {
        this.doc = xmlDoc;
    }
    
    public float gethv(final int nPeriod, final List meanDividend) {
        AnalysisToolsCore.logger.debug("gethv" + nPeriod);
        float hv = 0.0f;
        for (int i = nPeriod; i < meanDividend.size(); ++i) {
            AnalysisToolsCore.logger.debug(meanDividend.get(i));
            final float div = Float.parseFloat((String) meanDividend.get(i));
            AnalysisToolsCore.logger.debug(new StringBuilder().append(div).toString());
            int interestRateIndex;
            if (i >= this.interestRate.size()) {
                interestRateIndex = this.interestRate.size() - 1;
            }
            else {
                interestRateIndex = i;
            }
            hv = hv * (1.0f + this.interestRate.elementAt(interestRateIndex) / 100.0f) + div;
            Utils.logger.error(" (gethv) " + interestRateIndex);
        }
        hv *= 100.0f;
        hv = (float)(int)(hv + 0.5);
        hv /= 100.0f;
        AnalysisToolsCore.logger.debug("gethv" + nPeriod);
        return hv;
    }
    
    public float gethvSize(final int nPeriod, final int size, final List meanDividend) {
        AnalysisToolsCore.logger.debug("gethvSize" + nPeriod);
        float hv = 0.0f;
        for (int i = nPeriod; i < nPeriod + size; ++i) {
            AnalysisToolsCore.logger.debug(meanDividend.get(i));
            final float div = Float.parseFloat((String) meanDividend.get(i));
            AnalysisToolsCore.logger.debug(new StringBuilder().append(div).toString());
            int interestRateIndex;
            if (i >= this.interestRate.size()) {
                interestRateIndex = this.interestRate.size() - 1;
            }
            else {
                interestRateIndex = i;
            }
            hv = hv * (1.0f + this.interestRate.elementAt(interestRateIndex) / 100.0f) + div;
            Utils.logger.error(" " + interestRateIndex);
        }
        hv *= 100.0f;
        hv = (float)(int)(hv + 0.5);
        hv /= 100.0f;
        AnalysisToolsCore.logger.debug("gethvSize" + nPeriod);
        return hv;
    }
    
    public JPanel drawGraph() {
        System.out.print("drawGraphHolding");
        final JPanel returnPanel = new JPanel();
        System.out.print("drawGraph1");
        this.initDataSet();
        System.out.print("drawGraph2");
        final Iterator institutionsIter = this.institutions.iterator();
        final JTabbedPane tempTabbedPane = new JTabbedPane();
        while (institutionsIter.hasNext()) {
            final Element instit = (Element) institutionsIter.next();
            final String institutionName = instit.getAttributeValue("name");
            final XYSeriesCollection dataset = new XYSeriesCollection();
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(institutionName) + "price"));
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(instit.getAttributeValue("quotedAsset")) + "HV"));
            dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(instit.getAttributeValue("quotedAsset")) + "HVsize"));
            final JPanel institution = new ChartPanel(this.CreateChart(dataset));
            tempTabbedPane.add(institution, institutionName);
        }
        final GridBagLayout gridBagLayout1 = new GridBagLayout();
        returnPanel.setLayout(gridBagLayout1);
        returnPanel.add(tempTabbedPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        return returnPanel;
    }
    
    private JFreeChart CreateChart(final XYSeriesCollection dataset) {
        final JFreeChart chart = ChartFactory.createXYLineChart("Holding Value", "Time (s)", "Price ($)", dataset, PlotOrientation.VERTICAL, true, true, false);
        return chart;
    }
    
    private void initDataSet() {
        AnalysisToolsCore.logger.debug("Open XML");
        final Element experimentNode = this.doc.getRootElement();
        this.periodDuration = Float.parseFloat(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodDuration"));
        this.institutions = experimentNode.getChild("Setup").getChildren("Institution");
        Iterator institutionsIter = institutions.iterator();
        while (institutionsIter.hasNext()) {
          Element institution = (Element) institutionsIter.next();
            final String InstitutionName = institution.getAttributeValue("name");
            this.datasetInstitution.put(String.valueOf(InstitutionName) + "price", new XYSeries("Price"));
            AnalysisToolsCore.logger.debug("New Institution" + InstitutionName);
        }
        final List periods = experimentNode.getChildren("Period");
        final Element interestNode = experimentNode.getChild("Setup").getChild("GeneralParameters").getChild("InterestRate");
        final String periodCount = interestNode.getAttributeValue("periodCount");
        this.interestRate.setSize(periods.size());
        if (periodCount == null) {
            Utils.logger.error("invalid xml, assuming it is an older version");
            for (int i = 0; i < this.interestRate.size(); ++i) {
                this.interestRate.setElementAt(Float.parseFloat(interestNode.getText()), i);
            }
        }
        else {
            final Iterator interestNodes = interestNode.getChildren("PeriodRate").iterator();
            int j = 0;
            while (interestNodes.hasNext()) {
                final Element currentNode = (Element) interestNodes.next();
                this.interestRate.setElementAt(Float.parseFloat(currentNode.getAttributeValue("Rate")), j);
                ++j;
            }
        }
        AnalysisToolsCore.logger.debug("List of periods created");
        Iterator periodsIter = periods.iterator();
        while (periodsIter.hasNext()) {
          Element period = (Element) periodsIter.next();
            final List deals = period.getChildren("Deal");
            AnalysisToolsCore.logger.debug("List of deals created");
            Iterator dealIter = deals.iterator();
            float time;
            float price;
            while (dealIter.hasNext()) {
              Element deal = (Element) dealIter.next();
                  time = Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0f + this.sessionDuration;
                  price = Float.parseFloat(deal.getChild("Deal").getAttributeValue("price"));
                ((XYSeries) this.datasetInstitution.get(String.valueOf(deal.getChild("Deal").getAttributeValue("institution")) + "price")).add(time, price);
            }
            this.sessionDuration += (long)this.periodDuration;
        }
        final List assets = experimentNode.getChild("Setup").getChildren("Asset");
        AnalysisToolsCore.logger.debug("List of Assets created");
        final Iterator assetsIterator = assets.iterator();
        final HashMap dividendForTheAsset = new HashMap();
        while (assetsIterator.hasNext()) {
            final Element asset = (Element) assetsIterator.next();
            final String assetName = asset.getAttributeValue("name");
            AnalysisToolsCore.logger.debug("New Asset" + assetName);
            final Element dividendModel = asset.getChild("DividendModel");
            int size = 0;
            if (dividendModel.getAttributeValue("size") != null) {
                size = Integer.parseInt(dividendModel.getAttributeValue("size"));
            }
            final List dividendMeans = dividendModel.getChildren("Dividend");
            AnalysisToolsCore.logger.debug("List of Dividend created");
            final Iterator dividendMeansIterator = dividendMeans.iterator();
            while (dividendMeansIterator.hasNext()) {
                final List dividend = new Vector();
                for (int k = 0; k < dividendMeans.size(); ++k) {
                    final Element dividendMean = (Element) dividendMeansIterator.next();
                    final String div = dividendMean.getAttributeValue("mean");
                    dividend.add(div);
                }
                dividendForTheAsset.put(assetName, dividend);
            }
            AnalysisToolsCore.logger.debug("end");
            this.datasetInstitution.put(String.valueOf(assetName) + "HV", new XYSeries("Holding value on the experiment"));
            this.datasetInstitution.put(String.valueOf(assetName) + "HVsize", new XYSeries("Holding value on the window"));
            for (int l = 0; l < periods.size(); ++l) {
                final List meanDividend = (List) dividendForTheAsset.get(assetName);
                ((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HV")).add(this.periodDuration * l, this.gethv(l, meanDividend));
                AnalysisToolsCore.logger.debug("gzrsggqrgrd");
                ((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HV")).add(this.periodDuration * (l + 1), this.gethv(l, meanDividend));
            }
            for (int l = 0; l < periods.size(); ++l) {
                final List meanDividend = (List) dividendForTheAsset.get(assetName);
                ((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HVsize")).add(this.periodDuration * l, this.gethvSize(l, size, meanDividend));
                ((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HVsize")).add(this.periodDuration * (l + 1), this.gethvSize(l, size, meanDividend));
            }
            AnalysisToolsCore.logger.debug("superend");
        }
        AnalysisToolsCore.logger.debug("supesuperrend");
    }
}
