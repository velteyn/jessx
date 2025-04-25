package jessx.server.net;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import jessx.server.Server;
import jessx.utils.Utils;

public class ClientConnectionPoint extends Thread {
  private ServerSocket serverSocket;
  
  private String AddressIP;
  
  public ClientConnectionPoint() {
    super("ClientConnectionPoint");
    try {
      this.serverSocket = new ServerSocket(Integer.parseInt(Utils.appsProperties.getProperty("ServerWaitingPort")));
    } catch (NumberFormatException ex) {
      Utils.fatalError("Property ServerWaitingPort is not an integer. Could not initialise SocketServer. " + ex.toString(), 1, ex);
    } catch (IOException ex) {
      Utils.fatalError("An Input/output exception has occured while trying to initiate the serverSocket" + ex.toString(), 1, ex);
    } 
  }
  
  public String getIpAddressAndJavaVersion() {
    try {
      this.serverSocket.getInetAddress();
      this.AddressIP = InetAddress.getLocalHost().getHostAddress();
    } catch (UnknownHostException ex) {
      Utils.logger.warn("Unabled to get host IP address. [IGNORED]");
    } 
    return "\"" + this.AddressIP + "\" / \"" + System.getProperty("java.version") + "\"";
  }
  
  public void attenteConnexion() {
    try {
      this.serverSocket.getInetAddress();
      this.AddressIP = InetAddress.getLocalHost().getHostAddress();
      this.serverSocket.getInetAddress();
      Utils.logger.info("Server hostname : " + InetAddress.getLocalHost().getHostName() + ", Server IP address: " + this.AddressIP);
    } catch (UnknownHostException ex) {
      Utils.logger.warn("Unabled to get host IP address. [IGNORED]");
    } 
    while (Server.getServerState() == Server.SERVER_STATE_ONLINE) {
      try {
        (new PreConnectionClient(this.serverSocket.accept())).initiatePlayer();
      } catch (Exception ex1) {
        System.out.println(ex1.toString());
      } 
    } 
  }
  
  public void run() {
    attenteConnexion();
  }
}
