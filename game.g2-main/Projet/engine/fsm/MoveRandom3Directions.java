package fsm;

import java.util.Random;

import engine.model.Direction;
import engine.model.Entity;

public class MoveRandom3Directions extends Move {
	private final Direction dir1;
	private final Direction dir2;
	private final Direction dir3;

	// Constructeur qui prend trois directions possibles
	public MoveRandom3Directions(Direction dir1, Direction dir2, Direction dir3) {
		super(Direction.F, 1); // Par défaut, on avance d'une case
		this.dir1 = dir1;
		this.dir2 = dir2;
		this.dir3 = dir3;
	}

	@Override
	public void exec(Entity e) {
		// Choisir aléatoirement l'une des trois directions
		Random rand = new Random();
		Direction chosenDirection;
		int choice = rand.nextInt(3);
		System.out.println("the number is :" + choice);
		if (choice == 0) {
			chosenDirection = dir1;
		} else if (choice == 1) {
			chosenDirection = dir2;
		} else {
			chosenDirection = dir3;
		}
		e.bot.move(chosenDirection);

	}
}
