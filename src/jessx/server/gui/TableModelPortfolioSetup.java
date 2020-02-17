package jessx.server.gui;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import jessx.business.BusinessCore;
import jessx.business.Portfolio;
import jessx.business.event.AssetEvent;
import jessx.business.event.AssetListener;
import jessx.business.event.PortfolioEvent;
import jessx.business.event.PortfolioListener;

public class TableModelPortfolioSetup extends AbstractTableModel implements PortfolioListener, AssetListener {
	Portfolio portfolio;

	Vector mainColumn = new Vector();

	private boolean cellEditable = true;

	public TableModelPortfolioSetup(Portfolio p) {
		this.portfolio = p;
		float cash = this.portfolio.getCash();
		this.mainColumn.add("Cash");
		this.portfolio.setCash(Math.round(cash));
		this.portfolio.addListener(this);
		BusinessCore.addAssetListener(this);
		Iterator<String> assetIter = BusinessCore.getAssets().keySet().iterator();
		while (assetIter.hasNext()) {
			String key = assetIter.next();
			if (this.portfolio.getOwnings().containsKey(key)) {
				this.mainColumn.add(key);
				continue;
			}
			addRow(key);
		}
	}

	public int getRowCount() {
		return this.mainColumn.size();
	}

	public int getColumnCount() {
		return 2;
	}

	public Class getColumnClass(int column) {
		return (column == 0) ? String.class : Integer.class;
	}

	public String getColumnName(int column) {
		return (column == 0) ? "Assets" : "Quantity";
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (this.cellEditable)
			return (columnIndex == 1);
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0)
			return this.mainColumn.elementAt(rowIndex);
		if (rowIndex != 0)
			return new Integer(this.portfolio.getOwnings(this.mainColumn.elementAt(rowIndex).toString()));
		return new Integer(Math.round(this.portfolio.getCash()));
	}

	public void setValueAt(Object object, int row, int col) {
		if (col == 1)
			if (row == 0) {
				this.portfolio.setCash(((Integer) object).intValue());
				this.portfolio.setNonInvestedCash(((Integer) object).intValue());
			} else {
				this.portfolio.setOwnings(this.mainColumn.elementAt(row).toString(), ((Integer) object).intValue());
				this.portfolio.setNonInvestedOwnings(this.mainColumn.elementAt(row).toString(), ((Integer) object).intValue());
			}
	}

	public void portfolioModified(PortfolioEvent e) {
		if (e.getEvent() == 1) {
			fireTableCellUpdated(this.mainColumn.indexOf(e.getAssetUpdated()), 1);
		} else if (e.getEvent() == 0) {
			fireTableCellUpdated(0, 1);
		} else if (e.getEvent() == 2) {
			if (!this.mainColumn.contains(e.getAssetUpdated()))
				if (this.mainColumn.contains(e.getAssetUpdated())) {
					fireTableCellUpdated(this.mainColumn.indexOf(e.getAssetUpdated()), 1);
				} else {
					this.mainColumn.add(e.getAssetUpdated());
					fireTableRowsInserted(this.mainColumn.size() - 1, this.mainColumn.size() - 1);
				}
		} else if (e.getEvent() == 3 && this.mainColumn.contains(e.getAssetUpdated())) {
			int row = this.mainColumn.indexOf(e.getAssetUpdated());
			this.mainColumn.remove(e.getAssetUpdated());
			fireTableRowsDeleted(row, row);
		}
	}

	public void assetsModified(AssetEvent e) {
		if (e.getEvent() == 1) {
			addRow(e.getAssetName());
		} else if (e.getEvent() == 0) {
			removeRow(e.getAssetName());
		}
	}

	private void addRow(String assetName) {
		addRow(assetName, 0);
	}

	private void addRow(String assetName, int qtty) {
		this.portfolio.setOwnings(assetName, qtty);
	}

	private void removeRow(String assetName) {
		this.portfolio.removeAssetFromOwnings(assetName);
	}

	public void setCellEditable() {
		this.cellEditable = true;
	}

	public void setCellUneditable() {
		this.cellEditable = false;
	}
}
