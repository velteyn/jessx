// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.business.event.AssetEvent;
import jessx.business.event.PortfolioEvent;
import java.util.Iterator;
import jessx.business.BusinessCore;
import java.util.Vector;
import jessx.business.Portfolio;
import jessx.business.event.AssetListener;
import jessx.business.event.PortfolioListener;
import javax.swing.table.AbstractTableModel;

public class TableModelPortfolioSetup extends AbstractTableModel implements PortfolioListener, AssetListener
{
    Portfolio portfolio;
    Vector mainColumn;
    private boolean cellEditable;
    
    public TableModelPortfolioSetup(final Portfolio p) {
        this.mainColumn = new Vector();
        this.cellEditable = true;
        this.portfolio = p;
        final float cash = this.portfolio.getCash();
        this.mainColumn.add("Cash");
        this.portfolio.setCash((float)Math.round(cash));
        this.portfolio.addListener(this);
        BusinessCore.addAssetListener(this);
        Iterator assetIter = BusinessCore.getAssets().keySet().iterator();
        while (assetIter.hasNext()) {
          String key = (String) assetIter.next();
            if (this.portfolio.getOwnings().containsKey(key)) {
                this.mainColumn.add(key);
            }
            else {
                this.addRow(key);
            }
        }
    }
    
    public int getRowCount() {
        return this.mainColumn.size();
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    @Override
    public Class getColumnClass(final int column) {
        return (Class)((column == 0) ? String.class : Integer.class);
    }
    
    @Override
    public String getColumnName(final int column) {
        return (column == 0) ? "Assets" : "Quantity";
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return this.cellEditable && columnIndex == 1;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return this.mainColumn.elementAt(rowIndex);
        }
        if (rowIndex != 0) {
            return new Integer(this.portfolio.getOwnings(this.mainColumn.elementAt(rowIndex).toString()));
        }
        return new Integer(Math.round(this.portfolio.getCash()));
    }
    
    @Override
    public void setValueAt(final Object object, final int row, final int col) {
        if (col == 1) {
            if (row == 0) {
                this.portfolio.setCash((float)(int)object);
                this.portfolio.setNonInvestedCash((float)(int)object);
            }
            else {
                this.portfolio.setOwnings(this.mainColumn.elementAt(row).toString(), (int)object);
                this.portfolio.setNonInvestedOwnings(this.mainColumn.elementAt(row).toString(), (int)object);
            }
        }
    }
    
    public void portfolioModified(final PortfolioEvent e) {
        if (e.getEvent() == 1) {
            this.fireTableCellUpdated(this.mainColumn.indexOf(e.getAssetUpdated()), 1);
        }
        else if (e.getEvent() == 0) {
            this.fireTableCellUpdated(0, 1);
        }
        else if (e.getEvent() == 2) {
            if (!this.mainColumn.contains(e.getAssetUpdated())) {
                if (this.mainColumn.contains(e.getAssetUpdated())) {
                    this.fireTableCellUpdated(this.mainColumn.indexOf(e.getAssetUpdated()), 1);
                }
                else {
                    this.mainColumn.add(e.getAssetUpdated());
                    this.fireTableRowsInserted(this.mainColumn.size() - 1, this.mainColumn.size() - 1);
                }
            }
        }
        else if (e.getEvent() == 3 && this.mainColumn.contains(e.getAssetUpdated())) {
            final int row = this.mainColumn.indexOf(e.getAssetUpdated());
            this.mainColumn.remove(e.getAssetUpdated());
            this.fireTableRowsDeleted(row, row);
        }
    }
    
    public void assetsModified(final AssetEvent e) {
        if (e.getEvent() == 1) {
            this.addRow(e.getAssetName());
        }
        else if (e.getEvent() == 0) {
            this.removeRow(e.getAssetName());
        }
    }
    
    private void addRow(final String assetName) {
        this.addRow(assetName, 0);
    }
    
    private void addRow(final String assetName, final int qtty) {
        this.portfolio.setOwnings(assetName, qtty);
    }
    
    private void removeRow(final String assetName) {
        this.portfolio.removeAssetFromOwnings(assetName);
    }
    
    public void setCellEditable() {
        this.cellEditable = true;
    }
    
    public void setCellUneditable() {
        this.cellEditable = false;
    }
}
