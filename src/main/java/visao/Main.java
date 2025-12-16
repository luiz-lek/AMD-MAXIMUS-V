package visao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class Main extends Application {
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

    public static void main(String[] args) { launch(args); }
}

/*
      LOCO 8
      STOD CONT
      LOCO 1
      STOD CONST1
      LOCO 0
      STOD OP1
      LOCO 1
      STOD OP2
LOOP: LODD OP1
      ADDD OP2
      PUSH
      LODD OP2
      STOD OP1
      LODL 0
      STOD OP2
      LODD CONT
      SUBD CONST1
      JZER END
      STOD CONT
      JUMP LOOP
 END:
*/