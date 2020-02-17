package jessx.analysis.tools;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import jessx.utils.Utils;
import org.jdom.Document;
import org.jdom.Element;

public class TableModelPlayersClassification extends AbstractTableModel {
	private Vector vectorName = new Vector();

	private Vector vectorCash = new Vector();

	private HashMap assetValueAtTheEnd = new HashMap<Object, Object>();

	private int rowCount;

	public TableModelPlayersClassification(Document document, TableModelPlayersResults tableModelPlayersResults) {
		try {
			Utils.logger.debug("TableModelPlayersClassification");
			this.rowCount = tableModelPlayersResults.getRowCount();
			int columnCount = tableModelPlayersResults.getColumnCount();
			Element experimentNode = document.getRootElement();
			Utils.logger.debug("Asset");
			List assets = experimentNode.getChild("Setup").getChildren("Asset");
			Utils.logger.debug("PeriodNumber");
			int periodNumber = Integer.parseInt(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodNumber"));
			Iterator<Element> assetsIter = assets.iterator();
			Hashtable<Object, Object> assetAndColumnNumber = new Hashtable<Object, Object>();
			while (assetsIter.hasNext()) {
				Element asset = assetsIter.next();
				Utils.logger.debug("Dividend");
				List dividends = asset.getChild("DividendModel").getChildren("Dividend");
				Utils.logger.debug("name");
				String assetName = asset.getAttributeValue("name");
				this.assetValueAtTheEnd.put(assetName, assetValueAtTheEnd(dividends, periodNumber));
				int j = 3;
				while (assetName != tableModelPlayersResults.getColumnName(j) && j < columnCount)
					j += 2;
				Utils.logger.debug("assetAndColumnNumber.put(j,assetName)" + j);
				assetAndColumnNumber.put(j, assetName);
			}
			Utils.logger.debug("columnCount:" + columnCount);
			for (int i = 0; i < this.rowCount; i++) {
				Utils.logger.debug("i:" + i);
				this.vectorName.add(i, tableModelPlayersResults.getValueAt(i, 0));
				float cash = ((Float) tableModelPlayersResults.getValueAt(i, 1)).floatValue();
				int j = 3;
				while (j < columnCount) {
					Utils.logger.debug("j:" + j);
					cash += ((Float) tableModelPlayersResults.getValueAt(i, j)).floatValue() * ((Float) this.assetValueAtTheEnd.get(assetAndColumnNumber.get(j))).floatValue();
					j += 2;
				}
				this.vectorCash.add(i, new Float(cash));
			}
			sort();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sort() {
		Utils.logger.debug("sort");
		for (int i = 0; i < this.rowCount; i++) {
			int index = i;
			for (int j = i; j < this.rowCount; j++) {
				if (((Float) this.vectorCash.get(index)).floatValue() < ((Float) this.vectorCash.get(j)).floatValue())
					index = j;
			}
			Object tempCash = this.vectorCash.get(index);
			this.vectorCash.remove(index);
			this.vectorCash.add(i, tempCash);
			Object tempName = this.vectorName.get(index);
			this.vectorName.remove(index);
			this.vectorName.add(i, tempName);
		}
	}

	public Float assetValueAtTheEnd(List dividends, int periodNumber) {
		Utils.logger.debug("assetValueAtTheEnd");
		float assetValueAtTheEnd = 0.0F;
		List subDividends = dividends.subList(periodNumber, dividends.size());
		Iterator<Element> subDividendsIter = subDividends.iterator();
		while (subDividendsIter.hasNext()) {
			Element dividend = subDividendsIter.next();
			String divValue = dividend.getAttributeValue("mean");
			assetValueAtTheEnd += Float.parseFloat(divValue);
		}
		return new Float(assetValueAtTheEnd);
	}

	public int getRowCount() {
		return this.rowCount;
	}

	public int getColumnCount() {
		return 3;
	}

	public Class getColumnClass(int column) {
		if (column == 0)
			return String.class;
		if (column == 1)
			return String.class;
		return Float.class;
	}

	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Rank";
		case 1:
			return "Players' Name";
		case 2:
			return "Reassessed Cash ($)";
		}
		return "Cash ($)";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Utils.logger.debug("getValueAt: ( " + rowIndex + " ; " + columnIndex + " )");
		switch (columnIndex) {
		case 0:
			return Integer.toString(rowIndex + 1);
		case 1:
			return this.vectorName.get(rowIndex);
		}
		return this.vectorCash.get(rowIndex);
	}
}
