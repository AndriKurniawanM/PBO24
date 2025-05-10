package Breakout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class BreakOut extends Application {

    @Override
    public void start(Stage stage) {
        Board board = new Board();
        StackPane root = new StackPane(board);
        Scene scene = new Scene(root, Commons.WIDTH, Commons.HEIGHT);

        stage.setTitle("Breakout Game - JavaFX");
        stage.setScene(scene);
        stage.show();

        board.requestFocus(); // penting untuk input keyboard
    }

    public static void main(String[] args) {
        launch();
    }
}
