 /*  
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
package shell.task1;

import oop.graphics.Canvas;
import oop.graphics.Graphics;
import oop.graphics.Graphics.Colors;
import oop.graphics.Font;
import oop.streams.VirtualKeyCodes;

public class TextLine {
    private int cursorPosition = 0;
    private StringBuilder text = new StringBuilder();
    private int mouse_x;
    private int mouse_y;
    private boolean cursorVisible = true;
    private Listener listener;

    public interface Listener {
        void inserted(int pos, char c);
        void deleted(int pos, char c);
        void validated(String line);
    }

    public void set(Listener l) {
        this.listener = l;
    }

    public TextLine(Canvas canvas) {
        canvas.set(new PaintListener());
        canvas.set(new KeyListener());
        canvas.set(new MouseListener());
        canvas.repaint();

     
    }
	public void SetVisible() {
		this.cursorVisible = !this.cursorVisible;
	}
    class KeyListener implements Canvas.KeyListener {
        @Override
        public void pressed(Canvas canvas, int keyCode, char keyChar) {
            if (keyCode == VirtualKeyCodes.VK_BACK_SPACE && cursorPosition > 0) {
                char deletedChar = text.charAt(cursorPosition - 1);
                text.deleteCharAt(cursorPosition - 1);
                cursorPosition--;
                if (listener != null) listener.deleted(cursorPosition, deletedChar);
            } 
            else if (keyCode == VirtualKeyCodes.VK_LEFT && cursorPosition > 0) {
                cursorPosition--;
            } 
            else if (keyCode == VirtualKeyCodes.VK_RIGHT && cursorPosition < text.length()) {
                cursorPosition++;
            } 
            else if (keyCode == VirtualKeyCodes.VK_ENTER) {
                if (listener != null) listener.validated(text.toString());
                text.setLength(0);
                cursorPosition = 0;
            } 
            else if (keyChar != VirtualKeyCodes.CHAR_UNDEFINED) { // Vérifie si c'est un vrai caractère
                text.insert(cursorPosition, keyChar);
                if (listener != null) listener.inserted(cursorPosition, keyChar);
                cursorPosition++;
            }
            canvas.repaint();
        }

        @Override
        public void released(Canvas canvas, int keyCode, char keyChar) {}

        @Override
        public void typed(Canvas canvas, char keyChar) {}
    }

    class MouseListener implements Canvas.MouseListener {
        @Override
        public void moved(Canvas canvas, int x, int y) {
            mouse_x = x;
            mouse_y = y;
            canvas.repaint();
        }

        @Override
        public void pressed(Canvas canvas, int bno, int x, int y) {}

        @Override
        public void released(Canvas canvas, int bno, int x, int y) {}
    }

    class PaintListener implements Canvas.PaintListener {
        @Override
        public void paint(Canvas canvas, Graphics g) {
            g.setColor(Colors.black);
            g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

            g.setColor(Colors.white);
            g.drawString(text.toString(), mouse_x, mouse_y);

            if (cursorVisible) {
                String textBeforeCursor = text.substring(0, cursorPosition);
                Font font = g.getFont(); 
                int cursorX = mouse_x + font.getWidth(textBeforeCursor); 
                int cursorY = mouse_y - font.getAscent()-4; 

                g.setColor(Colors.white);
                g.fillRect(cursorX, cursorY, 2, font.getHeight());
            }
        }

        @Override
        public void visible(Canvas canvas) {}

        @Override
        public void revoked(Canvas canvas) {}
    }

  
}
