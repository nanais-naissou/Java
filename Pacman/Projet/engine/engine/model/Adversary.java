package engine.model;

import java.util.List;

import brain.Bot;

import engine.ICategory;

import engine.IView;

import fsm.Wandering2;
import game.Stunts.AdversaryStunt;

public class Adversary extends Entity {
	protected int Health;
	protected int MaxHealth = 3;
	public IView m_view;
	public boolean isDead;
	public boolean isInPrison;
	public boolean Killed;
	protected long lastRespawnTime;// Temps du respawn
	private List<int[]> pathToCage = null;
	private int pathIndex = 0;
	public long waitBeforeExitTime = -1;
	public Bot tempBot;

	public Adversary(Model m, int r, int c, int o, IView m_view) {
		super(m, r, c, o);
		this.stunt = new AdversaryStunt(m, this);
		this.isInPrison = true;
		this.lastRespawnTime = -1;
		
//		this.bot = (this.getisDead() && !this.inHisInitPos)? 
//				new WalkerBot(m, this) :  
//					((m.player().bot.PacGum)? 
//							new WalkerBot(m, this) : 
//								new WalkerBot(m, this));

		// SafeBot(m, this);
		//this.bot = new Wandering2(this, m);
		
		this.Health = MaxHealth;
		this.m_view = m_view;
		this.isDead = false;
		this.isInPrison = true;
		this.Killed = false;
		

	}

	@Override
	public ICategory category() {
		return game.Categories.ADVERSARY;
	}

public void getOut() {
		
	    List<int[]> doors = m_model.getDoorCoords();
	    if (!this.isInPrison || doors.isEmpty())
	        return;

	    for (int i = 0; i < m_model.nrows(); i++)
	        for (int j = 0; j < m_model.ncols(); j++)
	        	 ((Model) (m_model)).m_grid[i][j].remove(this);

	   
	    int[] door = doors.get(0);
	    int row = door[0] + 1;
	    int col = door[1];

	    this.m_row = row;
	    this.m_col = col;
	    this.SetCelltoMeter(row, col);
	  

	  
	    this.isInPrison = false;
	    
	    if (this.bot==null) {
	    		
	    	  this.bot = this.tempBot;
	    	  System.out.println(this.bot);
	    }
	   
	    ((Model) (m_model)).addAt(this);
	  
	    m_view.birth(this);
	    
	   


	    

	    System.out.println(this + " IS NOW OUT:  (" + this.row() + ", " + this.col() + ")");
	    

	}

	public void callRespawn(Adversary advR) {
		// ((Model) (m_model)).m_view.death(advR);
		// ((Model) (m_model)).m_adversaries.remove(advR);

		((Model) (m_model)).respawnList.add(advR);

		advR.lastRespawnTime = System.currentTimeMillis();
	}

	public void releaseAfterRespawn() {
		if (this.lastRespawnTime == -1) {
			this.getOut();
		}

		long timeSinceRespawn = System.currentTimeMillis() - this.lastRespawnTime;
		if (timeSinceRespawn >= 5000) {
			if (this.isInPrison) {
				this.getOut();
			}
		}
	}

	public int getHealth() {
		return Health;
	}

	public void setHealth(int health) {
		this.Health = health;
	}

	public int getMaxHealth() {
		return MaxHealth;
	}

	public IView m_view() {
		return m_view;
	}

	public void setisDead(boolean d) {
		this.isDead = d;
	}

	public boolean getisDead() {
		return isDead;
	}

	public void setBot(Bot b) {
		this.bot = b;

	}
	
	public void setPathToCage(List<int[]> path) {
		this.pathToCage = path;
		this.pathIndex = 0;
	}

	public List<int[]> getPathToCage() {
		return this.pathToCage;
	}

	public void followPathToCageFluid(Model model) {
	    if (pathToCage == null || pathIndex >= pathToCage.size()) return;

	    int[] target = pathToCage.get(pathIndex);
	    int tRow = target[0], tCol = target[1];

	    // Centre de la cellule cible en cm
	    float targetX = tCol * model.cellW_cm() + model.cellW_cm() / 2f;
	    float targetY = tRow * model.cellH_cm() + model.cellH_cm() / 2f;

	    // Position actuelle en cm
	    float currX = this.x();
	    float currY = this.y();

	    float dx = targetX - currX;
	    float dy = targetY - currY;
	    float dist = (float)Math.sqrt(dx * dx + dy * dy);

	    float speed = 4.5f; // Ajuste la vitesse ici (cm par tick)

	    if (dist < 2.0f) { // Si assez proche du centre de la cellule (epsilon en cm)
	        // Snap au centre
	        this.m_x_cm = targetX;
	        this.m_y_cm = targetY;
	        this.m_row = tRow;
	        this.m_col = tCol;
	        pathIndex++;
	        if (pathIndex >= pathToCage.size()) {
	            // Arrivé à destination, réinitialise tout
	            this.m_row = this.spawnRow;
	            this.m_col = this.spawnCol;
	            this.SetCelltoMeter(this.spawnRow, this.spawnCol);

	            this.setisDead(false);
	            this.inHisInitPos = true;
	            this.isInPrison=true;
	            this.Killed = false;
//	            ((Model)m_model).m_adversaries.add(this);
	            this.pathToCage = null;
	            this.pathIndex = 0;
	            System.out.println("Ghost respawned at original spawn location: (" + spawnRow + ", " + spawnCol + ")");
	            return;
	        }
	    } else {
	        // Avance vers le centre de la cellule cible (normalisé à "speed")
	        float vx = dx / dist * speed;
	        float vy = dy / dist * speed;
	        ((Model)m_model).moveFluid(this, vx, vy);
	    }
	}
	
//	public void followPathToCage() {
//	    if (pathToCage == null || pathIndex >= pathToCage.size()) return;
//
//	    int[] target = pathToCage.get(pathIndex);
//	    int tRow = target[0], tCol = target[1];
//
//	    if (this.row() == tRow && this.col() == tCol) {
//	        pathIndex++;
//	        if (pathIndex >= pathToCage.size()) {
//	          
//
//	         
//	            this.m_row = this.spawnRow;
//	            this.m_col = this.spawnCol;
//	            this.SetCelltoMeter(this.spawnRow, this.spawnCol); 
//
//	            //((Model) m_model).addAt(this);
//
//	          
//	            this.setisDead(false);
//	            this.inHisInitPos = true;
//	            this.isInPrison=true;
//	            this.Killed = false;
//	            ((Model)m_model).m_adversaries.add(this);
//	            this.pathToCage = null;
//	            this.pathIndex = 0;
//	            
//
//	            System.out.println("Ghost respawned at original spawn location: (" + spawnRow + ", " + spawnCol + ")");
//	            return;
//	        }
//	        
//	    }
//
//	    int dRow = Integer.compare(tRow, this.row());
//	    int dCol = Integer.compare(tCol, this.col());
//
//	    this.move(dRow, dCol); 
//	   
//	}

	
	
	
	
	public boolean isReadyToExit() {
	    return waitBeforeExitTime > 0 && (System.currentTimeMillis() - waitBeforeExitTime) >= 5000;
	}
	public void handleRespawnAtSpawn() {
	    if (this.inHisInitPos && this.isInPrison && this.waitBeforeExitTime > 0) {
	        long elapsed = System.currentTimeMillis() - waitBeforeExitTime;
	        if (elapsed >= 5000) {
	            this.getOut();
	            this.waitBeforeExitTime = -1;
	            System.out.println("👻 Ghost (was dead) exits after 5s at spawn");
	        }
	    }
	}



}
