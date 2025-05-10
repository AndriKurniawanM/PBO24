package Space;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SpaceInvader extends Application {
    @Override
    public void start(Stage stage) {
        Board board = new Board();
        StackPane root = new StackPane(board);
        Scene scene = new Scene(root, Commons.BOARD_WIDTH, Commons.BOARD_HEIGHT);
        stage.setTitle("Space Invaders - JavaFX");
        stage.setScene(scene);
        stage.show();
        board.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}

