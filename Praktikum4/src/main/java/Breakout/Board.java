package Breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class Board extends Canvas implements Commons {
    private final GraphicsContext gc;
    private final Ball ball;
    private final Paddle paddle;
    private final Brick[][] bricks;
    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public Board() {
        super(WIDTH, HEIGHT);
        gc = getGraphicsContext2D();

        ball = new Ball(WIDTH / 2, HEIGHT / 2);
        paddle = new Paddle(WIDTH / 2 - PADDLE_WIDTH / 2);
        bricks = new Brick[BRICK_ROWS][BRICK_COLUMNS];

        for (int i = 0; i < BRICK_ROWS; i++) {
            for (int j = 0; j < BRICK_COLUMNS; j++) {
                bricks[i][j] = new Brick(j * (BRICK_WIDTH + 5) + 20, i * (BRICK_HEIGHT + 5) + 30);
            }
        }

        Timeline gameLoop = new Timeline(new KeyFrame(Duration.millis(10), e -> update()));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = true;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = true;
        });
        setOnKeyReleased(e -> {
            if (e.getCode() == KeyCode.LEFT) leftPressed = false;
            if (e.getCode() == KeyCode.RIGHT) rightPressed = false;
        });
    }

    private void update() {
        if (leftPressed) paddle.move(-2);
        if (rightPressed) paddle.move(2);

        ball.move();

        // Bounce off walls
        if (ball.x < 0 || ball.x > WIDTH - BALL_RADIUS * 2) ball.reverseX();
        if (ball.y < 0) ball.reverseY();

        // Hit paddle
        if (paddle.isHit(ball.x + BALL_RADIUS, ball.y + BALL_RADIUS * 2)) ball.reverseY();

        // Hit bricks
        for (Brick[] row : bricks) {
            for (Brick brick : row) {
                if (brick.isHit(ball.x + BALL_RADIUS, ball.y + BALL_RADIUS)) {
                    brick.destroyed = true;
                    ball.reverseY();
                    break;
                }
            }
        }

        // Game over
        if (ball.y > HEIGHT) {
            gc.setFill(Color.BLACK);
            gc.fillText("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
            return;
        }

        // Draw everything
        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        ball.draw(gc);
        paddle.draw(gc);
        for (Brick[] row : bricks) {
            for (Brick brick : row) brick.draw(gc);
        }
    }
}
