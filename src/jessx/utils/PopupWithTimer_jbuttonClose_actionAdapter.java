// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class PopupWithTimer_jbuttonClose_actionAdapter implements ActionListener
{
    PopupWithTimer adaptee;
    
    PopupWithTimer_jbuttonClose_actionAdapter(final PopupWithTimer adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jbuttonClose_actionPerformed(e);
    }
}
