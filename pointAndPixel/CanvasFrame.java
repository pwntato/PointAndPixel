package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class CanvasFrame extends JFrame {

  private int height = 0;
  private int width = 0;
  
  private PixelCanvas canvas = null;
  private ToolsWindow toolsWindow = null;
  
  public CanvasFrame(ToolsWindow toolsWindow) {
    super("Point and Pixel - Draw Old Timey Pixel Art");   
    
    this.toolsWindow = toolsWindow;
	  
    height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	
	  canvas = new PixelCanvas(this, toolsWindow);
		JScrollPane scroller = new JScrollPane(canvas);  
    getContentPane().add(scroller);
    
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	  addWindowListener(new WAdapter());
		
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);      // worry about scaling laterS
  }
  
  public PixelCanvas getCanvas() {
    return canvas;
  }
  
  private class WAdapter extends WindowAdapter { 
    public void windowClosing(WindowEvent e) {
      toolsWindow.deleteCanvas(canvas);
      setVisible(false);
      canvas = null;
    }
  }
}

