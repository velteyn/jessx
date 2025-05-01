package jessx.business.operations;

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
import org.jdom.*;
import jessx.business.*;
import jessx.utils.*;

/***************************************************************/
/*                MarketOrder CLASS SECTION                    */
/***************************************************************/
/**
 * <p>Title : MarketOrder</p>
 * <p>Description : </p>
 * @author Christophe Grosjean
 * @version 1.0
 */

public class MarketOrder extends Order {

  private static final String operationName = "Market Order";

  private Integer quantity;
  private Float price;

  public int getQuantity() {
    return this.quantity.intValue();
  }

  public float getPrice() {
    return this.price.floatValue();
  }

  public void setPrice(float price) {
    this.price = new Float(price);
  }

  public void setQuantity(int qtty) {
    this.quantity = new Integer(qtty);
  }

  public float getOperationCost(float percentCost, float minimalCost) {
    return Math.max(minimalCost,
                    quantity.intValue() * price.floatValue() *
                    percentCost / 100 );
  }

  public MarketOrder() {
    super();
  }

  public String getOperationName() {
    return operationName;
  }

  public boolean initFromNetworkInput(Element node) {
    if (!super.initFromNetworkInput(node)) {
      return false;
    }

    Element marketOrder = node.getChild("MarketOrder");
    String price = marketOrder.getAttributeValue("price");
    String quantity = marketOrder.getAttributeValue("quantity");
    if ((price == null) || (quantity == null)) {
      Utils.logger.error("Invalid xml marketorder node: the attribute price or quantity is missing.");
      return false;
    }

    this.price = new Float(price);
    this.quantity = new Integer(quantity);

    return true;
  }

  public Element prepareForNetworkOutput(String pt) {
    Element root = super.prepareForNetworkOutput(pt);

    Element marketOrder = new Element("MarketOrder");
    marketOrder.setAttribute("price",this.price.toString());
    marketOrder.setAttribute("quantity",this.quantity.toString());

    root.addContent(marketOrder);
    return root;
  }

  public boolean isExecutingImmediately() {
    return true;
  }

  public void stopImmediateExecution() {
      //Must never happen !
  }

  public boolean hasDefinedPrice() {
    return false;
  }

  public boolean isVisibleInOrderbook() {
    return false;
  }

  public void definePrice(float price) {
    //must never happen !
  }

  public boolean isVisibleInTheClientPanel() {
    return true;
  }

  public ClientInputPanel getClientPanel(String institution) {
    return new MarketOrderClientPanel(institution);
  }

  public int getMinQtty() {
    return 1;
  }

  public int getMaxQtty() {
    return this.getQuantity();
  }

  /**
   * The min price at which the order could be executed
   * @return float
   */
  public float getMinPrice() {
    return (this.getSide() == Order.BID) ? 0 : this.getPrice();
  }

  /**
   * The max price at which this order could be executed
   * @return float
   */
  public float getMaxPrice() {
    return (this.getSide() == Order.BID) ?  this.getPrice() : Float.MAX_VALUE;
  }

  public void setRemainingOrder(int quantity, float price) {
    this.quantity = new Integer(this.quantity.intValue() - quantity);
  }

  static {
    try {
      System.out.println("Loading MarketOrder...");
      jessx.business.OperationCreator.operationFactories.put(operationName , Class.forName("jessx.business.operations.MarketOrder"));
    }
    catch (ClassNotFoundException exception) {
      System.out.println("Unabled to locate the MarketOrder class. Reason: bad class name spelling.");
      exception.printStackTrace();
    }
  }
}
