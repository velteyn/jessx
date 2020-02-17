package jessx.server.net;

/***************************************************************/
/*                     SOFTWARE SECTION                        */
/***************************************************************/
/*
 * <p>Name: Jessx</p>
 * <p>Description: Financial Market Simulation Software</p>
 * <p>Licence: GNU General Public License</p>
 * <p>Organisation: EC Lille / USTL</p>
 * <p>Persons involved in the project : group T.E.A.M.</p>
 * <p>More details about this source code at :
 *    http://eleves.ec-lille.fr/~ecoxp03  </p>
 * <p>Current version: 1.0</p>
 */

/***************************************************************/
/*                      LICENCE SECTION                        */
/***************************************************************/
/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

/***************************************************************/
/*                       IMPORT SECTION                        */
/***************************************************************/

import java.io.*;
import java.net.*;

import jessx.server.*;
import jessx.utils.*;

/***************************************************************/
/*           ClientConnectionPoint CLASS SECTION               */
/***************************************************************/
/**
 * <p>Title : ClientConnectionPoint</p>
 * <p>Description : </p>
 * @author Thierry Curtil
 * @version 1.0
 */

public class ClientConnectionPoint extends Thread {

  private ServerSocket serverSocket;
  private String AddressIP ;

  public ClientConnectionPoint() {
  super("ClientConnectionPoint");
    try {
      this.serverSocket = new ServerSocket(Integer.parseInt(Utils.appsProperties.getProperty("ServerWaitingPort")));
    }
    catch (NumberFormatException ex) {
      Utils.fatalError("Property ServerWaitingPort is not an integer. Could not initialise SocketServer. " + ex.toString(), 1, ex);
    }
    catch (IOException ex) {
      Utils.fatalError("An Input/output exception has occured while trying to initiate the serverSocket" + ex.toString(), 1, ex);
    }
  }

  /**
   *
   * @return String
   */
  public String getIpAddressAndJavaVersion ()
  {
    try {
      AddressIP=serverSocket.getInetAddress().getLocalHost().getHostAddress();
    }
    catch (UnknownHostException ex) {
      Utils.logger.warn("Unabled to get host IP address. [IGNORED]");
    }
    return "\""+AddressIP+"\" / \""+(System.getProperty("java.version"))+"\"";
  }


  /**
   *
   */
  public void attenteConnexion() {

   try {
     AddressIP=serverSocket.getInetAddress().getLocalHost().getHostAddress();
     Utils.logger.info("Server hostname : " + serverSocket.getInetAddress().getLocalHost().getHostName() + ", Server IP address: " + AddressIP);
   }
   catch (UnknownHostException ex) {
     Utils.logger.warn("Unabled to get host IP address. [IGNORED]");
   }

   while (Server.getServerState() == Server.SERVER_STATE_ONLINE) {

     try {
       new PreConnectionClient(serverSocket.accept()).initiatePlayer();
       // serveur.accept() wait until a client try to connect and return
       // a socket with which we will be able to communicate with the client.
       // Then we create a thread that will be dedicated to the communication
       // with this client.
     }
     catch (Exception ex1) {
       System.out.println(ex1.toString());
     }

     // Waiting next client.
   }
 }

 /**
  *
  */
 public void run() {
 this.attenteConnexion();
 }
}
