package Trobot;

import java.util.Date;
import java.util.Iterator;
import jessx.business.operations.LimitOrder;
import jessx.net.NetworkWritable;
import org.jdom.Document;

public class NotDiscreet extends Animator {
  private int lowLimit;
  
  private int highLimit;
  
  public NotDiscreet(int i, double InactivityPercentage, int lowLimit, int highLimit) {
    super(i, InactivityPercentage);
    this.lowLimit = lowLimit;
    this.highLimit = highLimit;
  }
  
  protected void MyAct() {
    Iterator<String> iterInstit = getRobotCore().getInstitutions().keySet().iterator();
    while (iterInstit.hasNext()) {
      String instit = iterInstit.next();
      if ((new Date()).getTime() - ((Date)getDatesLastOrder().get(instit)).getTime() > NextWakeUp(instit)) {
        int quantity = 1 + (int)(Math.random() * 49.0D);
        int side = (int)Math.round(Math.random());
        float price = ((int)(this.lowLimit + (this.highLimit - this.lowLimit) * Math.random()) * 100 / 100);
        LimitOrder lo = new LimitOrder();
        lo.setEmitter(getLogin());
        lo.setInstitutionName(instit);
        lo.setPrice(price);
        lo.setQuantity(quantity);
        lo.setSide(side);
        getRobotCore().send((NetworkWritable)lo);
      } 
    } 
  }
  
  public void objectReceived(Document xmlObject) {
    super.objectReceived(xmlObject);
  }
  
  protected String chooseName(int i) {
    return "PotentiallyDetectableZIT" + (i + 1);
  }
}
