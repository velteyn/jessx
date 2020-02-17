// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils.gui;

import java.awt.Color;
import javax.swing.JLabel;
import jessx.utils.Constants;
import javax.swing.table.DefaultTableModel;

public class TStrippedLinesTableModel extends DefaultTableModel implements Constants
{
    public TStrippedLinesTableModel(final Object[] columnNames, final int rowCount) {
        super(columnNames, rowCount);
    }
    
    @Override
    public void addRow(Object[] rowData) {
        if (rowData != null) {
            for (int i = 0; i < rowData.length; ++i) {
                if (!(rowData[i] instanceof JLabel)) {
                    rowData[i] = this.convertToLabel(rowData[i], this.getRowCount());
                }
                else {
                    ((JLabel)rowData[i]).setBackground(this.getRowColor(this.getRowCount()));
                }
            }
        }
        else {
            rowData = new Object[this.getColumnCount()];
            for (int i = 0; i < rowData.length; ++i) {
                rowData[i] = new JLabel();
                ((JLabel)rowData[i]).setText("");
                ((JLabel)rowData[i]).setBackground(this.getRowColor(this.getRowCount()));
            }
        }
        super.addRow(rowData);
    }
    
    @Override
    public void setValueAt(Object object, final int row, final int col) {
        if (row == this.getRowCount()) {
            this.addRow((Object[])null);
        }
        if (object != null) {
            if (!(object instanceof JLabel)) {
                object = this.convertToLabel(object, row);
            }
        }
        else {
            object = new JLabel();
            ((JLabel)object).setBackground(this.getRowColor(this.getRowCount()));
        }
        super.setValueAt(object, row, col);
    }
    
    protected void setValueAtNoConversion(final Object object, final int row, final int col) {
        super.setValueAt(object, row, col);
    }
    
    protected Color getRowColor(final int row) {
        return (row % 2 == 0) ? TStrippedLinesTableModel.COLOR_EVEN_LINE : TStrippedLinesTableModel.COLOR_ODD_LINE;
    }
    
    private JLabel convertToLabel(final Object object, final int row) {
        final JLabel templabel = new JLabel(object.toString());
        templabel.setBackground(this.getRowColor(row));
        templabel.setFont(TStrippedLinesTableModel.FONT_DEFAULT_LABEL);
        return templabel;
    }
    
    @Override
    public boolean isCellEditable(final int row, final int col) {
        return false;
    }
}
