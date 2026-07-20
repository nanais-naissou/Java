package game.Stunts;

import java.util.List;

import engine.model.Adversary;
import engine.model.Entity;
import engine.model.Model;
import engine.model.Stunt;
import engine.model.StuntFluide;
import game.Categories;
import game.Entities.Projectile;

public class ProjectileStunt extends StuntFluide {
	private Projectile projectile;
	private float dx, dy;
	boolean debugg = false;

	public ProjectileStunt(Model m, Projectile projectile, float dx, float dy) {
		super(m, projectile);
		this.projectile = projectile;
		this.dx = dx;
		this.dy = dy;
	}

	@Override
	public boolean move(int nrows, int ncols) {
		return false;
	}

	@Override
	public boolean move(float dx, float dy) {
		return super.move(dx, dy);
	}

	public void moveFree(float dx, float dy) {
		float m_x_tmp = projectile.x() + dx;
		float m_y_tmp = projectile.y() + dy;
//		projectile.m_x_cm = m_x_tmp;
//    	projectile.m_y_cm = m_y_tmp;
		if (m.config().tore) {
			m_x_tmp = m.normalizeFluid(m_x_tmp, m.ncols() * m.cellH_cm());
			m_y_tmp = m.normalizeFluid(m_y_tmp, m.nrows() * m.cellH_cm());
		} else {
			if (m_x_tmp < 0 || m_x_tmp >= m.ncols() * m.cellH_cm() || m_y_tmp < 0
					|| m_y_tmp >= m.nrows() * m.cellH_cm()) {
				return;
			}
		}
		Entity hit = collisionAt(m_x_tmp, m_y_tmp);
		if (hit != null && hit != projectile) {
			projectile.m_view().death(projectile);
			m.removeEntity(projectile);
			if (hit instanceof engine.model.Adversary) {
				engine.model.Adversary adv = (engine.model.Adversary) hit;
				if (debugg) {
					System.out.println("Health of ghost before hit: " + adv.getHealth());
				}
				adv.setHealth(adv.getHealth() - 1);
				if (adv.getHealth() <= 0) {
					System.out.println("Dead");
                    

				}

				if (debugg) {
					System.out.println("Health of ghost after hit: " + adv.getHealth());
				}
			}
			return;
		}
		if (isOutOfBounds()) {
			m.removeEntity(projectile);
		}

		projectile.m_x_cm = m_x_tmp;
		projectile.m_y_cm = m_y_tmp;

	}

	@Override
	public void rotate(int angle) {
	}

	@Override
	public void tick(int elapsed) {
		moveFree(dx * 5 * elapsed, dy * 5 * elapsed);
		

//		// Check collision with any entity
//		Entity hit = collisionAt(projectile.m_x_cm, projectile.m_y_cm);
//		if (hit != null && hit != projectile) {
//			projectile.m_view().death(projectile);
//
//			m.removeEntity(projectile);
//			m.removeEntity(hit);
//			return;
//		}
//
//		if (isOutOfBounds()) {
//			m.removeEntity(projectile);
//		}
	}

	private boolean isOutOfBounds() {
		return projectile.m_x_cm < 0 || projectile.m_y_cm < 0 || projectile.m_x_cm > m.ncols() * m.cellW_cm()
				|| projectile.m_y_cm > m.nrows() * m.cellH_cm();
	}

	private Entity collisionAt(float x, float y) {
		int row = (int) (y / m.cellH_cm());
		int col = (int) (x / m.cellW_cm());
		List<Entity> entities = m.entitiesAt(row, col);
		for (Entity e : entities) {
			if (debugg) {
//			System.out.println("collision at " + row + " , " + col + " with " + (e.category() == null ? "null" : e.category().toString()));
			}
			if (e != null) {
				if (debugg) {
					System.out.println("entity not null");
				}
				if (e.category() == Categories.ADVERSARY || e.category() == Categories.OBSTACLE) {
					if (debugg) {
						System.out.println("collision at " + row + " , " + col + " with " + e.category().toString());
					}
					return e;
				}
			}
		}
		return null;

	}

}
