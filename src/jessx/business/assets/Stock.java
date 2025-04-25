// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.assets;

import org.jdom.Element;
import javax.swing.JPanel;
import jessx.business.AssetCreator;
import jessx.business.Asset;

public class Stock extends Asset
{
    static {
        try {
            System.out.println("Loading stock...");
            AssetCreator.assetFactories.put("Stock", Class.forName("jessx.business.assets.Stock"));
        }
        catch (final ClassNotFoundException exception) {
            System.out.println("Unabled to locate the Stock class. Reason: bad class name spelling.");
            exception.printStackTrace();
        }
    }
    
    @Override
    public JPanel getAssetSetupGui() {
        return new StockSetupGui(this);
    }
    
    public void saveToXml(final Element node) {
    }
    
    public void loadFromXml(final Element node) {
    }
}
