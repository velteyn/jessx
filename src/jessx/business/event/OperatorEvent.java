// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.event;

public class OperatorEvent
{
    public static final int OPERATOR_REMOVED = 0;
    public static final int OPERATOR_ADDED = 1;
    private String operatorName;
    private String institutionName;
    private int event;
    
    public OperatorEvent(final String operatorName, final String institutionName, final int event) {
        this.operatorName = operatorName;
        this.event = event;
        this.institutionName = institutionName;
    }
    
    public String getOperatorName() {
        return this.operatorName;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public String getInsitutionName() {
        return this.institutionName;
    }
}
