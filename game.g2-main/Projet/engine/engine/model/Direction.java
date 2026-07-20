package engine.model;

public abstract class Direction {
	public static final Direction N; // north
	public static final Direction E; // east
	public static final Direction S; // south
	public static final Direction W; // west
	public static final Direction F; // forward
	public static final Direction B; // backward
	public static final Direction L; // left
	public static final Direction R; // right

	public abstract boolean isRelative();

	public abstract int degrees();

	public abstract boolean equals(Direction d);

	public abstract Direction rotate(int angle);

	public abstract Direction cardinalOf();

	static {
		N = new Absolute(270);
		E = new Absolute(0);
		S = new Absolute(90);
		W = new Absolute(180);
		F = new Relative(0);
		B = new Relative(180);
		L = new Relative(270);
		R = new Relative(90);
	}

	public static class Absolute extends Direction {
		private final int angle; // en degrés, 0=E, 90=S, 180=W, 270=N

		public Absolute(int angle) {
			this.angle = ((angle % 360) + 360) % 360;
		}

		@Override
		public boolean isRelative() {
			return false;
		}

		@Override
		public int degrees() {
			return angle;
		}

		@Override
		public boolean equals(Direction d) {
			return !d.isRelative() && this.degrees() == d.degrees();
		}

		@Override
		public Direction rotate(int delta) {
			return new Absolute(angle + delta);
		}

		@Override
		public Direction cardinalOf() {
			// Ramène à la direction cardinale la plus proche
			int a = ((angle % 360) + 360) % 360;
			if (a >= 315 || a < 45)
				return E;
			if (a >= 45 && a < 135)
				return S;
			if (a >= 135 && a < 225)
				return W;
			return N;
		}

		@Override
		public String toString() {
			if (equals(N))
				return "N";
			if (equals(S))
				return "S";
			if (equals(E))
				return "E";
			if (equals(W))
				return "W";
			return Integer.toString(angle);
		}
	}

	private static class Relative extends Direction {
		private final int delta; // ex: 0 = forward, 180 = backward, 90 = right, 270 = left

		public Relative(int delta) {
			this.delta = ((delta % 360) + 360) % 360;
		}

		@Override
		public boolean isRelative() {
			return true;
		}

		@Override
		public int degrees() {
			return delta;
		}

		@Override
		public boolean equals(Direction d) {
			return d.isRelative() && this.degrees() == d.degrees();
		}

		@Override
		public Direction rotate(int angle) {
			return new Relative(delta + angle);
		}

		@Override
		public Direction cardinalOf() {
			// Relative n'a pas de sens "absolu", retourne lui-même
			return this;
		}

		@Override
		public String toString() {
			if (equals(F))
				return "F";
			if (equals(B))
				return "B";
			if (equals(L))
				return "L";
			if (equals(R))
				return "R";
			return Integer.toString(delta);
		}
	}
}
