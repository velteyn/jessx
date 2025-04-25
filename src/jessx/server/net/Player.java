// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.net;

import org.jdom.JDOMException;
import java.io.Reader;
import java.io.StringReader;
import org.jdom.input.SAXBuilder;
import jessx.business.OperationNotCreatedException;
import jessx.business.Operation;
import java.util.Iterator;
import jessx.net.OperatorPlayed;
import jessx.net.ExpUpdate;
import java.io.Writer;
import java.io.StringWriter;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import org.jdom.Element;
import org.jdom.Document;
import java.io.IOException;
import jessx.business.BusinessCore;
import jessx.utils.Utils;
import jessx.net.NetworkWritable;
import jessx.business.event.PortfolioEvent;
import java.util.Vector;
import jessx.business.Portfolio;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import jessx.business.event.PortfolioListener;

public class Player extends Thread implements PortfolioListener
{
    public static final int CLIENT_STATUS_CONNECTED = 1;
    public static final int CLIENT_STATUS_DISCONNECTED = 0;
    public static final int CLIENT_READY = 0;
    public static final int CLIENT_BUSY = 1;
    private String login;
    private String password;
    private String playerType;
    private String javaversion;
    private Socket socket;
    private OutputStream output;
    private DataOutputStream dataOutput;
    private InputStream input;
    private DataInputStream dataInput;
    private Portfolio portfolio;
    private int status;
    private int state;
    private Vector listeners;
    
    public void portfolioModified(final PortfolioEvent e) {
        this.send(this.portfolio);
    }
    
    public String getLogin() {
        return this.login;
    }
    
    public String getPassword() {
        return this.password;
    }
    
    public String getPlayerCategory() {
        return this.playerType;
    }
    
    public int getPlayerStatus() {
        return this.status;
    }
    
    public int getPlayerState() {
        return (this.status == 0) ? 0 : this.state;
    }
    
    public String getPlayerIP() {
        return this.socket.getInetAddress().getHostAddress();
    }
    
    public void setPlayerCategory(final String playerCategory) {
        if (NetworkCore.getExperimentManager().getExperimentState() != 0 && this.playerType != null && !this.playerType.equals("")) {
            return;
        }
        Utils.logger.info("player " + this.getLogin() + " is now a " + playerCategory);
        this.playerType = playerCategory;
        Utils.logger.debug("Initiating his portfolio with the " + playerCategory + " default portfolio.");
        (this.portfolio = (Portfolio)BusinessCore.getScenario().getPlayerType(playerCategory).getPortfolio().clone()).setNonInvestedCash(this.portfolio.getCash());
        this.portfolio.setNonInvestedOwnings(this.portfolio.getOwnings());
        this.portfolioModified(null);
        this.portfolio.addListener(this);
        if (NetworkCore.getExperimentManager().getExperimentState() != 0) {
            this.initClient();
        }
    }
    
    public String getJavaversion() {
        return this.javaversion;
    }
    
    public void setJavaversion(final String javaVersion) {
        this.javaversion = javaVersion;
    }
    
    public Portfolio getPortfolio() {
        return this.portfolio;
    }
    
    private void setStatus(final int newStatus) {
        this.status = newStatus;
        this.firePlayerStateChanged();
    }
    
    public void setState(final int newState) {
        this.state = newState;
        this.firePlayerStateChanged();
    }
    
    public void reinitConnection(final PreConnectionClient connecSettings) {
        this.input = connecSettings.getInputStream();
        this.dataInput = new DataInputStream(this.input);
        this.output = connecSettings.getOutputStream();
        this.dataOutput = new DataOutputStream(this.output);
        this.socket = connecSettings.getSocket();
        this.setStatus(1);
        if (NetworkCore.getExperimentManager().getExperimentState() != 0 && this.playerType != null) {
            this.initClient();
        }
    }
    
    public void playerDeleteByServer() {
        this.setStatus(0);
        try {
            this.input.close();
            this.output.flush();
            this.output.close();
        }
        catch (final IOException ex) {
            Utils.logger.warn("playerDeleteByServer");
        }
    }
    
    public Player(final PreConnectionClient connecSettings) {
        this.status = 1;
        this.state = 2;
        this.listeners = new Vector();
        this.reinitConnection(connecSettings);
        this.listenToClient();
    }
    
    public Player(final PreConnectionClient connecSettings, final String playerType) {
        this(connecSettings);
        this.playerType = playerType;
    }
    
