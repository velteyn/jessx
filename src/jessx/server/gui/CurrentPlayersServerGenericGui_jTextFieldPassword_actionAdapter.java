// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CurrentPlayersServerGenericGui_jTextFieldPassword_actionAdapter implements ActionListener
{
    private ConnectionsPlayersServerGenericGui adaptee;
    
    CurrentPlayersServerGenericGui_jTextFieldPassword_actionAdapter(final ConnectionsPlayersServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jTextFieldPassword_actionPerformed(e);
    }
}
