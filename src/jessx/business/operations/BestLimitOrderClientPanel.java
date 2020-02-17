// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
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

public class BestLimitOrderClientPanel extends JPanel implements ClientInputPanel
{
    JLabel jLabelQuantityTransaction;
    JSpinner jSpinnerQuantity;
    JToggleButton jToggleButtonBuyBestLimitOrder;
    JToggleButton jToggleButtonSellBestLimitOrder;
    JButton jButtonSendOrderBestLimitOrder;
    GridBagLayout gridBagLayout1;
    JPanel jPanel1;
    GridBagLayout gridBagLayout2;
    private String institution;
    
    public BestLimitOrderClientPanel(final String institution) {
        this.jLabelQuantityTransaction = new JLabel();
        this.jSpinnerQuantity = new JSpinner();
        this.jToggleButtonBuyBestLimitOrder = new JToggleButton();
        this.jToggleButtonSellBestLimitOrder = new JToggleButton();
        this.jButtonSendOrderBestLimitOrder = new JButton();
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.gridBagLayout2 = new GridBagLayout();
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
        this.jToggleButtonBuyBestLimitOrder.setEnabled(false);
        this.jToggleButtonSellBestLimitOrder.setEnabled(false);
        this.jButtonSendOrderBestLimitOrder.setEnabled(false);
    }
    
    public void startEdition() {
        this.jToggleButtonBuyBestLimitOrder.setEnabled(true);
        this.jToggleButtonSellBestLimitOrder.setEnabled(true);
    }
    
