/*
 *  Copyright (C) Pr. Olivier Gruber <olivier dot gruber at acm dot org>
 *  
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package oop.term;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Font;
import oop.graphics.Graphics.Colors;
import oop.shell.ITerminal;

public class Terminal implements ITerminal {
  private final Canvas canvas;
   private final String fontName;
    private final int fontSize;
  
  private Text text;
      private Cursor cursor;
  
   private int nrows, ncols;
      private int charWidth, charHeight;
  private boolean initialisé = false;
  
   private boolean curseurVisible = true;
  
   // listener pour available/revoked
  private ITerminal.Listener listener;
  
    // monitor pour events
  private ITerminal.Monitor monitorListener;
  
  public Terminal(Canvas canvas, String fontName, int fontSize) {
    this.canvas = canvas;
      this.fontName = fontName;
   this.fontSize = fontSize;
    this.cursor = new Cursor();
  }
  
  public void clicked(int x, int y) {
      if (!initialisé) return;
      int row = y / charHeight;
   int col = x / charWidth;
    setCursor(row, col); // update clic
  }
  
  public void paint(Canvas canvas, Graphics g) {
    g.setColor(Colors.black); 
      g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    
    Font font = g.getFont(fontName, 0, fontSize);
       g.setFont(font);
    
    if (!initialisé) {
      // init font 
      charWidth = font.getWidth('A');
       charHeight = font.getHeight();
      ncols = canvas.getWidth() / charWidth;
         nrows = canvas.getHeight() / charHeight;
      text = new Text(nrows, ncols);
       cursor.setPosition(0, 0);
      initialisé = true;
    }
    
    g.setColor(Colors.green);
    for (int row = 0; row < nrows; row++) {
      StringBuilder sb = new StringBuilder();
      for (int col = 0; col < ncols; col++) {
        char c = text.getChar(row, col);
         if (c == '\0') {
             sb.append(' ');
         } else {
          sb.append(c);
         }
      }
      int yPos = row * charHeight + font.getAscent();
      g.drawString(sb.toString(), 0, yPos);
    }
    
    if (curseurVisible) {
      int Row = cursor.getRow();
         int Col = cursor.getCol();
      if (Row >= 0 && Row < nrows && Col >= 0 && Col < ncols) {
         int xPos = Col * charWidth;
          int yPos = Row * charHeight;
          char c = text.getChar(Row, Col);
          if (c == '\0') {
             c = ' ';
          }
         g.setColor(Colors.green);
         g.fillRect(xPos, yPos, charWidth, charHeight);
         g.setColor(Colors.black);
           int baseline = yPos + font.getAscent();
          g.drawString(String.valueOf(c), xPos, baseline);
      }
    }
  }
  
  @Override
  public int ncols() {
    if (initialisé) return ncols; else return 0; // nb colonnes
  }
  
  @Override
  public int nrows() {
    if (initialisé) return nrows; else return 0; // nb lignes
  }
  
  @Override
  public void setCursor(int row, int col) {
    if (!initialisé) return;
    if (row < 0) row = 0;
     if (row >= nrows) row = nrows - 1;
    if (col < 0) col = 0;
         if (col >= ncols) col = ncols - 1;
    cursor.setPosition(row, col); // set pos
    if (monitorListener != null) {
      monitorListener.cursorAt(row, col); // notify monitor
    }
    canvas.repaint();
  }
  
  @Override
  public int column() {
    return cursor.getCol(); // renvoie col
  }
  
  @Override
  public int row() {
    return cursor.getRow(); // renvoie row
  }
  
  @Override
  public void left() {
    int col = cursor.getCol();
    if (col > 0) {
      setCursor(cursor.getRow(), col - 1);
    }
  }
  
  @Override
  public void right() {
    int col = cursor.getCol();
    if (col < ncols - 1) {
      setCursor(cursor.getRow(), col + 1);
    }
  }
  
  @Override
  public void up() {
    int row = cursor.getRow();
    if (row > 0) {
      setCursor(row - 1, cursor.getCol());
    }
  }
  
  @Override
  public void down() {
    int row = cursor.getRow();
    if (row < nrows - 1) {
      setCursor(row + 1, cursor.getCol());
    }
  }
  
  @Override
  public void delete() {
    if (!initialisé) return;
    char deleted = text.delete(cursor.getRow(), cursor.getCol());
    if (monitorListener != null) {
      monitorListener.deleted(cursor.getRow(), cursor.getCol(), deleted); // notify delete
    }
    canvas.repaint();
  }
  
  @Override
  public void backspace() {
    if (!initialisé) return;
    int row = cursor.getRow();
    int col = cursor.getCol();
    if (col > 0) {
      setCursor(row, col - 1);
      char deleted = text.delete(row, col - 1);
         if (monitorListener != null) {
          monitorListener.deleted(row, col - 1, deleted); // back del
         }
      canvas.repaint();
    }
  }
  
  @Override
  public void clear() {
    if (!initialisé) return;
    text.initialisergrid();
       setCursor(0, 0);
    if (monitorListener != null) {
      monitorListener.cleared(); // clear full
    }
    canvas.repaint();
  }
  
  @Override
  public void clear(int row) {
    if (!initialisé) return;
    text.clear(row);
    setCursor(row, 0);
    if (monitorListener != null) {
       monitorListener.cleared(row); // clear line
    }
    canvas.repaint();
  }
  
  @Override
  public void enter() {
    if (!initialisé) return;
    int currentRow = cursor.getRow();
    if (currentRow < nrows - 1) {
      setCursor(currentRow + 1, 0); // new line
    } 
    canvas.repaint();
  }
  
  @Override
  public void insert(char c) {
    if (!initialisé) return;
    int row = cursor.getRow();
    int col = cursor.getCol();
    // insère le char, remplace si last col
    text.inserer(row, col, c);
    if (monitorListener != null) {
      monitorListener.inserted(row, col, c); // notif insert
    }
    if (col < ncols - 1) {
      right(); // move cursor a droite
    }
    canvas.repaint();
  }
  
  @Override
  public void set(Listener l) {
    if (this.listener != null) {
      this.listener.revoked(); // annule ancien listener
    }
    this.listener = l;
    if (this.listener != null) {
      this.listener.available(); // ok dispo
    }
  }
  
  @Override
  public void monitor(Monitor l) {
    if (this.monitorListener != null) {
      this.monitorListener.revoked(); // annule ancien monitor
    }
    this.monitorListener = l;
    if (this.monitorListener != null) {
      this.monitorListener.available(); // notif dispo
    }
  }
  
  public void curseurBlinkk() {
    cursor.setVisible(!cursor.isVisible());
       curseurVisible = !curseurVisible;
    canvas.repaint();
  }
}
