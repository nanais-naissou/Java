package fsm;

import java.util.Random;

import brain.Bot;
import engine.model.Direction;
import engine.model.Entity;
import engine.model.Model;
import fsm.FSM;
import fsm.Mode;
import fsm.Move;
import fsm.Transition;
import fsm.Cell;
import fsm.Conjonction;
import fsm.Sequence;
import game.Categories;

public class Wandering2 extends Bot {

	public Wandering2(Entity e, Model m) {
		super(m, e);

		this.fsm = new FSM();

		// Création des modes Wandering, Turn et Blocked
		Mode wanderingMode = fsm.canonical("Wandering");
		////////////////////////////////////////////////////////// RANDOM

		wanderingMode.add(new Transition(
				new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
						new Conjonction(new FreeCell(Direction.B), new FreeCell(Direction.R))),

				new MoveRandom(), wanderingMode));

		/////////////////////////////////////////////////// 1

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new Cell(Direction.R, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.L), wanderingMode));

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
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new FreeCell(Direction.R), // Case devant libre
				new Cell(Direction.L, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.F), wanderingMode));

		////////////////////////////////////////// 2
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.B, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.L), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.R, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.F), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.R), wanderingMode));
		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.F), // Case devant libre
				new FreeCell(Direction.L) // Case gauche libre
		), new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.B, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.L), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.B) // Case gauche libre
		), new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.B), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.B), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.L, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.R), wanderingMode));

		wanderingMode.add(new Transition(new Conjonction(new Conjonction(new FreeCell(Direction.L), // Case devant libre
				new FreeCell(Direction.R) // Case gauche libre
		), new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE), // Case devant libre
				new Cell(Direction.F, game.Categories.OBSTACLE) // Case gauche libre
		)),

				new Move(Direction.B), wanderingMode));

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

		/////////////////////////////////

		// Définir le mode initial (Wandering)
		fsm.setInitial(wanderingMode);
		this.setMode(wanderingMode); // Applique le mode Wandering
	}

	@Override
	public void think(int elapsed) {
		// Exécuter la logique du FSM à chaque tick
		fsm.transit(this);
	}

	// Classe MoveRandom pour effectuer un mouvement aléatoire

}
