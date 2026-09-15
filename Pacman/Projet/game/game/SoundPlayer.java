package game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
	private static Clip clip;

	public static void play(String filepath) {
		try {
			File file = new File(filepath);
			AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
			clip = AudioSystem.getClip();
			clip.open(audioStream);
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Boucle infinie
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void stopMusic() {
		if (clip != null && clip.isRunning()) {
			clip.stop();
		}
	}
}
