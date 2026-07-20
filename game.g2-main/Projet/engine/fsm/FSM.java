package fsm;

import java.util.ArrayList;
import java.util.List;

import brain.Bot;
import engine.model.Entity;

//--- L'AUTOMATE GAL ---
public class FSM {
	private final List<Mode> modes = new ArrayList<>();
	private Mode initial;

	public Mode canonical(String name) {
		System.out.println("Mode:"+name);
		for (Mode m : modes)
			if (m.getName().equals(name))
				return m;
		Mode m = new Mode(new State(name));
		modes.add(m);
		return m;
	}

	public void setInitial(Mode m) {
		this.initial = m;
	}

	public Mode getInitial() {
		return initial;
	}

	/** Transit pour un bot (ou entity qui possède un bot) */
	public boolean transit(Bot bot) {
		Mode mode = bot.getMode();
		Boolean possible=false;
		//System.out.println("1");
		for (Transition t : mode.getTransitions()) {
			
			Entity e = bot.getEntity();
			//System.out.println("2");
			if (t.getCondition().eval(e)) {
				ActionGAL action = t.getAction();
				if (action != null) {
					//System.out.println("3");
					t.getAction().exec(bot.getEntity());}
				//System.out.println(mode);
				bot.setMode(t.getTarget());
				possible=true;
				

			}
		}
		
		// Aucune transition applicable
		return possible;
	}
}
