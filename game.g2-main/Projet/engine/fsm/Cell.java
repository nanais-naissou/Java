package fsm;

import brain.Category;
import engine.model.Direction;
import engine.model.Entity;
import game.Entities.Apple;

public class Cell extends ConditionGAL {
	private final Direction dir;
	private final Category cat;


	public Cell(Direction d, Category c) {
		this.dir = d;
		this.cat = c;
	}

	@Override
	public boolean eval(Entity e) {
		
		return e.bot.cell(dir, cat) !=null;
	}
}
