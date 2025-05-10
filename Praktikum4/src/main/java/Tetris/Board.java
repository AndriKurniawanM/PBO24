package Tetris;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import Tetris.Shape.Tetrominoe;

public class Board extends Canvas implements Commons {
    private final GraphicsContext gc = getGraphicsContext2D();
    private Tetrominoe[] board = new Tetrominoe[BOARD_WIDTH * BOARD_HEIGHT];
    private Shape curPiece;
    private int curX, curY;
    private boolean isPaused = false;
    private boolean isFallingFinished = false;
    private int numLinesRemoved = 0;

    public Board() {
        super(WINDOW_WIDTH, WINDOW_HEIGHT);
        clearBoard();
        newPiece();

        Timeline gameLoop = new Timeline(
                new KeyFrame(Duration.millis(GAME_SPEED_MS), e -> gameCycle())
        );
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        setFocusTraversable(true);
        setOnKeyPressed(e -> {
            if (curPiece.getShape() == Tetrominoe.NoShape) return;
            KeyCode code = e.getCode();
            switch(code) {
                case P -> togglePause();
                case LEFT  -> tryMove(curPiece, curX - 1, curY);
                case RIGHT -> tryMove(curPiece, curX + 1, curY);
                case DOWN  -> tryMove(curPiece.rotateRight(), curX, curY);
                case UP    -> tryMove(curPiece.rotateLeft(), curX, curY);
                case SPACE -> dropDown();
                case D     -> oneLineDown();
                default -> {}
            }
        });
    }

    private void gameCycle() {
        if (!isPaused) {
            if (isFallingFinished) {
                isFallingFinished = false;
                newPiece();
            } else {
                oneLineDown();
            }
        }
        doDrawing();
    }

    private void clearBoard() {
        for (int i = 0; i < board.length; i++) {
            board[i] = Tetrominoe.NoShape;
        }
    }

    private void newPiece() {
        curPiece = new Shape();
        curPiece.setRandomShape();
        curX = BOARD_WIDTH / 2 + 1;
        curY = BOARD_HEIGHT - 1 + curPiece.minY();
        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(Tetrominoe.NoShape);
        }
    }

    private boolean tryMove(Shape newPiece, int newX, int newY) {
        for (int i = 0; i < 4; i++) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BOARD_WIDTH || y < 0 || y >= BOARD_HEIGHT)
                return false;
            if (board[y * BOARD_WIDTH + x] != Tetrominoe.NoShape)
                return false;
        }
        curPiece = newPiece;
        curX = newX; curY = newY;
        return true;
    }

    private void dropDown() {
        int newY = curY;
        while (newY > 0 && tryMove(curPiece, curX, newY - 1)) {
            newY--;
        }
        pieceDropped();
    }

    private void oneLineDown() {
        if (!tryMove(curPiece, curX, curY - 1)) {
            pieceDropped();
        }
    }

    private void pieceDropped() {
        for (int i = 0; i < 4; i++) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[y * BOARD_WIDTH + x] = curPiece.getShape();
        }
        removeFullLines();
        isFallingFinished = true;
    }

    private void removeFullLines() {
        int lines = 0;
        for (int i = BOARD_HEIGHT - 1; i >= 0; i--) {
            boolean full = true;
            for (int j = 0; j < BOARD_WIDTH; j++) {
                if (board[i * BOARD_WIDTH + j] == Tetrominoe.NoShape) {
                    full = false; break;
                }
            }
            if (full) {
                lines++;
                System.arraycopy(board, (i+1)*BOARD_WIDTH,
                        board, i*BOARD_WIDTH,
                        (BOARD_HEIGHT - i - 1)*BOARD_WIDTH);
                for (int k = BOARD_WIDTH*(BOARD_HEIGHT-1);
                     k < BOARD_WIDTH*BOARD_HEIGHT; k++)
                    board[k] = Tetrominoe.NoShape;
                i++; // cek baris yang baru turun
            }
        }
        if (lines > 0) numLinesRemoved += lines;
    }

    private void togglePause() {
        isPaused = !isPaused;
    }

    private void doDrawing() {
        // clear
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        // gambar board
        for (int i = 0; i < BOARD_HEIGHT; i++) {
            for (int j = 0; j < BOARD_WIDTH; j++) {
                Tetrominoe shape = board[i * BOARD_WIDTH + j];
                if (shape != Tetrominoe.NoShape) {
                    drawBlock(j * BLOCK_SIZE,
                            (BOARD_HEIGHT - i - 1) * BLOCK_SIZE,
                            shape);
                }
            }
        }
        // gambar current piece
        if (curPiece.getShape() != Tetrominoe.NoShape) {
            for (int i = 0; i < 4; i++) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawBlock(x * BLOCK_SIZE,
                        (BOARD_HEIGHT - y - 1) * BLOCK_SIZE,
                        curPiece.getShape());
            }
        }
        // status
        gc.setFill(Color.WHITE);
        gc.fillText("Lines: " + numLinesRemoved, 5, 15);
        if (curPiece.getShape() == Tetrominoe.NoShape) {
            gc.fillText("Game Over", WINDOW_WIDTH/2 - 30, WINDOW_HEIGHT/2);
        }
    }

    private void drawBlock(int x, int y, Tetrominoe shape) {
        Color[] colors = {
                Color.BLACK, Color.SALMON, Color.LIGHTGREEN, Color.LIGHTBLUE,
                Color.GOLD, Color.MEDIUMPURPLE, Color.CORAL, Color.TURQUOISE
        };
        gc.setFill(colors[shape.ordinal()]);
        gc.fillRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
        gc.setStroke(colors[shape.ordinal()].darker());
        gc.strokeRect(x, y, BLOCK_SIZE, BLOCK_SIZE);
    }
}

