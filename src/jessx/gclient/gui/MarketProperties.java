// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import org.jdom.Document;
import jessx.business.Operator;
import jessx.business.Deal;
import java.awt.Component;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import jessx.client.event.ConnectionListener;
import jessx.client.event.NetworkListener;
import jessx.client.event.ExperimentDeveloppmentListener;
import jessx.client.event.OperatorPlayedListener;
import jessx.utils.Constants;
import javax.swing.JPanel;

public class MarketProperties extends JPanel implements Constants, OperatorPlayedListener, ExperimentDeveloppmentListener, NetworkListener, ConnectionListener
{
    MarketEvolutionGraph evolutionGraph;
    TitledBorder titledBorder11;
    
    public MarketProperties() {
        this.evolutionGraph = new MarketEvolutionGraph();
        this.jbInit();
    }
    
    public void jbInit() {
        this.setBorder(this.titledBorder11 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "Market properties", 0, 0, MarketProperties.FONT_CLIENT_TITLE_BORDER));
        this.setMinimumSize(new Dimension(16, 10));
        this.setPreferredSize(new Dimension(291, 169));
        this.add(this.evolutionGraph);
    }
    
    public void addDeal(final Deal deal) {
        this.evolutionGraph.addDeal(deal);
    }
    
    public void initGraphic() {
        this.evolutionGraph.initGraphic();
    }
    
    public void addAssetEvolution(final String institutionName) {
        this.evolutionGraph.addAssetEvolution(institutionName);
    }
    
    public void newOperator(final Operator op) {
    }
    
    public void experimentBegins() {
    }
    
    public void experimentFinished() {
    }
    
    public void periodBegins() {
    }
    
    public void periodFinished() {
    }
    
    public void connectionStateChanged(final int newState) {
    }
    
    public void objectReceived(final Document xmlObject) {
    }
}
