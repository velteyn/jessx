// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class JessXTableModel extends DefaultTableModel
{
    private Vector columnEditionAllowed;
    
    public JessXTableModel(final Vector columnName, final int rows) {
        this.columnEditionAllowed = new Vector();
        this.setColumnIdentifiers(columnName);
        this.setNumRows(rows);
        for (int i = 0; i < columnName.size(); ++i) {
            this.columnEditionAllowed.add(new Boolean(false));
        }
    }
    
    public JessXTableModel(final Vector columnName, final int rows, final Boolean[] columnSelectionAllowed) {
        this.columnEditionAllowed = new Vector();
        this.setColumnIdentifiers(columnName);
        this.setNumRows(rows);
        this.columnEditionAllowed = new Vector();
        this.columnEditionAllowed = DefaultTableModel.convertToVector(columnSelectionAllowed);
    }
    
    public JessXTableModel(final Object[] columnName, final int rows) {
        this.columnEditionAllowed = new Vector();
        this.setColumnIdentifiers(columnName);
        this.setNumRows(rows);
        for (int i = 0; i < columnName.length; ++i) {
            this.columnEditionAllowed.add(new Boolean(false));
        }
    }
    
    public JessXTableModel(final Object[] columnName, final int rows, final Boolean[] columnSelectionAllowed) {
        this.columnEditionAllowed = new Vector();
        this.setColumnIdentifiers(columnName);
        this.setNumRows(rows);
        this.columnEditionAllowed = DefaultTableModel.convertToVector(columnSelectionAllowed);
    }
    
    @Override
    public Class getColumnClass(final int col) {
        if (this.getRowCount() > 0) {
            return this.getValueAt(0, col).getClass();
        }
        return "".getClass();
    }
    
    @Override
    public boolean isCellEditable(final int row, final int col) {
        return (boolean) this.columnEditionAllowed.elementAt(col);
    }
    
    @Override
    public void addColumn(final Object columnName, final Object[] columnData) {
        this.addColumn(columnName, columnData, false);
    }
    
    @Override
    public void addColumn(final Object columnName, final Vector columnData) {
        this.addColumn(columnName, columnData, false);
    }
    
    @Override
    public void addColumn(final Object columnName) {
        this.addColumn(columnName, false);
    }
    
    public void addColumn(final Object columnName, final Object[] columnData, final boolean selectionAllowed) {
        super.addColumn(columnName, columnData);
        this.columnEditionAllowed.add(new Boolean(selectionAllowed));
    }
    
    public void addColumn(final Object columnName, final Vector columnData, final boolean selectionAllowed) {
        super.addColumn(columnName, columnData);
        this.columnEditionAllowed.add(new Boolean(selectionAllowed));
    }
    
    public void addColumn(final Object columnName, final boolean selectionAllowed) {
        super.addColumn(columnName);
        this.columnEditionAllowed.add(new Boolean(selectionAllowed));
    }
    
    public void setEditable(final int col, final boolean editable) {
        this.columnEditionAllowed.setElementAt(new Boolean(editable), col);
    }
}
