// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils.gui;

import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.io.IOException;
import jessx.utils.Utils;
import javax.swing.JEditorPane;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.Icon;
import java.net.URL;
import java.awt.Component;
import java.awt.LayoutManager;
import java.awt.Dimension;
import javax.swing.border.Border;
import java.awt.Frame;
import java.awt.GridBagLayout;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import jessx.utils.Constants;
import java.awt.event.ActionListener;
import javax.swing.JDialog;

public abstract class AbstractAboutBox extends JDialog implements ActionListener, Constants
{
    JTabbedPane jTabbedPane1;
    JPanel panelTeam;
    JPanel panelVersion;
    JPanel panelLicence;
    JPanel panelModulesInfo;
    ImageIcon logoJessX;
    ImageIcon logoECLille;
    ImageIcon logoUSTL;
    ImageIcon logoEcoxp;
    ImageIcon logoTrading;
    ImageIcon logoMarket;
    ImageIcon logoTrobot;
    JTextField jTextFieldProductName;
    JTextField jTextFieldVersion;
    JTextField jTextFieldDate;
    JTextField jTextFieldRessources;
    JLabel jLabelTeam;
    JLabel jLabelLogoEcoXP;
    JLabel jLabelLogoEcLille;
    JLabel jLabelLogoTrading;
    JLabel jLabelLogoMarket;
    JLabel jLabelJessXLogo;
    JLabel jLabelLogoUSTL;
    JLabel jLabelLogoTrobot;
    JTextArea jTextAreaModulesAnalysis;
    JScrollPane jScrollPane1;
    JScrollPane jScrollPane2;
    GridBagLayout gridBagLayout1;
    GridBagLayout gridBagLayout2;
    GridBagLayout gridBagLayout3;
    GridBagLayout gridBagLayout4;
    String fileSeparator;
    
