// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.event;

import jessx.business.Dividend;

public class DividendEvent
{
    public static final int DIVIDEND_UPDATED = 0;
    private int period;
    private Dividend dividend;
    private int event;
    
    public DividendEvent(final Dividend value, final int period, final int event) {
        this.period = period;
        this.event = event;
        this.dividend = value;
    }
    
    public Dividend getDividend() {
        return this.dividend;
    }
    
    public int getPeriod() {
        return this.period;
    }
    
    public int getEvent() {
        return this.event;
    }
}
