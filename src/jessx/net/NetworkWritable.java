// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.net;

import org.jdom.Element;
import java.io.Serializable;

public interface NetworkWritable extends Serializable
{
    Element prepareForNetworkOutput(final String p0);
}
