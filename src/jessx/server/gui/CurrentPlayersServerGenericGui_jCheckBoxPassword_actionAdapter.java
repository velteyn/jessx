// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CurrentPlayersServerGenericGui_jCheckBoxPassword_actionAdapter implements ActionListener
{
    private ConnectionsPlayersServerGenericGui adaptee;
    
    CurrentPlayersServerGenericGui_jCheckBoxPassword_actionAdapter(final ConnectionsPlayersServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jCheckBoxPassword_actionPerformed(e);
    }
}
