// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class GClientFrame_jTabbedPaneServerComm_changeAdapter implements ChangeListener
{
    GClientFrame adaptee;
    
    GClientFrame_jTabbedPaneServerComm_changeAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void stateChanged(final ChangeEvent e) {
        this.adaptee.jTabbedPaneServerComm_stateChanged(e);
    }
}
