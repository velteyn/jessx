// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientFrame_jMenuItem2_actionAdapter implements ActionListener
{
    GClientFrame adaptee;
    
    ClientFrame_jMenuItem2_actionAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuItem2_actionPerformed(e);
    }
}
