package jessx.analysis.tools;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import jessx.analysis.AnalysisTool;
import jessx.analysis.AnalysisToolCreator;
import jessx.analysis.AnalysisToolsCore;
import jessx.utils.Utils;
import org.jdom.Document;
import org.jdom.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class HoldingValue extends JPanel implements AnalysisTool {
	private Document doc;

	private long sessionDuration = 0L;

	private float periodDuration;

	private Vector<Float> interestRate = new Vector<Float>();

	private HashMap datasetInstitution = new HashMap<Object, Object>();

	private List institutions;

	public String getToolName() {
		return "Holding Value";
	}

	public String getToolAuthor() {
		return "EC-Lille, USTL - 2005 - Mohamed Hamamouchi.";
	}

	public String getToolDescription() {
		return "An analysis that plots price evolution on the market and the holding value.";
	}

	public void setDocument(Document xmlDoc) {
		this.doc = xmlDoc;
	}

	public float gethv(int nPeriod, List<String> meanDividend) {
		AnalysisToolsCore.logger.debug("gethv" + nPeriod);
		float hv = 0.0F;
		for (int i = nPeriod; i < meanDividend.size(); i++) {
			int interestRateIndex;
			AnalysisToolsCore.logger.debug(meanDividend.get(i));
			float div = Float.parseFloat(meanDividend.get(i));
			AnalysisToolsCore.logger.debug(div);
			if (i >= this.interestRate.size()) {
				interestRateIndex = this.interestRate.size() - 1;
			} else {
				interestRateIndex = i;
			}
			hv = hv * (1.0F + ((Float) this.interestRate.elementAt(interestRateIndex)).floatValue() / 100.0F) + div;
			Utils.logger.error(" (gethv) " + interestRateIndex);
		}
		hv *= 100.0F;
		hv = (int) (hv + 0.5D);
		hv /= 100.0F;
		AnalysisToolsCore.logger.debug("gethv" + nPeriod);
		return hv;
	}

	public float gethvSize(int nPeriod, int size, List<String> meanDividend) {
		AnalysisToolsCore.logger.debug("gethvSize" + nPeriod);
		float hv = 0.0F;
		for (int i = nPeriod; i < nPeriod + size; i++) {
			int interestRateIndex;
			AnalysisToolsCore.logger.debug(meanDividend.get(i));
			float div = Float.parseFloat(meanDividend.get(i));
			AnalysisToolsCore.logger.debug(div);
			if (i >= this.interestRate.size()) {
				interestRateIndex = this.interestRate.size() - 1;
			} else {
				interestRateIndex = i;
			}
			hv = hv * (1.0F + ((Float) this.interestRate.elementAt(interestRateIndex)).floatValue() / 100.0F) + div;
			Utils.logger.error(" " + interestRateIndex);
		}
		hv *= 100.0F;
		hv = (int) (hv + 0.5D);
		hv /= 100.0F;
		AnalysisToolsCore.logger.debug("gethvSize" + nPeriod);
		return hv;
	}

	public JPanel drawGraph() {
		System.out.print("drawGraphHolding");
		JPanel returnPanel = new JPanel();
		System.out.print("drawGraph1");
		initDataSet();
		System.out.print("drawGraph2");
		Iterator<Element> institutionsIter = this.institutions.iterator();
		JTabbedPane tempTabbedPane = new JTabbedPane();
		while (institutionsIter.hasNext()) {
			Element instit = institutionsIter.next();
			String institutionName = instit.getAttributeValue("name");
			XYSeriesCollection dataset = new XYSeriesCollection();
			dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(institutionName) + "price"));
			dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(instit.getAttributeValue("quotedAsset")) + "HV"));
			dataset.addSeries((XYSeries) this.datasetInstitution.get(String.valueOf(instit.getAttributeValue("quotedAsset")) + "HVsize"));
			ChartPanel chartPanel = new ChartPanel(CreateChart(dataset));
			tempTabbedPane.add((Component) chartPanel, institutionName);
		}
		GridBagLayout gridBagLayout1 = new GridBagLayout();
		returnPanel.setLayout(gridBagLayout1);
		returnPanel.add(tempTabbedPane, new GridBagConstraints(1, 1, 1, 1, 1.0D, 1.0D, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
		return returnPanel;
	}

	private JFreeChart CreateChart(XYSeriesCollection dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart("Holding Value", "Time (s)", "Price ($)", (XYDataset) dataset, PlotOrientation.VERTICAL, true, true, false);
		return chart;
	}

	private void initDataSet() {
		AnalysisToolsCore.logger.debug("Open XML");
		Element experimentNode = this.doc.getRootElement();
		this.periodDuration = Float.parseFloat(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodDuration"));
		this.institutions = experimentNode.getChild("Setup").getChildren("Institution");
		Iterator<Element> institutionsIter = this.institutions.iterator();
		while (institutionsIter.hasNext()) {
			Element institution = institutionsIter.next();
			String InstitutionName = institution.getAttributeValue("name");
			this.datasetInstitution.put(String.valueOf(InstitutionName) + "price", new XYSeries("Price"));
			AnalysisToolsCore.logger.debug("New Institution" + InstitutionName);
		}
		List periods = experimentNode.getChildren("Period");
		Element interestNode = experimentNode.getChild("Setup").getChild("GeneralParameters").getChild("InterestRate");
		String periodCount = interestNode.getAttributeValue("periodCount");
		this.interestRate.setSize(periods.size());
		if (periodCount == null) {
			Utils.logger.error("invalid xml, assuming it is an older version");
			for (int i = 0; i < this.interestRate.size(); i++)
				this.interestRate.setElementAt(Float.valueOf(Float.parseFloat(interestNode.getText())), i);
		} else {
			Iterator<Element> interestNodes = interestNode.getChildren("PeriodRate").iterator();
			int i = 0;
			while (interestNodes.hasNext()) {
				Element currentNode = interestNodes.next();
				this.interestRate.setElementAt(Float.valueOf(Float.parseFloat(currentNode.getAttributeValue("Rate"))), i);
				i++;
			}
		}
		AnalysisToolsCore.logger.debug("List of periods created");
		Iterator<Element> periodsIter = periods.iterator();
		while (periodsIter.hasNext()) {
			Element period = periodsIter.next();
			List deals = period.getChildren("Deal");
			AnalysisToolsCore.logger.debug("List of deals created");
			Iterator<Element> dealIter = deals.iterator();
			while (dealIter.hasNext()) {
				Element deal = dealIter.next();
				float time = Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0F + (float) this.sessionDuration;
				float price = Float.parseFloat(deal.getChild("Deal").getAttributeValue("price"));
				((XYSeries) this.datasetInstitution.get(String.valueOf(deal.getChild("Deal").getAttributeValue("institution")) + "price")).add(time, price);
			}
			this.sessionDuration = (long) ((float) this.sessionDuration + this.periodDuration);
		}
		List assets = experimentNode.getChild("Setup").getChildren("Asset");
		AnalysisToolsCore.logger.debug("List of Assets created");
		Iterator<Element> assetsIterator = assets.iterator();
		HashMap<Object, Object> dividendForTheAsset = new HashMap<Object, Object>();
		while (assetsIterator.hasNext()) {
			Element asset = assetsIterator.next();
			String assetName = asset.getAttributeValue("name");
			AnalysisToolsCore.logger.debug("New Asset" + assetName);
			Element dividendModel = asset.getChild("DividendModel");
			int size = 0;
			if (dividendModel.getAttributeValue("size") != null)
				size = Integer.parseInt(dividendModel.getAttributeValue("size"));
			List dividendMeans = dividendModel.getChildren("Dividend");
			AnalysisToolsCore.logger.debug("List of Dividend created");
			Iterator<Element> dividendMeansIterator = dividendMeans.iterator();
			while (dividendMeansIterator.hasNext()) {
				List<String> dividend = new Vector();
				for (int i = 0; i < dividendMeans.size(); i++) {
					Element dividendMean = dividendMeansIterator.next();
					String div = dividendMean.getAttributeValue("mean");
					dividend.add(div);
				}
				dividendForTheAsset.put(assetName, dividend);
			}
			AnalysisToolsCore.logger.debug("end");
			this.datasetInstitution.put(String.valueOf(assetName) + "HV", new XYSeries("Holding value on the experiment"));
			this.datasetInstitution.put(String.valueOf(assetName) + "HVsize", new XYSeries("Holding value on the window"));
			int j;
			for (j = 0; j < periods.size(); j++) {
				List meanDividend = (List) dividendForTheAsset.get(assetName);
				((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HV")).add((this.periodDuration * j), gethv(j, meanDividend));
				AnalysisToolsCore.logger.debug("gzrsggqrgrd");
				((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HV")).add((this.periodDuration * (j + 1)), gethv(j, meanDividend));
			}
			for (j = 0; j < periods.size(); j++) {
				List meanDividend = (List) dividendForTheAsset.get(assetName);
				((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HVsize")).add((this.periodDuration * j), gethvSize(j, size, meanDividend));
				((XYSeries) this.datasetInstitution.get(String.valueOf(assetName) + "HVsize")).add((this.periodDuration * (j + 1)), gethvSize(j, size, meanDividend));
			}
			AnalysisToolsCore.logger.debug("superend");
		}
		AnalysisToolsCore.logger.debug("supesuperrend");
	}

	static {
		try {
			AnalysisToolCreator.analyseFactories.put("HoldingValue", Class.forName("jessx.analysis.tools.HoldingValue"));
			System.out.println("Holding Value registered");
		} catch (Exception ex) {
			System.out.println("Problem registering HoldingValue tool: " + ex.toString());
		}
	}
}
