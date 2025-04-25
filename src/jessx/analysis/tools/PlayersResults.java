// 
// Decompiled by Procyon v0.6.0
// 

package jessx.analysis.tools;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import jessx.utils.Utils;
import java.awt.LayoutManager;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.JTabbedPane;
import jessx.analysis.AnalysisToolCreator;
import org.jdom.Document;
import jessx.analysis.AnalysisTool;
import javax.swing.JPanel;

public class PlayersResults extends JPanel implements AnalysisTool
{
    private Document doc;
    
    static {
        try {
            AnalysisToolCreator.analyseFactories.put("PlayersResults", Class.forName("jessx.analysis.tools.PlayersResults"));
            System.out.println("Players Results Analysis registered");
        }
        catch (final Exception ex) {
            System.out.println("Problem registering PlayersResults tool: " + ex.toString());
        }
    }
    
    public String getToolName() {
        return "Players' results";
    }
    
    public String getToolAuthor() {
        return "EC-Lille, USTL - 2005 - Christophe Grosjean.";
    }
    
    public String getToolDescription() {
        return "An analysis that creates a summary of the players's results.";
    }
    
    public void setDocument(final Document xmlDoc) {
        this.doc = xmlDoc;
    }
    
    public JPanel drawGraph() {
        final JPanel returnPanel = new JPanel();
        final JTabbedPane jTabbedPane = new JTabbedPane();
        final JPanel jresults = new JPanel();
        final JPanel jClassification = new JPanel();
        jTabbedPane.add(jClassification, "Classification");
        jTabbedPane.add(jresults, "Results");
        final JScrollPane jScrollPaneResults = new JScrollPane();
        final JScrollPane jScrollPaneClassification = new JScrollPane();
        final GridBagLayout gridBagLayoutResults = new GridBagLayout();
        final GridBagLayout gridBagLayoutClassification = new GridBagLayout();
        jresults.setLayout(gridBagLayoutResults);
        jClassification.setLayout(gridBagLayoutClassification);
        Utils.logger.debug("TableModelPlayersResults try to create");
        final TableModelPlayersResults tableModelPlayersResults = new TableModelPlayersResults(this.doc);
        final JTable jTabResults = new JTable(tableModelPlayersResults);
        final JTable jTabClassification = new JTable(new TableModelPlayersClassification(this.doc, tableModelPlayersResults));
        Utils.logger.debug("TableModelPlayersResults created");
        jScrollPaneResults.add(jTabResults, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        jresults.add(jScrollPaneResults, new GridBagConstraints(0, 0, 0, 0, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        jScrollPaneResults.getViewport().add(jTabResults, null);
        jScrollPaneClassification.add(jTabClassification, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        jClassification.add(jScrollPaneClassification, new GridBagConstraints(0, 0, 0, 0, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        jScrollPaneClassification.getViewport().add(jTabClassification, null);
        returnPanel.setLayout(new GridBagLayout());
        returnPanel.add(jTabbedPane, new GridBagConstraints(1, 1, 1, 1, 1.0, 1.0, 10, 1, new Insets(4, 4, 4, 4), 0, 0));
        return returnPanel;
    }
}
