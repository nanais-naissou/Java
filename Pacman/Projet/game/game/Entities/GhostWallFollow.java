package game.Entities;

import engine.IView;
import engine.model.Adversary;
import engine.model.Model;
import fsm.LeftWallGhost;
import game.Stunts.AdversaryStunt;

public class GhostWallFollow extends Adversary {

	public GhostWallFollow(Model m, int r, int c, int o, IView m_view) {
		super(m, r, c, o, m_view);

		this.stunt = new AdversaryStunt(m, this);


		this.bot = new LeftWallGhost(this, m);
		this.tempBot = this.bot;


	}

}
