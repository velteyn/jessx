// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import java.awt.event.ActionEvent;
import java.io.File;
import javax.swing.JFileChooser;
import java.io.IOException;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import jessx.utils.Utils;
import org.jdom.Content;
import org.jdom.Element;
import org.jdom.Document;
import jessx.utils.FileChooserSave;
import java.util.Iterator;
import jessx.business.BusinessCore;
import javax.swing.JOptionPane;
import jessx.server.net.NetworkCore;
import java.awt.event.MouseEvent;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import jessx.utils.gui.JButtonRenderer;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.LayoutManager;
import javax.swing.border.TitledBorder;
import javax.swing.BorderFactory;
import java.awt.Color;
import javax.swing.table.TableModel;
import javax.swing.border.Border;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.GridBagLayout;
import javax.swing.JPanel;

public class ConnectionsPlayersServerGenericGui extends JPanel implements DisplayableNode
{
    JPanel jPanelPassword;
    JPanel jPanelList;
    GridBagLayout gridBagLayout;
    GridBagLayout gridBagLayout2;
    JScrollPane jScrollPane1;
    TableModelListOfParticipants tableModelParticipants;
    JTable jTable1;
    JButton jButtonSuppress;
    JButton jButtonAcquire;
    JButton jButtonSave;
    JButton jButtonLoad;
    JCheckBox jCheckBoxConnection;
    private JTextField jTextFieldPassword;
    private JCheckBox jCheckBoxPassword;
    JLabel jLabel1;
    Border border;
    Border borderList;
    Border border1;
    Border border2;
    Border border3;
    Border border4;
    Border border5;
    Border border6;
    Border border7;
    Border border8;
    Border border9;
    Border border10;
    
