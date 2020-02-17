// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
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
