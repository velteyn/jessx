// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class GClientFrame_jTabbedPaneGeneral_changeAdapter implements ChangeListener
{
    GClientFrame adaptee;
    
    GClientFrame_jTabbedPaneGeneral_changeAdapter(final GClientFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void stateChanged(final ChangeEvent e) {
        this.adaptee.jTabbedPaneGeneral_stateChanged(e);
    }
}
