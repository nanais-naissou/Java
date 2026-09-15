package fsm;

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

public class RightWallGhost extends Bot {
	private int wallSwitchCount = 0;
	private Mode previousMode = null;
	private static final int WALL_SWITCH_LIMIT = 8;

	public RightWallGhost(Entity e, Model m) {
		super(m, e);

		this.fsm = new FSM();

		Mode wallFollowing = fsm.canonical("WallFollowing");
		Mode wallFollowingR = fsm.canonical("WallFollowingR");
		Mode wallFollowingB = fsm.canonical("WallFollowingB");

		Mode wallFollowingF = fsm.canonical("WallFollowingF");

///////////////////////////////LEFT

		///////// 1 obstacle
		// 1. Cell(L,O) & Cell(F,V) ? Move()
		wallFollowing.add(
				new Transition(new Conjonction(new Cell(Direction.L, Categories.OBSTACLE), new FreeCell(Direction.F)),
						new Move(Direction.F), wallFollowing));

		// 2. Cell(R,O) & Cell(F,V) ? Move()
		wallFollowing.add(
				new Transition(new Conjonction(new Cell(Direction.R, Categories.OBSTACLE), new FreeCell(Direction.F)),
						new Move(Direction.F), wallFollowing));

		// 3. Cell(L,V) & Cell(F,O) ? Turn(L) ; Move()
		wallFollowing.add(
				new Transition(new Conjonction(new FreeCell(Direction.L), new Cell(Direction.F, Categories.OBSTACLE)),
						new Move(Direction.L), wallFollowing));
		wallFollowing.add(new Transition(new Conjonction(

				new FreeCell(Direction.L), new FreeCell(Direction.F)),

				new MoveRandom2Directions(Direction.L, Direction.F), wallFollowing));

		wallFollowing.add(new Transition(
				new Conjonction(new Cell(Direction.L, Categories.OBSTACLE), new Cell(Direction.F, Categories.OBSTACLE)),
				new MoveRandom2Directions(Direction.R, Direction.B), wallFollowingR));

		////////// 4 cases vides :priorite gauche
		wallFollowing.add(new Transition(
				new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
						new Conjonction(new FreeCell(Direction.B), new FreeCell(Direction.R))),

				new MoveRandom(), wallFollowing));

		///////////////////////////////

		////////////// 3 obstacles on passe a right
		wallFollowing.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.F, game.Categories.OBSTACLE)), null, wallFollowing));
		wallFollowing.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
						new Cell(Direction.L, game.Categories.OBSTACLE)),
				new Cell(Direction.F, game.Categories.OBSTACLE)), null, wallFollowingR));
		wallFollowing.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.L, game.Categories.OBSTACLE)), null, wallFollowing));
		wallFollowing.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.L, game.Categories.OBSTACLE)), null, wallFollowingR));

//////////////////////////////RIGHT
// 1. Cell(R,O) & Cell(F,V) ? Move()
		wallFollowingR.add(
				new Transition(new Conjonction(new Cell(Direction.R, Categories.OBSTACLE), new FreeCell(Direction.B)),
						new Move(Direction.B, 1), wallFollowingR));
		wallFollowingR.add(new Transition(

				new FreeCell(Direction.R),

				new Move(Direction.R), wallFollowing));
		wallFollowingR.add(new Transition(

				new FreeCell(Direction.B),

				new Move(Direction.B), wallFollowingR));

// 2. Cell(L,O) & Cell(F,V) ? Move()
		wallFollowingR.add(
				new Transition(new Conjonction(new Cell(Direction.L, Categories.OBSTACLE), new FreeCell(Direction.B)),
						new Move(Direction.B, 1), wallFollowingR));

// 3. Cell(R,V) & Cell(F,O) ? Turn(R) ; Move()
		wallFollowingR.add(
				new Transition(new Conjonction(new Cell(Direction.B, Categories.OBSTACLE), new FreeCell(Direction.R)),

						new Move(Direction.R, 1), wallFollowingR));
		wallFollowingR.add(new Transition(new Conjonction(

				new FreeCell(Direction.R), new FreeCell(Direction.B)),

				new MoveRandom2Directions(Direction.R, Direction.B), wallFollowingR));

		wallFollowingR.add(new Transition(
				new Conjonction(new Cell(Direction.B, Categories.OBSTACLE), new Cell(Direction.R, Categories.OBSTACLE)),
				new MoveRandom2Directions(Direction.F, Direction.L), wallFollowing));

//////////////2 4cases vides :priorite right
		wallFollowingR.add(new Transition(
				new Conjonction(new Conjonction(new FreeCell(Direction.F), new FreeCell(Direction.L)),
						new Conjonction(new FreeCell(Direction.B), new FreeCell(Direction.R))),

				new MoveRandom(), wallFollowingR));

///////////////////4 : 3 obstacles on revient vers left
		wallFollowingR.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.F, game.Categories.OBSTACLE)), null, wallFollowing));
		wallFollowingR
				.add(new Transition(
						new Conjonction(
								new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
										new Cell(Direction.L, game.Categories.OBSTACLE)),
								new Cell(Direction.F, game.Categories.OBSTACLE)),
						new Move(Direction.R), wallFollowingR));
		wallFollowingR.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.R, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.L, game.Categories.OBSTACLE)), null, wallFollowing));
		wallFollowingR.add(new Transition(new Conjonction(
				new Conjonction(new Cell(Direction.F, game.Categories.OBSTACLE),
						new Cell(Direction.B, game.Categories.OBSTACLE)),
				new Cell(Direction.L, game.Categories.OBSTACLE)), null, wallFollowingR));

//////////////////////////////////////////wallFollowing Backward

		wallFollowingB.add(new Transition(
				new Conjonction(new Conjonction(new Cell(Direction.R, Categories.OBSTACLE),
						new Cell(Direction.L, Categories.OBSTACLE)), new FreeCell(Direction.B)),
				new Move(Direction.B), wallFollowingB));

		wallFollowingB.add(new Transition(new FreeCell(Direction.L), new Move(Direction.L), wallFollowing));
		wallFollowingB.add(new Transition(new FreeCell(Direction.R), new Move(Direction.R), wallFollowingR));

//////////////////////////////////////wallfollowForward
		wallFollowingF.add(new Transition(
				new Conjonction(new Conjonction(new Cell(Direction.R, Categories.OBSTACLE),
						new Cell(Direction.L, Categories.OBSTACLE)), new FreeCell(Direction.F)),
				new Move(Direction.F), wallFollowingF));

		wallFollowingF.add(new Transition(new FreeCell(Direction.L), new Move(Direction.L), wallFollowing));
		wallFollowingF.add(new Transition(new FreeCell(Direction.R), new Move(Direction.R), wallFollowingR));

		fsm.setInitial(wallFollowingR);
		this.setMode(wallFollowingR);
	}

	@Override
	public void think(int elapsed) {
		fsm.transit(this);
	}
}