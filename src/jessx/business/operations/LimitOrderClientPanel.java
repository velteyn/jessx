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
/*            LimitOrderClientPanel CLASS SECTION              */
/***************************************************************/
/**
 * <p>Title : LimitOrderClientPanel</p>
 * <p>Description : </p>
 * @author Thierry Curtil, Jeremy Streque
 * @version 1.0
 */

public class LimitOrderClientPanel extends JPanel implements ClientInputPanel {

  JLabel jLabelPriceTransaction = new JLabel();
  //JSpinner jSpinnerPrice = new JSpinner();
  JFormattedTextField jTextPrice = new JFormattedTextField();

  JLabel jLabelQuantityTransaction = new JLabel();
  JSpinner jSpinnerQuantity = new JSpinner();
  JToggleButton jToggleButtonBuy = new JToggleButton();
  JToggleButton jToggleButtonSell = new JToggleButton();
  JButton jButtonSendOrder = new JButton();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
  JPanel jPanel1 = new JPanel();
  GridBagLayout gridBagLayout2 = new GridBagLayout();

  DecimalFormat priceFormat = new DecimalFormat();

  public void setPriceFormat() {
  DecimalFormatSymbols symboles = new DecimalFormatSymbols();
  symboles.setDecimalSeparator('.');
  symboles.setGroupingSeparator('\'');
  symboles.setMinusSign('-');
  this.priceFormat.setMaximumFractionDigits(2);
  //this.priceFormat.setMaximumIntegerDigits(3);
  this.priceFormat.setDecimalFormatSymbols(symboles);
  }
  //Price

  private String institution;
  public float lastPrice = 100;

  public LimitOrderClientPanel(String institution) {
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
    jToggleButtonBuy.setEnabled(false);
    jToggleButtonSell.setEnabled(false);
    jButtonSendOrder.setEnabled(false);
  }

  public void startEdition() {
    jToggleButtonBuy.setEnabled(true);
    jToggleButtonSell.setEnabled(true);
  }

