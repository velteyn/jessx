// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class JarClassLoader extends URLClassLoader
{
    public JarClassLoader(final String jarPath) throws MalformedURLException {
        super(new URL[] { new URL(jarPath) });
    }
    
    public Class findClass(final String className) throws ClassNotFoundException {
        return super.findClass(className);
    }
}
