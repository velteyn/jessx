// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import javax.swing.event.ChangeEvent;
import jessx.business.event.DividendEvent;
import org.jdom.Content;
import org.jdom.Element;
import jessx.business.event.DividendListener;
import java.util.Iterator;
import jessx.utils.Utils;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.event.ChangeListener;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public class DividendModel implements XmlExportable, XmlLoadable, ChangeListener
{
    private Vector dividendList;
    private Vector dividendListener;
    private int periodCount;
    private HashMap windowSizes;
    private String biggestWindowSize;
    
    public void setPeriodCount(final int periodCount) {
        this.periodCount = periodCount;
        this.setDividendListSize();
    }
    
    public void setWindowSize(final String playerType, final int size) {
        this.windowSizes.put(playerType, new Integer(size));
        if (!this.windowSizes.containsKey(this.biggestWindowSize)) {
            this.biggestWindowSize = playerType;
            this.setDividendListSize();
        }
        this.calcBiggestWinSize();
    }
    
    public int getPeriodCount() {
        return this.periodCount;
    }
    
    public int getBiggestWindowSize() {
        if (this.windowSizes.containsKey(this.biggestWindowSize)) {
            return (int) this.windowSizes.get(this.biggestWindowSize);
        }
        return 1;
    }
    
    public float getExperimentHoldingValue(final int offset) {
        float hv = 0.0f;
        for (int i = offset; i < this.dividendList.size(); ++i) {
            int interestRateIndex;
            if (i >= BusinessCore.getGeneralParameters().getPeriodCount()) {
                interestRateIndex = BusinessCore.getGeneralParameters().getPeriodCount() - 1;
            }
            else {
                interestRateIndex = i;
            }
            hv = hv * (1.0f + BusinessCore.getGeneralParameters().getInterestRate(interestRateIndex) / 100.0f) + ((Dividend) this.dividendList.elementAt(i)).getNormalValue();
        }
        hv *= 100.0f;
        hv = (float)(int)(hv + 0.5);
        hv /= 100.0f;
        return hv;
    }
    
    public float getWindowHoldingValue(final int offset, final int size) {
        float hv = 0.0f;
        for (int i = offset; i < this.dividendList.size() && i < offset + size; ++i) {
            int interestRateIndex;
            if (i >= BusinessCore.getGeneralParameters().getPeriodCount()) {
                interestRateIndex = BusinessCore.getGeneralParameters().getPeriodCount() - 1;
            }
            else {
                interestRateIndex = i;
            }
            hv = hv * (1.0f + BusinessCore.getGeneralParameters().getInterestRate(interestRateIndex) / 100.0f) + ((Dividend) this.dividendList.elementAt(i)).getNormalValue();
        }
        hv *= 100.0f;
        hv = (float)(int)(hv + 0.5);
        hv /= 100.0f;
        return hv;
    }
    
    public float getDividend(int period) {
        return ((Dividend)dividendList.elementAt(period)).getDividend();
      }

    public Dividend getDividendAt(final int period) {
        Utils.logger.debug("Dividend object at period " + period + " was asked.");
        return (Dividend) this.dividendList.elementAt(period);
    }
    
    @Deprecated
    public void setDividend(final Dividend div, final int period) {
        if (period < this.dividendList.size()) {
            this.dividendList.setElementAt(div, period);
            this.fireDividendUpdated(period);
        }
    }
    
    private void setDividendListSize() {
        final int size = this.getPeriodCount() + this.getBiggestWindowSize() - 1;
        if (this.dividendList.size() < size) {
            this.dividendList.add(new NormalDividend());
            this.setDividendListSize();
        }
        else if (this.dividendList.size() > size) {
            this.dividendList.setSize(size);
        }
    }
    
    private void calcBiggestWinSize() {
        Iterator iter = this.windowSizes.keySet().iterator();
        while(iter.hasNext()) {
          String key = (String) iter.next();
          if (((Integer)this.windowSizes.get(key)).intValue() >
              ((Integer)this.windowSizes.get(this.biggestWindowSize)).intValue()) {
            this.biggestWindowSize = key;
          }
        }
        this.setDividendListSize();
      }
    
    public void addDividendListener(final DividendListener listener) {
        this.dividendListener.add(listener);
    }
    
    public void removeDividendListener(final DividendListener listener) {
        this.dividendListener.remove(listener);
    }
    
    public int getDefinedPeriodCount() {
        return this.dividendList.size();
    }
    
    public void loadFromXml(final Element node) {
        Utils.logger.debug("Loading dividendModel from xml...");
        final String periodCount = node.getAttributeValue("periodCount");
        if (periodCount == null) {
            Utils.logger.error("invalid xml DividendModel node: attribute periodCount not found.");
            return;
        }
        this.periodCount = Integer.parseInt(periodCount);
        final String biggestWindow = node.getAttributeValue("biggestWindow");
        final String size = node.getAttributeValue("size");
        if (biggestWindow != null && size != null) {
            this.setWindowSize(biggestWindow, Integer.parseInt(size));
        }
        final Iterator divNodes = node.getChildren("Dividend").iterator();
        int i = 0;
        while (divNodes.hasNext()) {
            final Element divNode = (Element) divNodes.next();
            this.getDividendAt(i).loadFromXml(divNode);
            ++i;
        }
    }
    
    public void saveToXml(final Element node) {
        Utils.logger.debug("Saving dividend model to xml...");
        node.setAttribute("periodCount", Integer.toString(this.periodCount));
        if (this.windowSizes.containsKey(this.biggestWindowSize)) {
            node.setAttribute("biggestWindow", this.biggestWindowSize);
            node.setAttribute("size", Integer.toString(this.getBiggestWindowSize()));
        }
        for (int i = 0; i < this.dividendList.size(); ++i) {
            final Element dividend = new Element("Dividend");
            ((XmlExportable) this.dividendList.elementAt(i)).saveToXml(dividend);
            node.addContent(dividend);
        }
    }
    
    private void fireDividendUpdated(final int period) {
        for (int i = 0; i < this.dividendListener.size(); ++i) {
            ((DividendListener) this.dividendListener.elementAt(i)).dividendModified(new DividendEvent((Dividend) this.dividendList.elementAt(period), period, 0));
        }
    }
    
    public DividendModel() {
        this.dividendList = new Vector();
        this.dividendListener = new Vector();
        this.periodCount = 1;
        this.windowSizes = new HashMap();
        this.setPeriodCount(BusinessCore.getGeneralParameters().getPeriodCount());
        BusinessCore.getGeneralParameters().addPeriodCountChangeListener(this);
    }
    
    public void stateChanged(final ChangeEvent e) {
        this.setPeriodCount(BusinessCore.getGeneralParameters().getPeriodCount());
    }
}
