package jessx.business;

import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;
import jessx.net.WarnForClient;
import jessx.server.net.NetworkCore;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;

public abstract class Order extends Operation implements NetworkWritable, NetworkReadable {
  public static final int ASK = 0;
  
  public static final int BID = 1;
  
  public static final int ORDER_VALID = 1;
  
  public static final int NOT_ENOUGH_ASSET_FOR_ASK = 2;
  
  public static final int NOT_ENOUGH_CASH_FOR_ASK = 3;
  
  public static final int NOT_ENOUGH_CASH_FOR_BID = 4;
  
  public static final int NOT_ENOUGH_BIDS_IN_ORDERBOOK = 5;
  
  public static final int NOT_ENOUGH_ASKS_IN_ORDERBOOK = 6;
  
  private static int lastId = 0;
  
  private long timestamp;
  
  private int side;
  
  private int id;
  
  public long getTimestamp() {
    return this.timestamp;
  }
  
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
  }
  
  public int getSide() {
    return this.side;
  }
  
  public void setSide(int side) {
    this.side = side;
  }
  
  public float getTransactionFees(float dealAmount, float percentageCost, float minimalCost) {
    return Math.max(percentageCost * dealAmount / 100.0F, minimalCost);
  }
  
  public float getDealAmount(int side, float dealAmount, float percentageCost, float minimalCost) {
    return side * dealAmount + getTransactionFees(dealAmount, percentageCost, minimalCost);
  }
  
  public int getId() {
    return this.id;
  }
  
  public void newId() {
    this.id = lastId;
    lastId++;
  }
  
  public Order() {
    this.id = lastId;
    lastId++;
  }
  
  public Order(Element root) {
    String idS = root.getChild("Order").getAttributeValue("id");
    if (idS != null) {
      this.id = Integer.parseInt(idS);
    } else {
      this.id = lastId;
      lastId++;
    } 
    initFromNetworkInput(root);
  }
  
  public abstract float getMaxPrice();
  
  public abstract float getMinPrice();
  
  public abstract int getMinQtty();
  
  public abstract int getMaxQtty();
  
  public abstract boolean isExecutingImmediately();
  
  public abstract boolean hasDefinedPrice();
  
  public abstract boolean isVisibleInOrderbook();
  
  public abstract void definePrice(float paramFloat);
  
  public abstract void stopImmediateExecution();
  
  public boolean initFromNetworkInput(Element root) {
    if (!super.initFromNetworkInput(root))
      return false; 
    Element order = root.getChild("Order");
    String idS = order.getAttributeValue("id");
    String side = order.getAttributeValue("side");
    String timestamp = order.getAttributeValue("timestamp");
    if (side == null || timestamp == null || idS == null) {
      Utils.logger.error("Invalid xml order node: one of the attribute is missing.");
      return false;
    } 
    this.id = Integer.parseInt(idS);
    this.side = Integer.parseInt(side);
    this.timestamp = Long.parseLong(timestamp);
    return true;
  }
  
  public Element prepareForNetworkOutput(String pt) {
    Element root = super.prepareForNetworkOutput(pt);
    Element order = new Element("Order");
    order.setAttribute("id", Integer.toString(this.id));
    order.setAttribute("side", Integer.toString(this.side));
    order.setAttribute("timestamp", Long.toString(this.timestamp));
    root.addContent((Content)order);
    return root;
  }
  
  public void insertOrder(Order order) {
    Utils.logger.info("insertOrder " + order.getId() + " " + order.getMaxQtty());
    OrderBook orderbook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
    if (order.getSide() == 0) {
      Utils.logger.info("ask");
      if (!order.isExecutingImmediately() && orderbook.getAsk().size() > 0 && (
        (Order)orderbook.getAsk().elementAt(0)).getMinPrice() <= order.getMinPrice()) {
        insertOrderWithoutExec(order);
      } else if (orderbook.getBid().size() > 0) {
        if (!order.hasDefinedPrice() && order.isVisibleInOrderbook())
          order.definePrice(((Order)orderbook.getBid().elementAt(0)).getMaxPrice()); 
        executeOrders((Order) orderbook.getBid().elementAt(0), order);
        if (((Order)orderbook.getBid().elementAt(0)).getMaxQtty() == 0) {
          orderbook.getBid().removeElementAt(0);
          if (order.getMaxQtty() > 0)
            insertOrder(order); 
        } else if (order.hasDefinedPrice() && order.getMaxQtty() > 0) {
          if (order.isExecutingImmediately())
            order.stopImmediateExecution(); 
          orderbook.getAsk().insertElementAt(order, 0);
        } 
      } else if (order.isVisibleInOrderbook() && order.hasDefinedPrice()) {
        insertOrderWithoutExec(order);
      } else {
        String warnMessage = "This ask has not been fully passed because the orderbook is not deep enough";
        NetworkCore.getPlayer(order.getEmitter()).send((NetworkWritable)new WarnForClient(warnMessage));
      } 
    } else {
      Utils.logger.info("bid");
      if (!order.isExecutingImmediately() && orderbook.getBid().size() > 0 && (
        (Order)orderbook.getBid().elementAt(0)).getMaxPrice() >= order.getMaxPrice()) {
        insertOrderWithoutExec(order);
      } else if (orderbook.getAsk().size() > 0) {
        if (!order.hasDefinedPrice() && order.isVisibleInOrderbook())
          order.definePrice(((Order)orderbook.getAsk().elementAt(0)).getMinPrice()); 
				executeOrders((Order) orderbook.getAsk().elementAt(0), order);
        if (((Order)orderbook.getAsk().elementAt(0)).getMaxQtty() == 0) {
          orderbook.getAsk().removeElementAt(0);
          if (order.getMaxQtty() > 0)
            insertOrder(order); 
        } else if (order.hasDefinedPrice() && order.getMaxQtty() > 0) {
          if (order.isExecutingImmediately())
            order.stopImmediateExecution(); 
          orderbook.getBid().insertElementAt(order, 0);
        } 
      } else if (order.isVisibleInOrderbook() && order.hasDefinedPrice()) {
        insertOrderWithoutExec(order);
      } else {
        String warnMessage = "This bid has not been fully passed because the orderbook is not deep enough";
        NetworkCore.getPlayer(order.getEmitter()).send((NetworkWritable)new WarnForClient(warnMessage));
      } 
    } 
  }
  
  private void insertOrderWithoutExec(Order order) {
    Utils.logger.info("insertOrderWithoutExec " + order.getId() + " " + order.getMaxQtty());
    OrderBook orderbook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
    int i = 0;
    Utils.logger.info("Beginning insertion. i=0. side: " + order.side + " - min price: " + order.getMinPrice() + " - max price " + order.getMaxPrice());
    if (order.side == 1) {
      while (orderbook.getBid().size() > i && order.getMaxPrice() <= ((Order)orderbook.getBid().elementAt(i)).getMaxPrice()) {
        Utils.logger.info("i=" + i + " - Current orderBook price: " + ((Order)orderbook.getBid().elementAt(i)).getMaxPrice());
        i++;
      } 
      Utils.logger.info("Insertion place: i = " + i + ((i < orderbook.getBid().size()) ? (" - Current orderBook price at this place: " + ((Order)orderbook.getBid().elementAt(i)).getMaxPrice()) : ""));
      orderbook.getBid().insertElementAt(order, i);
    } else {
      while (orderbook.getAsk().size() > i && order.getMinPrice() >= ((Order)orderbook.getAsk().elementAt(i)).getMinPrice()) {
        Utils.logger.info("i=" + i + " - Current orderBook price: " + ((Order)orderbook.getAsk().elementAt(i)).getMinPrice());
        i++;
      } 
      Utils.logger.info("Insertion place: i = " + i + ((i < orderbook.getAsk().size()) ? (" - Current orderBook price at this place: " + ((Order)orderbook.getAsk().elementAt(i)).getMinPrice()) : ""));
      orderbook.getAsk().insertElementAt(order, i);
    } 
  }
  
  private void executeOrders(Order orderInBook, Order newOrder) {
    Utils.logger.info("-executeOrders (old) " + orderInBook.getId() + " " + orderInBook.getMaxQtty());
    Utils.logger.info("-executeOrders (new)" + newOrder.getId() + " " + newOrder.getMaxQtty());
    int exchangeableQty = Math.min(orderInBook.getMaxQtty(), 
        newOrder.getMaxQtty());
    boolean askPossible = (newOrder.getMinPrice() <= orderInBook.getMaxPrice());
    boolean bidPossible = (newOrder.getMaxPrice() >= orderInBook.getMinPrice());
    if (((newOrder.side == 0 && askPossible) || (
      newOrder.side == 1 && bidPossible)) && 
      exchangeableQty > 0) {
      String buyer, seller, buyerOperation, sellerOperation;
      float dealPrice = (newOrder.getSide() == 1) ? 
        orderInBook.getMinPrice() : orderInBook.getMaxPrice();
      float maxBidPrice = 0.0F;
      if (newOrder.hasDefinedPrice()) {
        maxBidPrice = (newOrder.getSide() == 1) ? 
          newOrder.getMaxPrice() : orderInBook.getMaxPrice();
      } else {
        maxBidPrice = dealPrice;
      } 
      String client = newOrder.getEmitter();
      String clientInBook = orderInBook.getEmitter();
      String institutionName = newOrder.getInstitutionName();
      String assetName = BusinessCore.getInstitution(institutionName).getAssetName();
      if (newOrder.getSide() == 1) {
        buyer = client;
        seller = clientInBook;
        buyerOperation = newOrder.getOperationName();
        sellerOperation = orderInBook.getOperationName();
        float sellerPercentageCost = BusinessCore.getInstitution(institutionName).getPercentageCost(sellerOperation);
        float sellerMinimalCost = BusinessCore.getInstitution(institutionName).getMinimalCost(sellerOperation);
        NetworkCore.getPlayer(seller).getPortfolio().soldAssetsInOrderBook(assetName, 
            dealPrice, 
            exchangeableQty, 
            institutionName, 
            sellerPercentageCost, 
            sellerMinimalCost);
        NetworkCore.sendToPlayer(NetworkCore.getPlayer(seller).getPortfolio(), seller);
      } else {
        buyer = clientInBook;
        seller = client;
        buyerOperation = orderInBook.getOperationName();
        sellerOperation = newOrder.getOperationName();
        float buyerPercentageCost = BusinessCore.getInstitution(institutionName).getPercentageCost(buyerOperation);
        float buyerMinimalCost = BusinessCore.getInstitution(institutionName).getMinimalCost(buyerOperation);
        NetworkCore.getPlayer(buyer).getPortfolio().boughtAssetsInOrderBook(assetName, 
            dealPrice, 
            exchangeableQty, 
            institutionName, 
            buyerPercentageCost, 
            buyerMinimalCost);
        NetworkCore.sendToPlayer(NetworkCore.getPlayer(buyer).getPortfolio(), buyer);
      } 
      Deal deal = new Deal(newOrder.getInstitutionName(), 
          dealPrice, 
          exchangeableQty, 
          newOrder.getTimestamp(), 
          buyer, seller, maxBidPrice, buyerOperation, sellerOperation);
      NetworkCore.sendToAllPlayers(deal);
      Element dealNode = new Element("Deal");
      deal.saveToXml(dealNode);
      NetworkCore.getLogManager().log(dealNode);
      newOrder.setRemainingOrder(exchangeableQty, dealPrice);
      orderInBook.setRemainingOrder(exchangeableQty, dealPrice);
    } 
  }
  
  public float orderValidity(Order order, Portfolio portfolio) {
    OrderBook orderBook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
    String assetName = BusinessCore.getInstitution(order.getInstitutionName()).getAssetName();
    float percentageCost = BusinessCore.getInstitution(order.getInstitutionName()).getPercentageCost(order.getOperationName());
    float minimalCost = BusinessCore.getInstitution(order.getInstitutionName()).getMinimalCost(order.getOperationName());
    float nonInvestedCash = portfolio.getNonInvestedCash();
    int nonInvestedOwnings = portfolio.getNonInvestedOwnings(assetName);
    boolean canBeAddedInTheOrderBook = order.hasDefinedPrice();
    float orderPrice = getOrderPrice(getSide());
    int totalQuantity = getMaxQtty();
    int orderRemainingQuantity = totalQuantity;
    if (getSide() == 0) {
      if (orderRemainingQuantity > nonInvestedOwnings)
        return 2.0F; 
      float f1 = 0.0F;
      for (int j = 0; j < orderBook.getBid().size(); j++) {
        if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
          canBeAddedInTheOrderBook = true;
          orderPrice = ((Order)orderBook.getBid().elementAt(0)).getMaxPrice();
        } 
        if (orderPrice <= ((Order)orderBook.getBid().elementAt(j)).getMaxPrice())
          if (orderRemainingQuantity > 0) {
            int dealQuantity = Math.min(orderRemainingQuantity, (
                (Order)orderBook.getBid().elementAt(j)).getMaxQtty());
            float dealPrice = ((Order)orderBook.getBid().elementAt(j)).getMaxPrice();
            f1 += dealPrice * dealQuantity;
            orderRemainingQuantity -= dealQuantity;
            if (getTransactionFees(f1, percentageCost, minimalCost) > nonInvestedCash)
              return 3.0F; 
          } else {
            portfolio.soldAssets(assetName, f1 - getTransactionFees(f1, percentageCost, minimalCost), totalQuantity);
            return 1.0F;
          }  
      } 
      float f2 = getTransactionFees(f1, percentageCost, minimalCost);
      float f3 = 0.0F;
      if (orderRemainingQuantity == 0) {
        if (totalQuantity != orderRemainingQuantity)
          portfolio.soldAssets(assetName, f1 - f2, totalQuantity - orderRemainingQuantity); 
        return 1.0F;
      } 
      if (canBeAddedInTheOrderBook) {
        f3 = orderRemainingQuantity * getTransactionFees(orderPrice, percentageCost, minimalCost);
        if (f2 + f3 > nonInvestedCash)
          return 3.0F; 
        if (totalQuantity != orderRemainingQuantity)
          portfolio.soldAssets(assetName, f1 - f2, totalQuantity - orderRemainingQuantity); 
        portfolio.wantedToBeSoldAssets(f3, orderRemainingQuantity, assetName, getInstitutionName());
        return 1.0F;
      } 
      return 5.0F;
    } 
    float immediateDealsAmountWithoutTF = 0.0F;
    for (int i = 0; i < orderBook.getAsk().size(); i++) {
      if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
        canBeAddedInTheOrderBook = true;
        orderPrice = ((Order)orderBook.getAsk().elementAt(0)).getMinPrice();
      } 
      if (orderPrice >= ((Order)orderBook.getAsk().elementAt(i)).getMinPrice())
        if (orderRemainingQuantity > 0) {
          int dealQuantity = Math.min(orderRemainingQuantity, (
              (Order)orderBook.getAsk().elementAt(i))
              .getMaxQtty());
          float dealPrice = ((Order)orderBook.getAsk().elementAt(i)).getMinPrice();
          immediateDealsAmountWithoutTF += dealPrice * dealQuantity;
          orderRemainingQuantity -= dealQuantity;
          if (immediateDealsAmountWithoutTF + 
            getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, 
              minimalCost) > nonInvestedCash)
            return 4.0F; 
        } else {
          portfolio.boughtAssets(assetName, 
              immediateDealsAmountWithoutTF + 
              getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost), totalQuantity);
          return 1.0F;
        }  
    } 
    float dealsCostWithTF = immediateDealsAmountWithoutTF + getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost);
    float newOrderCostWithTF = 0.0F;
    if (orderRemainingQuantity == 0) {
      if (totalQuantity != orderRemainingQuantity)
        portfolio.boughtAssets(assetName, dealsCostWithTF, totalQuantity - orderRemainingQuantity); 
      return 1.0F;
    } 
    if (canBeAddedInTheOrderBook) {
      newOrderCostWithTF = orderRemainingQuantity * (orderPrice + getTransactionFees(orderPrice, percentageCost, minimalCost));
      if (dealsCostWithTF + newOrderCostWithTF > nonInvestedCash)
        return 4.0F; 
      if (totalQuantity != orderRemainingQuantity)
        portfolio.boughtAssets(assetName, dealsCostWithTF, totalQuantity - orderRemainingQuantity); 
      portfolio.wantedToBeBoughtAssets(newOrderCostWithTF, getInstitutionName());
      return 1.0F;
    } 
    return 6.0F;
  }
  
  public float getOrderPrice(int side) {
    return (side == 1) ? getMaxPrice() : getMinPrice();
  }
  
  public abstract void setRemainingOrder(int paramInt, float paramFloat);
}
