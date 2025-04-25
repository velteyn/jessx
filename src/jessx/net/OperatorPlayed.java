// 
// Decompiled by Procyon v0.6.0
// 

package jessx.net;

import org.jdom.Element;

public class OperatorPlayed implements NetworkReadable, NetworkWritable
{
    private String operatorCompleteName;
    
    public String getOperatorName() {
        final int index = this.operatorCompleteName.lastIndexOf(" on ");
        return this.operatorCompleteName.substring(0, index);
    }
    
    public String getInstitutionName() {
        final int index = this.operatorCompleteName.lastIndexOf(" on ");
        return this.operatorCompleteName.substring(index + 4);
    }
    
    public OperatorPlayed(final String opCompleteName) {
        this.operatorCompleteName = opCompleteName;
    }
    
    public boolean initFromNetworkInput(final Element root) {
        if (root.getName().equals("OperatorPlayed")) {
            final String opName = root.getAttributeValue("name");
            if (opName != null) {
                this.operatorCompleteName = opName;
                return true;
            }
        }
        return false;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element opPlayed = new Element("OperatorPlayed");
        opPlayed.setAttribute("name", this.operatorCompleteName);
        return opPlayed;
    }
}
