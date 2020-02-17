// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.business.BusinessCore;
import javax.swing.JButton;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableModelMessages extends AbstractTableModel
{
    private int rowCount;
    private int columnCount;
    private Vector listMessages;
    boolean cellsEditableIsPossible;
    JPanel messagesServerGenericGui;
    
    public TableModelMessages(final int columnCount, final JPanel panel) {
        this.rowCount = 0;
        this.listMessages = new Vector();
        this.cellsEditableIsPossible = true;
        this.columnCount = columnCount;
        this.messagesServerGenericGui = panel;
    }
    
    public int getRowCount() {
        return this.rowCount;
    }
    
    public int getColumnCount() {
        return this.columnCount;
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return this.cellsEditableIsPossible;
    }
    
    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0: {
                return "Subject";
            }
            case 1: {
                return "Receivers";
            }
            case 2: {
                return "Period n°";
            }
            case 3: {
                return "Time(s)";
            }
            default: {
                return "Delete";
            }
        }
    }
    
    @Override
    public Class getColumnClass(final int column) {
        switch (column) {
            case 1: {
                return JComboBox.class;
            }
            case 2: {
                return JSpinner.class;
            }
            case 3: {
                return JSpinner.class;
            }
            case 4: {
                return JButton.class;
            }
            default: {
                return String.class;
            }
        }
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex != 4) {
            return ((String[])this.listMessages.get(rowIndex))[columnIndex];
        }
        final JButton deleteButton = new JButton();
        deleteButton.setText("Delete");
        return deleteButton;
    }
    
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        switch (columnIndex) {
            case 0: {
                final String[] message = { aValue.toString(), ((String[])this.listMessages.get(rowIndex))[1], ((String[])this.listMessages.get(rowIndex))[2], ((String[])this.listMessages.get(rowIndex))[3] };
                BusinessCore.getScenario().changeInformation(rowIndex, message);
                this.listMessages.set(rowIndex, message);
                this.fireTableDataChanged();
                break;
            }
            case 1: {
                final String[] message = { ((String[])this.listMessages.get(rowIndex))[0], aValue.toString(), ((String[])this.listMessages.get(rowIndex))[2], ((String[])this.listMessages.get(rowIndex))[3] };
                if (((MessagesServerGenericGui)this.messagesServerGenericGui).checkMessage(message, rowIndex)) {
                    BusinessCore.getScenario().changeInformation(rowIndex, message);
                    this.listMessages.set(rowIndex, message);
                    this.fireTableDataChanged();
                    break;
                }
                break;
            }
            case 2: {
                final String[] message = { ((String[])this.listMessages.get(rowIndex))[0], ((String[])this.listMessages.get(rowIndex))[1], ((JSpinner)aValue).getValue().toString(), ((String[])this.listMessages.get(rowIndex))[3] };
                this.fireTableDataChanged();
                if (((MessagesServerGenericGui)this.messagesServerGenericGui).checkMessage(message, rowIndex)) {
                    BusinessCore.getScenario().changeInformation(rowIndex, message);
                    this.listMessages.set(rowIndex, message);
                    break;
                }
                break;
            }
            case 3: {
                final String[] message = { ((String[])this.listMessages.get(rowIndex))[0], ((String[])this.listMessages.get(rowIndex))[1], ((String[])this.listMessages.get(rowIndex))[2], ((JSpinner)aValue).getValue().toString() };
                this.fireTableDataChanged();
                if (((MessagesServerGenericGui)this.messagesServerGenericGui).checkMessage(message, rowIndex)) {
                    BusinessCore.getScenario().changeInformation(rowIndex, message);
                    this.listMessages.set(rowIndex, message);
                    break;
                }
                break;
            }
        }
    }
    
    public void addRow(final String[] message) {
        this.listMessages.add(message);
        this.fireTableRowsInserted(++this.rowCount, this.rowCount);
    }
    
    public void deleteRow(final int row) {
        this.listMessages.remove(row);
        --this.rowCount;
        this.fireTableDataChanged();
    }
    
    public void setEditableCellsPossible(final boolean state) {
        this.cellsEditableIsPossible = state;
    }
    
    public void removeAll() {
        this.listMessages.clear();
        this.rowCount = 0;
        this.fireTableDataChanged();
    }
    
    public void deletePlayerType(final String playerType) {
        for (int i = 0; i < this.rowCount; ++i) {
            if (((String[])this.listMessages.get(i))[1].equals(playerType)) {
                this.deleteRow(i);
            }
        }
    }
    
    public Vector getListMessages() {
        return BusinessCore.getScenario().getListInformation();
    }
}
