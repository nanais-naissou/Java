package fsm;

import engine.IBrain.IBot;
import brain.Category;
import engine.model.Direction;
import engine.model.Entity;

public class Move extends ActionGAL {

	private final Direction direction;
	private final int steps;

	public Move(Direction d) {
		this(d, 1);
	}

	public Move(Direction d, int s) {
		this.direction = d;
		this.steps = s;
	}

	@Override
	public void exec(Entity e) {
		System.out.println("Move Fsm");
		int angle = direction.degrees();
		double radians = Math.toRadians(angle);

		float dx = (float) Math.cos(radians);
		float dy = (float) Math.sin(radians);
		e.stunt.move(steps * dx, steps * dy);
	}

}
