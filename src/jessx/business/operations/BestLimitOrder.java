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
/*                BestLimitOrder CLASS SECTION                 */
/***************************************************************/
/**
 * <p>Title : BestLimitOrder</p>
 * <p>Description : </p>

 * @author Tian XIA, Jeremy STREQUE
 * @version 1.0
 */

public class BestLimitOrder extends Order {

  private static final String operationName = "Best Limit Order";

  private Integer quantity;
  private Float price;
  private boolean priceDefined = false;

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

  public BestLimitOrder() {
    super();
  }

  public String getOperationName() {
    return operationName;
  }

  public boolean initFromNetworkInput(Element node) {
    if (!super.initFromNetworkInput(node)) {
      return false;
    }

    Element bestLimitOrder = node.getChild("BestLimitOrder");
    String price = bestLimitOrder.getAttributeValue("price");
    String quantity = bestLimitOrder.getAttributeValue("quantity");
    if ((price == null) || (quantity == null)) {
      Utils.logger.error("Invalid xml bestlimitorder node: the attribute price or quantity is missing.");
      return false;
    }

    this.price = new Float(price);
    this.quantity = new Integer(quantity);

    return true;
  }

  public Element prepareForNetworkOutput(String pt) {
    Element root = super.prepareForNetworkOutput(pt);

    Element bestLimitOrder = new Element("BestLimitOrder");
    bestLimitOrder.setAttribute("price",this.price.toString());
    bestLimitOrder.setAttribute("quantity",this.quantity.toString());

    root.addContent(bestLimitOrder);
    return root;
  }

  public boolean isExecutingImmediately() {
    return true;
  }

  public void stopImmediateExecution() {

  }

  public boolean hasDefinedPrice() {
    return priceDefined;
  }


  public boolean isVisibleInOrderbook() {
    return true;
  }

  public void definePrice(float price) {
    this.priceDefined = true;
    this.setPrice(price);
  }

  public boolean isVisibleInTheClientPanel() {
    return true;
  }

  public ClientInputPanel getClientPanel(String institution) {
    return new BestLimitOrderClientPanel(institution);
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
      System.out.println("Loading BestLimitOrder...");
      jessx.business.OperationCreator.operationFactories.put(operationName , Class.forName("jessx.business.operations.BestLimitOrder"));
    }
    catch (ClassNotFoundException exception) {
      // it's highly doubtful we won't find jessx.business.operations.BestLimitOrder
      System.out.println("Unabled to locate the BestLimitOrder class. Reason: bad class name spelling.");
      exception.printStackTrace();
    }
  }
}
