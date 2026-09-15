package game.Stunts;

import engine.model.Adversary;
import engine.model.Model;
import engine.model.StuntFluide;

public class FleeAdversaryStunt extends StuntFluide {
	private Adversary adv;

	public FleeAdversaryStunt(Model m, Adversary adv) {
		super(m, adv);
		this.adv = adv;
	}

	/*
	 * @Override public boolean move(int nrows, int ncols) { return
	 * super.move(nrows, ncols); }
	 */
	@Override
	public boolean move(float dx, float dy) {
		return super.move(dx * 20, dy * 20);
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

}
