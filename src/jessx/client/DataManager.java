// 
// Decompiled by Procyon v0.6.0
// 

package jessx.client;

import jessx.business.Operator;
import jessx.business.Institution;
import jessx.utils.Utils;
import jessx.net.OperatorPlayed;
import org.jdom.Document;
import jessx.client.event.NetworkListener;

public class DataManager implements NetworkListener
{
    public DataManager() {
        ClientCore.addNetworkListener(this, "Institution");
        ClientCore.addNetworkListener(this, "Portfolio");
        ClientCore.addNetworkListener(this, "OperatorPlayed");
    }
    
    public void objectReceived(final Document doc) {
        if (doc.getRootElement().getName().equals("Portfolio")) {
            ClientCore.getPortfolio().initFromNetworkInput(doc.getRootElement());
        }
        else if (doc.getRootElement().getName().equals("OperatorPlayed")) {
            final OperatorPlayed opPlayed = new OperatorPlayed("");
            if (opPlayed.initFromNetworkInput(doc.getRootElement())) {
                final Institution inst = ClientCore.getInstitution(opPlayed.getInstitutionName());
                if (inst != null) {
                    final Operator op = inst.getOperator(opPlayed.getOperatorName());
                    if (op != null) {
                        ClientCore.addOperatorPlayed(op);
                    }
                    else {
                        Utils.logger.warn("The operator has not been found on the institution given. (" + opPlayed.getInstitutionName() + ", " + opPlayed.getOperatorName() + ")");
                    }
                }
                else {
                    Utils.logger.warn("Operator plays on an institution we did not have. (" + opPlayed.getInstitutionName() + ", " + opPlayed.getOperatorName() + ")");
                }
            }
        }
        else if (doc.getRootElement().getName().equals("Institution")) {
            final Institution instit = Institution.loadInstitutionFromXml(doc.getRootElement());
            ClientCore.addInstitution(instit);
        }
    }
}
