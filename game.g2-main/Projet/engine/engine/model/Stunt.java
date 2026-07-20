package engine.model;

public abstract class Stunt {
	protected Model m;
	protected Entity e;
	protected Action action;
	protected int progress;

	public Stunt(Model m, Entity e) {
		this.m = m;
		this.e = e;
		this.action = null;
		this.progress = 0;
	}

	public abstract boolean move(int dRow, int dCol);

	public abstract boolean move(float dRow, float dCol);

	public abstract void rotate(int angle);

	public int progress() {
		return progress;
	}

	public boolean isMoving() {
		return action != null;
	}

	public void resetAction() {
		this.action = null;
		this.progress = 0;
	}

	public Action currentAction() {
		return action;
	}

	public void tick(int elapsed) {
		if (action != null) {
			action.tick(elapsed);
		}
	}

	public static interface Action {
		void tick(int elapsed);

		int kind();
	}
}
