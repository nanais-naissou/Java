package game.Entities;

import engine.ICategory;
import engine.IModel;
import engine.model.Entity;

public class Dot extends Entity {
	public Dot(IModel m_model, int row, int col, int orient) {
		super(m_model, row, col, orient);
	}

	@Override
	public ICategory category() {
		return game.Categories.DOT;
	}
}