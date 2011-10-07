package pointAndPixel;

import java.awt.*;

public class Action {
  private int x = 0;
  private int y = 0;
  private Color previousColor;
  
  public Action(int x, int y, Color previousColor) {
    this.x = x;
    this.y = y;
    this.previousColor = previousColor;
  }
  
  public int getX() {
    return x;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public int getY() {
    return y;
  }
  
  public void setY(int y) {
    this.y = y;
  }
  
  public Color getPreviousColor() {
    return previousColor;
  }
  
  public void setPreviousColor(Color previousColor) {
    this.previousColor = previousColor;
  }
}

