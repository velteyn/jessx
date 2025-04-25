// 
// Decompiled by Procyon v0.6.0
// 

package jessx.agent;

import java.util.Vector;
import jessx.net.NetworkWritable;
import jessx.net.ExpUpdate;
import org.jdom.Document;
import java.io.IOException;
import jessx.client.ClientCore;
import jessx.utils.Utils;
import java.util.Properties;
import jessx.client.event.NetworkListener;
import jessx.client.event.ExperimentDeveloppmentListener;

public abstract class Agent extends Thread implements ExperimentDeveloppmentListener, NetworkListener
{
    private static final int CALL_EXPERIMENT_BEGIN = 0;
    private static final int CALL_PERIOD_BEGIN = 1;
    private static final int CALL_EXPERIMENT_END = 2;
    private static final int CALL_PERIOD_END = 3;
    private static final int CALL_EXP_OFF = 4;
    private static final int CALL_EXP_ON_PER_OFF = 5;
    private static final int CALL_EXP_ON_PER_ON = 6;
    private static final int CALL_CLIENT_READY = 7;
    private static final String AGENT_PROPERTIES_FILES = "agent.properties";
    public static Properties agentProperties;
    private boolean execState;
    private AgentTaskQueue taskQueue;
    protected long pauseDuration;
    
    static {
        Agent.agentProperties = new Properties();
        JesseAgentApps.getLogger().debug("Loading agent properties...");
        final String path = String.valueOf(System.getProperty("user.dir")) + System.getProperty("file.separator") + "agent.properties";
        Utils.loadApplicationProperties(path, Agent.agentProperties);
        try {
            final String host = Agent.agentProperties.getProperty("host");
            final String user = Agent.agentProperties.getProperty("login");
            final String pass = Agent.agentProperties.getProperty("password");
            ClientCore.connecToServer(host, String.valueOf(user) + (int)(Math.random() * 10000.0), pass);
        }
        catch (final IOException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public final void stopAgent() {
        this.execState = false;
    }
    
    public final void startAgent() {
        this.execState = true;
    }
    
    public Agent(final long pauseDuration) {
        this.execState = false;
        this.taskQueue = new AgentTaskQueue();
        this.pauseDuration = 1000L;
        this.pauseDuration = pauseDuration;
        ClientCore.getExperimentManager().addListener(this);
        ClientCore.addNetworkListener(this, "ExperimentUpdate");
        this.start();
    }
    
    private static int getExperimentStage() {
        return ClientCore.getExperimentManager().getExperimentState();
    }
    
    public final void experimentBegins() {
        this.taskQueue.addTask(0);
    }
    
    public final void experimentFinished() {
        this.taskQueue.addTask(2);
    }
    
    public final void periodBegins() {
        this.taskQueue.addTask(1);
    }
    
    public final void periodFinished() {
        this.taskQueue.addTask(3);
    }
    
    public void objectReceived(final Document xmlObject) {
        final ExpUpdate update = new ExpUpdate(0, "", 0);
        if (update.initFromNetworkInput(xmlObject.getRootElement()) && update.getUpdateType() == 7) {
            this.taskQueue.addTask(7);
        }
    }
    
    protected final void notifyClientReady() {
        ClientCore.send(new ExpUpdate(7, "", -1));
    }
    
    protected void experimentStarting() {
        this.startAgent();
    }
    
    protected void experimentEnding() {
        this.stopAgent();
    }
    
    protected abstract void periodStarting();
    
    protected abstract void periodEnding();
    
    protected abstract void runExperimentOff();
    
    protected abstract void runExperimentOnPeriodOff();
    
    protected abstract void runExperimentOnPeriodOn();
    
    @Override
    public final void run() {
        while (true) {
            if (this.execState) {
                switch (getExperimentStage()) {
                    case 0: {
                        this.taskQueue.addTask(4);
                        break;
                    }
                    case 1: {
                        this.taskQueue.addTask(5);
                        break;
                    }
                    case 2: {
                        this.taskQueue.addTask(6);
                        break;
                    }
                    default: {
                        JesseAgentApps.getLogger().warn("Unexpected experiment state (state " + getExperimentStage() + "). If a " + "state has been added, you should update " + "the agent code so that it supports this " + "new state. Ignoring this state.");
                        break;
                    }
                }
            }
            try {
                if (this.pauseDuration <= 0L) {
                    continue;
                }
                Thread.sleep(this.pauseDuration);
            }
            catch (final InterruptedException ex) {
                JesseAgentApps.getLogger().warn(ex.getMessage());
            }
        }
    }
    
    class AgentTaskQueue extends Thread
    {
        private Vector tasks;
        
        public AgentTaskQueue() {
            this.tasks = new Vector();
            this.start();
        }
        
        public void addTask(final int task) {
            this.tasks.add(new Integer(task));
        }
        
        private void treatTask() {
            if (this.tasks.size() <= 0) {
                return;
            }
            final int task = (int) this.tasks.elementAt(0);
            this.tasks.remove(0);
            switch (task) {
                case 0: {
                    Agent.this.experimentStarting();
                    break;
                }
                case 2: {
                    Agent.this.experimentEnding();
                    break;
                }
                case 4: {
                    Agent.this.runExperimentOff();
                    break;
                }
                case 5: {
                    Agent.this.runExperimentOnPeriodOff();
                    break;
                }
                case 6: {
                    Agent.this.runExperimentOnPeriodOn();
                    break;
                }
                case 1: {
                    Agent.this.periodStarting();
                    break;
                }
                case 3: {
                    Agent.this.periodEnding();
                    break;
                }
                case 7: {
                    Agent.this.notifyClientReady();
                    break;
                }
                default: {
                    JesseAgentApps.getLogger().warn("Unexpected task (task " + task + "). If a task has been added, " + "you should update the agent code " + "so that it supports this " + "new task. Ignoring this task.");
                    break;
                }
            }
        }
        
        @Override
        public void run() {
            while (true) {
                if (this.tasks.size() > 0) {
                    this.treatTask();
                }
                else {
                    try {
                        Thread.sleep(500L);
                    }
                    catch (final InterruptedException ex) {
                        JesseAgentApps.getLogger().warn(ex.getMessage());
                    }
                }
            }
        }
    }
}
