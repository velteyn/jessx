// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.business;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;
import javax.swing.JPanel;
import jessx.utils.XmlExportable;
import jessx.utils.XmlLoadable;

public interface GeneralParameters extends XmlLoadable, XmlExportable
{
    JPanel getGeneralParameterSetupGui();
    
    void addPeriodCountChangeListener(final ChangeListener p0);
    
    void removePeriodCountChangeListener(final ChangeListener p0);
    
    void addPeriodDurationChangeListener(final ChangeListener p0);
    
    void removePeriodDurationChangeListener(final ChangeListener p0);
    
    boolean getAfterSetupJoiningAllowed();
    
    void setAfterSetupJoiningAllowed(final boolean p0);
    
    String getSetupFileName();
    
    void setSetupFileName(final String p0);
    
    String getXMLVersion();
    
    void setXMLVersion(final String p0);
    
    String getLoggingFileName();
    
    void setLoggingFileName(final String p0);
    
    int getPeriodCount();
    
    void setPeriodCount(final int p0);
    
    JSpinner getPeriodCountSpinner();
    
    JSpinner getPeriodDurationSpinner();
    
    String getWorkingDirectory();
    
    void setWorkingDirectory(final String p0);
    
    float getInterestRate(final int p0);
    
    void setInterestRate(final int p0, final float p1);
    
    int getPeriodDuration();
    
    void setPeriodDuration(final int p0);
    
    void initializeGeneralParameters();
}
