// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

class CurrentPlayersServerGenericGui_jButton2_mouseAdapter extends MouseAdapter
{
    private ConnectionsPlayersServerGenericGui adaptee;
    
    CurrentPlayersServerGenericGui_jButton2_mouseAdapter(final ConnectionsPlayersServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        this.adaptee.jButton2_mouseClicked(e);
    }
}
