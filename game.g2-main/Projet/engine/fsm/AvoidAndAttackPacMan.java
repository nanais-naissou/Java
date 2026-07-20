package fsm;

import engine.model.Entity;

import engine.model.Model;
import engine.model.Player;
import game.Entities.PacGum;
import engine.model.Direction;
import brain.Bot;

public class AvoidAndAttackPacMan extends Bot {

	private FSM fsm;
	private Mode avoidMode;
	private Mode attackMode;
	private Model model;

	public AvoidAndAttackPacMan(Model model, Entity e) {
		super(model, e);
		this.model = model;

		this.fsm = new FSM();

		avoidMode = fsm.canonical("AvoidingPacMan");
		attackMode = fsm.canonical("AttackingPacMan");

		avoidMode.add(new Transition(new Cell(Direction.F, game.Categories.PLAYER), new FleePacMan(), avoidMode));

		attackMode.add(new Transition(new Cell(Direction.F, game.Categories.PLAYER), new AttackPacMan(), attackMode));

		fsm.setInitial(avoidMode);
		this.setMode(avoidMode);
	}

	@Override
	public void think(int elapsed) {
		if (!(e instanceof Player))
			return;
		fsm.transit(this);
	}

	private class FleePacMan extends Move {
		public FleePacMan() {
			super(Direction.F, 1);
		}

		@Override
		public void exec(Entity e) {

			System.out.println("Fleeing from PacMan!");
		}
	}

	private class AttackPacMan extends Move {
		public AttackPacMan() {
			super(Direction.F, 1);
		}

		@Override
		public void exec(Entity e) {

			int row = e.row();
			int col = e.col();

			for (Entity entity : model.entitiesAt(row, col)) {
				if (entity instanceof PacGum) {
					model.removeEntity(entity);
					System.out.println("PacMan killed!");
					break;
				}
			}
		}
	}
}
