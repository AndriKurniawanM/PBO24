package Breakout;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Paddle {
    double x;

    public Paddle(double x) {
        this.x = x;
    }

    public void move(double dx) {
        x += dx;
        if (x < 0) x = 0;
        if (x > Commons.WIDTH - Commons.PADDLE_WIDTH) x = Commons.WIDTH - Commons.PADDLE_WIDTH;
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.BLUE);
        gc.fillRect(x, Commons.HEIGHT - Commons.PADDLE_HEIGHT - 10, Commons.PADDLE_WIDTH, Commons.PADDLE_HEIGHT);
    }

    public boolean isHit(double bx, double by) {
        return bx >= x && bx <= x + Commons.PADDLE_WIDTH &&
                by >= Commons.HEIGHT - Commons.PADDLE_HEIGHT - 10 &&
                by <= Commons.HEIGHT - 10;
    }
}

