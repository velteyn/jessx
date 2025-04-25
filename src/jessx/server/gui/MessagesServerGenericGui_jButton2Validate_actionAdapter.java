// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MessagesServerGenericGui_jButton2Validate_actionAdapter implements ActionListener
{
    private MessagesServerGenericGui adaptee;
    
    MessagesServerGenericGui_jButton2Validate_actionAdapter(final MessagesServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jButton2Validate_actionPerformed(e);
    }
}
