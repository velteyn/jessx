// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package Trobot;

import org.jdom.Document;
import jessx.client.event.ConnectionListener;
import jessx.client.event.OperatorPlayedListener;
import java.util.Iterator;
import jessx.client.event.NetworkListener;
import jessx.net.NetworkWritable;
import jessx.business.Operation;
import java.io.IOException;
import jessx.business.Operator;
import jessx.business.Institution;
import jessx.utils.Utils;
import java.util.Vector;
import jessx.business.Portfolio;
import java.util.HashMap;

public class RobotCore
{
    public final int CONNECTED = 1;
    public final int DISCONNECTED = 0;
    private HashMap operatorsPlayed;
    private HashMap institutions;
    private Portfolio portfolio;
    private RobotExperimentManager experimentManager;
    private String login;
    private HashMap networkListener;
    private Vector operatorPlayedListeners;
    private Vector connectionListeners;
    private CommRobot commModule;
    private Robot robot;
    
    public RobotCore(final Robot robot) {
        this.operatorsPlayed = new HashMap();
        this.institutions = new HashMap();
        this.portfolio = new Portfolio(0.0f, new HashMap());
        this.connectionListeners = new Vector();
        Utils.SetApplicationProperties("ServerWaitingPort", "6290");
        Utils.loadModules("jessx.business.operations.LimitOrder");
        Utils.loadModules("jessx.business.operations.DeleteOrder");
        Utils.loadModules("jessx.business.operations.MarketOrder");
        Utils.loadModules("jessx.business.operations.BestLimitOrder");
        Utils.loadModules("jessx.business.institutions.OrderMarket");
        this.networkListener = new HashMap();
        this.operatorPlayedListeners = new Vector();
        this.experimentManager = new RobotExperimentManager(this);
        this.robot = robot;
    }
    
    public CommRobot getCommRobot() {
        return this.commModule;
    }
    
    public Portfolio getPortfolio() {
        return this.portfolio;
    }
    
    public String getLogin() {
        return null;
    }
    
    public RobotExperimentManager getExperimentManager() {
        return this.experimentManager;
    }
    
    public void addInstitution(final Institution institution) {
        this.institutions.put(institution.getName(), institution);
    }
    
    public void addOperatorPlayed(final Operator oper) {
        if (!this.operatorsPlayed.containsKey(oper.getCompleteName())) {
            this.operatorsPlayed.put(oper.getCompleteName(), oper);
            this.fireNewOperatorPlayed(oper);
        }
    }
    
    public HashMap getInstitutions() {
        return this.institutions;
    }
    
    public HashMap getOperators() {
        return this.operatorsPlayed;
    }
    
    public Institution getInstitution(final String name) {
        return (Institution) this.institutions.get(name);
    }
    
    public Operator getOperator(final String name) {
        return (Operator) this.operatorsPlayed.get(name);
    }
    
    public void initializeForNewExperiment() {
        this.institutions.clear();
        this.operatorsPlayed.clear();
    }
    
    public void connecToServer(final String hostname, final String login, final String password) throws IOException {
        (this.commModule = new CommRobot(this)).connect(hostname, login, password);
        this.login = login;
    }
    
    public void executeOperation(final Operation op) {
    }
    
    public void send(final NetworkWritable message) {
        this.commModule.send(message);
    }
    
    public void addNetworkListener(final NetworkListener listener, final String expectedRootNode) {
        if (!this.networkListener.containsKey(expectedRootNode)) {
            this.networkListener.put(expectedRootNode, new Vector());
        }
        ((Vector) this.networkListener.get(expectedRootNode)).add(listener);
    }
    
    public void removeNetworkListener(final NetworkListener listener) {
        for (final Object key : this.networkListener.keySet()) {
            this.removeNetworkListener(listener, (String) key);
        }
    }
    
    public void removeNetworkListener(final NetworkListener listener, final String expectedRootNode) {
        ((HashMap) this.networkListener.get(expectedRootNode)).remove(listener);
    }
    
    public void addOperatorPLayedListener(final OperatorPlayedListener listener) {
        this.operatorPlayedListeners.add(listener);
    }
    
    public void removeOperatorPlayedListener(final OperatorPlayedListener listener) {
        this.operatorPlayedListeners.remove(listener);
    }
    
    public void addConnectionListener(final ConnectionListener listener) {
        this.connectionListeners.add(listener);
    }
    
    public void removeConnectionListener(final ConnectionListener listener) {
        this.connectionListeners.remove(listener);
    }
    
    public void fireConnectionStateChanged(final int newState) {
        for (int i = 0; i < this.connectionListeners.size(); ++i) {
            ((ConnectionListener) this.connectionListeners.elementAt(i)).connectionStateChanged(newState);
        }
    }
    
    public boolean isConnected() {
        return this.commModule.isConnected();
    }
    
    void fireObjectReceived(final Document object) {
        final Vector vect = (Vector) this.networkListener.get(object.getRootElement().getName());
        if (vect != null) {
            for (int i = 0; i < vect.size(); ++i) {
                ((NetworkListener) vect.elementAt(i)).objectReceived(object);
            }
        }
    }
    
    private void InitLogs(final boolean uselogfile) {
    }
    
    private void fireNewOperatorPlayed(final Operator op) {
        for (int i = 0; i < this.operatorPlayedListeners.size(); ++i) {
            ((OperatorPlayedListener) this.operatorPlayedListeners.elementAt(i)).newOperator(op);
        }
    }
    
    public Robot getRobot() {
        return this.robot;
    }
}
