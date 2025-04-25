// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import jessx.net.WarnForClient;
import jessx.server.net.NetworkCore;
import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;

public abstract class Order extends Operation implements NetworkWritable, NetworkReadable
{
    public static final int ASK = 0;
    public static final int BID = 1;
    public static final int ORDER_VALID = 1;
    public static final int NOT_ENOUGH_ASSET_FOR_ASK = 2;
    public static final int NOT_ENOUGH_CASH_FOR_ASK = 3;
    public static final int NOT_ENOUGH_CASH_FOR_BID = 4;
    public static final int NOT_ENOUGH_BIDS_IN_ORDERBOOK = 5;
    public static final int NOT_ENOUGH_ASKS_IN_ORDERBOOK = 6;
    private static int lastId;
    private long timestamp;
    private int side;
    private int id;
    
    static {
        Order.lastId = 0;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }
    
    public int getSide() {
        return this.side;
    }
    
    public void setSide(final int side) {
        this.side = side;
    }
    
    public float getTransactionFees(final float dealAmount, final float percentageCost, final float minimalCost) {
        return Math.max(percentageCost * dealAmount / 100.0f, minimalCost);
    }
    
    public float getDealAmount(final int side, final float dealAmount, final float percentageCost, final float minimalCost) {
        return side * dealAmount + this.getTransactionFees(dealAmount, percentageCost, minimalCost);
    }
    
    public int getId() {
        return this.id;
    }
    
    public void newId() {
        this.id = Order.lastId;
        ++Order.lastId;
    }
    
    public Order() {
        this.id = Order.lastId;
        ++Order.lastId;
    }
    
    public Order(final Element root) {
        final String idS = root.getChild("Order").getAttributeValue("id");
        if (idS != null) {
            this.id = Integer.parseInt(idS);
        }
        else {
            this.id = Order.lastId;
            ++Order.lastId;
        }
        this.initFromNetworkInput(root);
    }
    
    public abstract float getMaxPrice();
    
    public abstract float getMinPrice();
    
    public abstract int getMinQtty();
    
    public abstract int getMaxQtty();
    
    public abstract boolean isExecutingImmediately();
    
    public abstract boolean hasDefinedPrice();
    
    public abstract boolean isVisibleInOrderbook();
    
    public abstract void definePrice(final float p0);
    
    public abstract void stopImmediateExecution();
    
    @Override
    public boolean initFromNetworkInput(final Element root) {
        if (!super.initFromNetworkInput(root)) {
            return false;
        }
        final Element order = root.getChild("Order");
        final String idS = order.getAttributeValue("id");
        final String side = order.getAttributeValue("side");
        final String timestamp = order.getAttributeValue("timestamp");
        if (side == null || timestamp == null || idS == null) {
            Utils.logger.error("Invalid xml order node: one of the attribute is missing.");
            return false;
        }
        this.id = Integer.parseInt(idS);
        this.side = Integer.parseInt(side);
        this.timestamp = Long.parseLong(timestamp);
        return true;
    }
    
