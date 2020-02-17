// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.analysis.gui;

import jessx.analysis.AnalysisTool;
import java.util.Enumeration;
import jessx.analysis.AnalysisToolNotCreatedException;
import jessx.analysis.AnalysisToolCreator;
import jessx.analysis.AnalysisToolsCore;
import java.awt.Frame;
import jessx.utils.gui.AbstractAboutBox;

public class AnalysisAboutBox extends AbstractAboutBox
{
    public AnalysisAboutBox(final Frame parent) {
        super(parent);
        this.Analysis_AboutBoxEnabled();
    }
    
    @Override
    public void loadModulesInfo() {
        AnalysisToolsCore.logger.debug("Initiating about box with available modules...");
        final Enumeration analysisToolsIterator = AnalysisToolCreator.analyseFactories.keys();
        this.getModulesInfoTextArea().append("In this Analyzer, you will find the following functions:\n\n");
        while (analysisToolsIterator.hasMoreElements()) {
            final String key = (String) analysisToolsIterator.nextElement();
            AnalysisTool tempTool = null;
            try {
                tempTool = AnalysisToolCreator.createTool(key);
                AnalysisToolsCore.logger.debug("Getting " + tempTool.getToolName() + " tool info and displaying it on interface.");
                this.getModulesInfoTextArea().append("- " + tempTool.getToolName() + "\nBy " + tempTool.getToolAuthor() + "\n" + tempTool.getToolDescription() + "\n\n");
                AnalysisToolsCore.logger.debug("following tool added to about box: " + tempTool.getToolName());
            }
            catch (AnalysisToolNotCreatedException ex1) {
                ex1.printStackTrace();
            }
        }
    }
}
