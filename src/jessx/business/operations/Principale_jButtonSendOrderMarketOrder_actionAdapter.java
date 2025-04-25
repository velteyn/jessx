// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.operations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class Principale_jButtonSendOrderMarketOrder_actionAdapter implements ActionListener
{
    MarketOrderClientPanel adaptee;
    
    Principale_jButtonSendOrderMarketOrder_actionAdapter(final MarketOrderClientPanel adaptee) {
        this.adaptee = adaptee;
    }
    
    public void actionPerformed(final ActionEvent e) {
        this.adaptee.jButtonSendOrderMarketOrder_actionPerformed(e);
    }
}
