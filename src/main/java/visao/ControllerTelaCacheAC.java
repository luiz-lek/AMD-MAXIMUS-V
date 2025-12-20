package visao;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class ControllerTelaCacheAC {
    @FXML
    private Stage stage;

    @FXML
    private TextArea tabela;

    public void setTextoCache(String textoCache) {
        this.tabela.setText(textoCache);
    }

    public void setStage(Stage stage) { this.stage = stage; }
}