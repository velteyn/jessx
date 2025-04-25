// 
// Decompiled by Procyon v0.6.0
// 

package jessx.agent;

import java.awt.event.WindowEvent;
import java.awt.Point;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;
import java.awt.GridBagLayout;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JFrame;

public class JesseAgentFrame extends JFrame
{
    private static final long serialVersionUID = 4174169981939488216L;
    JPanel contentPane;
    JMenuBar jMenuBar1;
    JMenu jMenuFile;
    JMenuItem jMenuFileExit;
    JMenu jMenuHelp;
    JMenuItem jMenuHelpAbout;
    JLabel statusBar;
    JScrollPane jScrollPane1;
    JTextArea jTextArea1;
    GridBagLayout gridBagLayout1;
    Border border1;
    Border border2;
    
    public JesseAgentFrame() {
        this.jMenuBar1 = new JMenuBar();
        this.jMenuFile = new JMenu();
        this.jMenuFileExit = new JMenuItem();
        this.jMenuHelp = new JMenu();
        this.jMenuHelpAbout = new JMenuItem();
        this.statusBar = new JLabel();
        this.jScrollPane1 = new JScrollPane();
        this.jTextArea1 = new JTextArea();
        this.gridBagLayout1 = new GridBagLayout();
        this.enableEvents(64L);
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.contentPane = (JPanel)this.getContentPane();
        this.border1 = BorderFactory.createEmptyBorder();
        this.border2 = BorderFactory.createLineBorder(Color.black, 1);
        this.contentPane.setLayout(this.gridBagLayout1);
        this.setSize(new Dimension(400, 300));
        this.setTitle("Agent Settings");
        this.statusBar.setText(" ");
        this.jMenuFile.setText("File");
        this.jMenuFileExit.setText("Exit");
        this.jMenuFileExit.addActionListener(new JesseAgentFrame_jMenuFileExit_ActionAdapter(this));
        this.jMenuHelp.setText("Help");
        this.jMenuHelpAbout.setText("About");
        this.jMenuHelpAbout.addActionListener(new JesseAgentFrame_jMenuHelpAbout_ActionAdapter(this));
        this.jTextArea1.setEnabled(true);
        this.jTextArea1.setBorder(this.border1);
        this.jTextArea1.setEditable(false);
        this.jTextArea1.setMargin(new Insets(3, 3, 3, 3));
        this.jTextArea1.setText("");
        this.jScrollPane1.setBorder(this.border2);
        this.jMenuFile.add(this.jMenuFileExit);
        this.jMenuHelp.add(this.jMenuHelpAbout);
        this.jMenuBar1.add(this.jMenuFile);
        this.jMenuBar1.add(this.jMenuHelp);
        this.setJMenuBar(this.jMenuBar1);
        this.contentPane.add(this.statusBar, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 17, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.contentPane.add(this.jScrollPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTextArea1, null);
    }
    
    public void jMenuFileExit_actionPerformed(final ActionEvent e) {
        System.exit(0);
    }
    
    public void jMenuHelpAbout_actionPerformed(final ActionEvent e) {
        final JesseAgentFrame_AboutBox dlg = new JesseAgentFrame_AboutBox(this);
        final Dimension dlgSize = dlg.getPreferredSize();
        final Dimension frmSize = this.getSize();
        final Point loc = this.getLocation();
        dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
        dlg.setModal(true);
        dlg.pack();
        dlg.show();
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == 201) {
            this.jMenuFileExit_actionPerformed(null);
        }
    }
}
