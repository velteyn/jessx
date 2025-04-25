// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import jessx.utils.Utils;
import org.jdom.Document;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableModelPlayersResults extends AbstractTableModel
{
    private int columnCount;
    private int rowCount;
    private Vector assetsName;
    private HashMap endPortfolios;
    private HashMap endOwnings;
    private HashMap totalQuantity;
    private float sumOfCash;
    
    public TableModelPlayersResults(final Document document) {
        this.rowCount = 0;
        this.assetsName = new Vector();
        this.endPortfolios = new HashMap();
        this.endOwnings = new HashMap();
        this.totalQuantity = new HashMap();
        this.sumOfCash = 0.0f;
        final Element experimentNode = document.getRootElement();
        Utils.logger.debug(experimentNode.getChild("Setup"));
        if (experimentNode.getChild("Setup") == null) {
            Utils.logger.debug("Wrong file, Setup doesn't exists");
        }
        final List assets = experimentNode.getChild("Setup").getChildren("Asset");
        Iterator assetsIter = assets.iterator();
        while (assetsIter.hasNext()) {
          Element asset = (Element) assetsIter.next();
            final String assetName = asset.getAttributeValue("name");
            this.assetsName.add(assetName);
            this.totalQuantity.put(assetName, "0");
        }
        final List periods = experimentNode.getChildren("Period");
        final List portfolios = ((Element) periods.get(periods.size() - 1)).getChildren("Portfolio");
        final Iterator portfoliosIter = portfolios.iterator();
        int row = 0;
        while (portfoliosIter.hasNext()) {
            final Element portfolio = (Element) portfoliosIter.next();
            final List ownings = portfolio.getChildren("Owning");
            final Iterator owningsIter = ownings.iterator();
            this.endPortfolios.put(Integer.toString(row), portfolio);
            int col = 0;
            while (owningsIter.hasNext()) {
                final Element owning = (Element) owningsIter.next();
                final String quantity = owning.getAttributeValue("qtty");
                final String temp2 = (String) this.assetsName.elementAt(col);
                final int temp3 = Integer.parseInt(quantity);
                final int temp4 = Integer.parseInt((String) this.totalQuantity.get(temp2));
                this.totalQuantity.remove(temp2);
                this.totalQuantity.put(temp2, Integer.toString(temp3 + temp4));
                this.endOwnings.put("(" + Integer.toString(row) + "," + Integer.toString(2 * col + 3) + ")", quantity);
                ++col;
            }
            this.sumOfCash += Float.parseFloat(portfolio.getAttributeValue("cash"));
            ++row;
            ++this.rowCount;
        }
    }
    
    public int getRowCount() {
        return this.rowCount;
    }
    
    public int getColumnCount() {
        Utils.logger.debug("getColumnCount = " + this.columnCount);
        return this.columnCount = 3 + 2 * this.assetsName.size();
    }
    
    @Override
    public Class getColumnClass(final int column) {
        return (Class)((column == 0) ? String.class : Float.class);
    }
    
    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0: {
                return "Players' Name";
            }
            case 1: {
                return "Cash ($)";
            }
            case 2: {
                return "Cash (%)";
            }
            default: {
                return (String) ((column / 2 == (column - 1) / 2) ? this.assetsName.elementAt((column - 3) / 2) : (String.valueOf(this.assetsName.elementAt((column - 3) / 2)) + " (%)"));
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        Utils.logger.debug("isCellEditable(" + rowIndex + "," + columnIndex + ")");
        return false;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Utils.logger.debug("getValueAt: ( " + rowIndex + " ; " + columnIndex + " )");
        switch (columnIndex) {
            case 0: {
                return ((Element) this.endPortfolios.get(Integer.toString(rowIndex))).getAttributeValue("player");
            }
            case 1: {
                final String cash = ((Element) this.endPortfolios.get(Integer.toString(rowIndex))).getAttributeValue("cash");
                return new Float(Math.round(Float.parseFloat(cash) * 100.0f) / 100.0f);
            }
            case 2: {
                final String cash = ((Element) this.endPortfolios.get(Integer.toString(rowIndex))).getAttributeValue("cash");
                return new Float(Math.round(Float.parseFloat(cash) / this.sumOfCash * 10000.0f) / 100.0f);
            }
            default: {
                Utils.logger.debug("Attention");
                if (columnIndex / 2 != (columnIndex - 1) / 2) {
                    final String total = (String) this.totalQuantity.get(this.getColumnName(columnIndex - 1));
                    final float percent = (float)this.getValueAt(rowIndex, columnIndex - 1) / Float.parseFloat(total) * 100.0f;
                    return new Float(Math.round(percent * 100.0f) / 100.0f);
                }
                Utils.logger.debug("Modification");
                int i = 0;
                final List ownings = ((Element) this.endPortfolios.get(Integer.toString(rowIndex))).getChildren("Owning");
                if (ownings.size() == 0) {
                    return new Float(0.0f);
                }
                Element owning = ( (Element) ownings.get(i));
                while (! (owning.getAttributeValue(
                    "asset").toString().equals(this.getColumnName(columnIndex))) &&
                       (i < ownings.size() - 1)) {
                    Utils.logger.debug(owning.getAttributeValue("asset").toString());
                    Utils.logger.debug(this.getColumnName(columnIndex));
                }
                if (owning.getAttributeValue("asset").toString().equals(this.getColumnName(columnIndex))) {
                    return Float.valueOf(owning.getAttributeValue("qtty"));
                }
                return new Float(0.0f);
            }
        }
    }
}
