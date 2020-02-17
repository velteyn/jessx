// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.operations;

import jessx.business.ClientInputPanel;
import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.business.OperationCreator;
import jessx.business.Order;

public class LimitOrder extends Order
{
    private static final String operationName = "Limit Order";
    private Integer quantity;
    private Float price;
    
    static {
        try {
            System.out.println("Loading LimitOrder...");
            OperationCreator.operationFactories.put("Limit Order", Class.forName("jessx.business.operations.LimitOrder"));
        }
        catch (ClassNotFoundException exception) {
            System.out.println("Unabled to locate the LimitOrder class. Reason: bad class name spelling.");
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
    
    @Override
    public String getOperationName() {
        return "Limit Order";
    }
    
    @Override
    public boolean initFromNetworkInput(final Element node) {
        if (!super.initFromNetworkInput(node)) {
            return false;
        }
        final Element limitOrder = node.getChild("LimitOrder");
        final String price = limitOrder.getAttributeValue("price");
        final String quantity = limitOrder.getAttributeValue("quantity");
        if (price == null || quantity == null) {
            Utils.logger.error("Invalid xml limitorder node: the attribute price or quantity is missing.");
            return false;
        }
        this.price = new Float(price);
        this.quantity = new Integer(quantity);
        return true;
    }
    
    @Override
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = super.prepareForNetworkOutput(pt);
        final Element limitOrder = new Element("LimitOrder");
        limitOrder.setAttribute("price", this.price.toString());
        limitOrder.setAttribute("quantity", this.quantity.toString());
        root.addContent(limitOrder);
        return root;
    }
    
    @Override
    public boolean isExecutingImmediately() {
        return false;
    }
    
    @Override
    public void stopImmediateExecution() {
    }
    
    @Override
    public boolean hasDefinedPrice() {
        return true;
    }
    
    @Override
    public boolean isVisibleInOrderbook() {
        return true;
    }
    
    @Override
    public void definePrice(final float price) {
    }
    
    @Override
    public boolean isVisibleInTheClientPanel() {
        return true;
    }
    
    @Override
    public ClientInputPanel getClientPanel(final String institution) {
        return new LimitOrderClientPanel(institution);
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
