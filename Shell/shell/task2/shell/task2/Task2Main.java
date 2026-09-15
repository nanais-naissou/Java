package shell.task2;

import java.awt.Dimension;
import oop.graphics.Canvas;
import oop.runtime.EventPump;
import oop.tasks.Task;
import oop.term.Terminal;

public class Task2Main {
  public static void main(String args[]) {
    EventPump ep = new EventPump();
    Dimension d = new Dimension(640,480);
    Runnable r = new Runnable() {
      public void run() {
        Task task = Task.task();
        Canvas canvas = (Canvas)task.find("canvas");
        
        Terminal term = new Terminal(canvas, "Ubuntu Mono", 18);
        new PaintListener(canvas, term);
        new KeyListener(canvas, term);
        new MouseListener(canvas, term);
        
        task.post(new Runnable() {
          public void run() {
            term.curseurBlinkk();
            task.post(this, 500);
          }
        });
      }
    };
    ep.boot(d, r);
  }
}
