// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils;

import java.awt.Font;
import java.awt.Color;

public interface Constants
{
    public static final String CLIENT_LOG_FILE = "./client.log";
    public static final String DEFAULTPARAMETER_USERNAME = "Your Name";
    public static final String DEFAULTPARAMETER_PASSWORD = "";
    public static final String DEFAULTPARAMETER_HOST = "localhost";
    public static final String VERSION = "1.6";
    public static final String DATE_OF_RELEASE = "Released on May 2008";
    public static final String VERSION_SERVER_WITH_ROBOTS = "1";
    public static final String WEB_PAGE = "www.jessx.net";
    public static final String LOG_DIALOG_FILTER_DESCRIPTION = "JessX XML log files (*.xml)";
    public static final String LOG_DIALOG_FILTER_EXTENSION = ".xml";
    public static final String GENERAL_SETTINGS_DIRECTORY = ".ecoxp";
    public static final String ERR_NO_USER_DIR = "Could not locate or create home settings directory";
    public static final Color COLOR_ODD_LINE = new Color(208, 208, 208);
    public static final Color COLOR_EVEN_LINE = new Color(218, 218, 218);
    public static final String ERR_CLIENT_READ_LASTLOGIN = "Unable to load login configuration file : ";
    public static final String ERR_CLIENT_WRITE_LASTLOGIN = "Error while writing login configuration file : ";
    public static final String CURRENCY = "$";
    public static final Color CLIENT_STATE_GREEN = new Color(145, 220, 180);
    public static final Color CLIENT_STATE_RED = new Color(220, 160, 155);
    public static final Color CLIENT_STATE_WHITE = new Color(255, 255, 255);
    public static final Color CLIENT_BUY_INACTIVE = new Color(200, 250, 190);
    public static final Color CLIENT_SELL_INACTIVE = new Color(190, 230, 250);
    public static final Color CLIENT_EXECUTE_INACTIVE = new Color(208, 208, 208);
    public static final Color CLIENT_BUY_ACTIVE = new Color(145, 220, 180);
    public static final Color CLIENT_SELL_ACTIVE = new Color(170, 190, 245);
    public static final Color CLIENT_EXECUTE_ACTIVE = new Color(255, 185, 185);
    public static final Color CLIENT_BUY_INTERMEDIARY = new Color(145, 220, 180);
    public static final Color CLIENT_SELL_INTERMEDIARY = new Color(170, 190, 245);
    public static final Color CLIENT_BUY_HL = new Color(255, 255, 255);
    public static final Color CLIENT_SELL_HL = new Color(255, 255, 255);
    public static final Font FONT_CLIENT_TITLE_BORDER = new Font("Verdana", 1, 12);
    public static final Font FONT_CLIENT_TEXTAREA = new Font("Verdana", 0, 11);
    public static final Font FONT_CLIENT_TEXTAREA_LARGE = new Font("Verdana", 0, 12);
    public static final Font FONT_CLIENT_LABEL_UP = new Font("Verdana", 1, 14);
    public static final Font FONT_DEFAULT_LABEL = new Font("Verdana", 0, 13);
}
