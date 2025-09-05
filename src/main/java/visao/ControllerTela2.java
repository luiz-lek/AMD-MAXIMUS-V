package visao;

import back.comum.Conversao;
import back.cpu.CPU;
import back.cpu.MemoriaPrincipal;
import back.comum.MicroinstrucaoMap;
import back.montagem.Assembler;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.IOException;

public class ControllerTela2 {
    Parent rootTela2Voltar, rootFalha;
    Stage stageAtual, stageTela2Voltar, stageFalha;
    Scene sceneTela2Voltar, sceneFalha;

    @FXML
    private Button voltarTela1, proximaMic, lerMemoria, executarPc, executarMacroAtual, reiniciar;
    @FXML
    private TextArea macroprograma, microinstrucoes, memoria;
    @FXML
    private TextField PC, AC, SP, IR, TIR, MIR, MBR, MAR, MPC, A, buscarMemoria, lidoPosicaoMemoria, valorPc;
    @FXML
    private Rectangle highlightMacro;
    @FXML
    TranslateTransition translateMacro = new TranslateTransition();
    @FXML
    PauseTransition pause = new PauseTransition(Duration.millis(400));

    private CPU cpu;
    private MemoriaPrincipal memoriaPrincipal;

    private String macroPrograma, textoMics = "mar := pc; rd;\n";
    private String[] macroProgramaArray;

    private int qtdLinhasMacro = 0, linhaAtualMacro = 0, linhaAnteriorMacro = 0;

    private boolean execucaoEncerrada = false;

    private Assembler assembler;

    public void setConteudo(String macroPrograma, String[] macroProgramaArray, CPU cpu, MemoriaPrincipal mem, Assembler assembler) throws IOException {
        this.macroPrograma = macroPrograma;
        this.macroProgramaArray = macroProgramaArray;
        this.qtdLinhasMacro = macroProgramaArray.length;
        this.assembler = assembler;
        System.out.println("qtdLinhasMacro: " + this.qtdLinhasMacro);
        this.macroprograma.setText(macroPrograma);
        this.cpu = cpu;
        this.memoriaPrincipal = mem;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.translateMacro.setNode(this.highlightMacro);
        this.atualizarTela();
    }

    public void executarMicEAtualizar(ActionEvent e) throws Exception {
        this.executarCiclo();
        this.atualizarTela();
    }

    public void executarCiclo() throws IOException, Exception {
        if(this.execucaoEncerrada) throw new Exception("Programa já finalizado.");

        this.cpu.executarCiclo();
        this.atualizarTextoMics();

        if("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.linhaAtualMacro = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);
        }

        this.desabilitarExecucao();
    }

    //Execução macro instrução
    public void executarMacro() throws Exception {
        do {
            this.executarCiclo();
        } while(!"0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16)));
    }

    @FXML
    private void executarAtePc(ActionEvent e) throws Exception {
        if("".equals(this.valorPc.getText())) {
            this.telaFalha(e, "", "   Digite um valor para o PC!", "");
            throw new Exception("Valor para pc vazio.");
        }

        int valorParada = Integer.parseInt(this.valorPc.getText());

        if(valorParada >= this.qtdLinhasMacro){
            this.telaFalha(e, "", "  PC MAIOR QUE O PROGRAMA.", "");
            throw new Exception("PC MAIOR QUE O PROGRAMA.");
        }


        int valorPc = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);

        if(valorPc == valorParada) {
            this.executarMacro();
        }

        while((valorParada != this.linhaAtualMacro) && (this.linhaAtualMacro < this.qtdLinhasMacro)) {
             try{
                 this.executarMacro();
             } catch(Exception ex){
                 break;
             }
        }
        
        this.atualizarTela();
    }

    private void atualizarTela() throws IOException {
        this.atualizarMemoria();
        this.atualizarMicroinstrucoes();
        this.atualizarRegs();

        if("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.pularMacro(this.linhaAnteriorMacro, this.linhaAtualMacro);
        }
    }

    private void pularMacro(int linhaAnterior, int proxLinha) throws IOException {
        System.out.println("ProxPos: " + proxLinha);

        if(proxLinha >= this.qtdLinhasMacro) {
            proxLinha--;
        }

        System.out.println("Linha atual: " + linhaAnterior +
                "\nPróxima linha: " + proxLinha);

        if(this.execucaoEncerrada) {
            this.translateMacro.setByY(18 * (proxLinha - linhaAnterior));
            this.translateMacro.play();
            return;
        }

        this.bloquearExecucaoTemporariamente();

        this.translateMacro.setByY(18 * (proxLinha - linhaAnterior));
        this.translateMacro.play();

        this.linhaAnteriorMacro = proxLinha;

        System.out.println("\nHighlightMacro movido.\n");
    }

    @FXML
    public void executarMacroEAtualizar(ActionEvent e) throws Exception {
        this.executarMacro();
        this.atualizarTela();
    }

