package Tugas6;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puzzle extends Application {
    private static final int COLS = 3;
    private static final int ROWS = 4;
    private List<Piece> pieces = new ArrayList<>();
    private GridPane grid;
    private int tileWidth, tileHeight;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        // Load source image
        Image source = new Image(new FileInputStream("E:\\PBO\\Praktikum4\\src\\main\\img.png"));
        tileWidth = (int) (source.getWidth() / COLS);
        tileHeight = (int) (source.getHeight() / ROWS);

        grid = new GridPane();
        grid.setHgap(0);
        grid.setVgap(0);

        // Create pieces
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int idx = r * COLS + c;
                Button btn = new Button();
                btn.setPrefSize(tileWidth, tileHeight);

                // Last piece is empty
                if (idx < ROWS * COLS - 1) {
                    WritableImage tile = new WritableImage(
                            source.getPixelReader(),
                            c * tileWidth, r * tileHeight,
                            tileWidth, tileHeight);
                    ImageView iv = new ImageView(tile);
                    btn.setGraphic(iv);
                }

                Piece p = new Piece(btn, r, c);
                pieces.add(p);
            }
        }

        // Shuffle and add to grid
        shuffle();
        for (Piece p : pieces) {
            grid.add(p.button, p.col, p.row);
            p.button.setOnAction(e -> {
                handleClick(p);
            });
        }

        Scene scene = new Scene(grid);
        primaryStage.setTitle("Puzzle");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void shuffle() {
        // Randomize order
        Collections.shuffle(pieces);
        // Update positions in grid
        int i = 0;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Piece p = pieces.get(i++);
                p.row = r;
                p.col = c;
            }
        }
    }

    private void handleClick(Piece p) {
        Piece empty = pieces.stream()
                .filter(Piece::isEmpty)
                .findFirst()
                .orElse(null);
        if (empty != null && isAdjacent(p, empty)) {
            swap(p, empty);
            if (isSolved()) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Congratulations");
                alert.setHeaderText(null);
                alert.setContentText("Finished!");
                alert.showAndWait();
            }
        }
    }

    private boolean isAdjacent(Piece p1, Piece p2) {
        int dr = Math.abs(p1.row - p2.row);
        int dc = Math.abs(p1.col - p2.col);
        return dr + dc == 1;
    }

    private void swap(Piece p1, Piece p2) {
        // Swap in list
        int i1 = pieces.indexOf(p1);
        int i2 = pieces.indexOf(p2);
        Collections.swap(pieces, i1, i2);

        // Swap coordinates
        int r = p1.row;
        int c = p1.col;
        p1.row = p2.row;
        p1.col = p2.col;
        p2.row = r;
        p2.col = c;

        // Update GridPane
        GridPane.setRowIndex(p1.button, p1.row);
        GridPane.setColumnIndex(p1.button, p1.col);
        GridPane.setRowIndex(p2.button, p2.row);
        GridPane.setColumnIndex(p2.button, p2.col);
    }

    private boolean isSolved() {
        return pieces.stream().allMatch(Piece::isInOriginalPosition);
    }

    // Helper class for puzzle pieces
    private static class Piece {
        Button button;
        int originalRow, originalCol;
        int row, col;

        Piece(Button button, int originalRow, int originalCol) {
            this.button = button;
            this.originalRow = originalRow;
            this.originalCol = originalCol;
            this.row = originalRow;
            this.col = originalCol;
        }

        boolean isEmpty() {
            return button.getGraphic() == null;
        }

        boolean isInOriginalPosition() {
            return row == originalRow && col == originalCol;
        }
    }
}

