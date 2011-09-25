package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class PixelCanvas extends JPanel implements FocusListener {

  public enum ToolState {
    DRAW,
    DROPPER
  }
  
  private JFrame frame = null;
  private ToolsWindow toolsWindow = null;
  
  private ToolState toolState = ToolState.DRAW;

  private boolean gridOn = true;
  
  private int pixelSize = ToolsWindow.DEFAULT_PIXEL_SIZE;   // pixels are square
  private int heightPixels = ToolsWindow.DEFAULT_WIDTH_PIXELS;
  private int widthPixels = ToolsWindow.DEFAULT_HEIGHT_PIXELS;
  
  private Color selectedColor = new Color(0, 0, 0, 255);
  
  private Color[][] grid = new Color[widthPixels][heightPixels];

  public PixelCanvas(JFrame frame, ToolsWindow toolsWindow) {
    this.frame = frame;
    this.toolsWindow = toolsWindow;
  
    addMouseListener(new MAdapter(this));
    setFocusable(true);
    addFocusListener(this);
    
    setBackground(Color.WHITE);
    resetGrid();
    
    resizeWindow();
    
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
    
    if (gridOn) {
      for (int x = 0; x <= widthPixels; x++) {
        g2d.drawLine(x * pixelSize, 0, x * pixelSize, heightPixels * pixelSize);
      }
      
      for (int y = 0; y <= heightPixels; y++) {
        g2d.drawLine(0, y * pixelSize, widthPixels * pixelSize, y * pixelSize);
      }
    }
  }
  
  public void resetGrid() {
    Color background = new Color(255, 255, 255, 255);
    grid = new Color[widthPixels][heightPixels];
    
    repaint();
    
    for (int column=0; column<widthPixels; column++) {
      for (int row=0; row<heightPixels; row++) {
        grid[column][row] = background;
      }
    }
    
    repaint();
  }
  
  public void resizeGrid() {
    Color[][] oldGrid = grid;
    grid = new Color[widthPixels][heightPixels];
    
    for (int column=0; column<Math.min(widthPixels, oldGrid.length); column++) {
      for (int row=0; row<Math.min(heightPixels, oldGrid[0].length); row++) {
        grid[column][row] = oldGrid[column][row];
      }
    }
    
    repaint();
  }
  
  public void focusGained(FocusEvent e) {
    toolsWindow.select(this);
  }

  public void focusLost(FocusEvent e) {}
  
  public ToolState getToolState() {
    return toolState;
  }
  
  public void setToolState(ToolState toolState) {
    this.toolState = toolState;
  }
  
  public boolean isGridOn() {
    return gridOn;
  }
  
  public void setGridOn(boolean gridOn) {
    this.gridOn = gridOn;
  }
  
  public void resizeWindow() {
    frame.setSize((int)((widthPixels + 0.5) * pixelSize), (int)((heightPixels + 2) * pixelSize));
  }
  
  public Color getSelectedColor() {
    return selectedColor;
  }
  
  public void setSelectedColor(Color color) {
    selectedColor = color;
  }
  
  public Color[][] getGrid() {
    return grid;
  }
  
  public void setGrid(Color[][] grid) {
    this.grid = grid;
    repaint();
  }
  
  public int getPixelSize() {
    return pixelSize;
  }
  
  public void setPixelSize(int pixelSize) {
    this.pixelSize = pixelSize;
  }
  
  public int getHeight() {
    return heightPixels * pixelSize;
  }
  
  public int getHeightPixels() {
    return heightPixels;
  }
  
  public void setHeightPixels(int heightPixels) {
    this.heightPixels = heightPixels;
  }
  
  public int getWidth() {
    return widthPixels * pixelSize;
  }
  
  public int getWidthPixels() {
    return widthPixels;
  }
  
  public void setWidthPixels(int widthPixels) {
    this.widthPixels = widthPixels;
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
      
      switch (canvas.getToolState()) {
        case DRAW:
          canvas.colorPixelFromClick(x, y);
          break;
        case DROPPER:
          break;
      }
    }

    public void mouseReleased(MouseEvent e) {
    }
  }
}

