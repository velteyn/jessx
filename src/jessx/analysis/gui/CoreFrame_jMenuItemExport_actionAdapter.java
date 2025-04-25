// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CoreFrame_jMenuItemExport_actionAdapter implements ActionListener
{
    CoreFrame adaptee;
    
    CoreFrame_jMenuItemExport_actionAdapter(final CoreFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuItemExport_actionPerformed(e);
    }
}
