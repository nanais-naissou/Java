package engine.model;

import engine.ICategory;
import engine.IModel;

public class Item extends Entity {
	public Item(IModel m_model, int row, int col, int orient) {
		super(m_model, row, col, orient);
	}

	@Override
	public ICategory category() {
		return game.Categories.ITEM;
	}
}
