// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.gui;

import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Icon;
import java.awt.Dimension;
import java.net.URL;
import java.awt.LayoutManager;
import java.awt.Color;
import javax.swing.BorderFactory;
import java.awt.SystemColor;
import java.awt.Frame;
import javax.swing.border.Border;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import javax.swing.ImageIcon;
import javax.swing.JDialog;

public class AnalysisStartingPopup extends JDialog
{
    ImageIcon logoJessx;
    GridBagLayout gridBagLayout1;
    JPanel jPanel1;
    JLabel jLabel1;
    GridBagLayout gridBagLayout2;
    JLabel jLabel_progression;
    Border border1;
    Border border2;
    
    public AnalysisStartingPopup(final Frame parent) {
        super(parent);
        this.logoJessx = new ImageIcon();
        this.gridBagLayout1 = new GridBagLayout();
        this.jPanel1 = new JPanel();
        this.jLabel1 = new JLabel();
        this.gridBagLayout2 = new GridBagLayout();
        this.jLabel_progression = new JLabel();
        this.enableEvents(64L);
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public AnalysisStartingPopup() {
        this((Frame)null);
    }
    
    private void jbInit() throws Exception {
        this.border1 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
        this.border2 = BorderFactory.createLineBorder(SystemColor.controlText, 1);
        this.setUndecorated(true);
        this.getContentPane().setLayout(this.gridBagLayout1);
        this.setModal(false);
        this.setResizable(false);
        final String fileSeparator = System.getProperty("file.separator");
        final String imagesDir = String.valueOf(System.getProperty("user.dir")) + fileSeparator + "images" + fileSeparator;
        this.logoJessx = new ImageIcon(new URL("file:" + imagesDir + "logo_JessX_small.PNG"));
        this.jLabel1.setBackground(Color.white);
        this.jLabel1.setMaximumSize(new Dimension(387, 169));
        this.jLabel1.setMinimumSize(new Dimension(387, 169));
        this.jLabel1.setPreferredSize(new Dimension(387, 169));
        this.jLabel1.setIcon(this.logoJessx);
        this.jLabel1.setText("");
        this.jPanel1.setLayout(this.gridBagLayout2);
        this.jLabel_progression.setBackground(Color.white);
        this.jLabel_progression.setMaximumSize(new Dimension(800, 64));
        this.jLabel_progression.setMinimumSize(new Dimension(200, 16));
        this.jLabel_progression.setPreferredSize(new Dimension(200, 16));
        this.jPanel1.setBackground(SystemColor.inactiveCaptionText);
        this.jPanel1.setBorder(this.border2);
        this.getContentPane().add(this.jPanel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanel1.add(this.jLabel_progression, new GridBagConstraints(0, 1, 1, 1, 1.0, 0.0, 10, 2, new Insets(6, 6, 6, 6), 0, 0));
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
}
