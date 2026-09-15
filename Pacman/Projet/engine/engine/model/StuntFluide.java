package engine.model;

public class StuntFluide extends Stunt {
	protected boolean debugg = false;
	private static final float FLUID_SPEED = 1000.0f;

	public StuntFluide(Model m, Entity e) {
		super(m, e);
	}

	@Override
	public boolean move(int dRow, int dCol) {
		// Not supported for fluid-based
		return false;
	}

	// idee de chatgpt
	@Override
	public boolean move(float dx, float dy) {
		if (action == null) {
			float distance = (float) Math.sqrt(dx * dx + dy * dy);
			int duration = (FLUID_SPEED > 0) ? Math.max(16, (int) (distance / FLUID_SPEED * 1000.0f)) : 200;
			if (debugg) {
				System.out.println("[DEBUG] AVANT move : x=" + e.x() + " y=" + e.y());
			}
			this.action = new FluidMotion(dx, dy, duration);
			this.progress = 0;
			if (debugg) {
				System.out.println("[DEBUG] APRES move : x=" + e.x() + " y=" + e.y());
			}
			return true;
		}
		return false;
	}

	public void rotate(int angle) {
		angle = angle + e.orientation();
		
		e.face(e.normalize(angle));
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

	public class FluidMotion implements Action {
		private float dx, dy;
		private int duration, elapsedTime;
		private float startX, startY, targetX, targetY;

		FluidMotion(float dx, float dy, int duration) {
			this.dx = dx;
			this.dy = dy;
			this.duration = duration;
			this.elapsedTime = 0;
			this.startX = e.x();
			this.startY = e.y();
			this.targetX = startX + dx;
			this.targetY = startY + dy;
		}

		@Override
		public int kind() {
			return 0;
		}

		@Override
		public void tick(int elapsed) {
		    e.inHisInitPos = m.inHisInitPos(e);

		    elapsedTime += elapsed;
		    float percent = Math.min(1.0f, (float) elapsedTime / duration);
		    StuntFluide.this.progress = Math.min(100, (int) (100 * percent));

		    float currentX = startX + percent * (targetX - startX);
		    float currentY = startY + percent * (targetY - startY);

		    float dxStep = currentX - e.x();
		    float dyStep = currentY - e.y();

		    boolean canMove = m.moveFluid(e, dxStep, dyStep);
		    if (!canMove) {
		        // Stop motion if blocked
		        StuntFluide.this.action = null;
		        return;
		    }

		    if (percent >= 1.0f) {
		        StuntFluide.this.action = null;
		    }
		}

	}	
}
