package Trobot;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import jessx.business.Order;
import jessx.business.OrderBook;
import jessx.business.operations.LimitOrder;
import jessx.net.NetworkWritable;
import org.jdom.Document;

public class Discreet extends Animator {
  public Discreet(int name, double InactivityPercentage) {
    super(name, InactivityPercentage);
  }
  
  protected void MyAct() {
    Iterator<String> iterInstit = getRobotCore().getInstitutions().keySet().iterator();
    while (iterInstit.hasNext()) {
      String instit = iterInstit.next();
      if ((new Date()).getTime() - ((Date)getDatesLastOrder().get(instit)).getTime() > NextWakeUp(instit) && (
        (LinkedList)getOrderBooks().get(instit)).size() > 0) {
        OrderBook ob = ((LinkedList<OrderBook>)getOrderBooks().get(instit)).getLast();
        int side = (int)Math.round(Math.random());
        if (ob.getAsk().size() >= 1 && ob.getBid().size() >= 1 && side == 0) {
          int indiceAskOb = 0;
          int indiceBidOb = 0;
          int indiceTailleOb = 0;
          int totalQttySum = 0;
          int QttySum = 0;
          while (indiceAskOb < ob.getAsk().size()) {
            if (indiceTailleOb >= 5)
              break; 
            if (indiceAskOb < ob.getAsk().size() - 2 && indiceAskOb > 0) {
              if (((Order)ob.getAsk().elementAt(indiceAskOb)).getOrderPrice(0) == (
                (Order)ob.getAsk().elementAt(indiceAskOb + 1)).getOrderPrice(0))
                totalQttySum += ((Order)ob.getAsk().elementAt(indiceAskOb)).getMaxQtty(); 
            } else {
              indiceTailleOb++;
            } 
            indiceAskOb++;
          } 
          while (indiceBidOb < ob.getBid().size()) {
            if (indiceTailleOb >= 5)
              break; 
            if (indiceBidOb < ob.getBid().size() - 2 && indiceBidOb > 0 && (
              (Order)ob.getBid().elementAt(indiceBidOb)).getOrderPrice(1) == (
              (Order)ob.getBid().elementAt(indiceBidOb + 1)).getOrderPrice(1))
              totalQttySum += ((Order)ob.getBid().elementAt(indiceBidOb)).getMaxQtty(); 
            indiceBidOb++;
          } 
          while (indiceAskOb < ob.getAsk().size()) {
            if (indiceTailleOb >= 5 || 
              QttySum >= totalQttySum / 2)
              break; 
            if (indiceAskOb < ob.getAsk().size() - 2 && indiceAskOb > 0) {
              if (((Order)ob.getAsk().elementAt(indiceAskOb)).getOrderPrice(0) == (
                (Order)ob.getAsk().elementAt(indiceAskOb + 1)).getOrderPrice(0))
                QttySum += ((Order)ob.getAsk().elementAt(indiceAskOb)).getMaxQtty(); 
            } else {
              indiceTailleOb++;
            } 
            indiceAskOb++;
          } 
          float orderPrice = (int)((((Order)ob.getBid().elementAt(indiceBidOb - 1)).getOrderPrice(1) + Math.random() * ((
            (Order)ob.getAsk().elementAt(indiceAskOb - 1)).getOrderPrice(0) - (
            (Order)ob.getBid().elementAt(indiceBidOb - 1)).getOrderPrice(1))) * 100.0D) / 100.0F;
          LimitOrder lo = new LimitOrder();
          lo.setEmitter(getLogin());
          lo.setInstitutionName(instit);
          lo.setPrice(orderPrice);
          lo.setQuantity((int)(Math.random() * 50.0D));
          lo.setSide(0);
          getRobotCore().send((NetworkWritable)lo);
          continue;
        } 
        if (ob.getBid().size() >= 1 && ob.getAsk().size() >= 1 && side == 1) {
          int indiceBidOb = 0;
          int indiceAskOb = 0;
          int indiceTailleOb = 0;
          int totalQttySum = 0;
          int QttySum = 0;
          while (indiceBidOb < ob.getBid().size()) {
            if (indiceTailleOb >= 5)
              break; 
            if (indiceBidOb < ob.getBid().size() - 2 && indiceBidOb > 0) {
              if (((Order)ob.getBid().elementAt(indiceBidOb)).getOrderPrice(1) == (
                (Order)ob.getBid().elementAt(indiceBidOb + 1)).getOrderPrice(1))
                totalQttySum += ((Order)ob.getBid().elementAt(indiceBidOb)).getMaxQtty(); 
            } else {
              indiceTailleOb++;
            } 
            indiceBidOb++;
          } 
          while (indiceAskOb < ob.getAsk().size()) {
            if (indiceTailleOb >= 5)
              break; 
            if (indiceAskOb < ob.getAsk().size() - 2 && indiceAskOb > 0 && (
              (Order)ob.getAsk().elementAt(indiceAskOb)).getOrderPrice(0) == (
              (Order)ob.getAsk().elementAt(indiceAskOb + 1)).getOrderPrice(0))
              totalQttySum += ((Order)ob.getAsk().elementAt(indiceAskOb)).getMaxQtty(); 
            indiceAskOb++;
          } 
          while (indiceBidOb < ob.getBid().size()) {
            if (indiceTailleOb >= 5 || 
              QttySum >= totalQttySum / 2)
              break; 
            if (indiceBidOb < ob.getBid().size() - 2 && indiceBidOb > 0) {
              if (((Order)ob.getBid().elementAt(indiceBidOb)).getOrderPrice(1) == (
                (Order)ob.getBid().elementAt(indiceBidOb + 1)).getOrderPrice(1))
                QttySum += ((Order)ob.getAsk().elementAt(indiceBidOb)).getMaxQtty(); 
            } else {
              indiceTailleOb++;
            } 
            indiceBidOb++;
          } 
          float orderPrice = (int)(((Order)ob.getAsk().elementAt(indiceAskOb - 1)).getOrderPrice(0) + Math.random() * ((
            (Order)ob.getBid().elementAt(indiceBidOb - 1)).getOrderPrice(1) - (
            (Order)ob.getAsk().elementAt(indiceAskOb - 1)).getOrderPrice(0))) * 100.0F / 100.0F;
          LimitOrder lo = new LimitOrder();
          lo.setEmitter(getLogin());
          lo.setInstitutionName(instit);
          lo.setPrice(orderPrice);
          lo.setQuantity(1 + (int)(Math.random() * 49.0D));
          lo.setSide(1);
          getRobotCore().send((NetworkWritable)lo);
        } 
      } 
    } 
  }
  
  public void objectReceived(Document xmlObject) {
    super.objectReceived(xmlObject);
  }
  
  protected String chooseName(int i) {
    return "NonDetectableZIT" + (i + 1);
  }
}
