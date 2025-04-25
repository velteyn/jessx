// 
// Decompiled by Procyon v0.6.0
// 

package jessx.business.operations;

import jessx.net.NetworkWritable;
import jessx.client.ClientCore;
import java.awt.event.ActionEvent;
import javax.swing.JFormattedTextField;
import javax.swing.text.DefaultFormatter;
import java.text.Format;
import javax.swing.text.NumberFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.text.NumberFormat;
import javax.swing.SpinnerModel;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import jessx.utils.Constants;
import java.awt.event.ActionListener;
import java.awt.Font;
import java.awt.Color;
import javax.swing.SpinnerNumberModel;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JSpinner;
import javax.swing.JLabel;
import jessx.business.ClientInputPanel;
import javax.swing.JPanel;

public class MarketOrderClientPanel extends JPanel implements ClientInputPanel
{
    JLabel jLabelQuantityTransaction;
    JSpinner jSpinnerQuantity;
    JToggleButton jToggleButtonBuyMarketOrder;
    JToggleButton jToggleButtonSellMarketOrder;
    JButton jButtonSendOrderMarketOrder;
    GridBagLayout gridBagLayout1;
    JPanel jPanel1;
    GridBagLayout gridBagLayout2;
    private String institution;
    
    public MarketOrderClientPanel(final String institution) {
        this.jLabelQuantityTransaction = new JLabel();
        this.jSpinnerQuantity = new JSpinner();
        this.jToggleButtonBuyMarketOrder = new JToggleButton();
        this.jToggleButtonSellMarketOrder = new JToggleButton();
        this.jButtonSendOrderMarketOrder = new JButton();
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.gridBagLayout2 = new GridBagLayout();
        this.institution = institution;
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void stopEdition() {
        this.jToggleButtonBuyMarketOrder.setEnabled(false);
        this.jToggleButtonSellMarketOrder.setEnabled(false);
        this.jButtonSendOrderMarketOrder.setEnabled(false);
    }
    
    public void startEdition() {
        this.jToggleButtonBuyMarketOrder.setEnabled(true);
        this.jToggleButtonSellMarketOrder.setEnabled(true);
    }
    
    private void jbInit() {
        this.setLayout(this.gridBagLayout1);
        this.jLabelQuantityTransaction.setText("Quantity :");
        this.jSpinnerQuantity.setMinimumSize(new Dimension(70, 20));
        this.jSpinnerQuantity.setPreferredSize(new Dimension(70, 20));
        this.setIntegerSpinnerProperties(this.jSpinnerQuantity, new SpinnerNumberModel(1, 1, 999, 1));
        this.jToggleButtonBuyMarketOrder.setBackground(new Color(145, 255, 200));
        this.jToggleButtonBuyMarketOrder.setEnabled(false);
        this.jToggleButtonBuyMarketOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonBuyMarketOrder.setDoubleBuffered(false);
        this.jToggleButtonBuyMarketOrder.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonBuyMarketOrder.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonBuyMarketOrder.setOpaque(true);
        this.jToggleButtonBuyMarketOrder.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonBuyMarketOrder.setActionCommand("Bid");
        this.jToggleButtonBuyMarketOrder.setMnemonic(66);
        this.jToggleButtonBuyMarketOrder.setText("Buy");
        this.jToggleButtonBuyMarketOrder.addActionListener(new ClientFrame_jToggleButtonBuyMarketOrder_actionAdapter(this));
        this.jToggleButtonSellMarketOrder.setBackground(new Color(13, 219, 242));
        this.jToggleButtonSellMarketOrder.setEnabled(false);
        this.jToggleButtonSellMarketOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonSellMarketOrder.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonSellMarketOrder.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonSellMarketOrder.setOpaque(true);
        this.jToggleButtonSellMarketOrder.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonSellMarketOrder.setActionCommand("Ask");
        this.jToggleButtonSellMarketOrder.setMnemonic(83);
        this.jToggleButtonSellMarketOrder.setText("Sell");
        this.jToggleButtonSellMarketOrder.addActionListener(new ClientFrame_jToggleButtonSellMarketOrder_actionAdapter(this));
        this.jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
        this.jButtonSendOrderMarketOrder.setEnabled(false);
        this.jButtonSendOrderMarketOrder.setFont(new Font("Verdana", 1, 14));
        this.jButtonSendOrderMarketOrder.setForeground(Color.red);
        this.jButtonSendOrderMarketOrder.setMaximumSize(new Dimension(95, 30));
        this.jButtonSendOrderMarketOrder.setMinimumSize(new Dimension(95, 30));
        this.jButtonSendOrderMarketOrder.setPreferredSize(new Dimension(95, 30));
        this.jButtonSendOrderMarketOrder.setActionCommand("Send");
        this.jButtonSendOrderMarketOrder.setMnemonic(69);
        this.jButtonSendOrderMarketOrder.setText("Execute");
        this.jButtonSendOrderMarketOrder.addActionListener(new Principale_jButtonSendOrderMarketOrder_actionAdapter(this));
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.add(this.jLabelQuantityTransaction, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 6, 3), 0, 0));
        this.add(this.jSpinnerQuantity, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.add(this.jPanel1, new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jToggleButtonBuyMarketOrder, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.jPanel1.add(this.jToggleButtonSellMarketOrder, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0, new Insets(6, 3, 3, 3), 0, 0));
        this.jPanel1.add(this.jButtonSendOrderMarketOrder, new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, 10, 0, new Insets(6, 3, 6, 6), 0, 0));
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
    
