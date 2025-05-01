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

import java.text.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

import jessx.business.*;
import jessx.client.*;
import jessx.utils.*;

/***************************************************************/
/*          MarketOrderClientPanel CLASS SECTION               */
/***************************************************************/
/**
 * <p>Title : MarketOrderClientPanel</p>
 * <p>Description : </p>
 * @author Christophe Grosjean
 * @version 1.0
 */

public class MarketOrderClientPanel extends JPanel implements ClientInputPanel {

  JLabel jLabelQuantityTransaction = new JLabel();
  JSpinner jSpinnerQuantity = new JSpinner();
  JToggleButton jToggleButtonBuyMarketOrder = new JToggleButton();
  JToggleButton jToggleButtonSellMarketOrder = new JToggleButton();
  JButton jButtonSendOrderMarketOrder = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  private String institution;

  public MarketOrderClientPanel(String institution) {
    this.institution = institution;
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  public JPanel getPanel() {
    return this;
  }

  public void stopEdition() {
    jToggleButtonBuyMarketOrder.setEnabled(false);
    jToggleButtonSellMarketOrder.setEnabled(false);
    jButtonSendOrderMarketOrder.setEnabled(false);
  }

  public void startEdition() {
    jToggleButtonBuyMarketOrder.setEnabled(true);
    jToggleButtonSellMarketOrder.setEnabled(true);
  }

  private void jbInit() {
    this.setLayout(gridBagLayout1);



    jLabelQuantityTransaction.setText("Quantity :");

    jSpinnerQuantity.setMinimumSize(new Dimension(70, 20));
    jSpinnerQuantity.setPreferredSize(new Dimension(70, 20));
    setIntegerSpinnerProperties( jSpinnerQuantity, new SpinnerNumberModel(1, 1, 999, 1) );

    jToggleButtonBuyMarketOrder.setBackground(new java.awt.Color(145, 255, 200));
    jToggleButtonBuyMarketOrder.setEnabled(false);
    jToggleButtonBuyMarketOrder.setFont(new java.awt.Font("Lucida Console", 0, 14));
    jToggleButtonBuyMarketOrder.setDoubleBuffered(false);
    jToggleButtonBuyMarketOrder.setMaximumSize(new Dimension(68, 30));
    jToggleButtonBuyMarketOrder.setMinimumSize(new Dimension(68, 30));
    jToggleButtonBuyMarketOrder.setOpaque(true);
    jToggleButtonBuyMarketOrder.setPreferredSize(new Dimension(68, 30));
    jToggleButtonBuyMarketOrder.setActionCommand("Bid");
    jToggleButtonBuyMarketOrder.setMnemonic(KeyEvent.VK_B);
    jToggleButtonBuyMarketOrder.setText("Buy");
    jToggleButtonBuyMarketOrder.addActionListener(new ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter(this));
    jToggleButtonSellMarketOrder.setBackground(new java.awt.Color(13, 219, 242));

    jToggleButtonSellMarketOrder.setEnabled(false);
    jToggleButtonSellMarketOrder.setFont(new java.awt.Font("Lucida Console", 0, 14));
    jToggleButtonSellMarketOrder.setMaximumSize(new Dimension(68, 30));
    jToggleButtonSellMarketOrder.setMinimumSize(new Dimension(68, 30));
    jToggleButtonSellMarketOrder.setOpaque(true);
    jToggleButtonSellMarketOrder.setPreferredSize(new Dimension(68, 30));
    jToggleButtonSellMarketOrder.setActionCommand("Ask");
    jToggleButtonSellMarketOrder.setMnemonic(KeyEvent.VK_S);
    jToggleButtonSellMarketOrder.setText("Sell");
    jToggleButtonSellMarketOrder.addActionListener(new ClientFrame_jToggleButtonSellMarketOrder_actionAdapter(this));

    jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
    jButtonSendOrderMarketOrder.setEnabled(false);
    jButtonSendOrderMarketOrder.setFont(new java.awt.Font("Lucida Console", 1, 14));
    jButtonSendOrderMarketOrder.setForeground(Color.red);
    jButtonSendOrderMarketOrder.setMaximumSize(new Dimension(95, 30));
    jButtonSendOrderMarketOrder.setMinimumSize(new Dimension(95, 30));
    jButtonSendOrderMarketOrder.setPreferredSize(new Dimension(95, 30));
    jButtonSendOrderMarketOrder.setActionCommand("Send");
    jButtonSendOrderMarketOrder.setMnemonic(KeyEvent.VK_E);
    jButtonSendOrderMarketOrder.setText("Execute");
    jButtonSendOrderMarketOrder.addActionListener(new Principale_jButtonSendOrderMarketOrder_actionAdapter(this));
    jPanel1.setLayout(gridBagLayout2);
    this.add(jLabelQuantityTransaction,               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 6, 6, 3), 0, 0));
    this.add(jSpinnerQuantity,          new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 6, 3), 0, 0));
    this.add(jPanel1,        new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jToggleButtonBuyMarketOrder,            new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 6, 3), 0, 0));
    jPanel1.add(jToggleButtonSellMarketOrder,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 3, 3, 3), 0, 0));
    jPanel1.add(jButtonSendOrderMarketOrder,      new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 3, 6, 6), 0, 0));



  }

  /**
   * @author Franck P
   * @param spinner JSpinner
   * @param model SpinnerNumberModel
   * forbids non-numeric input and sets the model of the spinner
   */
  private void setCurrencySpinnerProperties (JSpinner spinner, SpinnerNumberModel model) {

    if (model != null) {
      spinner.setModel(model);
    }

    JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();

    NumberFormat nf = NumberFormat.getInstance ();
    nf.setMaximumFractionDigits(2);
    nf.setMinimumFractionDigits(2);


    DefaultFormatterFactory factory = (DefaultFormatterFactory) spinnerTextField.getFormatterFactory();
    NumberFormatter formatter = (NumberFormatter) factory.getDefaultFormatter();
    formatter.setFormat(nf);

    ((DefaultFormatter) spinnerTextField.getFormatter ()).setAllowsInvalid (false);

  }

  /**
   * @author Franck P
   * @param spinner JSpinner
   * @param model SpinnerNumberModel
   * forbids non-numeric input and sets the model of the spinner
   */
  private void setIntegerSpinnerProperties (JSpinner spinner, SpinnerNumberModel model) {

    if (model != null) {
      spinner.setModel(model);
    }

    JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();

    NumberFormat nf = NumberFormat.getNumberInstance ();
    nf.setParseIntegerOnly (true);

    DefaultFormatterFactory factory = (DefaultFormatterFactory) spinnerTextField.getFormatterFactory();
    NumberFormatter formatter = (NumberFormatter) factory.getDefaultFormatter();
    formatter.setFormat(nf);

    ((DefaultFormatter) spinnerTextField.getFormatter ()).setAllowsInvalid (false);

  }

  void jToggleButtonBuyMarketOrder_actionPerformed(ActionEvent e) {
    if (this.jToggleButtonBuyMarketOrder.isSelected()) {
      this.activateBuyButton();
      this.desactivateSellButton();
    }
    else {
      this.desactivateBuyButton();
      this.activateSellButton();
    }
    this.activateExecuteButton();
  }

  void jToggleButtonSellMarketOrder_actionPerformed(ActionEvent e) {
    if (this.jToggleButtonSellMarketOrder.isSelected()) {
      this.desactivateBuyButton();
      this.activateSellButton();
    }
    else {
      this.activateBuyButton();
      this.desactivateSellButton();
    }
    this.activateExecuteButton();

  }

  void desactivateSellButton() {
    this.jToggleButtonSellMarketOrder.setSelected(false);
    this.jToggleButtonSellMarketOrder.setFont(new Font("Lucida Console",Font.PLAIN,14));
    this.jToggleButtonSellMarketOrder.setBackground(Constants.CLIENT_SELL_INACTIVE);
  }

  void desactivateBuyButton() {
    this.jToggleButtonBuyMarketOrder.setSelected(false);
    this.jToggleButtonBuyMarketOrder.setFont(new Font("Lucida Console",Font.PLAIN,14));
    this.jToggleButtonBuyMarketOrder.setBackground(Constants.CLIENT_BUY_INACTIVE);

  }

  void desactivateExecuteButton() {
    this.jButtonSendOrderMarketOrder.setEnabled(false);
    this.jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);

  }

  void activateSellButton() {
    this.jToggleButtonSellMarketOrder.setSelected(true);
    this.jToggleButtonSellMarketOrder.setFont(new Font("Lucida Console",Font.BOLD,14));
    this.jToggleButtonSellMarketOrder.setBackground(Constants.CLIENT_SELL_ACTIVE);
    this.desactivateBuyButton();
    this.activateExecuteButton();
  }

  void activateBuyButton() {
    this.jToggleButtonBuyMarketOrder.setSelected(true);
    this.jToggleButtonBuyMarketOrder.setFont(new Font("Lucida Console",Font.BOLD,14));
    this.jToggleButtonBuyMarketOrder.setBackground(Constants.CLIENT_BUY_ACTIVE);
    this.desactivateSellButton();
    this.activateExecuteButton();
  }

  void activateExecuteButton() {
    this.jButtonSendOrderMarketOrder.setEnabled(true);
    this.jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_ACTIVE);
  }


  void jButtonSendOrderMarketOrder_actionPerformed(ActionEvent e) {
    MarketOrder marketOrder = new MarketOrder();
    if (this.orderValid()) {
      this.desactivateExecuteButton();

      //if (this.jToggleButtonBuyMarketOrder.isSelected()) {
      //  this.activateBuyButton();
      //}
      //else {
      //  this.activateSellButton();
      //}

      //Previous version for correcting bugs about activated buttons
      if (this.jToggleButtonBuyMarketOrder.isSelected()) {
        marketOrder.setPrice(Float.MAX_VALUE);
      } else {
        marketOrder.setPrice(Float.MIN_VALUE);
      }
      marketOrder.setEmitter(ClientCore.getLogin());
      marketOrder.setInstitutionName(institution);

      marketOrder.setQuantity(Integer.parseInt(jSpinnerQuantity.getValue().
                                              toString()));
      marketOrder.setSide( (this.jToggleButtonBuyMarketOrder.isSelected()) ? Order.BID :
                         Order.ASK);

      ClientCore.send(marketOrder);

      this.desactivateBuyButton();
      this.desactivateSellButton();

    }
       else {
     //Prevents you to sell instead of buying if you have not cash enough to buy (when you click twice on "Execute")
    }
  }



  /*public boolean orderValid() {
    float price =  Float.parseFloat(jSpinnerPrice.getValue().toString());
    int qtty = Integer.parseInt(jSpinnerQuantity.getValue().toString());

    float cash = ClientCore.getPortfolio().getCash();

    String assetName = ClientCore.getInstitution(institution).getAssetName();
    int assetsOwnedNumber = ClientCore.getPortfolio().getOwnings(assetName);

    return ( (this.jToggleButtonBuyMarketOrder.isSelected()) ? (cash >= price*qtty) : (assetsOwnedNumber >= qtty));
  }

Previous version before //CBJ
  */


  public boolean orderValid() {
    boolean response = true;

    int qtty = Integer.parseInt(jSpinnerQuantity.getValue().toString());

    String assetName = ClientCore.getInstitution(institution).getAssetName();

    float operationMinimalCost = ClientCore.getInstitution(institution).getMinimalCost("Market Order");
    float operationPercentageCost = ClientCore.getInstitution(institution).getPercentageCost("Market Order");


    if (this.jToggleButtonBuyMarketOrder.isSelected()) {
      float price = Float.MAX_VALUE;
      /*if (ClientCore.getPortfolio().hasEnoughCashForBid(qtty, price, operationPercentageCost, operationMinimalCost)) {
        ClientCore.getPortfolio().wantedToBeBoughtAsset(qtty, price, operationPercentageCost, operationMinimalCost, institution);
      }
      else {
        String warnMessage = "You have not enough cash to afford all the bids you placed.";
        ClientCore.send(new WarnForClient(warnMessage));
        response = false;

      }*/

    //impossible (et trop complexe) de verifier la solvabilite du joueur
    }
    else {
      float price = Float.MIN_VALUE;
      /*
      if (!ClientCore.getPortfolio().hasEnoughAsset(assetName, qtty)) {
      //ClientCore.getPortfolio().wantedToBeSoldAsset(assetName, qtty, price, operationPercentageCost, operationMinimalCost, institution);
        String warnMessage = "You have not enough assets to afford all the asks you placed.";
        ClientCore.send(new WarnForClient(warnMessage));
        response = false;
      }
      */
    }

    return response;
  }

//New Version after CBJ
//Prevents the player to sell more assets than he owns or buy more than he can

  public String toString() {
    return "Market Order";
  }
}

/***************************************************************/
/*                   EVENT CLASSES SECTION                     */
/***************************************************************/
class ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter
    implements java.awt.event.ActionListener {
  MarketOrderClientPanel adaptee;

  ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter(MarketOrderClientPanel
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jToggleButtonBuyMarketOrder_actionPerformed(e);
  }
}

class ClientFrame_jToggleButtonSellMarketOrder_actionAdapter
    implements java.awt.event.ActionListener {
  MarketOrderClientPanel adaptee;

  ClientFrame_jToggleButtonSellMarketOrder_actionAdapter(MarketOrderClientPanel
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jToggleButtonSellMarketOrder_actionPerformed(e);
  }
}

class Principale_jButtonSendOrderMarketOrder_actionAdapter
    implements java.awt.event.ActionListener {
  MarketOrderClientPanel adaptee;

  Principale_jButtonSendOrderMarketOrder_actionAdapter(MarketOrderClientPanel
      adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButtonSendOrderMarketOrder_actionPerformed(e);
  }
}
