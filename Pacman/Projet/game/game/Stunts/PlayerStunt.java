package game.Stunts;

import engine.model.Model;
import engine.model.Player;

import java.util.ArrayList;
import java.util.List;

import engine.model.Adversary;
import engine.model.Entity;
import engine.model.StuntFluide;
import game.Game;

public class PlayerStunt extends StuntFluide {
	private long lastKillTime = -1;
	private final long KILL_DELAY = 3000; // 3s
	private boolean isKillPending = false;
	public static List<Object[]> PendingRespawns ;
	
	public PlayerStunt(Model m, Entity e) {
		super(m, e);
		this.PendingRespawns = new ArrayList<>();
	}

	@Override
	public boolean move(float dx, float dy) {
		if (action == null) {
			if (debugg) {
				System.out.println("[DEBUG] PlayerStunt: Accepting move dx=" + dx + " dy=" + dy);
			}
			return super.move(dx, dy);
		} else {
			if (debugg) {
				System.out.println("[DEBUG] PlayerStunt: Move IGNORED, still busy with action: " + action);
			}
			return false;
		}
	}

	public void left() {
		e.face(180);
		move(-5f, 0f);

	}

	public void right() {
		e.face(0);
		move(5f, 0f);
	}

	public void up() {
		e.face(270);
		move(0f, -5f);
	}

	public void down() {
		e.face(90);
		move(0f, 5f);

	}
	public void pacmanKilledGhost(Adversary adv) {
		if (!m.respawnList.contains(adv)) {
 
         
                lastKillTime = System.currentTimeMillis(); // Met à jour le temps du dernier kill
                isKillPending = true; // Indique qu'un kill est en attente de délai
                
                // Met le fantôme à mort, mais n'appelle pas encore death
                adv.setisDead(true);
                m.removeEntity(adv);
                adv.Killed = true;
                
                PendingRespawns.add(new Object[] {adv, System.currentTimeMillis()});
            
            
        }
		

	}
	public void GhostRespawnAfterKill() {
		if (!PendingRespawns.isEmpty()) {
            for (int i = 0; i < PendingRespawns.size(); i++) {
                Object[] pending = PendingRespawns.get(i);
                Adversary adv = (Adversary) pending[0]; 
                long killTime = (long) pending[1];    
                
                // Délai de 3 s
                if (System.currentTimeMillis() - killTime >= KILL_DELAY) {
                	m.m_view.death(adv);
                    adv.callRespawn(adv); 
                    PendingRespawns.remove(i); 
                   
                    i--; 
                }
            }
        }
		
	}
	
	

	@Override
	public boolean move(int dRow, int dCol) {
		// Not supported
		return false;
	}

	@Override
	public void tick(int elapsed) {
		super.tick(elapsed);
		
		Player p = (Player) e;
		int row = e.row();
		int col = e.col();
		
		if (m.dotCount()==0) {m.m_view.set_gameWon(true);}
		
		// PacMan kill Ghosts & get Killed 
		
		for (Entity entityInCell : m.entitiesAt(row, col)) {
	
			if (entityInCell instanceof Adversary && !(p.bot.PacGum)) {
				
			    if (!m.respawnList.contains(e)) { // Pour ne pas passer plusieur fois
			        m.removeEntity(e);      
			        m.respawnList.add(e);       
			        m.m_view.death(e);
			        p.player_max_lives--;
			        
			        p.setdead(true);
			        
			        m.m_view.set_playerDeath(true);
			        System.out.println(" Killed");
			        System.out.println("PACMAN lost 1 life");
			        System.out.println("PACMAN has "+p.player_max_lives+" lives left!");
			        
			        if (p.player_max_lives==0) {
			        	m.m_view.set_gameOver(true);
			        }
			    }
			 
			
			}
			else if (entityInCell instanceof Adversary && (p.bot.PacGum)) {
			    Adversary advInCell = (Adversary) entityInCell;

			    advInCell.setisDead(true);
			    advInCell.inHisInitPos = false;
			    advInCell.Killed = true;
			    
			    
			    advInCell.bot=null;
			    
			    // Appel de la méthode Game pour calculer le chemin
			    List<int[]> path = Game.instance.findPathToNearestDoor(advInCell);
			    advInCell.setPathToCage(path);
			}

			
			if (!PendingRespawns.isEmpty()) {
	            for (int i = 0; i < PendingRespawns.size(); i++) {
	                Object[] pending = PendingRespawns.get(i);
	                Adversary adv = (Adversary) pending[0]; 
	                long killTime = (long) pending[1];    
	                
	                // Délai de 3 s
	                if (System.currentTimeMillis() - killTime >= KILL_DELAY) {
	                    //adv.callRespawn(adv); 
	                    PendingRespawns.remove(i); 
	                    System.out.println("Adversaire respawné : " + adv);
	                    i--; 
	                }
	            }
	        }
			
				
		}

		
		
		
		if (action == null) {
		
			// System.out.println("[DEBUG] PlayerStunt: Action finished, ready for new
			// action");
		} else {
			// System.out.println("[DEBUG] PlayerStunt: Ticking action: " + action);
		}
	}
}
