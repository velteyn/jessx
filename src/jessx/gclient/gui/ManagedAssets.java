// 
// Decompiled by Procyon v0.6.0
// 

package jessx.gclient.gui;

import javax.swing.table.TableCellRenderer;
import jessx.utils.gui.JLabelRenderer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import javax.swing.table.TableModel;
import javax.swing.JTable;
import java.awt.GridBagLayout;
import javax.swing.border.Border;
import javax.swing.JScrollPane;
import jessx.utils.Constants;
import javax.swing.JPanel;

public class ManagedAssets extends JPanel implements Constants
{
    JScrollPane ScrollPaneManagedAssets;
    Border border1;
    PortfolioTableModel tableJTable2Model;
    GridBagLayout gridBagLayoutManagedAssets;
    JTable TableManagedAssets;
    
    public ManagedAssets() {
        this.ScrollPaneManagedAssets = new JScrollPane();
        this.tableJTable2Model = new PortfolioTableModel(new String[] { "Asset Name", " Quantity" });
        this.gridBagLayoutManagedAssets = new GridBagLayout();
        this.TableManagedAssets = new JTable(this.tableJTable2Model);
        this.jbInit();
    }
    
    public void jbInit() {
        this.border1 = new TitledBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6), "My Portfolio", 0, 0, ManagedAssets.FONT_CLIENT_TITLE_BORDER);
        this.setEnabled(true);
        this.setBorder(this.border1);
        this.setMinimumSize(new Dimension(50, 110));
        this.setOpaque(true);
        this.setPreferredSize(new Dimension(200, 110));
        this.setLayout(this.gridBagLayoutManagedAssets);
        this.add(this.ScrollPaneManagedAssets, new GridBagConstraints(0, 0, 1, 1, 1.0, 0.5, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.ScrollPaneManagedAssets.setHorizontalScrollBarPolicy(30);
        this.ScrollPaneManagedAssets.setAutoscrolls(false);
        this.ScrollPaneManagedAssets.setRequestFocusEnabled(true);
        this.ScrollPaneManagedAssets.getViewport().add(this.TableManagedAssets, null);
        this.TableManagedAssets.setRowSelectionAllowed(false);
        this.TableManagedAssets.getColumnModel().getColumn(0).setCellRenderer(new JLabelRenderer());
        this.TableManagedAssets.getColumnModel().getColumn(1).setCellRenderer(new JLabelRenderer());
        this.TableManagedAssets.setRowHeight(18);
    }
}
