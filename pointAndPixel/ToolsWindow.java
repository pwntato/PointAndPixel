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
  
  public ToolsWindow(PixelCanvas canvas) {
    super("Pixel Tools");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    this.canvas = canvas;
    
    setSize(200, 400);
		setResizable(false);
    setAlwaysOnTop(true);
    
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
    if ("Exit".equals(e.getActionCommand())) {
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
		fileMenu.addSeparator();
		
		fileMenu.add(setupMenu("Save"));	
		fileMenu.add(setupMenu("Save As"));	
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

