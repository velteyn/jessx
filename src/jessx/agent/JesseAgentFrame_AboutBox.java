// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.agent;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.Component;
import javax.swing.BorderFactory;
import java.awt.LayoutManager;
import javax.swing.Icon;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public class JesseAgentFrame_AboutBox extends JDialog implements ActionListener
{
    private static final long serialVersionUID = -6739302799009766586L;
    JPanel panel1;
    JPanel panel2;
    JPanel insetsPanel1;
    JPanel insetsPanel2;
    JPanel insetsPanel3;
    JButton button1;
    JLabel imageLabel;
    JLabel label1;
    JLabel label2;
    JLabel label3;
    JLabel label4;
    ImageIcon image1;
    BorderLayout borderLayout1;
    BorderLayout borderLayout2;
    FlowLayout flowLayout1;
    GridLayout gridLayout1;
    String product;
    String version;
    String copyright;
    String comments;
    
    public JesseAgentFrame_AboutBox(final Frame parent) {
        super(parent);
        this.panel1 = new JPanel();
        this.panel2 = new JPanel();
        this.insetsPanel1 = new JPanel();
        this.insetsPanel2 = new JPanel();
        this.insetsPanel3 = new JPanel();
        this.button1 = new JButton();
        this.imageLabel = new JLabel();
        this.label1 = new JLabel();
        this.label2 = new JLabel();
        this.label3 = new JLabel();
        this.label4 = new JLabel();
        this.image1 = new ImageIcon();
        this.borderLayout1 = new BorderLayout();
        this.borderLayout2 = new BorderLayout();
        this.flowLayout1 = new FlowLayout();
        this.gridLayout1 = new GridLayout();
        this.product = "JesseAgent";
        this.version = "0.1";
        this.copyright = "Copyright (c) 2005";
        this.comments = "agent automatique pour jESSE";
        this.enableEvents(64L);
        try {
            this.jbInit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    JesseAgentFrame_AboutBox() {
        this((Frame)null);
    }
    
    private void jbInit() throws Exception {
        this.image1 = new ImageIcon(JesseAgentFrame.class.getResource("about.png"));
        this.imageLabel.setIcon(this.image1);
        this.setTitle("About");
        this.panel1.setLayout(this.borderLayout1);
        this.panel2.setLayout(this.borderLayout2);
        this.insetsPanel1.setLayout(this.flowLayout1);
        this.insetsPanel2.setLayout(this.flowLayout1);
        this.insetsPanel2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.gridLayout1.setRows(4);
        this.gridLayout1.setColumns(1);
        this.label1.setText(this.product);
        this.label2.setText(this.version);
        this.label3.setText(this.copyright);
        this.label4.setText(this.comments);
        this.insetsPanel3.setLayout(this.gridLayout1);
        this.insetsPanel3.setBorder(BorderFactory.createEmptyBorder(10, 60, 10, 10));
        this.button1.setText("Ok");
        this.button1.addActionListener(this);
        this.insetsPanel2.add(this.imageLabel, null);
        this.panel2.add(this.insetsPanel2, "West");
        this.getContentPane().add(this.panel1, null);
        this.insetsPanel3.add(this.label1, null);
        this.insetsPanel3.add(this.label2, null);
        this.insetsPanel3.add(this.label3, null);
        this.insetsPanel3.add(this.label4, null);
        this.panel2.add(this.insetsPanel3, "Center");
        this.insetsPanel1.add(this.button1, null);
        this.panel1.add(this.insetsPanel1, "South");
        this.panel1.add(this.panel2, "North");
        this.setResizable(true);
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == 201) {
            this.cancel();
        }
        super.processWindowEvent(e);
    }
    
    void cancel() {
        this.dispose();
    }
    
    public void actionPerformed(final ActionEvent e) {
        if (e.getSource() == this.button1) {
            this.cancel();
        }
    }
}
