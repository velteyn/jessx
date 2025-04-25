// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server;

import javax.swing.UIManager;
import org.apache.log4j.PropertyConfigurator;
import java.util.Properties;
import java.awt.Dimension;
import java.awt.Toolkit;
import org.jdom.Document;
import javax.swing.JFrame;
import jessx.utils.Utils;
import jessx.business.GeneralParameters;
import jessx.business.BusinessCore;
import jessx.server.gui.GeneralParameterSetupGui;
import jessx.server.net.NetworkCore;
import java.io.File;
import java.io.FileFilter;
import jessx.server.gui.GeneralServerFrame;

public class Server
{
    public static int EXPERIMENT_STATE_SETUP;
    public static int EXPERIMENT_STATE_RUNNING;
    public static int EXPERIMENT_STATE_ENDED;
    public static int SERVER_STATE_OFFLINE;
    public static int SERVER_STATE_ONLINE;
    boolean packFrame;
    private GeneralServerFrame frame;
    private static int experimentState;
    private static int serverState;
    private static final FileFilter filter;
    public static final String SERVER_LOG_FILE = "./server.log";
    
    static {
        Server.EXPERIMENT_STATE_SETUP = 0;
        Server.EXPERIMENT_STATE_RUNNING = 1;
        Server.EXPERIMENT_STATE_ENDED = 2;
        Server.SERVER_STATE_OFFLINE = 3;
        Server.SERVER_STATE_ONLINE = 4;
        Server.experimentState = 0;
        Server.serverState = 3;
        filter = new FileFilter() {
            public boolean accept(final File pathname) {
                if (pathname.isDirectory()) {
                    return true;
                }
                if (!pathname.isFile()) {
                    return false;
                }
                final String path = pathname.getAbsolutePath();
                return path.endsWith(".class") && path.indexOf(36) < 0;
            }
        };
    }
    
    public static int getExperimentState() {
        return Server.experimentState;
    }
    
    public static int getServerState() {
        return Server.serverState;
    }
    
    public static void setServerState(final int servState) {
        Server.serverState = servState;
        if (servState == Server.SERVER_STATE_ONLINE) {
            NetworkCore.setServerOnline();
        }
        else {
            NetworkCore.setServerOffline();
        }
    }
    
    public Server(final String scenarioFile, final boolean graphicalMode) {
        this.packFrame = true;
        BusinessCore.setGeneralParameters(new GeneralParameterSetupGui(graphicalMode));
        Server.experimentState = Server.EXPERIMENT_STATE_SETUP;
        Server.serverState = Server.SERVER_STATE_OFFLINE;
        this.loadServerProperties();
        this.loadJessXModules();
        if (graphicalMode) {
            this.buildFrame();
        }
        try {
            if (scenarioFile != "") {
                System.out.println("Il y a un fichier, chargement...");
                final Document xmlDoc = Utils.readXmlFile(scenarioFile);
                BusinessCore.loadFromXml(xmlDoc.getRootElement(), this.frame);
            }
        }
        catch (final Exception ex) {}
    }
    
    private void loadJessXModules() {
        Utils.logger.debug("Loading all available modules.");
        Utils.loadModules("jessx.business.operations.LimitOrder");
        Utils.loadModules("jessx.business.operations.DeleteOrder");
        Utils.loadModules("jessx.business.operations.MarketOrder");
        Utils.loadModules("jessx.business.operations.BestLimitOrder");
        Utils.loadModules("jessx.business.institutions.OrderMarket");
        Utils.loadModules("jessx.business.assets.Stock");
        Utils.logger.debug("All available modules loaded.");
    }
    
    private void buildFrame() {
        Utils.logger.info("Building frame...");
        this.frame = new GeneralServerFrame();
        if (this.packFrame) {
            this.frame.pack();
            Utils.logger.debug("Frame packed.");
        }
        else {
            this.frame.validate();
            Utils.logger.debug("Frame validated.");
        }
        Utils.logger.debug("Placing frame...");
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension frameSize = this.frame.getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        this.frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        Utils.logger.debug("Setting the frame visible.");
        this.frame.setVisible(true);
    }
    
    private void loadServerProperties() {
        Utils.logger.debug("Sets WaitingPort");
        Utils.SetApplicationProperties("ServerWaitingPort", "6290");
    }
    
    public void startServer() {
    }
    
    public static void InitLogs() {
        final File file = new File("./server.log");
        if (file.exists()) {
            file.delete();
        }
        final Properties log4jconf = new Properties();
        log4jconf.setProperty("log4j.rootCategory", "debug, stdout, R");
        log4jconf.setProperty("log4j.category.your.category.name", "DEBUG");
        log4jconf.setProperty("log4j.category.your.category.name", "INHERITED");
        log4jconf.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
        log4jconf.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
        log4jconf.setProperty("log4j.appender.stdout.layout.ConversionPattern", "%5p [%t] (%F:%L) - %m%n");
        log4jconf.setProperty("log4j.appender.R", "org.apache.log4j.RollingFileAppender");
        log4jconf.setProperty("log4j.appender.R.File", "./server.log");
        log4jconf.setProperty("log4j.appender.R.MaxFileSize", "500000KB");
        log4jconf.setProperty("log4j.appender.R.MaxBackupIndex", "1");
        log4jconf.setProperty("log4j.appender.R.layout", "org.apache.log4j.PatternLayout");
        log4jconf.setProperty("log4j.appender.R.layout.ConversionPattern", "%r [%p] %m  [%t] (%F:%L) \r\n");
        PropertyConfigurator.configure(log4jconf);
        Utils.logger.debug("Logging enabled. Starting logging...");
    }
    
    public static void main(final String[] args) {
        InitLogs();
        try {
            Utils.logger.debug("Getting and setting look and feel...");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (final Exception e) {
            Utils.logger.error("Error while loading look and feel: " + e.toString());
            e.printStackTrace();
        }
        Utils.logger.debug("Understanding Parameters");
        boolean graphicalMode = true;
        String xmlfile = "";
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == "textmode") {
                graphicalMode = false;
            }
            if (args[i].contains("xml")) {
                xmlfile = args[i];
            }
        }
        Utils.logger.debug("Creating core object.");
        new Server(xmlfile, graphicalMode);
    }
}
