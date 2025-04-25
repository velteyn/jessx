// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils.gui;

import java.awt.Font;
import jessx.utils.Constants;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;

public class JLabelRenderer extends JLabel implements TableCellRenderer
{
    public JLabelRenderer() {
        this.setOpaque(true);
    }
    
    public Component getTableCellRendererComponent(final JTable table, final Object jLabel, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        Color newColor;
        String newtext;
        Font newFont;
        if (jLabel != null) {
            newColor = ((JLabel)jLabel).getBackground();
            newtext = ((JLabel)jLabel).getText();
            newFont = ((JLabel)jLabel).getFont();
        }
        else {
            newColor = new Color(255, 255, 255);
            newtext = "";
            newFont = Constants.FONT_DEFAULT_LABEL;
        }
        final Color newColor2 = new Color(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), 128);
        this.setBackground(newColor2);
        this.setText(newtext);
        this.setHorizontalAlignment(0);
        this.setVerticalAlignment(0);
        this.setFont(newFont);
        return this;
    }
}
