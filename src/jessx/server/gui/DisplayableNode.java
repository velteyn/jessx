// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import javax.swing.JPanel;

public interface DisplayableNode
{
    JPanel getPanel();
    
    void setEditable();
    
    void setUneditable();
}
