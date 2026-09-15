package game.Stunts;

import engine.model.Model;
import engine.model.Obstacle;
import engine.model.StuntGrid;

public class ObstacleStunt extends StuntGrid {
    private Obstacle obstacle;

    public ObstacleStunt(Model m, Obstacle obstacle) {
        super(m, obstacle);
        this.obstacle = obstacle;
    }

    @Override
    public boolean move(int nrows, int ncols) {
        return super.move(nrows, ncols);
    }

    @Override
    public boolean move(float dx, float dy) {
        return false;
    }

    // Directional helpers
    public void left()  { move(0, -1); }
    public void right() { move(0, 1); }
    public void up()    { move(-1, 0); }
    public void down()  { move(1, 0); }

    public void rotate(int angle) {
        super.rotate(angle); 
    }

    @Override
    public void tick(int elapsed) {
        super.tick(elapsed);
    }

}
