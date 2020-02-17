// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import jessx.business.Asset;
import javax.swing.JPanel;

public class AssetNode implements DisplayableNode
{
    private JPanel panel;
    
    public AssetNode() {
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public AssetNode(final Asset asset) {
        this.panel = asset.getPanel();
    }
    
    public void setEditable() {
    }
    
    public void setUneditable() {
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
