import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import pointAndPixel.*;

class PointAndPixel extends JFrame implements ActionListener {

  private int height = 0;
  private int width = 0;

  private JFrame tools = null;
  
  public PointAndPixel() {
    super("Point and Pixel - Draw Old Timey Pixel Art");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
    height = (int)Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    width = (int)Toolkit.getDefaultToolkit().getScreenSize().getWidth();
	
		JScrollPane scroller = new JScrollPane(new PixelCanvas(this));  
    getContentPane().add(scroller);
		
		setupMenu();
		
		setLocationRelativeTo(null);
		setVisible(true);
		setResizable(false);      // worry about scaling later
		
		tools = new ToolsWindow();
		JDialog d2 = new JDialog(tools);
    d2.setModalityType(Dialog.ModalityType.MODELESS);
    tools.setLocationRelativeTo(null);
    tools.setLocation(0, 80);
		tools.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    if ("Exit".equals(e.getActionCommand())) {
      System.exit(0);
    }
    else {
      JOptionPane.showMessageDialog(this, "Unhandled action: " + e.getActionCommand());
    }
  }
  
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
  
  public static void main(String args[]) {
    PointAndPixel pointAndPixel = new PointAndPixel();
  }
}

