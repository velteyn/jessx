// 
// Decompiled by Procyon v0.6.0
// 

package jessx.net;

import org.jdom.Element;
import java.io.Serializable;

public interface NetworkWritable extends Serializable
{
    Element prepareForNetworkOutput(final String p0);
}
