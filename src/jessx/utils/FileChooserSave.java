// 
//This program is free software; GNU license ; USE AT YOUR RISK , WITHOUT ANY WARRANTY
// 

package jessx.utils;

import javax.swing.JFileChooser;
import java.io.OutputStream;
import java.io.FileOutputStream;
import javax.swing.JOptionPane;
import java.io.File;
import org.jdom.output.XMLOutputter;
import org.jdom.output.Format;
import java.awt.Component;
import org.jdom.Document;

public class FileChooserSave
{
    int ans;
    String fileName;
    String directoryName;
    public static final int SAVE_DONE = 0;
    public static final int SAVE_UNDONE = 1;
    
    public FileChooserSave(final Document doc, final Component parent, final String jessXSection, final String extension) {
        this.ans = 1;
        final JFileChooser chooser = Utils.newFileChooser(null, "", String.valueOf(extension) + " files", extension);
        chooser.removeChoosableFileFilter(chooser.getAcceptAllFileFilter());
        final int option = chooser.showSaveDialog(parent);
        if (option == 1) {
            this.ans = 1;
            return;
        }
        final XMLOutputter sortie = new XMLOutputter(Format.getPrettyFormat());
        try {
            System.out.print(chooser.getCurrentDirectory());
            int answer = 0;
            if ((chooser.getSelectedFile().getAbsolutePath().endsWith("." + extension) && chooser.getSelectedFile().exists()) || new File(String.valueOf(chooser.getSelectedFile().getAbsolutePath()) + "." + extension).exists()) {
                answer = JOptionPane.showConfirmDialog(parent, "A file with the same name already exists.\nDo you want to replace it?", jessXSection, 0, -1);
                this.ans = answer * answer;
            }
            if (answer == 0) {
                if (chooser.getSelectedFile().getAbsolutePath().endsWith("." + extension)) {
                    sortie.output(doc, new FileOutputStream(chooser.getSelectedFile().getAbsolutePath()));
                    this.ans = 0;
                    this.fileName = chooser.getSelectedFile().getName().substring(0, chooser.getSelectedFile().getName().length() - 4);
                    this.directoryName = chooser.getSelectedFile().getParent();
                }
                else {
                    sortie.output(doc, new FileOutputStream(new File(String.valueOf(chooser.getSelectedFile().getAbsolutePath()) + "." + extension)));
                    this.ans = 0;
                    this.fileName = chooser.getSelectedFile().getName();
                    this.directoryName = chooser.getSelectedFile().getParent();
                }
            }
        }
        catch (Exception ex) {
            System.out.print("Error when the file is created...\n" + ex.toString());
            this.ans = 1;
        }
    }
    
    public String getDirectoryName() {
        return this.directoryName;
    }
    
    public String getFileName() {
        return this.fileName;
    }
    
    public int getAnswer() {
        return this.ans;
    }
}
