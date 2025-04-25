// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.net;

import java.util.Vector;
import jessx.business.BusinessCore;
import java.io.DataOutputStream;
import java.io.Writer;
import org.jdom.Document;
import jessx.net.Message;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import java.io.StringWriter;
import java.io.DataInputStream;
import java.io.IOException;
import jessx.utils.Utils;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.Socket;

public class PreConnectionClient
{
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    
    public OutputStream getOutputStream() {
        return this.output;
    }
    
    public InputStream getInputStream() {
        return this.input;
    }
    
    public Socket getSocket() {
        return this.socket;
    }
    
    public PreConnectionClient(final Socket socket) {
        this.socket = socket;
        try {
            Utils.logger.debug("Getting communication streams...");
            this.output = socket.getOutputStream();
            this.input = socket.getInputStream();
        }
        catch (final IOException ex) {
            Utils.logger.error("Error while getting streams from the socket. " + ex.toString());
        }
    }
    
    public void initiatePlayer() {
        Document doc = null;
        try {
            Utils.logger.debug("Waiting for the client to send his identification.");
            final DataInputStream dataInput = new DataInputStream(this.input);
            for (String data = ""; doc == null; doc = Utils.readXmlFromNetwork(data)) {
                data = String.valueOf(data) + dataInput.readUTF();
                Utils.logger.info(data);
            }
        }
        catch (final IOException ex1) {
            this.loginFailed("Could not read the login from the client. Error: " + ex1.toString());
            return;
        }
        final String login = doc.getRootElement().getAttributeValue("login");
        final String pw = doc.getRootElement().getAttributeValue("password");
        final String javaversion = doc.getRootElement().getAttributeValue("javaversion");
        final Player player = NetworkCore.getPlayer(login);
        if (player != null || NetworkCore.getExperimentManager().getExperimentState() != 0) {
            if (NetworkCore.getExperimentManager().getExperimentState() == 0) {
                this.loginFailed("A client with the same login is already connected once.");
            }
            this.loginFailed("Experiment has already begun.");
            return;
        }
        if (this.arePasswordAndLoginValid(login, pw)) {
            final Player newPlayer = new Player(this, "", login, pw);
            NetworkCore.addPlayer(newPlayer);
            newPlayer.setJavaversion(javaversion);
            try {
                Utils.logger.info("Player " + login + " accepted on server");
                final StringWriter writer = new StringWriter();
                new XMLOutputter(Format.getRawFormat()).output(new Document(new Message("Connection accepted.").prepareForNetworkOutput("")), writer);
                new DataOutputStream(this.output).writeUTF(String.valueOf(writer.getBuffer().toString()) + "[JessX-end]");
                this.output.flush();
            }
            catch (final IOException ex2) {
                Utils.logger.error("Something fails when sending back the accepted message to the client: " + ex2.toString());
            }
            return;
        }
        this.loginFailed("Invalid login or password.");
    }
    
    private void loginFailed(final String reason) {
        try {
            final StringWriter writer = new StringWriter();
            new XMLOutputter(Format.getRawFormat()).output(new Document(new Message("login failed: " + reason).prepareForNetworkOutput("")), writer);
            new DataOutputStream(this.output).writeUTF(String.valueOf(writer.getBuffer().toString()) + "[JessX-end]");
            this.output.flush();
            Utils.logger.error("login failed. reason: " + reason);
            this.output.close();
            this.input.close();
        }
        catch (final IOException ex) {
            Utils.logger.error("Could not send to the client his login failed. " + ex.toString());
        }
    }
    
    public boolean arePasswordAndLoginValid(final String login, final String password) {
        if (BusinessCore.getScenario().isPasswordUsed() && (!BusinessCore.getScenario().islistOfParticipantsUsed() || password.equals(BusinessCore.getScenario().getPassword()))) {
            return password.equals(BusinessCore.getScenario().getPassword());
        }
        if (BusinessCore.getScenario().islistOfParticipantsUsed()) {
            Vector vector;
            int i;
            for (vector = BusinessCore.getScenario().getlistOfParticipants(), i = 0; (!((String[])vector.get(i))[0].equals(login) || !((String[])vector.get(i))[1].equals(password)) && i < vector.size() - 1; ++i) {}
            return ((String[])vector.get(i))[0].equals(login) && ((String[])vector.get(i))[1].equals(password);
        }
        return true;
    }
}
