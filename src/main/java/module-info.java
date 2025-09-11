module com.example.amdmaximusv {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported.desktop;
    requires java.sql;


    opens visao to javafx.fxml;
    exports visao;
}