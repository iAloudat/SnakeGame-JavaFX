module snakegame.v5 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens snakegame to javafx.fxml;
    exports snakegame;
}