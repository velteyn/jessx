// 
// Decompiled by Procyon v0.6.0
// 

package jessx.agent;

import jessx.agent.impl.AgentTest;
import org.apache.log4j.Logger;

public class JesseAgentApps
{
    boolean packFrame;
    private static Logger logger;
    
    static {
        JesseAgentApps.logger = Logger.getLogger(JesseAgentApps.class.getClass());
    }
    
    public static Logger getLogger() {
        return JesseAgentApps.logger;
    }
    
    public JesseAgentApps(final int nbOrder, final int nbDeal) {
        this.packFrame = false;
        new AgentTest(nbOrder, nbDeal);
    }
    
    public static void main(final String[] args) {
        if (args.length != 2) {
            System.out.println("Usage :");
            System.out.println("java -jar JesseAgent.jar nbOrder nbDeal");
            System.out.println("Ou nbOrder sera le nombre d'ordre passes par minute");
            System.out.println("et nbDeal sera le nombre minimum de deals par minute.");
            return;
        }
        new JesseAgentApps(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
    }
}
