// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.net;

import org.jdom.Element;

public class Message implements NetworkWritable, NetworkReadable
{
    private String message;
    
    public String getMessage() {
        return this.message;
    }
    
    public Message(final String message) {
        this.message = message;
    }
    
    public boolean initFromNetworkInput(final Element node) {
        if (!node.getName().equals("Message")) {
            return false;
        }
        this.message = node.getText();
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element node = new Element("Message");
        return node.setText(this.message);
    }
}
