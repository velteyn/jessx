// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business.operations;

import javax.swing.JFrame;
import jessx.utils.PopupWithTimer;
import javax.swing.JTextArea;
import java.text.ParseException;
import jessx.net.NetworkWritable;
import jessx.client.ClientCore;
import java.awt.event.ActionEvent;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.NumberFormat;
import javax.swing.SpinnerModel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import jessx.utils.Constants;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.Insets;
import java.text.Format;
import java.awt.LayoutManager;
import java.text.DecimalFormatSymbols;
import java.text.DecimalFormat;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import jessx.business.ClientInputPanel;
import javax.swing.JPanel;

public class LimitOrderClientPanel extends JPanel implements ClientInputPanel
{
    JLabel jLabelPriceTransaction;
    JFormattedTextField jTextPrice;
    JLabel jLabelQuantityTransaction;
    JSpinner jSpinnerQuantity;
    JToggleButton jToggleButtonBuy;
    JToggleButton jToggleButtonSell;
    JButton jButtonSendOrder;
    GridBagLayout gridBagLayout1;
    JPanel jPanel1;
    GridBagLayout gridBagLayout2;
    DecimalFormat priceFormat;
    private String institution;
    public float lastPrice;
    
    public void setPriceFormat() {
        final DecimalFormatSymbols symboles = new DecimalFormatSymbols();
        symboles.setDecimalSeparator('.');
        symboles.setGroupingSeparator('\'');
        symboles.setMinusSign('-');
        this.priceFormat.setMaximumFractionDigits(2);
        this.priceFormat.setDecimalFormatSymbols(symboles);
    }
    
