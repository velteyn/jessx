// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server.net;

import java.util.Iterator;
import javax.swing.ProgressMonitor;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.io.BufferedInputStream;
import java.net.Socket;
import java.awt.Component;
import javax.swing.JOptionPane;
import jessx.utils.Utils;
import java.io.File;
import java.util.Date;
import java.text.SimpleDateFormat;

public class LogGetter
{
    private int port;
    private String path;
    
    public LogGetter(final int port) {
        final String date = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        this.path = "./logs/" + date + "/";
        final File file = new File(this.path);
        if (file.exists()) {
            Utils.logger.error("Folder " + file.getAbsolutePath() + " already exists.");
            JOptionPane.showConfirmDialog(null, "Folder " + file.getAbsolutePath() + " already exists.", "Erreur", -1, 0);
            return;
        }
        Utils.logger.info("Creating directory " + this.path + " : " + file.mkdirs());
        this.port = port;
    }
    
    public void getLog(final String playerName) {
        Utils.logger.debug("Getting log for the player: " + playerName);
        final String ip = NetworkCore.getPlayer(playerName).getPlayerIP();
        Utils.logger.debug("- opening socket...");
        InputStream in;
        try {
            final Socket socket = new Socket(ip, this.port);
            in = new BufferedInputStream(socket.getInputStream());
        }
        catch (UnknownHostException ex) {
            Utils.logger.error(ex.getMessage());
            return;
        }
        catch (IOException ex2) {
            Utils.logger.error(ex2.getMessage());
            return;
        }
        Utils.logger.debug("- opening file...");
        final byte[] buffer = new byte[16384];
        final File file = new File(String.valueOf(this.path) + playerName + ".log");
        OutputStream out;
        try {
            file.createNewFile();
            out = new BufferedOutputStream(new FileOutputStream(file));
        }
        catch (IOException ex3) {
            Utils.logger.error(ex3.getMessage());
            return;
        }
        Utils.logger.debug("- getting data...");
        try {
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
                out.flush();
            }
        }
        catch (IOException ex4) {
            Utils.logger.error(ex4.getMessage());
        }
        Utils.logger.debug("- closing socket and file...");
        try {
            out.close();
            in.close();
        }
        catch (IOException ex5) {
            Utils.logger.error(ex5.getMessage());
            return;
        }
        Utils.logger.debug(String.valueOf(playerName) + " logs successfully saved.");
    }
    
    public void getLogs(final ProgressMonitor monitor) {
        final Iterator it = NetworkCore.getPlayerList().keySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            ++i;
            final String playerName = (String) it.next();
            monitor.setNote(playerName);
            this.getLog(playerName);
            monitor.setProgress(1);
            if (monitor.isCanceled()) {
                Utils.logger.warn("Getting log operation cancelled.");
                break;
            }
        }
        monitor.close();
    }
}
