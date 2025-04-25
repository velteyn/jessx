// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class GeneralParameterSetupGui_jSpinner1_changeAdapter implements ChangeListener
{
    private GeneralParameterSetupGui adaptee;
    
    GeneralParameterSetupGui_jSpinner1_changeAdapter(final GeneralParameterSetupGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void stateChanged(final ChangeEvent e) {
        this.adaptee.jSpinner1_stateChanged(e);
    }
}
