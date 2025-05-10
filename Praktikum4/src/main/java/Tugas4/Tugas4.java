package Tugas4;

import javafx.application.Application;              // Base class JavaFX
import javafx.geometry.Insets;                     // Padding untuk layout
import javafx.geometry.Pos;                        // Align center
import javafx.scene.Scene;                         // Wadah tampilan
import javafx.scene.control.Button;                // Tombol aksi
import javafx.scene.control.ComboBox;               // Dropdown pilihan
import javafx.scene.control.Label;                 // Teks label
import javafx.scene.control.TextField;             // Input teks
import javafx.scene.layout.GridPane;               // Layout grid
import javafx.stage.Stage;                         // Window utama

public class Tugas4 extends Application {
    // Rate nilai tukar: 1 USD setara dengan 17.000 IDR
    private static final double RATE = 17000;

    @Override
    public void start(Stage primaryStage) {
        // 1. Komponen input nilai
        Label lblValue = new Label("Jumlah:");               // Label untuk kolom input nilai
        TextField tfValue = new TextField();                   // TextField sebagai tempat input angka
        tfValue.setPromptText("Masukkan angka");             // Placeholder teks saat kosong

        // 2. Dropdown pilihan 'Dari' (unit asal)
        Label lblFrom = new Label("Dari:");                  // Label untuk ComboBox sumber mata uang
        ComboBox<String> cbFrom = new ComboBox<>();           // ComboBox untuk unit asal
        cbFrom.getItems().addAll("USD", "IDR");            // Tambahkan opsi USD dan IDR
        cbFrom.setValue("USD");                              // Set default pilihan USD

        // 3. Dropdown pilihan 'Ke' (unit tujuan)
        Label lblTo = new Label("Ke:");                      // Label untuk ComboBox target mata uang
        ComboBox<String> cbTo = new ComboBox<>();             // ComboBox untuk unit tujuan
        cbTo.getItems().addAll("IDR", "USD");              // Tambahkan opsi IDR dan USD
        cbTo.setValue("IDR");                                // Set default pilihan IDR

        // 4. Tombol Konversi dan Label hasil
        Button btnConvert = new Button("Konversi");          // Tombol untuk menjalankan konversi
        Label lblResult = new Label();                         // Label kosong untuk menampilkan hasil

        // 5. Event handler: logika konversi saat tombol diklik
        btnConvert.setOnAction(e -> {
            try {
                double input = Double.parseDouble(tfValue.getText().trim()); // Ambil dan parsing input
                String from = cbFrom.getValue();            // Unit sumber
                String to   = cbTo.getValue();              // Unit tujuan
                double result;

                if (from.equals("USD") && to.equals("IDR")) {
                    // Konversi USD ke IDR
                    result = usdToIdr(input);
                    lblResult.setText(String.format(
                            "%.2f USD = %.2f IDR", input, result
                    ));
                } else if (from.equals("IDR") && to.equals("USD")) {
                    // Konversi IDR ke USD
                    result = idrToUsd(input);
                    lblResult.setText(String.format(
                            "%.2f IDR = %.4f USD", input, result
                    ));
                } else {
                    // Kasus unit sama atau tidak sesuai opsi
                    lblResult.setText("Pilih konversi yang valid!");
                }

            } catch (NumberFormatException ex) {
                // Jika input bukan angka
                lblResult.setText("Masukkan angka valid!");
            }
        });

        // 6. Layout GridPane: mengatur posisi komponen dalam baris dan kolom
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(15));           // Padding di sekeliling grid
        grid.setHgap(10);                          // Jarak horizontal antar kolom
        grid.setVgap(10);                          // Jarak vertikal antar baris
        grid.setAlignment(Pos.CENTER);             // Center-align seluruh konten

        // Menambahkan komponen ke grid dengan koordinat (kolom, baris)
        grid.add(lblValue,    0, 0);
        grid.add(tfValue,     1, 0);
        grid.add(lblFrom,     0, 1);
        grid.add(cbFrom,      1, 1);
        grid.add(lblTo,       0, 2);
        grid.add(cbTo,        1, 2);
        grid.add(btnConvert,  0, 3);
        grid.add(lblResult,   1, 3);

        // 7. Buat dan tampilkan scene
        Scene scene = new Scene(grid, 400, 250);  // Ukuran window: lebar 400, tinggi 250
        primaryStage.setTitle("Tugas 4: Konversi Mata Uang");
        primaryStage.setScene(scene);             // Pasang scene ke stage
        primaryStage.show();                      // Tampilkan stage
    }

    /**
     * Method untuk mengubah USD ke IDR
     */
    private double usdToIdr(double usd) {
        return usd * RATE;
    }

    /**
     * Method untuk mengubah IDR ke USD
     */
    private double idrToUsd(double idr) {
        return idr / RATE;
    }

    /**
     * Main method: entry point untuk jalankan aplikasi JavaFX
     */
    public static void main(String[] args) {
        launch(args);
    }
}

