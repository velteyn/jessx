// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.JPanel;

public class RobotsNode extends JPanel implements DisplayableNode
{
    public RobotsNode(final ExperimentSetupTree treeModel) {
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void setEditable() {
    }
    
    public void setbEditable() {
    }
    
    public void setUneditable() {
    }
    
    @Override
    public String toString() {
        return "Robot";
    }
}
