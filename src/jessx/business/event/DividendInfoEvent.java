// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.event;

public class DividendInfoEvent
{
    private String assetName;
    private String playerType;
    private int event;
    public static int DIVIDEND_INFO_UPDATED;
    
    static {
        DividendInfoEvent.DIVIDEND_INFO_UPDATED = 0;
    }
    
    public String getAssetName() {
        return this.assetName;
    }
    
    public String getPlayerType() {
        return this.playerType;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public DividendInfoEvent(final String assetName, final String playerType, final int event) {
        this.assetName = assetName;
        this.playerType = playerType;
        this.event = event;
    }
}
