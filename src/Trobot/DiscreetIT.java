package Trobot;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import jessx.business.Institution;
import jessx.business.Order;
import jessx.business.OrderBook;
import jessx.business.operations.LimitOrder;
import jessx.net.NetworkWritable;
import org.jdom.Document;

public class DiscreetIT extends Animator {
  public DiscreetIT(int name, double InactivityPercentage) {
    super(name, InactivityPercentage);
  }
  
  protected void MyAct() {
    Iterator<String> iterInstit = getRobotCore().getInstitutions().keySet().iterator();
    while (iterInstit.hasNext()) {
      String institname = iterInstit.next();
      Institution instit = getRobotCore().getInstitution(institname);
      if ((new Date()).getTime() - ((Date)getDatesLastOrder().get(institname)).getTime() > NextWakeUp(institname) && (
        (LinkedList)getOrderBooks().get(institname)).size() > 0) {
        OrderBook ob = ((LinkedList<OrderBook>)getOrderBooks().get(institname)).getLast();
        float DifferentInstitutions = getRobotCore().getInstitutions().size();
        System.out.println("(Initialisation Ordre) Nombre d'institutions diff"+ DifferentInstitutions);
        String assetname = instit.getAssetName();
        System.out.println("(Initialisation Ordre) Assetname:" + assetname);
        int assets = getRobotCore().getPortfolio().getOwnings(assetname);
        System.out.println("(Initialisation Ordre) Nombre d'Assets:" + assets);
        float cash = getRobotCore().getPortfolio().getCash();
        System.out.println("(Initialisation Ordre) Cash:" + cash);
        int side = (int)Math.round(Math.random());
        int i = 0;
        if (ob.getAsk().size() >= 1 && ob.getBid().size() >= 1 && side == 0) {
          System.out.println("Dde l'itpour le calcul de l'assetValue:");
          Iterator<String> iterAssets = getRobotCore().getInstitutions().keySet().iterator();
          int AssetsValue = 0;
          while (iterAssets.hasNext()) {
            String assetsnameAV = iterAssets.next();
            Institution institAV = getRobotCore().getInstitution(institname);
            String assetnameAV = institAV.getAssetName();
            if (((LinkedList)getOrderBooks().get(assetsnameAV)).size() > 0) {
              OrderBook obAv = ((LinkedList<OrderBook>)getOrderBooks().get(assetsnameAV)).getLast();
              System.out.println("AV Assetname:" + assetsnameAV);
              System.out.println("AV Quantitd'assets:" + assets);
              if (obAv.getAsk().size() >= 1 && obAv.getBid().size() >= 1) {
                int indiceAskAv = 0;
                int indiceBidAv = 0;
                int indiceTailleAv = 0;
                int totalQttySumAv = 0;
                int QttySumAv = 0;
                while (indiceAskAv < obAv.getAsk().size()) {
                  if (indiceTailleAv >= 5)
                    break; 
                  if (indiceAskAv < obAv.getAsk().size() - 2 && indiceAskAv > 0) {
                    if (((Order)obAv.getAsk().elementAt(indiceAskAv)).getOrderPrice(0) == (
                      (Order)obAv.getAsk().elementAt(indiceAskAv + 1)).getOrderPrice(0))
                      totalQttySumAv += ((Order)obAv.getAsk().elementAt(indiceAskAv)).getMaxQtty(); 
                  } else {
                    indiceTailleAv++;
                  } 
                  indiceAskAv++;
                } 
                while (indiceBidAv < obAv.getBid().size()) {
                  if (indiceTailleAv >= 5)
                    break; 
                  if (indiceBidAv < obAv.getBid().size() - 2 && indiceBidAv > 0 && (
                    (Order)obAv.getBid().elementAt(indiceBidAv)).getOrderPrice(1) == (
                    (Order)obAv.getBid().elementAt(indiceBidAv + 1)).getOrderPrice(1))
                    totalQttySumAv += ((Order)obAv.getBid().elementAt(indiceBidAv)).getMaxQtty(); 
                  indiceBidAv++;
                } 
                while (indiceAskAv < obAv.getAsk().size()) {
                  if (indiceTailleAv >= 5 || 
                    QttySumAv >= totalQttySumAv / 2)
                    break; 
                  if (indiceAskAv < obAv.getAsk().size() - 2 && indiceAskAv > 0) {
                    if (((Order)obAv.getAsk().elementAt(indiceAskAv)).getOrderPrice(0) == (
                      (Order)obAv.getAsk().elementAt(indiceAskAv + 1)).getOrderPrice(0))
                      QttySumAv += ((Order)obAv.getAsk().elementAt(indiceAskAv)).getMaxQtty(); 
                  } else {
                    indiceTailleAv++;
                  } 
                  indiceAskAv++;
                } 
                float orderPriceAV = (int)((((Order)obAv.getBid().elementAt(indiceBidAv - 1)).getOrderPrice(1) + Math.random() * ((
                  (Order)obAv.getAsk().elementAt(indiceAskAv - 1)).getOrderPrice(0) - (
                  (Order)obAv.getBid().elementAt(indiceBidAv - 1)).getOrderPrice(1))) * 100.0D) / 100.0F;
                System.out.println("AV OrderPrice:" + orderPriceAV);
                AssetsValue = (int)(AssetsValue + orderPriceAV * getRobotCore().getPortfolio().getOwnings(assetnameAV));
                i++;
                System.out.println("AV Valeur des Assets:" + AssetsValue);
              } 
            } 
          } 
          System.out.println("Fin de l'itde calcul de l'AssetValue");
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
          float orderPrice = (int)(((Order)ob.getBid().elementAt(indiceBidOb - 1)).getOrderPrice(1) * 100.0F) / 100.0F;
          int QA = 0;
          System.out.println("(Ordre) (QuantitNombre d'assets:" + assets);
          System.out.println("(Ordre) (QuantitOrderPrice:" + orderPrice);
          System.out.println("(Ordre) (QuantitAssetsValue:" + AssetsValue);
          System.out.println("(Ordre) (QuantitCash:" + cash);
          System.out.println("(Ordre) (QuantitNombre d'institutions:" + DifferentInstitutions);
          System.out.println("(Ordre) (QuantitPourcentage : (assets*orderPrice/(AssetsValue+cash):" + (assets * orderPrice / (AssetsValue + cash)));
          System.out.println("(Ordre) (QuantitPourcentage voulu : 1/(DifferentInstitutions):" + (1.0F / DifferentInstitutions));
          if (assets * orderPrice / (AssetsValue + cash) > 1.0F / (DifferentInstitutions + 1.0F) && i == DifferentInstitutions) {
            QA = (int)Math.floor((int)Math.abs(assets - (AssetsValue + cash) / orderPrice * (DifferentInstitutions + 1.0F)));
            if (QA > assets * 25 / 100)
              QA = assets * 25 / 100; 
          } else {
            QA = 0;
          } 
          System.out.println("QA:" + QA);
          LimitOrder lo = new LimitOrder();
          lo.setEmitter(getLogin());
          lo.setInstitutionName(institname);
          lo.setPrice(orderPrice);
          lo.setQuantity(QA);
          lo.setSide(0);
          getRobotCore().send((NetworkWritable)lo);
          continue;
        } 
        if (ob.getBid().size() >= 1 && ob.getAsk().size() >= 1 && side == 1) {
          Iterator<String> iterAssets = getRobotCore().getInstitutions().keySet().iterator();
          int AssetsValue = 0;
          while (iterAssets.hasNext()) {
            String assetsnameAV = iterAssets.next();
            Institution institAV = getRobotCore().getInstitution(institname);
            String assetnameAV = institAV.getAssetName();
            if (((LinkedList)getOrderBooks().get(assetsnameAV)).size() > 0) {
              OrderBook obAv = ((LinkedList<OrderBook>)getOrderBooks().get(assetsnameAV)).getLast();
              System.out.println("AV AssetName:" + assetsnameAV);
              System.out.println("AV Quantitd'assets:" + assets);
              if (obAv.getBid().size() >= 1 && obAv.getAsk().size() >= 1) {
                int indiceBidAv = 0;
                int indiceAskAv = 0;
                int indiceTailleAv = 0;
                int totalQttySumAv = 0;
                int QttySumAv = 0;
                while (indiceBidAv < obAv.getBid().size()) {
                  if (indiceTailleAv >= 5)
                    break; 
                  if (indiceBidAv < obAv.getBid().size() - 2 && indiceBidAv > 0) {
                    if (((Order)obAv.getBid().elementAt(indiceBidAv)).getOrderPrice(1) == (
                      (Order)obAv.getBid().elementAt(indiceBidAv + 1)).getOrderPrice(1))
                      totalQttySumAv += ((Order)obAv.getBid().elementAt(indiceBidAv)).getMaxQtty(); 
                  } else {
                    indiceTailleAv++;
                  } 
                  indiceBidAv++;
                } 
                while (indiceAskAv < obAv.getAsk().size()) {
                  if (indiceTailleAv >= 5)
                    break; 
                  if (indiceAskAv < obAv.getAsk().size() - 2 && indiceAskAv > 0 && (
                    (Order)obAv.getAsk().elementAt(indiceAskAv)).getOrderPrice(0) == (
                    (Order)obAv.getAsk().elementAt(indiceAskAv + 1)).getOrderPrice(0))
                    totalQttySumAv += ((Order)obAv.getAsk().elementAt(indiceAskAv)).getMaxQtty(); 
                  indiceAskAv++;
                } 
                while (indiceBidAv < obAv.getBid().size()) {
                  if (indiceTailleAv >= 5 || 
                    QttySumAv >= totalQttySumAv / 2)
                    break; 
                  if (indiceBidAv < obAv.getBid().size() - 2 && indiceBidAv > 0) {
                    if (((Order)obAv.getBid().elementAt(indiceBidAv)).getOrderPrice(1) == (
                      (Order)obAv.getBid().elementAt(indiceBidAv + 1)).getOrderPrice(1))
                      QttySumAv += ((Order)obAv.getAsk().elementAt(indiceBidAv)).getMaxQtty(); 
                  } else {
                    indiceTailleAv++;
                  } 
                  indiceBidAv++;
                } 
                float orderPriceAV = (int)((((Order)obAv.getBid().elementAt(indiceBidAv - 1)).getOrderPrice(1) + Math.random() * ((
                  (Order)obAv.getAsk().elementAt(indiceAskAv - 1)).getOrderPrice(0) - (
                  (Order)obAv.getBid().elementAt(indiceBidAv - 1)).getOrderPrice(1))) * 100.0D) / 100.0F;
                System.out.println("OrderPrice:" + orderPriceAV);
                AssetsValue = (int)(AssetsValue + orderPriceAV * getRobotCore().getPortfolio().getOwnings(assetnameAV));
                i++;
                System.out.println("Valeur des Assets:" + AssetsValue);
              } 
            } 
          } 
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
          float orderPrice = (int)((Order)ob.getAsk().elementAt(indiceAskOb - 1)).getOrderPrice(0) * 100.0F / 100.0F;
          int QB = 0;
          System.out.println("Nombre d'Assets:" + assets);
          System.out.println("OrderPrice:" + orderPrice);
          System.out.println("AssetsValue:" + AssetsValue);
          System.out.println("Cash:" + cash);
          System.out.println("n:" + DifferentInstitutions);
          System.out.println("(Ordre) (QuantitPourcentage : (assets*orderPrice/(AssetsValue+cash):" + (assets * orderPrice / (AssetsValue + cash)));
          System.out.println("(Ordre) (QuantitPourcentage voulu : 1/(DifferentInstitutions):" + (1.0F / DifferentInstitutions));
          if (assets == 0) {
            QB = (int)Math.floor((int)Math.abs(cash / orderPrice * (DifferentInstitutions + 1.0F)));
          } else if (assets * orderPrice / (AssetsValue + cash) < 1.0F / (DifferentInstitutions + 1.0F) && i == DifferentInstitutions) {
            QB = (int)Math.floor(((int)Math.abs((AssetsValue + cash) / orderPrice * (DifferentInstitutions + 1.0F)) - assets));
            if (QB > assets * 25 / 100)
              QB = assets * 25 / 100; 
          } else {
            QB = 0;
          } 
          System.out.println("QB:" + QB);
          LimitOrder lo = new LimitOrder();
          lo.setEmitter(getLogin());
          lo.setInstitutionName(institname);
          lo.setPrice(orderPrice);
          lo.setQuantity(QB);
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
    return "NonDetectableIT" + (i + 1);
  }
}
