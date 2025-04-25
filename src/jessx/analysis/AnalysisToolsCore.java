// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis;

import javax.swing.UIManager;
import org.apache.log4j.PropertyConfigurator;
import jessx.utils.JarClassLoader;
import java.io.File;
import java.awt.Dimension;
import java.awt.Toolkit;
import jessx.analysis.gui.CoreFrame;
import jessx.analysis.gui.AnalysisStartingPopup;
import org.apache.log4j.Logger;

public class AnalysisToolsCore
{
    boolean packFrame;
    public static Logger logger;
    private AnalysisStartingPopup popupFrame;
    
    static {
        AnalysisToolsCore.logger = Logger.getLogger(AnalysisToolsCore.class.getName());
    }
    
    public AnalysisToolsCore() {
        this.packFrame = false;
        AnalysisToolsCore.logger.info("Core initialisation...");
        this.showStartingPopup();
        this.loadModules();
        AnalysisToolsCore.logger.debug("Building frame...");
        final CoreFrame frame = new CoreFrame();
        if (this.packFrame) {
            frame.pack();
            AnalysisToolsCore.logger.debug("frame packed.");
        }
        else {
            frame.validate();
            AnalysisToolsCore.logger.debug("frame validated.");
        }
        AnalysisToolsCore.logger.debug("Placing the window at the center of the screen.");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        frame.setVisible(true);
        this.popupFrame.setVisible(false);
    }
    
    public void showStartingPopup() {
        (this.popupFrame = new AnalysisStartingPopup()).pack();
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = this.popupFrame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.popupFrame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        this.popupFrame.setVisible(true);
    }
    
    public void loadModules() {
        final String fileSep = System.getProperty("file.separator");
        AnalysisToolsCore.logger.info("Looking for analysis modules in: " + System.getProperty("user.dir") + fileSep + "AnalysisTools" + fileSep);
        final File analysisToolFolder = new File("." + fileSep + "AnalysisTools" + fileSep);
        if (analysisToolFolder != null) {
            final String[] fileList = analysisToolFolder.list();
            if (fileList != null) {
                for (int i = 0; i < fileList.length; ++i) {
                    try {
                        final String fileName = "file:" + System.getProperty("user.dir") + fileSep + "AnalysisTools" + fileSep + fileList[i];
                        if (fileList[i].endsWith(".jar")) {
                            AnalysisToolsCore.logger.debug("good file .jar");
                            AnalysisToolsCore.logger.debug("loading: " + fileName);
                            final JarClassLoader extLoader = new JarClassLoader(fileName);
                            final String className = "jessx.analysis.tools." + fileList[i].substring(0, fileList[i].length() - 4);
                            AnalysisToolsCore.logger.debug("loading class: " + className);
                            final Class tempClass = extLoader.findClass(className);
                            tempClass.newInstance();
                        }
                        else {
                            AnalysisToolsCore.logger.debug("bad file not .jar :" + fileList[i]);
                        }
                    }
                    catch (final Exception ex1) {
                        AnalysisToolsCore.logger.debug("Error while loading analysis module: " + ex1.toString());
                        ex1.printStackTrace();
                    }
                }
            }
        }
        AnalysisToolsCore.logger.info("Finished loading modules.");
    }
    
    public static void main(final String[] args) throws ClassNotFoundException {
        PropertyConfigurator.configure("log4j.analysis.properties");
        AnalysisToolsCore.logger.info("Analyzer launched...");
        AnalysisToolsCore.logger.debug("Logging parameters read.");
        try {
            AnalysisToolsCore.logger.debug("Getting look and feel...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e) {
            AnalysisToolsCore.logger.fatal("Error: " + e.toString());
            e.printStackTrace();
        }
        AnalysisToolsCore.logger.debug(" done.");
        AnalysisToolsCore.logger.debug("Creating the core object...");
        new AnalysisToolsCore();
    }
}
