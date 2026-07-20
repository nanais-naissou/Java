package engine.model;

import java.util.ArrayList; 
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import brain.Category;
import brain.MainCategories;
import engine.IView;
import engine.view.View;
import game.Generator;
import game.Entities.Apple;
import game.Entities.Dot;
import game.Entities.PacGum;
import game.Entities.Strawberry;
import oop.graphics.Canvas;

public class Model implements engine.IModel, Iterable<Entity> {
	private int m_ncols, m_nrows;
	public List<Entity>[][] m_grid;
	Player m_player;
	List<Entity> m_entities;
	public IView m_view;
	private Config m_conf;
	public int cellW_cm = 100;
	public int cellH_cm = 100;
	boolean debugg = false;
	private List<int[]> doorCoords = new ArrayList<>();
	public List<Adversary> m_adversaries = new ArrayList<>();



	@SuppressWarnings("unchecked")
	public Model(int nr, int nc) {
		m_ncols = nc;
		m_nrows = nr;
		m_grid = (List<Entity>[][]) new ArrayList[nr][nc];

		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				m_grid[i][j] = new ArrayList<>();
			}
		}

		m_entities = new LinkedList<Entity>();
	}

	/*
	 * A callback from the constructor of an entity to notify this model of a new
	 * entity to add to this model.
	 */

	public void addAt(Entity e) {
	    int r = e.row();
	    int c = e.col();

	
	    if (!m_grid[r][c].contains(e)) {
	        m_grid[r][c].add(e);
	    }

	    
	   
	    if (!m_entities.contains(e)) {
	        m_entities.add(e);
	    }

	   
	    if (e instanceof Adversary) {
	        if (!m_adversaries.contains(e)) {
	        	
	            m_adversaries.add((Adversary) e);
	           
	        }
	    }

	    if (e instanceof Player) {
	        m_player = (Player) e;
	    }
	}


	/*
	 * Move the given entity from its current location by adding the given number of
	 * rows and columns to its current location.
	 */
	public boolean move(Entity e, int nrows, int ncols) {
		int oldRow = e.row();
		int oldCol = e.col();

		int newRow = oldRow + nrows;
		int newCol = oldCol + ncols;

		if (m_conf.tore) {
			newRow = normalize(newRow, m_nrows);
			newCol = normalize(newCol, m_ncols);
		} else {
			if (newRow < 0) {
				newRow = 0;
			} else if (newRow >= m_nrows) {
				newRow = m_nrows - 1;
			}
			if (newCol < 0) {
				newCol = 0;
			} else if (newCol >= m_ncols) {
				newCol = m_ncols - 1;
			}
		}
		System.out.println("JJ");

		if (!check(MainCategories.OBSTACLE, m_grid[newRow][newCol])/* m_grid[newRow][newCol] == null */) {

			m_grid[oldRow][oldCol].remove(e);
			e.m_row = newRow;
			e.m_col = newCol;
			m_grid[newRow][newCol].add(e);
			e.at(newRow, newCol);
			
			e.SetCelltoMeter(newRow, newCol);
			return true;
		}
		return false;
	}