    public LimitOrderClientPanel(final String institution) {
        this.jLabelPriceTransaction = new JLabel();
        this.jTextPrice = new JFormattedTextField();
        this.jLabelQuantityTransaction = new JLabel();
        this.jSpinnerQuantity = new JSpinner();
        this.jToggleButtonBuy = new JToggleButton();
        this.jToggleButtonSell = new JToggleButton();
        this.jButtonSendOrder = new JButton();
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.gridBagLayout2 = new GridBagLayout();
        this.priceFormat = new DecimalFormat();
        this.lastPrice = 100.0f;
        this.institution = institution;
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void stopEdition() {
        this.jToggleButtonBuy.setEnabled(false);
        this.jToggleButtonSell.setEnabled(false);
        this.jButtonSendOrder.setEnabled(false);
    }
    
    public void startEdition() {
        this.jToggleButtonBuy.setEnabled(true);
        this.jToggleButtonSell.setEnabled(true);
    }
    
    private void jbInit() {
        this.setLayout(this.gridBagLayout1);
        this.setPriceFormat();
        this.jLabelPriceTransaction.setText("Price :");
        (this.jTextPrice = new JFormattedTextField(this.priceFormat)).setValue(new Float(this.lastPrice));
        this.jTextPrice.setColumns(5);
        this.jTextPrice.setMargin(new Insets(1, 5, 2, 8));
        this.jTextPrice.setMinimumSize(new Dimension(68, 21));
        this.jTextPrice.setPreferredSize(new Dimension(68, 21));
        this.jLabelQuantityTransaction.setText("Quantity :");
        this.jSpinnerQuantity.setMinimumSize(new Dimension(70, 20));
        this.jSpinnerQuantity.setPreferredSize(new Dimension(70, 20));
        this.setIntegerSpinnerProperties(this.jSpinnerQuantity, new SpinnerNumberModel(1, 1, 999, 1));
        this.jToggleButtonBuy.setBackground(new Color(145, 255, 200));
        this.jToggleButtonBuy.setEnabled(false);
        this.jToggleButtonBuy.setFont(new Font("Verdana", 0, 12));
        this.jToggleButtonBuy.setDoubleBuffered(false);
        this.jToggleButtonBuy.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonBuy.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonBuy.setOpaque(false);
        this.jToggleButtonBuy.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonBuy.setActionCommand("Bid");
        this.jToggleButtonBuy.setMnemonic(66);
        this.jToggleButtonBuy.setText("Buy");
        this.jToggleButtonBuy.addActionListener(new ClientFrame_jToggleButtonBuy_actionAdapter(this));
        this.jToggleButtonSell.setBackground(new Color(13, 219, 242));
        this.jToggleButtonSell.setEnabled(false);
        this.jToggleButtonSell.setFont(new Font("Verdana", 0, 12));
        this.jToggleButtonSell.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonSell.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonSell.setOpaque(false);
        this.jToggleButtonSell.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonSell.setActionCommand("Ask");
        this.jToggleButtonSell.setMnemonic(83);
        this.jToggleButtonSell.setText("Sell");
        this.jToggleButtonSell.addActionListener(new ClientFrame_jToggleButtonSell_actionAdapter(this));
        this.jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
        this.jButtonSendOrder.setEnabled(false);
        this.jButtonSendOrder.setFont(new Font("Verdana", 1, 12));
        this.jButtonSendOrder.setForeground(Color.red);
        this.jButtonSendOrder.setOpaque(false);
        this.jButtonSendOrder.setMaximumSize(new Dimension(95, 30));
        this.jButtonSendOrder.setMinimumSize(new Dimension(95, 30));
        this.jButtonSendOrder.setPreferredSize(new Dimension(95, 30));
        this.jButtonSendOrder.setActionCommand("Send");
        this.jButtonSendOrder.setMnemonic(69);
        this.jButtonSendOrder.setText("Execute");
        this.jButtonSendOrder.addActionListener(new Main_jButtonSendOrder_actionAdapter(this));
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.add(this.jLabelPriceTransaction, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(6, 6, 3, 3), 0, 0));
        this.add(this.jLabelQuantityTransaction, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 6, 3), 0, 0));
        this.add(this.jSpinnerQuantity, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.add(this.jPanel1, new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jToggleButtonBuy, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.jPanel1.add(this.jToggleButtonSell, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0, new Insets(6, 3, 3, 3), 0, 0));
        this.jPanel1.add(this.jButtonSendOrder, new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, 10, 0, new Insets(6, 3, 6, 6), 0, 0));
        this.add(this.jTextPrice, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(6, 3, 3, 3), 0, 0));
    }
    
    private void setCurrencySpinnerProperties(final JSpinner spinner, final SpinnerNumberModel model) {
        if (model != null) {
            spinner.setModel(model);
        }
        final JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
        final NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        final DefaultFormatterFactory factory = (DefaultFormatterFactory)spinnerTextField.getFormatterFactory();
        final NumberFormatter formatter = (NumberFormatter)factory.getDefaultFormatter();
        formatter.setFormat(nf);
        ((DefaultFormatter)spinnerTextField.getFormatter()).setAllowsInvalid(false);
    }
    
    private void setIntegerSpinnerProperties(final JSpinner spinner, final SpinnerNumberModel model) {
        if (model != null) {
            spinner.setModel(model);
        }
        final JFormattedTextField spinnerTextField = ((JSpinner.DefaultEditor)spinner.getEditor()).getTextField();
        final NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setParseIntegerOnly(true);
        final DefaultFormatterFactory factory = (DefaultFormatterFactory)spinnerTextField.getFormatterFactory();
        final NumberFormatter formatter = (NumberFormatter)factory.getDefaultFormatter();
        formatter.setFormat(nf);
        ((DefaultFormatter)spinnerTextField.getFormatter()).setAllowsInvalid(false);
    }
    
    void jToggleButtonBuy_actionPerformed(final ActionEvent e) {
        this.activateBuyButton();
        this.activateExecuteButton();
    }
    
    void jToggleButtonSell_actionPerformed(final ActionEvent e) {
        this.activateSellButton();
        this.activateExecuteButton();
    }
    
    void desactivateSellButton() {
        this.jToggleButtonSell.setSelected(false);
        this.jToggleButtonSell.setFont(new Font("Verdana", 0, 12));
        this.jToggleButtonSell.setBackground(Constants.CLIENT_SELL_INACTIVE);
    }
    
    void desactivateBuyButton() {
        this.jToggleButtonBuy.setSelected(false);
        this.jToggleButtonBuy.setFont(new Font("Verdana", 0, 12));
        this.jToggleButtonBuy.setBackground(Constants.CLIENT_BUY_INACTIVE);
    }
    
    void desactivateExecuteButton() {
        this.jButtonSendOrder.setEnabled(false);
        this.jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
    }
    
    void activateSellButton() {
        this.jToggleButtonSell.setSelected(true);
        this.jToggleButtonSell.setFont(new Font("Verdana", 1, 12));
        this.jToggleButtonSell.setBackground(Constants.CLIENT_SELL_ACTIVE);
        this.desactivateBuyButton();
        this.activateExecuteButton();
    }
    
    void activateBuyButton() {
        this.jToggleButtonBuy.setSelected(true);
        this.jToggleButtonBuy.setFont(new Font("Verdana", 1, 12));
        this.jToggleButtonBuy.setBackground(Constants.CLIENT_BUY_ACTIVE);
        this.desactivateSellButton();
        this.activateExecuteButton();
    }
    
    void activateExecuteButton() {
        this.jButtonSendOrder.setEnabled(true);
        this.jButtonSendOrder.setBackground(Constants.CLIENT_EXECUTE_ACTIVE);
    }
    
    void jButtonSendOrder_actionPerformed(final ActionEvent e) throws ParseException {
        final LimitOrder limitOrder = new LimitOrder();
        if (this.orderValid()) {
            this.desactivateExecuteButton();
            try {
                limitOrder.setEmitter(ClientCore.getLogin());
                limitOrder.setInstitutionName(this.institution);
                float price = this.priceFormat.parse(this.jTextPrice.getText()).floatValue();
                if (price < 0.0f) {
                    price = -price;
                    this.jTextPrice.setValue(new Float(price));
                }
                limitOrder.setPrice(price);
                limitOrder.setQuantity(Integer.parseInt(this.jSpinnerQuantity.getValue().toString()));
                limitOrder.setSide(this.jToggleButtonBuy.isSelected() ? 1 : 0);
                ClientCore.send(limitOrder);
                this.desactivateBuyButton();
                this.desactivateSellButton();
            }
            catch (Exception ex) {}
        }
    }
    
    public void showWarnMessage(final String warnMessage) {
        final int rows = warnMessage.length() / 50 + 1;
        final int columns = (rows < 2) ? warnMessage.length() : 50;
        final JTextArea jTextArea = new JTextArea(warnMessage, rows, columns);
        jTextArea.setEditable(false);
        jTextArea.setOpaque(false);
        jTextArea.setAutoscrolls(true);
        jTextArea.setWrapStyleWord(true);
        jTextArea.setVisible(true);
        final int time = Math.min(Math.round((float)(ClientCore.getExperimentManager().getRemainingTimeInPeriod() / 1000L)), 15);
        new PopupWithTimer(time, jTextArea, jTextArea.getPreferredSize(), "JessX client", null).run();
    }
    
    public boolean orderValid() throws ParseException {
        final boolean response = true;
        try {
            float price = this.priceFormat.parse(this.jTextPrice.getText()).floatValue();
            if (price < 0.0f) {
                price = -price;
                this.jTextPrice.setValue(new Float(price));
            }
            this.lastPrice = price;
            final int qtty = Integer.parseInt(this.jSpinnerQuantity.getValue().toString());
            final String assetName = ClientCore.getInstitution(this.institution).getAssetName();
            final float operationMinimalCost = ClientCore.getInstitution(this.institution).getMinimalCost("Limit Order");
            final float operationPercentageCost = ClientCore.getInstitution(this.institution).getPercentageCost("Limit Order");
            this.jToggleButtonBuy.isSelected();
        }
        catch (Exception ex) {}
        return response;
    }
    
    @Override
    public String toString() {
        return "Limit Order";
    }
}
