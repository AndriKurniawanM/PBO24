package Tetris;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tetris extends Application {

    @Override
    public void start(Stage stage) {
        Board board = new Board();
        StackPane root = new StackPane(board);
        Scene scene = new Scene(root, Commons.WINDOW_WIDTH, Commons.WINDOW_HEIGHT);

        stage.setTitle("Tetris - JavaFX");
        stage.setScene(scene);
        stage.show();

        board.requestFocus();  // penting agar key events diterima
    }

    public static void main(String[] args) {
        launch();
    }
}

