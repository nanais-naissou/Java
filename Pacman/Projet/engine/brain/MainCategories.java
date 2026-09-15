package brain;

public class MainCategories {
    public static final Category ANYTHING = new Category("Anything", null);
    public static final Category TEAM = new Category("Team", ANYTHING);
    public static final Category PLAYER = new Category("Player", TEAM);
    public static final Category ADVERSARY = new Category("Adversary", ANYTHING);
    public static final Category ITEM = new Category("Item", ANYTHING);
    public static final Category VOID = new Category("Void", null);
    public static final Category OBSTACLE = new Category("Obstacle", ANYTHING);
   
}
