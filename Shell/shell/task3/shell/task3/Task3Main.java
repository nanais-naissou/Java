package shell.task3;

import java.awt.Dimension;
import oop.graphics.Canvas;
import oop.runtime.EventPump;
import oop.tasks.Task;
import oop.term.Terminal;
import oop.utils.Shell;

public class Task3Main implements Runnable {

    public static void main(String[] args) {
        EventPump ep = new EventPump();
        Dimension d = new Dimension(640, 480);
        Task3Main boot = new Task3Main();
        ep.boot(d, boot);
    }
    
    Canvas m_canvas;  
    Terminal m_term;
    Shell m_shell;
    
    @Override
    public void run() { //un runnable toutes les 500ms
        Task task = Task.task();
        m_canvas = (Canvas) task.find("canvas");
        m_term = new Terminal(m_canvas, "Ubuntu Mono", 18);
        new PaintListener(m_canvas, m_term); //rafraichit a chaque fois le terminal
        
        task.post(new Runnable() {
            @Override
            public void run() {
                m_shell = new Shell(50);
                m_shell.setTerminal(m_term); //ici on passe le terminal au shell
                m_canvas.set(new KeyListener(m_shell));
            }
        }, 500); 
    }
}
