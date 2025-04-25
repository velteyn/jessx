// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;
import jessx.server.gui.AssetServerGenericGui;
import javax.swing.JPanel;
import jessx.utils.XmlLoadable;
import jessx.utils.XmlExportable;

public abstract class Asset implements XmlExportable, XmlLoadable
{
    private String name;
    private DividendModel dividendModel;
    private String activity;
    
    public Asset() {
        this.dividendModel = new DividendModel();
    }
    
    public void setAssetName(final String assetname) {
        this.name = assetname;
    }
    
    public void setDividendModel(final DividendModel divModel) {
        this.dividendModel = divModel;
    }
    
    public DividendModel getDividendModel() {
        return this.dividendModel;
    }
    
    public String getAssetName() {
        return this.name;
    }
    
    public static JPanel getAssetServerGenericGui() {
        return new AssetServerGenericGui();
    }
    
    public abstract JPanel getAssetSetupGui();
    
    public JPanel getPanel() {
        return this.getAssetSetupGui();
    }
    
    @Override
    public String toString() {
        return this.getAssetName();
    }
    
    public String getAssetType() {
        return this.getClass().toString().substring(this.getClass().toString().lastIndexOf(".") + 1);
    }
    
    public void setAssetActivity(final String assetActivity) {
        this.activity = assetActivity;
    }
    
    public String getAssetActivity() {
        return this.activity;
    }
    
    public static void saveAssetToXml(final Element node, final Asset assetToSave) {
        node.setAttribute("type", assetToSave.getAssetType()).setAttribute("name", assetToSave.getAssetName());
        final Element divModelNode = new Element("DividendModel");
        assetToSave.getDividendModel().saveToXml(divModelNode);
        node.addContent(divModelNode);
        assetToSave.saveToXml(node);
    }
    
    public static Asset loadAssetFromXml(final Element node) {
        final String assetType = node.getAttributeValue("type");
        if (assetType == null) {
            Utils.logger.error("Invlid xml: no asset type in asset definition.");
            return null;
        }
        Asset asset;
        try {
            asset = AssetCreator.createAsset(assetType);
        }
        catch (final AssetNotCreatedException ex) {
            Utils.logger.error("Asset type not found on server: " + ex.toString());
            return null;
        }
        final String assetName = node.getAttributeValue("name");
        if (assetName == null) {
            Utils.logger.error("Invalid asset definition in xml files: no asset name given.");
            return null;
        }
        asset.setAssetName(assetName);
        final DividendModel divModel = new DividendModel();
        divModel.loadFromXml(node.getChild("DividendModel"));
        asset.setDividendModel(divModel);
        return asset;
    }
}
