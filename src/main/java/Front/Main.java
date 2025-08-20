package Front;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application{

    public static void main(String[] args){
        launch(args) ;
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String css = getClass().getResource("/css/StyleTela1.css").toExternalForm();
        scene.getStylesheets().add(css);
        primaryStage.setScene(scene);
        primaryStage.setTitle("AMD MAXIMUS-V");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

/*
loco 5
push
loco 6
push
pop
halt
*/