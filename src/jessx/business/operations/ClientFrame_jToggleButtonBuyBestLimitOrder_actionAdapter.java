// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientFrame_jToggleButtonBuyBestLimitOrder_actionAdapter implements ActionListener
{
    BestLimitOrderClientPanel adaptee;
    
    ClientFrame_jToggleButtonBuyBestLimitOrder_actionAdapter(final BestLimitOrderClientPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jToggleButtonBuyBestLimitOrder_actionPerformed(e);
    }
}
