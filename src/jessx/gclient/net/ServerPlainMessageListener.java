// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.net;

import jessx.utils.PopupWithTimer;
import javax.swing.border.Border;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import org.jdom.Document;
import jessx.client.ClientCore;
import javax.swing.JFrame;
import jessx.client.event.NetworkListener;

public class ServerPlainMessageListener implements NetworkListener
{
    private JFrame parent;
    
    public ServerPlainMessageListener() {
        try {
            this.jbInit();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public ServerPlainMessageListener(final JFrame frame) {
        this.parent = frame;
        ClientCore.addNetworkListener(this, "Message");
    }
    
    public void objectReceived(final Document doc) {
        if (doc.getRootElement().getName().equals("Message")) {
            final String message = doc.getRootElement().getText();
            final int rows = message.length() / 50 + 1;
            final int columns = (rows < 2) ? message.length() : 50;
            final JTextArea jTextArea = new JTextArea(message, rows, columns);
            final JScrollPane jScrollPane = new JScrollPane();
            jScrollPane.getViewport().add(jTextArea, null);
            jTextArea.setEditable(false);
            jTextArea.setOpaque(false);
            jTextArea.setAutoscrolls(true);
            jTextArea.setVisible(true);
            jScrollPane.doLayout();
            jTextArea.setBackground(new Color(203, 230, 211));
            if (doc.getRootElement().getText().equals("Connection accepted.")) {
                jScrollPane.setBorder(null);
            }
            new PopupWithTimer(120, jScrollPane, jTextArea.getPreferredSize(), "JessX Server", this.parent).run();
        }
    }
    
    private void jbInit() throws Exception {
    }
}
