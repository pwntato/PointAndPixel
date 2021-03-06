package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import java.util.*;

public class PixelCanvas extends JPanel implements FocusListener {

  private CanvasFrame frame = null;
  private ToolsWindow toolsWindow = null;

  private boolean gridOn = true;
  private boolean gridExportOn = false;
  private boolean showSelected = true;
  
  private int pixelSize = ToolsWindow.DEFAULT_PIXEL_SIZE;   // pixels are square
  private int heightPixels = ToolsWindow.DEFAULT_WIDTH_PIXELS;
  private int widthPixels = ToolsWindow.DEFAULT_HEIGHT_PIXELS;
  
  private Color[][] grid = new Color[widthPixels][heightPixels];
  
  private File saveFile = null;
  private ArrayList<Action> history = null;
  
  private int selectedX = -1;
  private int selectedY = -1;
  private int selectedX1 = -1;
  private int selectedY1 = -1;
  private int selectedX2 = -1;
  private int selectedY2 = -1;  

  public PixelCanvas(CanvasFrame frame, ToolsWindow toolsWindow) {
    this.frame = frame;
    this.toolsWindow = toolsWindow;
  
    addMouseListener(new MAdapter(this));
    setFocusable(true);
    addFocusListener(this);
    
    resetGrid();
    
    resizeWindow();
    
    setVisible(true);
  }
  
  public void colorPixel(int column, int row, Color color) {
    addToHistory(column, row, grid[column][row]);
    grid[column][row] = color;
    repaint();
  }
  
  public void colorPixel(int column, int row) {
    colorPixel(column, row, toolsWindow.getSelectedColor());
  }
  
  public void addToHistory(int column, int row, Color color) {    
    history.add(new Action(column, row, color));
  }
  
