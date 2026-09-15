package game.Entities;

import engine.IView;
import engine.model.Adversary;
import engine.model.Model;
import fsm.Wandering3;
import game.Stunts.AdversaryStunt;

public class GhostWandering extends Adversary {

	public GhostWandering(Model m, int r, int c, int o, IView m_view) {
		super(m, r, c, o, m_view);
		this.stunt = new AdversaryStunt(m, this);
		this.bot = new Wandering3(this,m);
		this.tempBot = this.bot;


	}

}
