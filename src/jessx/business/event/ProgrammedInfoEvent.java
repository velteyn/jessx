// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.event;

public class ProgrammedInfoEvent
{
    public static final int PROGRAMMEDINFO_REMOVED = 0;
    public static final int PROGRAMMEDINFO_ALLREMOVED = 1;
    public static final int PROGRAMMEDINFO_ADDED = 2;
    public static final int PROGRAMMEDINFO_LISTLOADED = 3;
    private int event;
    private Object programmedInfoObject;
    
    public Object getProgrammedInfoObject() {
        return this.programmedInfoObject;
    }
    
    public int getEvent() {
        return this.event;
    }
    
    public ProgrammedInfoEvent(final Object PgInfo, final int event) {
        this.programmedInfoObject = PgInfo;
        this.event = event;
    }
}
