// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import jessx.business.Operator;
import jessx.client.ClientCore;
import org.jdom.Document;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Dimension;
import javax.swing.border.Border;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.event.ChangeListener;
import java.awt.Color;
import javax.swing.event.ChangeEvent;
import java.awt.GridBagLayout;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import jessx.client.event.ConnectionListener;
import jessx.client.event.NetworkListener;
import jessx.client.event.ExperimentDeveloppmentListener;
import jessx.client.event.OperatorPlayedListener;
import jessx.utils.Constants;
import javax.swing.JTabbedPane;

public class PanelServerComm extends JTabbedPane implements Constants, OperatorPlayedListener, ExperimentDeveloppmentListener, NetworkListener, ConnectionListener
{
    private JTextArea TextAreaCommunication;
    private JTextArea TextAreaCommunication2;
    private JScrollPane ScrollPaneCommunication;
    private JScrollPane ScrollPaneCommunication2;
    private JPanel PanelCommunication;
    private JPanel PanelCommunication2;
    private int indexcomm12;
    private int indexcomm22;
    TitledBorder titledBorder162;
    TitledBorder titledBorder172;
    GridBagLayout gridBagLayoutCommunication;
    
    public PanelServerComm() {
        this.TextAreaCommunication = new JTextArea();
        this.TextAreaCommunication2 = new JTextArea();
        this.ScrollPaneCommunication = new JScrollPane();
        this.ScrollPaneCommunication2 = new JScrollPane();
        this.PanelCommunication = new JPanel();
        this.PanelCommunication2 = new JPanel();
        this.indexcomm12 = 0;
        this.indexcomm22 = 1;
        this.gridBagLayoutCommunication = new GridBagLayout();
        this.jbInit();
    }
    
    void PanelServerComm_stateChanged(final ChangeEvent e) {
        final int newIndex = this.getSelectedIndex();
        this.setForegroundAt(newIndex, Color.black);
    }
    
    public void jbInit() {
        this.addChangeListener(new PanelServerComm_changeAdapter(this));
        this.titledBorder162 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "", 0, 0, PanelServerComm.FONT_CLIENT_TITLE_BORDER);
        this.titledBorder172 = new TitledBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0), "", 0, 0, PanelServerComm.FONT_CLIENT_TITLE_BORDER);
        this.TextAreaCommunication.setBackground(UIManager.getColor("Button.background"));
        this.TextAreaCommunication.setEnabled(true);
        this.TextAreaCommunication.setBorder(BorderFactory.createLoweredBevelBorder());
        this.TextAreaCommunication.setEditable(false);
        this.TextAreaCommunication.setSelectedTextColor(Color.white);
        this.TextAreaCommunication.setLineWrap(true);
        this.TextAreaCommunication.setWrapStyleWord(true);
        this.TextAreaCommunication.setFont(PanelServerComm.FONT_CLIENT_TEXTAREA);
        this.TextAreaCommunication2.setBackground(UIManager.getColor("Button.background"));
        this.TextAreaCommunication2.setEnabled(true);
        this.TextAreaCommunication2.setBorder(BorderFactory.createLoweredBevelBorder());
        this.TextAreaCommunication2.setEditable(false);
        this.TextAreaCommunication2.setSelectedTextColor(Color.white);
        this.TextAreaCommunication2.setLineWrap(true);
        this.TextAreaCommunication2.setWrapStyleWord(true);
        this.TextAreaCommunication2.setFont(PanelServerComm.FONT_CLIENT_TEXTAREA);
        this.PanelCommunication.setBorder(this.titledBorder162);
        this.PanelCommunication.setMinimumSize(new Dimension(150, 160));
        this.PanelCommunication.setPreferredSize(new Dimension(150, 160));
        this.PanelCommunication.setLayout(this.gridBagLayoutCommunication);
        this.PanelCommunication2.setBorder(this.titledBorder172);
        this.PanelCommunication2.setMinimumSize(new Dimension(150, 100));
        this.PanelCommunication2.setPreferredSize(new Dimension(150, 100));
        this.PanelCommunication2.setLayout(this.gridBagLayoutCommunication);
        this.PanelCommunication.add(this.ScrollPaneCommunication, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 2, 4), 0, 0));
        this.ScrollPaneCommunication.getViewport().add(this.TextAreaCommunication, null);
        this.PanelCommunication2.add(this.ScrollPaneCommunication2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 2, 4), 0, 0));
        this.ScrollPaneCommunication2.getViewport().add(this.TextAreaCommunication2, null);
        this.add(this.PanelCommunication, "Financial Information", this.indexcomm12);
        this.add(this.PanelCommunication2, "Press Review", this.indexcomm22);
    }
    
    public void messageReceived(final String msg) {
        this.TextAreaCommunication.append("\n===============================\n" + msg);
        final NewMessageCommTimer timerMessage = new NewMessageCommTimer(this, this.indexcomm12);
        timerMessage.start();
    }
    
    public void informationReceived(final String msg) {
        this.TextAreaCommunication2.append("\n===============================\n" + msg);
        final NewMessageCommTimer timerMessage2 = new NewMessageCommTimer(this, this.indexcomm22);
        timerMessage2.start();
    }
    
    public void objectReceived(final Document xmlDoc) {
        if (xmlDoc.getRootElement().getName().equals("Initialisation")) {
            System.out.print("\nInitialisation Client\n");
            ClientCore.initializeForNewExperiment();
            this.TextAreaCommunication.setText("");
            this.TextAreaCommunication2.setText("");
        }
        else if (xmlDoc.getRootElement().getName().equals("Message")) {
            this.TextAreaCommunication.append("\n===============================\n" + xmlDoc.getRootElement().getText());
            final NewMessageCommTimer timerMessage = new NewMessageCommTimer(this, this.indexcomm12);
            timerMessage.start();
        }
        else if (xmlDoc.getRootElement().getName().equals("Information")) {
            this.TextAreaCommunication2.append("\n===============================\n" + xmlDoc.getRootElement().getText());
            final NewMessageCommTimer timerMessage2 = new NewMessageCommTimer(this, this.indexcomm22);
            timerMessage2.start();
        }
    }
    
    public void newOperator(final Operator op) {
    }
    
    public void experimentBegins() {
    }
    
    public void experimentFinished() {
    }
    
    public void periodBegins() {
    }
    
    public void periodFinished() {
    }
    
    public void connectionStateChanged(final int newState) {
    }
}
