// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.analysis;

import java.util.Hashtable;

public abstract class AnalysisToolCreator
{
    public static Hashtable analyseFactories;
    
    static {
        AnalysisToolCreator.analyseFactories = new Hashtable();
    }
    
    public static AnalysisTool createTool(final String name) throws AnalysisToolNotCreatedException {
        AnalysisToolsCore.logger.info("Trying to create the following tool: " + name);
        AnalysisToolsCore.logger.debug("Looking for its factory in the Hashtable.");
        Class analysisClass = (Class) AnalysisToolCreator.analyseFactories.get(name);
        if (analysisClass == null) {
            AnalysisToolsCore.logger.debug("Factory not found. The tool has never been loaded.");
            AnalysisToolsCore.logger.debug("As all tools from the AnalysisTools directory has already been loaded, we are looking in the classPath.");
            try {
                Class.forName("jessx.analysis.tools." + name);
                analysisClass = (Class) AnalysisToolCreator.analyseFactories.get(name);
                if (analysisClass == null) {
                    AnalysisToolsCore.logger.warn("Tools not found in the classPath.");
                    throw new AnalysisToolNotCreatedException();
                }
            }
            catch (ClassNotFoundException e) {
                AnalysisToolsCore.logger.warn("Tools not found in the classPath.");
                throw new AnalysisToolNotCreatedException();
            }
        }
        AnalysisToolsCore.logger.debug("Returning the result of the create method of the factory: the tool.");
        try {
            return (AnalysisTool) analysisClass.newInstance();
        }
        catch (IllegalAccessException ex) {
            AnalysisToolsCore.logger.error("error creating the requested tool: " + ex.toString());
            ex.printStackTrace();
            return null;
        }
        catch (InstantiationException ex2) {
            AnalysisToolsCore.logger.error("error creating the requested tool: " + ex2.toString());
            ex2.printStackTrace();
            return null;
        }
    }
}
