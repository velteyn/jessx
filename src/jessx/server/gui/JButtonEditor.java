// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.Component;
import javax.swing.JTable;
import jessx.business.BusinessCore;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;

class JButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener
{
    JButton button;
    TableModelMessages tableModelMessages1;
    
    public JButtonEditor(final TableModelMessages tableModelMessages) {
        this.button = new JButton();
        this.tableModelMessages1 = tableModelMessages;
    }
    
    public void actionPerformed(final ActionEvent e) {
        BusinessCore.getScenario().removeInformation(Integer.parseInt(e.getActionCommand().toString()));
    }
    
    public Object getCellEditorValue() {
        return this.button;
    }
    
    public Component getTableCellEditorComponent(final JTable table, final Object jbutton, final boolean isSelected, final int row, final int column) {
        this.button = (JButton)jbutton;
        if (this.button != null) {
            this.button.setActionCommand(Integer.toString(row));
            this.button.addActionListener(this);
        }
        return this.button;
    }
}
