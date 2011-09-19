package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class PixelCanvas extends JPanel {
  
  private JFrame frame = null;

  private boolean gridOn = true;
  
  private int pixelSize = 10;   // pixels are square
  private int heightPixels = 60;
  private int widthPixels = 60;
  
  private Color selectedColor = new Color(0, 0, 0, 255);
  
  private Color[][] grid = new Color[widthPixels][heightPixels];

  public PixelCanvas(JFrame frame) {
    this.frame = frame;
  
    addMouseListener(new MAdapter(this));
    setFocusable(true);
    
    setBackground(Color.WHITE);
    
    frame.setSize((int)((widthPixels + 0.5) * pixelSize), (int)((heightPixels + 0.5) * pixelSize));
    
    setVisible(true);
  }
  
  public void colorPixelFromClick(int x, int y) {
    colorPixel((int)(x / pixelSize), (int)(y / pixelSize));
  }
  
  public void colorPixel(int column, int row) {
    grid[column][row] = selectedColor;
    repaint();
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
    
    for (int column = 0; column < widthPixels; column++) {
      for (int row = 0; row < heightPixels; row++) {
        if (grid[column][row] != null) {
          Color c = g2d.getColor();
          
          g2d.setColor(grid[column][row]);
          g2d.fillRect(column * pixelSize, row * pixelSize, pixelSize, pixelSize);
          
          g2d.setColor(c);
        }
      }
    }
  }

  class MAdapter extends MouseAdapter 
	{
	  private PixelCanvas canvas = null;
	  
	  public MAdapter(PixelCanvas canvas) {
	    this.canvas = canvas;
	  }
	  
    public void mousePressed(MouseEvent e) {
      int x = e.getX();
      int y = e.getY();
      
      canvas.colorPixelFromClick(x, y);
    }

    public void mouseReleased(MouseEvent e) {
    }
  }
}

