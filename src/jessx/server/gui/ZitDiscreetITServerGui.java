// 
// Decompiled by Procyon v0.6.0
// 

package jessx.server.gui;

import org.jdom.Content;
import org.jdom.Element;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.GridBagLayout;
import javax.swing.JSpinner;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ZitDiscreetITServerGui extends JPanel implements DisplayableNode
{
    private JLabel labelNotDiscreet;
    private JTextArea textAreaDescription;
    private JLabel labelFrequence;
    private JSlider sliderFrequency;
    private JSpinner spinnerLimit1;
    private JSpinner spinnerLimit2;
    private JLabel labelNumberOfRobots;
    private JSpinner spinnerNumberOfRobots;
    private JPanel jPanel;
    private GridBagLayout gridBagLayout1;
    
    public int getJSpinnerNumberOfRobots() {
        return new Integer(this.spinnerNumberOfRobots.getValue().toString());
    }
    
    public void setJSpinnerNumberOfRobots(final int num) {
        this.spinnerNumberOfRobots.setValue(num);
    }
    
    public int getJSliderFrequency() {
        if (this.sliderFrequency.getValue() == 0) {
            return 1;
        }
        return this.sliderFrequency.getValue();
    }
    
    public void setJsliderFrequency(final int value) {
        this.sliderFrequency.setValue(value);
    }
    
    public ZitDiscreetITServerGui() {
        this.labelNotDiscreet = new JLabel();
        this.textAreaDescription = new JTextArea();
        this.labelFrequence = new JLabel();
        this.sliderFrequency = new JSlider(0, 50, 10);
        this.spinnerLimit1 = new JSpinner();
        this.spinnerLimit2 = new JSpinner();
        this.labelNumberOfRobots = new JLabel();
        this.spinnerNumberOfRobots = new JSpinner();
        this.jPanel = new JPanel();
        this.gridBagLayout1 = new GridBagLayout();
        try {
            this.jbInit();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void initializePanel() {
        this.sliderFrequency.setValue(10);
        this.spinnerNumberOfRobots.setValue(0);
    }
    
    public void setEditable() {
        this.sliderFrequency.setEnabled(true);
        this.spinnerNumberOfRobots.setEnabled(true);
    }
    
    public void setUneditable() {
        this.sliderFrequency.setEnabled(false);
        this.spinnerNumberOfRobots.setEnabled(false);
    }
    
    @Override
    public String toString() {
        return "Non-detectable IT";
    }
    
    public JPanel getPanel() {
        return this;
    }
    
    public void jbInit() {
        this.labelNotDiscreet.setText("<html><body><p><h1>Non-detectable IT <br></h1></p></body></html>");
        this.labelFrequence.setText("Frequency :");
        this.labelNumberOfRobots.setText("Number of robots : ");
        this.textAreaDescription.setText("Description : The Non-detectable IT (intelligent trader) has been created to be able to manage several stocks and bonds in order to have an equally weighted portfolio.  ");
        this.spinnerNumberOfRobots.setModel(new SpinnerNumberModel(0, 0, 999, 1));
        this.sliderFrequency.setMajorTickSpacing(10);
        this.sliderFrequency.setMinorTickSpacing(1);
        this.sliderFrequency.setPaintLabels(true);
        this.sliderFrequency.setPaintTicks(true);
        this.setLayout(this.gridBagLayout1);
        this.add(this.labelNotDiscreet, new GridBagConstraints(0, 1, 0, 1, 0.0, 1.0, 10, 0, new Insets(3, 3, 3, 3), 0, 0));
        this.add(this.labelNumberOfRobots, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, 13, 0, new Insets(6, 6, 3, 3), 0, 20));
        this.add(this.spinnerNumberOfRobots, new GridBagConstraints(1, 3, 2, 1, 0.0, 0.0, 17, 0, new Insets(6, 3, 3, 6), 30, 0));
        this.add(this.labelFrequence, new GridBagConstraints(0, 9, 1, 1, 0.0, 0.0, 13, 0, new Insets(3, 3, 3, 6), 0, 20));
        this.add(this.sliderFrequency, new GridBagConstraints(1, 9, 1, 1, 1.0, 0.0, 17, 2, new Insets(3, 3, 3, 6), 70, 0));
        this.textAreaDescription.setBackground(SystemColor.inactiveCaptionText);
        this.textAreaDescription.setBackground(new Color(255, 255, 255));
        this.textAreaDescription.setLineWrap(true);
        this.textAreaDescription.setWrapStyleWord(true);
        this.textAreaDescription.setEditable(false);
        this.add(this.textAreaDescription, new GridBagConstraints(0, 11, 2, 4, 0.0, 1.0, 15, 2, new Insets(3, 3, 3, 3), 0, 120));
    }
    
    public void saveToXml(final Element node) {
        final Element zitDiscreetIT = new Element("ItDiscreet");
        final Element numOfRobots = new Element("NumberOfRobots");
        numOfRobots.setText(Integer.toString(this.getJSpinnerNumberOfRobots()));
        final Element periodPercentage = new Element("PeriodPercentage");
        periodPercentage.setText(Integer.toString(this.getJSliderFrequency()));
        zitDiscreetIT.addContent(numOfRobots);
        zitDiscreetIT.addContent(periodPercentage);
        node.addContent(zitDiscreetIT);
    }
    
    public void loadFromXml(final Element robots) {
        final Element robotParam = robots.getChild("ItDiscreet");
        this.setJSpinnerNumberOfRobots(Integer.parseInt(robotParam.getChild("NumberOfRobots").getText()));
        this.setJsliderFrequency(Integer.parseInt(robotParam.getChild("PeriodPercentage").getText()));
    }
}
