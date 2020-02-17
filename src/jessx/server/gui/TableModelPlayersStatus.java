package jessx.server.gui;

/***************************************************************/
/*                     SOFTWARE SECTION                        */
/***************************************************************/
/*
 * <p>Name: Jessx</p>
 * <p>Description: Financial Market Simulation Software</p>
 * <p>Licence: GNU General Public License</p>
 * <p>Organisation: EC Lille / USTL</p>
 * <p>Persons involved in the project : group T.E.A.M.</p>
 * <p>More details about this source code at :
 *    http://eleves.ec-lille.fr/~ecoxp03  </p>
 * <p>Current version: 1.0</p>
 */

/***************************************************************/
/*                      LICENCE SECTION                        */
/***************************************************************/
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/***************************************************************/
/*                       IMPORT SECTION                        */
/***************************************************************/

import java.util.*;

import javax.swing.*;
import javax.swing.table.*;

import jessx.business.*;
import jessx.business.event.*;
import jessx.server.net.*;

/***************************************************************/
/*         TableModelPlayersStatus CLASS SECTION               */
/***************************************************************/
/**
 * <p>Title : TableModelPlayersStatus</p>
 * <p>Description : </p>
 * @author Thierry Curtil
 * @version 1.0
 */

public class TableModelPlayersStatus extends AbstractTableModel implements PlayerListener, PlayerStateListener {

  private String[] columnNames = { "Player Login", "Player Type", "IP/Version java", "Online", "Ready", "Delete" }; //charles
  private Vector playersLogin = new Vector();

  public TableModelPlayersStatus() {
    NetworkCore.addListener(this);
  }

  /**
   * @return The class of the column choosen
   * @param column the number of the column
   */
  public Class getColumnClass(int column) {
    switch (column) {
     case 3:
       return JLabel.class;
     case 4:
       return JLabel.class;
     case 5:
       return JButton.class;
     default:
       return String.class;
   }
  }

  /**
   * @return A string containing the default name of each column
   * @param column the number of the column
   */
  public String getColumnName(int column) {
    return this.columnNames[column];
  }

  /**
   * @return The number of rows
   */
  public int getRowCount() {
    return NetworkCore.getPlayerList().size();
  }

  /**
 * @return The number of columns
 */
  public int getColumnCount() {
    return columnNames.length;
  }

  /**
   * @return The object in the choosen cell
   * @param rowIndex the number of the row to choose the cell
   * @param columnIndex the number of the column to choose the cell
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    if (columnIndex == 0) {
      return this.playersLogin.elementAt(rowIndex);
    }
    else if (columnIndex == 1) {
      return NetworkCore.getPlayer((String)this.playersLogin.elementAt(rowIndex)).getPlayerCategory();
    }
    else if (columnIndex == 2) {
      return NetworkCore.getPlayer( (String)this.playersLogin.elementAt(
          rowIndex)).getPlayerIP() + " / " +
          NetworkCore.getPlayer( (String)this.playersLogin.elementAt(
          rowIndex)).getJavaversion();
    }
    else if (columnIndex == 3) {
      JLabel label = new JLabel();
      label.setBackground((NetworkCore.getPlayer((String)this.playersLogin.elementAt(rowIndex)).getPlayerStatus() == Player.CLIENT_STATUS_CONNECTED)  ? jessx.utils.Constants.CLIENT_STATE_GREEN : jessx.utils.Constants.CLIENT_STATE_RED);
      return label;
    }
    else if (columnIndex == 4) {
      JLabel label = new JLabel();
      label.setBackground((NetworkCore.getPlayer((String)this.playersLogin.elementAt(rowIndex)).getPlayerState() == Player.CLIENT_READY)  ? jessx.utils.Constants.CLIENT_STATE_GREEN : jessx.utils.Constants.CLIENT_STATE_RED);
      return label;
    }
    else if (columnIndex == 5) {
      JButton deleteButton = new JButton();
      deleteButton.setText("Delete");
      return deleteButton;
    }
    else {
      return "";
    }
  }

  /**
   * @return The value true if the cell choosen is editable
   * @param rowIndex the number of the row to choose the cell
   * @param columnIndex the number of the column to choose the cell
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    return ( columnIndex == 1 || columnIndex == 5);
  }

  public void setValueAt(Object object, int rowIndex, int columnIndex) {
    if (columnIndex == 1 && object != null) {
      NetworkCore.getPlayer( (String)this.playersLogin.elementAt(rowIndex)).
          setPlayerCategory(object.toString());
    }
    else {
      // non editable columns...
    }
  }

  public void setDefaultPlayerCategory(Object object){
    int count = this.getRowCount();
    for (int i=0;i<count;i++){
      setValueAt(object, i, 1);
    }
  }

  public void playerListModified(PlayerEvent e) {
    if (e.getEvent() == PlayerEvent.PLAYER_ADDED) {
      this.playersLogin.add(e.getPlayerName());
      if (BusinessCore.getScenario().getPlayerTypes().size() == 1) {
        NetworkCore.getPlayer(e.getPlayerName()).setPlayerCategory( (
            String) BusinessCore.getScenario().
            getPlayerTypes().keySet().iterator().next());
      }
      this.fireTableRowsInserted(this.playersLogin.size() - 1,
                                 this.playersLogin.size() - 1);
      NetworkCore.getPlayer(e.getPlayerName()).addListener(this);

    }
    else if (e.getEvent() == PlayerEvent.PLAYER_REMOVED) {
      this.playersLogin.remove(e.getPlayerName());
      this.fireTableDataChanged();
    }
  }

  public void playerStateChanged(String login) {
    this.fireTableDataChanged();
  }
}
