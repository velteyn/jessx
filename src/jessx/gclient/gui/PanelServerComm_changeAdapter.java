// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class PanelServerComm_changeAdapter implements ChangeListener
{
    PanelServerComm adaptee;
    
    PanelServerComm_changeAdapter(final PanelServerComm adaptee) {
        this.adaptee = adaptee;
    }
    
    public void stateChanged(final ChangeEvent e) {
        this.adaptee.PanelServerComm_stateChanged(e);
    }
}
