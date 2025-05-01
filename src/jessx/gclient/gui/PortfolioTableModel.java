

/* Decompiler 45ms, total 261ms, lines 43 */
package jessx.gclient.gui;

import java.util.Iterator;
import java.util.Vector;
import jessx.business.event.PortfolioEvent;
import jessx.business.event.PortfolioListener;
import jessx.client.ClientCore;
import jessx.utils.gui.TStrippedLinesTableModel;

public class PortfolioTableModel extends TStrippedLinesTableModel implements PortfolioListener {
   private Vector mainColumn = new Vector();

   public PortfolioTableModel(Object[] columnNames) {
      super(columnNames, 0);
      this.mainColumn = new Vector();
      this.mainColumn.add("Cash");
      super.addRow(new Object[]{"Cash", new Float(Math.floor((double)(ClientCore.getPortfolio().getCash() * 100.0F)) / 100.0D)});
      Iterator iter = ClientCore.getPortfolio().getOwnings().keySet().iterator();

      while(iter.hasNext()) {
         String key = (String)iter.next();
         super.addRow(new Object[]{key, new Integer(ClientCore.getPortfolio().getOwnings(key))});
         this.mainColumn.add(key);
      }

      ClientCore.getPortfolio().addListener(this);
   }

   public void portfolioModified(PortfolioEvent e) {
      if (e.getEvent() == 0) {
         super.setValueAt(new Float(Math.floor((double)(ClientCore.getPortfolio().getCash() * 100.0F)) / 100.0D), 0, 1);
      } else if (e.getEvent() == 1) {
         int i = this.mainColumn.indexOf(e.getAssetUpdated());
         super.setValueAt(new Float(Math.floor((double)(ClientCore.getPortfolio().getCash() * 100.0F)) / 100.0D), 0, 1);
         super.setValueAt(new Integer(ClientCore.getPortfolio().getOwnings(e.getAssetUpdated())), i, 1);
      } else if (e.getEvent() == 2 && !this.mainColumn.contains(e.getAssetUpdated())) {
         this.mainColumn.add(e.getAssetUpdated());
         super.addRow(new Object[]{e.getAssetUpdated(), new Integer(ClientCore.getPortfolio().getOwnings(e.getAssetUpdated()))});
      }

   }
}

