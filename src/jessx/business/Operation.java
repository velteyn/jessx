// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

import jessx.utils.Utils;
import org.jdom.Element;
import jessx.net.NetworkWritable;
import jessx.net.NetworkReadable;

public abstract class Operation implements NetworkReadable, NetworkWritable
{
    private String emitter;
    private String institutionName;
    
    public String getEmitter() {
        return this.emitter;
    }
    
    public void setEmitter(final String emitter) {
        this.emitter = emitter;
    }
    
    public abstract float getOperationCost(final float p0, final float p1);
    
    public String getInstitutionName() {
        return this.institutionName;
    }
    
    public void setInstitutionName(final String institutionName) {
        this.institutionName = institutionName;
    }
    
    public abstract ClientInputPanel getClientPanel(final String p0);
    
    public abstract String getOperationName();
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element root = new Element("Operation");
        root.setAttribute("type", this.getOperationName());
        root.setAttribute("emitter", this.getEmitter());
        root.setAttribute("institution", this.institutionName);
        return root;
    }
    
    public boolean initFromNetworkInput(final Element root) {
        final String emitter = root.getAttributeValue("emitter");
        final String institution = root.getAttributeValue("institution");
        if (emitter == null) {
            Utils.logger.error("Invalid xml operation node: attribute emitter not found.");
            return false;
        }
        this.institutionName = institution;
        this.emitter = emitter;
        return true;
    }
    
    public abstract boolean isVisibleInTheClientPanel();
    
    public static Operation initOperationFromXml(final Element root) throws OperationNotCreatedException {
        final Operation op = OperationCreator.createOperation(root.getAttributeValue("type"));
        op.initFromNetworkInput(root);
        return op;
    }
}
