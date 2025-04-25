// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.event;

public class AssetEvent
{
    public static final int ASSET_REMOVED = 0;
    public static final int ASSET_ADDED = 1;
    private String assetName;
    private int event;
    
    public AssetEvent(final String assetName, final int event) {
        this.assetName = assetName;
        this.event = event;
    }
    
    public String getAssetName() {
        return this.assetName;
    }
    
    public int getEvent() {
        return this.event;
    }
}
