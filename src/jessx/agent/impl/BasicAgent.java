// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.agent.impl;

import jessx.agent.JesseAgentApps;
import jessx.agent.Agent;

public class BasicAgent extends Agent
{
    public BasicAgent(final long pauseDuration) {
        super(pauseDuration);
    }
    
    @Override
    protected void experimentStarting() {
        JesseAgentApps.getLogger().info("Experiment starting");
        super.experimentStarting();
    }
    
    @Override
    protected void experimentEnding() {
        JesseAgentApps.getLogger().info("Experiment ending");
        super.experimentEnding();
    }
    
    @Override
    protected void periodStarting() {
        JesseAgentApps.getLogger().info("period starting");
    }
    
    @Override
    protected void periodEnding() {
        JesseAgentApps.getLogger().info("period ending");
    }
    
    @Override
    protected void runExperimentOff() {
        JesseAgentApps.getLogger().info("Experiment is off.");
    }
    
    @Override
    protected void runExperimentOnPeriodOff() {
        JesseAgentApps.getLogger().info("Experiment is on. Period is off.");
    }
    
    @Override
    protected void runExperimentOnPeriodOn() {
        JesseAgentApps.getLogger().info("Experiment is on. Period is on.");
    }
}
