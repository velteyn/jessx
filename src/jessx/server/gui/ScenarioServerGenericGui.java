// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import javax.swing.JPanel;

public class ScenarioServerGenericGui extends JPanel implements DisplayableNode
{
    public ScenarioServerGenericGui(final ExperimentSetupTree treeModel) {
    }
    
    public void setEditable() {
    }
    
    public void setUneditable() {
    }
    
    @Override
    public String toString() {
        return "Scenario";
    }
    
    public JPanel getPanel() {
        return this;
    }
}
