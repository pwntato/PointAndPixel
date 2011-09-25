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
  
  public CanvasFrame() {
    super("Point and Pixel - Draw Old Timey Pixel Art");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
    height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	
	  canvas = new PixelCanvas(this);
		JScrollPane scroller = new JScrollPane(canvas);  
    getContentPane().add(scroller);
		
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);      // worry about scaling laterS
  }
  
  public PixelCanvas getCanvas() {
    return canvas;
  }
}

