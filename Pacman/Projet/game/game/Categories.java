package game;

import brain.Category;
import brain.MainCategories;

public class Categories extends MainCategories {

	public static final Category TREE = new Category("Tree", OBSTACLE);
	public static final Category ROCK = new Category("Rock", OBSTACLE);
	public static final Category WALL = new Category("Wall", OBSTACLE);

	public static final Category GHOST = new Category("Ghost", ADVERSARY);
	public static final Category ASSASSIN = new Category("Assassin", ADVERSARY);
	public static final Category PROJECTILE = new Category("Projectile", ITEM);
	public static final Category APPLE = new Category("Apple", ITEM);
	public static final Category STRAWBERRY = new Category("Strawberry", ITEM);
	public static final Category DOT = new Category("Dot", ITEM);
	public static final Category PACGUM = new Category("Pacgum", ITEM);

}
