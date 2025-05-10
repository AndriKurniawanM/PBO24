package Breakout;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Brick {
    double x, y;
    boolean destroyed = false;

    public Brick(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void draw(GraphicsContext gc) {
        if (!destroyed) {
            gc.setFill(Color.ORANGE);
            gc.fillRect(x, y, Commons.BRICK_WIDTH, Commons.BRICK_HEIGHT);
        }
    }

    public boolean isHit(double bx, double by) {
        return !destroyed &&
                bx >= x && bx <= x + Commons.BRICK_WIDTH &&
                by >= y && by <= y + Commons.BRICK_HEIGHT;
    }
}
