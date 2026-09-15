package game.Entities;

import engine.ICategory;
import engine.IModel;
import engine.model.Item;

public class PacGum extends Item {
	protected boolean Eaten;

	public PacGum(IModel m, int r, int c, int o) {
		super(m, r, c, o);
		this.Eaten = false;
		// TODO Auto-generated constructor stub
	}

	@Override
	public ICategory category() {
		// TODO Auto-generated method stub
		return game.Categories.PACGUM;
	}

	public void setEaten(boolean ea) {
		this.Eaten = ea;
	}

	public boolean getEaten() {
		return Eaten;
	}

}
