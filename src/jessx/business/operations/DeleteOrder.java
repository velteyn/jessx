// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.operations;

import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.business.ClientInputPanel;
import jessx.business.BusinessCore;
import jessx.business.Portfolio;
import jessx.business.Order;
import jessx.business.OperationCreator;
import jessx.business.Operation;

public class DeleteOrder extends Operation
{
    private static final String operationName = "Delete Order";
    public static final int DELETE_ORDER_VALID = 1;
    public static final int NOT_ENOUGH_CASH_FOR_DELETE_ORDER = 2;
    private int orderId;
    private Integer quantity;
    private Float price;
    
    static {
        try {
            System.out.println("Loading DeleteOrder...");
            OperationCreator.operationFactories.put("Delete Order", Class.forName("jessx.business.operations.DeleteOrder"));
        }
        catch (ClassNotFoundException exception) {
            System.out.println("Unabled to locate the DeleteOrder class. Reason: probably a bad class name spelling.");
            exception.printStackTrace();
        }
    }
    
    private void setOrderId(final int orderId) {
        this.orderId = orderId;
    }
    
    public int getOrderId() {
        return this.orderId;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public float getPrice() {
        return this.price;
    }
    
    @Override
    public float getOperationCost(final float percentCost, final float minimalCost) {
        return Math.max(minimalCost, this.quantity * this.price * percentCost / 100.0f);
    }
    
    public float deleteOrderValidity(final Order order, final Portfolio portfolio) {
        final String assetName = BusinessCore.getInstitution(order.getInstitutionName()).getAssetName();
        final String institutionName = order.getInstitutionName();
        final float orderPercentageCost = BusinessCore.getInstitution(order.getInstitutionName()).getPercentageCost(order.getOperationName());
        final float orderMinimalCost = BusinessCore.getInstitution(order.getInstitutionName()).getMinimalCost(order.getOperationName());
        final float deletionPercentageCost = BusinessCore.getInstitution(order.getInstitutionName()).getPercentageCost("Delete Order");
        final float deletionMinimalCost = BusinessCore.getInstitution(order.getInstitutionName()).getMinimalCost("Delete Order");
        this.price = new Float(order.getOrderPrice(order.getSide()));
        this.quantity = new Integer(order.getMaxQtty());
        if (this.getOperationCost(deletionPercentageCost, deletionMinimalCost) <= portfolio.getNonInvestedCash()) {
            portfolio.cancelOrder(order.getSide(), order.getOrderPrice(order.getSide()), order.getMaxQtty(), assetName, institutionName, orderPercentageCost, orderMinimalCost, deletionPercentageCost, deletionMinimalCost);
            return 1.0f;
        }
        return 2.0f;
    }
    
    public DeleteOrder(final int orderId) {
        this.setOrderId(orderId);
    }
    
    public DeleteOrder() {
    }
    
    @Override
    public boolean isVisibleInTheClientPanel() {
        return false;
    }
    
    @Override
    public String getOperationName() {
        return "Delete Order";
    }
    
    @Override
    public ClientInputPanel getClientPanel(final String institution) {
        return null;
    }
    
    @Override
    public boolean initFromNetworkInput(final Element node) {
        if (!super.initFromNetworkInput(node)) {
            return false;
        }
        final Element deleteOrder = node.getChild("DeleteOrder");
        final String orderId = deleteOrder.getAttributeValue("orderId");
        if (orderId == null) {
            Utils.logger.error("Invalid xml deleteorder node: the attribute orderId is missing.");
            return false;
        }
        this.orderId = Integer.parseInt(orderId);
        return true;
    }
    
    @Override
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = super.prepareForNetworkOutput(pt);
        final Element deleteOrder = new Element("DeleteOrder");
        deleteOrder.setAttribute("orderId", Integer.toString(this.orderId));
        root.addContent(deleteOrder);
        return root;
    }
}
