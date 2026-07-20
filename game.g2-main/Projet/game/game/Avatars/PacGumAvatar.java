package game.Avatars;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.model.Entity;
import engine.view.Avatar;

public class PacGumAvatar extends Avatar {
	private BufferedImage PacGumImage = null;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public PacGumAvatar(Entity e) {
		super(e);
		try {
			PacGumImage = ImageIO.read(getClass().getResource("/pacman-art/other/PacGum.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round((e.x()));
		int y_cm = Math.round((e.y()));
		int size = (int) (cellW_cm * 0.8);

		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);

		if (PacGumImage != null) {
			g.drawImage(PacGumImage, -size / 2, -size / 2, size, size, null);
		} else {
			g.setColor(java.awt.Color.GREEN);
			g.fillOval(-size / 2, -size / 2, size, size);
		}

		g.setTransform(saved);
	}

}
