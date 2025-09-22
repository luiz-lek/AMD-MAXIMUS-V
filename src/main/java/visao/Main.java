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
//teste comentario
*/

/*
loco 1000 //Endereço do início do vetor
stod inicio_vetor
loco 10 //Tamanho do vetor
stod tam_vetor
loco 1
stod incrementador
loco 0
stod i //I inicial do loop
lodd inicio_vetor
swap
stod sp_inicial //Armazenaa aposição do sp, para ser reajustado no final
loop: lodd tam_vetor //Verificação de fim do loop
subd i
jzer end_loop //Fim verificação
lodd inicio_vetor //Cálculo posição atual do vetor
addd i
swap
lodd i
push
addd incrementador //Incrementa i
stod i
jump loop
end_loop: lodd sp_inicial
swap
end:
*/