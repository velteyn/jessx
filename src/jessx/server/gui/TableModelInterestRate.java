// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.util.Iterator;
import org.jdom.Content;
import org.jdom.Element;
import jessx.utils.Utils;
import java.util.Vector;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;
import javax.swing.table.AbstractTableModel;

public class TableModelInterestRate extends AbstractTableModel implements XmlExportable, XmlLoadable
{
    private Vector<Float> interestRate;
    private boolean cellEditable;
    private String[] columnNames;
    
    public TableModelInterestRate() {
        this.interestRate = new Vector<Float>();
        this.cellEditable = true;
        this.columnNames = new String[] { "Period n° ", "Interest Rate (%) " };
        this.interestRate.setSize(1);
        this.interestRate.setElementAt(0.0f, 0);
    }
    
    public Vector getVectorVal() {
        return this.interestRate;
    }
    
    public void setVectorVal(final Object bidule, final int i) {
    }
    
    public int getColumnCount() {
        return 2;
    }
    
    public int getRowCount() {
        return this.interestRate.size();
    }
    
    @Override
    public Class getColumnClass(final int column) {
        return (Class)((column == 0) ? Integer.class : Float.class);
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return new Integer(rowIndex + 1);
        }
        return this.interestRate.elementAt(rowIndex);
    }
    
    @Override
    public String getColumnName(final int column) {
        return this.columnNames[column];
    }
    
    @Override
    public void setValueAt(final Object aValue, final int rowIndex, final int columnIndex) {
        if (aValue != null) {
            Utils.logger.error("setValueAt  " + aValue + "  " + rowIndex);
        }
        this.interestRate.setElementAt((Float)aValue, rowIndex);
    }
    
    public void setInterestSize(final int size) {
        final int oldSize = this.interestRate.size();
        this.interestRate.setSize(size);
        for (int i = oldSize; i < size; ++i) {
            this.interestRate.setElementAt(this.interestRate.elementAt(oldSize - 1), i);
        }
    }
    
    public void saveToXml(final Element node) {
        node.setAttribute("periodCount", Integer.toString(this.interestRate.size()));
        for (int i = 0; i < this.interestRate.size(); ++i) {
            final Element periodRate = new Element("PeriodRate");
            periodRate.setAttribute("Rate", Float.toString(this.interestRate.elementAt(i)));
            node.addContent(periodRate);
        }
    }
    
    public void fillInterestRate(final float interest) {
        for (int i = 0; i < this.interestRate.size(); ++i) {
            this.interestRate.setElementAt(interest, i);
        }
    }
    
    public void loadFromXml(final Element node) {
        final String periodCount = node.getAttributeValue("periodCount");
        if (periodCount == null) {
            Utils.logger.error("invalid xml, assuming it is an older version");
            this.fillInterestRate(Float.parseFloat(node.getText()));
        }
        else {
            this.setInterestSize(Integer.parseInt(periodCount));
            final Iterator interestNodes = node.getChildren("PeriodRate").iterator();
            int i = 0;
            while (interestNodes.hasNext()) {
                final Element interestNode = (Element) interestNodes.next();
                this.setValueAt(Float.parseFloat(interestNode.getAttributeValue("Rate")), i, 2);
                ++i;
            }
        }
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return this.cellEditable && columnIndex > 0;
    }
    
    public void setCellEditable() {
        this.cellEditable = true;
    }
    
    public void setCellUneditable() {
        this.cellEditable = false;
    }
}
