// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.institutions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class OrderMarketSetupGui_KeepingOrderBook_actionAdapter implements ActionListener
{
    private OrderMarketSetupGui adaptee;
    
    OrderMarketSetupGui_KeepingOrderBook_actionAdapter(final OrderMarketSetupGui adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.KeepingOrderBook_actionPerformed(e);
    }
}
