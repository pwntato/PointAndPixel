package pointAndPixel;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

public class ToolsWindow extends JFrame implements ActionListener {

  private Container container = null;

  public ToolsWindow() {
    super("Pixel Tools");
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    
    setSize(150, 200);
		setResizable(false);
    setAlwaysOnTop(true);
    
    container = getContentPane();
		container.setLayout(new GridLayout(2, 1));
		
		container.add(setupButton("Draw Pixel"));
		container.add(setupButton("Copy Color"));
    
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
  
  public JButton setupButton(String buttonText) {
		JButton button = new JButton(buttonText);
		button.setActionCommand(buttonText);
		button.addActionListener(this);
		return button;
  }
}

