package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class ToolsWindow extends JFrame implements ActionListener, DocumentListener {

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
    setAlwaysOnTop(true);
    
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
		pRed.add(red);
		container.add(pRed);
		
		JPanel pGreen = new JPanel(new GridLayout(1, 2));
		pGreen.add(new JLabel("Green:"));
		green = new JTextField(3);
		green.setText(String.valueOf(color.getGreen()));
		green.getDocument().addDocumentListener(this);
		pGreen.add(green);
		container.add(pGreen);
		
		JPanel pBlue = new JPanel(new GridLayout(1, 2));
		pBlue.add(new JLabel(" Blue:"));
		blue = new JTextField(3);
		blue.setText(String.valueOf(color.getBlue()));
		blue.getDocument().addDocumentListener(this);
		pBlue.add(blue);
		container.add(pBlue);
		
		JPanel pAlpha = new JPanel(new GridLayout(1, 2));
		pAlpha.add(new JLabel("Alpha:"));
		alpha = new JTextField(3);
		alpha.setText(String.valueOf(color.getAlpha()));
		alpha.getDocument().addDocumentListener(this);
		pAlpha.add(alpha);
		container.add(pAlpha);
		
		colorSample = new JPanel();
		colorSample.setBackground(color);
		container.add(colorSample);
    
    setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    if ("Save".equals(e.getActionCommand())) {
      fc.resetChoosableFileFilters();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      ImgFilter filter = new ImgFilter();
      filter.addExtension("pixel");
      filter.setDescription("Point and Pixel File (*.pixel)");
      fc.setFileFilter(filter);
      fc.setSelectedFile(saveFile);
      int returnVal = fc.showSaveDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        saveFile = fc.getSelectedFile();
        savePixelFile(saveFile);
      }
    }
    else if ("Exit".equals(e.getActionCommand())) {
      System.exit(0);
    }
    else {
      JOptionPane.showMessageDialog(this, "Unhandled action: " + e.getActionCommand());
    }
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
		keyMenu.add(setupMenu("Canvas Size"));	
		
		setJMenuBar(menuBar);
  }
  
  public void savePixelFile(File file) {
    Writer output = null;    
    
    try {      
      Color[][] grid = canvas.getGrid();
      output = new BufferedWriter(new FileWriter(file));

      output.write("{");
      output.write("\"pixel_size\":" + canvas.getPixelSize());
      output.write(",\"height_pixels\":" + canvas.getHeightPixels());
      output.write(",\"width_pixels\":" + canvas.getWidthPixels());
      output.write(",\"pixels\":[");
      
      boolean firstPixel = true;
      
      for (int column = 0; column < grid.length; column++) {
        for (int row = 0; row < grid[column].length; row++) {
          if (grid[column][row] != null) {
            Color c = grid[column][row];
            if (!firstPixel) {
              output.write(",");
            }
            firstPixel = false;
            output.write("{\"x\":" + column + ",\"y\":" + row + ",\"r\":" + c.getRed() + ",\"g\":" + c.getGreen() + ",\"b\":" + c.getBlue() + ",\"a\":" + c.getAlpha() + "}");
          }
        }
      }
      
      output.write("]}");
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

