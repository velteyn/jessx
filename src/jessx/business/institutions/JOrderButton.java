// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.institutions;

import jessx.business.Order;
import javax.swing.JButton;

class JOrderButton extends JButton
{
    private Order order;
    
    public Order getOrder() {
        return this.order;
    }
    
    JOrderButton(final Order order) {
        this.order = order;
        this.setText("Delete");
    }
}
