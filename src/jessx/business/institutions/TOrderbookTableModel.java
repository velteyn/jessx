// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.institutions;

import javax.swing.table.DefaultTableModel;

public class TOrderbookTableModel extends DefaultTableModel
{
    public TOrderbookTableModel(final Object[] columnNames, final int rowCount) {
        super(columnNames, rowCount);
    }
    
    @Override
    public boolean isCellEditable(final int row, final int column) {
        return false;
    }
}
