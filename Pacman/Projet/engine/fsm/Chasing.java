package fsm;

import brain.Bot;

import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;
import game.Categories;

public class Chasing extends Bot {

	public Chasing(Model m, Entity e) {
		super(m, e);

		this.fsm = new FSM();

		Mode chasing = fsm.canonical("Chasing");
		Mode wandering = fsm.canonical("Wandering");

		int steps = 5;

		// Transitions pour le mode : Chasing
		chasing.add(new Transition(new Closest(Categories.PLAYER, Direction.N, steps), new Move(Direction.N), chasing));
		chasing.add(new Transition(new Closest(Categories.PLAYER, Direction.S, steps), new Move(Direction.S), chasing));
		chasing.add(new Transition(new Closest(Categories.PLAYER, Direction.W, steps), new Move(Direction.W), chasing));
		chasing.add(new Transition(new Closest(Categories.PLAYER, Direction.E, steps), new Move(Direction.E), chasing));

		chasing.add(new Transition(new True(), new MoveRandom(), wandering));

		// Transitions pour le mode : Wandering
		wandering.add(
				new Transition(new Closest(Categories.PLAYER, Direction.N, steps), new Move(Direction.N), chasing));
		wandering.add(
				new Transition(new Closest(Categories.PLAYER, Direction.S, steps), new Move(Direction.S), chasing));
		wandering.add(
				new Transition(new Closest(Categories.PLAYER, Direction.W, steps), new Move(Direction.W), chasing));
		wandering.add(
				new Transition(new Closest(Categories.PLAYER, Direction.E, steps), new Move(Direction.E), chasing));

		wandering.add(new Transition(new True(), new MoveRandom(), wandering));

		fsm.setInitial(wandering);
		this.setMode(wandering);

	}

	@Override
	public void think(int elapsed) {
		fsm.transit(this);

	}

}
