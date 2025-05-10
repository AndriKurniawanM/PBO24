package Tugas2;

import javafx.application.Application;              // Base class JavaFX
import javafx.collections.FXCollections;           // Utility untuk membuat daftar observable
import javafx.collections.ObservableList;          // List yang auto‑update ke GUI
import javafx.geometry.Insets;                     // Untuk memberi padding/margin
import javafx.scene.Scene;                         // Wadah tampilan aplikasi
import javafx.scene.control.Button;                // Komponen tombol
import javafx.scene.control.ComboBox;               // Dropdown pilihan
import javafx.scene.control.Label;                 // Komponen teks
import javafx.scene.control.TextField;             // Komponen input teks
import javafx.scene.layout.GridPane;               // Layout berbasis grid
import javafx.stage.Stage;                         // Window utama

public class Tugas2 extends Application {
    @Override
    public void start(Stage primaryStage) {
        // 1. Daftar unit SI
        ObservableList<String> units = FXCollections.observableArrayList(
                "km", "hm", "dam", "m", "dm", "cm", "mm"
        );

        // 2. Komponen GUI
        Label lblFrom   = new Label("Dari:");
        ComboBox<String> cbFrom = new ComboBox<>(units);
        cbFrom.setValue("m");                    // Default: meter

        Label lblTo     = new Label("Ke:");
        ComboBox<String> cbTo = new ComboBox<>(units);
        cbTo.setValue("cm");                     // Default: centimeter
        Label lblValue  = new Label("Nilai:");
        TextField tfValue = new TextField();
        tfValue.setPromptText("Masukkan angka");

        Button btnConvert = new Button("Konversi");

        Label lblResult = new Label();           // Akan menampilkan hasil

        // 3. Event handler tombol Konversi
        btnConvert.setOnAction(e -> {
            try {
                double input = Double.parseDouble(tfValue.getText());
                String fromUnit = cbFrom.getValue();
                String toUnit   = cbTo.getValue();
                double meter = toMeter(input, fromUnit);
                double result = fromMeter(meter, toUnit);
                lblResult.setText(String.format("%.4f %s = %.4f %s",
                        input, fromUnit, result, toUnit));
            } catch (NumberFormatException ex) {
                lblResult.setText("Masukkan angka valid!");
            }
        });

        // 4. Layout GridPane: 2 kolom, beberapa baris
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));         // Jarak ke tepi window
        grid.setHgap(10);                        // Jarak antar kolom
        grid.setVgap(10);                        // Jarak antar baris

        // Tambahkan komponen ke grid: (kolom, baris)
        grid.add(lblValue,    0, 0);
        grid.add(tfValue,     1, 0);
        grid.add(lblFrom,     0, 1);
        grid.add(cbFrom,      1, 1);
        grid.add(lblTo,       0, 2);
        grid.add(cbTo,        1, 2);
        grid.add(btnConvert,  0, 3);
        grid.add(lblResult,   1, 3);

        // 5. Siapkan dan tampilkan scene
        Scene scene = new Scene(grid, 350, 200);
        primaryStage.setTitle("Tugas 2: Konversi Panjang SI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Konversi nilai 'value' dari unit 'unit' ke meter
    private double toMeter(double value, String unit) {
        switch (unit) {
            case "km":  return value * 1000;
            case "hm":  return value * 100;
            case "dam": return value * 10;
            case "m":   return value;
            case "dm":  return value * 0.1;
            case "cm":  return value * 0.01;
            case "mm":  return value * 0.001;
            default:    return 0;
        }
    }

    // Konversi nilai 'meter' ke unit 'unit'
    private double fromMeter(double meter, String unit) {
        switch (unit) {
            case "km":  return meter / 1000;
            case "hm":  return meter / 100;
            case "dam": return meter / 10;
            case "m":   return meter;
            case "dm":  return meter / 0.1;
            case "cm":  return meter / 0.01;
            case "mm":  return meter / 0.001;
            default:    return 0;
        }
    }

    // Main method untuk menjalankan dari IDE
    public static void main(String[] args) {
        launch(args);
    }
}

