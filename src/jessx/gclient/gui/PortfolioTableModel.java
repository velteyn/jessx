// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import jessx.business.event.PortfolioEvent;
import java.util.Iterator;
import jessx.client.ClientCore;
import java.util.Vector;
import jessx.business.event.PortfolioListener;
import jessx.utils.gui.TStrippedLinesTableModel;

public class PortfolioTableModel extends TStrippedLinesTableModel implements PortfolioListener
{
    private Vector mainColumn;
    
    public PortfolioTableModel(final Object[] columnNames) {
        super(columnNames, 0);
        this.mainColumn = new Vector();
        (this.mainColumn = new Vector()).add("Cash");
        super.addRow(new Object[] { "Cash", new Float(Math.floor(ClientCore.getPortfolio().getCash() * 100.0f) / 100.0) });
        Iterator iter = ClientCore.getPortfolio().getOwnings().keySet().iterator();

        while(iter.hasNext()) {

          String key = (String)iter.next();
            super.addRow(new Object[] { key, new Integer(ClientCore.getPortfolio().getOwnings(key)) });
            this.mainColumn.add(key);
        }
        ClientCore.getPortfolio().addListener(this);
    }
    
    public void portfolioModified(final PortfolioEvent e) {
        if (e.getEvent() == 0) {
            super.setValueAt(new Float(Math.floor(ClientCore.getPortfolio().getCash() * 100.0f) / 100.0), 0, 1);
        }
        else if (e.getEvent() == 1) {
            final int i = this.mainColumn.indexOf(e.getAssetUpdated());
            super.setValueAt(new Float(Math.floor(ClientCore.getPortfolio().getCash() * 100.0f) / 100.0), 0, 1);
            super.setValueAt(new Integer(ClientCore.getPortfolio().getOwnings(e.getAssetUpdated())), i, 1);
        }
        else if (e.getEvent() == 2 && !this.mainColumn.contains(e.getAssetUpdated())) {
            this.mainColumn.add(e.getAssetUpdated());
            super.addRow(new Object[] { e.getAssetUpdated(), new Integer(ClientCore.getPortfolio().getOwnings(e.getAssetUpdated())) });
        }
    }
}
