package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.util.regex.*;

public class ToolsWindow extends JFrame implements ActionListener, DocumentListener, FocusListener {

  public static final int DEFAULT_PIXEL_SIZE = 20;
  public static final int DEFAULT_WIDTH_PIXELS = 20;
  public static final int DEFAULT_HEIGHT_PIXELS = 20;

  private Container container = null;
  private PixelCanvas canvas = null;
  
  private JTextField red = null;
  private JTextField green = null;
  private JTextField blue = null;
  private JTextField alpha = null;
  
  private JPanel colorSample = null;
  
  private File saveFile = null;
  
  private JFileChooser fc = null;
  
  public ToolsWindow(PixelCanvas canvas) {
    super("Pixel Tools");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    this.canvas = canvas;
    
    setSize(200, 400);
		setResizable(false);
    //setAlwaysOnTop(true);
    
    saveFile = new File("drawing.pixel");
    fc = new JFileChooser();
    
		setupMenu();
    
    container = getContentPane();
		container.setLayout(new GridLayout(7, 1));
		
		container.add(setupButton("Draw Pixel"));
		container.add(setupButton("Copy Color"));
		
		Color color = canvas.getSelectedColor();
		
		JPanel pRed = new JPanel(new GridLayout(1, 2));
		pRed.add(new JLabel("  Red:"));
		red = new JTextField(3);
		red.setText(String.valueOf(color.getRed()));
		red.getDocument().addDocumentListener(this);
		red.addFocusListener(this);
		pRed.add(red);
		container.add(pRed);
		
		JPanel pGreen = new JPanel(new GridLayout(1, 2));
		pGreen.add(new JLabel("Green:"));
		green = new JTextField(3);
		green.setText(String.valueOf(color.getGreen()));
		green.getDocument().addDocumentListener(this);
		green.addFocusListener(this);
		pGreen.add(green);
		container.add(pGreen);
		
		JPanel pBlue = new JPanel(new GridLayout(1, 2));
		pBlue.add(new JLabel(" Blue:"));
		blue = new JTextField(3);
		blue.setText(String.valueOf(color.getBlue()));
		blue.getDocument().addDocumentListener(this);
		blue.addFocusListener(this);
		pBlue.add(blue);
		container.add(pBlue);
		
		JPanel pAlpha = new JPanel(new GridLayout(1, 2));
		pAlpha.add(new JLabel("Alpha:"));
		alpha = new JTextField(3);
		alpha.setText(String.valueOf(color.getAlpha()));
		alpha.getDocument().addDocumentListener(this);
		alpha.addFocusListener(this);
		pAlpha.add(alpha);
		container.add(pAlpha);
		
		colorSample = new JPanel();
		colorSample.setBackground(color);
		container.add(colorSample);
    
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    if ("Save".equals(e.getActionCommand())) {
      int returnVal = selectPixelFile();
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        saveFile = fc.getSelectedFile();
        savePixelFile(saveFile);
      }
    }
    else if ("Open".equals(e.getActionCommand())) {
      int returnVal = selectPixelFile();
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        saveFile = fc.getSelectedFile();
        loadPixelFile(saveFile);
      }
    }
    else if ("New".equals(e.getActionCommand())) {
      saveFile = new File("drawing.pixel");
      canvas.setPixelSize(DEFAULT_PIXEL_SIZE);
      canvas.setHeightPixels(DEFAULT_HEIGHT_PIXELS);
      canvas.setWidthPixels(DEFAULT_WIDTH_PIXELS);
      canvas.resetGrid();
    }
    else if ("Pixel Size".equals(e.getActionCommand())) {
      String response = JOptionPane.showInputDialog(null, "Set Pixel Size", "Pixel Size", JOptionPane.QUESTION_MESSAGE);
      canvas.setPixelSize(Integer.parseInt(response));
      
      canvas.resizeWindow();
      
      repaint();
    }
    else if ("Exit".equals(e.getActionCommand())) {
      System.exit(0);
    }
    else {
      JOptionPane.showMessageDialog(this, "Unhandled action: " + e.getActionCommand());
    }
  }
  
  public void focusGained(FocusEvent e) {
    ((JTextField)e.getComponent()).selectAll();
  }

  public void focusLost(FocusEvent e) {}
  
  public int selectPixelFile() {
    fc.resetChoosableFileFilters();
    fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
    ImgFilter filter = new ImgFilter();
    filter.addExtension("pixel");
    filter.setDescription("Point and Pixel File (*.pixel)");
    fc.setFileFilter(filter);
    fc.setSelectedFile(saveFile);
    return fc.showSaveDialog(this);
  }
  
  public void insertUpdate(DocumentEvent e) {
    try {
      int redPart = Integer.parseInt(red.getText());
      int greenPart = Integer.parseInt(green.getText());
      int bluePart = Integer.parseInt(blue.getText());
      int alphaPart = Integer.parseInt(alpha.getText());
      
      Color color = new Color(redPart, greenPart, bluePart, alphaPart);
      
      colorSample.setBackground(color);
      
      canvas.setSelectedColor(color);
    }
    catch (Exception ex) { ex.printStackTrace(); }
  }
    
  public void removeUpdate(DocumentEvent e) {}
  
  public void changedUpdate(DocumentEvent e) {}
  
  public void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
				
		fileMenu.add(setupMenu("New"));	
		fileMenu.add(setupMenu("Open"));		
		fileMenu.add(setupMenu("Save"));	
		fileMenu.addSeparator();		
		
		fileMenu.add(setupMenu("Import Image"));	
		fileMenu.add(setupMenu("Export Image"));		
		fileMenu.addSeparator();	
		
		fileMenu.add(setupMenu("Exit"));
		
		JMenu keyMenu = new JMenu("Settings");
		menuBar.add(keyMenu);
		
		keyMenu.add(setupMenu("Pixel Size"));	
		keyMenu.add(setupMenu("Canvas Width"));	
		keyMenu.add(setupMenu("Canvas Height"));	
		
		setJMenuBar(menuBar);
  }
  
  public int getValue(String json, String valueName) {
    int retVal = -1;
    Pattern pattern = Pattern.compile("\"" + valueName + "\":(\\d+)");
    Matcher matcher = pattern.matcher(json);
    if (matcher.find()) {
      retVal = Integer.parseInt(matcher.group(1));
    }
    return retVal;
  }
  
  public void loadPixelFile(File file) {
    BufferedReader input = null; 
    Color[][] grid = null;
    
    try {      
      input = new BufferedReader(new FileReader(file));
      String line = "";
      String json = "";
      int heightPixels = 0;
      int widthPixels = 0;
      int pixelSize = 0;
      
      while ((line = input.readLine()) != null) {
        json += line;
      }
      
      heightPixels = getValue(json, "height_pixels");
      widthPixels = getValue(json, "width_pixels");
      pixelSize = getValue(json, "pixel_size");
      
      canvas.setPixelSize(pixelSize);
      grid = new Color[widthPixels][heightPixels];  
      canvas.resizeWindow();    
      
      Pattern pixels = Pattern.compile("\\{\\S*\\}");
      Matcher matcher = pixels.matcher(json);
      while (matcher.find()) {
        String pixel = matcher.group();
        
        int xVal = getValue(pixel, "x");
        int yVal = getValue(pixel, "y");
        int rVal = getValue(pixel, "r");
        int gVal = getValue(pixel, "g");
        int bVal = getValue(pixel, "b"); 
        int aVal = getValue(pixel, "a");
        
        grid[xVal][yVal] = new Color(rVal, gVal, bVal, aVal);
      }
      
      canvas.setGrid(grid);
    } catch (IOException e) { System.out.println(e); }
    finally {
      if (input != null) {
        try { input.close(); } catch (Exception e) {}
      }
    }
  }
  
  public void savePixelFile(File file) {
    Writer output = null;    
    
    try {      
      Color[][] grid = canvas.getGrid();
      output = new BufferedWriter(new FileWriter(file));

      output.write("{\n");
      output.write("  \"pixel_size\":" + canvas.getPixelSize());
      output.write(",\n  \"height_pixels\":" + canvas.getHeightPixels());
      output.write(",\n  \"width_pixels\":" + canvas.getWidthPixels());
      output.write(",\n  \"pixels\":[");
      
      boolean firstPixel = true;
      
      for (int column = 0; column < grid.length; column++) {
        for (int row = 0; row < grid[column].length; row++) {
          if (grid[column][row] != null) {
            Color c = grid[column][row];
            if (!firstPixel) {
              output.write(",");
            }
            firstPixel = false;
            output.write("\n    {\"x\":" + column + ",\"y\":" + row + ",\"r\":" + c.getRed() + ",\"g\":" + c.getGreen() + ",\"b\":" + c.getBlue() + ",\"a\":" + c.getAlpha() + "}");
          }
        }
      }
      
      output.write("\n  ]\n}");
    } catch (IOException e) { System.out.println(e); }
    finally {
      if (output != null) {
        try { output.close(); } catch (Exception e) {}
      }
    }
  }
  
  public JMenuItem setupMenu(String menuText) {
		JMenuItem menuItem = new JMenuItem(menuText);
		menuItem.setActionCommand(menuText);
		menuItem.addActionListener(this);
		return menuItem;
  }
  
  public JButton setupButton(String buttonText) {
		JButton button = new JButton(buttonText);
		button.setActionCommand(buttonText);
		button.addActionListener(this);
		return button;
  }
}

