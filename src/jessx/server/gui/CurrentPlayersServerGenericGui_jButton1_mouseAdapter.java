// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

class CurrentPlayersServerGenericGui_jButton1_mouseAdapter extends MouseAdapter
{
    private ConnectionsPlayersServerGenericGui adaptee;
    
    CurrentPlayersServerGenericGui_jButton1_mouseAdapter(final ConnectionsPlayersServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        this.adaptee.jButton1_mouseClicked(e);
    }
}
