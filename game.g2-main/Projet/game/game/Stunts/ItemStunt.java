package game.Stunts;

import engine.model.Model;
import engine.model.Item;
import engine.model.StuntGrid;

public class ItemStunt extends StuntGrid {
	private Item item;

	public ItemStunt(Model m, Item item) {
		super(m, item);
		this.item = item;
	}

	@Override
	public boolean move(int nrows, int ncols) {
		return super.move(nrows, ncols);
	}

	@Override
	public boolean move(float dx, float dy) {
		return false;
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

	@Override
	public void tick(int elapsed) {
		super.tick(elapsed);
	}

}
