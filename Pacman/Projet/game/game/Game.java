package game;

import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import engine.controller.Controller;
import engine.IModel;
import engine.IModel.Config;
import engine.IView;
import engine.model.Adversary;
import engine.model.Entity;
import engine.model.Item;
import engine.model.Model;
import engine.model.Obstacle;
import engine.model.Player;
import engine.view.View;
import fsm.Chasing;
import game.Entities.Apple;
import game.Entities.Dot;
import game.Entities.GhostWallFollow;
import game.Entities.GhostWandering;
import game.Entities.Ghostchase;
import game.Entities.PacGum;
import game.Entities.Projectile;
import game.Entities.Strawberry;
import oop.graphics.Canvas;

public class Game {
	private Canvas m_canvas;
	private IModel m_model;
	private IView m_view;
	private Controller m_controller;
	boolean debugg = false;
	private long lastGhostRelease;

	public List<Long> tickTimes = new ArrayList<>();

	public List<Long> paintTimes = new ArrayList<>();
	public List<Long> fpsTimes = new ArrayList<>();

	Generator gen;
	public static Game instance;


	boolean mode; // true = hard, false = easy

	List<Projectile> m_projectiles = new ArrayList<>();

	Game(Canvas canvas, int nrows, int ncols, boolean mode) {

		// Pass mode as constructor argument
		this.mode = mode;

		System.out.println(" Mode Activated: " + (mode ? "HARD" : "EASY"));

		// Config conf = new Config();
		// conf.tore = false; // Pac-Man classic is not toric!

		Config conf = new Config();
		conf.tore = ConfigLoader.loadTore(); //	 this reads from Config.txt
		System.out.println("Tore mode = " + conf.tore);
		conf.mode = mode;

		Game.instance = this;


		this.m_canvas = canvas;
		this.gen = new Generator(nrows, ncols, conf.tore);
		char[][] maze = gen.getMaze();
		Generator.STATIC_MAZE = maze;
		gen.printMaze();
		int mapRows = maze.length;
		int mapCols = maze[0].length;
		this.lastGhostRelease = System.currentTimeMillis();

		if (mapRows != nrows || mapCols != ncols) {
			// System.out.println("[WARN] Ignoring supplied nrows/ncols, using Pac-Man map
			// size: " + mapRows + "x" + mapCols);
		}

		m_model = new Model(mapRows, mapCols);
		m_model.config(conf);
		m_view = new View0(canvas, m_model);

		m_model.register(m_view);
		m_model.setDoorCoords(gen.getDoors());

		// Fill entities as shown before
		for (int row = 0; row < mapRows; row++) {
			for (int col = 0; col < mapCols; col++) {
				char c = maze[row][col];
				switch (c) {
				case '#':
					Obstacle obs = new Obstacle((Model) m_model, row, col, 0);
					m_model.addAt(obs);
					m_view.birth(obs);
					break;
				case 'P':
					Player p = new Player((Model) m_model, row, col, 0);
					m_model.addAt(p);
					m_view.birth(p);
					p.getBot().setView((View) m_view);

					break;

				/*
				 * IMPORTANT Lors de la création des ghosts, ils sont d'abord ajoutés uniquement
				 * à la liste des adversaires 'm_adversaries' dans Model. Leur insertion dans la
				 * grille et dans la liste 'm_entities' se fait uniquement lors de leur sortie
				 * de la prison, dans la méthode 'getOut' de la classe Adversary.
				 */

				case 'G':
					if (mode) {
						System.out.println("You are playing in hard mode");

						Ghostchase GD = new Ghostchase((Model) m_model, row, col, 0, m_view);
						((Model) (m_model)).m_adversaries.add(GD);
						m_view.birth(GD);

						break;
					} else {

						GhostWandering GW = new GhostWandering((Model) m_model, row, col, 0, m_view);
						((Model) (m_model)).m_adversaries.add(GW);
						m_view.birth(GW);


					}

					break;

				case 'g':
					GhostWallFollow GWF = new GhostWallFollow((Model) m_model, row, col, 0, m_view);
					// m_model.addAt(GWF);
					((Model) (m_model)).m_adversaries.add(GWF);
					m_view.birth(GWF);
					// break;

					// Adversary ghost = new Adversary((Model) m_model, row, col, 0, m_view);
					// m_model.addAt(ghost);
					// m_view.birth(ghost);

					// ghost.setBot(new Chasing((Model) m_model, ghost));
					break;
				case '.':
					Dot dot = new Dot((Model) m_model, row, col, 0);
					m_model.addAt(dot);
					m_view.birth(dot);
					break;
				case 'A':
					Apple apple = new Apple((Model) m_model, row, col, 0);
					m_model.addAt(apple);
					m_view.birth(apple);
					break;
				case 'S':
					Strawberry strawberry = new Strawberry((Model) m_model, row, col, 0);
					m_model.addAt(strawberry);
					m_view.birth(strawberry);
					break;
				case 'C':
					PacGum pacgum = new PacGum((Model) m_model, row, col, 0);
					m_model.addAt(pacgum);
					m_view.birth(pacgum);
					break;
				}
			}
		}

		m_controller = new Controller0(canvas, m_model, m_view);
		new Ticker(this);
	}

