package game.Avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Random;

import engine.model.Entity;

public class AdversaryAvatar extends engine.view.Avatar {
	private BufferedImage[] ghostFrames = new BufferedImage[5];
	private int chosenIndex; // Will hold the randomly chosen ghost for this adversary
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public AdversaryAvatar(Entity e) {
		super(e);
		try {
			ghostFrames[0] = ImageIO.read(getClass().getResource("/pacman-art/ghosts/blinky.png"));
			ghostFrames[1] = ImageIO.read(getClass().getResource("/pacman-art/ghosts/pinky.png"));
			ghostFrames[2] = ImageIO.read(getClass().getResource("/pacman-art/ghosts/clyde.png"));
			ghostFrames[3] = ImageIO.read(getClass().getResource("/pacman-art/ghosts/inky.png"));
			ghostFrames[4] = ImageIO.read(getClass().getResource("/pacman-art/ghosts/blue_ghost.png"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
		Random rand = new Random();
		chosenIndex = rand.nextInt(ghostFrames.length - 1);
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round((e.x()));
		int y_cm = Math.round((e.y()));
		int size = (int) (cellW_cm * 0.8);

		if (e instanceof engine.model.Adversary) {
			engine.model.Adversary adv = (engine.model.Adversary) e;
			int barWidth = size;
			int barHeight = 10;
			int maxHealth = adv.getMaxHealth();
			int health = adv.getHealth();
			float ratio = Math.max(0, Math.min(1f, health / (float) maxHealth)); // clamp entre 0 et 1

			// Position (au-dessus de l'entité)
			int barX = x_cm - barWidth / 2;
			int barY = y_cm - size / 2 - barHeight - 5;

			// Barre grise (fond)
			g.setColor(java.awt.Color.GRAY);
			g.fillRect(barX, barY, barWidth, barHeight);

			// Barre verte (vie restante)
			if (health < 2) {
				g.setColor(java.awt.Color.red);

			} else {
				g.setColor(java.awt.Color.GREEN);

			}
			g.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);

			// Bordure noire
			g.setColor(java.awt.Color.BLACK);
			g.drawRect(barX, barY, barWidth, barHeight);
		}

		// --- Puis dessin du sprite/sprite de base ---
		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);
		BufferedImage img = (e.m_model().player().bot.getPac()) ? ghostFrames[4] : ghostFrames[chosenIndex];
		if (img != null) {
			g.drawImage(img, -size / 2, -size / 2, size, size, null);
		} else {
			g.setColor(java.awt.Color.MAGENTA);
			g.fillOval(-size / 2, -size / 2, size, size);
		}

		g.setTransform(saved);

	}
}
