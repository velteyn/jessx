// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.gclient.gui;

import java.io.IOException;
import jessx.client.ClientCore;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.LayoutManager;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.Properties;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import jessx.utils.Utils;
import java.awt.Frame;
import jessx.utils.Constants;

public class ConnectionPopup implements Constants
{
    public ConnectionPopup(final Frame parentFrame) {
        Utils.logger.debug("looking for user home directory.");
        String settingsFile = null;
        final String settingsDirectory = Utils.getApplicationSettingsDirectory();
        if (settingsDirectory != null) {
            settingsFile = String.valueOf(settingsDirectory) + "lastLogin";
        }
        Utils.logger.debug("Initializing the connexion pop up.");
        final JTextField loginField = new JTextField("Your Name");
        final JTextField addressField = new JTextField("localhost");
        final JPasswordField passwordField = new JPasswordField();
        if (settingsFile != null) {
            try {
                Utils.logger.debug("loading last connection parameters...");
                final Properties p = new Properties();
                p.load(new FileInputStream(settingsFile));
                final String login = p.getProperty("login", "");
                final String host = p.getProperty("host", "");
                loginField.setText(login);
                addressField.setText(host);
                Utils.logger.debug("done.");
            }
            catch (Exception e) {
                Utils.logger.error("Unable to load login configuration file : " + settingsFile);
            }
        }
        final JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Login: "));
        panel.add(loginField);
        panel.add(new JLabel("Password: "));
        panel.add(passwordField);
        panel.add(new JLabel("Server address: "));
        panel.add(addressField);
        Utils.logger.info("Launching pop up.");
        final int reponse = JOptionPane.showConfirmDialog(parentFrame, panel, "JessX server", -1, 1);
        if (reponse == 0) {
            String host = addressField.getText();
            if (host.equals("")) {
                host = "localhost";
            }
            Utils.logger.info("host: " + host);
            String login2 = loginField.getText();
            if (!login2.equals("")) {
                Utils.logger.info("login: " + login2);
            }
            else {
                login2 = "Your Name";
                Utils.logger.warn("No login entered. Setting a default login: " + login2);
            }
            String password = String.valueOf(passwordField.getPassword());
            if (!password.equals("")) {
                Utils.logger.debug("got password. ");
            }
            else {
                Utils.logger.warn("No password entered.");
            }
            if (settingsFile != null) {
                try {
                    Utils.logger.debug("Saving parameters to config file...");
                    final Properties p2 = new Properties();
                    p2.setProperty("host", host);
                    p2.setProperty("login", login2);
                    p2.store(new FileOutputStream(settingsFile), "Login information");
                    Utils.logger.debug("done.");
                }
                catch (Exception e2) {
                    Utils.logger.error("Error while writing login configuration file : " + settingsFile);
                }
            }
            try {
                ClientCore.connecToServer(host, login2, password);
            }
            catch (IOException ex) {
                Utils.logger.warn("java version : " + System.getProperty("java.version"));
                JOptionPane.showMessageDialog(parentFrame, "Connection to server failed. Check host, IP and route.");
            }
            password = null;
        }
    }
}
