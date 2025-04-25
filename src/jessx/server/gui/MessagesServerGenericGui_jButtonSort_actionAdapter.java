// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MessagesServerGenericGui_jButtonSort_actionAdapter implements ActionListener
{
    private MessagesServerGenericGui adaptee;
    
    MessagesServerGenericGui_jButtonSort_actionAdapter(final MessagesServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jButtonSort_actionPerformed(e);
    }
}
