package brain;

import java.util.Iterator;
import java.util.List;

import engine.ICategory;
import engine.IModel;
import engine.model.Direction;
import engine.model.Entity;
import engine.view.View;
import fsm.Mode;
import fsm.FSM;

public abstract class Bot implements engine.IBrain.IBot {

	public IModel m;
	public FSM fsm;
	public Boolean PacGum;
	protected Brain b;
	protected Entity e;
	protected Mode mode;
	protected View view;
	
	public long pacgumActivatedTime;
	

	public Bot(IModel m, Entity e) {
		this.m = m;
		this.e = e;
		this.PacGum = false;
		this.pacgumActivatedTime = 0;
		e.bot = this;

	}

	public Boolean getPac() {
		return this.PacGum;
	}

	public void setPac(Boolean a) {
		this.PacGum = a;
		if (a) {
			pacgumActivatedTime = System.currentTimeMillis();
		}
	}

	abstract public void think(int elapsed);

	public void turn(Direction d) {

		int angle;
		if (d.isRelative()) {
			angle = d.degrees();
		} else {
			angle = d.degrees() - e.orientation();

		}

		// System.out.println("angle orientaton" + e.orientation());
		e.rotate(angle);
	}

	public void move(Direction d) {

		int dx = 0, dy = 0;
		int angle = d.isRelative() ? (e.orientation() + d.degrees()) % 360 : d.degrees();
		switch ((angle + 360) % 360) {
		case 0:
			dx = 1;
			dy = 0;
			break;
		case 90:
			dx = 0;
			dy = 1;
			break;
		case 180:
			dx = -1;
			dy = 0;
			break;
		case 270:
			dx = 0;
			dy = -1;
			break;
		default:
			break;
		}

		e.stunt.move((float) dy, (float) dx);

	}

	public void move2(Direction d) {

		if (e != null) {
			int angle = d.degrees();
			double radians = Math.toRadians(angle);

			float dx = (float) Math.cos(radians);
			float dy = (float) Math.sin(radians);
			e.stunt.move(dx, dy);
		}
	}

	public List<Entity> cell(Direction d) {
		int dx = 0, dy = 0;
		int angle = d.isRelative() ? (e.orientation() + d.degrees()) % 360 : d.degrees();

		switch ((angle + 360) % 360) {
		case 0:
			dx = 1;
			dy = 0;
			break;
		case 90:
			dx = 0;
			dy = 1;
			break;
		case 180:
			dx = -1;
			dy = 0;
			break;
		case 270:
			dx = 0;
			dy = -1;
			break;
		}

		int row = e.row() + dy;
		int col = e.col() + dx;

		return m.entitiesAt(row, col);
	}

	public Entity cell(Direction d, ICategory c) {
		List<Entity> entities = cell(d);
		for (Entity ent : entities) {
			if (ent.category().specializes((Category) c)) {
				return ent;
			}
		}
		return null;
	}

	protected Entity closest(ICategory c) {
		Entity closest = null;
		double minDist = Double.MAX_VALUE;
		for (Iterator<Entity> it = m.iterator(); it.hasNext();) {
			Entity ent = it.next();
			if (ent != e && ent.category().specializes((Category) c)) {
				double dist = Math.pow(ent.row() - e.row(), 2) + Math.pow(ent.col() - e.col(), 2);
				if (dist < minDist) {
					minDist = dist;
					closest = ent;
				}
			}
		}
		return closest;
	}

	public void setMode(Mode m) {
		this.mode = m;
	}

	public Mode getMode() {
		return mode;
	}

	public void setView(View view) {
		this.view = view;
	}

	public Entity getEntity() {
		return e;
	}

}