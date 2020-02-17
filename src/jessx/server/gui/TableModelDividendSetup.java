// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.business.event.DividendEvent;
import jessx.utils.Utils;
import jessx.business.NormalDividend;
import jessx.business.Dividend;
import jessx.business.DividendModel;
import jessx.business.event.DividendListener;
import javax.swing.table.AbstractTableModel;

public class TableModelDividendSetup extends AbstractTableModel implements DividendListener
{
    private Class dividendDistribution;
    private DividendModel dividendModel;
    private boolean cellEditable;
    
    public TableModelDividendSetup() {
        this.cellEditable = true;
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public TableModelDividendSetup(final Class dividend, final DividendModel divModel) {
        this.cellEditable = true;
        if (dividend.getSuperclass().toString().equalsIgnoreCase(Dividend.class.toString())) {
            this.dividendDistribution = dividend;
        }
        else {
            this.dividendDistribution = NormalDividend.class;
        }
        (this.dividendModel = divModel).addDividendListener(this);
    }
    
    public void setPeriodCount(final int i) {
        this.dividendModel.setPeriodCount(i);
    }
    
    public int getRowCount() {
        return this.dividendModel.getDefinedPeriodCount();
    }
    
    public int getColumnCount() {
        return this.createDividend().getParamCount() + 1;
    }
    
    @Override
    public Class getColumnClass(final int column) {
        return (column == 0) ? Integer.class : this.createDividend().getParamClass(column - 1);
    }
    
    @Override
    public String getColumnName(final int column) {
        return (column == 0) ? "Period" : this.createDividend().getParamNames()[column - 1];
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return new Integer(rowIndex + 1);
        }
        return this.dividendModel.getDividendAt(rowIndex).getParameter(columnIndex - 1);
    }
    
    @Override
    public void setValueAt(final Object value, final int row, final int col) {
        Utils.logger.error("setValueAt");
        this.dividendModel.getDividendAt(row).setParameter(col - 1, value);
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return this.cellEditable && columnIndex > 0;
    }
    
    public void dividendModified(final DividendEvent e) {
        if (e.getEvent() == 0) {
            this.fireTableRowsUpdated(e.getPeriod(), e.getPeriod());
        }
    }
    
    private Dividend createDividend() {
        try {
            final Dividend div = (Dividend) this.dividendDistribution.newInstance();
            return div;
        }
        catch (Exception ex) {
            Utils.logger.error("Failed to create dividend object from the class given to the constructor.");
            Utils.logger.error("error: " + ex.toString());
            Utils.logger.error("Will return a NormalDividend instead of a " + this.dividendDistribution.toString());
            return new NormalDividend();
        }
    }
    
    public void setCellEditable() {
        this.cellEditable = true;
    }
    
    public void setCellUneditable() {
        this.cellEditable = false;
    }
    
    private void jbInit() throws Exception {
    }
}
