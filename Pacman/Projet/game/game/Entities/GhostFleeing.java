package game.Entities;

import engine.IView;
import engine.model.Adversary;
import engine.model.Model;
import fsm.Chasing;
import fsm.FleeingBot2;
import game.Stunts.AdversaryStunt;
import game.Stunts.FleeAdversaryStunt;



public class GhostFleeing extends Adversary {

	public GhostFleeing(Model m, int r, int c, int o, IView m_view) {
		super(m, r, c, o, m_view);

		this.stunt = new FleeAdversaryStunt(m, this);

		this.bot = new FleeingBot2(this, m);
		this.tempBot = this.bot;

	}

}
