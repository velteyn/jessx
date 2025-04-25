// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import javax.swing.UIManager;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import jessx.net.DividendInfo;
import org.jdom.Document;
import java.util.Vector;
import jessx.client.ClientCore;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import jessx.utils.Constants;
import jessx.client.event.NetworkListener;
import javax.swing.JPanel;

public class InstitutionInfoPane extends JPanel implements NetworkListener, Constants
{
    private String assetName;
    private String opCompleteName;
    private String institutionName;
    private JScrollPane jScrollPane1;
    private JSplitPane jSplitPane1;
    JTextArea jTextArea1;
    GridBagLayout gridBagLayout1;
    
    public InstitutionInfoPane(final String opCompleteName) {
        this.jScrollPane1 = new JScrollPane();
        this.jSplitPane1 = new JSplitPane();
        this.jTextArea1 = new JTextArea();
        this.gridBagLayout1 = new GridBagLayout();
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        final int index = opCompleteName.lastIndexOf(" on ");
        this.opCompleteName = opCompleteName;
        this.institutionName = opCompleteName.substring(index + 4);
        this.assetName = ClientCore.getInstitution(this.institutionName).getAssetName();
        ClientCore.addNetworkListener(this, "DividendInfo");
    }
    
    public String institutionReport() {
        String message = ":: Operations Costs on " + this.institutionName + " ::\n";
        final Vector grantedOperations = ClientCore.getOperator(this.opCompleteName).getGrantedOperations();
        for (int i = 0; i < grantedOperations.size(); ++i) {
            final String operation = grantedOperations.elementAt(i).toString();
            message = String.valueOf(message) + "* " + operation + " : \n";
            final String percentageCost = Float.toString(ClientCore.getInstitution(this.institutionName).getPercentageCost(operation));
            final String minimalCost = Float.toString(ClientCore.getInstitution(this.institutionName).getMinimalCost(operation));
            message = String.valueOf(message) + "  - Percentage cost : " + percentageCost + "\n  - Minimal cost : " + minimalCost + "\n\n";
        }
        message = String.valueOf(message) + "_______________\n\n";
        return message;
    }
    
    public void objectReceived(final Document xmlDoc) {
        if (xmlDoc.getRootElement().getName().equals("DividendInfo")) {
            final DividendInfo divInfo = new DividendInfo();
            divInfo.initFromNetworkInput(xmlDoc.getRootElement());
            if (divInfo.getAssetName().equals(this.assetName)) {
                if (divInfo.getIsShowingOperationsCosts()) {
                    this.jTextArea1.setText(String.valueOf(this.institutionReport()) + divInfo.produceInfoReport());
                }
                else {
                    this.jTextArea1.setText(divInfo.produceInfoReport());
                }
            }
        }
    }
    
    private void jbInit() throws Exception {
        final GridBagLayout gridBagLayout1 = new GridBagLayout();
        this.setLayout(gridBagLayout1);
        this.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 2, 4), 0, 0));
        this.jTextArea1.setBackground(UIManager.getColor("Panel.background"));
        this.jTextArea1.setEnabled(true);
        this.jTextArea1.setEditable(false);
        this.jTextArea1.setLineWrap(true);
        this.jTextArea1.setWrapStyleWord(true);
        this.jTextArea1.setFont(InstitutionInfoPane.FONT_CLIENT_TEXTAREA);
        this.setLayout(gridBagLayout1);
        this.jScrollPane1.getViewport().add(this.jTextArea1, null);
    }
}
