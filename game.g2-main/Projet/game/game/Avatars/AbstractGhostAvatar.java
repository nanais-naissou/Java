package game.Avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import engine.model.Adversary;
import engine.model.Entity;

public abstract class AbstractGhostAvatar extends engine.view.Avatar {
	protected BufferedImage normalFrame;
	protected BufferedImage blueFrame;
	protected BufferedImage eyesFrame;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public AbstractGhostAvatar(Entity e, String ghostImagePath) {
		super(e);
		try {
			normalFrame = ImageIO.read(getClass().getResource(ghostImagePath));
			blueFrame = ImageIO.read(getClass().getResource("/pacman-art/ghosts/blue_ghost.png"));
			eyesFrame = ImageIO.read(getClass().getResource("/pacman-art/ghosts/eyes.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round(e.x());
		int y_cm = Math.round(e.y());
		int size = (int) (cellW_cm * 0.8);
		
			


		
		// Draw health bar if Adversary
		if (e instanceof engine.model.Adversary) {
			engine.model.Adversary adv = (engine.model.Adversary) e;
			int barWidth = size;
			int barHeight = 10;
			int maxHealth = adv.getMaxHealth();
			int health = adv.getHealth();
			float ratio = Math.max(0, Math.min(1f, health / (float) maxHealth));

			x_cm = Math.round(e.x());
			y_cm = Math.round(e.y());
			int barX = x_cm - barWidth / 2;
			int barY = y_cm - size / 2 - barHeight - 5;

			g.setColor(java.awt.Color.GRAY);
			g.fillRect(barX, barY, barWidth, barHeight);

			g.setColor(health < 2 ? java.awt.Color.RED : java.awt.Color.GREEN);
			g.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);

			g.setColor(java.awt.Color.BLACK);
			g.drawRect(barX, barY, barWidth, barHeight);
		}

		// Choose appropriate image
		BufferedImage img;
		if (e instanceof engine.model.Adversary adv) {
			if (adv.getisDead() && !adv.inHisInitPos) {
				img = eyesFrame;
			} else {
				img = (e.m_model().player().bot.getPac()) ? blueFrame : normalFrame;
			}
		} else {
			img = normalFrame;
		}

		// Draw ghost image
		AffineTransform saved = g.getTransform();
		x_cm = Math.round(e.x());
		y_cm = Math.round(e.y());
		g.translate(x_cm, y_cm);
		if (img != null) {
			g.drawImage(img, -size / 2, -size / 2, size, size, null);
		} else {
			g.setColor(java.awt.Color.MAGENTA);
			g.fillOval(-size / 2, -size / 2, size, size);
		}
		g.setTransform(saved);
	}
}
