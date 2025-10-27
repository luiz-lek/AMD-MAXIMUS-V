package visao;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ControllerTela1Falha {
    @FXML
    private Button confirmarFalha;
    @FXML
    private Label linha1, linha2, linha3;

    @FXML
    private void confirmarFalha(ActionEvent event) {
        Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setTextoAlerta(String l1, String l2, String l3) {
        this.linha1.setText(l1);
        this.linha2.setText(l2);
        this.linha3.setText(l3);
    }
}