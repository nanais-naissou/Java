package engine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import engine.IModel.Config;
import engine.model.Entity;
import engine.model.Player;
import game.Entities.Apple;
import game.Entities.PacGum;
import game.Entities.Strawberry;
import oop.graphics.Canvas;

public interface IModel extends Iterable<Entity> {
	int ncols();

	int nrows();

	Player player();

	Iterator<Entity> iterator();

	List<Entity> entitiesAt(int r, int c);
	
	public List<Object[]> respawnListGhosts = new ArrayList<>();
	
	
	

	public class Config {
		public boolean tore;
		public boolean mode;
	}

	Config config();

	void config(Config c);

	public void addAt(Entity e);

	public boolean move(Entity e, int dRow, int dCol);

	public boolean moveFluid(Entity e, float dx, float dy);

	public double viseCursor(int mouseX, int mouseY, Player p, Canvas n_canvas);

	public boolean moveFluide(Entity e, float speed);

	public int cellW_cm();

	public int cellH_cm();

	public void register(IView v);

	public void unregister(IView v);

	public void removeEntity(Entity e);

	public float normalizeFluid(float value, int max);

	public int dotCount();

	public int fruitsCount();

	public int PacGumCount();
	
	public void setDoorCoords(List<int[]> coords);
	
	public List<int[]> getDoorCoords() ;
}
