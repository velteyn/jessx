package jessx.business.institutions;

import javax.swing.JPanel;
import jessx.business.BusinessCore;
import jessx.business.Institution;
import jessx.business.InstitutionCreator;
import jessx.business.Operation;
import jessx.business.Operator;
import jessx.business.Order;
import jessx.business.operations.DeleteOrder;
import jessx.net.NetworkWritable;
import jessx.net.WarnForClient;
import jessx.server.net.NetworkCore;
import jessx.utils.Utils;
import org.jdom.Element;

public class OrderMarket extends Institution {
	OrderMarketSetupGui orderMarketSetupGui;

	public void desactivePanel() {
		this.orderMarketSetupGui.desactive();
	}

	public void activePanel() {
		this.orderMarketSetupGui.active();
	}

	public JPanel getInstitutionSetupGui() {
		this.orderMarketSetupGui = new OrderMarketSetupGui(this);
		return this.orderMarketSetupGui;
	}

	public boolean isOperationValid(Operation op) {
		throw new UnsupportedOperationException("Method isOperationValid() not yet implemented.");
	}

	public boolean isOperationSupported(Operation op) {
		return !(!(op instanceof Order) && !(op instanceof DeleteOrder));
	}

	public void sendWarningMessage(String playerName, float warningType) {
		if (warningType == 3.0F) {
			String warnMessage = "You have not enough cash for the operation costs.";
			NetworkCore.getPlayer(playerName).send((NetworkWritable) new WarnForClient(warnMessage));
		} else if (warningType == 4.0F) {
			String warnMessage = "You have not enough cash to afford all the bids you placed.";
			NetworkCore.getPlayer(playerName).send((NetworkWritable) new WarnForClient(warnMessage));
		} else if (warningType == 2.0F) {
			String warnMessage = "You have not enough assets to afford all the asks you placed.";
			NetworkCore.getPlayer(playerName).send((NetworkWritable) new WarnForClient(warnMessage));
		} else if (warningType == 6.0F) {
			String warnMessage = "There are not enough asks in the orderbook to pass your bid.";
			NetworkCore.getPlayer(playerName).send((NetworkWritable) new WarnForClient(warnMessage));
		} else if (warningType == 5.0F) {
			String warnMessage = "There are not enough bids in the orderbook to pass your ask.";
			NetworkCore.getPlayer(playerName).send((NetworkWritable) new WarnForClient(warnMessage));
		}
	}

	public void treatOperation(Operation op) {
		if (isOperationSupported(op)) {
			super.treatOperation(op);
			if (op instanceof Order) {
				long time = NetworkCore.getExperimentManager().getTimeInPeriod();
				((Order) op).setTimestamp(time);
				float orderValidity = 0.0F;
				Utils.logger.info("--> Debut id=" + ((Order) op).getId());
				synchronized (BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook()) {
					Utils.logger.info("--> Debut sync id=" + ((Order) op).getId());
					orderValidity = ((Order) op).orderValidity((Order) op, NetworkCore.getPlayer(((Order) op).getEmitter()).getPortfolio());
					if (orderValidity == 1.0F) {
						((Order) op).newId();
						NetworkCore.getLogManager().log(op.prepareForNetworkOutput(""));
						((Order) op).insertOrder((Order) op);
					}
				}
				Utils.logger.info("--> Fin id=" + ((Order) op).getId());
				if (orderValidity == 1.0F) {
					NetworkCore.sendToAllPlayers((NetworkWritable) getOrderBook());
					NetworkCore.sendToPlayer((NetworkWritable) NetworkCore.getPlayer(((Order) op).getEmitter()).getPortfolio(), ((Order) op).getEmitter());
					Element orderbook = new Element("OrderBook");
					orderbook.setAttribute("timestamp", Long.toString(time));
					getOrderBook().saveToXml(orderbook);
					NetworkCore.getLogManager().log(orderbook);
				} else {
					sendWarningMessage(((Order) op).getEmitter(), orderValidity);
				}
			} else if (op instanceof DeleteOrder) {
				// timestamping operation
				long time = NetworkCore.getExperimentManager().getTimeInPeriod();

				float deleteOrderValidity = 0;

				// trying to delete order
				synchronized (BusinessCore.getInstitution(op.getInstitutionName()).getOrderBook()) {
					Order orderToDelete = this.getOrderBook().getOrder(((DeleteOrder) op).getOrderId());
					deleteOrderValidity = ((DeleteOrder) op).deleteOrderValidity(orderToDelete, NetworkCore.getPlayer(((DeleteOrder) op).getEmitter()).getPortfolio());
					if (deleteOrderValidity == ((DeleteOrder) op).DELETE_ORDER_VALID) {
						this.getOrderBook().deleteOrder(((DeleteOrder) op).getOrderId());
					}
				}
				if (deleteOrderValidity == ((DeleteOrder) op).DELETE_ORDER_VALID) {
					// sending modified orderbook
					NetworkCore.sendToAllPlayers(this.getOrderBook());
					// logging operation
					NetworkCore.getLogManager().log(op.prepareForNetworkOutput(""));
					// logging orderbook
					Element orderbook = new Element("OrderBook");
					orderbook.setAttribute("timestamp", Long.toString(time));
					this.getOrderBook().saveToXml(orderbook);
					NetworkCore.getLogManager().log(orderbook);
				} else {
					String warnMessage = "You have not enough cash to afford the cancelling of this order.";
					NetworkCore.getPlayer(((DeleteOrder) op).getEmitter()).send(new WarnForClient(warnMessage));
				}
			}
		}
	}

	static {
		try {
			System.out.println("Loading OrderMarket...");
			InstitutionCreator.institutionFactories.put("OrderMarket", Class.forName("jessx.business.institutions.OrderMarket"));
		} catch (ClassNotFoundException exception) {
			System.out.println("Unabled to locate the OrderMarket class. Reason: bad class name spelling.");
			exception.printStackTrace();
		}
	}

	public String toString() {
		return "an institution";
	}

	public JPanel getClientPanel(Operator op) {
		return new OrderMarketClientPanel(op);
	}

	public void loadFromXml(Element node) {
	}

	public void saveToXml(Element parentNode) {
	}
}
