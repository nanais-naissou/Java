package fsm;

import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;
import brain.Bot;

public class Wandering extends Bot {

	public Wandering(Entity e, Model m) {
		super(m, e);

		this.fsm = new FSM();

		Mode wanderingMode = fsm.canonical("Wandering");

		wanderingMode.add(new Transition(
				new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
						new Conjonction(new FreeCell(Direction.B), new FreeCell(Direction.R))),

				new MoveRandom(), wanderingMode));

		wanderingMode
				.add(new Transition(
						new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
								new Conjonction(new FreeCell(Direction.B),
										new Cell(Direction.R, game.Categories.OBSTACLE))),

						new Move(Direction.B), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.F), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new Cell(Direction.L, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.L), wanderingMode));
		/////////////////////////////////////////////////// 2
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE),
				new Cell(Direction.B, game.Categories.OBSTACLE)), new Move(Direction.L), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE),
				new Cell(Direction.R, game.Categories.OBSTACLE)), new Move(Direction.F), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE),
				new Cell(Direction.F, game.Categories.OBSTACLE)), new Move(Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
				new Cell(Direction.F, game.Categories.OBSTACLE)), new Move(Direction.L), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE),
				new Cell(Direction.B, game.Categories.OBSTACLE)), new Move(Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
				new Cell(Direction.B, game.Categories.OBSTACLE)), new Move(Direction.L), wanderingMode));
		/////////////////////////////////////////////////////////////
		wanderingMode
				.add(new Transition(
						new Conjonction(
								new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
										new Cell(Direction.B, game.Categories.OBSTACLE)),
								new Cell(Direction.F, game.Categories.OBSTACLE)),
						new Move(Direction.L), wanderingMode));
		wanderingMode
				.add(new Transition(
						new Conjonction(
								new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
										new Cell(Direction.L, game.Categories.OBSTACLE)),
								new Cell(Direction.F, game.Categories.OBSTACLE)),
						new Move(Direction.B), wanderingMode));
		wanderingMode
				.add(new Transition(
						new Conjonction(
								new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
										new Cell(Direction.B, game.Categories.OBSTACLE)),
								new Cell(Direction.L, game.Categories.OBSTACLE)),
						new Move(Direction.F), wanderingMode));
		wanderingMode
				.add(new Transition(
						new Conjonction(
								new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE),
										new Cell(Direction.B, game.Categories.OBSTACLE)),
								new Cell(Direction.L, game.Categories.OBSTACLE)),
						new Move(Direction.R), wanderingMode));

		this.mode = wanderingMode;
	}

	@Override
	public void think(int elapsed) {
		fsm.transit(this);
	}

}
