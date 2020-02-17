// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package Trobot;

import java.util.TimerTask;
import java.util.Timer;
import jessx.business.OrderBook;
import org.jdom.Document;
import java.util.Date;

public abstract class Animator extends Robot
{
    private Date LastDateOrder;
    private double InactivityPercentage;
    
    public Animator(final int i, final double InactivityPercentage) {
        super(i);
        this.InactivityPercentage = InactivityPercentage;
    }
    
    @Override
    public long NextWakeUp(final String instit) {
        return Math.max(1000L, (long)((0.8 + Math.random() * 40.0 / 100.0) * (this.InactivityPercentage / 100.0) * this.getRobotCore().getExperimentManager().getCurrentPeriodDuration() - (new Date().getTime() - this.getDatesLastOrder().get(instit).getTime())));
    }
    
    @Override
    public long BasicNextWakeUp() {
        return Math.max(1000L, (long)((0.8 + Math.random() * 40.0 / 100.0) * (this.InactivityPercentage / 100.0) * this.getRobotCore().getExperimentManager().getCurrentPeriodDuration()));
    }
    
    public double getInactivityPercentage() {
        return this.InactivityPercentage;
    }
    
    public void setInactivityPercentage(final double InactivityPercentage) {
        this.InactivityPercentage = InactivityPercentage;
    }
    
    @Override
    public void objectReceived(final Document xmlDoc) {
        if (xmlDoc.getRootElement().getName().equals("OrderBook")) {
            final OrderBook ob = new OrderBook();
            ob.initFromNetworkInput(xmlDoc.getRootElement());
            this.getMySchedulers().get(ob.getInstitution()).cancel();
            this.getMySchedulers().put(ob.getInstitution(), new Timer());
            if (this.isOrdersAllowed() && this.isHasToRun()) {
                final long Delay = this.BasicNextWakeUp();
                final Task task = new Task(this, Delay);
                System.out.println("schedule delay threadcount animator" + Delay + "  " + Thread.activeCount());
                this.getMySchedulers().get(ob.getInstitution()).schedule(task, Delay);
            }
        }
        super.objectReceived(xmlDoc);
    }
    
    @Override
    protected abstract void MyAct();
    
    @Override
    protected abstract String chooseName(final int p0);
}
