// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import jessx.business.BusinessCore;
import jessx.business.event.PlayerEvent;
import jessx.utils.Constants;
import javax.swing.JButton;
import javax.swing.JLabel;
import jessx.server.net.NetworkCore;
import java.util.Vector;
import jessx.server.net.PlayerStateListener;
import jessx.business.event.PlayerListener;
import javax.swing.table.AbstractTableModel;

public class TableModelPlayersStatus extends AbstractTableModel implements PlayerListener, PlayerStateListener
{
    private String[] columnNames;
    private Vector playersLogin;
    
    public TableModelPlayersStatus() {
        this.columnNames = new String[] { "Player Login", "Player Type", "IP/Version java", "Online", "Ready", "Delete" };
        this.playersLogin = new Vector();
        NetworkCore.addListener(this);
    }
    
    @Override
    public Class getColumnClass(final int column) {
        switch (column) {
            case 3: {
                return JLabel.class;
            }
            case 4: {
                return JLabel.class;
            }
            case 5: {
                return JButton.class;
            }
            default: {
                return String.class;
            }
        }
    }
    
    @Override
    public String getColumnName(final int column) {
        return this.columnNames[column];
    }
    
    public int getRowCount() {
        return NetworkCore.getPlayerList().size();
    }
    
    public int getColumnCount() {
        return this.columnNames.length;
    }
    
    public Object getValueAt(final int rowIndex, final int columnIndex) {
        if (columnIndex == 0) {
            return this.playersLogin.elementAt(rowIndex);
        }
        if (columnIndex == 1) {
            return NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).getPlayerCategory();
        }
        if (columnIndex == 2) {
            return String.valueOf(NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).getPlayerIP()) + " / " + NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).getJavaversion();
        }
        if (columnIndex == 3) {
            final JLabel label = new JLabel();
            label.setBackground((NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).getPlayerStatus() == 1) ? Constants.CLIENT_STATE_GREEN : Constants.CLIENT_STATE_RED);
            return label;
        }
        if (columnIndex == 4) {
            final JLabel label = new JLabel();
            label.setBackground((NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).getPlayerState() == 0) ? Constants.CLIENT_STATE_GREEN : Constants.CLIENT_STATE_RED);
            return label;
        }
        if (columnIndex == 5) {
            final JButton deleteButton = new JButton();
            deleteButton.setText("Delete");
            return deleteButton;
        }
        return "";
    }
    
    @Override
    public boolean isCellEditable(final int rowIndex, final int columnIndex) {
        return columnIndex == 1 || columnIndex == 5;
    }
    
    @Override
    public void setValueAt(final Object object, final int rowIndex, final int columnIndex) {
        if (columnIndex == 1 && object != null) {
            NetworkCore.getPlayer((String) this.playersLogin.elementAt(rowIndex)).setPlayerCategory(object.toString());
        }
    }
    
    public void setDefaultPlayerCategory(final Object object) {
        for (int count = this.getRowCount(), i = 0; i < count; ++i) {
            this.setValueAt(object, i, 1);
        }
    }
    
    public void playerListModified(final PlayerEvent e) {
        if (e.getEvent() == PlayerEvent.PLAYER_ADDED) {
            this.playersLogin.add(e.getPlayerName());
            if (BusinessCore.getScenario().getPlayerTypes().size() == 1) {
                NetworkCore.getPlayer(e.getPlayerName()).setPlayerCategory((String) BusinessCore.getScenario().getPlayerTypes().keySet().iterator().next());
            }
            this.fireTableRowsInserted(this.playersLogin.size() - 1, this.playersLogin.size() - 1);
            NetworkCore.getPlayer(e.getPlayerName()).addListener(this);
        }
        else if (e.getEvent() == PlayerEvent.PLAYER_REMOVED) {
            this.playersLogin.remove(e.getPlayerName());
            this.fireTableDataChanged();
        }
    }
    
    public void playerStateChanged(final String login) {
        this.fireTableDataChanged();
    }
}
