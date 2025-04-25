// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OperationPane_operationsButtons_actionAdapter implements ActionListener
{
    OperationPane adaptee;
    
    OperationPane_operationsButtons_actionAdapter(final OperationPane adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.operationsButtons_actionPerformed(e);
    }
}
