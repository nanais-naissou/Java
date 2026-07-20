package game;

import java.awt.Dimension;
import oop.graphics.Canvas;
import oop.runtime.EventPump;
import oop.tasks.Task;
import java.io.IOException;
import javax.sound.sampled.*;

public class MainTask3 {

	public static void main(String args[]) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		
		SoundPlayer.play("src/resources/dexter_theme.wav");
		
		boolean mode = ConfigLoader.loadMode();

		EventPump ep = new EventPump();
		Dimension d = new Dimension(640, 480);

		Runnable r = new Runnable() {
			public void run() {
				Task task = Task.task();
				Canvas canvas = (Canvas) task.find("canvas");

				Painter painter = new Painter(canvas, 20, 20);
				painter.setMode(mode); 
			}
		};

		ep.boot(d, r);
	}
}
