package pointAndPixel;

import java.awt.*;

public class Pixel {
  private Color color = null;
  private boolean selected = false;
  
  public Pixel(Color color) {
    this.color = color;
  }
  
  public Color getColor() {
    return color;
  }
  
  public void setColor(Color color) {
    this.color = color;
  }
  
  public boolean isSelected() {
    return selected;
  }
  
  public void setSelected(boolean selected) {
    this.selected = selected;
  }
}

