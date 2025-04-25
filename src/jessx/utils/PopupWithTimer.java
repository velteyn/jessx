// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils;

import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.BorderFactory;
import java.awt.Color;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import java.awt.GridBagLayout;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JFrame;

public class PopupWithTimer extends Thread
{
    private Dialogue dialog;
    private int time;
    private String name;
    private JFrame parentComponent;
    private JButton jbuttonClose;
    private boolean closeActivated;
    
    public PopupWithTimer(final int timeSeconde, final Component comp, final Dimension frameSize, final String title, final JFrame parent) {
        this.jbuttonClose = new JButton("Ok");
        this.closeActivated = false;
        this.dialog = new Dialogue(parent);
        this.parentComponent = parent;
        this.time = timeSeconde;
        this.name = title;
        this.jbuttonClose.addActionListener(new PopupWithTimer_jbuttonClose_actionAdapter(this));
        final JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(203, 230, 211));
        panel.setBorder(BorderFactory.createBevelBorder(1, Color.white, Color.white, new Color(115, 114, 105), new Color(165, 163, 151)));
        panel.add(comp, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 283));
        this.dialog.getContentPane().setLayout(new GridBagLayout());
        this.dialog.getContentPane().add(this.jbuttonClose, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.dialog.getContentPane().add(panel, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Utils.logger.debug("Dimension Screen" + screenSize);
        Utils.logger.debug("Dimension Frame" + frameSize);
        this.dialog.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - Math.min(frameSize.height + 75 + 2, 400)) / 2);
        this.dialog.setSize(Math.max(frameSize.width + 15, 250), Math.min(frameSize.height + 100 + 2, 500));
    }
    
    @Override
    public void run() {
        this.dialog.show();
        this.dialog.setDefaultCloseOperation(1);
        this.parentComponent.setFocusable(false);
        this.dialog.getRootPane().setDefaultButton(this.jbuttonClose);
        try {
            int i = this.time;
            while (!this.closeActivated && i > 0) {
                if (this.dialog.closeEvent) {
                    break;
                }
                --i;
                this.dialog.setTitle(String.valueOf(this.name) + " :" + i + "s");
                int j = 0;
                while (!this.closeActivated && 10 > j++ && !this.dialog.closeEvent) {
                    Thread.sleep(100L);
                }
            }
        }
        catch (final InterruptedException ex) {
            this.parentComponent.setFocusable(true);
            this.dialog.hide();
        }
        this.parentComponent.setFocusable(true);
        this.dialog.hide();
    }
    
    public void jbuttonClose_actionPerformed(final ActionEvent e) {
        this.closeActivated = true;
    }
}
