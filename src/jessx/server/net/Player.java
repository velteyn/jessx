package jessx.server.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;
import jessx.business.BusinessCore;
import jessx.business.Operation;
import jessx.business.OperationNotCreatedException;
import jessx.business.Portfolio;
import jessx.business.event.PortfolioEvent;
import jessx.business.event.PortfolioListener;
import jessx.net.ExpUpdate;
import jessx.net.NetworkWritable;
import jessx.net.OperatorPlayed;
import jessx.utils.Utils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class Player extends Thread implements PortfolioListener {
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
  
  private int status = 1;
  
  private int state = 2;
  
  private Vector listeners = new Vector();
  
  public void portfolioModified(PortfolioEvent e) {
    send((NetworkWritable)this.portfolio);
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
  
  public void setPlayerCategory(String playerCategory) {
    if (NetworkCore.getExperimentManager().getExperimentState() != 0)
      if (this.playerType != null && !this.playerType.equals(""))
        return;  
    Utils.logger.info("player " + getLogin() + " is now a " + 
        playerCategory);
    this.playerType = playerCategory;
    Utils.logger.debug("Initiating his portfolio with the " + playerCategory + 
        " default portfolio.");
    this.portfolio = (Portfolio)BusinessCore.getScenario().getPlayerType(
        playerCategory).getPortfolio().clone();
    this.portfolio.setNonInvestedCash(this.portfolio.getCash());
    this.portfolio.setNonInvestedOwnings(this.portfolio.getOwnings());
    portfolioModified((PortfolioEvent)null);
    this.portfolio.addListener(this);
    if (NetworkCore.getExperimentManager().getExperimentState() != 0)
      initClient(); 
  }
  
  public String getJavaversion() {
    return this.javaversion;
  }
  
  public void setJavaversion(String javaVersion) {
    this.javaversion = javaVersion;
  }
  
  public Portfolio getPortfolio() {
    return this.portfolio;
  }
  
  private void setStatus(int newStatus) {
    this.status = newStatus;
    firePlayerStateChanged();
  }
  
  public void setState(int newState) {
    this.state = newState;
    firePlayerStateChanged();
  }
  
  public void reinitConnection(PreConnectionClient connecSettings) {
    this.input = connecSettings.getInputStream();
    this.dataInput = new DataInputStream(this.input);
    this.output = connecSettings.getOutputStream();
    this.dataOutput = new DataOutputStream(this.output);
    this.socket = connecSettings.getSocket();
    setStatus(1);
    if (NetworkCore.getExperimentManager().getExperimentState() != 0 && 
      this.playerType != null)
      initClient(); 
  }
  
  public void playerDeleteByServer() {
    setStatus(0);
    try {
      this.input.close();
      this.output.flush();
      this.output.close();
    } catch (IOException ex) {
      Utils.logger.warn("playerDeleteByServer");
    } 
  }
  
  public Player(PreConnectionClient connecSettings) {
    reinitConnection(connecSettings);
    listenToClient();
  }
  
  public Player(PreConnectionClient connecSettings, String playerType) {
    this(connecSettings);
    this.playerType = playerType;
  }
  
  public Player(PreConnectionClient connecSettings, String playerType, String login, String password) {
    this(connecSettings, playerType);
    this.login = login;
    this.password = password;
  }
  
  public void listenToClient() {
    start();
  }
  
  public synchronized void send(NetworkWritable message) {
    if (getPlayerStatus() == 1) {
      Utils.logger.debug("preparing object " + message.getClass().toString() + 
          " for the client " + this.login + ".");
      Element rootNode = message.prepareForNetworkOutput(
          getPlayerCategory());
      send(new Document(rootNode));
    } 
  }
  
  public synchronized void send(Document document) {
    if (getPlayerStatus() == 1)
      try {
        Utils.logger.debug("Writing rootNode  to the client " + this.login + ".");
        XMLOutputter xmlWritter = new XMLOutputter(Format.getRawFormat());
        StringWriter writer = new StringWriter();
        xmlWritter.output(document, writer);
        this.dataOutput.writeUTF(String.valueOf(writer.getBuffer().toString()) + "[JessX-end]");
        this.dataOutput.flush();
      } catch (IOException ex) {
        Utils.logger.error("Error writing to the client: " + ex.toString());
      }  
  }
  
  public void addListener(PlayerStateListener listener) {
    this.listeners.add(listener);
  }
  
  public void removeListener(PlayerStateListener listener) {
    this.listeners.remove(listener);
  }
  
  private void firePlayerStateChanged() {
    for (int i = 0; i < this.listeners.size(); i++)
      ((PlayerStateListener)this.listeners.elementAt(i)).playerStateChanged(
          getLogin()); 
  }
  
  public void run() {
    String dataRemaining = "";
    while (true) {
      while (this.status != 1) {
        try {
          sleep(1000L);
        } catch (InterruptedException ex) {
          IOException ex1 = null;
          Utils.logger.debug("Sleep of the client " + this.login + 
              " was interrupted for an unknown reason. (" + 
              ex1.toString() + ")[IGNORED]");
        } 
      } 
      try {
        Utils.logger.debug("Waiting for some data...");
        dataRemaining = readXmlFromNetwork(String.valueOf(dataRemaining) + this.dataInput.readUTF());
      } catch (IOException iOException) {
        Utils.logger.warn("Client stream has been closed. " + iOException.toString());
        setStatus(0);
      } 
    } 
  }
  
  public void isClientReady(String explanation) {
    setState(1);
    send((NetworkWritable)new ExpUpdate(7, explanation, 
          NetworkCore.getExperimentManager()
          .getPeriodNum()));
  }
  
  public void initClient() {
    send((NetworkWritable)getPortfolio());
    Vector<String> institutionList = new Vector();
    Iterator<String> iter = BusinessCore.getScenario().getPlayerType(
        getPlayerCategory()).getOperatorsPlayed().keySet().iterator();
    while (iter.hasNext()) {
      String opCompleteName = iter.next();
      int index = opCompleteName.lastIndexOf(" on ");
      String institutionName = opCompleteName.substring(index + 4);
      if (!institutionList.contains(institutionName))
        institutionList.add(institutionName); 
    } 
    Utils.logger.debug("Sending institutions the client needs.");
    for (int i = 0; i < institutionList.size(); i++)
      send((NetworkWritable)BusinessCore.getInstitution(institutionList.elementAt(
              i))); 
    Utils.logger.debug("Sending the operators the player is allowed to play.");
    Iterator<String> iter2 = BusinessCore.getScenario().getPlayerType(
        getPlayerCategory()).getOperatorsPlayed().keySet().iterator();
    while (iter2.hasNext())
      send((NetworkWritable)new OperatorPlayed(iter2.next())); 
  }
  
  private void fireObjectReceived(Document xmlDoc) {
    Utils.logger.info("Object received. Type: " + 
        xmlDoc.getRootElement().getName());
    if (xmlDoc.getRootElement().getName().equals("ExperimentUpdate")) {
      setState(0);
    } else if (xmlDoc.getRootElement().getName().equals("Warn")) {
      send(xmlDoc);
    } else if (xmlDoc.getRootElement().getName().equals("Operation")) {
      try {
        Operation op = Operation.initOperationFromXml(xmlDoc.getRootElement());
        BusinessCore.getInstitution(op.getInstitutionName()).treatOperation(op);
      } catch (OperationNotCreatedException ex) {
        Utils.logger.error(
            "Could not create operation received from client. [IGNORED]");
      } 
    } 
  }
  
  private String readXmlFromNetwork(String data) {
    int begin = data.indexOf("<?");
    int end = data.indexOf("[JessX-end]", begin);
    if (begin != -1 && end != -1) {
      String message = data.substring(begin, end);
      SAXBuilder sax = new SAXBuilder();
      try {
        fireObjectReceived(sax.build(new StringReader(message)));
      } catch (IOException iOException) {
      
      } catch (JDOMException ex) {
        Utils.logger.error("Could not read message : " + message + ". Error: " + 
            ex.toString());
      } 
      return readXmlFromNetwork(data.substring(end + "[JessX-end]".length()));
    } 
    if (begin == -1)
      return ""; 
    return data.substring(begin);
  }
}
