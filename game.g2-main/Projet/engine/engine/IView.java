package engine;

import oop.graphics.Canvas;
import java.awt.Graphics2D;

import engine.model.Entity;

public interface IView {

	void focus(int px, int py);

	public void paint(Canvas canvas, Graphics2D g);

	public void Z(int acc);

	public void debugging_opt(int x);

	public Canvas canvas();

	public int mX();

	public int mY();
	
	public void set_playerDeath(boolean b);
	
	public void set_gameOver(boolean b);
	
	public void set_gameWon(boolean b);
	
	public void birth(Entity e);

	public void death(Entity e);

}
