// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

import java.util.Iterator;
import java.util.List;
import org.jdom.Element;
import java.util.Hashtable;
import jessx.utils.Utils;
import org.jdom.Document;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.table.AbstractTableModel;

public class TableModelPlayersClassification extends AbstractTableModel
{
    private Vector vectorName;
    private Vector vectorCash;
    private HashMap assetValueAtTheEnd;
    private int rowCount;
    
    public TableModelPlayersClassification(final Document document, final TableModelPlayersResults tableModelPlayersResults) {
        this.vectorName = new Vector();
        this.vectorCash = new Vector();
        this.assetValueAtTheEnd = new HashMap();
        try {
            Utils.logger.debug("TableModelPlayersClassification");
            this.rowCount = tableModelPlayersResults.getRowCount();
            final int columnCount = tableModelPlayersResults.getColumnCount();
            final Element experimentNode = document.getRootElement();
            Utils.logger.debug("Asset");
            final List assets = experimentNode.getChild("Setup").getChildren("Asset");
            Utils.logger.debug("PeriodNumber");
            final int periodNumber = Integer.parseInt(experimentNode.getChild("Setup").getChild("GeneralParameters").getChildText("PeriodNumber"));
            final Iterator assetsIter = assets.iterator();
            final Hashtable assetAndColumnNumber = new Hashtable();
            while (assetsIter.hasNext()) {
                final Element asset = (Element) assetsIter.next();
                Utils.logger.debug("Dividend");
                final List dividends = asset.getChild("DividendModel").getChildren("Dividend");
                Utils.logger.debug("name");
                final String assetName = asset.getAttributeValue("name");
                this.assetValueAtTheEnd.put(assetName, this.assetValueAtTheEnd(dividends, periodNumber));
                int j;
                for (j = 3; assetName != tableModelPlayersResults.getColumnName(j) && j < columnCount; j += 2) {}
                Utils.logger.debug("assetAndColumnNumber.put(j,assetName)" + j);
                assetAndColumnNumber.put(new StringBuilder().append(j).toString(), assetName);
            }
            Utils.logger.debug("columnCount:" + columnCount);
            for (int i = 0; i < this.rowCount; ++i) {
                Utils.logger.debug("i:" + i);
                this.vectorName.add(i, tableModelPlayersResults.getValueAt(i, 0));
                float cash = (Float)tableModelPlayersResults.getValueAt(i, 1);
                int j = 3;
                while (j < columnCount) {
                  Utils.logger.debug("j:" + j);
                  cash += ( (Float) tableModelPlayersResults.getValueAt(i, j)).floatValue() *
                      ( (Float) assetValueAtTheEnd.get(assetAndColumnNumber.get("" + j))).
                      floatValue();
                  j += 2;
                }
                this.vectorCash.add(i, new Float(cash));
            }
            this.sort();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void sort() {
        Utils.logger.debug("sort");
        for (int i = 0; i < this.rowCount; ++i) {
            int index = i;
            for (int j = i; j < this.rowCount; ++j) {
            	 if (((Float)vectorCash.get(index)).floatValue() < ((Float)vectorCash.get(j)).floatValue()) {
                    index = j;
                }
            }
            final Object tempCash = this.vectorCash.get(index);
            this.vectorCash.remove(index);
            this.vectorCash.add(i, tempCash);
            final Object tempName = this.vectorName.get(index);
            this.vectorName.remove(index);
            this.vectorName.add(i, tempName);
        }
    }
    
    public Float assetValueAtTheEnd(final List dividends, final int periodNumber) {
        Utils.logger.debug("assetValueAtTheEnd");
        float assetValueAtTheEnd = 0.0f;
        final List subDividends = dividends.subList(periodNumber, dividends.size());
        Iterator subDividendsIter = subDividends.iterator();
        while (subDividendsIter.hasNext()) {
            Element dividend = (Element) subDividendsIter.next();
            final String divValue = dividend.getAttributeValue("mean");
            assetValueAtTheEnd += Float.parseFloat(divValue);
        }
        return new Float(assetValueAtTheEnd);
    }
    
    public int getRowCount() {
        return this.rowCount;
    }
    
    public int getColumnCount() {
        return 3;
    }
    
    @Override
    public Class getColumnClass(final int column) {
        if (column == 0) {
            return String.class;
        }
        if (column == 1) {
            return String.class;
        }
        return Float.class;
    }
    
    @Override
    public String getColumnName(final int column) {
        switch (column) {
            case 0: {
                return "Rank";
            }
            case 1: {
                return "Players' Name";
            }
            case 2: {
                return "Reassessed Cash ($)";
            }
            default: {
                return "Cash ($)";
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return false;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        Utils.logger.debug("getValueAt: ( " + rowIndex + " ; " + columnIndex + " )");
        switch (columnIndex) {
            case 0: {
                return Integer.toString(rowIndex + 1);
            }
            case 1: {
                return this.vectorName.get(rowIndex);
            }
            default: {
                return this.vectorCash.get(rowIndex);
            }
        }
    }
}
