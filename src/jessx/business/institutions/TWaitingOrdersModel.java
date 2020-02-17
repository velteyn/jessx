// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import javax.swing.JButton;
import jessx.client.ClientCore;
import jessx.utils.Utils;
import jessx.business.operations.DeleteOrder;
import jessx.business.Operator;
import jessx.utils.gui.TStrippedLinesTableModel;

public class TWaitingOrdersModel extends TStrippedLinesTableModel
{
    private Operator operator;
    private boolean deleteOrderGranted;
    
    public TWaitingOrdersModel(final int ec, final Operator op) {
        super((Object[])null, ec);
        this.operator = op;
        this.deleteOrderGranted = this.operator.isGrantedTo(new DeleteOrder().getOperationName());
    }
    
    @Override
    public int getColumnCount() {
        return this.deleteOrderGranted ? 4 : 3;
    }
    
    @Override
    public String getColumnName(final int colIndex) {
        switch (colIndex) {
            case 0: {
                return "Side";
            }
            case 1: {
                return "Price ($)";
            }
            case 2: {
                return "Quantity";
            }
            case 3: {
                return "Delete";
            }
            default: {
                Utils.logger.error("waiting order table column index out of bound");
                return "";
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int row, final int col) {
        ClientCore.getExperimentManager();
        return 2 == ClientCore.getExperimentManager().getExperimentState() && col > 2;
    }
    
    @Override
    public void setValueAt(final Object object, final int row, final int col) {
        if (col != 3) {
            if (object == null) {
                this.removeRow(row);
            }
            else {
                super.setValueAt(object, row, col);
            }
        }
        else {
            ((JButton)object).setBackground(super.getRowColor(row));
            super.setValueAtNoConversion(object, row, col);
        }
    }
}
