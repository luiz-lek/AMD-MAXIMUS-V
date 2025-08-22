module com.example.amdmaximusv {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported.desktop;


    opens visao to javafx.fxml;
    exports visao;
}