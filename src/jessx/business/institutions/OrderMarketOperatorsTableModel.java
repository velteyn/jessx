package jessx.business.institutions;

import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;
import jessx.business.Institution;
import jessx.business.Operator;
import jessx.business.event.OperatorEvent;
import jessx.business.event.OperatorListener;
import jessx.utils.Utils;

public class OrderMarketOperatorsTableModel extends AbstractTableModel implements OperatorListener {
	private HashMap operators;

	private Vector allowedOperations;

	private Vector mainColumn = new Vector();

	private boolean cellEditablePossible = true;

	public OrderMarketOperatorsTableModel(Institution institution) {
		this.operators = institution.getOperators();
		this.mainColumn = new Vector(this.operators.keySet());
		this.allowedOperations = institution.getSupportedOperation();
		institution.addNewOperatorListener(this);
	}

	public int getRowCount() {
		return this.mainColumn.size();
	}

	public int getColumnCount() {
		return this.allowedOperations.size() + 2;
	}

	public Class getColumnClass(int col) {
		if (col == 0)
			return String.class;
		if (col == 1)
			return Integer.class;
		return Boolean.class;
	}

	public String getColumnName(int col) {
		switch (col) {
		case 0:
			return "Operator Name";
		case 1:
			return "Visible Orderbook depth";
		}
		return this.allowedOperations.elementAt(col - 2).toString();
	}

	public boolean isCellEditable(int row, int col) {
		Utils.logger.debug("isCellEditable(" + row + "," + col + ")");
		if (this.cellEditablePossible)
			return (col > 0);
		return false;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Utils.logger.debug("Getting a value at (" + rowIndex + ", " + columnIndex + ")");
		if (columnIndex == 0) {
			Utils.logger.debug("value: " + this.mainColumn.elementAt(rowIndex).toString());
			return this.mainColumn.elementAt(rowIndex).toString();
		}
		Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(rowIndex).toString());
		if (columnIndex == 1) {
			Utils.logger.debug("value: " + oper.getOrderBookVisibility());
			return new Integer(oper.getOrderBookVisibility());
		}
		Utils.logger.debug("value: " + oper.isGrantedTo(this.allowedOperations.elementAt(columnIndex - 2).toString()));
		return new Boolean(oper.isGrantedTo(this.allowedOperations.elementAt(columnIndex - 2).toString()));
	}

	public void setValueAt(Object object, int row, int col) {
		Utils.logger.debug("Setting a value: " + object + " at (" + row + ", " + col + ")");
		if (col > 1) {
			Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
			if (((Boolean) object).booleanValue()) {
				oper.grant(getColumnName(col));
			} else {
				oper.deny(getColumnName(col));
			}
		} else if (col == 1) {
			Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
			oper.setOrderbookVisibility(((Integer) object).intValue());
		} else if (col == 0) {
			Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
			this.mainColumn.setElementAt(object.toString(), row);
			oper.setName(object.toString());
		}
		fireTableCellUpdated(row, col);
	}

	public void operatorsModified(OperatorEvent e) {
		if (e.getEvent() == 1) {
			addRow(e.getOperatorName());
		} else if (e.getEvent() == 0) {
			removeRow(e.getOperatorName());
		}
	}

	private void addRow(String operName) {
		this.mainColumn.add(operName);
		fireTableRowsInserted(this.mainColumn.size() - 1, this.mainColumn.size() - 1);
	}

	private void removeRow(String operName) {
		int row = this.mainColumn.indexOf(operName);
		this.mainColumn.remove(operName);
		fireTableRowsDeleted(row, row);
	}

	public void setUneditable() {
		this.cellEditablePossible = false;
	}

	public void setEditable() {
		this.cellEditablePossible = true;
	}
}
