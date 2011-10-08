package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

public class CanvasFrame extends JFrame implements KeyListener {

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
	  canvas.addKeyListener(this);
		JScrollPane scroller = new JScrollPane(canvas);  
		//scroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		//scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    getContentPane().add(scroller);
    
	  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	  addWindowListener(new WAdapter());
	  
	  addKeyListener(this);
		
		setLocationRelativeTo(null);
		setVisible(true);
  }
  
  public PixelCanvas getCanvas() {
    return canvas;
  }
  
  public void keyPressed(KeyEvent evt) {
    if (evt.getModifiersEx() == 128) {
      switch(evt.getKeyCode()) {
        case 'S':
          toolsWindow.save();
          break;
        case 'O':
          toolsWindow.open();
          break;
        case 'N':
          toolsWindow.newPixelCanvas();
          break;
        case 'C':
          toolsWindow.copy();
          break;
        case 'V':
          toolsWindow.paste();
          break;
        case 'I':
          toolsWindow.importImage();
          break;
        case 'E':
          toolsWindow.exportImage();
          break;
        case 'P':
          toolsWindow.pixelSize();
          break;
        case 'H':
          toolsWindow.height();
          break;
        case 'W':
          toolsWindow.width();
          break;
        case 'Z':
          canvas.undo();
          break;
        case 'Q':
          System.exit(0);
          break;
      }
    }
  }

  public void keyReleased(KeyEvent evt) {
  }

  public void keyTyped(KeyEvent evt) {
  }
  
  private class WAdapter extends WindowAdapter { 
    public void windowClosing(WindowEvent e) {
      toolsWindow.deleteCanvas(canvas);
      setVisible(false);
      canvas = null;
    }
  }
}

