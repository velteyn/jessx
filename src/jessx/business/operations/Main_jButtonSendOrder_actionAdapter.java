// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Main_jButtonSendOrder_actionAdapter implements ActionListener
{
    LimitOrderClientPanel adaptee;
    
    Main_jButtonSendOrder_actionAdapter(final LimitOrderClientPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        try {
            this.adaptee.jButtonSendOrder_actionPerformed(e);
        }
        catch (final Exception ex) {}
    }
}
