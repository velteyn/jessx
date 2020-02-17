// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.business.Institution;
import javax.swing.JPanel;

public class InstitutionNode implements DisplayableNode
{
    private JPanel panel;
    
    public InstitutionNode() {
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void setEditable() {
    }
    
    public void setUneditable() {
    }
    
    public InstitutionNode(final Institution instit) {
        this.panel = instit.getServerPanel();
    }
    
    public JPanel getPanel() {
        return this.panel;
    }
    
    @Override
    public String toString() {
        return this.panel.toString();
    }
    
    private void jbInit() throws Exception {
    }
}