    public Player(final PreConnectionClient connecSettings, final String playerType, final String login, final String password) {
        this(connecSettings, playerType);
        this.login = login;
        this.password = password;
    }
    
    public void listenToClient() {
        this.start();
    }
    
    public synchronized void send(final NetworkWritable message) {
        if (this.getPlayerStatus() == 1) {
            Utils.logger.debug("preparing object " + message.getClass().toString() + " for the client " + this.login + ".");
            final Element rootNode = message.prepareForNetworkOutput(this.getPlayerCategory());
            this.send(new Document(rootNode));
        }
    }
    
    public synchronized void send(final Document document) {
        if (this.getPlayerStatus() == 1) {
            try {
                Utils.logger.debug("Writing rootNode  to the client " + this.login + ".");
                final XMLOutputter xmlWritter = new XMLOutputter(Format.getRawFormat());
                final StringWriter writer = new StringWriter();
                xmlWritter.output(document, writer);
                this.dataOutput.writeUTF(String.valueOf(writer.getBuffer().toString()) + "[JessX-end]");
                this.dataOutput.flush();
            }
            catch (final IOException ex) {
                Utils.logger.error("Error writing to the client: " + ex.toString());
            }
        }
    }
    
    public void addListener(final PlayerStateListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeListener(final PlayerStateListener listener) {
        this.listeners.remove(listener);
    }
    
    private void firePlayerStateChanged() {
        for (int i = 0; i < this.listeners.size(); ++i) {
            ((PlayerStateListener) this.listeners.elementAt(i)).playerStateChanged(this.getLogin());
        }
    }
    
    @Override
    public void run() {
        String dataRemaining = "";
        while (true) {
            if (this.status != 1) {
                try {
                    Thread.sleep(1000L);
                }
                catch (final InterruptedException ex) {
                    Utils.logger.debug("Sleep of the client " + this.login + " was interrupted for an unknown reason. (" + ex.toString() + ")[IGNORED]");
                }
            }
            else {
                try {
                    Utils.logger.debug("Waiting for some data...");
                    dataRemaining = this.readXmlFromNetwork(String.valueOf(dataRemaining) + this.dataInput.readUTF());
                }
                catch (final IOException ex2) {
                    Utils.logger.warn("Client stream has been closed. " + ex2.toString());
                    this.setStatus(0);
                }
            }
        }
    }
    
    public void isClientReady(final String explanation) {
        this.setState(1);
        this.send(new ExpUpdate(7, explanation, NetworkCore.getExperimentManager().getPeriodNum()));
    }
    
    public void initClient() {
        this.send(this.getPortfolio());
        final Vector institutionList = new Vector();

        Iterator iter = BusinessCore.getScenario().getPlayerType(this.
            getPlayerCategory()).getOperatorsPlayed().keySet().iterator();
        while (iter.hasNext()) {
          String opCompleteName = (String) iter.next();
            final int index = opCompleteName.lastIndexOf(" on ");
            final String institutionName = opCompleteName.substring(index + 4);
            if (!institutionList.contains(institutionName)) {
                institutionList.add(institutionName);
            }
        }
        Utils.logger.debug("Sending institutions the client needs.");
        for (int i = 0; i < institutionList.size(); ++i) {
            this.send(BusinessCore.getInstitution((String) institutionList.elementAt(i)));
        }
        Utils.logger.debug("Sending the operators the player is allowed to play.");
        final Iterator iter2 = BusinessCore.getScenario().getPlayerType(this.getPlayerCategory()).getOperatorsPlayed().keySet().iterator();
        while (iter2.hasNext()) {
            this.send(new OperatorPlayed((String) iter2.next()));
        }
    }
    
    private void fireObjectReceived(final Document xmlDoc) {
        Utils.logger.info("Object received. Type: " + xmlDoc.getRootElement().getName());
        if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
            this.setState(0);
        }
        else if (xmlDoc.getRootElement().getName().equals("Warn")) {
            this.send(xmlDoc);
        }
        else if (xmlDoc.getRootElement().getName().equals("Operation")) {
            try {
                final Operation op = Operation.initOperationFromXml(xmlDoc.getRootElement());
                BusinessCore.getInstitution(op.getInstitutionName()).treatOperation(op);
            }
            catch (final OperationNotCreatedException ex) {
                Utils.logger.error("Could not create operation received from client. [IGNORED]");
            }
        }
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
            catch (final IOException ex2) {}
            catch (final JDOMException ex) {
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