//	//under dev
//	public void followCursor(int mouseX, int mouseY, Player p, Canvas canvas) {
//		assert (p != null);
//
//		viseCursor(mouseX, mouseY, p, canvas);
//
//		float dx = mouseX - p.x(); // Difference in cm
//		float dy = mouseY - p.y(); // Difference in cm
//
//		float distance = (float) Math.sqrt(dx * dx + dy * dy);
//
//		if (distance > 0) {
//
//			p.moveFluid(dx, dy); 
//		}
//	}

	public double viseCursor(int mouseX, int mouseY, Player p, Canvas canvas) {
		int w = canvas.getWidth();
		int h = canvas.getHeight();
		int nrows = nrows();
		int ncols = ncols();

		float cellW = (float) w / ncols;
		float cellH = (float) h / nrows;

		// Convert mouse position in pixels to cm
		float mouseXGrid = (float) mouseX * cellW_cm / cellW;
		float mouseYGrid = (float) mouseY * cellH_cm / cellH;

		float dx = mouseXGrid - p.x();
		float dy = mouseYGrid - p.y();

		double angle = Math.toDegrees(Math.atan2(dy, dx));
		p.face((int) angle);
		return angle;
	}

	public boolean moveFluid(Entity e, float dx, float dy) {
		float m_x_tmp = e.x() + dx;
		float m_y_tmp = e.y() + dy;
		float size = cellW_cm * 0.8f;
		float halfSize = size / 2f;

		if (m_conf.tore) {
			m_x_tmp = normalizeFluid(m_x_tmp, m_ncols * cellW_cm);
			m_y_tmp = normalizeFluid(m_y_tmp, m_nrows * cellH_cm);
		} else {
			if (m_x_tmp - halfSize < 0)
				m_x_tmp = halfSize;
			if (m_x_tmp + halfSize >= m_ncols * cellW_cm)
				m_x_tmp = m_ncols * cellW_cm - halfSize - 1;
			if (m_y_tmp - halfSize < 0)
				m_y_tmp = halfSize;
			if (m_y_tmp + halfSize >= m_nrows * cellH_cm)
				m_y_tmp = m_nrows * cellH_cm - halfSize - 1;
		}

		float[] checkX = { m_x_tmp - halfSize + 1, m_x_tmp + halfSize - 1 };
		float[] checkY = { m_y_tmp - halfSize + 1, m_y_tmp + halfSize - 1 };

		for (float cx : checkX) {
			for (float cy : checkY) {
				int col = normalize((int) (cx / cellW_cm), m_ncols);
				int row = normalize((int) (cy / cellH_cm), m_nrows);
				
				//Verifie si Porte 
				if (e instanceof Player || (e instanceof Adversary && !(((Adversary) e).isDead)  )) {
					for (int[] door : doorCoords) {
				        if (door[0] == row && door[1] == col) {
				        
				        	if (debugg)
			                    System.out.println("Door cell : Player movement blocked");
			                return false;
				        }
				        	
				    }
				}
				
				if (check(MainCategories.OBSTACLE, m_grid[row][col])) {
					return false ;
				}
			}
		}

		int newRow = normalize((int) (m_y_tmp / cellH_cm), m_nrows);
		int newCol = normalize((int) (m_x_tmp / cellW_cm), m_ncols);

		m_grid[e.m_row][e.m_col].remove(e);
		m_grid[newRow][newCol].add(e);

		e.m_row = newRow;
		e.m_col = newCol;
		e.m_x_cm = m_x_tmp;
		e.m_y_cm = m_y_tmp;
		

		if (debugg) {
			System.out.println("[Model] moveFluid: after (" + e.x() + ", " + e.y() + ")");
		}
		
		return true ;
	}
	

	private boolean check(Category c, List<Entity> cell) {
		for (Entity e : cell) {
			if (e.category() == c) {
				return true;
			}
		}
		return false;
	}

	public boolean moveFluide(Entity e, float speed) {
		float radians = (float) Math.toRadians(e.m_orientation);
		float dx = speed * (float) Math.cos(radians);
		float dy = speed * (float) Math.sin(radians);
		float m_x_tmp = e.x() + dx;
		float m_y_tmp = e.y() + dy;

		float size = cellW_cm * 0.8f;
		float halfSize = size / 2f;

		if (m_conf.tore) {
			m_x_tmp = normalizeFluid(m_x_tmp, m_ncols * cellW_cm);
			m_y_tmp = normalizeFluid(m_y_tmp, m_nrows * cellH_cm);
		} else {
			if (m_x_tmp - halfSize < 0)
				m_x_tmp = halfSize;
			if (m_x_tmp + halfSize >= m_ncols * cellW_cm)
				m_x_tmp = m_ncols * cellW_cm - halfSize - 1;
			if (m_y_tmp - halfSize < 0)
				m_y_tmp = halfSize;
			if (m_y_tmp + halfSize >= m_nrows * cellH_cm)
				m_y_tmp = m_nrows * cellH_cm - halfSize - 1;
		}

		float[] checkX = { m_x_tmp - halfSize + 1, m_x_tmp + halfSize - 1 };
		float[] checkY = { m_y_tmp - halfSize + 1, m_y_tmp + halfSize - 1 };

		for (float cx : checkX) {
			for (float cy : checkY) {
				int col = normalize((int) (cx / cellW_cm), m_ncols);
				int row = normalize((int) (cy / cellH_cm), m_nrows);
				
				
				//Verifie si Porte 
				
				if (e instanceof Player || (e instanceof Adversary && !(((Adversary) e).isDead)  )) {
					for (int[] door : doorCoords) {
				        if (door[0] == row && door[1] == col) {
				        	
				        	if (debugg)
			                    System.out.println("Door cell : Player movement blocked");
			                return false;
				        }
				        	
				    }
				}
				
				
				if (check(MainCategories.OBSTACLE, m_grid[row][col])) {
					return false;
				}
			}
		}
		int newRow = normalize((int) (m_y_tmp / cellH_cm), m_nrows);
		int newCol = normalize((int) (m_x_tmp / cellW_cm), m_ncols);
		m_grid[e.m_row][e.m_col].remove(e);
		m_grid[newRow][newCol].add(e);
		e.m_row = newRow;
		e.m_col = newCol;
		e.m_x_cm = m_x_tmp;
		e.m_y_cm = m_y_tmp;
		e.at(newRow, newCol);
		if (debugg) {
			System.out.println("[Model] moveFluide: after move (" + e.x() + ", " + e.y() + ")");
		}

		return true;
	}

	/*
	 * Normalize a number back to the range [0,lengh[
	 */
	private int normalize(int n, int length) {
		n = n % length;
		if (n < 0)
			n += length;
		return n;
	}

	public float normalizeFluid(float value, int max) {
		float r = value % max;
		if (r < 0)
			r += max;
		return r;
	}

	public List<Entity> entitiesAt(int r, int c) {

		return m_grid[normalize(r, m_nrows)][normalize(c, m_ncols)];
	}

	public Iterator<Entity> iterator() {
		return m_entities.iterator();
	}

	public Config config() {
		return m_conf;
	}

	public void config(Config c) {
		m_conf = c;
	}

	public Player player() {
		return m_player;
	}

	public int ncols() {
		return m_ncols;
	}

	public int nrows() {
		return m_nrows;
	}

	public int cellW_cm() {
		return cellW_cm;
	}

	public int cellH_cm() {
		return cellH_cm;
	}

	public IView m_view() {
		return m_view;
	}

	public List<Entity> toRemove = new ArrayList<>();

	public void removeEntity(Entity e) {
		if (!toRemove.contains(e)) {
			toRemove.add(e);
		}
	}

	public void purgeRemovedEntities() {
		for (Entity e : toRemove) {
			int row = e.row();
			int col = e.col();
			 for (int i = 0; i <nrows(); i++)
			        for (int j = 0; j < ncols(); j++)
			        	 m_grid[i][j].remove(e);
			m_entities.remove(e);
			if (e instanceof Adversary) {
				m_adversaries.remove((Adversary) e);
			}
		}
		
		toRemove.clear();
	}
	
	
	public List<Entity> respawnList = new ArrayList<>();
	
	public void respawn(Entity e) {
	  
	    for (int i = 0; i < m_nrows; i++)
	        for (int j = 0; j < m_ncols; j++)
	            m_grid[i][j].remove(e);

	    e.m_row = e.spawnRow;
	    e.m_col = e.spawnCol;
	    e.SetCelltoMeter(e.spawnRow, e.spawnCol);

	   

	    if (e instanceof Adversary) {
	        ((Adversary) e).setHealth(((Adversary) (e)).MaxHealth);
	        ((Adversary) (e)).setisDead(false);
	        ((Adversary) e).isInPrison = true;
	        ((Adversary) (e)).Killed = false;
	       
	        
	        if (!m_adversaries.contains((Adversary)e)) {
	        	System.out.println("HERE IN CAGE");
	        	m_adversaries.add((Adversary) e);
	        
	        }
	        
	    } else {
	        m_view.birth(e);
	        addAt(e);
	    }
	}


	
	

	
	public void respawnGhosts(Entity e, int row, int col) {
	   // m_grid[e.m_row][e.m_col].remove(e); 
	    e.m_row = row;
	    e.m_col = col;
	    e.SetCelltoMeter(row, col);
	    addAt(e);
	  
	    m_view.birth(e);
	    

	}
	
	
	boolean inHisInitPos(Entity e) {
		if (e.m_row==e.spawnRow && e.m_col==e.spawnCol) {
			return true;
		}
		return false;
	}
	
	public void purgeRespawnEntities() {
	    for (Entity e : respawnList) {
	        respawn(e);
	    }
	    for (Object[] arr : respawnListGhosts) {
	        Entity e = (Entity) arr[0];
	        int row = (Integer) arr[1];
	        int col = (Integer) arr[2];
	        respawnGhosts(e, row, col);
	    }
	    respawnList.clear();
	    respawnListGhosts.clear();
	}


	@Override
	public void register(IView v) {
		m_view = v;

	}

	@Override
	public void unregister(IView v) {
		// TODO Auto-generated method stub

	}

	public int dotCount() {
		int count = 0;
		for (Entity e : m_entities) {
			if (e instanceof Dot) {
				count++;
			}
		}
		return count;
	}

	public int fruitsCount() {
		int count = 0;
		for (Entity e : m_entities) {
			if (e instanceof Strawberry || e instanceof Apple) {
				count++;
			}
		}
		return count;
	}

	public int PacGumCount() {
		int count = 0;
		for (Entity e : m_entities) {
			if (e instanceof PacGum) {
				count++;
			}
		}
		return count;
	}
	

	public void setDoorCoords(List<int[]> coords) {
	    this.doorCoords = coords;
	}
	
	public List<int[]> getDoorCoords() {
		return doorCoords;
	}
	
	
	public void followPath(Entity e, List<int[]> path) {
		if (path == null || path.isEmpty()) return;

		// Trouve la position actuelle dans le path
		int row = e.row();
		int col = e.col();

		for (int i = 0; i < path.size(); i++) {
			int[] pos = path.get(i);
			if (pos[0] == row && pos[1] == col && i + 1 < path.size()) {
				int[] next = path.get(i + 1);
				int dy = next[0] - row;
				int dx = next[1] - col;

				// Appelle moveFluid avec dx/dy en pixel (float)
				e.move( dx * cellW_cm, dy * cellH_cm);
				return;
			}
		}
	}

	
	
	
	

}
