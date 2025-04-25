// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Content;
import jessx.utils.Utils;
import org.jdom.Element;
import jessx.utils.XmlExportable;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;

public class Deal implements NetworkWritable, NetworkReadable, XmlExportable
{
    private float price;
    private String institution;
    private int quantity;
    private long timestamp;
    private String buyer;
    private String seller;
    private String buyerOperation;
    private String sellerOperation;
    private float maxBidPrice;
    
    public float getDealPrice() {
        return this.price;
    }
    
    public float getMaxBidPrice() {
        return this.maxBidPrice;
    }
    
    public String getDealInstitution() {
        return this.institution;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public long getTimestamp() {
        return this.timestamp;
    }
    
    public String getBuyer() {
        return this.buyer;
    }
    
    public String getSeller() {
        return this.seller;
    }
    
    public String getBuyerOperation() {
        return this.buyerOperation;
    }
    
    public String getSellerOperation() {
        return this.sellerOperation;
    }
    
    public Deal(final String institution, final float price, final int quantity, final long timestamp, final String buyer, final String seller, final float maxBidPrice, final String buyerOperation, final String sellerOperation) {
        this.institution = institution;
        this.price = price;
        this.quantity = quantity;
        this.timestamp = timestamp;
        this.buyer = buyer;
        this.seller = seller;
        this.maxBidPrice = maxBidPrice;
        this.buyerOperation = buyerOperation;
        this.sellerOperation = sellerOperation;
    }
    
    public boolean initFromNetworkInput(final Element root) {
        final String timestamp = root.getAttributeValue("timestamp");
        final String institution = root.getAttributeValue("institution");
        final String price = root.getAttributeValue("price");
        final String quantity = root.getAttributeValue("quantity");
        final String buyer = root.getAttributeValue("buyer");
        final String seller = root.getAttributeValue("seller");
        final String maxBidPrice = root.getAttributeValue("maxBidPrice");
        final String buyerOperation = root.getAttributeValue("buyerOperation");
        final String sellerOperation = root.getAttributeValue("sellerOperation");
        if (timestamp == null || institution == null || price == null || quantity == null || maxBidPrice == null || buyer == null || seller == null || buyerOperation == null || sellerOperation == null) {
            Utils.logger.error("Invalid deal xml node: one of the attribute is missing.");
            return false;
        }
        this.timestamp = Long.parseLong(timestamp);
        this.institution = institution;
        this.price = Float.parseFloat(price);
        this.quantity = Integer.parseInt(quantity);
        this.buyer = buyer;
        this.seller = seller;
        this.maxBidPrice = Float.parseFloat(maxBidPrice);
        this.buyerOperation = buyerOperation;
        this.sellerOperation = sellerOperation;
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = new Element("Deal");
        root.setAttribute("timestamp", Long.toString(this.timestamp)).setAttribute("institution", this.institution).setAttribute("price", Float.toString(this.price)).setAttribute("quantity", Integer.toString(this.quantity)).setAttribute("buyer", this.buyer).setAttribute("seller", this.seller).setAttribute("maxBidPrice", Float.toString(this.maxBidPrice)).setAttribute("buyerOperation", this.buyerOperation).setAttribute("sellerOperation", this.sellerOperation);
        return root;
    }
    
    public void saveToXml(final Element node) {
        final Element dNode = new Element("Deal");
        dNode.setAttribute("timestamp", Long.toString(this.timestamp)).setAttribute("institution", this.institution).setAttribute("price", Float.toString(this.price)).setAttribute("quantity", Integer.toString(this.quantity)).setAttribute("buyer", this.buyer).setAttribute("seller", this.seller).setAttribute("maxBidPrice", Float.toString(this.maxBidPrice)).setAttribute("buyerOperation", this.buyerOperation).setAttribute("sellerOperation", this.sellerOperation);
        node.addContent(dNode);
    }
}
