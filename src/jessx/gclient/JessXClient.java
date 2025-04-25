// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient;

import java.awt.Dimension;
import java.awt.Toolkit;
import jessx.gclient.gui.GClientFrame;

public class JessXClient
{
    boolean packFrame;
    
    public JessXClient() {
        this.packFrame = false;
        final GClientFrame frame = new GClientFrame();
        if (this.packFrame) {
            frame.pack();
        }
        else {
            frame.validate();
        }
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
    }
    
    public static void main(final String[] args) {
        new JessXClient();
    }
}
