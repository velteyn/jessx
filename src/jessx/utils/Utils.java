// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import org.jdom.JDOMException;
import java.io.Reader;
import java.io.StringReader;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import java.io.OutputStream;
import java.io.FileOutputStream;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import org.jdom.input.SAXBuilder;
import org.jdom.Document;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import javax.swing.JFileChooser;
import java.awt.Component;
import javax.swing.JOptionPane;
import java.util.Properties;
import org.apache.log4j.Logger;

public abstract class Utils implements Constants
{
    public static Logger logger;
    public static Properties appsProperties;
    
    static {
        Utils.logger = Logger.getLogger(Utils.class.getName());
        Utils.appsProperties = new Properties();
    }
    
    public static void showErrorDialog(final String message) {
        JOptionPane.showMessageDialog(null, message, "Error", 0);
    }
    
    public static JFileChooser newFileChooser(final String directory, final String selectedFile, final String filterDescription, final String filterExtension) {
        System.out.println("Creating file chooser...");
        JFileChooser chooser;
        if (directory != null) {
            chooser = new JFileChooser(directory);
        }
        else {
            chooser = new JFileChooser(System.getProperty("user.dir"));
        }
        if (filterDescription != null && filterExtension != null) {
            final FileFilter filter = new FileChooserFilter(filterDescription, filterExtension);
            chooser.addChoosableFileFilter(filter);
        }
        if (selectedFile != null) {
            chooser.setSelectedFile(new File(selectedFile));
        }
        System.out.println("Sending back the chooser...");
        return chooser;
    }
    
    public static String getApplicationSettingsDirectory() {
        return null;
    }
    
    public static void SetApplicationProperties(final String key, final String value) {
        Utils.appsProperties.setProperty(key, value);
    }
    
    public static void loadApplicationProperties(final String path, final Properties prop) {
        Utils.logger.debug("Loading client properties from file: " + path);
        FileInputStream in;
        try {
            in = new FileInputStream(path);
        }
        catch (final FileNotFoundException ex) {
            final String errorMessage = "Error, missing file: " + path + "\nThis file should be included in the software distribution.";
            Utils.logger.fatal(errorMessage);
            JOptionPane.showMessageDialog(null, errorMessage, "error: missing file.", 0);
            ex.printStackTrace();
            System.exit(1);
            return;
        }
        try {
            prop.load(in);
        }
        catch (final IOException ex2) {
            final String errorMessage = "Error, could not read file: " + path + "\nYou should have read access to this file.";
            Utils.logger.fatal(errorMessage);
            JOptionPane.showMessageDialog(null, errorMessage, "error: could not read file.", 0);
            ex2.printStackTrace();
            System.exit(1);
            return;
        }
        try {
            in.close();
        }
        catch (final IOException ex3) {
            final String errorMessage = "Error, could not close file: " + path + "\nApparently reading successful. [IGNORED]";
            Utils.logger.error(errorMessage);
        }
    }
    
    public static void loadApplicationProperties(final String path) {
        loadApplicationProperties(path, Utils.appsProperties);
    }
    
    public static Document readXmlFile(final String fileName) throws Exception {
        final SAXBuilder sxb = new SAXBuilder();
        return sxb.build(new File(fileName));
    }
    
    public static void saveXmlDocument(final String fileName, final Document xmlDoc) throws Exception {
        final XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        sortie.output(xmlDoc, new FileOutputStream(fileName));
    }
    
    public static JLabel createColoredLabel(final String string, final Font font, final Color color) {
        final JLabel templabel = new JLabel(string);
        templabel.setBackground(color);
        templabel.setFont(font);
        return templabel;
    }
    
    public static void fatalError(final String text, final int errorCode, final Exception ex) {
        Utils.logger.fatal(text);
        JOptionPane.showConfirmDialog(null, text, "Fatal (for this program) error - execution will stop.", 2, 0);
        ex.printStackTrace();
        System.exit(errorCode);
    }
    
    public static void loadModules(final String folder, final String basePackage) {
        final String fileSep = System.getProperty("file.separator");
        Utils.logger.info("Looking for analysis modules in: " + System.getProperty("user.dir") + fileSep + folder + fileSep);
        final File analysisToolFolder = new File("." + fileSep + folder + fileSep);
        if (analysisToolFolder != null) {
            final String[] fileList = analysisToolFolder.list();
            if (fileList != null) {
                for (int i = 0; i < fileList.length; ++i) {
                    try {
                        final String fileName = "file:" + System.getProperty("user.dir") + fileSep + folder + fileSep + fileList[i];
                        Utils.logger.debug("loading: " + fileName);
                        final JarClassLoader extLoader = new JarClassLoader(fileName);
                        final String className = String.valueOf(basePackage) + "." + fileList[i].substring(0, fileList[i].length() - 4);
                        Utils.logger.debug("loading class: " + className);
                        final Class tempClass = extLoader.findClass(className);
                        tempClass.newInstance();
                    }
                    catch (final Exception ex1) {
                        Utils.logger.error("Error while loading module: " + ex1.toString());
                        ex1.printStackTrace();
                    }
                }
            }
        }
        Utils.logger.info("Finished loading module.");
    }
    
    public static void loadModules(final String className) {
        try {
            Utils.logger.debug("loading class: " + className);
            final Class tempClass = Class.forName(className);
            tempClass.newInstance();
        }
        catch (final Exception ex1) {
            Utils.logger.error("Error while loading module: " + ex1.toString());
            ex1.printStackTrace();
        }
        Utils.logger.info("Finished loading module.");
    }
    
    public static Document readXmlFromNetwork(final String data) {
        final int begin = data.indexOf("<?");
        final int end = data.indexOf("[JessX-end]", begin);
        if (begin != -1 && end != -1) {
            final String message = data.substring(begin, end);
            final SAXBuilder sax = new SAXBuilder();
            try {
                return sax.build(new StringReader(message));
            }
            catch (final IOException ex2) {}
            catch (final JDOMException ex) {
                Utils.logger.error("Could not read message : " + message + ". Error: " + ex.toString());
            }
        }
        return null;
    }
    
    public static Vector sortVector(Vector vect) {
        int count = vect.size();
        Object obj;
        if (count > 0)
          for (int i = 0; i < count - 1; i++)
            for(int j = i + 1; j < count; j++)
              if (((String)vect.get(i)).compareTo((String)vect.get(j))>0) {
                obj = vect.get(i);
                vect.setElementAt(vect.get(j),i);
                vect.setElementAt(obj,j);
              }
        return vect;
      }

    public static Vector convertAndSortMapToVector(Map map) {

        Iterator iter = map.keySet().iterator();
        Vector keys = new Vector();
        while (iter.hasNext())
          keys.add(iter.next());

        int keysCount = keys.size();
        Object key;
        if (keysCount > 0)
          for (int i = 0; i < keysCount - 1; i++)
            for(int j = i + 1; j < keysCount; j++)
              if (((String)keys.get(i)).compareTo((String)keys.get(j))>0) {
                key = keys.get(i);
                keys.setElementAt(keys.get(j),i);
                keys.setElementAt(key,j);
              }
        return keys;
      }
}
