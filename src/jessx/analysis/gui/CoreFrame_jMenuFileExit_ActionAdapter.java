// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class CoreFrame_jMenuFileExit_ActionAdapter implements ActionListener
{
    CoreFrame adaptee;
    
    CoreFrame_jMenuFileExit_ActionAdapter(final CoreFrame adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jMenuFileExit_actionPerformed(e);
    }
}