    void jToggleButtonBuyMarketOrder_actionPerformed(final ActionEvent e) {
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
    
    void jToggleButtonSellMarketOrder_actionPerformed(final ActionEvent e) {
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
        this.jToggleButtonSellMarketOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonSellMarketOrder.setBackground(Constants.CLIENT_SELL_INACTIVE);
    }
    
    void desactivateBuyButton() {
        this.jToggleButtonBuyMarketOrder.setSelected(false);
        this.jToggleButtonBuyMarketOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonBuyMarketOrder.setBackground(Constants.CLIENT_BUY_INACTIVE);
    }
    
    void desactivateExecuteButton() {
        this.jButtonSendOrderMarketOrder.setEnabled(false);
        this.jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
    }
    
    void activateSellButton() {
        this.jToggleButtonSellMarketOrder.setSelected(true);
        this.jToggleButtonSellMarketOrder.setFont(new Font("Verdana", 1, 14));
        this.jToggleButtonSellMarketOrder.setBackground(Constants.CLIENT_SELL_ACTIVE);
        this.desactivateBuyButton();
        this.activateExecuteButton();
    }
    
    void activateBuyButton() {
        this.jToggleButtonBuyMarketOrder.setSelected(true);
        this.jToggleButtonBuyMarketOrder.setFont(new Font("Verdana", 1, 14));
        this.jToggleButtonBuyMarketOrder.setBackground(Constants.CLIENT_BUY_ACTIVE);
        this.desactivateSellButton();
        this.activateExecuteButton();
    }
    
    void activateExecuteButton() {
        this.jButtonSendOrderMarketOrder.setEnabled(true);
        this.jButtonSendOrderMarketOrder.setBackground(Constants.CLIENT_EXECUTE_ACTIVE);
    }
    
    void jButtonSendOrderMarketOrder_actionPerformed(final ActionEvent e) {
        final MarketOrder marketOrder = new MarketOrder();
        if (this.orderValid()) {
            this.desactivateExecuteButton();
            if (this.jToggleButtonBuyMarketOrder.isSelected()) {
                marketOrder.setPrice(Float.MAX_VALUE);
            }
            else {
                marketOrder.setPrice(Float.MIN_VALUE);
            }
            marketOrder.setEmitter(ClientCore.getLogin());
            marketOrder.setInstitutionName(this.institution);
            marketOrder.setQuantity(Integer.parseInt(this.jSpinnerQuantity.getValue().toString()));
            marketOrder.setSide(this.jToggleButtonBuyMarketOrder.isSelected() ? 1 : 0);
            ClientCore.send(marketOrder);
            this.desactivateBuyButton();
            this.desactivateSellButton();
        }
    }
    
    public boolean orderValid() {
        final boolean response = true;
        final int qtty = Integer.parseInt(this.jSpinnerQuantity.getValue().toString());
        final String assetName = ClientCore.getInstitution(this.institution).getAssetName();
        final float operationMinimalCost = ClientCore.getInstitution(this.institution).getMinimalCost("Market Order");
        final float operationPercentageCost = ClientCore.getInstitution(this.institution).getPercentageCost("Market Order");
        if (this.jToggleButtonBuyMarketOrder.isSelected()) {
            final float price = Float.MAX_VALUE;
        }
        return response;
    }
    
    @Override
    public String toString() {
        return "Market Order";
    }
}
