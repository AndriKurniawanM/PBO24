package Breakout;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Ball {
    double x, y, dx = 1, dy = -1;

    public Ball(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillOval(x, y, Commons.BALL_RADIUS * 2, Commons.BALL_RADIUS * 2);
    }

    public void reverseX() { dx = -dx; }
    public void reverseY() { dy = -dy; }
}
