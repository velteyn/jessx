// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import jessx.utils.Utils;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NewMessageTimer extends Thread
{
    JTextArea jtextArea;
    JScrollPane jScrollPane;
    int previousTextAreaHeight;
    int windowHeight;
    int messageHeight;
    
    public NewMessageTimer(final JTextArea jTextAreaCommunication, final JScrollPane jScrollPaneCommunication, final int jTextAreaCommunicationHeightBeforeUpdate, final int heightOfWindow) {
        this.jtextArea = jTextAreaCommunication;
        this.jScrollPane = jScrollPaneCommunication;
        this.previousTextAreaHeight = jTextAreaCommunicationHeightBeforeUpdate;
        this.windowHeight = heightOfWindow;
    }
    
    @Override
    public void run() {
        try {
            this.messageHeight = this.jtextArea.getHeight() - this.previousTextAreaHeight;
            if (this.windowHeight >= this.messageHeight) {
                this.jScrollPane.getVerticalScrollBar().setValue(this.jScrollPane.getVerticalScrollBar().getMaximum());
            }
            else {
                this.jScrollPane.getVerticalScrollBar().setValue(this.jScrollPane.getVerticalScrollBar().getMaximum() - this.messageHeight + this.windowHeight);
            }
            this.jtextArea.setBackground(Color.RED);
            Thread.sleep(300L);
            this.jtextArea.setBackground(UIManager.getColor("Button.background").brighter());
            Thread.sleep(300L);
            this.jtextArea.setBackground(UIManager.getColor("Button.background").darker());
            Thread.sleep(300L);
            this.jtextArea.setBackground(UIManager.getColor("Button.background").brighter());
            Thread.sleep(300L);
            this.jtextArea.setBackground(UIManager.getColor("Button.background"));
        }
        catch (final InterruptedException ex) {
            Utils.logger.warn("NewMessageTimer sleep interrupted. " + ex.toString());
            this.jtextArea.setBackground(UIManager.getColor("Button.background"));
        }
    }
}
