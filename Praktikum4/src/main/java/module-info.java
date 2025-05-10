module com.example.praktikum4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;

    opens com.example.praktikum4 to javafx.fxml;
    exports com.example.praktikum4;
    opens Tugas1 to javafx.fxml;
    exports Tugas1;
    opens Tugas2 to javafx.fxml;
    exports Tugas2;
    opens Tugas4 to javafx.fxml;
    exports Tugas4;
    opens Tugas6 to javafx.fxml;
    exports Tugas6;
    opens Breakout to javafx.fxml;
    exports Breakout;
    opens Tetris to javafx.fxml;
    exports Tetris;
    opens Pacman to javafx.fxml;
    exports Pacman;
    opens Sokoban to javafx.fxml;
    exports Sokoban;
    opens Space to javafx.fxml;
    exports Space;
}