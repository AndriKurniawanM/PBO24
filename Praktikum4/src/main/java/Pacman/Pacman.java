package Pacman;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Pacman extends Application {

    @Override
    public void start(Stage stage) {
        Board board = new Board();
        StackPane root = new StackPane(board);
        Scene scene = new Scene(root, Commons.SCREEN_SIZE, Commons.SCREEN_SIZE+Commons.BLOCK_SIZE);
        stage.setTitle("Pac‑Man - JavaFX");
        stage.setScene(scene);
        stage.show();
        board.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}
