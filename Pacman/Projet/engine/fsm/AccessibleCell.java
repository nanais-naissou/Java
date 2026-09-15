package fsm;

import engine.model.Direction;
import engine.model.Entity;
import engine.model.Obstacle;

public class AccessibleCell extends ConditionGAL {
	Direction dir;

	public AccessibleCell(Direction d) {
		this.dir = d;
	}

	@Override
	public boolean eval(Entity e) {
		for (Entity entity : e.bot.cell(dir)) { // Utilise la méthode cell(Direction d)
			if (entity instanceof Obstacle) {
				return false;
			}
		}
		return true;
	}

}
