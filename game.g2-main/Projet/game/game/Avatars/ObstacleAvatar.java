package game.Avatars;

import engine.model.Entity;
import game.Generator;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class ObstacleAvatar extends engine.view.Avatar {

	protected int cellW_cm = 100;
	protected int cellH_cm = 100;

	public ObstacleAvatar(Entity e) {
		super(e);
	}

	@Override
	public void render(Graphics2D g, float cellW, float cellH) {
		int x_cm = Math.round(e.x());
		int y_cm = Math.round(e.y());
		int size = cellW_cm / 2;

		int row = e.row();
		int col = e.col();

		AffineTransform saved = g.getTransform();
		g.translate(x_cm, y_cm);

		g.setColor(new Color(10, 10, 20));
		g.fillRect(-size, -size, size * 2, size * 2);

		g.setStroke(new BasicStroke(3f));
		g.setColor(new Color(0, 191, 255)); // bleu néon

		if (isExposed(row - 1, col)) // haut
			g.drawLine(-size, -size, size, -size);
		if (isExposed(row + 1, col)) // bas
			g.drawLine(-size, size, size, size);
		if (isExposed(row, col - 1)) // gauche
			g.drawLine(-size, -size, -size, size);
		if (isExposed(row, col + 1)) // droite
			g.drawLine(size, -size, size, size);

		if (isExposed(row - 1, col) && isExposed(row, col - 1)) // haut-gauche
			g.drawArc(-size, -size, size, size, 90, 90);
		if (isExposed(row - 1, col) && isExposed(row, col + 1)) // haut-droit
			g.drawArc(0, -size, size, size, 0, 90);
		if (isExposed(row + 1, col) && isExposed(row, col - 1)) // bas-gauche
			g.drawArc(-size, 0, size, size, 180, 90);
		if (isExposed(row + 1, col) && isExposed(row, col + 1)) // bas-droit
			g.drawArc(0, 0, size, size, 270, 90);

		g.setTransform(saved);
	}

	private boolean isExposed(int row, int col) {
		char[][] maze = Generator.STATIC_MAZE;
		if (maze == null)
			return true;
		if (row < 0 || row >= maze.length || col < 0 || col >= maze[0].length)
			return true;
		return maze[row][col] != '#';
	}
}
