package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class PixelCanvas extends JPanel {

  private boolean gridOn = true;
  
  private int pixelSize = 10;   // pixels are square
  private int heightPixels = 60;
  private int widthPixels = 60;
  
  private JFrame frame = null;

  public PixelCanvas(JFrame frame) {
    this.frame = frame;
  
    addMouseListener(new MAdapter());
    setFocusable(true);
    
    setBackground(Color.WHITE);
    
    frame.setSize((int)((widthPixels + 0.5) * pixelSize), (int)((heightPixels + 0.5) * pixelSize));
    
    setVisible(true);
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    if (gridOn) {
      for (int x = 0; x <= widthPixels * pixelSize; x += pixelSize) {
        g2d.drawLine(x, 0, x, heightPixels * pixelSize);
      }
      
      for (int y = 0; y <= heightPixels * pixelSize; y += pixelSize) {
        g2d.drawLine(0, y, widthPixels * pixelSize, y);
      }
    }
  }

  class MAdapter extends MouseAdapter 
	{
    public void mousePressed(MouseEvent e) {
      //int x = e.getX();
      //int y = e.getY();
    }

    public void mouseReleased(MouseEvent e) {
    }
  }
}

