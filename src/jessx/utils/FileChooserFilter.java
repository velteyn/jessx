// 
// Decompiled by Procyon v0.6.0
// 

package jessx.utils;

import java.io.File;
import javax.swing.filechooser.FileFilter;

public class FileChooserFilter extends FileFilter
{
    private String description;
    private String extension;
    
    public FileChooserFilter(final String description, final String extension) {
        if (description == null || extension == null) {
            throw new NullPointerException("The description (or extension) should not be null.");
        }
        this.description = description;
        this.extension = extension;
    }
    
    @Override
    public boolean accept(final File file) {
        if (file.isDirectory()) {
            return true;
        }
        final String nomFichier = file.getName().toLowerCase();
        return nomFichier.endsWith(this.extension);
    }
    
    @Override
    public String getDescription() {
        return this.description;
    }
}
