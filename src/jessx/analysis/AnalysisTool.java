// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.analysis;

import javax.swing.JPanel;
import org.jdom.Document;

public interface AnalysisTool
{
    String getToolName();
    
    String getToolAuthor();
    
    String getToolDescription();
    
    void setDocument(final Document p0);
    
    JPanel drawGraph();
}
