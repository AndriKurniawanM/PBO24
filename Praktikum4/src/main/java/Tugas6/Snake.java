package Tugas6;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Kelas BoardFX: game Snake menggunakan JavaFX.
 * - Extend Application untuk lifecycle JavaFX.
 * - Gunakan Canvas dan Timeline untuk loop game.
 */
public class Snake extends Application {

    // Ukuran papan dan dot
    private static final int B_WIDTH = 300;
    private static final int B_HEIGHT = 300;
    private static final int DOT_SIZE = 10;
    private static final int ALL_DOTS = (B_WIDTH / DOT_SIZE) * (B_HEIGHT / DOT_SIZE);
    private static final int RAND_POS = B_WIDTH / DOT_SIZE;
    private static final int DELAY = 140;  // ms per frame

    // Model snake dan apple
    private Point2D[] body = new Point2D[ALL_DOTS];   // array koordinat snake
    private int dots;                                 // panjang snake
    private Point2D apple;                            // posisi apel

    // Arah gerak
    private enum Direction { UP, DOWN, LEFT, RIGHT }
    private Direction dir = Direction.RIGHT;
    private boolean running = false;

    // JavaFX elements
    private Canvas canvas;
    private GraphicsContext gc;
    private Timeline timeline;

    @Override
    public void start(Stage stage) {
        // Inisialisasi Canvas
        canvas = new Canvas(B_WIDTH, B_HEIGHT);
        gc = canvas.getGraphicsContext2D();

        // Setup scene dan input
        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> {
            KeyCode code = e.getCode();
            switch (code) {
                case LEFT:  if (dir != Direction.RIGHT) dir = Direction.LEFT;  break;
                case RIGHT: if (dir != Direction.LEFT)  dir = Direction.RIGHT; break;
                case UP:    if (dir != Direction.DOWN)  dir = Direction.UP;    break;
                case DOWN:  if (dir != Direction.UP)    dir = Direction.DOWN;  break;
                default: break;
            }
        });

        stage.setScene(scene);
        stage.setTitle("Snake Game JavaFX");
        stage.show();

        // Mulai game
        initGame();
        initLoop();
    }

    // Inisialisasi kondisi awal snake dan apel
    private void initGame() {
        dots = 3;
        // Set posisi awal di tengah
        int startX = B_WIDTH / 2;
        int startY = B_HEIGHT / 2;
        for (int i = 0; i < dots; i++) {
            body[i] = new Point2D(startX - i * DOT_SIZE, startY);
        }
        placeApple();
        running = true;
    }

    // Setup loop game menggunakan Timeline
    private void initLoop() {
        timeline = new Timeline(new KeyFrame(Duration.millis(DELAY), e -> {
            if (running) {
                update();
                render();
            } else {
                timeline.stop();
                renderGameOver();
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Pindahkan apel ke posisi acak
    private void placeApple() {
        int r = (int) (Math.random() * RAND_POS) * DOT_SIZE;
        apple = new Point2D(r, (int) (Math.random() * RAND_POS) * DOT_SIZE);
    }

    // Update logika permainan
    private void update() {
        // Hitung kepala baru
        Point2D head = body[0];
        Point2D newHead = switch (dir) {
            case UP    -> head.add(0, -DOT_SIZE);
            case DOWN  -> head.add(0, DOT_SIZE);
            case LEFT  -> head.add(-DOT_SIZE, 0);
            case RIGHT -> head.add(DOT_SIZE, 0);
        };
        // Cek collision
        if (newHead.getX() < 0 || newHead.getX() >= B_WIDTH
                || newHead.getY() < 0 || newHead.getY() >= B_HEIGHT) {
            running = false;
            return;
        }
        for (int i = dots; i > 0; i--) {
            body[i] = body[i - 1];
        }
        body[0] = newHead;
        // Check apple
        if (newHead.equals(apple)) {
            dots++;
            placeApple();
        }
    }

    // Render semua elemen game
    private void render() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, B_WIDTH, B_HEIGHT);
        // Gambar apel merah
        gc.setFill(Color.RED);
        gc.fillOval(apple.getX(), apple.getY(), DOT_SIZE, DOT_SIZE);
        // Gambar snake hijau
        gc.setFill(Color.LIME);
        for (int i = 0; i < dots; i++) {
            gc.fillRect(body[i].getX(), body[i].getY(), DOT_SIZE, DOT_SIZE);
        }
    }

    // Render teks Game Over
    private void renderGameOver() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, B_WIDTH, B_HEIGHT);
        gc.setFill(Color.WHITE);
        gc.fillText("Game Over", B_WIDTH/2 - 30, B_HEIGHT/2);
    }

    public static void main(String[] args) {
        launch();
    }
}