    public ConnectionsPlayersServerGenericGui() {
        this.jPanelPassword = new JPanel();
        this.jPanelList = new JPanel();
        this.gridBagLayout = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.jScrollPane1 = new JScrollPane();
        this.tableModelParticipants = new TableModelListOfParticipants();
        this.jTable1 = new JTable(this.tableModelParticipants);
        this.jButtonSuppress = new JButton();
        this.jButtonAcquire = new JButton();
        this.jButtonSave = new JButton();
        this.jButtonLoad = new JButton();
        this.jCheckBoxConnection = new JCheckBox();
        this.jTextFieldPassword = new JTextField("");
        this.jCheckBoxPassword = new JCheckBox();
        this.jLabel1 = new JLabel();
        this.border = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));
        this.borderList = new TitledBorder(this.border, "List of participants");
        this.border1 = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));
        this.border2 = new TitledBorder(this.border1, "Global password for the session");
        this.border3 = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));
        this.border4 = new TitledBorder(this.border3, "List of allowed participants");
        this.border5 = BorderFactory.createEmptyBorder();
        this.border6 = BorderFactory.createLineBorder(Color.white, 2);
        this.border7 = BorderFactory.createLineBorder(new Color(127, 157, 185), 2);
        this.border8 = BorderFactory.createLineBorder(new Color(127, 157, 185), 2);
        this.border9 = BorderFactory.createLineBorder(new Color(127, 157, 185), 1);
        this.border10 = BorderFactory.createEmptyBorder();
        try {
            this.jbInit();
        }
        catch (final Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.setLayout(this.gridBagLayout);
        this.jPanelList.setLayout(this.gridBagLayout2);
        this.jPanelPassword.setLayout(this.gridBagLayout);
        this.jPanelList.setBorder(this.border4);
        this.jPanelPassword.setBorder(this.border2);
        this.jButtonSuppress.setText("Suppress the list");
        this.jButtonAcquire.setText("Acquire a new list");
        this.jButtonLoad.setText("Load a list");
        this.jButtonSave.setText("Save the list");
        this.jButtonSuppress.addMouseListener(new CurrentPlayersServerGenericGui_jButtonSuppress_mouseAdapter(this));
        this.jButtonAcquire.addMouseListener(new CurrentPlayersServerGenericGui_jButtonAcquire_mouseAdapter(this));
        this.jButtonSave.addMouseListener(new CurrentPlayersServerGenericGui_jButton1_mouseAdapter(this));
        this.jButtonLoad.addMouseListener(new CurrentPlayersServerGenericGui_jButton2_mouseAdapter(this));
        this.jCheckBoxConnection.setText("Use the list to allow the connection of the clients");
        this.jCheckBoxConnection.addActionListener(new CurrentPlayersServerGenericGui_jCheckBox1_actionAdapter(this));
        this.jCheckBoxPassword.setText("Check the connections with this password");
        this.jCheckBoxPassword.addActionListener(new CurrentPlayersServerGenericGui_jCheckBoxPassword_actionAdapter(this));
        this.jCheckBoxPassword.setSelected(false);
        this.jLabel1.setText("Password for the session :");
        this.jTextFieldPassword.addActionListener(new CurrentPlayersServerGenericGui_jTextFieldPassword_actionAdapter(this));
        this.jScrollPane1.setBorder(this.border9);
        this.jTable1.setBorder(this.border10);
        this.jPanelPassword.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
        this.add(this.jPanelPassword, new GridBagConstraints(0, 0, 1, 1, 0.2, 0.0, 10, 1, new Insets(2, 5, 2, 5), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTable1);
        this.jPanelList.add(this.jButtonSuppress, new GridBagConstraints(3, 0, 1, 1, 0.25, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelList.add(this.jButtonLoad, new GridBagConstraints(2, 0, 1, 1, 0.25, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelList.add(this.jButtonSave, new GridBagConstraints(1, 0, 1, 1, 0.25, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelList.add(this.jButtonAcquire, new GridBagConstraints(0, 0, 1, 1, 0.25, 0.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelList.add(this.jCheckBoxConnection, new GridBagConstraints(0, 1, 4, 1, 0.0, 0.0, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
        this.add(this.jPanelList, new GridBagConstraints(0, 2, 1, 1, 1.0, 0.8, 10, 1, new Insets(2, 5, 2, 5), 0, 0));
        this.jPanelList.add(this.jScrollPane1, new GridBagConstraints(0, 3, 4, 1, 0.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jPanelPassword.add(this.jTextFieldPassword, new GridBagConstraints(1, 0, 1, 1, 1.0, 0.0, 10, 2, new Insets(5, 5, 5, 5), 0, 0));
        this.jPanelPassword.add(this.jCheckBoxPassword, new GridBagConstraints(0, 1, 2, 1, 0.0, 0.0, 17, 2, new Insets(5, 5, 5, 5), 0, 0));
        this.jTable1.getColumnModel().getColumn(2).setCellRenderer(new JButtonRenderer());
        this.jTable1.getColumnModel().getColumn(2).setCellEditor(new JButtonEdition(this.tableModelParticipants));
    }
    
    public void setEditable() {
    }
    
    public void setUneditable() {
    }
    
    @Override
    public String toString() {
        return "Connection players";
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void jButtonAcquire_mouseClicked(final MouseEvent e) {
        final Iterator iter = NetworkCore.getPlayerList().keySet().iterator();
        if (this.tableModelParticipants.getListParticipants().size() == 0 || JOptionPane.showConfirmDialog(null, "Do you really want to suppress the previous list?", "Warning", 2) == 0) {
            this.tableModelParticipants.removeAll();
            while (iter.hasNext()) {
                final String key = (String) iter.next();
                this.tableModelParticipants.addRow(key, NetworkCore.getPlayer(key).getPassword());
            }
        }
        BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
    }
    
    public void jButtonSuppress_mouseClicked(final MouseEvent e) {
        this.tableModelParticipants.removeAll();
        BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
    }
    
    public void jButton1_mouseClicked(final MouseEvent e) {
        new FileChooserSave(this.createParticipantsXmlDocument(), this, "JessX Server", "xml");
    }
    
    private Document createParticipantsXmlDocument() {
        final Document doc = new Document();
        final Element experiment = new Element("JessXParticipants");
        Iterator iter = tableModelParticipants.getListParticipants().iterator();
        while (iter.hasNext()) {
          String participantAndPassword[] = (String[]) iter.next();
            final Element player = new Element("player");
            player.setAttribute("Name", participantAndPassword[0]);
            player.setAttribute("Password", participantAndPassword[1]);
            experiment.addContent(player);
        }
        doc.setRootElement(experiment);
        return doc;
    }
    
    public void jButton2_mouseClicked(final MouseEvent e) {
        final JFileChooser chooser = Utils.newFileChooser(null, "", "xml files", "xml");
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        if (chooser.showOpenDialog(this) == 0) {
            final File file = chooser.getSelectedFile();
            if (!file.exists()) {
                JOptionPane.showMessageDialog(this, "No file selected.", "Error", 2);
                return;
            }
            if (!file.getName().endsWith("xml")) {
                JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
                return;
            }
            final SAXBuilder sax = new SAXBuilder();
            try {
                final Document xmlLog = sax.build(file);
                final Element root = xmlLog.getRootElement();
                final Iterator iter = root.getChildren("player").iterator();
                this.tableModelParticipants.removeAll();
                while (iter.hasNext()) {
                    final Element player = (Element) iter.next();
                    this.tableModelParticipants.addRow(player.getAttributeValue("Name"), player.getAttributeValue("Password"));
                }
                BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
            }
            catch (final JDOMException ex) {
                BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
                JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            }
            catch (final IOException ex2) {
                ex2.printStackTrace();
                JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
            }
        }
    }
    
    public void jCheckBox1_actionPerformed(final ActionEvent e) {
        BusinessCore.getScenario().setlistOfParticipantsUsed(this.jCheckBoxConnection.isSelected());
        if (this.jCheckBoxConnection.isSelected()) {
            BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
        }
    }
    
    public void jCheckBoxPassword_actionPerformed(final ActionEvent e) {
        BusinessCore.getScenario().setPasswordUsed(this.jCheckBoxPassword.isSelected());
        if (this.jCheckBoxPassword.isSelected()) {
            BusinessCore.getScenario().setPassword(this.jTextFieldPassword.getText());
        }
    }
    
    public void jTextFieldPassword_actionPerformed(final ActionEvent e) {
        BusinessCore.getScenario().setPassword(this.jTextFieldPassword.getText());
    }
}
