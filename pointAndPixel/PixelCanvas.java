package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class PixelCanvas extends JPanel implements FocusListener {

  private JFrame frame = null;
  private ToolsWindow toolsWindow = null;

  private boolean gridOn = true;
  private boolean gridExportOn = false;
  
  private int pixelSize = ToolsWindow.DEFAULT_PIXEL_SIZE;   // pixels are square
  private int heightPixels = ToolsWindow.DEFAULT_WIDTH_PIXELS;
  private int widthPixels = ToolsWindow.DEFAULT_HEIGHT_PIXELS;
  
  private Color[][] grid = new Color[widthPixels][heightPixels];

  public PixelCanvas(JFrame frame, ToolsWindow toolsWindow) {
    this.frame = frame;
    this.toolsWindow = toolsWindow;
  
    addMouseListener(new MAdapter(this));
    setFocusable(true);
    addFocusListener(this);
    
    //setBackground(Color.WHITE);
    resetGrid();
    
    resizeWindow();
    
    setVisible(true);
  }
  
  public void colorPixel(int column, int row) {
    grid[column][row] = toolsWindow.getSelectedColor();
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
  
  public void fill(int column, int row, Color clickedColor) {
    if (column < 0 || column > widthPixels - 1 || row < 0 || row > heightPixels - 1) {
      return;
    }
    else if (grid[column][row].equals(clickedColor)) {
      grid[column][row] = toolsWindow.getSelectedColor();
      
      fill(column - 1, row, clickedColor);
      fill(column + 1, row, clickedColor);
      fill(column, row - 1, clickedColor);
      fill(column, row + 1, clickedColor);
      
      repaint();
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
  
  public boolean isGridOn() {
    return gridOn;
  }
  
  public void setGridOn(boolean gridOn) {
    this.gridOn = gridOn;
    repaint();
  }
  
  public boolean isGridExportOn() {
    return gridExportOn;
  }
  
  public void setGridExportOn(boolean gridExportOn) {
    this.gridExportOn = gridExportOn;
  }
  
  public void resizeWindow() {
    frame.setSize((int)((widthPixels + 0.5) * pixelSize), (int)((heightPixels + 2) * pixelSize));
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
    }

    public void mouseReleased(MouseEvent e) {
      int column = (int)(e.getX() / pixelSize);
      int row = (int)(e.getY() / pixelSize);
      
      switch (toolsWindow.getToolState()) {
        case DRAW:
          canvas.colorPixel(column, row);
          break;
        case DROPPER:
          toolsWindow.setSelectedColor(canvas.getGrid()[column][row]);
          toolsWindow.setToolState(ToolsWindow.ToolState.DRAW);
          break;
        case FILL:
          if (!grid[column][row].equals(toolsWindow.getSelectedColor())) {
            fill(column, row, grid[column][row]);
          }
          break;
        default:
          JOptionPane.showMessageDialog(frame, "Unhandled tool state: " + toolsWindow.getToolState());
      }
    }
  }
}

