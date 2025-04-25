// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientFrame_jMenuItem1_actionAdapter implements ActionListener
{
    GClientFrame adaptee;
    
    ClientFrame_jMenuItem1_actionAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuItem1_actionPerformed(e);
    }
}
