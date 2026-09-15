package game.Avatars;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import engine.model.Entity;
import engine.view.Avatar;

public class DotAvatar extends Avatar {
	private BufferedImage dotImage = null;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public DotAvatar(Entity e) {
		super(e);
		try {
			dotImage = ImageIO.read(getClass().getResource("/pacman-art/other/dot.png"));
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round(e.x());
		int y_cm = Math.round(e.y());

		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);

		// ✅ Pas de fond de cellule
		// ✅ Juste un point blanc pur (dot arcade)
		g.setColor(Color.WHITE);
		g.fillOval(-4, -4, 8, 8); // petit point visible

		g.setTransform(saved);
	}

}
