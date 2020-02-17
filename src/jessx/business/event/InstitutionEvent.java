// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
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
