package visao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerTela2Voltar {
    Stage stage, stageTela2;
    Scene scene;

    @FXML
    private Button confirmarVoltar, cancelarVoltar;

    String macroPrograma;

    @FXML
    public void voltarTela1 (ActionEvent e) throws IOException {
        this.fecharJanela(e);
        this.stageTela2.close();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1.fxml"));
        Parent root = loader.load();
        ControllerTela1 controllerTela1 = loader.getController();
        controllerTela1.setMacroPrograma(this.macroPrograma);
        this.stage = new Stage();
        this.scene = new Scene(root);
        String css = getClass().getResource("/css/StyleTela1.css").toExternalForm();
        this.scene.getStylesheets().add(css);
        this.stage.setScene(this.scene);
        this.stage.setTitle("AMD MAXIMUS-V");
        //this.stage.setResizable(false);
        this.stage.show();
    }

    @FXML
    private void fecharJanela(ActionEvent e) throws IOException {
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMacroPrograma(String macroPrograma) {
        this.macroPrograma = macroPrograma;
        System.out.println("Programa recebido: " + this.macroPrograma);
    }

    public void setStage2(Stage stage) { this.stageTela2 = stage; }
}
