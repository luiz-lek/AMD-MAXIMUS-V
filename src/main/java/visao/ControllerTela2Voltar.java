package visao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

import static back.comum.Constantes.*;

public class ControllerTela2Voltar {
    private Stage stage, stageTela2;
    private Scene scene;

    @FXML
    private Button confirmarVoltar, cancelarVoltar;
    @FXML
    private Label l1;

    String macroPrograma;

    @FXML
    public void voltarTela1 (ActionEvent event) throws IOException {
        this.fecharJanela(event);
        this.stageTela2.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA1));
        Parent root = loader.load();
        ControllerTela1 controllerTela1 = loader.getController();
        controllerTela1.setMacroPrograma(this.macroPrograma);
        this.stage = new Stage();
        this.scene = new Scene(root);
        String css = getClass().getResource(PATH_CSS_TELA1).toExternalForm();
        this.scene.getStylesheets().add(css);
        this.stage.setScene(this.scene);
        this.stage.setTitle("AMD MAXIMUS-V");
        this.stage.setResizable(false);
        this.stage.show();
    }

    @FXML
    private void fecharJanela(ActionEvent event
    ) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMacroPrograma(String macroPrograma) {
        this.macroPrograma = macroPrograma;
        System.out.println("Programa recebido: " + this.macroPrograma);
    }

    public void setStage2(Stage stage) { this.stageTela2 = stage; }
}
