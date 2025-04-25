// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

class MessagesServerGenericGui_jComboBox2inTable_mouseAdapter extends MouseAdapter
{
    private MessagesServerGenericGui adaptee;
    
    MessagesServerGenericGui_jComboBox2inTable_mouseAdapter(final MessagesServerGenericGui adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public void mouseEntered(final MouseEvent e) {
        this.adaptee.jComboBox2inTable_mouseEntered(e);
    }
}
