// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter implements ActionListener
{
    MarketOrderClientPanel adaptee;
    
    ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter(final MarketOrderClientPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jToggleButtonBuyMarketOrder_actionPerformed(e);
    }
}
