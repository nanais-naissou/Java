package engine.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import engine.IModel;
import engine.model.Adversary;
import engine.model.Entity;
import engine.model.Item;
import engine.model.Model;
import engine.model.Obstacle;
import engine.model.Player;
import game.Game;
import game.SoundPlayer;
import game.Avatars.AdversaryAvatar;
import game.Avatars.AppleAvatar;
import game.Avatars.DotAvatar;
import game.Avatars.GhostRunAvatar;
import game.Avatars.GhostWallFollowAvatar;
import game.Avatars.GhostWanderingAvatar;
import game.Avatars.GhostchaseAvatar;
import game.Avatars.ItemAvatar;
import game.Avatars.ObstacleAvatar;
import game.Avatars.PacGumAvatar;
import game.Avatars.PlayerAvatar;
import game.Avatars.ProjectileAvatar;
import game.Avatars.StrawberryAvatar;
import game.Entities.Apple;
import game.Entities.Dot;
import game.Entities.GhostRun;
import game.Entities.GhostWallFollow;
import game.Entities.GhostWandering;
import game.Entities.Ghostchase;
import game.Entities.PacGum;
import game.Entities.Projectile;
import game.Entities.Strawberry;
import oop.graphics.Canvas;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;

public abstract class View implements engine.IView {

	public Canvas m_canvas;
	protected IModel m_model;
	protected int m_focusRow = -1;
	protected int m_focusCol = -1;
	protected float scale = 1f;
	protected float zoom = 1f;
	protected int mouseX = -1;
	protected int mouseY = -1;
	private boolean debug = false; // debug=True to show Performance Stats [Ticker,paint]
	private boolean debugg = false;
	public boolean gameOver = false;
	public boolean playerDeath = false;
	public boolean winner = false;

	private boolean PRJ = true;
	protected int nrows;
	protected int ncols;
	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	private Image backgroundImage = null;
	private boolean triedToLoadBg = false;

	private List<Avatar> avatars = new ArrayList<>();

	public View(Canvas canvas, IModel m_model2) {
		m_canvas = canvas;
		m_model = m_model2;
		nrows = m_model.nrows();
		ncols = m_model.ncols();

	}

	@Override
	public void focus(int px, int py) {
		// TODO Auto-generated method stub
		int w = m_canvas.getWidth();
		int h = m_canvas.getHeight();

		float cellW = (float) w / ncols;
		float cellH = (float) h / nrows;

		m_focusCol = (int) (px / cellW);
		m_focusRow = (int) (py / cellH);
		mouseX = px;
		mouseY = py;

		// m_canvas.repaint();

	}

	@Override
	public void paint(oop.graphics.Canvas _canvas, Graphics2D g) {
		
		//STATS
		
		long paintStart = System.nanoTime();

		
		int w = m_canvas.getWidth();
		int h = m_canvas.getHeight();
		float cellW = (float) w / ncols;
		float cellH = (float) h / nrows;

		if (gameOver) {
			drawBg(m_canvas.getWidth(), m_canvas.getHeight(), g);
			g.setColor(Color.RED);
			g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
			g.drawString("GAME OVER !", w / 2 - 120, h / 2);

			return;
		}

		if (winner) {
			drawBg(m_canvas.getWidth(), m_canvas.getHeight(), g);
			g.setColor(Color.GREEN);
			g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 40));
			g.drawString("GAME WON !", w / 2 - 120, h / 2);

