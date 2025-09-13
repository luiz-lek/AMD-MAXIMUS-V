package visao;

import back.comum.Conversao;
import back.comum.MAX;
import back.cpu.CPU;
import back.cpu.ExecutarPrograma;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import java.io.IOException;
import java.util.Arrays;

public class ControllerTela2 {
    Parent rootTela2Voltar, rootFalha;
    Stage stageAtual, stageTela2Voltar, stageFalha;
    Scene sceneTela2Voltar, sceneFalha;

    @FXML
    public Button voltarTela1, executarMicroinstrucao, executarMacroAtual, reiniciar, executarTudo, pausar;
    @FXML
    public TextArea macroprograma, microinstrucoes, memoriaEmBinario;
    @FXML
    public TextField PC, AC, SP, IR, TIR, MIR, MBR, MAR, MPC, A, receberEnderecoMemoria, valorLidoMemoria, valorPc, textoLinhaAtualMacro;
    @FXML
    public Rectangle highlightMacro;
    @FXML
    private Line divisoriaMemoria;
    @FXML
    private TranslateTransition translateMacro = new TranslateTransition();
    @FXML
    private PauseTransition pause = new PauseTransition(Duration.millis(400));
    @FXML
    private Label labelLinhaAtual;

    public CPU cpu;
    public MemoriaPrincipal memoriaPrincipal;

    public String macroProgramaFormatado, macroProgramaUsuario, textoMics = "mar := pc; rd;\n";
    public String[] macroProgramaArray;

    public int qtdLinhasMacro = 0, qtdLinhasTextoMics = 1, linhaAtualMacro = 0, linhaAnteriorMacro = 0;

    public boolean execucaoEncerrada = false, pausarPrograma = false;

    public Assembler assembler;

    public void setConteudo(String macroPrograma, CPU cpu, MemoriaPrincipal mem, Assembler assembler) throws Exception {
        this.macroProgramaFormatado = assembler.parser.progFormatado();
        this.macroProgramaUsuario = macroPrograma;
        this.macroProgramaArray = this.macroProgramaFormatado.split("\\r?\\n|\\r");
        System.out.println("macroProgramaFormatado: " + Arrays.toString(macroProgramaArray));
        this.qtdLinhasMacro = assembler.getTamProg();
        this.assembler = assembler;

        if(this.qtdLinhasMacro >= 26) {
            this.highlightMacro.setVisible(false);
            this.labelLinhaAtual.setVisible(true);
            this.textoLinhaAtualMacro.setVisible(true);
        }

        this.macroprograma.setText(this.macroProgramaFormatado);
        this.cpu = cpu;
        this.memoriaPrincipal = mem;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.translateMacro.setNode(this.highlightMacro);
        this.atualizarTela();
        this.pausar.setDisable(true);
    }

    public void executarMicEAtualizar(ActionEvent e) throws Exception {
        this.executarCiclo();
        this.atualizarTela();
    }

    public void executarCiclo() throws Exception {
        if(this.execucaoEncerrada) throw new Exception("Programa já finalizado.");

        this.cpu.executarCiclo();
        this.atualizarTextoMics();

        if("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.linhaAtualMacro = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);
            // Pega o valor do registrador "PC" para setar a linha atual
        }

        this.avaliarEstadoProgramaEDesativarExecucao();
    }

    public void desabilitarExecucao() {
        this.executarMicroinstrucao.setDisable(true);
        this.executarMacroAtual.setDisable(true);
        this.valorPc.setDisable(true);
    }

    public void avaliarEstadoProgramaEDesativarExecucao() {
        this.execucaoEncerrada = this.linhaAtualMacro >= this.qtdLinhasMacro;

        if(this.execucaoEncerrada) {
            this.desabilitarExecucao();
            this.executarTudo.setDisable(true);
        }
    }

    public void avaliarEstadoProgramaEAtivarExecucao() {
        if(this.execucaoEncerrada) return;

        this.executarMicroinstrucao.setDisable(false);
        this.executarMacroAtual.setDisable(false);
        this.valorPc.setDisable(false);
        this.executarTudo.setDisable(false);
    }

    //Execução macro instrução
    public void executarMacro() throws Exception {
        do {
            this.executarCiclo();
        } while(!"0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16)));
    }

    @FXML
    private void executarAtePc(ActionEvent event) throws Exception {
        if("".equals(this.valorPc.getText())) {
            this.telaFalha(event, "", "Digite um valor para o PC!", "");
            throw new Exception("Valor para pc vazio.");
        }

        int valorParada = Integer.parseInt(this.valorPc.getText());

        if(valorParada > this.qtdLinhasMacro){
            this.telaFalha(event, "", "PC MAIOR QUE O PROGRAMA.", "");
            throw new Exception("PC MAIOR QUE O PROGRAMA.");
        }


        int valorPc = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);

        if(valorPc == valorParada) {
            this.executarMacro();
        }

        while((valorParada != this.linhaAtualMacro) && (this.linhaAtualMacro < this.qtdLinhasMacro)) {
             try{
                 this.executarMacro();
             } catch(Exception e){
                 break;
             }
        }
        
        this.atualizarTela();
        this.valorPc.setText("");
    }
    @FXML
    public void pausarExecucao(ActionEvent event) throws Exception {
        if(!this.executarTudo.isDisable()) return;

        this.executarTudo.setDisable(false);
        this.pausarPrograma = true;
    }

    @FXML
    private void executarTodoPrograma(ActionEvent event) throws Exception {
        this.pausar.setDisable(false);
        Thread thread = new Thread(new ExecutarPrograma(this));
        thread.start();
    }


