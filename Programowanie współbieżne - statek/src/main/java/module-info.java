module com.example.projektpw_statek {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.projektpw_statek to javafx.fxml;
    exports com.example.projektpw_statek;
}