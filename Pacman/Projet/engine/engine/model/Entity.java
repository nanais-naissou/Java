package engine.model;

import brain.Bot;
import engine.IBrain.IBot;
import engine.ICategory;
import engine.IModel;
import engine.view.Avatar;
import game.Stunts.PlayerStunt;

public abstract class Entity {
	public IModel m_model;
	public int m_row;
	public int m_col;
	public float m_x_cm, m_y_cm;
	protected int m_orientation;
	public Object avatar;
	public Stunt stunt;
	public StuntFluide StuntFluide;
	public Bot bot;
	public int spawnRow,spawnCol;
	public boolean inHisInitPos;

	public Entity(IModel m, int r, int c, int o) {
		m_model = m;
		m_row = r;
		m_col = c;
		spawnRow = r;
		spawnCol = c;
		m_x_cm = c * m.cellW_cm() + m.cellW_cm() / 2;
		m_y_cm = r * m.cellH_cm() + m.cellH_cm() / 2;
		m_orientation = normalize(o);
		this.inHisInitPos=true;
//		if (!(this instanceof Projectile)) 

	}

	// Normalize an angle back to the range [0:360[
	public int normalize(int angle) {
		int a = angle;

		while (a < 0) {
			a += 360;
		}

		while (a > 360) {
			a -= 360;
		}

		return a;
	}

	/*
	 * Rotate the entity by the given angle
	 */
	public void rotate(int theta) {
		m_orientation = normalize(orientation() + theta);
	}

	/*
	 * Get the entity to face the orientation match the given angle
	 */
	public void face(int theta) {
		m_orientation = normalize(theta);
	}

	public int orientation() {
		return m_orientation;
	}

	public int row() {
		return m_row;
	}

	public int col() {
		return m_col;
	}

	public float x() {
		return m_x_cm;
	}

	public float y() {
		return m_y_cm;
	}

	/*
	 * Move this entity in the model by the given count of rows and columns.
	 */
	public void move(int nrows, int ncols) {

		m_model.move(this, nrows, ncols);

	}

	public void moveFluid(float dx, float dy) {
		m_model.moveFluid(this, dx, dy);
		// m_row =

	}

	public abstract ICategory category();

	public void SetCelltoMeter(int nrow, int ncol) {
		this.m_x_cm = ncol * m_model.cellW_cm() + m_model.cellW_cm() / 2;
		this.m_y_cm = nrow * m_model.cellH_cm() + m_model.cellH_cm() / 2;
	}

	protected void at(int row, int col) {
		this.m_row = row;
		this.m_col = col;
	}

	public Stunt stunt() {
		return stunt;
	}

	public IModel m_model() {
		return m_model;
	}

}