  private void jbInit() {
    this.setLayout(gridBagLayout1);


    this.setPriceFormat();
    jLabelPriceTransaction.setText("Price :");
    //NumberFormat priceFormat = NumberFormat.getNumberInstance();
    jTextPrice = new JFormattedTextField(priceFormat);
    jTextPrice.setValue(new Float(lastPrice));
    jTextPrice.setColumns(5);
    jTextPrice.setMargin(new Insets(1, 5, 2, 8));
    jTextPrice.setMinimumSize(new Dimension(68, 21));
    jTextPrice.setPreferredSize(new Dimension(68, 21)); //jSpinnerPrice.setMinimumSize(new Dimension(70, 20));
    //jSpinnerPrice.setPreferredSize(new Dimension(70, 20));
    //setCurrencySpinnerProperties( jSpinnerPrice, new SpinnerNumberModel(0,0,999,0.01) );
    //charles ?
    jLabelQuantityTransaction.setText("Quantity :");

    jSpinnerQuantity.setMinimumSize(new Dimension(70, 20));
    jSpinnerQuantity.setPreferredSize(new Dimension(70, 20));
    setIntegerSpinnerProperties( jSpinnerQuantity, new SpinnerNumberModel(1, 1, 999, 1) );

    //jToggleButtonBuy.setBackground(Constants.CLIENT_BUY_INACTIVE);
    jToggleButtonBuy.setBackground(new Color(145,255,200));
    jToggleButtonBuy.setEnabled(false);
    jToggleButtonBuy.setFont(new java.awt.Font("Lucida Console", 0, 14));
    jToggleButtonBuy.setDoubleBuffered(false);
    jToggleButtonBuy.setMaximumSize(new Dimension(68, 30));
    jToggleButtonBuy.setMinimumSize(new Dimension(68, 30));
    jToggleButtonBuy.setOpaque(true);
    jToggleButtonBuy.setPreferredSize(new Dimension(68, 30));
    jToggleButtonBuy.setActionCommand("Bid");
    jToggleButtonBuy.setMnemonic(KeyEvent.VK_B);
    jToggleButtonBuy.setText("Buy");
    jToggleButtonBuy.addActionListener(new ClientFrame_jToggleButtonBuy_actionAdapter(this));


    //jToggleButtonSell.setBackground(Constants.CLIENT_SELL_INACTIVE);
    jToggleButtonSell.setBackground(new Color(13,219,242));
    jToggleButtonSell.setEnabled(false);
    jToggleButtonSell.setFont(new java.awt.Font("Lucida Console", 0, 14));
    jToggleButtonSell.setMaximumSize(new Dimension(68, 30));
    jToggleButtonSell.setMinimumSize(new Dimension(68, 30));
    jToggleButtonSell.setOpaque(true);
    jToggleButtonSell.setPreferredSize(new Dimension(68, 30));
    jToggleButtonSell.setActionCommand("Ask");
    jToggleButtonSell.setMnemonic(KeyEvent.VK_S);
    jToggleButtonSell.setText("Sell");
    jToggleButtonSell.addActionListener(new ClientFrame_jToggleButtonSell_actionAdapter(this));


    jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
    jButtonSendOrder.setEnabled(false);
    jButtonSendOrder.setFont(new java.awt.Font("Lucida Console", 1, 14));
    jButtonSendOrder.setForeground(Color.red);
    jButtonSendOrder.setMaximumSize(new Dimension(95, 30));
    jButtonSendOrder.setMinimumSize(new Dimension(95, 30));
    jButtonSendOrder.setPreferredSize(new Dimension(95, 30));
    jButtonSendOrder.setActionCommand("Send");
    jButtonSendOrder.setMnemonic(KeyEvent.VK_E);
    jButtonSendOrder.setText("Execute");
    jButtonSendOrder.addActionListener(new Main_jButtonSendOrder_actionAdapter(this));
    jPanel1.setLayout(gridBagLayout2);
    this.add(jLabelPriceTransaction,              new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 6, 3, 3), 0, 0));
    this.add(jLabelQuantityTransaction,               new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 6, 6, 3), 0, 0));
    this.add(jSpinnerQuantity,          new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 6, 3), 0, 0));
    this.add(jPanel1,        new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel1.add(jToggleButtonBuy,            new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(3, 3, 6, 3), 0, 0));
    jPanel1.add(jToggleButtonSell,     new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 3, 3, 3), 0, 0));
    jPanel1.add(jButtonSendOrder,      new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(6, 3, 6, 6), 0, 0));
    this.add(jTextPrice, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                                                , GridBagConstraints.CENTER,
                                                GridBagConstraints.NONE,
                                                new Insets(6, 3, 3, 3), 0, 0));
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

  void jToggleButtonBuy_actionPerformed(ActionEvent e) {
 /*   if (this.jToggleButtonBuy.isSelected()) {
    */  this.activateBuyButton();/*
      this.desactivateSellButton();
    }
    else {
      this.desactivateBuyButton();
      this.activateSellButton();
    }*/
    this.activateExecuteButton();
  }

  void jToggleButtonSell_actionPerformed(ActionEvent e) {
 /*   if (this.jToggleButtonSell.isSelected()) {
      this.desactivateBuyButton();
     */ this.activateSellButton();/*
    }
    else {
      this.activateBuyButton();
      this.desactivateSellButton();
    }*/
    this.activateExecuteButton();

  }


  void desactivateSellButton() {
    this.jToggleButtonSell.setSelected(false);
    this.jToggleButtonSell.setFont(new Font("Lucida Console",Font.PLAIN,14));
    this.jToggleButtonSell.setBackground(Constants.CLIENT_SELL_INACTIVE);
  }

  void desactivateBuyButton() {
    this.jToggleButtonBuy.setSelected(false);
    this.jToggleButtonBuy.setFont(new Font("Lucida Console",Font.PLAIN,14));
    this.jToggleButtonBuy.setBackground(Constants.CLIENT_BUY_INACTIVE);

  }

  void desactivateExecuteButton() {
    this.jButtonSendOrder.setEnabled(false);
    this.jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);

  }

  void activateSellButton() {
    this.jToggleButtonSell.setSelected(true);
    this.jToggleButtonSell.setFont(new Font("Lucida Console",Font.BOLD,14));
    this.jToggleButtonSell.setBackground(Constants.CLIENT_SELL_ACTIVE);
    this.desactivateBuyButton();
    this.activateExecuteButton();
  }

  void activateBuyButton() {
    this.jToggleButtonBuy.setSelected(true);
    this.jToggleButtonBuy.setFont(new Font("Lucida Console",Font.BOLD,14));
    this.jToggleButtonBuy.setBackground(Constants.CLIENT_BUY_ACTIVE);
    this.desactivateSellButton();
    this.activateExecuteButton();
  }

  void activateExecuteButton() {
    this.jButtonSendOrder.setEnabled(true);
    this.jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_ACTIVE);
  }


  void jButtonSendOrder_actionPerformed(ActionEvent e) throws ParseException {
    LimitOrder limitOrder = new LimitOrder();
    if (this.orderValid()) {
      this.desactivateExecuteButton();

      //if (this.jToggleButtonBuy.isSelected()) {
      //  this.activateBuyButton();
      //}
      //else {
      //  this.activateSellButton();
      //}

      //Previous version for correcting bugs about activated buttons
      try {
      limitOrder.setEmitter(ClientCore.getLogin());
      limitOrder.setInstitutionName(institution);
      //limitOrder.setPrice(Float.parseFloat(jSpinnerPrice.getValue().toString()));
      float price = priceFormat.parse(jTextPrice.getText()).floatValue();
      if (price<0) {
        price = -price;
        jTextPrice.setValue(new Float (price));
      }
      limitOrder.setPrice(price);
      //Prix
      limitOrder.setQuantity(Integer.parseInt(jSpinnerQuantity.getValue().
                                              toString()));
      limitOrder.setSide( (this.jToggleButtonBuy.isSelected()) ? Order.BID :
                         Order.ASK);

      ClientCore.send(limitOrder);

      this.desactivateBuyButton();
      this.desactivateSellButton();
      }catch (Exception ex) {}
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

    return ( (this.jToggleButtonBuy.isSelected()) ? (cash >= price*qtty) : (assetsOwnedNumber >= qtty));
  }

Previous version before //CBJ
  */

 public void showWarnMessage(String warnMessage) {
   int rows = warnMessage.length() / 50 + 1;
   int columns = (rows < 2) ? warnMessage.length() : 50;
   JTextArea jTextArea = new JTextArea(warnMessage, rows, columns);
   jTextArea.setEditable(false);
   jTextArea.setOpaque(false);
   jTextArea.setAutoscrolls(true);
   jTextArea.setWrapStyleWord(true);
   jTextArea.setVisible(true);
   int time = Math.min(Math.round(ClientCore.getExperimentManager().getRemainingTimeInPeriod() / 1000), 15);
   new PopupWithTimer(time, jTextArea, jTextArea.getPreferredSize(), "JessX client", null).run();
 }


  public boolean orderValid() throws ParseException {
    boolean response = true;
    //float price =  Float.parseFloat(jSpinnerPrice.getValue().toString());
    //float price = Float.parseFloat(jTextPrice.getValue().toString());
    try {
    float price = priceFormat.parse(jTextPrice.getText()).floatValue();
    if (price<0) {
      price = -price;
      jTextPrice.setValue(new Float (price));
    }
    this.lastPrice = price;
    //Prix
    int qtty = Integer.parseInt(jSpinnerQuantity.getValue().toString());
    String assetName = ClientCore.getInstitution(institution).getAssetName();
    float operationMinimalCost = ClientCore.getInstitution(institution).getMinimalCost("Limit Order");
    float operationPercentageCost = ClientCore.getInstitution(institution).getPercentageCost("Limit Order");

    if (this.jToggleButtonBuy.isSelected()) {
    }
    else {
    }
    } catch (Exception e) {}
    return response;
  }

//New Version after CBJ
//Prevents the player to sell more assets than he owns or buy more than he can

  public String toString() {
    return "Limit Order";
  }
}

/***************************************************************/
/*                   EVENT CLASSES SECTION                     */
/***************************************************************/
class ClientFrame_jToggleButtonBuy_actionAdapter implements java.awt.event.ActionListener {
  LimitOrderClientPanel adaptee;

  ClientFrame_jToggleButtonBuy_actionAdapter(LimitOrderClientPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jToggleButtonBuy_actionPerformed(e);
  }
}

class ClientFrame_jToggleButtonSell_actionAdapter implements java.awt.event.ActionListener {
  LimitOrderClientPanel adaptee;

  ClientFrame_jToggleButtonSell_actionAdapter(LimitOrderClientPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jToggleButtonSell_actionPerformed(e);
  }
}

class Main_jButtonSendOrder_actionAdapter implements java.awt.event.ActionListener {
  LimitOrderClientPanel adaptee;

  Main_jButtonSendOrder_actionAdapter(LimitOrderClientPanel adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    try {
      adaptee.jButtonSendOrder_actionPerformed(e);
    } catch (Exception ex) {}
  }
}
