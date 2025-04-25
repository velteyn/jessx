// 
// Decompiled by Procyon v0.6.0
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
