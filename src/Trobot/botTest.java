package Trobot;

import java.util.Iterator;
import jessx.business.operations.LimitOrder;
import jessx.net.NetworkWritable;

public class botTest extends Robot {
  public botTest(int name) {
    super(name);
  }
  
  public long BasicNextWakeUp() {
    return 5000L;
  }
  
  protected void MyAct() {
    Iterator<String> iterInstit = getRobotCore().getInstitutions().keySet().iterator();
    while (iterInstit.hasNext()) {
      String instit = iterInstit.next();
      int side = (int)Math.round(Math.random());
      float price = 25.0F;
      LimitOrder lo = new LimitOrder();
      lo.setEmitter(getLogin());
      lo.setInstitutionName(instit);
      lo.setPrice(price);
      lo.setQuantity(10);
      lo.setSide(side);
      getRobotCore().send((NetworkWritable)lo);
    } 
  }
  
  public long NextWakeUp(String instit) {
    return 5000L;
  }
  
  protected String chooseName(int i) {
    return "botTest" + (i + 1);
  }
}
