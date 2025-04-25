// 
// Decompiled by Procyon v0.6.0
// 

package jessx.net;

import org.jdom.Element;

public class WarnForClient implements NetworkWritable, NetworkReadable
{
    private String warn;
    
    public String getWarn() {
        return this.warn;
    }
    
    public WarnForClient(final String message) {
        this.warn = message;
    }
    
    public boolean initFromNetworkInput(final Element node) {
        if (!node.getName().equals("Warn")) {
            return false;
        }
        this.warn = node.getText();
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element node = new Element("Warn");
        return node.setText(this.warn);
    }
}
