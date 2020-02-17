// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils;

import java.awt.event.WindowEvent;
import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.JDialog;

class Dialogue extends JDialog
{
    boolean closeEvent;
    
    public Dialogue(final JFrame parent) {
        super(parent);
        this.closeEvent = false;
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == 201) {
            this.closeEvent = true;
        }
    }
}
