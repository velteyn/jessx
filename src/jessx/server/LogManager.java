// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.server;

import java.awt.Component;
import javax.swing.JOptionPane;
import jessx.utils.Utils;
import jessx.server.net.NetworkCore;
import org.jdom.Content;
import jessx.business.BusinessCore;
import org.jdom.Element;
import org.jdom.Document;
import jessx.server.net.event.ExperimentStateListener;

public class LogManager implements ExperimentStateListener
{
    private Document experimentDoc;
    private Element currentPeriodNode;
    private Element experimentLogNode;
    
    private void initLogging() {
        this.experimentLogNode = new Element("Experiment");
        final Element setupNode = new Element("Setup");
        BusinessCore.saveToXml(setupNode);
        this.experimentLogNode.addContent(setupNode);
        this.experimentDoc = new Document(this.experimentLogNode);
        (this.currentPeriodNode = new Element("Period")).setAttribute("num", "0");
        this.experimentLogNode.addContent(this.currentPeriodNode);
    }
    
    private void changeOfPeriod() {
        if (Integer.parseInt(this.currentPeriodNode.getAttributeValue("num")) != NetworkCore.getExperimentManager().getPeriodNum()) {
            (this.currentPeriodNode = new Element("Period")).setAttribute("num", Integer.toString(NetworkCore.getExperimentManager().getPeriodNum()));
            this.experimentLogNode.addContent(this.currentPeriodNode);
        }
    }
    
    public void log(final Element toLog) {
        this.currentPeriodNode.addContent(toLog);
    }
    
    public void logPortfoliosendOfPeriod(final Element portfolios) {
        this.experimentLogNode.addContent(portfolios);
    }
    
    public LogManager() {
        NetworkCore.getExperimentManager().addExperimentStateListener(this);
    }
    
    public void saveLogToFile() {
        String pwd = BusinessCore.getGeneralParameters().getWorkingDirectory();
        String fileName = BusinessCore.getGeneralParameters().getLoggingFileName();
        if (pwd == null || pwd.equalsIgnoreCase("") || fileName == null || fileName.equalsIgnoreCase("")) {
            Utils.logger.warn("The logging fileName or the working directory are not defined. [logging to log.xml]");
            return;
        }
        if (!pwd.substring(pwd.length() - 1).equals(System.getProperty("file.separator"))) {
            pwd = String.valueOf(pwd) + System.getProperty("file.separator");
        }
        if (fileName.length() < 5 || !fileName.substring(fileName.length() - 4).equalsIgnoreCase(".xml")) {
            fileName = String.valueOf(fileName) + ".xml";
        }
        try {
            Utils.logger.debug("Saving xml setup file to : " + pwd + fileName);
            Utils.saveXmlDocument(String.valueOf(pwd) + fileName, this.experimentDoc);
        }
        catch (Exception ex) {
            Utils.logger.error("Error while saving xml document. " + ex.toString());
            JOptionPane.showConfirmDialog(null, "Error occured during writing of the xml document:\n" + ex.toString(), "Error: unable to write xml document", 2, 0);
        }
    }
    
    public void experimentStateChanged(final int newState) {
        if (newState != 0) {
            if (this.experimentLogNode == null || NetworkCore.getExperimentManager().getPeriodNum() == -1) {
                this.initLogging();
            }
            if (NetworkCore.getExperimentManager().getPeriodNum() != -1) {
                this.changeOfPeriod();
            }
        }
    }
}
