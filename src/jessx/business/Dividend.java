// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business;

import org.jdom.Element;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;

public abstract class Dividend implements XmlLoadable, XmlExportable
{
    public abstract float getDividend();
    
    public abstract void setParameter(final int p0, final Object p1);
    
    public abstract Object getParameter(final int p0);
    
    public abstract int getParamCount();
    
    public abstract String[] getParamNames();
    
    public abstract Class getParamClass(final int p0);
    
    public abstract float getNormalValue();
    
    public abstract String getDetails();
    
    public abstract void saveToXml(final Element p0);
    
    public abstract void loadFromXml(final Element p0);
    
    public abstract Object clone();
}
