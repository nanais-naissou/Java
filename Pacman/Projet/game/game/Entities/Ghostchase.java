package game.Entities;

import engine.IView;
import engine.model.Adversary;
import engine.model.Model;
import fsm.Chasing;
import game.Stunts.AdversaryStunt;

public class Ghostchase extends Adversary {

	public Ghostchase(Model m, int r, int c, int o, IView m_view) {
		super(m, r, c, o, m_view);
		this.stunt = new AdversaryStunt(m, this);
		this.bot = new Chasing(m, this);
		this.tempBot = this.bot;
		// this.bot = new FleeingBot2(this,m);

	}

}
