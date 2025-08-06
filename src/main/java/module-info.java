module com.example.amdmaximusv {
    requires javafx.controls;
    requires javafx.fxml;


    opens Front to javafx.fxml;
    exports Front;
}