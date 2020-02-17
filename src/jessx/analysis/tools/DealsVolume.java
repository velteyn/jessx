package jessx.analysis.tools;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import jessx.analysis.AnalysisTool;
import jessx.analysis.AnalysisToolCreator;
import jessx.analysis.AnalysisToolsCore;
import org.jdom.Document;
import org.jdom.Element;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class DealsVolume extends JPanel implements AnalysisTool {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7000960404198757831L;

	private Document doc;

	private XYSeriesCollection dataset;

	private HashMap dealsXYSeries = new HashMap<Object, Object>();

	public String getToolName() {
		return "Deals volume analysis";
	}

	public String getToolAuthor() {
		return "EC-Lille, USTL - 2005 - Mohamed Amine Hamamouchi, Christophe Grosjean.";
	}

	public String getToolDescription() {
		return "An analysis that plots exchanged volume on the market.";
	}

	public void setDocument(Document xmlDoc) {
		this.doc = xmlDoc;
	}

	public JPanel drawGraph() {
		initDataSet();
		JFreeChart chart = ChartFactory.createXYLineChart("Exchanged Volume", "Time (s)", "Volume (quantity of assets)", (XYDataset) this.dataset, PlotOrientation.VERTICAL, true, true, false);
		return (JPanel) new ChartPanel(chart);
	}

	private void initDataSet() {
		AnalysisToolsCore.logger.debug("Open XML");
		Element experimentNode = this.doc.getRootElement();
		float duration = Float.parseFloat(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodDuration"));
		this.dataset = new XYSeriesCollection();
		List institutions = experimentNode.getChild("Setup").getChildren("Institution");
		Iterator<Element> institutionsIter = institutions.iterator();
		while (institutionsIter.hasNext()) {
			AnalysisToolsCore.logger.debug("New Institution");
			Element institution = institutionsIter.next();
			String InstitutionName = institution.getAttributeValue("name");
			this.dealsXYSeries.put(InstitutionName, new XYSeries(InstitutionName));
			this.dataset.addSeries((XYSeries) this.dealsXYSeries.get(InstitutionName));
			((XYSeries) this.dealsXYSeries.get(InstitutionName)).add(0.0D, 0.0D);
			AnalysisToolsCore.logger.debug(InstitutionName);
		}
		long periodDuration = 0L;
		List periods = experimentNode.getChildren("Period");
		Iterator<Element> periodsIter = periods.iterator();
		float volumexchange = 0.0F;
		while (periodsIter.hasNext()) {
			Element period = periodsIter.next();
			List deals = period.getChildren("Deal");
			Iterator<Element> dealIter = deals.iterator();
			while (dealIter.hasNext()) {
				Element deal = dealIter.next();
				volumexchange = Float.parseFloat(deal.getChild("Deal").getAttributeValue("quantity"));
				((XYSeries) this.dealsXYSeries.get(deal.getChild("Deal").getAttributeValue("institution"))).add((Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0F + (float) periodDuration), 0.0D);
				((XYSeries) this.dealsXYSeries.get(deal.getChild("Deal").getAttributeValue("institution"))).add((Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0F + (float) periodDuration), volumexchange);
				((XYSeries) this.dealsXYSeries.get(deal.getChild("Deal").getAttributeValue("institution"))).add((Float.parseFloat(deal.getChild("Deal").getAttributeValue("timestamp")) / 1000.0F + (float) periodDuration), 0.0D);
			}
			periodDuration = (long) ((float) periodDuration + duration);
		}
	}

	static {
		try {
			AnalysisToolCreator.analyseFactories.put("DealsVolume", Class.forName("jessx.analysis.tools.DealsVolume"));
			System.out.println("Deals Volume Analysis registered");
		} catch (Exception ex) {
			System.out.println("Problem registering DealsVolume tool: " + ex.toString());
		}
	}
}
