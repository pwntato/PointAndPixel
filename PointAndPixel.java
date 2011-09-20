import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import pointAndPixel.*;

class PointAndPixel extends JFrame {

  private int height = 0;
  private int width = 0;

  private JFrame tools = null;
  
  public PointAndPixel() {
    super("Point and Pixel - Draw Old Timey Pixel Art");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
    height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	
	  PixelCanvas canvas = new PixelCanvas(this);
		JScrollPane scroller = new JScrollPane(canvas);  
    getContentPane().add(scroller);
		
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);      // worry about scaling later
		
		tools = new ToolsWindow(canvas);
		JDialog d2 = new JDialog(tools);
    d2.setModalityType(Dialog.ModalityType.MODELESS);
    tools.setLocationRelativeTo(null);
    tools.setLocation(0, 80);
		tools.setVisible(true);
  }
  
  public static void main(String args[]) {
    PointAndPixel pointAndPixel = new PointAndPixel();
  }
}

