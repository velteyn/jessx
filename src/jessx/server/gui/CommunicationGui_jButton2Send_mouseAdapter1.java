// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

class CommunicationGui_jButton2Send_mouseAdapter1 extends MouseAdapter
{
    private CommunicationGui adaptee;
    
    CommunicationGui_jButton2Send_mouseAdapter1(final CommunicationGui adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
        this.adaptee.jButton2Send_mouseEntered(e);
    }
    
    @Override
    public void mouseClicked(final MouseEvent e) {
        this.adaptee.jButton2Send_mouseClicked(e);
    }
}
