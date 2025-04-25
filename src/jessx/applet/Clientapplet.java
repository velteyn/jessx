// 
// Decompiled by Procyon v0.6.0
// 

package jessx.applet;

import java.io.InputStream;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.awt.TextArea;
import javax.swing.JEditorPane;
import javax.swing.ImageIcon;
import javax.swing.JApplet;

public class Clientapplet extends JApplet
{
    static final long serialVersionUID = 2L;
    static final String APPLETPARAMETER_USERNAME = "user";
    static final String APPLETPARAMETER_PASSWORD = "password";
    String username;
    String userpwd;
    
    public String getParameter(final String key, final String def) {
        return (this.getParameter(key) != null) ? this.getParameter(key) : def;
    }
    
    public Clientapplet() {
        this.username = null;
        this.userpwd = null;
    }
    
    @Override
    public void init() {
        try {
            this.username = this.getParameter("user", "Your Name");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            this.userpwd = this.getParameter("password", "");
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
        final ImageIcon[] images = { new ImageIcon(this.getImage(this.getCodeBase(), "images/logo_JessX_small.PNG")), new ImageIcon(this.getImage(this.getCodeBase(), "images/ecllogo_small.PNG")), new ImageIcon(this.getImage(this.getCodeBase(), "images/ustl_small.PNG")), new ImageIcon(this.getImage(this.getCodeBase(), "images/ecoxp_small.PNG")), new ImageIcon(this.getImage(this.getCodeBase(), "images/logo_trading.PNG")), new ImageIcon(this.getImage(this.getCodeBase(), "images/market_logo.PNG")) };
        final JEditorPane gnuEditorPane = new JEditorPane();
        final TextArea text = this.loadText("gnulicense.txt");
        gnuEditorPane.setText(text.getText());
        FilesLoaded.setImageIconArray(images);
        FilesLoaded.setJEditorPane(gnuEditorPane);
    }
    
    public TextArea loadText(final String fileName) {
        final TextArea zoneTexte = new TextArea();
        try {
            final URL url = new URL(this.getCodeBase(), fileName);
            final InputStream ips = url.openStream();
            final BufferedReader in = new BufferedReader(new InputStreamReader(ips));
            System.out.println("buffered reader ok");
            String ligne;
            while ((ligne = in.readLine()) != null) {
                zoneTexte.append(String.valueOf(ligne) + "\n");
            }
            in.close();
        }
        catch (final Exception e) {
            zoneTexte.append("\n" + e.toString() + "\n");
            System.out.println("fichier non trouv\u00e9");
        }
        return zoneTexte;
    }
}
