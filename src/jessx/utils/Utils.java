package jessx.utils;

import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public abstract class Utils implements Constants {
  public static Logger logger = Logger.getLogger(Utils.class.getName());
  
  public static Properties appsProperties = new Properties();
  
  public static void showErrorDialog(String message) {
    JOptionPane.showMessageDialog(null, message, "Error", 0);
  }
  
  public static JFileChooser newFileChooser(String directory, String selectedFile, String filterDescription, String filterExtension) {
    JFileChooser chooser;
    System.out.println("Creating file chooser...");
    if (directory != null) {
      chooser = new JFileChooser(directory);
    } else {
      chooser = new JFileChooser(System.getProperty("user.dir"));
    } 
    if (filterDescription != null && filterExtension != null) {
      FileFilter filter = new FileChooserFilter(filterDescription, filterExtension);
      chooser.addChoosableFileFilter(filter);
    } 
    if (selectedFile != null)
      chooser.setSelectedFile(new File(selectedFile)); 
    System.out.println("Sending back the chooser...");
    return chooser;
  }
  
  public static String getApplicationSettingsDirectory() {
    return null;
  }
  
  public static void SetApplicationProperties(String key, String value) {
    appsProperties.setProperty(key, value);
  }
  
  public static void loadApplicationProperties(String path, Properties prop) {
    FileInputStream in;
    logger.debug("Loading client properties from file: " + path);
    try {
      in = new FileInputStream(path);
    } catch (FileNotFoundException ex) {
      String errorMessage = "Error, missing file: " + path + "\nThis file should be included in the software distribution.";
      logger.fatal(errorMessage);
      JOptionPane.showMessageDialog(null, errorMessage, "error: missing file.", 0);
      ex.printStackTrace();
      System.exit(1);
      return;
    } 
    try {
      prop.load(in);
    } catch (IOException ex1) {
      String errorMessage = "Error, could not read file: " + path + "\nYou should have read access to this file.";
      logger.fatal(errorMessage);
      JOptionPane.showMessageDialog(null, errorMessage, "error: could not read file.", 0);
      ex1.printStackTrace();
      System.exit(1);
      return;
    } 
    try {
      in.close();
    } catch (IOException ex2) {
      String errorMessage = "Error, could not close file: " + path + "\nApparently reading successful. [IGNORED]";
      logger.error(errorMessage);
    } 
  }
  
  public static void loadApplicationProperties(String path) {
    loadApplicationProperties(path, appsProperties);
  }
  
  public static Document readXmlFile(String fileName) throws Exception {
    SAXBuilder sxb = new SAXBuilder();
    return sxb.build(new File(fileName));
  }
  
  public static void saveXmlDocument(String fileName, Document xmlDoc) throws Exception {
    XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
    sortie.output(xmlDoc, new FileOutputStream(fileName));
  }
  
  public static JLabel createColoredLabel(String string, Font font, Color color) {
    JLabel templabel = new JLabel(string);
    templabel.setBackground(color);
    templabel.setFont(font);
    return templabel;
  }
  
  public static void fatalError(String text, int errorCode, Exception ex) {
    logger.fatal(text);
    JOptionPane.showConfirmDialog(null, text, "Fatal (for this program) error - execution will stop.", 2, 0);
    ex.printStackTrace();
    System.exit(errorCode);
  }
  
  public static void loadModules(String folder, String basePackage) {
    String fileSep = System.getProperty("file.separator");
    logger.info("Looking for analysis modules in: " + System.getProperty("user.dir") + fileSep + folder + fileSep);
    File analysisToolFolder = new File("." + fileSep + folder + fileSep);
    if (analysisToolFolder != null) {
      String[] fileList = analysisToolFolder.list();
      if (fileList != null)
        for (int i = 0; i < fileList.length; i++) {
          try {
            String fileName = "file:" + System.getProperty("user.dir") + fileSep + 
              folder + fileSep + fileList[i];
            logger.debug("loading: " + fileName);
            JarClassLoader extLoader = new JarClassLoader(fileName);
            String className = String.valueOf(basePackage) + "." + fileList[i].substring(0, fileList[i].length() - 4);
            logger.debug("loading class: " + className);
            Class tempClass = extLoader.findClass(className);
            tempClass.newInstance();
          } catch (Exception ex1) {
            logger.error("Error while loading module: " + ex1.toString());
            ex1.printStackTrace();
          } 
        }  
    } 
    logger.info("Finished loading module.");
  }
  
  public static void loadModules(String className) {
    try {
      logger.debug("loading class: " + className);
      Class<?> tempClass = Class.forName(className);
      tempClass.newInstance();
    } catch (Exception ex1) {
      logger.error("Error while loading module: " + ex1.toString());
      ex1.printStackTrace();
    } 
    logger.info("Finished loading module.");
  }
  
  public static Document readXmlFromNetwork(String data) {
    int begin = data.indexOf("<?");
    int end = data.indexOf("[JessX-end]", begin);
    if (begin != -1 && end != -1) {
      String message = data.substring(begin, end);
      SAXBuilder sax = new SAXBuilder();
      try {
        return sax.build(new StringReader(message));
      } catch (IOException iOException) {
      
      } catch (JDOMException ex) {
        logger.error("Could not read message : " + message + ". Error: " + 
            ex.toString());
      } 
    } 
    return null;
  }
  
  public static Vector sortVector(Vector<String> vect) {
    int count = vect.size();
    if (count > 0)
      for (int i = 0; i < count - 1; i++) {
        for (int j = i + 1; j < count; j++) {
          if (((String)vect.get(i)).compareTo(vect.get(j)) > 0) {
            Object obj = vect.get(i);
            vect.setElementAt(vect.get(j), i);
            vect.setElementAt((String) obj, j);
          } 
        } 
      }  
    return vect;
  }
  
  public static Vector convertAndSortMapToVector(Map map) {
    Iterator iter = map.keySet().iterator();
    Vector<String> keys = new Vector();
    while (iter.hasNext())
      keys.add((String) iter.next()); 
    int keysCount = keys.size();
    if (keysCount > 0)
      for (int i = 0; i < keysCount - 1; i++) {
        for (int j = i + 1; j < keysCount; j++) {
          if (((String)keys.get(i)).compareTo(keys.get(j)) > 0) {
            Object key = keys.get(i);
            keys.setElementAt(keys.get(j), i);
            keys.setElementAt((String) key, j);
          } 
        } 
      }  
    return keys;
  }
}
