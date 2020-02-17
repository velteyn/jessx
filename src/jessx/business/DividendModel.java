package jessx.business;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import jessx.business.event.DividendEvent;
import jessx.business.event.DividendListener;
import jessx.utils.Utils;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;
import org.jdom.Content;
import org.jdom.Element;

public class DividendModel implements XmlExportable, XmlLoadable, ChangeListener {
  private Vector dividendList = new Vector();
  
  private Vector dividendListener = new Vector();
  
  private int periodCount = 1;
  
  private HashMap windowSizes = new HashMap<Object, Object>();
  
  private String biggestWindowSize;
  
  public void setPeriodCount(int periodCount) {
    this.periodCount = periodCount;
    setDividendListSize();
  }
  
  public void setWindowSize(String playerType, int size) {
    this.windowSizes.put(playerType, new Integer(size));
    if (!this.windowSizes.containsKey(this.biggestWindowSize)) {
      this.biggestWindowSize = playerType;
      setDividendListSize();
    } 
    calcBiggestWinSize();
  }
  
  public int getPeriodCount() {
    return this.periodCount;
  }
  
  public int getBiggestWindowSize() {
    if (this.windowSizes.containsKey(this.biggestWindowSize))
      return ((Integer)this.windowSizes.get(this.biggestWindowSize)).intValue(); 
    return 1;
  }
  
  public float getExperimentHoldingValue(int offset) {
    float hv = 0.0F;
    for (int i = offset; i < this.dividendList.size(); i++) {
      int interestRateIndex;
      if (i >= BusinessCore.getGeneralParameters().getPeriodCount()) {
        interestRateIndex = BusinessCore.getGeneralParameters().getPeriodCount() - 1;
      } else {
        interestRateIndex = i;
      } 
      hv = hv * (1.0F + BusinessCore.getGeneralParameters().getInterestRate(interestRateIndex) / 100.0F) + ((Dividend)this.dividendList.elementAt(i)).getNormalValue();
    } 
    hv *= 100.0F;
    hv = (int)(hv + 0.5D);
    hv /= 100.0F;
    return hv;
  }
  
  public float getWindowHoldingValue(int offset, int size) {
    float hv = 0.0F;
    for (int i = offset; i < this.dividendList.size() && i < offset + size; i++) {
      int interestRateIndex;
      if (i >= BusinessCore.getGeneralParameters().getPeriodCount()) {
        interestRateIndex = BusinessCore.getGeneralParameters().getPeriodCount() - 1;
      } else {
        interestRateIndex = i;
      } 
      hv = hv * (1.0F + BusinessCore.getGeneralParameters().getInterestRate(interestRateIndex) / 100.0F) + ((Dividend)this.dividendList.elementAt(i)).getNormalValue();
    } 
    hv *= 100.0F;
    hv = (int)(hv + 0.5D);
    hv /= 100.0F;
    return hv;
  }
  
  public float getDividend(int period) {
    return ((Dividend)this.dividendList.elementAt(period)).getDividend();
  }
  
  public Dividend getDividendAt(int period) {
    Utils.logger.debug("Dividend object at period " + period + " was asked.");
    return (Dividend) this.dividendList.elementAt(period);
  }
  
  public void setDividend(Dividend div, int period) {
    if (period < this.dividendList.size()) {
      this.dividendList.setElementAt(div, period);
      fireDividendUpdated(period);
    } 
  }
  
  private void setDividendListSize() {
    int size = getPeriodCount() + getBiggestWindowSize() - 1;
    if (this.dividendList.size() < size) {
      this.dividendList.add(new NormalDividend());
      setDividendListSize();
    } else if (this.dividendList.size() > size) {
      this.dividendList.setSize(size);
    } 
  }
  
  private void calcBiggestWinSize() {
    Iterator<String> iter = this.windowSizes.keySet().iterator();
    while (iter.hasNext()) {
      String key = iter.next();
      if (((Integer)this.windowSizes.get(key)).intValue() > (
        (Integer)this.windowSizes.get(this.biggestWindowSize)).intValue())
        this.biggestWindowSize = key; 
    } 
    setDividendListSize();
  }
  
  public void addDividendListener(DividendListener listener) {
    this.dividendListener.add(listener);
  }
  
  public void removeDividendListener(DividendListener listener) {
    this.dividendListener.remove(listener);
  }
  
  public int getDefinedPeriodCount() {
    return this.dividendList.size();
  }
  
  public void loadFromXml(Element node) {
    Utils.logger.debug("Loading dividendModel from xml...");
    String periodCount = node.getAttributeValue("periodCount");
    if (periodCount == null) {
      Utils.logger.error("invalid xml DividendModel node: attribute periodCount not found.");
      return;
    } 
    this.periodCount = Integer.parseInt(periodCount);
    String biggestWindow = node.getAttributeValue("biggestWindow");
    String size = node.getAttributeValue("size");
    if (biggestWindow != null && size != null)
      setWindowSize(biggestWindow, Integer.parseInt(size)); 
    Iterator<Element> divNodes = node.getChildren("Dividend").iterator();
    int i = 0;
    while (divNodes.hasNext()) {
      Element divNode = divNodes.next();
      getDividendAt(i).loadFromXml(divNode);
      i++;
    } 
  }
  
  public void saveToXml(Element node) {
    Utils.logger.debug("Saving dividend model to xml...");
    node.setAttribute("periodCount", Integer.toString(this.periodCount));
    if (this.windowSizes.containsKey(this.biggestWindowSize)) {
      node.setAttribute("biggestWindow", this.biggestWindowSize);
      node.setAttribute("size", Integer.toString(getBiggestWindowSize()));
    } 
    for (int i = 0; i < this.dividendList.size(); i++) {
      Element dividend = new Element("Dividend");
      ((Dividend)this.dividendList.elementAt(i)).saveToXml(dividend);
      node.addContent((Content)dividend);
    } 
  }
  
  private void fireDividendUpdated(int period) {
	    for(int i = 0; i < this.dividendListener.size(); i++) {
	      ((DividendListener)this.dividendListener.elementAt(i)).dividendModified(new DividendEvent((Dividend)this.dividendList.elementAt(period),period,DividendEvent.DIVIDEND_UPDATED));;
	    }
	  }
  
  public DividendModel() {
    setPeriodCount(BusinessCore.getGeneralParameters().getPeriodCount());
    BusinessCore.getGeneralParameters().addPeriodCountChangeListener(this);
  }
  
  public void stateChanged(ChangeEvent e) {
    setPeriodCount(BusinessCore.getGeneralParameters().getPeriodCount());
  }
}
