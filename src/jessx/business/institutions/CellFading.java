// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import java.awt.Color;
import javax.swing.event.TableModelEvent;
import javax.swing.JLabel;
import javax.swing.JTable;
import java.util.Vector;

class CellFading extends Thread
{
    private Vector cells;
    private JTable jTableAsk;
    private JTable jTableBid;
    static int STATE_NUM;
    static int DURATION;
    
    static {
        CellFading.STATE_NUM = 10;
        CellFading.DURATION = 500;
    }
    
    public CellFading(final JTable ask, final JTable bid) {
        this.cells = new Vector();
        this.jTableAsk = ask;
        this.jTableBid = bid;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(CellFading.DURATION / CellFading.STATE_NUM);
            }
            catch (InterruptedException ex) {}
            for (int i = 0; i < this.cells.size(); ++i) {
                final ColoredCell tempCell = (ColoredCell) this.cells.elementAt(i);
                tempCell.nextColor();
                if (tempCell.isBidSide()) {
                    ((JLabel)this.jTableBid.getValueAt(tempCell.getRow(), tempCell.getCol())).setBackground(tempCell.getCurrentColor());
                }
                else {
                    ((JLabel)this.jTableAsk.getValueAt(tempCell.getRow(), tempCell.getCol())).setBackground(tempCell.getCurrentColor());
                }
                if (tempCell.getStep() >= CellFading.STATE_NUM) {
                    this.cells.removeElementAt(i);
                    --i;
                }
            }
            this.jTableAsk.tableChanged(new TableModelEvent(this.jTableAsk.getModel()));
            this.jTableBid.tableChanged(new TableModelEvent(this.jTableBid.getModel()));
        }
    }
    
    public void registerCell(final int side, final int col, final int row, final Color iColor, final Color fColor) {
        this.cells.add(new ColoredCell(side, col, row, iColor, fColor, CellFading.STATE_NUM));
    }
}
