package game.Stunts;

import engine.model.Model;

import engine.model.StuntFluide;

import java.util.List;

import engine.model.Adversary;
import fsm.FleeingBot2;
import game.Game;

public class AdversaryStunt extends StuntFluide {
	private Adversary adv;

	public AdversaryStunt(Model m, Adversary adv) {
		super(m, adv);
		this.adv = adv;
	}

	@Override
	public boolean move(float dx, float dy) {
		return super.move(dx*100, dy*100);

	}

	public void left() {
		move(0, -1);
	}

	public void right() {
		move(0, 1);
	}

	public void up() {
		move(-1, 0);
	}

	public void down() {
		move(1, 0);
	}

	public void rotate(int angle) {
		super.rotate(angle);
	}

	
	
	@Override
	public void tick(int elapsed) {
	    super.tick(elapsed);

	    Adversary adv = (Adversary) e;


	    if (adv.getisDead()) {
	        adv.followPathToCageFluid(m);
	        adv.setHealth(adv.getMaxHealth());
	        return;
	    }
	    
	    /*
	     * CHANGEMENT DE BOT [MODE FleeingBot PACGUM]
	     * Le ticker des ghosts vérifie si le mode PacGum est ACTIVE
	     * PacGum=ACTIVE AND Ghost=Alive : Effectue le changement de bot [fuir Pacman] 
	     * PacGum!=ACTIVE : les ghosts récupèrent automatiquement leur bot initial.
	     */

	    if (m.player().bot.getPac() && !(adv.isInPrison) && !(adv.isDead)) {
	    	adv.bot = new FleeingBot2(adv,m);
	    }
	    if (!(m.player().bot.getPac()) && !(adv.isInPrison) && !(adv.isDead)){
	    	adv.bot = adv.tempBot;
	    }
	    
	    
	   
	    else if (adv.getHealth() <= 0) {			
				adv.setisDead(true);
				adv.inHisInitPos = false;
				adv.Killed = true;
				//adv.setHealth(adv.getMaxHealth());
			    
			    adv.bot=null;
			    
			    // Appel de la méthode Game pour calculer le chemin
			    List<int[]> path = Game.instance.findPathToNearestDoor(adv);
			    adv.setPathToCage(path);
                
			}
		}

	    
}
	




