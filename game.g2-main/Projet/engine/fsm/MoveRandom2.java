package fsm;

import java.util.Random;
import engine.model.Direction;
import engine.model.Entity;
import engine.model.Obstacle;

public class MoveRandom2 extends Move {
	public MoveRandom2() {
		super(Direction.F, 1);
	}

	@Override
	public void exec(Entity e) {
		moveRandomly(e);
	}

	private void moveRandomly(Entity e) {
		Random rand = new Random();
		int n = rand.nextInt(10);

		Direction chosenDirection;
		if (n < 6) {
			chosenDirection = Direction.S;
		} else if (n == 6) {
			chosenDirection = Direction.N;
		} else if (n == 7) {
			chosenDirection = Direction.W;
		} else {
			chosenDirection = Direction.E;
		}

		if (!(e.bot.cell(chosenDirection) instanceof Obstacle)) {
			e.bot.move(chosenDirection);
		}
	}
}