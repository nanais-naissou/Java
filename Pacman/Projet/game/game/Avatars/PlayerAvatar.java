package game.Avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import engine.model.Entity;

public class PlayerAvatar extends engine.view.Avatar {
	private BufferedImage[][] pacmanFrames = new BufferedImage[4][3]; // [direction][frame]
	private int frameIndex = 0;
	private int ticks = 0;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;
	protected int eatingSpeed;;
	int size = (int) (cellW_cm * 0.8);

	// Directions: 0 = right, 1 = down, 2 = left, 3 = up
	public PlayerAvatar(Entity e) {
		super(e);
		String[] dirNames = { "right", "down", "left", "up" };
		try {
			for (int dir = 0; dir < 4; dir++) {
				for (int f = 0; f < 3; f++) {
					pacmanFrames[dir][f] = ImageIO.read(
							getClass().getResource("/pacman-art/pacman-" + dirNames[dir] + "/" + (f + 1) + ".png"));
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round((e.x()));
		int y_cm = Math.round((e.y()));

		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);
		// No need to rotate: we pick the correct frame for the direction!
		eatingSpeed = e.m_model().player().bot.getPac() ? 3 : 6;
		ticks++;
		if (ticks % eatingSpeed == 0) {
			frameIndex = (frameIndex + 1) % 3;
		}

		int orientation = ((e.orientation() % 360) + 360) % 360;
		int dirIdx = 0; // default right
		if (orientation == 0) {
			dirIdx = 0; // right
		} else if (orientation == 90) {
			dirIdx = 1; // down
		} else if (orientation == 180) {
			dirIdx = 2; // left
		} else if (orientation == 270) {
			dirIdx = 3; // up
		}
		// If orientation is not exactly one of these, you could add logic to choose
		// closest

		BufferedImage img = pacmanFrames[dirIdx][frameIndex];
		if (img != null) {
			g.drawImage(img, -size / 2, -size / 2, size, size, null);
		} else {
			g.setColor(java.awt.Color.YELLOW);
			g.fillOval(-size / 2, -size / 2, size, size);
		}

		g.setTransform(saved);
	}

	public int size() {
		return size;
	}
}
