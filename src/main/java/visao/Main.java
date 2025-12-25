package visao;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import static back.comum.Constantes.*;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA1));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        String css = getClass().getResource(PATH_CSS_TELA1).toExternalForm();
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
      PUSH
      LOCO 1
      STOD OP2
      PUSH
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