package game.Entities;

import engine.ICategory;
import engine.IModel;
import engine.model.Item;

public class Strawberry extends Item {
	public Strawberry(IModel m_model, int row, int col, int orient) {
		super(m_model, row, col, orient);
	}

	@Override
	public ICategory category() {
		return game.Categories.STRAWBERRY;
	}
}
