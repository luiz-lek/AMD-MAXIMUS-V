package visao;

import back.comum.ConversaoTipos;
import back.cpu.CPU;
import back.cpu.MemoriaPrincipal;
import back.comum.MicroinstrucaoMap;
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
    Stage stageAtual, stageConfirmarVoltar;
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
    private int linhaAtualMacro, qtdLinhasMacro = 0;

    public void setConteudo(String macroPrograma, String[] macroProgramaArray, CPU cpu, MemoriaPrincipal mem) throws IOException{
        this.macroPrograma = macroPrograma;
        this.macroProgramaArray = macroProgramaArray;
        this.qtdLinhasMacro = macroProgramaArray.length;
        this.macroprograma.setText(macroPrograma);
        this.cpu = cpu;
        this.memoriaPrincipal = mem;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.translateMacro.setNode(this.highlightMacro);
        this.translateMic.setNode(this.highlightMic);
        this.atualizarTela();
    }


    public void executarCiclo(ActionEvent e) throws  IOException {
        this.cpu.executarCiclo();
        this.atualizarTela();
    }

    private void atualizarTela() throws IOException {
        StringBuilder temp = new StringBuilder();
        temp.append(this.microinstrucoes.getText());
        temp.append(MicroinstrucaoMap.getDescricao(this.cpu.getMpc())).append("\n");

        this.microinstrucoes.setText(temp.toString());
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
        this.pilha.setText(this.memoriaPrincipal.lerStack(this.cpu.getValorRegistrador(2)));
        this.pilha.positionCaret(this.pilha.getText().length());
        this.pilha.setScrollTop(Double.MAX_VALUE);
        this.MBR.setText(this.cpu.getMbrValor());
        this.MAR.setText(this.cpu.getValorHexadecimalMar());
        this.MIR.setText(this.cpu.getMir());
        this.MPC.setText(this.cpu.getValorMPC());
        this.PC.setText(ConversaoTipos.binarioToInt(this.cpu.getValorRegistrador(0), 16, true));
        this.AC.setText(ConversaoTipos.binarioToInt(this.cpu.getValorRegistrador(1), 16, true));
        this.SP.setText(ConversaoTipos.binarioToInt(this.cpu.getValorRegistrador(2), 16, true));
        this.IR.setText(this.cpu.getValorRegistrador(3));
        this.TIR.setText(this.cpu.getValorRegistrador(4));

        if("0".equals(this.cpu.getValorMPC())) this.pularMacro();
    }

    private void pularMacro() {
        int proxPos = Integer.parseInt(this.cpu.getValorRegistrador(0), 2);

        if(proxPos >= this.qtdLinhasMacro) {
            this.proximaMic.setDisable(true);
            if(!"HALT".equals(macroProgramaArray[qtdLinhasMacro-1])) return;
        }


        this.translateMacro.setByY(18 * (proxPos - this.linhaAtualMacro));
        this.translateMacro.play();

        System.out.println("Moveu highlight macro.");

        this.linhaAtualMacro = proxPos;
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
        controllerTela2Voltar.setStage2( this.stageConfirmarVoltar );
        this.stageAtual = new Stage();
        this.scene = new Scene(this.root);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.scene.getStylesheets().add(css);
        this.stageAtual.setScene(this.scene);
        this.stageAtual.initModality(Modality.APPLICATION_MODAL);
        this.stageAtual.initOwner(((Node) e.getSource()).getScene().getWindow());
        this.stageAtual.initStyle(StageStyle.UNDECORATED);
        this.stageAtual.showAndWait();
    }

    public void setStageAtual (Stage stage) { this.stageConfirmarVoltar = stage; }
}
