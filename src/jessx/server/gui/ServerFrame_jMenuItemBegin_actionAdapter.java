// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ServerFrame_jMenuItemBegin_actionAdapter implements ActionListener
{
    GeneralServerFrame adaptee;
    
    ServerFrame_jMenuItemBegin_actionAdapter(final GeneralServerFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuItemBegin_actionPerformed(e);
    }
}
