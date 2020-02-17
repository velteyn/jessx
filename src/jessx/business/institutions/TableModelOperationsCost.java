// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import jessx.business.Institution;
import javax.swing.table.AbstractTableModel;

public class TableModelOperationsCost extends AbstractTableModel
{
    private Institution institution;
    private boolean cellEditablePossible;
    
    public TableModelOperationsCost(final Institution institution) {
        this.cellEditablePossible = true;
        this.institution = institution;
    }
    
    public int getRowCount() {
        return this.institution.getSupportedOperation().size();
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return this.cellEditablePossible && columnIndex != 0;
    }
    
    @Override
    public String getColumnName(final int column) {
        if (column == 0) {
            return "Operation";
        }
        return (column == 1) ? "Percentage" : "Minimal cost";
    }
    
    @Override
    public Class getColumnClass(final int column) {
        return (Class)((column == 0) ? String.class : Float.class);
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return this.institution.getSupportedOperation().elementAt(rowIndex);
        }
        if (columnIndex == 2) {
            return new Float(this.institution.getMinimalCost(this.institution.getSupportedOperation().elementAt(rowIndex).toString()));
        }
        return new Float(this.institution.getPercentageCost(this.institution.getSupportedOperation().elementAt(rowIndex).toString()));
    }
    
    public void setUneditable() {
        this.cellEditablePossible = false;
    }
    
    public void setEditable() {
        this.cellEditablePossible = true;
    }
    
    @Override
    public void setValueAt(final Object value, final int row, final int col) {
        if (col == 2) {
            this.institution.setMinimalCost(this.institution.getSupportedOperation().elementAt(row).toString(), (Float)value);
        }
        if (col == 1) {
            this.institution.setPercentageCost(this.institution.getSupportedOperation().elementAt(row).toString(), (Float)value);
        }
    }
}
