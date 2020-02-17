package jessx.server.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import jessx.business.BusinessCore;
import jessx.server.net.NetworkCore;
import jessx.utils.Utils;
import jessx.utils.gui.JButtonRenderer;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class ConnectionsPlayersServerGenericGui extends JPanel implements DisplayableNode {
	JPanel jPanelPassword = new JPanel();

	JPanel jPanelList = new JPanel();

	GridBagLayout gridBagLayout = new GridBagLayout();

	GridBagLayout gridBagLayout2 = new GridBagLayout();

	JScrollPane jScrollPane1 = new JScrollPane();

	TableModelListOfParticipants tableModelParticipants = new TableModelListOfParticipants();

	JTable jTable1 = new JTable(this.tableModelParticipants);

	JButton jButtonSuppress = new JButton();

	JButton jButtonAcquire = new JButton();

	JButton jButtonSave = new JButton();

	JButton jButtonLoad = new JButton();

	JCheckBox jCheckBoxConnection = new JCheckBox();

	private JTextField jTextFieldPassword = new JTextField("");

	private JCheckBox jCheckBoxPassword = new JCheckBox();

	JLabel jLabel1 = new JLabel();

	Border border = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));

	Border borderList = new TitledBorder(this.border, "List of participants");

	Border border1 = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));

	Border border2 = new TitledBorder(this.border1, "Global password for the session");

	Border border3 = BorderFactory.createEtchedBorder(Color.white, new Color(156, 156, 158));

	Border border4 = new TitledBorder(this.border3, "List of allowed participants");

	Border border5 = BorderFactory.createEmptyBorder();

	Border border6 = BorderFactory.createLineBorder(Color.white, 2);

	Border border7 = BorderFactory.createLineBorder(new Color(127, 157, 185), 2);

	Border border8 = BorderFactory.createLineBorder(new Color(127, 157, 185), 2);

	Border border9 = BorderFactory.createLineBorder(new Color(127, 157, 185), 1);

	Border border10 = BorderFactory.createEmptyBorder();

	public ConnectionsPlayersServerGenericGui() {
		try {
			jbInit();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		setLayout(this.gridBagLayout);
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
		this.jPanelPassword.add(this.jLabel1, new GridBagConstraints(0, 0, 1, 1, 0.0D, 0.0D, 17, 0, new Insets(5, 5, 5, 5), 0, 0));
		add(this.jPanelPassword, new GridBagConstraints(0, 0, 1, 1, 0.2D, 0.0D, 10, 1, new Insets(2, 5, 2, 5), 0, 0));
		this.jScrollPane1.getViewport().add(this.jTable1);
		this.jPanelList.add(this.jButtonSuppress, new GridBagConstraints(3, 0, 1, 1, 0.25D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanelList.add(this.jButtonLoad, new GridBagConstraints(2, 0, 1, 1, 0.25D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanelList.add(this.jButtonSave, new GridBagConstraints(1, 0, 1, 1, 0.25D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanelList.add(this.jButtonAcquire, new GridBagConstraints(0, 0, 1, 1, 0.25D, 0.0D, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanelList.add(this.jCheckBoxConnection, new GridBagConstraints(0, 1, 4, 1, 0.0D, 0.0D, 10, 2, new Insets(0, 0, 0, 0), 0, 0));
		add(this.jPanelList, new GridBagConstraints(0, 2, 1, 1, 1.0D, 0.8D, 10, 1, new Insets(2, 5, 2, 5), 0, 0));
		this.jPanelList.add(this.jScrollPane1, new GridBagConstraints(0, 3, 4, 1, 0.0D, 1.0D, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
		this.jPanelPassword.add(this.jTextFieldPassword, new GridBagConstraints(1, 0, 1, 1, 1.0D, 0.0D, 10, 2, new Insets(5, 5, 5, 5), 0, 0));
		this.jPanelPassword.add(this.jCheckBoxPassword, new GridBagConstraints(0, 1, 2, 1, 0.0D, 0.0D, 17, 2, new Insets(5, 5, 5, 5), 0, 0));
		this.jTable1.getColumnModel().getColumn(2).setCellRenderer((TableCellRenderer) new JButtonRenderer());
		this.jTable1.getColumnModel().getColumn(2).setCellEditor(new JButtonEdition(this.tableModelParticipants));
	}

	public void setEditable() {
	}

	public void setUneditable() {
	}

	public String toString() {
		return "Connection players";
	}

	public JPanel getPanel() {
		return this;
	}

	public void jButtonAcquire_mouseClicked(MouseEvent e) {
		Iterator<String> iter = NetworkCore.getPlayerList().keySet().iterator();
		if (this.tableModelParticipants.getListParticipants().size() == 0 || JOptionPane.showConfirmDialog(null, "Do you really want to suppress the previous list?", "Warning", 2) == 0) {
			this.tableModelParticipants.removeAll();
			while (iter.hasNext()) {
				String key = iter.next();
				this.tableModelParticipants.addRow(key, NetworkCore.getPlayer(key).getPassword());
			}
		}
		BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
	}

	public void jButtonSuppress_mouseClicked(MouseEvent e) {
		this.tableModelParticipants.removeAll();
		BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
	}

	public void jButton1_mouseClicked(MouseEvent e) {
	}

	private Document createParticipantsXmlDocument() {
		Document doc = new Document();
		Element experiment = new Element("JessXParticipants");
		Iterator<String[]> iter = this.tableModelParticipants.getListParticipants().iterator();
		while (iter.hasNext()) {
			String[] participantAndPassword = iter.next();
			Element player = new Element("player");
			player.setAttribute("Name", participantAndPassword[0]);
			player.setAttribute("Password", participantAndPassword[1]);
			experiment.addContent((Content) player);
		}
		doc.setRootElement(experiment);
		return doc;
	}

	public void jButton2_mouseClicked(MouseEvent e) {
		JFileChooser chooser = Utils.newFileChooser(null, "", "xml files", "xml");
		chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
		if (chooser.showOpenDialog(this) == 0) {
			File file = chooser.getSelectedFile();
			if (!file.exists()) {
				JOptionPane.showMessageDialog(this, "No file selected.", "Error", 2);
				return;
			}
			if (!file.getName().endsWith("xml")) {
				JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
				return;
			}
			SAXBuilder sax = new SAXBuilder();
			try {
				Document xmlLog = sax.build(file);
				Element root = xmlLog.getRootElement();
				Iterator<Element> iter = root.getChildren("player").iterator();
				this.tableModelParticipants.removeAll();
				while (iter.hasNext()) {
					Element player = iter.next();
					this.tableModelParticipants.addRow(player.getAttributeValue("Name"), player.getAttributeValue("Password"));
				}
				BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
			} catch (JDOMException ex) {
				BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
				JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
				return;
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(this, "The file you choose is incorrect.", "Error", 2);
				return;
			}
		}
	}

	public void jCheckBox1_actionPerformed(ActionEvent e) {
		BusinessCore.getScenario().setlistOfParticipantsUsed(this.jCheckBoxConnection.isSelected());
		if (this.jCheckBoxConnection.isSelected())
			BusinessCore.getScenario().setlistOfParticipants(this.tableModelParticipants.getListParticipants());
	}

	public void jCheckBoxPassword_actionPerformed(ActionEvent e) {
		BusinessCore.getScenario().setPasswordUsed(this.jCheckBoxPassword.isSelected());
		if (this.jCheckBoxPassword.isSelected())
			BusinessCore.getScenario().setPassword(this.jTextFieldPassword.getText());
	}

	public void jTextFieldPassword_actionPerformed(ActionEvent e) {
		BusinessCore.getScenario().setPassword(this.jTextFieldPassword.getText());
	}
}
