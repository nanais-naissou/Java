package fsm;

import java.util.Random;

import engine.model.Direction;
import engine.model.Entity;

public class MoveRandom2Directions extends Move {
	private final Direction dir1;
	private final Direction dir2;

	// Constructeur qui prend deux directions possibles
	
	public MoveRandom2Directions(Direction dir1, Direction dir2) {
		super(Direction.F, 1);
		this.dir1 = dir1;
		this.dir2 = dir2;
	}

	@Override
	public void exec(Entity e) {
		
		// Choose randomly one of the two directions
		
		Random rand = new Random();
		Direction chosenDirection = rand.nextBoolean() ? dir1 : dir2;
		System.out.println("move 2 direction " + chosenDirection.toString());
		e.bot.move2(chosenDirection);

	}
}