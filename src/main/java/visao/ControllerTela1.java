package visao;

import back.montagem.Assembler;
import back.cpu.CPU;
import back.cpu.MemoriaPrincipal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class ControllerTela1 {
    Stage stage, stageFalha;
    Scene scene, sceneFalha;
    Parent root, rootFalha;

    @FXML
    private TextArea macroPrograma;
    @FXML
    private Button gravarNaMemoria, confirmarFalha;
    private CPU cpu;
    private MemoriaPrincipal memoriaPrincipal;
    private Assembler assembler = new Assembler();

    @FXML
    private void carregarPrograma(ActionEvent e) throws IOException {
        try {
            String[] programaArray = this.escreverProgramaMemoria();

            this.cpu = new CPU();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2.fxml"));
            this.root = loader.load();
            ControllerTela2 controllerTela2 = loader.getController();
            controllerTela2.setConteudo(String.join("\n", programaArray), programaArray, this.cpu, this.memoriaPrincipal);
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            controllerTela2.setStageAtual(this.stage);
            this.scene = new Scene(root);
            String css = getClass().getResource("/css/StyleTela2.css").toExternalForm();
            this.scene.getStylesheets().add(css);
            this.stage.setScene(this.scene);
            this.stage.centerOnScreen();
            this.stage.show();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            this.telaFalha(e);
        }
    }

    public void setMacroPrograma(String macroPrograma) {
        this.macroPrograma.setText(macroPrograma);
        this.macroPrograma.positionCaret(macroPrograma.length());
    }

    public String[] escreverProgramaMemoria() throws IOException {
        this.memoriaPrincipal = new MemoriaPrincipal();

        String[] progFormatado = this.assembler.montar(this.memoriaPrincipal, macroPrograma.getText());

        return progFormatado;
    }

    public void telaFalha(ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1Falha.fxml"));
        this.rootFalha = loader.load();
        ControllerTela1Falha controllerTela1Falha = loader.getController();
        controllerTela1Falha.setTextoAlerta("Falha ao carregar programa", "", "         Digite novamente.");
        this.stageFalha = new Stage();
        this.sceneFalha = new Scene(this.rootFalha);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.sceneFalha.getStylesheets().add(css);
        this.stageFalha.setScene(this.sceneFalha);
        this.stageFalha.initModality(Modality.APPLICATION_MODAL);
        this.stageFalha.initOwner(((Node) e.getSource()).getScene().getWindow());
        this.stageFalha.initStyle(StageStyle.UNDECORATED);
        this.stageFalha.showAndWait();
    }
}