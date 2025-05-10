package Tugas1;
// Import semua class yang diperlukan dari JavaFX
import javafx.application.Application;         // Kelas utama JavaFX
import javafx.geometry.Pos;
import javafx.scene.Scene;                     // Wadah tampilan
import javafx.scene.control.Button;            // Komponen tombol
import javafx.scene.control.Label;             // Komponen label teks
import javafx.scene.layout.VBox;               // Layout vertikal
import javafx.stage.Stage;                     // Window utama
import javafx.application.Application;       // Base class JavaFX
import javafx.scene.Scene;                 // Wadah tampilan
import javafx.scene.control.Button;        // Komponen tombol
import javafx.scene.control.Label;         // Komponen label
import javafx.scene.layout.VBox;           // Layout vertikal
import javafx.scene.paint.Color;           // Warna
import javafx.scene.text.Font;             // Mengatur font
import javafx.stage.Stage;                 // Window utama
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Tugas1 extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 1. Label awal dengan font lebih besar dan warna lembut
        Label label = new Label("Silakan tekan tombol!");
        label.setFont(new Font("Arial", 20));
        label.setTextFill(Color.DARKSLATEGRAY);

        // 2. Tombol dengan styling pastel yang nyaman di mata
        Button tombol = new Button("Klik Saya");
        tombol.setFont(new Font("Verdana", 16));
        tombol.setTextFill(Color.WHITE);
        tombol.setStyle("-fx-background-color: #6c757d; -fx-background-radius: 8;");

        // 3. VBox sebagai root, center alignment, padding dan spacing
        VBox root = new VBox(15);
        root.setAlignment(Pos.CENTER);  // Center semua komponen
        root.setStyle("-fx-background-color: #f0f4f8;");  // Latar pastel biru muda
        root.setPadding(new Insets(30));
        root.getChildren().addAll(label, tombol);

        // 4. Event: klik tombol mengubah teks dan warna label
        tombol.setOnAction(e -> {
            label.setText("Tombol ditekan!");
            label.setTextFill(Color.SEAGREEN);
        });

        // 5. Scene dan stage
        Scene scene = new Scene(root, 400, 250);
        primaryStage.setTitle("Tugas1");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
