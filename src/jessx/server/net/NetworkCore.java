package jessx.server.net;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import jessx.business.BusinessCore;
import jessx.business.event.PlayerEvent;
import jessx.business.event.PlayerListener;
import jessx.net.NetworkWritable;
import jessx.server.LogManager;
import org.jdom.Document;

public abstract class NetworkCore {
	private static ClientConnectionPoint connectionPoint = new ClientConnectionPoint();

	public static ClientConnectionPoint getConnectionPoint() {
		return connectionPoint;
	}

	private static HashMap playersList = new HashMap<Object, Object>();

	private static Vector listeners = new Vector();

	private static ExperimentManager experimentManager = new ExperimentManager();

	private static LogManager logManager = new LogManager();

	public static ExperimentManager getExperimentManager() {
		return experimentManager;
	}

	public static LogManager getLogManager() {
		return logManager;
	}

	public static Player getPlayer(String login) {
		return (Player) playersList.get(login);
	}

	public static HashMap getPlayerList() {
		return playersList;
	}

	public static void addPlayer(Player player) {
		if (playersList.containsKey(player.getLogin())) {
			playersList.put(player.getLogin(), player);
		} else {
			playersList.put(player.getLogin(), player);
			firePlayerAdded(player.getLogin());
		}
	}

	public static void removePlayer(String playerName) {
		if (playersList.containsKey(playerName)) {
			getPlayer(playerName).playerDeleteByServer();
			if (getExperimentManager().getExperimentState() != 0) {
				String playerType = getPlayer(playerName).getPlayerCategory();
				BusinessCore.getScenario().getPlayerType(playerType);
				Vector<String> institutions = BusinessCore.getScenario().getPlayerType(playerType).getInstitutionsWherePlaying();
				for (int i = 0; i < institutions.size(); i++) {
					synchronized (BusinessCore.getInstitution(institutions.get(i)).getOrderBook()) {
						BusinessCore.getInstitution(institutions.get(i)).getOrderBook().clearPlayer(playerName);
					}
					sendToAllPlayers((NetworkWritable) BusinessCore.getInstitution(institutions.get(i)).getOrderBook());
				}
			}
			playersList.remove(playerName);
			firePlayerRemoved(playerName);
		}
	}

	public static void setServerOnline() {
		if (!connectionPoint.isAlive())
			connectionPoint.start();
	}

	public static void setServerOffline() {
		if (!connectionPoint.isAlive())
			connectionPoint.destroy();
	}

	public static void sendToAllPlayers(NetworkWritable message) {
		Iterator<String> categories = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
		while (categories.hasNext()) {
			String category = categories.next();
			sendToPlayerCategory(message, category);
		}
	}

	public static void sendToPlayerCategory(NetworkWritable message, String playerCat) {
		Iterator<String> iter = getPlayerList().keySet().iterator();
		Document doc = new Document(message.prepareForNetworkOutput(playerCat));
		while (iter.hasNext()) {
			String key = iter.next();
			if (getPlayer(key).getPlayerCategory().equalsIgnoreCase(playerCat))
				getPlayer(key).send(doc);
		}
	}

	public static void sendToPlayer(NetworkWritable message, String login) {
		getPlayer(login).send(message);
	}

	public static void arePlayersReady(String explanation) {
		Iterator<String> iter = playersList.keySet().iterator();
		while (iter.hasNext())
			getPlayer(iter.next()).isClientReady(explanation);
	}

	private static void firePlayerAdded(String playerName) {
		for (int i = 0; i < listeners.size(); i++)
			((PlayerListener) listeners.elementAt(i)).playerListModified(new PlayerEvent(playerName, PlayerEvent.PLAYER_ADDED));
	}

	private static void firePlayerRemoved(String playerName) {
		for (int i = 0; i < listeners.size(); i++)
			((PlayerListener) listeners.elementAt(i)).playerListModified(new PlayerEvent(playerName, PlayerEvent.PLAYER_REMOVED));
	}

	public static void addListener(PlayerListener listener) {
		listeners.add(listener);
	}

	public static void removeListener(PlayerListener listener) {
		listeners.remove(listener);
	}
}
