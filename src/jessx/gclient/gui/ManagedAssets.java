package jessx.gclient.gui;

import java.awt.Dimension;
import java.awt.Font; // Importa la classe Font
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import jessx.utils.Constants;
import jessx.utils.gui.JLabelRenderer;

public class ManagedAssets extends JPanel implements Constants {
    /**
     *
     */
    private static final long serialVersionUID = 6278989316181107908L;
    JScrollPane ScrollPaneManagedAssets;
    Border border1;
    PortfolioTableModel tableJTable2Model;
    GridBagLayout gridBagLayoutManagedAssets;
    JTable TableManagedAssets;

    public static final Font FONT_CLIENT_TITLE_BORDER = new Font("NomeFont", Font.BOLD, 12); // Inizializzazione statica

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
        this.ScrollPaneManagedAssets.setViewportView(this.TableManagedAssets);
        this.TableManagedAssets.setRowSelectionAllowed(false);
        this.TableManagedAssets.getColumnModel().getColumn(0).setCellRenderer(new JLabelRenderer());
        this.TableManagedAssets.getColumnModel().getColumn(1).setCellRenderer(new JLabelRenderer());
        this.TableManagedAssets.setRowHeight(18);
    }
}