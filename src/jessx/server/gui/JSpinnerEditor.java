// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import jessx.business.BusinessCore;
import javax.swing.JSpinner;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;

class JSpinnerEditor extends AbstractCellEditor implements TableCellEditor
{
    JSpinner jspinner;
    
    public JSpinnerEditor(final String type) {
        this.jspinner = new JSpinner();
        if (type.equals("time")) {
            this.jspinner.setModel(new SpinnerNumberModel(1, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
        }
        else {
            this.jspinner.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
        }
    }
    
    public Object getCellEditorValue() {
        return this.jspinner;
    }
    
    public Component getTableCellEditorComponent(final JTable table, final Object jbutton, final boolean isSelected, final int row, final int column) {
        if (column == 2) {
            this.jspinner.setModel(new SpinnerNumberModel(1, 1, BusinessCore.getGeneralParameters().getPeriodCount(), 1));
            this.jspinner.setValue(Integer.valueOf((String)table.getValueAt(row, column)));
        }
        if (column == 3) {
            this.jspinner.setModel(new SpinnerNumberModel(1, 0, BusinessCore.getGeneralParameters().getPeriodDuration(), 1));
            this.jspinner.setValue(Integer.valueOf((String)table.getValueAt(row, column)));
        }
        return this.jspinner;
    }
}
