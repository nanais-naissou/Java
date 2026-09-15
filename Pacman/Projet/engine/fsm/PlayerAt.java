package fsm;

import brain.MainCategories;
import engine.model.Direction;
import engine.model.Entity;

public class PlayerAt extends ConditionGAL {
	private final Direction dir;

	public PlayerAt(Direction d) {
		this.dir = d;
	}

	@Override
	public boolean eval(Entity e) {
		Entity target = e.bot.cell(dir, MainCategories.PLAYER);
		return target != null;
	}
}
