// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Content;
import java.util.Iterator;
import jessx.utils.Utils;
import org.jdom.Element;
import java.util.Vector;
import jessx.utils.XmlExportable;
import jessx.net.NetworkReadable;
import jessx.net.NetworkWritable;

public class OrderBook implements NetworkWritable, NetworkReadable, XmlExportable
{
    private String institution;
    private Vector bid;
    private Vector ask;
    
    public Vector getBid() {
        return this.bid;
    }
    
    public Vector getAsk() {
        return this.ask;
    }
    
    public String getInstitution() {
        return this.institution;
    }
    
    public void setInstitution(final String institution) {
        this.institution = institution;
    }
    
    public void reinit() {
        this.ask.clear();
        this.bid.clear();
    }
    
    public OrderBook() {
        this.bid = new Vector();
        this.ask = new Vector();
    }
    
    public boolean initFromNetworkInput(final Element root) {
        final String institution = root.getAttributeValue("institution");
        if (institution == null) {
            Utils.logger.debug("Invalid orderbook xml node: institution attribute is missing.");
            return false;
        }
        this.institution = institution;
        this.bid.removeAllElements();
        this.ask.removeAllElements();
        final Element askNode = root.getChild("Ask");
        final Element bidNode = root.getChild("Bid");
        if (askNode != null) {
            final Iterator iter = askNode.getChildren().iterator();
            while (iter.hasNext()) {
                try {
                    final Operation op = Operation.initOperationFromXml((Element) iter.next());
                    if (!(op instanceof Order)) {
                        continue;
                    }
                    this.ask.add(op);
                }
                catch (final OperationNotCreatedException ex) {
                    Utils.logger.error("Unable to load operation. [IGNORED]");
                }
            }
        }
        if (bidNode != null) {
            final Iterator iter = bidNode.getChildren().iterator();
            while (iter.hasNext()) {
                try {
                    final Operation op = Operation.initOperationFromXml((Element) iter.next());
                    if (!(op instanceof Order)) {
                        continue;
                    }
                    this.bid.add(op);
                }
                catch (final OperationNotCreatedException ex) {
                    Utils.logger.error("Unable to load operation. [IGNORED]");
                }
            }
        }
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element askNode = new Element("Ask");
        final Element bidNode = new Element("Bid");
        for (int i = 0; i < this.ask.size(); ++i) {
            if (((Order) this.ask.elementAt(i)).isVisibleInOrderbook()) {
                askNode.addContent(((NetworkWritable) this.ask.elementAt(i)).prepareForNetworkOutput(pt));
            }
        }
        for (int i = 0; i < this.bid.size(); ++i) {
            if (((Order) this.bid.elementAt(i)).isVisibleInOrderbook()) {
                bidNode.addContent(((NetworkWritable) this.bid.elementAt(i)).prepareForNetworkOutput(pt));
            }
        }
        final Element root = new Element("OrderBook");
        root.addContent(askNode);
        root.addContent(bidNode);
        root.setAttribute("institution", this.institution);
        return root;
    }
    
    public void saveToXml(final Element rootNode) {
        final Element askNode = new Element("Ask");
        final Element bidNode = new Element("Bid");
        for (int i = 0; i < this.ask.size(); ++i) {
            askNode.addContent(((NetworkWritable) this.ask.elementAt(i)).prepareForNetworkOutput(""));
        }
        for (int i = 0; i < this.bid.size(); ++i) {
            bidNode.addContent(((NetworkWritable) this.bid.elementAt(i)).prepareForNetworkOutput(""));
        }
        rootNode.addContent(askNode);
        rootNode.addContent(bidNode);
        rootNode.setAttribute("institution", this.institution);
    }
    
    public Order getOrder(final int orderId) {
        for (int i = this.ask.size() - 1; i >= 0; --i) {
            if (((Order) this.ask.elementAt(i)).getId() == orderId) {
                return (Order) this.ask.elementAt(i);
            }
        }
        for (int i = this.bid.size() - 1; i >= 0; --i) {
            if (((Order) this.bid.elementAt(i)).getId() == orderId) {
                return (Order) this.bid.elementAt(i);
            }
        }
        return (Order) this.ask.elementAt(0);
    }
    
    public void deleteOrder(final int orderId) {
        Utils.logger.debug("Deleting order Id = " + orderId + " ...");
        for (int i = this.ask.size() - 1; i >= 0; --i) {
            if (((Order) this.ask.elementAt(i)).getId() == orderId) {
                Utils.logger.debug("Order found. It was an ask. Removing ...");
                this.ask.removeElementAt(i);
                Utils.logger.debug("Order successfully removed.");
            }
        }
        for (int i = this.bid.size() - 1; i >= 0; --i) {
            if (((Order) this.bid.elementAt(i)).getId() == orderId) {
                Utils.logger.debug("Order found. It was an bid. Removing ...");
                this.bid.removeElementAt(i);
                Utils.logger.debug("Order successfully removed.");
            }
        }
    }
    
    public void clearPlayer(final String playerName) {
        for (int i = this.ask.size() - 1; i >= 0; --i) {
            if (((Operation) this.ask.elementAt(i)).getEmitter().equals(playerName)) {
                this.ask.removeElementAt(i);
            }
        }
        for (int i = this.bid.size() - 1; i >= 0; --i) {
            if (((Operation) this.bid.elementAt(i)).getEmitter().equals(playerName)) {
                this.bid.removeElementAt(i);
            }
        }
    }
}
