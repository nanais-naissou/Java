package game.Entities;

import engine.ICategory;
import engine.IModel;
import engine.IView;
import engine.model.Entity;
import engine.model.Model;
import game.Stunts.ProjectileStunt;

public class Projectile extends Entity {
	ProjectileStunt m_stunt;
	private float rayon;
	IView m_view;
	boolean debugg = false;

	public Projectile(Model m, float x, float y, int angleDegrees, IView m_view) {

		super(m, (int) (y / m.cellH_cm), (int) (x / m.cellW_cm), angleDegrees);
		if (debugg) {
			System.out.println("[Projectile] created at x=" + x + " y=" + y);
		}
		this.m_view = m_view;

		this.rayon = 15;
		this.m_x_cm = x;
		this.m_y_cm = y;

		double angleRad = Math.toRadians(angleDegrees);
		float speed = 0.2f;
		float dx = (float) Math.cos(angleRad) * speed;
		float dy = (float) Math.sin(angleRad) * speed;

		m_stunt = new ProjectileStunt(m, this, dx, dy);
	}
	// ... le reste ...

	@Override
	public ProjectileStunt stunt() {
		return m_stunt;
	}

	public float rayon() {
		return rayon;
	}

	public IView m_view() {
		return m_view;
	}

	@Override
	public ICategory category() {
		return game.Categories.PROJECTILE;
	}
}
