// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.assets;

import java.awt.Component;
import javax.swing.JLabel;
import jessx.server.gui.DisplayableNode;
import javax.swing.JPanel;

public class StockSetupGui extends JPanel implements DisplayableNode
{
    JLabel jLabel1;
    private Stock stock;
    
    public StockSetupGui(final Stock aStock) {
        this.jLabel1 = new JLabel();
        this.stock = aStock;
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void setUneditable() {
    }
    
    public void setEditable() {
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    @Override
    public String toString() {
        return this.stock.getAssetName();
    }
    
    private void jbInit() throws Exception {
        this.jLabel1.setText("Nothing to parameter yet.");
        this.add(this.jLabel1, null);
    }
}
