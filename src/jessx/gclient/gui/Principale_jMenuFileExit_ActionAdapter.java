// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Principale_jMenuFileExit_ActionAdapter implements ActionListener
{
    GClientFrame adaptee;
    
    Principale_jMenuFileExit_ActionAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuFileExit_actionPerformed(e);
    }
}
