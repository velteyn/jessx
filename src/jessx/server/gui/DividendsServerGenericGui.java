// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.JPanel;

public class DividendsServerGenericGui extends JPanel implements DisplayableNode
{
    public void setEditable() {
    }
    
    public void setUneditable() {
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return "Asset parameters";
    }
}
