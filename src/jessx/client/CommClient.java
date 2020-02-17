// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.client;

import java.io.Writer;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import java.io.StringWriter;
import org.jdom.JDOMException;
import java.io.Reader;
import java.io.StringReader;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import jessx.net.NetworkWritable;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import jessx.utils.Utils;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class CommClient extends Thread
{
    private Socket socket;
    private int state;
    private OutputStream output;
    private InputStream input;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    
    CommClient() {
        this.state = 0;
    }
    
    private void setState(final int newState) {
        if (newState != this.state) {
            ClientCore.fireConnectionStateChanged(this.state = newState);
        }
    }
    
    public void connect(final String hostName, final String login, final String password) throws IOException {
        try {
            Utils.logger.debug("Getting the socket to the server...");
            this.socket = new Socket(InetAddress.getByName(hostName), Integer.parseInt(Utils.appsProperties.getProperty("ServerWaitingPort")));
        }
        catch (UnknownHostException ex4) {
            Utils.logger.error("Host " + hostName + " unknown. Connection aborted. Retry with an other hostname.");
            return;
        }
        catch (NumberFormatException ex) {
            Utils.logger.fatal("Could not connect: property ServerWaitingPort in client.properties is not an integer: " + ex.toString());
            throw ex;
        }
        catch (IOException ex2) {
            Utils.logger.fatal("IOError while trying to connect to server: " + ex2.toString());
            throw ex2;
        }
        this.setState(1);
        try {
            Utils.logger.debug("Getting communications streams...");
            this.input = this.socket.getInputStream();
            this.dataInput = new DataInputStream(this.input);
            this.output = this.socket.getOutputStream();
            this.dataOutput = new DataOutputStream(this.output);
        }
        catch (IOException ex3) {
            Utils.logger.error("Error getting streams from the socket: " + ex3.toString() + ". try to reconnect later.");
            return;
        }
        final String javaversion = System.getProperty("java.version");
        this.send(new LoginMessage(login, password, javaversion));
        this.start();
    }
    
    public boolean isConnected() {
        return this.state == 1;
    }
    
    @Override
    public void run() {
        String dataRemaining = "";
        Utils.logger.debug("Listenning to input streams...");
        while (this.state == 1) {
            try {
                Utils.logger.debug("Waiting for data...");
                dataRemaining = this.readXmlFromNetwork(String.valueOf(dataRemaining) + this.dataInput.readUTF());
            }
            catch (IOException ex) {
                Utils.logger.error("Error reading input stream: " + ex.toString());
                this.setState(0);
            }
        }
    }
    
    private void fireObjectReceived(final Document doc) {
        Utils.logger.info("Object received. Type: " + doc.getRootElement().getName());
        ClientCore.fireObjectReceived(doc);
    }
    
    private String readXmlFromNetwork(final String data) {
        final int begin = data.indexOf("<?");
        final int end = data.indexOf("[JessX-end]", begin);
        if (begin != -1 && end != -1) {
            final String message = data.substring(begin, end);
            final SAXBuilder sax = new SAXBuilder();
            try {
                Utils.logger.debug(message);
                this.fireObjectReceived(sax.build(new StringReader(message)));
            }
            catch (IOException ex2) {}
            catch (JDOMException ex) {
                Utils.logger.error("Could not read message : " + message + ". Error: " + ex.toString());
            }
            return this.readXmlFromNetwork(data.substring(end + "[JessX-end]".length()));
        }
        if (begin == -1) {
            return "";
        }
        return data.substring(begin);
    }
    
    public synchronized void send(final NetworkWritable object) {
        try {
            Utils.logger.debug("Preparing the stream and object (" + object.getClass().toString() + ") for output...");
            final StringWriter writer = new StringWriter();
            new XMLOutputter(Format.getRawFormat()).output(new Document(object.prepareForNetworkOutput(null)), writer);
            final String message = writer.getBuffer().toString();
            Utils.logger.debug("Writing to server:" + message);
            this.dataOutput.writeUTF(String.valueOf(message) + "[JessX-end]");
            this.dataOutput.flush();
            Utils.logger.debug("Output done successfully.");
        }
        catch (IOException ex) {
            Utils.logger.error("Unable to write to output streams: " + ex.toString());
        }
    }
}
