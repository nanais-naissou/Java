package shell.task3;

import oop.graphics.Canvas;
import oop.utils.Shell;

public class KeyListener implements Canvas.KeyListener {
    
    private Shell shell;
    
    public KeyListener(Shell shell) {
        this.shell = shell;
    }

    @Override
    public void pressed(Canvas canvas, int keyCode, char keyChar) {
        shell.pressed(keyCode, keyChar);
    }

    @Override
    public void released(Canvas canvas, int keyCode, char keyChar) {
        shell.released(keyCode, keyChar);
    }

    @Override
    public void typed(Canvas canvas, char keyChar) {
        shell.typed(keyChar);
    }
}
