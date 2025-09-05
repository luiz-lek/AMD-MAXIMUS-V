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
loco 6
push
loco 1
push
loco 0
push
push
loco 1
push
loop: lodl 4
subl 3
stol 4
jnze end
lodl 0
stol 2
addl 1
stol 0
lodl 2
stol 1
jump loop
end:

1000111111111011
1000111111111011
*/

/*
loco 6
stod contador
loco 0
stod soma
stod aux
loco 1
stod anterior
stod decrementador
loop: lodd contador
subd decrementador
jzer end
stod contador
lodd soma
stod aux
addd anterior
stod soma
loco aux
stod anterior
jump loop
end: lodd soma
push
 */