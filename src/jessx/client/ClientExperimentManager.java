// 
// Decompiled by Procyon v0.6.0
// 

package jessx.client;

import jessx.client.event.ExperimentDeveloppmentListener;
import jessx.net.ExpUpdate;
import org.jdom.Document;
import java.util.Vector;
import java.util.Date;
import jessx.client.event.NetworkListener;

public class ClientExperimentManager implements NetworkListener
{
    public static final int EXP_OFF = 0;
    public static final int EXP_ON_PER_OFF = 1;
    public static final int EXP_ON_PER_ON = 2;
    private int experimentState;
    private long currentPeriodDuration;
    private int currentPeriod;
    private Date syncDate;
    private Vector listeners;
    
    public long getRemainingTimeInPeriod() {
        final Date now = new Date();
        return this.currentPeriodDuration - now.getTime() + this.syncDate.getTime();
    }
    
    public int getCurrentPeriod() {
        return this.currentPeriod;
    }
    
    public int getExperimentState() {
        return this.experimentState;
    }
    
    private void setExperimentState(final int newState) {
        if (newState != this.getExperimentState()) {
            this.experimentState = newState;
        }
    }
    
    public ClientExperimentManager() {
        this.experimentState = 0;
        this.currentPeriod = 0;
        this.listeners = new Vector();
        ClientCore.addNetworkListener(this, "ExperimentUpdate");
    }
    
    public void objectReceived(final Document doc) {
        if (doc.getRootElement().getName().equals("ExperimentUpdate")) {
            final ExpUpdate update = new ExpUpdate(7, "", -1);
            if (!update.initFromNetworkInput(doc.getRootElement())) {
                return;
            }
            switch (update.getUpdateType()) {
                case 5: {
                    this.setExperimentState(1);
                    this.fireExperimentBegins();
                    break;
                }
                case 4: {
                    this.setExperimentState(0);
                    break;
                }
                case 0: {
                    this.setExperimentState(1);
                    this.fireExperimentBegins();
                    break;
                }
                case 1: {
                    this.setExperimentState(0);
                    this.fireExperimentEnds();
                    break;
                }
                case 2: {
                    ++this.currentPeriod;
                    this.currentPeriodDuration = Integer.parseInt(update.getUpdateMessage());
                    this.syncDate = new Date();
                    this.currentPeriod = update.getCurrentPeriod();
                    this.setExperimentState(2);
                    this.firePeriodBegins();
                    break;
                }
                case 3: {
                    this.setExperimentState(1);
                    this.firePeriodEnds();
                    break;
                }
            }
        }
    }
    
    public void addListener(final ExperimentDeveloppmentListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final ExperimentDeveloppmentListener listener) {
        this.listeners.remove(listener);
    }
    
    private void fireExperimentBegins() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ExperimentDeveloppmentListener) this.listeners.elementAt(i)).experimentBegins();
        }
    }
    
    private void fireExperimentEnds() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ExperimentDeveloppmentListener) this.listeners.elementAt(i)).experimentFinished();
        }
    }
    
    private void firePeriodBegins() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ExperimentDeveloppmentListener) this.listeners.elementAt(i)).periodBegins();
        }
    }
    
    private void firePeriodEnds() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((ExperimentDeveloppmentListener) this.listeners.elementAt(i)).periodFinished();
        }
    }
}
