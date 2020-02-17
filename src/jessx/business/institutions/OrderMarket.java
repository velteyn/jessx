// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import jessx.business.Operator;
import org.jdom.Element;
import jessx.business.BusinessCore;
import jessx.utils.Utils;
import jessx.net.NetworkWritable;
import jessx.net.WarnForClient;
import jessx.server.net.NetworkCore;
import jessx.business.operations.DeleteOrder;
import jessx.business.Order;
import jessx.business.Operation;
import javax.swing.JPanel;
import jessx.business.InstitutionCreator;
import jessx.business.Institution;

public class OrderMarket extends Institution
{
    OrderMarketSetupGui orderMarketSetupGui;
    
    static {
        try {
            System.out.println("Loading OrderMarket...");
            InstitutionCreator.institutionFactories.put("OrderMarket", Class.forName("jessx.business.institutions.OrderMarket"));
        }
        catch (ClassNotFoundException exception) {
            System.out.println("Unabled to locate the OrderMarket class. Reason: bad class name spelling.");
            exception.printStackTrace();
        }
    }
    
    @Override
    public void desactivePanel() {
        this.orderMarketSetupGui.desactive();
    }
    
    @Override
    public void activePanel() {
        this.orderMarketSetupGui.active();
    }
    
    @Override
    public JPanel getInstitutionSetupGui() {
        return this.orderMarketSetupGui = new OrderMarketSetupGui(this);
    }
    
    @Override
    public boolean isOperationValid(final Operation op) {
        throw new UnsupportedOperationException("Method isOperationValid() not yet implemented.");
    }
    
    @Override
    public boolean isOperationSupported(final Operation op) {
        return op instanceof Order || op instanceof DeleteOrder;
    }
    
    public void sendWarningMessage(final String playerName, final float warningType) {
        if (warningType == 3.0f) {
            final String warnMessage = "You have not enough cash for the operation costs.";
            NetworkCore.getPlayer(playerName).send(new WarnForClient(warnMessage));
        }
        else if (warningType == 4.0f) {
            final String warnMessage = "You have not enough cash to afford all the bids you placed.";
            NetworkCore.getPlayer(playerName).send(new WarnForClient(warnMessage));
        }
        else if (warningType == 2.0f) {
            final String warnMessage = "You have not enough assets to afford all the asks you placed.";
            NetworkCore.getPlayer(playerName).send(new WarnForClient(warnMessage));
        }
        else if (warningType == 6.0f) {
            final String warnMessage = "There are not enough asks in the orderbook to pass your bid.";
            NetworkCore.getPlayer(playerName).send(new WarnForClient(warnMessage));
        }
        else if (warningType == 5.0f) {
            final String warnMessage = "There are not enough bids in the orderbook to pass your ask.";
            NetworkCore.getPlayer(playerName).send(new WarnForClient(warnMessage));
        }
    }
    
    @Override
    public void treatOperation(final Operation op) {
        if (this.isOperationSupported(op)) {
            super.treatOperation(op);
            if (op instanceof Order) {
                final long time = NetworkCore.getExperimentManager().getTimeInPeriod();
                ((Order)op).setTimestamp(time);
                float orderValidity = 0.0f;
                Utils.logger.info("--> Debut id=" + ((Order)op).getId());
                synchronized (BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook()) {
                    Utils.logger.info("--> Debut sync id=" + ((Order)op).getId());
                    orderValidity = ((Order)op).orderValidity((Order)op, NetworkCore.getPlayer(((Order)op).getEmitter()).getPortfolio());
                    if (orderValidity == 1.0f) {
                        ((Order)op).newId();
                        NetworkCore.getLogManager().log(op.prepareForNetworkOutput(""));
                        ((Order)op).insertOrder((Order)op);
                    }
                }
                // monitorexit(BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook())
                Utils.logger.info("--> Fin id=" + ((Order)op).getId());
                if (orderValidity == 1.0f) {
                    NetworkCore.sendToAllPlayers(this.getOrderBook());
                    NetworkCore.sendToPlayer(NetworkCore.getPlayer(((Order)op).getEmitter()).getPortfolio(), ((Order)op).getEmitter());
                    final Element orderbook = new Element("OrderBook");
                    orderbook.setAttribute("timestamp", Long.toString(time));
                    this.getOrderBook().saveToXml(orderbook);
                    NetworkCore.getLogManager().log(orderbook);
                }
                else {
                    this.sendWarningMessage(((Order)op).getEmitter(), orderValidity);
                }
            }
            else if (op instanceof DeleteOrder) {
                final long time = NetworkCore.getExperimentManager().getTimeInPeriod();
                float deleteOrderValidity = 0.0f;
                synchronized (BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook()) {
                    final Order orderToDelete = this.getOrderBook().getOrder(((DeleteOrder)op).getOrderId());
                    final float deleteOrderValidity2;
                    deleteOrderValidity = (deleteOrderValidity2 = ((DeleteOrder)op).deleteOrderValidity(orderToDelete, NetworkCore.getPlayer(((DeleteOrder)op).getEmitter()).getPortfolio()));
                    final DeleteOrder deleteOrder = (DeleteOrder)op;
                    if (deleteOrderValidity2 == 1.0f) {
                        this.getOrderBook().deleteOrder(((DeleteOrder)op).getOrderId());
                    }
                }
                // monitorexit(BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook())
                final float n = deleteOrderValidity;
                final DeleteOrder deleteOrder2 = (DeleteOrder)op;
                if (n == 1.0f) {
                    NetworkCore.sendToAllPlayers(this.getOrderBook());
                    NetworkCore.getLogManager().log(op.prepareForNetworkOutput(""));
                    final Element orderbook = new Element("OrderBook");
                    orderbook.setAttribute("timestamp", Long.toString(time));
                    this.getOrderBook().saveToXml(orderbook);
                    NetworkCore.getLogManager().log(orderbook);
                }
                else {
                    final String warnMessage = "You have not enough cash to afford the cancelling of this order.";
                    NetworkCore.getPlayer(((DeleteOrder)op).getEmitter()).send(new WarnForClient(warnMessage));
                }
            }
        }
    }
    
    @Override
    public String toString() {
        return "an institution";
    }
    
    @Override
    public JPanel getClientPanel(final Operator op) {
        return new OrderMarketClientPanel(op);
    }
    
    @Override
    public void loadFromXml(final Element node) {
    }
    
    @Override
    public void saveToXml(final Element parentNode) {
    }
}
