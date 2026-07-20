package fsm;

import engine.model.Entity;
import engine.model.Model;
import engine.model.Player;
import brain.Bot;
import engine.model.Direction;
import game.Entities.Apple;
import game.Entities.Dot;
import game.Entities.PacGum;
import game.Entities.Strawberry;

public class PlayerBot extends Bot {

	private FSM fsm;
	private Mode collectingMode;
	private Mode idleMode;
	private Model model;
	Player p;

	public PlayerBot(Model model, Entity e) {
		super(model, e);
		this.model = model;
		this.p = (Player) e;

		this.fsm = new FSM();

		collectingMode = fsm.canonical("Collecting");
		idleMode = fsm.canonical("Idle");

		collectingMode.add(new Transition(new True(), new CollectDot(), collectingMode));
		collectingMode.add(new Transition(new True(), new CollectApple(), collectingMode));
		collectingMode.add(new Transition(new True(), new CollectStrawberry(), collectingMode));
		collectingMode.add(new Transition(new True(), new CollectPacGum(), collectingMode));

		fsm.setInitial(collectingMode);
		this.setMode(collectingMode); // Applique le mode initial au bot
	}

	@Override
	public void think(int elapsed) {
		if (!(e instanceof Player))
			return; // Vérifie si c'est bien un Player
		fsm.transit(this); // Fait avancer l'automate et gère les transitions
	}

	// Classe pour collecter un Dot
	private class CollectDot extends Move {
		public CollectDot() {
			super(Direction.F, 1); // Avance de 1 case pour collecter
		}

		@Override
		public void exec(Entity e) {
			if (!(e instanceof Player))
				return;

			int row = e.row();
			int col = e.col();

			for (Entity entity : model.entitiesAt(row, col)) {
				if (entity instanceof PacGum) {
					e.bot.setPac(true);
				}
				if (entity instanceof Dot) {
					model.removeEntity(entity); // Retire le Dot du modèle
					if (view != null) {
						view.death(entity); // Mise à jour graphique
					}
					break; // Ne collecte qu'un seul Dot à la fois
				}
			}
		}
	}

	private class CollectApple extends Move {
		public CollectApple() {
			super(Direction.F, 1); // Avance de 1 case pour collecter
		}

		@Override
		public void exec(Entity e) {
			if (!(e instanceof Player))
				return;

			int row = e.row();
			int col = e.col();

			for (Entity entity : model.entitiesAt(row, col)) {
				if (entity instanceof PacGum) {
					e.bot.setPac(true);
				}
				if (entity instanceof Apple) {

					p.projectile_ammo += 2;

					model.removeEntity(entity); // Retire le Apple du modèle
					if (view != null) {
						view.death(entity); // Mise à jour graphique
					}
					break; // Ne collecte qu'un seul Dot à la fois
				}
			}
		}
	}

	private class CollectStrawberry extends Move {
		public CollectStrawberry() {
			super(Direction.F, 1); // Avance de 1 case pour collecter
		}

		@Override
		public void exec(Entity e) {
			if (!(e instanceof Player))
				return;

			int row = e.row();
			int col = e.col();

			for (Entity entity : model.entitiesAt(row, col)) {
				if (entity instanceof PacGum) {
					e.bot.setPac(true);
				}
				if (entity instanceof Strawberry) {
					p.projectile_ammo += 2;
					model.removeEntity(entity); // Retire le Strawbery du modèle
					if (view != null) {
						view.death(entity); // Mise à jour graphique
					}
					break; // Ne collecte qu'un seul Dot à la fois
				}
			}
		}
	}

	private class CollectPacGum extends Move {
		public CollectPacGum() {
			super(Direction.F, 1); // Avance de 1 case pour collecter
		}

		@Override
		public void exec(Entity e) {
			if (!(e instanceof Player))
				return;

			int row = e.row();
			int col = e.col();

			for (Entity entity : model.entitiesAt(row, col)) {

				if (entity instanceof PacGum) {
					e.bot.setPac(true);
					model.removeEntity(entity);

					if (view != null) {
						view.death(entity); // Mise à jour graphique
					}
					break; // Ne collecte qu'un seul Dot à la fois
				}
			}
		}
	}

}
