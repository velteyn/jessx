// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils;

import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;

public class PopupListener extends MouseAdapter
{
    JPopupMenu popup;
    
    public PopupListener(final JPopupMenu popup) {
        this.popup = popup;
    }
    
    @Override
    public void mousePressed(final MouseEvent e) {
        this.maybeShowPopup(e);
    }
    
    @Override
    public void mouseReleased(final MouseEvent e) {
        this.maybeShowPopup(e);
    }
    
    private void maybeShowPopup(final MouseEvent e) {
        if (e.isPopupTrigger()) {
            this.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }
}
