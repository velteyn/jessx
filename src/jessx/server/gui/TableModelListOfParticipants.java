// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.JButton;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableModelListOfParticipants extends AbstractTableModel
{
    private int rowCount;
    private int columnCount;
    private Vector listOfParticipants;
    
    public TableModelListOfParticipants() {
        this.rowCount = 0;
        this.columnCount = 3;
        this.listOfParticipants = new Vector();
    }
    
    public int getRowCount() {
        return this.rowCount;
    }
    
    public int getColumnCount() {
        return this.columnCount;
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return columnIndex == 2;
    }
    
    @Override
    public String getColumnName(final int column) {
        if (column == 0) {
            return "Name";
        }
        if (column == 1) {
            return "Password";
        }
        return "Delete";
    }
    
    @Override
    public Class getColumnClass(final int column) {
        if (column == 2) {
            return JButton.class;
        }
        return String.class;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return ((String[])this.listOfParticipants.get(rowIndex))[0];
        }
        if (columnIndex == 1) {
            return ((String[])this.listOfParticipants.get(rowIndex))[1];
        }
        final JButton deleteButton = new JButton();
        deleteButton.setText("Delete");
        return deleteButton;
    }
    
    public void addRow(final String name, final String password) {
        this.listOfParticipants.add(new String[] { name, password });
        this.fireTableRowsInserted(++this.rowCount, this.rowCount);
    }
    
    public void deleteRow(final int row) {
        this.listOfParticipants.remove(row);
        --this.rowCount;
        this.fireTableDataChanged();
    }
    
    public void removeAll() {
        this.rowCount = 0;
        this.listOfParticipants.clear();
        this.fireTableDataChanged();
    }
    
    public Vector getListParticipants() {
        return this.listOfParticipants;
    }
}
