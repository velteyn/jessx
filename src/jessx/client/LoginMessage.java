// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.client;

import org.jdom.Element;
import jessx.net.NetworkWritable;

public class LoginMessage implements NetworkWritable
{
    private String login;
    private String password;
    private String javaversion;
    
    public LoginMessage(final String login, final String password, final String javaversion) {
        this.login = login;
        this.password = password;
        this.javaversion = javaversion;
    }
    
    public Element prepareForNetworkOutput(final String pt) {
        final Element toSend = new Element("Login").setAttribute("login", this.login).setAttribute("password", this.password).setAttribute("javaversion", this.javaversion);
        return toSend;
    }
}
