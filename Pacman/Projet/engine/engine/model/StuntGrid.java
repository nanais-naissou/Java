package engine.model;

public class StuntGrid extends Stunt {
	protected boolean debugg = false;
	public StuntGrid(Model m, Entity e) {
		
		super(m, e);
	}

	@Override
	public boolean move(int dRow, int dCol) {
		if (action == null) {
			// Example: grid-based motion, 1000ms duration
			this.action = new Motion(dRow, dCol, 1000); // Use your existing logic
			this.progress = 0;
			// Optionally: post to task system if you have one
			return true;
		}
		return false;
	}

	@Override
	public boolean move(float dRow, float dCol) {
		// Not supported for grid-based
		return false;
	}

	// Optionally override tick to sync progress from action

	// TODO : méthode rotate animée
	public void rotate(int angle) {
		angle = angle + e.orientation();
		e.face(angle);
	}

	@Override
	public void tick(int elapsed) {
		if (action == null) {
			if (e.bot != null)
				e.bot.think(elapsed);
		} else {
			action.tick(elapsed);
		}
	}

	// Action MOVE animée sur 1000 ms
	public class Motion implements Action {
		public int nrows;
		public int ncols;
		private int duration, delay, step, elapsedTime;
		private boolean moved;

		Motion(int nrows, int ncols, int duration) {
			this.nrows = nrows;
			this.ncols = ncols;
			this.duration = duration;
			this.delay = duration / 2;
			this.step = 0;
			this.elapsedTime = 0;
			this.moved = false;
		}

		@Override
		public int kind() {
			return 0;
		}

		@Override
		public void tick(int elapsed) {
			e.inHisInitPos=m.inHisInitPos(e);

			this.elapsedTime += elapsed;
			float percent = (float) this.elapsedTime / (float) duration;
			StuntGrid.this.progress = Math.min(100, (int) (100 * percent));
			delay -= elapsed;
			if (delay > 0)
				return;
			switch (step) {
			case 0:
				m.move(e, nrows, ncols);
				step++;
				this.delay = duration / 2;
				return;
			case 1:
				step++;
				StuntGrid.this.action = null; // Correction importante !
				return;
			}
		}
	}
}