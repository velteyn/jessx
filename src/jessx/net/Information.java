// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.net;

import org.jdom.Element;

public class Information implements NetworkWritable, NetworkReadable
{
    private String information;
    
    public String getInformation() {
        return this.information;
    }
    
    public Information(final String message) {
        this.information = message;
    }
    
    public boolean initFromNetworkInput(final Element node) {
        if (!node.getName().equals("Information")) {
            return false;
        }
        this.information = node.getText();
        return true;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element node = new Element("Information");
        return node.setText(this.information);
    }
}
