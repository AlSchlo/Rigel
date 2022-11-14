module ch.epfl.rigel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens ch.epfl.rigel to javafx.fxml;
    exports ch.epfl.rigel.gui;
}