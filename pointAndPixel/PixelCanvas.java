package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class PixelCanvas extends JPanel {

  public PixelCanvas() {
    addMouseListener(new MAdapter());
    setFocusable(true);
    
    setBackground(Color.WHITE);
    
    setVisible(true);
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    g2d.drawLine(200, 0, 200, getHeight());
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

