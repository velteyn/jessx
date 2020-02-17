// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
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
