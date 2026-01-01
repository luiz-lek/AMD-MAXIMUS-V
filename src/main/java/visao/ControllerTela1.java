package visao;

import static back.comum.Constantes.*;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ControllerTela1 implements Initializable {
    Stage stageSimulacao, stageFalha;
    Scene sceneSimulacao, sceneFalha;
    Parent rootSimulacao, rootFalha;

    @FXML
    private TextArea macroPrograma;
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
        String tipoCache;
        try {
            this.assembler = new Assembler();
            this.escreverProgramaMemoria();
            String op = escolhaCache.getValue();
            Cache cache;

            switch (op) {
                case "Associativa" -> tipoCache = CACHE_TIPO_ASS;
                case "Mapeamento direto" -> tipoCache = CACHE_TIPO_MD;
                case "Associtiva por conjunto" -> tipoCache = CACHE_TIPO_AC;
                default -> tipoCache = CACHE_TIPO_SEM_CACHE;
            }

            cache = CacheFactory.criarCache(tipoCache, memoriaPrincipal);

            this.cpu = new CPU();

            FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA_SIMULACAO));
            this.rootSimulacao = loader.load();
            ControllerSimulacao controllerSimulacao = loader.getController();

            this.stageSimulacao = (Stage) ((Node) event.getSource()).getScene().getWindow();
            controllerSimulacao.setStage(this.stageSimulacao);

            this.sceneSimulacao = new Scene(this.rootSimulacao);
            String css = getClass().getResource(PATH_CSS_SIMULACAO).toExternalForm();
            this.sceneSimulacao.getStylesheets().add(css);
            this.sceneSimulacao.setFill(Color.web("#111111"));
            this.stageSimulacao.setScene(this.sceneSimulacao);

            this.stageSimulacao.centerOnScreen();
            this.stageSimulacao.setResizable(true);

//            controllerSimulacao.setConteudo(this.macroPrograma.getText(), comp);
            this.stageSimulacao.show();
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA1_FALHA));
        this.rootFalha = loader.load();
        ControllerTela1Falha controllerTela1Falha = loader.getController();
        controllerTela1Falha.setTextoAlerta(mensagem, "", "Digite novamente.");
        this.stageFalha = new Stage();
        this.sceneFalha = new Scene(this.rootFalha);
        String css = getClass().getResource(PATH_CSS_TELA1).toExternalForm();
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