	public void paint(Canvas canvas, Graphics2D g) {
		m_view.paint(canvas, g);
	}

	public boolean isInGhostHouse(Entity e, Generator gen) {
		int minRow = gen.houseY;
		int maxRow = gen.houseY + gen.HOUSE_H - 1;
		int minCol = gen.houseX;
		int maxCol = gen.houseX + gen.HOUSE_W - 1;
		return (e.row() >= minRow && e.row() <= maxRow && e.col() >= minCol && e.col() <= maxCol);
	}

	public List<int[]> findPathToNearestDoor(Entity entity) {
		List<int[]> doors = gen.getDoors();
		char[][] maze = Generator.STATIC_MAZE;
		int rows = maze.length;
		int cols = maze[0].length;
		boolean[][] visited = new boolean[rows][cols];
		int[][][] parent = new int[rows][cols][2];

		Queue<int[]> queue = new LinkedList<>();
		int startRow = entity.row();
		int startCol = entity.col();
		queue.add(new int[] { startRow, startCol });
		visited[startRow][startCol] = true;
		parent[startRow][startCol] = new int[] { -1, -1 };

		int[][] directions = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 } };

		while (!queue.isEmpty()) {
			int[] current = queue.poll();
			int y = current[0], x = current[1];

			for (int[] dir : directions) {
				int ny = y + dir[0];
				int nx = x + dir[1];

				if (ny >= 0 && ny < rows && nx >= 0 && nx < cols && !visited[ny][nx]) {
					if (maze[ny][nx] != '#') {
						visited[ny][nx] = true;
						parent[ny][nx] = new int[] { y, x };
						queue.add(new int[] { ny, nx });
						for (int[] door : doors) {
							if (ny == door[0] && nx == door[1]) {
								LinkedList<int[]> path = new LinkedList<>();
								int[] p = { ny, nx };
								while (p[0] != -1 && p[1] != -1) {
									path.addFirst(p);
									p = parent[p[0]][p[1]];
								}
								return path;
							}
						}
					}
				}
			}
		}
		return null; // Aucun chemin trouvé
	}

	public static String getStats(List<Long> times) {
		if (times.isEmpty())
			return "N/A";
		long min = Long.MAX_VALUE, max = Long.MIN_VALUE, sum = 0;
		for (long t : times) {
			if (t < min)
				min = t;
			if (t > max)
				max = t;
			sum += t;
		}
		long avg = sum / times.size();

		return String.format("min: %.2f ms, max: %.2f ms, avg: %.2f ms", min / 1e6, max / 1e6, avg / 1e6);
	}

	public static String getFPS(List<Long> times) {
		if (times.isEmpty())
			return "N/A";
		double sum = 0;
		for (long t : times)
			sum += t;
		double avg = sum / times.size();
		return String.format("FPS: %.2f", 1e9 / avg);
	}

	public void tick(int elapsed) {
		if (m_controller instanceof Controller0) {
			Controller0 c0 = (Controller0) m_controller;
//			System.out.println("number of fruitsat start:" + gen.countFruits());
//			System.out.println("number of fruitsat now:" + m_model.fruitsCount());

			Player p = m_model.player();

			p.setcoinsCount(gen.countDots() - m_model.dotCount());
			p.setfruitsCount(gen.countFruits() - m_model.fruitsCount());
			if (p != null) {
				if (debugg) {
					System.out.println("[DEBUG] Game: Before tick, player action: " + p.stunt().currentAction());
				}
				p.stunt().tick(elapsed);

				p.stunt().tick(elapsed);

				p.checkPacGumTimeout();

				if (debugg) {
					System.out.println("[DEBUG] Game: After tick, player action: " + p.stunt().currentAction());
				}

				if (c0.movePlayer) {
					m_model.moveFluide(p, 5); // A Modif pour la collision
				}

				/* Ghost Exit */

				/*
				 * if (System.currentTimeMillis() - lastGhostRelease >= 5000) { for (Adversary
				 * adv : ((Model)m_model).m_adversaries) { if (adv.isInPrison) { adv.getOut();
				 * lastGhostRelease = System.currentTimeMillis(); break; } } }
				 * 
				 */

			}

			Iterator<Entity> it = m_model.iterator();
			while (it.hasNext()) {
				Entity e = it.next();
				if (e.stunt() != null) {
					if (e instanceof Adversary && ((Adversary) (e)).isInPrison) {

					}

					e.stunt().tick(elapsed);

				}
			}

			if (System.currentTimeMillis() - lastGhostRelease >= 5000) {
				for (Adversary adv : ((Model) m_model).m_adversaries) {
					if (adv.isInPrison && !adv.Killed) {

						adv.releaseAfterRespawn();
						lastGhostRelease = System.currentTimeMillis();

						break;
					}

				}
			}

			// Suppression réelle après tout le tick, donc plus de modification concurrente

			p.stunt().GhostRespawnAfterKill();

			((Model) m_model).purgeRemovedEntities();

			((Model) m_model).purgeRespawnEntities();

		}

	}

}
