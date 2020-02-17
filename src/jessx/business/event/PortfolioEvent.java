// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.event;

public class PortfolioEvent
{
    public static final int CASH_UPDATED = 0;
    public static final int ASSET_UPDATED = 1;
    public static final int ASSET_ADDED = 2;
    public static final int ASSET_REMOVED = 3;
    public static final int ALL_UPDATED = 4;
    private String assetUpdated;
    private int event;
    
    public PortfolioEvent(final String assetUpdated, final int event) {
        this.assetUpdated = assetUpdated;
        this.event = event;
    }
    
    public PortfolioEvent(final int event) {
        this(null, event);
    }
    
    public String getAssetUpdated() {
        return this.assetUpdated;
    }
    
    public int getEvent() {
        return this.event;
    }
}
