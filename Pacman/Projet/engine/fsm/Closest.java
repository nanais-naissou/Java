package fsm;

import brain.Bot;
import brain.Category;
import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Player;
import engine.IModel;

public class Closest extends ConditionGAL {

	public Direction direction;
	public Category category;
	public int steps;

	public Closest(Category category, Direction direction, int steps) {
		this.category = category;
		this.direction = direction;
		this.steps = steps;
	}

	public boolean eval(Entity e) {
		Bot bot = e.bot;
		IModel model = bot.m;
		Player player = model.player();

		if (player == null) {
			return false;
		}

		int dRow = player.row() - e.row();
		int dCol = player.col() - e.col();

		if (direction.equals(Direction.N)) {
			if (dRow < 0 && dCol == 0 && Math.abs(dRow) <= steps) {
				for (int i = 1; i < Math.abs(dRow); i++) {
					if (model.entitiesAt(e.row() - i, e.col()).stream()
							.anyMatch(entity -> entity.category() == brain.MainCategories.OBSTACLE)) {
						return false; // wall blocks the view
					}
				}

				return true;
			}
		} else if (direction.equals(Direction.S)) {
			if (dRow > 0 && dCol == 0 && dRow <= steps) {
				for (int i = 1; i < dRow; i++) {
					if (model.entitiesAt(e.row() + i, e.col()).stream()
							.anyMatch(entity -> entity.category() == brain.MainCategories.OBSTACLE)) {
						return false;
					}
				}

				return true;
			}
		} else if (direction.equals(Direction.W)) {
			if (dCol < 0 && dRow == 0 && Math.abs(dCol) <= steps) {
				for (int i = 1; i < Math.abs(dCol); i++) {
					if (model.entitiesAt(e.row(), e.col() - i).stream()
							.anyMatch(entity -> entity.category() == brain.MainCategories.OBSTACLE)) {
						return false;
					}
				}

				return true;
			}
		} else if (direction.equals(Direction.E)) {
			if (dCol > 0 && dRow == 0 && dCol <= steps) {
				for (int i = 1; i < dCol; i++) {
					if (model.entitiesAt(e.row(), e.col() + i).stream()
							.anyMatch(entity -> entity.category() == brain.MainCategories.OBSTACLE)) {
						return false;
					}
				}

				return true;
			}
		}

		return false;
	}

	/*
	 * @Override public boolean eval(Entity e) { Bot bot = e.bot; IModel model =
	 * bot.m; Player player = model.player();
	 * 
	 * if (player == null) { return false; }
	 * 
	 * int dRow = player.row() - e.row(); int dCol = player.col() - e.col();
	 * 
	 * if (direction.equals(Direction.N)) { if (dRow < 0 && dCol == 0 &&
	 * Math.abs(dRow) <= steps) { System.out.println("Detected player NORTH at " +
	 * player.row() + "," + player.col()); return true; } } else if
	 * (direction.equals(Direction.S)) { if (dRow > 0 && dCol == 0 && dRow <= steps)
	 * { System.out.println("Detected player SOUTH at " + player.row() + "," +
	 * player.col()); return true; } } else if (direction.equals(Direction.W)) { if
	 * (dCol < 0 && dRow == 0 && Math.abs(dCol) <= steps) {
	 * System.out.println("Detected player WEST at " + player.row() + "," +
	 * player.col()); return true; } } else if (direction.equals(Direction.E)) { if
	 * (dCol > 0 && dRow == 0 && dCol <= steps) {
	 * System.out.println("Detected player EAST at " + player.row() + "," +
	 * player.col()); return true; } }
	 * 
	 * return false; }
	 */

}