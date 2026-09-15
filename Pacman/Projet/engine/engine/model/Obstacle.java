package engine.model;

import engine.ICategory;
import engine.IModel;
import game.Stunts.AdversaryStunt;
import game.Stunts.ObstacleStunt;

public class Obstacle extends Entity {
	public Obstacle(Model m, int row, int col, int orient) {
		super(m, row, col, orient);
		this.stunt = new ObstacleStunt(m, this);
	}

	@Override
	public ICategory category() {
		return game.Categories.OBSTACLE;
	}
}
