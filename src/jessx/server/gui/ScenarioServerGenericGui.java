// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
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
