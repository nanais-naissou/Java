package fsm;

import brain.Bot;
import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;

public class FleeingBot2 extends Bot {

	public FleeingBot2(Entity e, Model m) {
		super(m, e);
		this.fsm = new FSM();
		Mode flee = fsm.canonical("Flee");

		flee.add(new Transition(new PlayerAt(Direction.N).and(new FreeCell(Direction.S)), new Move(Direction.S, 100),
				flee));
		flee.add(new Transition(new PlayerAt(Direction.S).and(new FreeCell(Direction.N)), new Move(Direction.N, 100),
				flee));
		flee.add(new Transition(new PlayerAt(Direction.W).and(new FreeCell(Direction.E)), new Move(Direction.E, 100),
				flee));
		flee.add(new Transition(new PlayerAt(Direction.E).and(new FreeCell(Direction.W)), new Move(Direction.W, 100),
				flee));

		flee.add(new Transition(new FreeCell(Direction.S), new MoveRandom2(), flee));
		flee.add(new Transition(new FreeCell(Direction.W), new MoveRandom2(), flee));
		flee.add(new Transition(new FreeCell(Direction.E), new MoveRandom2(), flee));
		/*
		 * flee.add(new Transition( new PlayerNearby(1), // Distance max pour "attaque"
		 * = 2 cases new MoveTowardsPlayer(), flee ));
		 */
		this.mode = flee;
	}

	@Override
	public void think(int elapsed) {
		fsm.transit(this);
	}
}