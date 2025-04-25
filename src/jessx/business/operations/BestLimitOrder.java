// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.operations;

import jessx.business.ClientInputPanel;
import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.business.OperationCreator;
import jessx.business.Order;

public class BestLimitOrder extends Order
{
    private static final String operationName = "Best Limit Order";
    private Integer quantity;
    private Float price;
    private boolean priceDefined;
    
    static {
        try {
            System.out.println("Loading BestLimitOrder...");
            OperationCreator.operationFactories.put("Best Limit Order", Class.forName("jessx.business.operations.BestLimitOrder"));
        }
        catch (final ClassNotFoundException exception) {
            System.out.println("Unabled to locate the BestLimitOrder class. Reason: bad class name spelling.");
            exception.printStackTrace();
        }
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public float getPrice() {
        return this.price;
    }
    
    public void setPrice(final float price) {
        this.price = new Float(price);
    }
    
    public void setQuantity(final int qtty) {
        this.quantity = new Integer(qtty);
    }
    
    @Override
    public float getOperationCost(final float percentCost, final float minimalCost) {
        return Math.max(minimalCost, this.quantity * this.price * percentCost / 100.0f);
    }
    
    public BestLimitOrder() {
        this.priceDefined = false;
    }
    
    @Override
    public String getOperationName() {
        return "Best Limit Order";
    }
    
    @Override
    public boolean initFromNetworkInput(final Element node) {
        if (!super.initFromNetworkInput(node)) {
            return false;
        }
        final Element bestLimitOrder = node.getChild("BestLimitOrder");
        final String price = bestLimitOrder.getAttributeValue("price");
        final String quantity = bestLimitOrder.getAttributeValue("quantity");
        if (price == null || quantity == null) {
            Utils.logger.error("Invalid xml bestlimitorder node: the attribute price or quantity is missing.");
            return false;
        }
        this.price = new Float(price);
        this.quantity = new Integer(quantity);
        return true;
    }
    
    @Override
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = super.prepareForNetworkOutput(pt);
        final Element bestLimitOrder = new Element("BestLimitOrder");
        bestLimitOrder.setAttribute("price", this.price.toString());
        bestLimitOrder.setAttribute("quantity", this.quantity.toString());
        root.addContent(bestLimitOrder);
        return root;
    }
    
    @Override
    public boolean isExecutingImmediately() {
        return true;
    }
    
    @Override
    public void stopImmediateExecution() {
    }
    
    @Override
    public boolean hasDefinedPrice() {
        return this.priceDefined;
    }
    
    @Override
    public boolean isVisibleInOrderbook() {
        return true;
    }
    
    @Override
    public void definePrice(final float price) {
        this.priceDefined = true;
        this.setPrice(price);
    }
    
    @Override
    public boolean isVisibleInTheClientPanel() {
        return true;
    }
    
    @Override
    public ClientInputPanel getClientPanel(final String institution) {
        return new BestLimitOrderClientPanel(institution);
    }
    
    @Override
    public int getMinQtty() {
        return 1;
    }
    
    @Override
    public int getMaxQtty() {
        return this.getQuantity();
    }
    
    @Override
    public float getMinPrice() {
        return (this.getSide() == 1) ? 0.0f : this.getPrice();
    }
    
    @Override
    public float getMaxPrice() {
        return (this.getSide() == 1) ? this.getPrice() : Float.MAX_VALUE;
    }
    
    @Override
    public void setRemainingOrder(final int quantity, final float price) {
        this.quantity = new Integer(this.quantity - quantity);
    }
}
