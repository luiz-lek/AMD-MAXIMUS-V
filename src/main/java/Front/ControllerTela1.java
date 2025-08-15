package Front;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerTela1 {
    Stage stage;
    Scene scene;
    Parent root;
    String css;

    @FXML
    private TextArea macroPrograma;
    @FXML
    private Button gravarNaMemoria;

    @FXML
    private void carregarPrograma(ActionEvent e) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2.fxml"));
            Parent root = loader.load();
            ControllerTela2 controllerTela2 = loader.getController();
            controllerTela2.setConteudo(macroPrograma.getText());
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            this.scene = new Scene(root);
            this.css = getClass().getResource("/css/StyleTela2.css").toExternalForm();
            this.scene.getStylesheets().add(this.css);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