  public void paint(Graphics g) {
    super.paint(g);
    Graphics2D g2d = (Graphics2D)g;
    
    for (int column = 0; column < widthPixels; column++) {
      for (int row = 0; row < heightPixels; row++) {
        if (grid[column][row] != null) {
          Color c = g2d.getColor();
          Color pixelColor = grid[column][row];
          
          if (selectedX1 >= 0 && selectedY1 >= 0 && selectedX2 >= 0 && selectedY2 >= 0) {
            if ((column >= Math.min(selectedX1, selectedX2) && column <= Math.max(selectedX1, selectedX2)) 
                && (row >= Math.min(selectedY1, selectedY2) && row <= Math.max(selectedY1, selectedY2))) {
              int[] colorParts = ToolsWindow.intToARGB(pixelColor.getRGB());
              pixelColor = new Color(colorParts[1], colorParts[2], colorParts[3], 128);
            }
          }
          
          g2d.setColor(pixelColor);
          g2d.fillRect(column * pixelSize, row * pixelSize, pixelSize, pixelSize);
          
          g2d.setColor(c);
        }
      }
    }
    
    if (gridOn) {
      g2d.drawRect(0, 0, widthPixels * pixelSize - 1, heightPixels * pixelSize - 1); 
      
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
    else if ((grid[column][row] == null && clickedColor == null) 
          || (grid[column][row] != null && grid[column][row].equals(clickedColor))) {
      colorPixel(column, row);
      
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
    
    newHistorySpot();
    
    for (int column=0; column<widthPixels; column++) {
      for (int row=0; row<heightPixels; row++) {
        grid[column][row] = background;
      }
    }
    
    newHistorySpot();
    
    repaint();
  }
  
  public void resizeGrid() {
    Color[][] oldGrid = grid;
    grid = new Color[widthPixels][heightPixels];
    
    newHistorySpot();
    
    for (int column=0; column<Math.min(widthPixels, oldGrid.length); column++) {
      for (int row=0; row<Math.min(heightPixels, oldGrid[0].length); row++) {
        colorPixel(column, row, oldGrid[column][row]);
      }
    }
    
    fill(widthPixels - 1, heightPixels - 1, Color.WHITE);
    
    repaint();
  }
  
  public Color[][] getSelected() {
    if ((selectedX1 < 0 && selectedY1 < 0) && (selectedX2 < 0 && selectedY2 < 0)) {
      return null;
    }
    
    Color[][] selected = new Color[Math.abs(selectedX2 - selectedX1) + 2][Math.abs(selectedY2 - selectedY1) + 2];
    
    for (int x = Math.min(selectedX1, selectedX2); x <= Math.max(selectedX1, selectedX2); x++) {
      for (int y = Math.min(selectedY1, selectedY2); y <= Math.max(selectedY1, selectedY2); y++) {
        selected[x - Math.min(selectedX1, selectedX2)][y - Math.min(selectedY1, selectedY2)] = grid[x][y];
      }
    }
    
    return selected;
  }
  
  public void pasteSelected(Color[][] selected) {
    if ((selectedX < 0 && selectedY < 0) || selected == null) {
      return;
    }
    
    newHistorySpot();
    
    for (int x = 0; x < selected.length - 1; x++) {
      for (int y = 0; y < selected[x].length - 1; y++) {
        if (selectedX + x < widthPixels && selectedY + y < heightPixels) {
          colorPixel(selectedX + x, selectedY + y, selected[x][y]);
        }
      }
    }
    
    clearSelected();
    repaint();
  }
  
  public void clearSelected() {
    selectedX1 = -1;
    selectedY1 = -1;
    selectedX2 = -1;
    selectedY2 = -1;
    
    repaint();
  }
  
  public void newHistorySpot() {
    history = new ArrayList<Action>();
  }
  
  public void focusGained(FocusEvent e) {
    toolsWindow.select(this);
  }

  public void focusLost(FocusEvent e) {}
  
  public void undo() {
    ArrayList<Action> toUndo = history;
    history = new ArrayList<Action>();
    
    for (int i=0; i<toUndo.size(); i++) {
      Action revert = toUndo.get(i);
      colorPixel(revert.getX(), revert.getY(), revert.getPreviousColor());
    }
    
    repaint();
  }
  
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
    setPreferredSize(new Dimension(widthPixels * pixelSize + 1, heightPixels * pixelSize + 1));
    frame.setSize(new Dimension(widthPixels * pixelSize + 6, heightPixels * pixelSize + 36));
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
  
  public boolean isShowSelected() {
    return showSelected;
  }
  
  public void setShowSelected(boolean showSelected) {
    this.showSelected = showSelected;
  }
  
  public int getSelectedX() {
    return selectedX;
  }
  
  public void setSelectedX(int x) {
    selectedX = x;
  }
  
  public int getSelectedY() {
    return selectedY;
  }
  
  public void setSelectedY(int y) {
    selectedY = y;
  }
  
  public File getSaveFile() {
    return saveFile;
  }
  
  public void setSaveFile(File saveFile) {
    this.saveFile = saveFile;
  }
  
  public CanvasFrame getFrame() {
    return frame;
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
      
      if (column >= widthPixels || row >= heightPixels) {
        return;
      }
      
      canvas.setSelectedX(column);
      canvas.setSelectedY(row);
      
      switch (toolsWindow.getToolState()) {
        case DRAW:
          canvas.newHistorySpot();
          canvas.colorPixel(column, row);
          break;
        case DROPPER:
          toolsWindow.setSelectedColor(canvas.getGrid()[column][row]);
          toolsWindow.setToolState(ToolsWindow.ToolState.DRAW);
          break;
        case FILL:
          if (!toolsWindow.getSelectedColor().equals(grid[column][row])) {
            newHistorySpot();
            fill(column, row, grid[column][row]);
          }
          break;
        case SELECT:
          if (selectedX1 < 0 && selectedY1 < 0) {
            selectedX1 = column;
            selectedY1 = row;
            
            if (selectedX2 < 0 && selectedY2 < 0) {
              selectedX2 = column;
              selectedY2 = row;
            }
          }
          else {
            selectedX2 = column;
            selectedY2 = row;
          }
          
          repaint();
          break;
        default:
          JOptionPane.showMessageDialog(frame, "Unhandled tool state: " + toolsWindow.getToolState());
      }
    }
  }
}

