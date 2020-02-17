// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientFrame_jToggleButtonSell_actionAdapter implements ActionListener
{
    LimitOrderClientPanel adaptee;
    
    ClientFrame_jToggleButtonSell_actionAdapter(final LimitOrderClientPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jToggleButtonSell_actionPerformed(e);
    }
}
