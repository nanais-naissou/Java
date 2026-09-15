package fsm;

import java.util.Arrays;
import java.util.LinkedList;

import brain.Bot;
import engine.model.Entity;

public class Sequence extends ActionGAL {
    LinkedList<ActionGAL> actions;
    public Sequence(ActionGAL... acts) {
        actions = new LinkedList<>(Arrays.asList(acts));
    }
    void exec(Bot bot) {
        for(ActionGAL act : actions) act.exec(bot.getEntity());
    }
	@Override
	public void exec(Entity e) {
		
	        for (ActionGAL act : actions) {
	            act.exec(e);  // Exécuter chaque action sur l'entité
	        }
	    
		
	}

}
