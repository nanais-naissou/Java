package game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import engine.IModel;
import engine.model.Model;
import engine.model.Player;
import engine.view.View;
import oop.graphics.Canvas;

public class View0 extends engine.view.View {
	private static BufferedImage heartImage;
	private static BufferedImage bulletImage;

	public View0(Canvas canvas, IModel m_model) {
		super(canvas, m_model);

		try {
			heartImage = ImageIO.read(View.class.getResource("/pacman-art/other/heart.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bulletImage = ImageIO.read(View.class.getResource("/pacman-art/other/bullet.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// TODO Auto-generated constructor stub

	}

	private void paintPlayer(Graphics2D g, Player p) {
		int x_cm = Math.round((p.x()));
		int y_cm = Math.round((p.y()));
		int size = cellW_cm / 2;
		Polygon hex = new Polygon();

		hex.addPoint(0, -size); // Top
		hex.addPoint((int) (size * Math.cos(Math.PI / 6)), (int) (-size * Math.sin(Math.PI / 6))); // Top right
		hex.addPoint((int) (size * Math.cos(Math.PI / 6)), (int) (size * Math.sin(Math.PI / 6))); // Bottom right
		hex.addPoint(0, size); // Bottom
		hex.addPoint((int) (-size * Math.cos(Math.PI / 6)), (int) (size * Math.sin(Math.PI / 6))); // Bottom left
		hex.addPoint((int) (-size * Math.cos(Math.PI / 6)), (int) (-size * Math.sin(Math.PI / 6))); // Top left

//		if (p.row() == m_focusRow && p.col() == m_focusCol) {
//			g.setColor(Color.RED);
//		} else {
//			g.setColor(Color.BLACK);
//		}
		g.setColor(new Color(255, 215, 0));
		int d = p.orientation();
		double rot = Math.toRadians(d);
		AffineTransform saved = g.getTransform();

		g.translate(x_cm, y_cm);
		g.rotate(rot);
		g.fillPolygon(hex);
		g.setTransform(saved);

	}

	protected void specialized_paint(Graphics2D g) {

		int w_pix = m_canvas.getWidth();
		int h_pix = m_canvas.getHeight();
		float scaleW = ((float) w_pix) / (ncols * cellW_cm);
		float scaleH = ((float) h_pix) / (nrows * cellH_cm);
		if (m_focusRow >= 0 && m_focusCol >= 0 && m_focusRow < nrows && m_focusCol < ncols) {

			int x = (int) (m_focusCol * scaleW);
			int y = (int) (m_focusRow * scaleH);

			g.setColor(Color.YELLOW);
			g.fillRect(mouseX, mouseY, Math.round(scaleW), Math.round(scaleH));

//			g.setColor(Color.BLACK);
//			g.drawString("BIKO", mouseX, mouseY + (int) (scaleH / 2));
		}

		// Player p = m_model.player();
		// assert (p != null);
		// paintPlayer(g, p);
		// float xMeters = p.x();
		// float yMeters = p.y();

		// int px = Math.round((xMeters + 0.5f) * scaleW);
		// int py = Math.round((yMeters + 0.5f) * scaleH);

	}

	protected void drawLives(Graphics2D g, int lives, int x, int y) {
		int size = 16;
		int space = 0;

		if (heartImage == null)
			return;

		for (int i = 0; i < lives; i++) {
			g.drawImage(heartImage, x + i * (size + space), y, size, size, null);
		}
	}

	protected void drawBullets(Graphics2D g, int lives, int x, int y) {
		int size = 25;
		int space = 0;

		if (bulletImage == null)
			return;

		for (int i = 0; i < lives; i++) {
			g.drawImage(bulletImage, x + i * (size + space), y, size, size, null);
		}
	}

}
