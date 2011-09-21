package pointAndPixel;

import java.util.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImgFilter extends FileFilter {
  private boolean pngOnly = false;
  private ArrayList<String> extensions = null;
  private String description = "";
  
  public ImgFilter() {
    extensions = new ArrayList<String>();
  }

  public boolean accept(File f) {
    if (f.isDirectory()) {
      return true;
    }
    
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      String extension = s.substring(i + 1).toLowerCase();
      
      for (String ext: extensions) {
        if (ext.equals(extension)) {
          return true;
        }
      }
    }

    return false;
  }
  
  public void addExtension(String extension) {
    extensions.add(extension);
  }

  public String getDescription() {
    return description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
}
