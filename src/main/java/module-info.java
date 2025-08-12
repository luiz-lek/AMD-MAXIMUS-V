module com.example.amdmaximusv {
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.unsupported.desktop;


    opens Front to javafx.fxml;
    exports Front;
}