    public void atualizarTela() throws Exception {
        this.atualizarMemoria();
        this.atualizarMicroinstrucoes();
        this.atualizarRegs();

        if("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.pularMacro(this.linhaAnteriorMacro, this.linhaAtualMacro);
            this.textoLinhaAtualMacro.setText(this.macroProgramaArray[this.linhaAtualMacro]);
        }
    }

    private void pularMacro(int linhaAnterior, int proxLinha) {;
        if(proxLinha > this.qtdLinhasMacro) {
            proxLinha--;
        }

        this.linhaAnteriorMacro = proxLinha;

        System.out.println("Linha atual: " + linhaAnterior +
                "\nPróxima linha: " + proxLinha);

        if(this.execucaoEncerrada) {
            this.translateMacro.setByY(16 * (proxLinha - linhaAnterior));
            this.translateMacro.play();
            return;
        }

        this.bloquearExecucaoTemporariamente();

        this.translateMacro.setByY(16 * (proxLinha - linhaAnterior));
        this.translateMacro.play();

        System.out.println("\nHighlightMacro movido.\n");
    }

    @FXML
    public void executarMacroEAtualizar(ActionEvent event) throws Exception {
        this.executarMacro();
        this.atualizarTela();
    }

    private void ativarExecucao() {
        this.executarMicroinstrucao.setDisable(false);
        this.executarMacroAtual.setDisable(false);
        this.valorPc.setDisable(false);
        this.executarTudo.setDisable(false);
    }

    @FXML
    public void lerPosicaoMemoria(ActionEvent event) throws IOException {
        String busca = this.receberEnderecoMemoria.getText();

        Integer posicaoMemoria;

        try{
            posicaoMemoria = Integer.parseInt(busca);
        } catch (NumberFormatException e){
            posicaoMemoria = this.assembler.parser.getValorVariavel(busca);
            if(posicaoMemoria == null) {
                this.telaFalha(event, "", "Endereço \" " + busca + "\" não encontrada", "");
                return;
            }
        }

        String posicaoMemoriaSTR = Integer.toBinaryString(posicaoMemoria);

        try{
            this.valorLidoMemoria.setText(memoriaPrincipal.ler(posicaoMemoriaSTR));
        } catch (Exception e) {
            this.telaFalha(event, "ENDEREÇO DEVE ESTAR", "", "ENTRE 0 E 4095!");
        }
    }

    @FXML
    private void confirmarVoltar (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Tela2Voltar.fxml"));
        this.rootTela2Voltar = loader.load();
        ControllerTela2Voltar controllerTela2Voltar = loader.getController();
        controllerTela2Voltar.setMacroPrograma(this.macroProgramaUsuario);
        controllerTela2Voltar.setStage2( this.stageTela2Voltar );
        this.stageAtual = new Stage();
        this.sceneTela2Voltar = new Scene(this.rootTela2Voltar);
        String css = getClass().getResource("/css/StyleTela1Falha.css").toExternalForm();
        this.sceneTela2Voltar.getStylesheets().add(css);
        this.stageAtual.setScene(this.sceneTela2Voltar);
        this.stageAtual.initModality(Modality.APPLICATION_MODAL);
        this.stageAtual.initOwner(((Node) event.getSource()).getScene().getWindow());
        this.stageAtual.initStyle(StageStyle.UNDECORATED);
        this.stageAtual.showAndWait();
    }

    private void atualizarMemoria() throws Exception {
        this.memoriaEmBinario.setText(this.memoriaPrincipal.posicoesAcessadasBinario());
        int deslocamentoBarraDivisao = this.memoriaPrincipal.maiorEnderecoAcessado();
        this.divisoriaMemoria.setTranslateX(8 * (deslocamentoBarraDivisao - 1));
    }

    private void atualizarMicroinstrucoes() throws IOException {
        this.microinstrucoes.setText(textoMics);
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
    }

    private void atualizarTextoMics() throws Exception {
        if(this.qtdLinhasTextoMics > MAX.MAXTAMTEXTOMICS) {
            int i;

            for(i = 0; this.textoMics.charAt(i) != '\n'; i++);

            this.textoMics = this.textoMics.substring(i + 1);
            this.qtdLinhasTextoMics--;
        }

        StringBuilder temp = new StringBuilder(this.textoMics);
        temp.append(MicroinstrucaoMap.getDescricao(this.cpu.getValorMpc())).append("\n");
        this.textoMics = temp.toString();
        this.qtdLinhasTextoMics++;
    }

    private void atualizarRegs() throws Exception {
        this.MBR.setText(this.cpu.getValorMbr());
        this.MAR.setText(Conversao.binarioToHexadecimal(this.cpu.getValorMar()));
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
        this.executarMicroinstrucao.setDisable(true);
        this.executarMacroAtual.setDisable(true);
        this.reiniciar.setDisable(true);

        this.pause.setOnFinished(event -> {
            this.executarMicroinstrucao.setDisable(false);
            this.executarMacroAtual.setDisable(false);
            this.reiniciar.setDisable(false);
        });

        pause.play();
    }

    @FXML
    public void reiniciarPrograma(ActionEvent event) throws Exception {
        this.assembler = new Assembler();
        this.memoriaPrincipal = new MemoriaPrincipal();
        this.cpu = new CPU();
        this.assembler.montar(this.memoriaPrincipal, this.macroProgramaFormatado);
        this.pularMacro(this.linhaAnteriorMacro, 0);
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.textoMics = "mar := pc; rd;\n";
        this.ativarExecucao();
        this.execucaoEncerrada = false;
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