			return;

		}

		int w_pix = m_canvas.getWidth();
		int h_pix = m_canvas.getHeight();
		float scaleW = ((float) w_pix) / (ncols * cellW_cm);
		float scaleH = ((float) h_pix) / (nrows * cellH_cm);

		drawBg(w_pix, h_pix, g);

		AffineTransform old = g.getTransform();

		if (zoom == 1.0f) { // (Affiche toute la carte)
			drawGrid(w_pix, h_pix, g, scaleH, scaleW); // Zoom = 1
			g.scale(scaleW, scaleH);

		} else { // Zoom
			Player player = m_model.player();
			float px_cm = player.x();
			float py_cm = player.y();

			float sizeMapW = ncols * cellW_cm * scaleW * zoom;
			float sizeMapH = nrows * cellH_cm * scaleH * zoom;

			float cx = w_pix / 2f;
			float cy = h_pix / 2f;
			float transX = cx - (px_cm * scaleW * zoom);
			float transY = cy - (py_cm * scaleH * zoom);

			float minTransX = w_pix - sizeMapW;
			float minTransY = h_pix - sizeMapH;

			transX = Math.max(Math.min(transX, 0), minTransX);
			transY = Math.max(Math.min(transY, 0), minTransY);

			g.translate(transX, transY);

			// Zoom Fix
			drawGrid((int) (w_pix * zoom), (int) (h_pix * zoom), g, scaleH * zoom, scaleW * transY * zoom);
			drawGrid((int) (w_pix * zoom), (int) (h_pix * zoom), g, scaleH * transX * zoom, scaleW * zoom);

			g.scale(zoom, zoom);
			g.scale(scaleW, scaleH);

		}

		/*
		 * 
		 * Iterator<Entity> it = m_model.entities(); while (it.hasNext()) { Entity e =
		 * it.next(); if (e instanceof engine.model.Projectile) {
		 * engine.model.Projectile prj = (engine.model.Projectile) e; px =
		 * Math.round((prj.x() + 0.5f) * scaleW); py = Math.round((prj.y() + 0.5f) *
		 * scaleH);
		 * 
		 * float rayon = prj.rayon();
		 * 
		 * g.setColor(Color.YELLOW); g.fillOval(px, py, (int) rayon, (int) rayon);
		 * 
		 * } }
		 */

		specialized_paint(g);

		for (Avatar a : avatars) {
			a.render(g, cellW, cellH);
		}
		
		 
		Player player = m_model.player();
		int coinCount = player.getcoinsCount();
		int fruitsCount = player.getfruitsCount();
		int score = (coinCount + (10 * fruitsCount));

		g.setColor(Color.YELLOW); // Choose a color for the text
		g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 15)); // Choose a font and size

		g.setTransform(old);
		g.drawString("Score: " + score, w - (cellW_cm), 20); // Draw the coin count at position (10, 30)
		drawLives(g, m_model.player().player_max_lives, 20, 10);
		drawBullets(g, m_model.player().projectile_ammo, 20, 20);
		
		g.setColor(Color.WHITE);
		int statsY = 40;
		


		// drawGrid(w_pix, h_pix, g, scaleH*zoom , scaleW*zoom );
		
		long paintEnd = System.nanoTime();
		Game.instance.paintTimes.add(paintEnd - paintStart);
		if (Game.instance.paintTimes.size() > 100) Game.instance.paintTimes.remove(0);
		
		if (debug) {
			g.drawString("Tick: " + Game.getStats(Game.instance.tickTimes), 10, statsY); statsY += 20;
			g.drawString("Paint: " + Game.getStats(Game.instance.paintTimes), 10, statsY); statsY += 20;
		}

	}

	protected abstract void specialized_paint(Graphics2D g);

	protected abstract void drawLives(Graphics2D g, int lives, int x, int y);
	
	protected abstract void drawBullets(Graphics2D g, int lives, int x, int y);

	private void drawBg(int w_pix, int h_pix, Graphics2D g) {

		g.setColor(new Color(25, 25, 35));
		g.fillRect(0, 0, w_pix, h_pix);
		g.fillRect(0, 0, w_pix, h_pix);
	}

	private void drawGrid(int w_pix, int h, Graphics2D g, float scale_H, float scale_W) {

		if (debug) {
			g.setColor(Color.GREEN);
			for (int r = 0; r <= nrows; r++) {
				int y_pix = Math.round(r * cellH_cm * scale_H);
				g.drawLine(0, y_pix, w_pix, y_pix);
			}
			for (int c = 0; c <= ncols; c++) {
				int x = Math.round(c * cellW_cm * scale_W); // FIXME scale
				g.drawLine(x, 0, x, h);
			}
		}
	}

	public void Z(int acc) {
		if (acc == 0) {
			if (zoom < 5) {
				zoom *= 1.1; // ZoomIN
			}
		} else if (acc == 1) {
			if (zoom > 1) {
				zoom /= 1.1; // ZoomOut
			}
		} else {
			zoom = 1;
		}

	}

	public void debugging_opt(int x) {
		if (x == 0) {
			debug = false;
		} else {
			debug = true;
		}
	}

	public int[] Mouse() {
		int[] m = new int[2];
		m[0] = mouseX;
		m[1] = mouseY;
		return m;
	}

	public Canvas canvas() {
		return m_canvas;
	}

	public int mX() {
		return mouseX;
	}

	public int mY() {
		return mouseY;
	}

	public void set_playerDeath(boolean b) {
		playerDeath = b;
	}

	public void set_gameOver(boolean b) {
		gameOver = b;
		if (b) {
			SoundPlayer.stopMusic(); // Static method to stop music when the game is over
		}
	}
	
	public void set_gameWon(boolean b) {
		winner = b;
		if (b) {
			SoundPlayer.stopMusic(); // Static method to stop music when the game is over
		}
	}
	
	
	

	public void birth(Entity e) {
		Avatar a;
		 
		if (e instanceof Player) {
			a = new PlayerAvatar(e);
		} else if (e instanceof Obstacle) {
			a = new ObstacleAvatar(e);
		} else if (e instanceof Projectile) {
			a = new ProjectileAvatar(e);
		} else if (e instanceof Apple) {
			a = new AppleAvatar(e);
		} else if (e instanceof Strawberry) {
			a = new StrawberryAvatar(e);
		} else if (e instanceof Dot) {
			a = new DotAvatar(e);
		} else if (e instanceof PacGum) {
			a = new PacGumAvatar(e);
		} else if (e instanceof Ghostchase) {
			a = new GhostchaseAvatar(e);
		} else if (e instanceof GhostRun) {
			a = new GhostRunAvatar(e);
		} else if (e instanceof GhostWallFollow) {
			a = new GhostWallFollowAvatar(e);
		} else if (e instanceof GhostWandering) {
			a = new GhostWanderingAvatar(e);
		} else if (e instanceof Adversary) {
			a = new AdversaryAvatar(e);
		}

		else {
			a = new ItemAvatar(e);
		}
		e.avatar = a;

		avatars.add(a);
	}

	public void death(Entity e) {
		avatars.remove(e.avatar);
	}

}
