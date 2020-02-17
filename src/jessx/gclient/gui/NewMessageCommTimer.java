// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import jessx.utils.Utils;
import java.awt.Color;
import javax.swing.JTabbedPane;

public class NewMessageCommTimer extends Thread
{
    JTabbedPane jTabbedPane;
    int index;
    
    public NewMessageCommTimer(final JTabbedPane jTabbedPaneServerComm, final int indexcomm) {
        this.jTabbedPane = jTabbedPaneServerComm;
        this.index = indexcomm;
    }
    
    @Override
    public void run() {
        try {
            this.jTabbedPane.setForegroundAt(this.index, Color.red);
            Thread.sleep(400L);
            this.jTabbedPane.setForegroundAt(this.index, Color.black);
            Thread.sleep(400L);
            this.jTabbedPane.setForegroundAt(this.index, Color.red);
            Thread.sleep(400L);
            this.jTabbedPane.setForegroundAt(this.index, Color.black);
            Thread.sleep(400L);
            this.jTabbedPane.setForegroundAt(this.index, Color.red);
            Thread.sleep(400L);
            if (this.jTabbedPane.getSelectedIndex() == this.index) {
                this.jTabbedPane.setForegroundAt(this.index, Color.black);
            }
            else {
                this.jTabbedPane.setForegroundAt(this.index, Color.red);
            }
        }
        catch (InterruptedException ex) {
            Utils.logger.warn("NewMessageCommTimer sleep interrupted. " + ex.toString());
            this.jTabbedPane.setForegroundAt(this.index, Color.black);
        }
    }
}
