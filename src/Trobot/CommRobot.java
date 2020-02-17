// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package Trobot;

import org.jdom.JDOMException;
import java.io.Reader;
import java.io.StringReader;
import org.jdom.input.SAXBuilder;
import java.io.Writer;
import org.jdom.Document;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import java.io.StringWriter;
import jessx.net.NetworkWritable;
import jessx.client.LoginMessage;
import java.io.IOException;
import java.net.UnknownHostException;
import jessx.utils.Utils;
import java.net.InetAddress;
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class CommRobot extends Thread
{
    private Socket socket;
    private int state;
    private OutputStream output;
    private InputStream input;
    private DataInputStream dataInput;
    private DataOutputStream dataOutput;
    RobotCore robotCore;
    
    public CommRobot(final RobotCore robotCore) {
        this.state = 0;
        this.robotCore = robotCore;
    }
    
    public void connect(final String hostName, final String login, final String password) throws IOException {
        try {
            this.socket = new Socket(InetAddress.getByName(hostName), Integer.parseInt(Utils.appsProperties.getProperty("ServerWaitingPort")));
        }
        catch (UnknownHostException ex3) {
            return;
        }
        catch (NumberFormatException ex) {
            throw ex;
        }
        catch (IOException ex2) {
            throw ex2;
        }
        this.robotCore.getClass();
        this.setState(1);
        try {
            this.input = this.socket.getInputStream();
            this.dataInput = new DataInputStream(this.input);
            this.output = this.socket.getOutputStream();
            this.dataOutput = new DataOutputStream(this.output);
        }
        catch (IOException ex4) {
            return;
        }
        final String javaversion = System.getProperty("java.version");
        this.send(new LoginMessage(login, password, javaversion));
        this.start();
    }
    
    public boolean isConnected() {
        final int state = this.state;
        this.robotCore.getClass();
        return state == 1;
    }
    
    @Override
    public void run() {
        String dataRemaining = "";
        while (true) {
            final int state = this.state;
            this.robotCore.getClass();
            if (state != 1) {
                break;
            }
            try {
                dataRemaining = this.readXmlFromNetwork(String.valueOf(dataRemaining) + this.dataInput.readUTF());
            }
            catch (IOException ex) {
                this.robotCore.getClass();
                this.setState(0);
                this.robotCore.getRobot().setHasToRun(false);
            }
        }
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
    
    private void setState(final int newState) {
        if (newState != this.state) {
            this.state = newState;
            this.robotCore.fireConnectionStateChanged(newState);
        }
    }
    
    private void fireObjectReceived(final Document doc) {
        this.robotCore.fireObjectReceived(doc);
    }
    
    private String readXmlFromNetwork(final String data) {
        final int begin = data.indexOf("<?");
        final int end = data.indexOf("[JessX-end]", begin);
        if (begin != -1 && end != -1) {
            final String message = data.substring(begin, end);
            final SAXBuilder sax = new SAXBuilder();
            try {
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
}
