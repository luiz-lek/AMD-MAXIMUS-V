package Front;

import Back.CPU;
import Back.MemoriaPrincipal;
import Back.MicroinstrucaoMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class ControllerTela2 {
    Parent root, rootVoltar;
    Stage stage, stageTelaAtual;
    Scene scene, sceneVoltar;

    @FXML
    private Button voltarTela1, proximaMic, lerMemoria;
    @FXML
    private TextArea macroprograma, microinstrucoes, pilha;
    @FXML
    private TextField PC, AC, SP, IR, TIR, MIR, MBR, MAR, MPC, buscarMemoria, lidoPosicaoMemoria;

    private String macroPrograma;
    private CPU cpu;
    private MemoriaPrincipal memoriaPrincipal;

    public void setConteudo(String macroPrograma, CPU cpu, MemoriaPrincipal mem) {
        this.macroPrograma = macroPrograma;
        this.macroprograma.setText(macroPrograma);
        this.cpu = cpu;
        this.memoriaPrincipal = mem;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        executarCicloAux();
    }


    public void executarCiclo(ActionEvent e) {
        this.executarCicloAux();
    }

    private void executarCicloAux(){
        StringBuilder temp = new StringBuilder();
        temp.append(this.microinstrucoes.getText());
        temp.append(MicroinstrucaoMap.getDescricao(this.cpu.getMpc()) + "\n");
        this.microinstrucoes.setText(temp.toString());
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
        this.cpu.executarCiclo();
        this.pilha.setText(this.memoriaPrincipal.lerStack(this.cpu.getValorRegistrador(2)));
        this.MBR.setText(this.cpu.getMbrValor());
        this.MAR.setText(this.cpu.getMarValorHexadecimal());
        this.MIR.setText(this.cpu.getMir());
        this.MPC.setText(this.cpu.getMpcValor());
        this.PC.setText(this.cpu.getValorRegistrador(0));
        this.AC.setText(this.cpu.getValorRegistrador(1));
        this.SP.setText(this.cpu.getValorRegistrador(2));
        this.IR.setText(this.cpu.getValorRegistrador(3));
        this.TIR.setText(this.cpu.getValorRegistrador(4));
    }

    @FXML
    private void confirmarVoltar (ActionEvent e) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2Voltar.fxml"));
        this.root = loader.load();
        ControllerTela2Voltar controllerTela2Voltar = loader.getController();
        controllerTela2Voltar.setMacroPrograma(this.macroPrograma);
        controllerTela2Voltar.setStage2( this.stageTelaAtual );
        this.stage = new Stage();
        this.scene = new Scene(this.root);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.scene.getStylesheets().add(css);
        this.stage.setScene(this.scene);
        this.stage.initModality(Modality.APPLICATION_MODAL);
        this.stage.initOwner(((Node) e.getSource()).getScene().getWindow());
        this.stage.initStyle(StageStyle.UNDECORATED);
        this.stage.showAndWait();
    }

    @FXML
    public void voltarTela1 (ActionEvent e) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1.fxml"));
            Parent root = loader.load();
            ControllerTela1 controllerTela1 = loader.getController();
            controllerTela1.setMacroPrograma(macroPrograma);
            this.stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            this.scene = new Scene(root);
            String css = getClass().getResource("/css/StyleTela1.css").toExternalForm();
            this.scene.getStylesheets().add(css);
            this.stage.setScene(this.scene);
            this.stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setStageAtual (Stage stage) { this.stageTelaAtual = stage; }
}
