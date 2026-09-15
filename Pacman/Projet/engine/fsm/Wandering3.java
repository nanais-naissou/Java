package fsm;

import brain.Bot;
import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;

public class Wandering3 extends Bot {

	public Wandering3(Entity e, Model m) {
		super(m, e);

		this.fsm = new FSM();

		Mode wanderingMode = fsm.canonical("Wandering");

		wanderingMode.add(new Transition(
				new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
						new Conjonction(new FreeCell(Direction.B), new FreeCell(Direction.R))),

				new MoveRandom(), wanderingMode));

		///////// 1

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new Cell(Direction.R, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom3Directions(Direction.F, Direction.L, Direction.B), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom3Directions(Direction.F, Direction.L, Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom3Directions(Direction.B, Direction.L, Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new Cell(Direction.L, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom3Directions(Direction.B, Direction.F, Direction.R), wanderingMode));

		////////////////////////////////////////// 2
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.B, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.R, Direction.L), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.R, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.B, Direction.F), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.R, Direction.F), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.F, Direction.L), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.B, Direction.L), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.B, Direction.R), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new MoveRandom2Directions(Direction.R, Direction.L), wanderingMode));

		/////////////////////////////////////////////////////////////// 3
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

		// Définir le mode initial (Wandering)
		fsm.setInitial(wanderingMode);
		this.setMode(wanderingMode); // Applique le mode Wandering
	}

	@Override
	public void think(int elapsed) {

		fsm.transit(this);
	}

}