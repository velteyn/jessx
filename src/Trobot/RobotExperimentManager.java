package Trobot;

import java.util.Date;
import java.util.Vector;
import jessx.client.event.ExperimentDeveloppmentListener;
import jessx.client.event.NetworkListener;
import jessx.net.ExpUpdate;
import org.jdom.Document;

public class RobotExperimentManager implements NetworkListener {
  public static final int EXP_OFF = 0;
  
  public static final int EXP_ON_PER_OFF = 1;
  
  public static final int EXP_ON_PER_ON = 2;
  
  private int experimentState = 0;
  
  private long currentPeriodDuration;
  
  private int currentPeriod = 0;
  
  private Date syncDate;
  
  private Vector listeners = new Vector();
  
  public long getRemainingTimeInPeriod() {
    Date now = new Date();
    return this.currentPeriodDuration - now.getTime() + this.syncDate.getTime();
  }
  
  public int getCurrentPeriod() {
    return this.currentPeriod;
  }
  
  public int getExperimentState() {
    return this.experimentState;
  }
  
  private void setExperimentState(int newState) {
    if (newState != getExperimentState())
      this.experimentState = newState; 
  }
  
  public RobotExperimentManager(RobotCore robotCore) {
    robotCore.addNetworkListener(this, "ExperimentUpdate");
  }
  
  public void objectReceived(Document doc) {
    if (doc.getRootElement().getName().equals("ExperimentUpdate")) {
      ExpUpdate update = new ExpUpdate(7, "", -1);
      if (!update.initFromNetworkInput(doc.getRootElement()))
        return; 
      switch (update.getUpdateType()) {
        case 5:
          setExperimentState(1);
          fireExperimentBegins();
          break;
        case 4:
          setExperimentState(0);
          break;
        case 0:
          setExperimentState(1);
          fireExperimentBegins();
          break;
        case 1:
          setExperimentState(0);
          fireExperimentEnds();
          break;
        case 2:
          this.currentPeriod++;
          this.currentPeriodDuration = Integer.parseInt(update.getUpdateMessage());
          this.syncDate = new Date();
          this.currentPeriod = update.getCurrentPeriod();
          setExperimentState(2);
          firePeriodBegins();
          break;
        case 3:
          setExperimentState(1);
          firePeriodEnds();
          break;
      } 
    } 
  }
  
  public void addListener(ExperimentDeveloppmentListener listener) {
    this.listeners.add(listener);
  }
  
  public void removeListener(ExperimentDeveloppmentListener listener) {
    this.listeners.remove(listener);
  }
  
  private void fireExperimentBegins() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((ExperimentDeveloppmentListener)this.listeners.elementAt(i)).experimentBegins(); 
  }
  
  private void fireExperimentEnds() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((ExperimentDeveloppmentListener)this.listeners.elementAt(i)).experimentFinished(); 
  }
  
  private void firePeriodBegins() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((ExperimentDeveloppmentListener)this.listeners.elementAt(i)).periodBegins(); 
  }
  
  private void firePeriodEnds() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((ExperimentDeveloppmentListener)this.listeners.elementAt(i)).periodFinished(); 
  }
  
  public long getCurrentPeriodDuration() {
    return this.currentPeriodDuration;
  }
}
