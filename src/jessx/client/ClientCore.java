// 
// Decompiled by Procyon v0.6.0
// 

package jessx.client;

import jessx.client.event.ConnectionListener;
import jessx.client.event.OperatorPlayedListener;
import org.jdom.Document;

import java.util.Hashtable;
import java.util.Iterator;
import jessx.client.event.NetworkListener;
import jessx.net.NetworkWritable;
import jessx.business.Operation;
import java.io.IOException;
import jessx.business.Operator;
import jessx.business.Institution;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;
import java.io.File;
import jessx.utils.Utils;
import java.util.Vector;
import jessx.business.Portfolio;
import java.util.HashMap;

public abstract class ClientCore
{
    public static final String CLIENT_LOG_FILE = "./client.log";
    public static final int CONNECTED = 1;
    public static final int DISCONNECTED = 0;
    private static HashMap operatorsPlayed;
    private static HashMap institutions;
    private static Portfolio portfolio;
    private static CommClient commModule;
    private static ClientExperimentManager experimentManager;
    private static String login;
    private static HashMap networkListener;
    private static Vector operatorPlayedListeners;
    private static Vector connectionListeners;
    
    static {
        InitLogs();
        Utils.logger.debug("Setting application properties...");
        Utils.logger.debug("Sets WaitingPort");
        Utils.SetApplicationProperties("ServerWaitingPort", "6290");
        Utils.logger.info("Loading operations and institutions modules...");
        Utils.loadModules("jessx.business.operations.LimitOrder");
        Utils.loadModules("jessx.business.operations.DeleteOrder");
        Utils.loadModules("jessx.business.operations.MarketOrder");
        Utils.loadModules("jessx.business.operations.BestLimitOrder");
        Utils.loadModules("jessx.business.institutions.OrderMarket");
        Utils.logger.info("Loading modules done.");
        ClientCore.networkListener = new HashMap();
        ClientCore.operatorPlayedListeners = new Vector();
        ClientCore.experimentManager = new ClientExperimentManager();
        new DataManager();
        new LogSender(24456);
        ClientCore.operatorsPlayed = new HashMap();
        ClientCore.institutions = new HashMap();
        ClientCore.portfolio = new Portfolio(0.0f, new HashMap());
        ClientCore.connectionListeners = new Vector();
    }
    
    private static void InitLogs() {
        final File file = new File("./client.log");
        if (file.exists()) {
            file.delete();
        }
        final Properties log4jconf = new Properties();
        log4jconf.setProperty("log4j.rootCategory", "debug, stdout, R");
        log4jconf.setProperty("log4j.category.your.category.name", "DEBUG");
        log4jconf.setProperty("log4j.category.your.category.name", "INHERITED");
        log4jconf.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jconf.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jconf.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%5p [%t] (%F:%L) - %m%n");
        log4jconf.setProperty("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
        log4jconf.setProperty("log4j.appender.R.File", "./client.log");
        log4jconf.setProperty("log4j.appender.R.MaxFileSize", "5000KB");
        log4jconf.setProperty("log4j.appender.R.MaxBackupIndex", "1");
        log4jconf.setProperty("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
        log4jconf.setProperty("log4j.appender.R.layout.ConversionPattern", "%r [%p] %m  [%t] (%F:%L) \r\n");
        PropertyConfigurator.configure(log4jconf);
        Utils.logger.debug("Logging enabled.");
    }
    
    public static Portfolio getPortfolio() {
        return ClientCore.portfolio;
    }
    
    public static String getLogin() {
        return ClientCore.login;
    }
    
    public static ClientExperimentManager getExperimentManager() {
        return ClientCore.experimentManager;
    }
    
    public static void addInstitution(final Institution institution) {
        ClientCore.institutions.put(institution.getName(), institution);
    }
    
    public static void addOperatorPlayed(final Operator oper) {
        if (!ClientCore.operatorsPlayed.containsKey(oper.getCompleteName())) {
            ClientCore.operatorsPlayed.put(oper.getCompleteName(), oper);
            fireNewOperatorPlayed(oper);
        }
    }
    
    public static HashMap getInstitutions() {
        return ClientCore.institutions;
    }
    
    public static HashMap getOperators() {
        return ClientCore.operatorsPlayed;
    }
    
    public static Institution getInstitution(final String name) {
        return (Institution) ClientCore.institutions.get(name);
    }
    
    public static Operator getOperator(final String name) {
        return (Operator) ClientCore.operatorsPlayed.get(name);
    }
    
    public static void initializeForNewExperiment() {
        ClientCore.institutions.clear();
        ClientCore.operatorsPlayed.clear();
    }
    
    public static void connecToServer(final String hostname, final String login, final String password) throws IOException {
        (ClientCore.commModule = new CommClient()).connect(hostname, login, password);
        ClientCore.login = login;
    }
    
    public static void executeOperation(final Operation op) {
    }
    
    public static void send(final NetworkWritable message) {
        ClientCore.commModule.send(message);
    }
    
    public static void addNetworkListener(final NetworkListener listener, final String expectedRootNode) {
        Utils.logger.debug("Adding a network listener on object " + expectedRootNode);
        if (!ClientCore.networkListener.containsKey(expectedRootNode)) {
            ClientCore.networkListener.put(expectedRootNode, new Vector());
        }
        ((Vector) ClientCore.networkListener.get(expectedRootNode)).add(listener);
    }
    
    public static void removeNetworkListener(final NetworkListener listener) {
        Utils.logger.debug("Removing a network listener from all classes it was registered for.");
        for (final Object key :   ClientCore.networkListener.keySet()) {
            removeNetworkListener(listener, (String) key);
        }
    }
    
    public static void removeNetworkListener(final NetworkListener listener, final String expectedRootNode) {
        Utils.logger.debug("removing a network listener from object: " + expectedRootNode);
        ((Hashtable<Object, Object>) ClientCore.networkListener.get(expectedRootNode)).remove(listener);
    }
    
    static void fireObjectReceived(final Document object) {
        Utils.logger.debug("Dispatching object received to listener...");
        final Vector vect = (Vector) ClientCore.networkListener.get(object.getRootElement().getName());
        if (vect != null) {
            for (int i = 0; i < vect.size(); ++i) {
                ((NetworkListener) vect.elementAt(i)).objectReceived(object);
            }
        }
    }
    
    public static void addOperatorPLayedListener(final OperatorPlayedListener listener) {
        ClientCore.operatorPlayedListeners.add(listener);
    }
    
    public static void removeOperatorPlayedListener(final OperatorPlayedListener listener) {
        ClientCore.operatorPlayedListeners.remove(listener);
    }
    
    private static void fireNewOperatorPlayed(final Operator op) {
        for (int i = 0; i < ClientCore.operatorPlayedListeners.size(); ++i) {
            ((OperatorPlayedListener) ClientCore.operatorPlayedListeners.elementAt(i)).newOperator(op);
        }
    }
    
    public static void addConnectionListener(final ConnectionListener listener) {
        ClientCore.connectionListeners.add(listener);
    }
    
    public static void removeConnectionListener(final ConnectionListener listener) {
        ClientCore.connectionListeners.remove(listener);
    }
    
    public static void fireConnectionStateChanged(final int newState) {
        for (int i = 0; i < ClientCore.connectionListeners.size(); ++i) {
            ((ConnectionListener) ClientCore.connectionListeners.elementAt(i)).connectionStateChanged(newState);
        }
    }
    
    public static boolean isConnected() {
        return ClientCore.commModule.isConnected();
    }
}
