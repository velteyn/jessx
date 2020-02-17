package jessx.analysis.tools;

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

import javax.swing.table.*;

import org.jdom.*;
import jessx.utils.*;

/***************************************************************/
/*         TableModelPlayersResults CLASS SECTION              */
/***************************************************************/

/**
 * <p>Title: TableModelPlayersResults</p>
 * <p>Description : This class enables to create a TableModel
 * with a Document document coming from an experiment creating with jessx
 * and show the results of each player. A right document XML is necessary
 * to understand the following functions. You can have one using the
 * software jessx with several clients and a server.</p>
 * @author Christophe Grosjean
 * @version 1.0
 */

public class TableModelPlayersResults
    extends AbstractTableModel {
  /**
   * The number of columns
   */
  private int columnCount;
  /**
   * The number of columns
   */
  private int rowCount = 0;
  /**
   * Contains the name of the columns 3 to the end
   * @see getColumnName
   */
  private Vector assetsName = new Vector();
  private HashMap endPortfolios = new HashMap();
  private HashMap endOwnings = new HashMap();
  /**
   * This Hashmap give for a string which is the name of an asset the quantity
   * on the market when the experiment is ended
   */
  private HashMap totalQuantity = new HashMap();
  /**
   * Contains the sum of cash on the market when the experiment is ended
   */
  private float sumOfCash = 0;

  /**
   * @param document A right Document XML coming from the software jessx after an experiment
   */
  public TableModelPlayersResults(Document document) {
    Element experimentNode = document.getRootElement();
    Utils.logger.debug(experimentNode.getChild("Setup"));
    if (experimentNode.getChild("Setup") == null) {
      Utils.logger.debug("Wrong file, Setup doesn't exists");
    }
    List assets = experimentNode.getChild("Setup").getChildren("Asset");
    Iterator assetsIter = assets.iterator();
    while (assetsIter.hasNext()) {
      Element asset = (Element) assetsIter.next();
      String assetName = asset.getAttributeValue("name");
      assetsName.add(assetName);
      totalQuantity.put(assetName, "0");
    }
    List periods = experimentNode.getChildren("Period");
    List portfolios = ( (Element) periods.get(periods.size() - 1)).
        getChildren("Portfolio");
    Iterator portfoliosIter = portfolios.iterator();
    int row = 0;
    while (portfoliosIter.hasNext()) {
      Element portfolio = (Element) portfoliosIter.next();
      List ownings = portfolio.getChildren("Owning");
      Iterator owningsIter = ownings.iterator();
      endPortfolios.put(Integer.toString(row), portfolio);
      int col = 0;
      while (owningsIter.hasNext()) {
        Element owning = (Element) owningsIter.next();
        String quantity = owning.getAttributeValue("qtty");
        String temp2 = (String)this.assetsName.elementAt(col);
        int temp3 = Integer.parseInt(quantity);
        int temp4 = Integer.parseInt( (String) totalQuantity.get(temp2));
        totalQuantity.remove(temp2);
        totalQuantity.put(temp2, Integer.toString(temp3 + temp4));
        endOwnings.put("(" + Integer.toString(row) + "," +
                       Integer.toString(2 * col + 3) + ")", quantity);
        col++;
      }
      sumOfCash += Float.parseFloat(portfolio.getAttributeValue("cash"));

      row++;
      rowCount++;
    }
  }

  /**
   * @return The number of rows
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * @return The number of columns
   */
  public int getColumnCount() {
    Utils.logger.debug("getColumnCount = " + columnCount);
    columnCount = 3 + 2 * assetsName.size();
    return columnCount;
  }

  /**
   * @return The class of the column choosen
   * @param column the number of the column
   */
  public Class getColumnClass(int column) {
    return (column == 0) ? String.class : Float.class;
  }

  /**
   * @return A string containing the default name of each column
   * @param column the number of the column
   */
  public String getColumnName(int column) {
  //  Utils.logger.debug("getColumnName" + column);
    switch (column) {
      case 0:
        return "Players' Name";
      case 1:
        return "Cash ($)";
      case 2:
        return "Cash (%)";
      default:
        return (column / 2 == (column - 1) / 2) ?
            (String)this.assetsName.elementAt( (column - 3) / 2) :
            (String)this.assetsName.elementAt( (column - 3) / 2) + " (%)";
    }
  }

  /**
   * @return The value true if the cell choosen is editable
   * @param rowIndex the number of the row to choose the cell
   * @param columnIndex the number of the column to choose the cell
   */
  public boolean isCellEditable(int rowIndex, int columnIndex) {
    Utils.logger.debug("isCellEditable(" + rowIndex + "," + columnIndex + ")");
    return false;
  }

  /**
   * @return The object in the cell choosen
   * @param rowIndex the number of the row to choose the cell
   * @param columnIndex the number of the column to choose the cell
   */
  public Object getValueAt(int rowIndex, int columnIndex) {
    Utils.logger.debug("getValueAt: ( " + rowIndex + " ; " + columnIndex + " )");
    switch (columnIndex) {
      case 0: {
        return (String) ( (Element) endPortfolios.get(Integer.toString(rowIndex))).
            getAttributeValue("player");
      }
      case 1: {
        String cash = ( (Element) endPortfolios.get(Integer.toString(rowIndex))).
            getAttributeValue("cash");
        return new Float( (float) Math.round(Float.parseFloat(cash) * 100) /
                         100);
      }
      case 2: {
        String cash = ( (Element) endPortfolios.get(Integer.toString(rowIndex))).
            getAttributeValue("cash");
        return new Float( ( (float) Math.round(Float.parseFloat(cash) /
                                               sumOfCash *
                                               10000)) / 100);
      }
      default:
        Utils.logger.debug("Attention");

        if (columnIndex / 2 == (columnIndex - 1) / 2) {
          Utils.logger.debug("Modification");
          int i = 0;
          List ownings = ( (Element) endPortfolios.get(Integer.toString(
              rowIndex))).getChildren("Owning");
              if (ownings.size()!=0){
                Element owning = ( (Element) ownings.get(i));
                while (! (owning.getAttributeValue(
                    "asset").toString().equals(this.getColumnName(columnIndex))) &&
                       (i < ownings.size() - 1)) {
                  Utils.logger.debug(owning.getAttributeValue(
                      "asset").toString());
                  Utils.logger.debug(this.getColumnName(columnIndex));
                  i++;
                  owning = ( (Element) ownings.get(i));
                }
                if (owning.getAttributeValue("asset").toString().equals(this.
                    getColumnName(columnIndex))) {
                  return Float.valueOf(owning.getAttributeValue("qtty"));
                }
                else return new Float(0);
              }
          else return new Float(0);
        }
        else {
          String total = ( (String) totalQuantity.get(this.getColumnName(columnIndex-1)));
          float percent = (((Float)(getValueAt(rowIndex,columnIndex-1))).floatValue() / Float.parseFloat(total)) *
              100;
          return new Float( ( (float) Math.round(percent * 100)) / 100);
        }
    }
  }
}
