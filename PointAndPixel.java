import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import pointAndPixel.*;

class PointAndPixel {

  private int height = 0;
  private int width = 0;

  private JFrame tools = null;
  private CanvasFrame canvas = null;
  
  public PointAndPixel() {
    canvas = new CanvasFrame();
		JDialog d1 = new JDialog(canvas);
    d1.setModalityType(Dialog.ModalityType.MODELESS);
    canvas.setLocationRelativeTo(null);
    canvas.setLocation(0, 80);
		canvas.setVisible(true);
		
		tools = new ToolsWindow(canvas.getCanvas());
		JDialog d2 = new JDialog(tools);
    d2.setModalityType(Dialog.ModalityType.MODELESS);
    tools.setLocationRelativeTo(null);
    tools.setLocation(0, 80);
		tools.setVisible(true);
  }
  
  public static void main(String args[]) {
    PointAndPixel pointAndPixel = new PointAndPixel();
  }
}