    public AbstractAboutBox(final Frame parent) {
        super(parent);
        this.jTabbedPane1 = new JTabbedPane();
        this.panelTeam = new JPanel();
        this.panelVersion = new JPanel();
        this.panelLicence = new JPanel();
        this.panelModulesInfo = new JPanel();
        this.logoJessX = new ImageIcon();
        this.logoECLille = new ImageIcon();
        this.logoUSTL = new ImageIcon();
        this.logoEcoxp = new ImageIcon();
        this.logoTrading = new ImageIcon();
        this.logoMarket = new ImageIcon();
        this.logoTrobot = new ImageIcon();
        this.jTextFieldProductName = new JTextField("JessX: java Experimental Simulated Stock Exchange");
        this.jTextFieldVersion = new JTextField("Version : 1.6");
        this.jTextFieldDate = new JTextField("Released on May 2008");
        this.jTextFieldRessources = new JTextField("Ressources and tutorials at www.jessx.net");
        this.jLabelTeam = new JLabel();
        this.jLabelLogoEcoXP = new JLabel();
        this.jLabelLogoEcLille = new JLabel();
        this.jLabelLogoTrading = new JLabel();
        this.jLabelLogoMarket = new JLabel();
        this.jLabelJessXLogo = new JLabel();
        this.jLabelLogoUSTL = new JLabel();
        this.jLabelLogoTrobot = new JLabel();
        this.jTextAreaModulesAnalysis = new JTextArea();
        this.jScrollPane2 = new JScrollPane();
        this.gridBagLayout1 = new GridBagLayout();
        this.gridBagLayout2 = new GridBagLayout();
        this.gridBagLayout3 = new GridBagLayout();
        this.gridBagLayout4 = new GridBagLayout();
        this.fileSeparator = System.getProperty("file.separator");
        this.enableEvents(64L);
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private void jbInit() throws Exception {
        this.jTextFieldProductName.setBorder(null);
        this.jTextFieldProductName.setOpaque(false);
        this.jTextFieldProductName.setEditable(false);
        this.jTextFieldProductName.setHorizontalAlignment(0);
        this.jTextFieldVersion.setBorder(null);
        this.jTextFieldVersion.setOpaque(false);
        this.jTextFieldVersion.setEditable(false);
        this.jTextFieldVersion.setHorizontalAlignment(0);
        this.jTextFieldDate.setBorder(null);
        this.jTextFieldDate.setOpaque(false);
        this.jTextFieldDate.setEditable(false);
        this.jTextFieldDate.setHorizontalAlignment(0);
        this.jTextFieldRessources.setBorder(null);
        this.jTextFieldRessources.setOpaque(false);
        this.jTextFieldRessources.setEditable(false);
        this.jTextFieldRessources.setHorizontalAlignment(0);
        this.jTabbedPane1.setMinimumSize(new Dimension(560, 580));
        this.jTabbedPane1.setPreferredSize(new Dimension(560, 580));
        this.panelLicence.setLayout(this.gridBagLayout3);
        this.panelVersion.setLayout(this.gridBagLayout4);
        this.jScrollPane2.getViewport().add(this.getLicense());
        final String imagesDir = String.valueOf(System.getProperty("user.dir")) + this.fileSeparator + "images" + this.fileSeparator;
        this.logoJessX = new ImageIcon(new URL("file:" + imagesDir + "logo_JessX_small.PNG"));
        this.logoECLille = new ImageIcon(new URL("file:" + imagesDir + "ecllogo_small.PNG"));
        this.logoUSTL = new ImageIcon(new URL("file:" + imagesDir + "ustl_small.PNG"));
        this.logoEcoxp = new ImageIcon(new URL("file:" + imagesDir + "ecoxp_small.PNG"));
        this.logoTrading = new ImageIcon(new URL("file:" + imagesDir + "logo_trading.PNG"));
        this.logoMarket = new ImageIcon(new URL("file:" + imagesDir + "market_logo.PNG"));
        this.logoTrobot = new ImageIcon(new URL("file:" + imagesDir + "Trobot_logo.JPG"));
        this.setTitle("About JessX");
        this.getContentPane().setLayout(this.gridBagLayout2);
        this.panelTeam.setLayout(this.gridBagLayout1);
        this.jLabelJessXLogo.setIcon(this.logoJessX);
        this.jLabelTeam.setText("<html><body><p><h1>Researchers</h1>Olivier BRANDOUY, R&eacute;mi BACHELET</p><p><h1>Development Teams</h1><u>ECOXP Team (2003 - 2005)</u><br>Thierry CURTIL (also June 2005 - September 2005), Imad DAOUDI, David KISSENBERGER<br>Franck LASRY, Franck PEREZ, Julien TERRIER<br></p><p><u>TRADING Team (2004 - 2006)</u><br>Christophe GROSJEAN, Mohamed HAMAMOUCHI, Charles MONTEZ<br>Cl&eacute;ment PLAIGNAUD, J&eacute;r&eacute;my STREQUE, Tian XIA<br></p><p><u>MARKET Team (2005 - 2007)</u><br>Etienne BROUTIN, Christophe BURLETT, Roger ISS<br>Benoit LEBEGUE, R&eacute;mi QUILLIET, Guillaume TROMP<br></p><p><u>TROBOT Team (2006 - 2008)</u><br>Julien CHAPLET, Ga&euml;l DENIAU, Felipe DE ALMEIDA GATTASS, Paul GAILLARD, S&eacute;bastien GOMES, Beno&icirc;t PARIS</p><br></body></html>");
        this.jLabelTeam.setVerticalAlignment(1);
        this.jLabelTeam.setHorizontalAlignment(0);
        this.jLabelLogoEcLille.setIcon(this.logoECLille);
        this.jLabelLogoUSTL.setIcon(this.logoUSTL);
        this.jLabelLogoEcoXP.setIcon(this.logoEcoxp);
        this.jLabelLogoTrading.setIcon(this.logoTrading);
        this.jLabelLogoMarket.setIcon(this.logoMarket);
        this.jLabelLogoTrobot.setIcon(this.logoTrobot);
        this.getContentPane().add(this.jTabbedPane1, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.jTabbedPane1.add(this.panelVersion, "Version");
        this.jTabbedPane1.add(this.panelTeam, "Team");
        this.jTabbedPane1.add(this.panelLicence, "License");
        this.panelLicence.add(this.jScrollPane2, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, 10, 1, new Insets(0, 0, 0, 0), 0, 0));
        this.panelVersion.add(this.jTextFieldRessources, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 0, 5, 0), 0, 0));
        this.panelVersion.add(this.jTextFieldDate, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 0, 5, 0), 0, 0));
        this.panelVersion.add(this.jTextFieldVersion, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 0, 5, 0), 0, 0));
        this.panelVersion.add(this.jTextFieldProductName, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, 10, 1, new Insets(5, 0, 5, 0), 0, 0));
        this.panelVersion.add(this.jLabelJessXLogo, new GridBagConstraints(0, 4, 1, 1, 1.0, 1.0, 10, 0, new Insets(5, 5, 5, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoEcoXP, new GridBagConstraints(0, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoTrading, new GridBagConstraints(1, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoMarket, new GridBagConstraints(2, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoTrobot, new GridBagConstraints(3, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoEcLille, new GridBagConstraints(4, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 5), 0, 0));
        this.panelTeam.add(this.jLabelLogoUSTL, new GridBagConstraints(5, 8, 1, 1, 1.0, 1.0, 10, 0, new Insets(0, 0, 0, 0), 0, 0));
        this.panelTeam.add(this.jLabelTeam, new GridBagConstraints(0, 0, 5, 1, 1.0, 0.1, 10, 1, new Insets(0, 0, 5, 0), 0, 0));
    }
    
    @Override
    protected void processWindowEvent(final WindowEvent e) {
        if (e.getID() == 201) {
            this.dispose();
        }
        super.processWindowEvent(e);
    }
    
    public abstract void loadModulesInfo();
    
    public void actionPerformed(final ActionEvent e) {
        this.dispose();
    }
    
    private JEditorPane getLicense() {
        String directory = System.getProperty("user.dir");
        directory = directory.subSequence(0, directory.lastIndexOf(this.fileSeparator) + 1).toString();
        JEditorPane license = new JEditorPane();
        try {
            license = new JEditorPane(new URL("file:" + directory + "GNU GENERAL PUBLIC LICENSE.txt"));
            license.setEditable(false);
        }
        catch (final IOException ex1) {
            Utils.logger.error("GNU GENERAL PUBLIC LICENSE.txt not found.");
            license.setText("No text found. The file GNU GENERAL PUBLIC LICENSE.txt is absent.\nWrite to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA to receive a copy of the GNU General Public License.");
        }
        return license;
    }
    
    public void Analysis_AboutBoxEnabled() {
        this.jTextAreaModulesAnalysis.setBackground(SystemColor.inactiveCaptionText);
        this.jTextAreaModulesAnalysis.setBackground(new Color(255, 255, 255));
        this.jTextAreaModulesAnalysis.setFont(new Font("Lucida Console", 0, 12));
        this.jTextAreaModulesAnalysis.setEditable(false);
        this.jTextAreaModulesAnalysis.setLineWrap(true);
        this.jTextAreaModulesAnalysis.setWrapStyleWord(true);
        this.loadModulesInfo();
        this.jTabbedPane1.add(this.panelModulesInfo, "Modules Information");
        (this.jScrollPane1 = new JScrollPane()).setPreferredSize(new Dimension(400, 400));
        this.panelModulesInfo.add(this.jScrollPane1, new GridBagConstraints(1, 5, 1, 1, 1.0, 1.0, 10, 1, new Insets(5, 5, 5, 5), 0, 0));
        this.jScrollPane1.getViewport().add(this.jTextAreaModulesAnalysis);
    }
    
    public JTextArea getModulesInfoTextArea() {
        return this.jTextAreaModulesAnalysis;
    }
}