    public void desabilitarExecucao() {
        System.out.println("Linha atual macro em desabilitarExecução: " + this.linhaAtualMacro);
        this.execucaoEncerrada = this.linhaAtualMacro >= this.qtdLinhasMacro;
        if(!this.execucaoEncerrada) {
            this.execucaoEncerrada = "0000000000000000".equals(this.macroProgramaArray[this.linhaAtualMacro]);
        }

        if(this.execucaoEncerrada) {
            this.proximaMic.setDisable(true);
            this.executarMacroAtual.setDisable(true);
            this.executarPc.setDisable(true);
            this.valorPc.setDisable(true);
        }
    }

    private void ativarExecucao() {
        this.proximaMic.setDisable(false);
        this.executarMacroAtual.setDisable(false);
        this.executarPc.setDisable(false);
        this.valorPc.setDisable(false);
    }

    @FXML
    public void lerPosicaoMemoria(ActionEvent e) throws IOException {
        Integer posicaoMemoria;
        String busca = this.buscarMemoria.getText();

        try{
            posicaoMemoria = Integer.parseInt(busca);
        } catch (NumberFormatException ex){
            posicaoMemoria = this.assembler.parser.getValorVariavel(busca);
            if(posicaoMemoria == null) {
                this.telaFalha(e, "", "Posição " + busca + "não encontrada", "");
                return;
            }
        }

        String posicaoMemoriaSTR = Integer.toBinaryString(posicaoMemoria);

        try{
            this.lidoPosicaoMemoria.setText(memoriaPrincipal.ler(posicaoMemoriaSTR));
        } catch (IllegalAccessError ex) {
            System.out.println(ex.getMessage());
            this.telaFalha(e, "   Endereço deve estar entre", "", "                  0 e 4095!");
        }
    }

    @FXML
    private void confirmarVoltar (ActionEvent e) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2Voltar.fxml"));
        this.rootTela2Voltar = loader.load();
        ControllerTela2Voltar controllerTela2Voltar = loader.getController();
        controllerTela2Voltar.setMacroPrograma(this.macroPrograma);
        controllerTela2Voltar.setStage2( this.stageTela2Voltar );
        this.stageAtual = new Stage();
        this.sceneTela2Voltar = new Scene(this.rootTela2Voltar);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.sceneTela2Voltar.getStylesheets().add(css);
        this.stageAtual.setScene(this.sceneTela2Voltar);
        this.stageAtual.initModality(Modality.APPLICATION_MODAL);
        this.stageAtual.initOwner(((Node) e.getSource()).getScene().getWindow());
        this.stageAtual.initStyle(StageStyle.UNDECORATED);
        this.stageAtual.showAndWait();
    }

    private void atualizarMemoria() throws IOException {
        this.memoria.setText(this.memoriaPrincipal.posicoesAcessadas());
        this.memoria.positionCaret(this.memoria.getText().length());
        this.memoria.setScrollTop(Double.MAX_VALUE);
    }

    private void atualizarMicroinstrucoes() throws IOException {
        this.microinstrucoes.setText(textoMics);
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
    }

    private void atualizarTextoMics() throws IOException {
        StringBuilder temp = new StringBuilder();
        temp.append(this.textoMics);
        temp.append(MicroinstrucaoMap.getDescricao(this.cpu.getValorMpc())).append("\n");
        this.textoMics = temp.toString();
    }

    private void atualizarRegs() throws IOException {
        this.MBR.setText(this.cpu.getValorMbr());
        this.MAR.setText(Conversao.binarioToHexadecimal(this.cpu.getMar()));
        this.MIR.setText(this.cpu.getValorMir());
        this.MPC.setText(this.cpu.getValorMPC());
        this.PC.setText(Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16, true));
        this.AC.setText(Conversao.binarioToInt(this.cpu.getValorRegistrador(1), 16, true));
        this.SP.setText(Conversao.binarioToInt(this.cpu.getValorRegistrador(2), 16, true));
        this.IR.setText(this.cpu.getValorRegistrador(3));
        this.TIR.setText(this.cpu.getValorRegistrador(4));
        this.A.setText(this.cpu.getValorRegistrador(10));
    }

    private void bloquearExecucaoTemporariamente() {
        this.proximaMic.setDisable(true);
        this.executarMacroAtual.setDisable(true);
        this.executarPc.setDisable(true);

        this.pause.setOnFinished(event -> {
            this.proximaMic.setDisable(false);
            this.executarMacroAtual.setDisable(false);
            this.executarPc.setDisable(false);
        });

        pause.play();
    }

    @FXML
    public void reiniciarPrograma(ActionEvent e) throws IOException {
        this.assembler = new Assembler();
        this.memoriaPrincipal = new MemoriaPrincipal();
        this.cpu = new CPU();
        this.assembler.montar(this.memoriaPrincipal, this.macroPrograma);
        this.pularMacro(this.linhaAnteriorMacro, 0);
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.textoMics = "mar := pc; rd;\n";
        this.ativarExecucao();
        this.execucaoEncerrada = false;
        this.linhaAnteriorMacro = 0;
        this.linhaAtualMacro = 0;
        this.atualizarTela();
    }

    public void setStageAtual (Stage stage) { this.stageTela2Voltar = stage; }

    public void telaFalha(ActionEvent e, String l1, String l2, String l3) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela1Falha.fxml"));
        this.rootFalha = loader.load();
        ControllerTela1Falha controllerTela1Falha = loader.getController();
        controllerTela1Falha.setTextoAlerta(l1, l2, l3);
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