// 
// Decompiled by Procyon v0.6.0
// 

package jessx.net;

import org.jdom.Element;

public class Initialisation implements NetworkWritable, NetworkReadable
{
    public boolean initFromNetworkInput(final Element node) {
        return node.getName().equals("Initialisation");
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        return new Element("Initialisation");
    }
}