    private void jbInit() {
        this.setLayout(this.gridBagLayout1);
        this.jLabelQuantityTransaction.setText("Quantity :");
        this.jSpinnerQuantity.setMinimumSize(new Dimension(70, 20));
        this.jSpinnerQuantity.setPreferredSize(new Dimension(70, 20));
        this.setIntegerSpinnerProperties(this.jSpinnerQuantity, new SpinnerNumberModel(1, 1, 999, 1));
        this.jToggleButtonBuyBestLimitOrder.setBackground(new Color(145, 255, 200));
        this.jToggleButtonBuyBestLimitOrder.setEnabled(false);
        this.jToggleButtonBuyBestLimitOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonBuyBestLimitOrder.setDoubleBuffered(false);
        this.jToggleButtonBuyBestLimitOrder.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonBuyBestLimitOrder.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonBuyBestLimitOrder.setOpaque(true);
        this.jToggleButtonBuyBestLimitOrder.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonBuyBestLimitOrder.setActionCommand("Bid");
        this.jToggleButtonBuyBestLimitOrder.setMnemonic(66);
        this.jToggleButtonBuyBestLimitOrder.setText("Buy");
        this.jToggleButtonBuyBestLimitOrder.addActionListener(new ClientFrame_jToggleButtonBuyBestLimitOrder_actionAdapter(this));
        this.jToggleButtonSellBestLimitOrder.setBackground(new Color(13, 219, 242));
        this.jToggleButtonSellBestLimitOrder.setEnabled(false);
        this.jToggleButtonSellBestLimitOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonSellBestLimitOrder.setMaximumSize(new Dimension(68, 30));
        this.jToggleButtonSellBestLimitOrder.setMinimumSize(new Dimension(68, 30));
        this.jToggleButtonSellBestLimitOrder.setOpaque(true);
        this.jToggleButtonSellBestLimitOrder.setPreferredSize(new Dimension(68, 30));
        this.jToggleButtonSellBestLimitOrder.setActionCommand("Ask");
        this.jToggleButtonSellBestLimitOrder.setMnemonic(83);
        this.jToggleButtonSellBestLimitOrder.setText("Sell");
        this.jToggleButtonSellBestLimitOrder.addActionListener(new ClientFrame_jToggleButtonSellBestLimitOrder_actionAdapter(this));
        this.jButtonSendOrderBestLimitOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
        this.jButtonSendOrderBestLimitOrder.setEnabled(false);
        this.jButtonSendOrderBestLimitOrder.setFont(new Font("Verdana", 1, 14));
        this.jButtonSendOrderBestLimitOrder.setForeground(Color.red);
        this.jButtonSendOrderBestLimitOrder.setMaximumSize(new Dimension(95, 30));
        this.jButtonSendOrderBestLimitOrder.setMinimumSize(new Dimension(95, 30));
        this.jButtonSendOrderBestLimitOrder.setPreferredSize(new Dimension(95, 30));
        this.jButtonSendOrderBestLimitOrder.setActionCommand("Send");
        this.jButtonSendOrderBestLimitOrder.setMnemonic(69);
        this.jButtonSendOrderBestLimitOrder.setText("Execute");
        this.jButtonSendOrderBestLimitOrder.addActionListener(new Principale_jButtonSendOrderBestLimitOrder_actionAdapter(this));
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.add(this.jLabelQuantityTransaction, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 6, 6, 3), 0, 0));
        this.add(this.jSpinnerQuantity, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.add(this.jPanel1, new GridBagConstraints(2, 0, 1, 2, 1.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jToggleButtonBuyBestLimitOrder, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 0, new Insets(3, 3, 6, 3), 0, 0));
        this.jPanel1.add(this.jToggleButtonSellBestLimitOrder, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.0, 10, 0, new Insets(6, 3, 3, 3), 0, 0));
        this.jPanel1.add(this.jButtonSendOrderBestLimitOrder, new GridBagConstraints(1, 0, 1, 2, 1.0, 0.0, 10, 0, new Insets(6, 3, 6, 6), 0, 0));
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
    
    void jToggleButtonBuyBestLimitOrder_actionPerformed(final ActionEvent e) {
        if (this.jToggleButtonBuyBestLimitOrder.isSelected()) {
            this.activateBuyButton();
            this.desactivateSellButton();
        }
        else {
            this.desactivateBuyButton();
            this.activateSellButton();
        }
        this.activateExecuteButton();
    }
    
    void jToggleButtonSellBestLimitOrder_actionPerformed(final ActionEvent e) {
        if (this.jToggleButtonSellBestLimitOrder.isSelected()) {
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
        this.jToggleButtonSellBestLimitOrder.setSelected(false);
        this.jToggleButtonSellBestLimitOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonSellBestLimitOrder.setBackground(Constants.CLIENT_SELL_INACTIVE);
    }
    
    void desactivateBuyButton() {
        this.jToggleButtonBuyBestLimitOrder.setSelected(false);
        this.jToggleButtonBuyBestLimitOrder.setFont(new Font("Verdana", 0, 14));
        this.jToggleButtonBuyBestLimitOrder.setBackground(Constants.CLIENT_BUY_INACTIVE);
    }
    
    void desactivateExecuteButton() {
        this.jButtonSendOrderBestLimitOrder.setEnabled(false);
        this.jButtonSendOrderBestLimitOrder.setBackground(Constants.CLIENT_EXECUTE_INACTIVE);
    }
    
    void activateSellButton() {
        this.jToggleButtonSellBestLimitOrder.setSelected(true);
        this.jToggleButtonSellBestLimitOrder.setFont(new Font("Verdana", 1, 14));
        this.jToggleButtonSellBestLimitOrder.setBackground(Constants.CLIENT_SELL_ACTIVE);
        this.desactivateBuyButton();
        this.activateExecuteButton();
    }
    
    void activateBuyButton() {
        this.jToggleButtonBuyBestLimitOrder.setSelected(true);
        this.jToggleButtonBuyBestLimitOrder.setFont(new Font("Verdana", 1, 14));
        this.jToggleButtonBuyBestLimitOrder.setBackground(Constants.CLIENT_BUY_ACTIVE);
        this.desactivateSellButton();
        this.activateExecuteButton();
    }
    
    void activateExecuteButton() {
        this.jButtonSendOrderBestLimitOrder.setEnabled(true);
        this.jButtonSendOrderBestLimitOrder.setBackground(Constants.CLIENT_EXECUTE_ACTIVE);
    }
    
    void jButtonSendOrderBestLimitOrder_actionPerformed(final ActionEvent e) {
        final BestLimitOrder bestLimitOrder = new BestLimitOrder();
        if (this.orderValid()) {
            this.desactivateExecuteButton();
            if (this.jToggleButtonBuyBestLimitOrder.isSelected()) {
                bestLimitOrder.setPrice(Float.MAX_VALUE);
            }
            else {
                bestLimitOrder.setPrice(Float.MIN_VALUE);
            }
            bestLimitOrder.setEmitter(ClientCore.getLogin());
            bestLimitOrder.setInstitutionName(this.institution);
            bestLimitOrder.setQuantity(Integer.parseInt(this.jSpinnerQuantity.getValue().toString()));
            bestLimitOrder.setSide(this.jToggleButtonBuyBestLimitOrder.isSelected() ? 1 : 0);
            ClientCore.send(bestLimitOrder);
            this.desactivateBuyButton();
            this.desactivateSellButton();
        }
    }
    
    public boolean orderValid() {
        final boolean response = true;
        final int qtty = Integer.parseInt(this.jSpinnerQuantity.getValue().toString());
        final String assetName = ClientCore.getInstitution(this.institution).getAssetName();
        final float operationMinimalCost = ClientCore.getInstitution(this.institution).getMinimalCost("BestLimit Order");
        final float operationPercentageCost = ClientCore.getInstitution(this.institution).getPercentageCost("BestLimit Order");
        if (this.jToggleButtonBuyBestLimitOrder.isSelected()) {
            final float price = Float.MAX_VALUE;
        }
        return response;
    }
    
    @Override
    public String toString() {
        return "BestLimit Order";
    }
}
