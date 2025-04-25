// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.event;

public class InstitutionEvent
{
    public static final int INSTITUTION_REMOVED = 0;
    public static final int INSTITUTION_ADDED = 1;
    private String institutionName;
    private int event;
    
    public InstitutionEvent(final String institutionName, final int event) {
        this.institutionName = institutionName;
        this.event = event;
    }
    
    public String getInstitutionName() {
        return this.institutionName;
    }
    
    public int getEvent() {
        return this.event;
    }
}
