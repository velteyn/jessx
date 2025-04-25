// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.institutions;

import jessx.business.event.OperatorEvent;
import jessx.business.Operator;
import jessx.utils.Utils;
import java.util.Collection;
import jessx.business.Institution;
import java.util.Vector;
import java.util.HashMap;
import jessx.business.event.OperatorListener;
import javax.swing.table.AbstractTableModel;

public class OrderMarketOperatorsTableModel extends AbstractTableModel implements OperatorListener
{
    private HashMap operators;
    private Vector allowedOperations;
    private Vector mainColumn;
    private boolean cellEditablePossible;
    
    public OrderMarketOperatorsTableModel(final Institution institution) {
        this.mainColumn = new Vector();
        this.cellEditablePossible = true;
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
    
    @Override
    public Class getColumnClass(final int col) {
        if (col == 0) {
            return String.class;
        }
        if (col == 1) {
            return Integer.class;
        }
        return Boolean.class;
    }
    
    @Override
    public String getColumnName(final int col) {
        switch (col) {
            case 0: {
                return "Operator Name";
            }
            case 1: {
                return "Visible Orderbook depth";
            }
            default: {
                return this.allowedOperations.elementAt(col - 2).toString();
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int row, final int col) {
        Utils.logger.debug("isCellEditable(" + row + "," + col + ")");
        return this.cellEditablePossible && col > 0;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Utils.logger.debug("Getting a value at (" + rowIndex + ", " + columnIndex + ")");
        if (columnIndex == 0) {
            Utils.logger.debug("value: " + this.mainColumn.elementAt(rowIndex).toString());
            return this.mainColumn.elementAt(rowIndex).toString();
        }
        final Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(rowIndex).toString());
        if (columnIndex == 1) {
            Utils.logger.debug("value: " + oper.getOrderBookVisibility());
            return new Integer(oper.getOrderBookVisibility());
        }
        Utils.logger.debug("value: " + oper.isGrantedTo(this.allowedOperations.elementAt(columnIndex - 2).toString()));
        return new Boolean(oper.isGrantedTo(this.allowedOperations.elementAt(columnIndex - 2).toString()));
    }
    
    @Override
    public void setValueAt(final Object object, final int row, final int col) {
        Utils.logger.debug("Setting a value: " + object + " at (" + row + ", " + col + ")");
        if (col > 1) {
            final Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
            if ((boolean) object) {
                oper.grant(this.getColumnName(col));
            }
            else {
                oper.deny(this.getColumnName(col));
            }
        }
        else if (col == 1) {
            final Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
            oper.setOrderbookVisibility((int)object);
        }
        else if (col == 0) {
            final Operator oper = (Operator) this.operators.get(this.mainColumn.elementAt(row).toString());
            this.mainColumn.setElementAt(object.toString(), row);
            oper.setName(object.toString());
        }
        this.fireTableCellUpdated(row, col);
    }
    
    public void operatorsModified(final OperatorEvent e) {
        if (e.getEvent() == 1) {
            this.addRow(e.getOperatorName());
        }
        else if (e.getEvent() == 0) {
            this.removeRow(e.getOperatorName());
        }
    }
    
    private void addRow(final String operName) {
        this.mainColumn.add(operName);
        this.fireTableRowsInserted(this.mainColumn.size() - 1, this.mainColumn.size() - 1);
    }
    
    private void removeRow(final String operName) {
        final int row = this.mainColumn.indexOf(operName);
        this.mainColumn.remove(operName);
        this.fireTableRowsDeleted(row, row);
    }
    
    public void setUneditable() {
        this.cellEditablePossible = false;
    }
    
    public void setEditable() {
        this.cellEditablePossible = true;
    }
}
