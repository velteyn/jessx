// 
// Decompiled by Procyon v0.6.0
// 

package jessx.client;

import java.io.OutputStream;
import java.net.Socket;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.BufferedOutputStream;
import java.net.ServerSocket;
import jessx.utils.Utils;
import java.io.File;

public class LogSender implements Runnable
{
    private int port;
    
    public LogSender(final int port) {
        this.port = port;
        new Thread(this).start();
    }
    
    public void run() {
        final File file = new File("./client.log");
        if (!file.exists()) {
            Utils.logger.error("Logging file not found... (excepting ./client.log)");
            return;
        }
        try {
            final ServerSocket socketserver = new ServerSocket(this.port);
            while (true) {
                final Socket socket = socketserver.accept();
                final OutputStream out = new BufferedOutputStream(socket.getOutputStream());
                final InputStream in = new BufferedInputStream(new FileInputStream(file));
                Utils.logger.debug("Reading " + in.available() + " bytes from the stream");
                final byte[] buffer = new byte[in.available()];
                final long s = in.read(buffer);
                Utils.logger.debug(String.valueOf(s) + " bytes read.");
                Utils.logger.debug("writing them on network...");
                out.write(buffer);
                out.flush();
                Utils.logger.debug("done.");
                Utils.logger.debug("Closing connection...");
                in.close();
                out.close();
                socket.close();
                Utils.logger.debug("done.");
            }
        }
        catch (final IOException ex) {
            Utils.logger.debug(ex.getMessage());
        }
    }
}
