// 
// Decompiled by Procyon v0.6.0
// 

package jessx.applet;

import javax.swing.JEditorPane;
import javax.swing.ImageIcon;

public abstract class FilesLoaded
{
    private static ImageIcon[] images;
    private static JEditorPane pane;
    
    public static void setImageIconArray(final ImageIcon[] imagesent) {
        FilesLoaded.images = imagesent;
    }
    
    public static void setJEditorPane(final JEditorPane panesent) {
        FilesLoaded.pane = panesent;
    }
    
    public static ImageIcon[] getImageIconArray() {
        return FilesLoaded.images;
    }
    
    public static JEditorPane getJEditorPane() {
        return FilesLoaded.pane;
    }
}
