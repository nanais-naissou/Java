package oop.term;

public class Cursor {
    private int row;
    private int col;
    private boolean visible;

    public Cursor() {
        row = 0;
        col = 0;
        visible = true;
    }

    public int getRow() {
        return row;
    }
    public int getCol() {
        return col;
    } 
  public boolean isVisible() {
        return visible; }
    
    public void setVisible(boolean vis) {
      visible = vis;}
    
    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;}
    
 public void moveLeft() {
        if (col > 0) {
            col--;
        }
    }
    
    public void moveRight(int maxCols) {
        if (col < maxCols - 1) {
            col++;}
    }
    
    public void moveUp() {
        if (row > 0) {
            row--;}
    }
    
    public void moveDown(int maxRows) {
        if (row < maxRows - 1) {
            row++;
        }
    }
}
