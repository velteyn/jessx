// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Principale_jMenuHelpAbout_ActionAdapter implements ActionListener
{
    GClientFrame adaptee;
    
    Principale_jMenuHelpAbout_ActionAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuHelpAbout_actionPerformed(e);
    }
}
