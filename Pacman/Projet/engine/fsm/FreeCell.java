package fsm;

import brain.MainCategories;
import engine.model.Direction;
import engine.model.Entity;

public class FreeCell extends ConditionGAL {
	private final Direction dir;

	public FreeCell(Direction d) {
		this.dir = d;
	}

	@Override
	public boolean eval(Entity e) {
		return e.bot.cell(dir, MainCategories.OBSTACLE) == null;
	}
}