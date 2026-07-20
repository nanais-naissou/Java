package fsm;

import java.util.Random;

import brain.Bot;

import engine.model.Direction;
import engine.model.Entity;
import engine.model.Obstacle;
import fsm.Move;

public class MoveRandom extends Move {

	public MoveRandom() {
		super(Direction.F, 1);
	}

	@Override
	public void exec(Entity e) {
		moveRandomly(e);
	}

	private void moveRandomly(Entity e) {
		Direction[] directions = { Direction.N, Direction.S, Direction.E, Direction.W };
		Random rand = new Random();
		Direction chosenDirection = directions[rand.nextInt(directions.length)];

		// System.out.println("MoveRandom");
		if (!(e.bot.cell(chosenDirection) instanceof Obstacle)) {

			e.bot.move(chosenDirection);
		}

	}
}
