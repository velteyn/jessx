// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.event;

public class PlayerEvent
{
    public static int PLAYER_ADDED;
    public static int PLAYER_REMOVED;
    private String playerName;
    private int event;
    
    static {
        PlayerEvent.PLAYER_ADDED = 0;
        PlayerEvent.PLAYER_REMOVED = 1;
    }
    
    public String getPlayerName() {
        return this.playerName;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public PlayerEvent(final String playerName, final int event) {
        this.playerName = playerName;
        this.event = event;
    }
}
