package engine.view;

import java.awt.Graphics2D;

import engine.model.Entity;

public abstract class Avatar {
	protected Entity e;

	public Avatar(Entity e) {
		this.e = e;
		e.avatar = this; // Optionally link back
	}

	// Called by the View to render this avatar
	public abstract void render(Graphics2D g, float cellW, float cellH);
}
