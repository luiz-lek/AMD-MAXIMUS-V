package Front;

import Back.CPU;
import Back.MemoriaPrincipal;
import Back.MicroinstrucaoMap;
import javafx.animation.TranslateTransition;
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
import javafx.scene.shape.Rectangle;
import java.io.IOException;

public class ControllerTela2 {
    Parent root;
    Stage stage, stageTelaAtual;
    Scene scene;

    @FXML
    private Button voltarTela1, proximaMic, lerMemoria, pularMacro;
    @FXML
    private TextArea macroprograma, microinstrucoes, pilha;
    @FXML
    private TextField PC, AC, SP, IR, TIR, MIR, MBR, MAR, MPC, buscarMemoria, lidoPosicaoMemoria;
    @FXML
    private Rectangle highlightMacro, highlightMic;
    @FXML
    TranslateTransition translateMacro = new TranslateTransition(),
            translateMic = new TranslateTransition();

    private CPU cpu;
    private MemoriaPrincipal memoriaPrincipal;

    private String macroPrograma;
    private String[] macroProgramaArray;
    private int linhaAtual, qtdLinhas = 0;

    public void setConteudo(String macroPrograma, String[] macroProgramaArray, CPU cpu, MemoriaPrincipal mem) {
        this.macroPrograma = macroPrograma;
        this.macroProgramaArray = macroProgramaArray;
        this.qtdLinhas = macroProgramaArray.length;
        this.macroprograma.setText(macroPrograma);
        this.cpu = cpu;
        this.memoriaPrincipal = mem;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.translateMacro.setNode(this.highlightMacro);
        this.translateMic.setNode(this.highlightMic);
        this.atualizarTela();
    }


    public void executarCiclo(ActionEvent e) {
        this.cpu.executarCiclo();
        this.atualizarTela();
    }

    private void atualizarTela() {
        StringBuilder temp = new StringBuilder();
        temp.append(this.microinstrucoes.getText());
        temp.append(MicroinstrucaoMap.getDescricao(this.cpu.getMpc()) + "\n");

        this.microinstrucoes.setText(temp.toString());
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
        this.pilha.setText(this.memoriaPrincipal.lerStack(this.cpu.getValorRegistrador(2)));
        this.pilha.positionCaret(this.pilha.getText().length());
        this.pilha.setScrollTop(Double.MAX_VALUE);
        this.MBR.setText(this.cpu.getMbrValor());
        this.MAR.setText(this.cpu.getMarValorHexadecimal());
        this.MIR.setText(this.cpu.getMir());
        this.MPC.setText(this.cpu.getMpcValor());
        this.PC.setText(this.cpu.getValorRegistrador(0));
        this.AC.setText(this.cpu.getValorRegistrador(1));
        this.SP.setText(this.cpu.getValorRegistrador(2));
        this.IR.setText(this.cpu.getValorRegistrador(3));
        this.TIR.setText(this.cpu.getValorRegistrador(4));

        if("0".equals(this.cpu.getMpcValor())) this.pularMacro();
    }

    private void pularMacro() {
        int proxPos = Integer.parseInt(this.cpu.getValorRegistrador(0), 2);

        if(proxPos >= this.qtdLinhas) {
            this.proximaMic.setDisable(true);
            if(!"HALT".equals(macroProgramaArray[qtdLinhas-1])) return;
        }


        this.translateMacro.setByY(18 * (proxPos - this.linhaAtual));
        this.translateMacro.play();

        System.out.println("Moveu highlight macro.");

        this.linhaAtual = proxPos;
    }

    @FXML
    public void lerPosicaoMemoria(ActionEvent e) throws IOException {
        int posicaoMemoria = Integer.parseInt(buscarMemoria.getText());

        String posicaoMemoriaSTR = Integer.toBinaryString(posicaoMemoria);

        try{
            this.lidoPosicaoMemoria.setText(memoriaPrincipal.ler(posicaoMemoriaSTR));
        } catch (IllegalAccessError ex) {
            System.out.println(ex.getMessage());
            this.buscarMemoria.setText("");
            this.buscarMemoria.setPromptText("Endereço deve estar entre 0 e 4095!");
        }
    }

    @FXML
    private void confirmarVoltar (ActionEvent e) throws IOException {
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

    public void setStageAtual (Stage stage) { this.stageTelaAtual = stage; }
}
