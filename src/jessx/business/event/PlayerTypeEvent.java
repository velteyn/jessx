// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.event;

import jessx.business.Operator;
import jessx.business.PlayerType;

public class PlayerTypeEvent
{
    public static final int PLAYER_REMOVED = 0;
    public static final int PLAYER_ADDED = 1;
    public static final int OPERATOR_GRANTED = 2;
    public static final int OPERATOR_DENIED = 3;
    private int event;
    private PlayerType playerType;
    private Operator operator;
    
    public Operator getOperator() {
        return this.operator;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public PlayerType getPlayerType() {
        return this.playerType;
    }
    
    public PlayerTypeEvent(final PlayerType playerType, final int event, final Operator oper) {
        this.playerType = playerType;
        this.event = event;
        this.operator = oper;
    }
}
