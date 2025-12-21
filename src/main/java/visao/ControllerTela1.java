package visao;

import back.memorias.*;
import back.montagem.Assembler;
import back.cpu.CPU;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerTela1 implements Initializable {
    Stage stageTela2, stageFalha;
    Scene scenetela2, sceneFalha;
    Parent rootTela2, rootFalha;

    @FXML
    private TextArea macroPrograma;
    @FXML
    private Button gravarNaMemoria, confirmarFalha;
    @FXML
    private ChoiceBox<String> escolhaCache;

    private CPU cpu;
    private MemoriaPrincipal memoriaPrincipal;
    private Assembler assembler;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        escolhaCache.getItems().addAll("Sem cache", "Associativa", "Mapeamento direto", "Associtiva por conjunto");
        escolhaCache.setValue("Sem cache");
    }

    @FXML
    private void carregarPrograma(ActionEvent event) throws Exception {
        try {
            this.assembler = new Assembler();
            this.escreverProgramaMemoria();
            String op = escolhaCache.getValue();
            Cache cache;

            switch (op) {
                case "Associtaiva" -> cache = new CacheAssociativaConjunto(memoriaPrincipal);
//                case "Mapeamento direto" -> cache = new CacheMapeamentoDireto(memoriaPrincipal);
                case "Associtiva por conjunto" -> cache = new CacheAssociativaConjunto(memoriaPrincipal);
                default ->cache = new SemCache(memoriaPrincipal);
            }

            this.cpu = new CPU();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2.fxml"));
            this.rootTela2 = loader.load();
            ControllerTela2 controllerTela2 = loader.getController();
            this.stageTela2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controllerTela2.setStageAtual(this.stageTela2);
            controllerTela2.setConteudo(this.macroPrograma.getText(), this.cpu, this.memoriaPrincipal, this.assembler, cache);
            this.scenetela2 = new Scene(this.rootTela2);
            String css = getClass().getResource("/css/StyleTela2.css").toExternalForm();
            this.scenetela2.getStylesheets().add(css);
            this.stageTela2.setScene(this.scenetela2);
            this.stageTela2.centerOnScreen();
            this.stageTela2.setResizable(true);
            this.stageTela2.show();
        } catch (Exception e) {
            this.telaFalha(event, e.getMessage());
        }
    }

    public void setMacroPrograma(String macroPrograma) {
        this.macroPrograma.setText(macroPrograma);
        this.macroPrograma.positionCaret(macroPrograma.length());
    }

    public void escreverProgramaMemoria() throws Exception {
        this.memoriaPrincipal = new MemoriaPrincipal();
        this.assembler.montar(this.memoriaPrincipal, macroPrograma.getText());
    }

    public void telaFalha(ActionEvent event, String mensagem) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1Falha.fxml"));
        this.rootFalha = loader.load();
        ControllerTela1Falha controllerTela1Falha = loader.getController();
        controllerTela1Falha.setTextoAlerta(mensagem, "", "Digite novamente.");
        this.stageFalha = new Stage();
        this.sceneFalha = new Scene(this.rootFalha);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.sceneFalha.getStylesheets().add(css);
        this.stageFalha.setScene(this.sceneFalha);
        this.stageFalha.initModality(Modality.APPLICATION_MODAL);
        this.stageFalha.initOwner(((Node) event.getSource()).getScene().getWindow());
        this.stageFalha.initStyle(StageStyle.UNDECORATED);
        this.stageFalha.showAndWait();
        this.cpu = new CPU();
        this.memoriaPrincipal = new MemoriaPrincipal();
        this.assembler = new Assembler();
    }
}