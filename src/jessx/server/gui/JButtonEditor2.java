// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import javax.swing.JTable;
import jessx.server.net.NetworkCore;
import java.awt.Component;
import javax.swing.JOptionPane;
import jessx.utils.Utils;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.AbstractCellEditor;

class JButtonEditor2 extends AbstractCellEditor implements TableCellEditor, ActionListener
{
    JButton button;
    PlayersTypeServerGenericGui playersTypeServerGenericGui1;
    TableModelPlayersStatus tableModelPlayersStatus;
    JFrame parentFrame;
    
    public JButtonEditor2(final TableModelPlayersStatus tableModelPlayers, final JFrame parentFrame) {
        this.button = new JButton();
        this.tableModelPlayersStatus = tableModelPlayers;
        this.parentFrame = parentFrame;
    }
    
    public void actionPerformed(final ActionEvent e) {
        final String playerToRemove = new String((String)this.tableModelPlayersStatus.getValueAt(Integer.parseInt(e.getActionCommand().toString()), 0));
        Utils.logger.warn("Player removed" + playerToRemove);
        if (JOptionPane.showConfirmDialog(this.parentFrame, "Do you really want to delete this client?", "JessX Client", 2, 2) == 0) {
            NetworkCore.removePlayer(playerToRemove);
        }
        Utils.logger.warn("A Player has been deleted :" + playerToRemove);
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
