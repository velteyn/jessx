// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils.gui;

import java.awt.Font;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JButton;

public class JButtonRenderer extends JButton implements TableCellRenderer
{
    public JButtonRenderer() {
        this.setOpaque(true);
    }
    
    public Component getTableCellRendererComponent(final JTable table, final Object jButton, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        String newtext;
        if (jButton != null && jButton instanceof JButton) {
            final Color newColor = ((JButton)jButton).getBackground();
            newtext = ((JButton)jButton).getText();
            final Font newFont = ((JButton)jButton).getFont();
            this.setBackground(newColor);
            this.setFont(newFont);
            this.setBorderPainted(false);
        }
        else {
            newtext = "";
        }
        this.setText(newtext);
        this.setHorizontalAlignment(0);
        this.setVerticalAlignment(0);
        return this;
    }
}
