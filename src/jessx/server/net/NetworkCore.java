// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.net;

import jessx.business.event.PlayerEvent;
import jessx.business.event.PlayerListener;
import org.jdom.Document;
import java.util.Iterator;
import jessx.net.NetworkWritable;
import jessx.business.BusinessCore;
import jessx.server.LogManager;
import java.util.Vector;
import java.util.HashMap;

public abstract class NetworkCore
{
    private static ClientConnectionPoint connectionPoint;
    private static HashMap playersList;
    private static Vector listeners;
    private static ExperimentManager experimentManager;
    private static LogManager logManager;
    
    static {
        NetworkCore.connectionPoint = new ClientConnectionPoint();
        NetworkCore.playersList = new HashMap();
        NetworkCore.listeners = new Vector();
        NetworkCore.experimentManager = new ExperimentManager();
        NetworkCore.logManager = new LogManager();
    }
    
    public static ClientConnectionPoint getConnectionPoint() {
        return NetworkCore.connectionPoint;
    }
    
    public static ExperimentManager getExperimentManager() {
        return NetworkCore.experimentManager;
    }
    
    public static LogManager getLogManager() {
        return NetworkCore.logManager;
    }
    
    public static Player getPlayer(final String login) {
        return (Player) NetworkCore.playersList.get(login);
    }
    
    public static HashMap getPlayerList() {
        return NetworkCore.playersList;
    }
    
    public static void addPlayer(final Player player) {
        if (NetworkCore.playersList.containsKey(player.getLogin())) {
            NetworkCore.playersList.put(player.getLogin(), player);
        }
        else {
            NetworkCore.playersList.put(player.getLogin(), player);
            firePlayerAdded(player.getLogin());
        }
    }
    
    public static void removePlayer(String playerName) {
        if (playersList.containsKey(playerName)) {
          getPlayer(playerName).playerDeleteByServer();

          if (getExperimentManager().getExperimentState() != ExperimentManager.EXP_OFF) {
            String playerType = getPlayer(playerName).getPlayerCategory();
            if(BusinessCore.getScenario().getPlayerType(playerType)!= null){

            }
            Vector institutions = BusinessCore.getScenario().getPlayerType(playerType).getInstitutionsWherePlaying();
            for (int i = 0; i < institutions.size(); i++) {
              synchronized (BusinessCore.getInstitution( (String) institutions.get(i)).getOrderBook()) {
                BusinessCore.getInstitution( (String) institutions.get(i)).getOrderBook().clearPlayer(playerName);
              }
              sendToAllPlayers(BusinessCore.getInstitution( (String) institutions.get(i)).getOrderBook());
            }
          }
          playersList.remove(playerName);
          firePlayerRemoved(playerName);
        }
      }
    
    public static void setServerOnline() {
        if (!NetworkCore.connectionPoint.isAlive()) {
            NetworkCore.connectionPoint.start();
        }
    }
    
    public static void setServerOffline() {
        if (!NetworkCore.connectionPoint.isAlive()) {
            NetworkCore.connectionPoint.destroy();
        }
    }
    
    public static void sendToAllPlayers(NetworkWritable message) {
        Iterator categories = BusinessCore.getScenario().getPlayerTypes().keySet().iterator();
        while (categories.hasNext()) {
          String category = (String)categories.next();
          sendToPlayerCategory(message, category);
        }
      }
    
    public static void sendToPlayerCategory(final NetworkWritable message, final String playerCat) {
        final Iterator iter = getPlayerList().keySet().iterator();
        final Document doc = new Document(message.prepareForNetworkOutput(playerCat));
        while (iter.hasNext()) {
            final String key = (String) iter.next();
            if (getPlayer(key).getPlayerCategory().equalsIgnoreCase(playerCat)) {
                getPlayer(key).send(doc);
            }
        }
    }
    
    public static void sendToPlayer(final NetworkWritable message, final String login) {
        getPlayer(login).send(message);
    }
    
    public static void arePlayersReady(final String explanation) {
        final Iterator iter = NetworkCore.playersList.keySet().iterator();
        while (iter.hasNext()) {
            getPlayer((String) iter.next()).isClientReady(explanation);
        }
    }
    
    private static void firePlayerAdded(final String playerName) {
        for (int i = 0; i < NetworkCore.listeners.size(); ++i) {
            ((PlayerListener) NetworkCore.listeners.elementAt(i)).playerListModified(new PlayerEvent(playerName, PlayerEvent.PLAYER_ADDED));
        }
    }
    
    private static void firePlayerRemoved(final String playerName) {
        for (int i = 0; i < NetworkCore.listeners.size(); ++i) {
            ((PlayerListener) NetworkCore.listeners.elementAt(i)).playerListModified(new PlayerEvent(playerName, PlayerEvent.PLAYER_REMOVED));
        }
    }
    
    public static void addListener(final PlayerListener listener) {
        NetworkCore.listeners.add(listener);
    }
    
    public static void removeListener(final PlayerListener listener) {
        NetworkCore.listeners.remove(listener);
    }
}