    @Override
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = super.prepareForNetworkOutput(pt);
        final Element order = new Element("Order");
        order.setAttribute("id", Integer.toString(this.id));
        order.setAttribute("side", Integer.toString(this.side));
        order.setAttribute("timestamp", Long.toString(this.timestamp));
        root.addContent(order);
        return root;
    }
    
    public void insertOrder(final Order order) {
        Utils.logger.info("insertOrder " + order.getId() + " " + order.getMaxQtty());
        final OrderBook orderbook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
        if (order.getSide() == 0) {
            Utils.logger.info("ask");
            if (!order.isExecutingImmediately() && orderbook.getAsk().size() > 0 && ((Order) orderbook.getAsk().elementAt(0)).getMinPrice() <= order.getMinPrice()) {
                this.insertOrderWithoutExec(order);
            }
            else if (orderbook.getBid().size() > 0) {
                if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
                    order.definePrice(((Order) orderbook.getBid().elementAt(0)).getMaxPrice());
                }
                this.executeOrders((Order) orderbook.getBid().elementAt(0), order);
                if (((Order) orderbook.getBid().elementAt(0)).getMaxQtty() == 0) {
                    orderbook.getBid().removeElementAt(0);
                    if (order.getMaxQtty() > 0) {
                        this.insertOrder(order);
                    }
                }
                else if (order.hasDefinedPrice() && order.getMaxQtty() > 0) {
                    if (order.isExecutingImmediately()) {
                        order.stopImmediateExecution();
                    }
                    orderbook.getAsk().insertElementAt(order, 0);
                }
            }
            else if (order.isVisibleInOrderbook() && order.hasDefinedPrice()) {
                this.insertOrderWithoutExec(order);
            }
            else {
                final String warnMessage = "This ask has not been fully passed because the orderbook is not deep enough";
                NetworkCore.getPlayer(order.getEmitter()).send(new WarnForClient(warnMessage));
            }
        }
        else {
            Utils.logger.info("bid");
            if (!order.isExecutingImmediately() && orderbook.getBid().size() > 0 && ((Order) orderbook.getBid().elementAt(0)).getMaxPrice() >= order.getMaxPrice()) {
                this.insertOrderWithoutExec(order);
            }
            else if (orderbook.getAsk().size() > 0) {
                if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
                    order.definePrice(((Order) orderbook.getAsk().elementAt(0)).getMinPrice());
                }
                this.executeOrders((Order) orderbook.getAsk().elementAt(0), order);
                if (((Order) orderbook.getAsk().elementAt(0)).getMaxQtty() == 0) {
                    orderbook.getAsk().removeElementAt(0);
                    if (order.getMaxQtty() > 0) {
                        this.insertOrder(order);
                    }
                }
                else if (order.hasDefinedPrice() && order.getMaxQtty() > 0) {
                    if (order.isExecutingImmediately()) {
                        order.stopImmediateExecution();
                    }
                    orderbook.getBid().insertElementAt(order, 0);
                }
            }
            else if (order.isVisibleInOrderbook() && order.hasDefinedPrice()) {
                this.insertOrderWithoutExec(order);
            }
            else {
                final String warnMessage = "This bid has not been fully passed because the orderbook is not deep enough";
                NetworkCore.getPlayer(order.getEmitter()).send(new WarnForClient(warnMessage));
            }
        }
    }
    
    private void insertOrderWithoutExec(final Order order) {
        Utils.logger.info("insertOrderWithoutExec " + order.getId() + " " + order.getMaxQtty());
        final OrderBook orderbook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
        int i = 0;
        Utils.logger.info("Beginning insertion. i=0. side: " + order.side + " - min price: " + order.getMinPrice() + " - max price " + order.getMaxPrice());
        if (order.side == 1) {
            while (orderbook.getBid().size() > i && order.getMaxPrice() <= ((Order) orderbook.getBid().elementAt(i)).getMaxPrice()) {
                Utils.logger.info("i=" + i + " - Current orderBook price: " + ((Order) orderbook.getBid().elementAt(i)).getMaxPrice());
                ++i;
            }
            Utils.logger.info("Insertion place: i = " + i + ((i < orderbook.getBid().size()) ? (" - Current orderBook price at this place: " + ((Order) orderbook.getBid().elementAt(i)).getMaxPrice()) : ""));
            orderbook.getBid().insertElementAt(order, i);
        }
        else {
            while (orderbook.getAsk().size() > i && order.getMinPrice() >= ((Order) orderbook.getAsk().elementAt(i)).getMinPrice()) {
                Utils.logger.info("i=" + i + " - Current orderBook price: " + ((Order) orderbook.getAsk().elementAt(i)).getMinPrice());
                ++i;
            }
            Utils.logger.info("Insertion place: i = " + i + ((i < orderbook.getAsk().size()) ? (" - Current orderBook price at this place: " + ((Order) orderbook.getAsk().elementAt(i)).getMinPrice()) : ""));
            orderbook.getAsk().insertElementAt(order, i);
        }
    }
    
    private void executeOrders(final Order orderInBook, final Order newOrder) {
        Utils.logger.info("-executeOrders (old) " + orderInBook.getId() + " " + orderInBook.getMaxQtty());
        Utils.logger.info("-executeOrders (new)" + newOrder.getId() + " " + newOrder.getMaxQtty());
        final int exchangeableQty = Math.min(orderInBook.getMaxQtty(), newOrder.getMaxQtty());
        final boolean askPossible = newOrder.getMinPrice() <= orderInBook.getMaxPrice();
        final boolean bidPossible = newOrder.getMaxPrice() >= orderInBook.getMinPrice();
        if (((newOrder.side == 0 && askPossible) || (newOrder.side == 1 && bidPossible)) && exchangeableQty > 0) {
            final float dealPrice = (newOrder.getSide() == 1) ? orderInBook.getMinPrice() : orderInBook.getMaxPrice();
            float maxBidPrice = 0.0f;
            if (newOrder.hasDefinedPrice()) {
                maxBidPrice = ((newOrder.getSide() == 1) ? newOrder.getMaxPrice() : orderInBook.getMaxPrice());
            }
            else {
                maxBidPrice = dealPrice;
            }
            final String client = newOrder.getEmitter();
            final String clientInBook = orderInBook.getEmitter();
            final String institutionName = newOrder.getInstitutionName();
            final String assetName = BusinessCore.getInstitution(institutionName).getAssetName();
            String buyer;
            String seller;
            String buyerOperation;
            String sellerOperation;
            if (newOrder.getSide() == 1) {
                buyer = client;
                seller = clientInBook;
                buyerOperation = newOrder.getOperationName();
                sellerOperation = orderInBook.getOperationName();
                final float sellerPercentageCost = BusinessCore.getInstitution(institutionName).getPercentageCost(sellerOperation);
                final float sellerMinimalCost = BusinessCore.getInstitution(institutionName).getMinimalCost(sellerOperation);
                NetworkCore.getPlayer(seller).getPortfolio().soldAssetsInOrderBook(assetName, dealPrice, exchangeableQty, institutionName, sellerPercentageCost, sellerMinimalCost);
                NetworkCore.sendToPlayer(NetworkCore.getPlayer(seller).getPortfolio(), seller);
            }
            else {
                buyer = clientInBook;
                seller = client;
                buyerOperation = orderInBook.getOperationName();
                sellerOperation = newOrder.getOperationName();
                final float buyerPercentageCost = BusinessCore.getInstitution(institutionName).getPercentageCost(buyerOperation);
                final float buyerMinimalCost = BusinessCore.getInstitution(institutionName).getMinimalCost(buyerOperation);
                NetworkCore.getPlayer(buyer).getPortfolio().boughtAssetsInOrderBook(assetName, dealPrice, exchangeableQty, institutionName, buyerPercentageCost, buyerMinimalCost);
                NetworkCore.sendToPlayer(NetworkCore.getPlayer(buyer).getPortfolio(), buyer);
            }
            final Deal deal = new Deal(newOrder.getInstitutionName(), dealPrice, exchangeableQty, newOrder.getTimestamp(), buyer, seller, maxBidPrice, buyerOperation, sellerOperation);
            NetworkCore.sendToAllPlayers(deal);
            final Element dealNode = new Element("Deal");
            deal.saveToXml(dealNode);
            NetworkCore.getLogManager().log(dealNode);
            newOrder.setRemainingOrder(exchangeableQty, dealPrice);
            orderInBook.setRemainingOrder(exchangeableQty, dealPrice);
        }
    }
    
    public float orderValidity(final Order order, final Portfolio portfolio) {
        final OrderBook orderBook = BusinessCore.getInstitution(order.getInstitutionName()).getOrderBook();
        final String assetName = BusinessCore.getInstitution(order.getInstitutionName()).getAssetName();
        final float percentageCost = BusinessCore.getInstitution(order.getInstitutionName()).getPercentageCost(order.getOperationName());
        final float minimalCost = BusinessCore.getInstitution(order.getInstitutionName()).getMinimalCost(order.getOperationName());
        final float nonInvestedCash = portfolio.getNonInvestedCash();
        final int nonInvestedOwnings = portfolio.getNonInvestedOwnings(assetName);
        boolean canBeAddedInTheOrderBook = order.hasDefinedPrice();
        float orderPrice = this.getOrderPrice(this.getSide());
        int orderRemainingQuantity;
        final int totalQuantity = orderRemainingQuantity = this.getMaxQtty();
        if (this.getSide() == 0) {
            if (orderRemainingQuantity > nonInvestedOwnings) {
                return 2.0f;
            }
            float immediateDealsAmountWithoutTF = 0.0f;
            for (int i = 0; i < orderBook.getBid().size(); ++i) {
                if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
                    canBeAddedInTheOrderBook = true;
                    orderPrice = ((Order) orderBook.getBid().elementAt(0)).getMaxPrice();
                }
                if (orderPrice <= ((Order) orderBook.getBid().elementAt(i)).getMaxPrice()) {
                    if (orderRemainingQuantity <= 0) {
                        portfolio.soldAssets(assetName, immediateDealsAmountWithoutTF - this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost), totalQuantity);
                        return 1.0f;
                    }
                    final int dealQuantity = Math.min(orderRemainingQuantity, ((Order) orderBook.getBid().elementAt(i)).getMaxQtty());
                    final float dealPrice = ((Order) orderBook.getBid().elementAt(i)).getMaxPrice();
                    immediateDealsAmountWithoutTF += dealPrice * dealQuantity;
                    orderRemainingQuantity -= dealQuantity;
                    if (this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost) > nonInvestedCash) {
                        return 3.0f;
                    }
                }
            }
            final float dealsCostWithTF = this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost);
            float newOrderCostWithTF = 0.0f;
            if (orderRemainingQuantity == 0) {
                if (totalQuantity != orderRemainingQuantity) {
                    portfolio.soldAssets(assetName, immediateDealsAmountWithoutTF - dealsCostWithTF, totalQuantity - orderRemainingQuantity);
                }
                return 1.0f;
            }
            if (!canBeAddedInTheOrderBook) {
                return 5.0f;
            }
            newOrderCostWithTF = orderRemainingQuantity * this.getTransactionFees(orderPrice, percentageCost, minimalCost);
            if (dealsCostWithTF + newOrderCostWithTF > nonInvestedCash) {
                return 3.0f;
            }
            if (totalQuantity != orderRemainingQuantity) {
                portfolio.soldAssets(assetName, immediateDealsAmountWithoutTF - dealsCostWithTF, totalQuantity - orderRemainingQuantity);
            }
            portfolio.wantedToBeSoldAssets(newOrderCostWithTF, orderRemainingQuantity, assetName, this.getInstitutionName());
            return 1.0f;
        }
        else {
            float immediateDealsAmountWithoutTF = 0.0f;
            for (int i = 0; i < orderBook.getAsk().size(); ++i) {
                if (!order.hasDefinedPrice() && order.isVisibleInOrderbook()) {
                    canBeAddedInTheOrderBook = true;
                    orderPrice = ((Order) orderBook.getAsk().elementAt(0)).getMinPrice();
                }
                if (orderPrice >= ((Order) orderBook.getAsk().elementAt(i)).getMinPrice()) {
                    if (orderRemainingQuantity <= 0) {
                        portfolio.boughtAssets(assetName, immediateDealsAmountWithoutTF + this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost), totalQuantity);
                        return 1.0f;
                    }
                    final int dealQuantity = Math.min(orderRemainingQuantity, ((Order) orderBook.getAsk().elementAt(i)).getMaxQtty());
                    final float dealPrice = ((Order) orderBook.getAsk().elementAt(i)).getMinPrice();
                    immediateDealsAmountWithoutTF += dealPrice * dealQuantity;
                    orderRemainingQuantity -= dealQuantity;
                    if (immediateDealsAmountWithoutTF + this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost) > nonInvestedCash) {
                        return 4.0f;
                    }
                }
            }
            final float dealsCostWithTF = immediateDealsAmountWithoutTF + this.getTransactionFees(immediateDealsAmountWithoutTF, percentageCost, minimalCost);
            float newOrderCostWithTF = 0.0f;
            if (orderRemainingQuantity == 0) {
                if (totalQuantity != orderRemainingQuantity) {
                    portfolio.boughtAssets(assetName, dealsCostWithTF, totalQuantity - orderRemainingQuantity);
                }
                return 1.0f;
            }
            if (!canBeAddedInTheOrderBook) {
                return 6.0f;
            }
            newOrderCostWithTF = orderRemainingQuantity * (orderPrice + this.getTransactionFees(orderPrice, percentageCost, minimalCost));
            if (dealsCostWithTF + newOrderCostWithTF > nonInvestedCash) {
                return 4.0f;
            }
            if (totalQuantity != orderRemainingQuantity) {
                portfolio.boughtAssets(assetName, dealsCostWithTF, totalQuantity - orderRemainingQuantity);
            }
            portfolio.wantedToBeBoughtAssets(newOrderCostWithTF, this.getInstitutionName());
            return 1.0f;
        }
    }
    
    public float getOrderPrice(final int side) {
        return (side == 1) ? this.getMaxPrice() : this.getMinPrice();
    }
    
    public abstract void setRemainingOrder(final int p0, final float p1);
}
