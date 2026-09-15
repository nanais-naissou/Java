package game.Avatars;

import engine.model.Entity;
import engine.model.Model;
import engine.view.Avatar;
import game.Entities.Projectile;

import java.awt.Graphics2D;
import java.awt.Color;

public class ProjectileAvatar extends Avatar {
	Projectile proj;

	public ProjectileAvatar(Entity e) {
		super(e);
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round((e.x()));
		int y_cm = Math.round((e.y()));
		if (e instanceof Projectile) {
			proj = (Projectile) e;

			Model m = (Model) proj.m_model;
			g.setColor(java.awt.Color.RED);
			g.fillOval(x_cm, y_cm, (int) proj.rayon(), (int) proj.rayon());
		}
	}

}
