package visao;

import back.comum.Conversao;
import back.comum.Constantes;
import back.cpu.CPU;
import back.cpu.ExecutarPrograma;
import back.memorias.Cache;
import back.memorias.CacheFactory;
import back.memorias.MemoriaPrincipal;
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

import static back.comum.Constantes.*;
import java.io.IOException;

public class ControllerTela2 {
    Parent rootTela2Voltar, rootFalha, rootCache;
    Stage stageAtual, stageTela2Voltar, stageFalha, stageCache;
    Scene sceneTela2Voltar, sceneFalha, sceneCache;
    private String fxmlTelaCache;

    @FXML
    public Button voltarTela1, executarMicroinstrucao, executarMacroAtual, reiniciar, executarTudo, pausar, mostrarCache;
    @FXML
    public TextArea macroprograma, microinstrucoes, memoriaEmBinario;
    @FXML
    public TextField PC, AC, SP, IR, TIR, MIR, MBR, MAR, MPC, A, receberEnderecoMemoria, valorLidoMemoria, valorPc, textoLinhaAtualMacro, tempo, ciclo;
    @FXML
    public Rectangle highlightMacro;
    @FXML
    private Line divisoriaMemoria, divisoriaMemoriaEndereco;
    @FXML
    private TranslateTransition translateMacro = new TranslateTransition();
    @FXML
    private PauseTransition pause = new PauseTransition(Duration.millis(400));
    @FXML
    private Label labelLinhaAtual;
    private ControllerCache controllerCache;

    public CPU cpu;
    public MemoriaPrincipal memoriaPrincipal;
    public Cache cache;

    public String macroProgramaFormatado, macroProgramaUsuario, textoMics = "mar := pc; rd;\n", pathTelaCache;
    public String[] macroProgramaArray;

    public int qtdLinhasMacro = 0, qtdLinhasTextoMics = 1, linhaAtualMacro = 0, linhaAnteriorMacro = 0, cicloAt = 0;

    public boolean execucaoEncerrada = false, pausarPrograma = false;

    public long acTempo = 0;

    public Assembler assembler;

    public void setConteudo(String macroPrograma, ComponentesTela2 componentes) throws Exception {
        this.assembler = componentes.assembler;
        this.setMacroPrograma(macroPrograma);

        this.tempo.setText("0.0");
        this.ciclo.setText("0");

        this.qtdLinhasMacro = this.assembler.getTamProg();

        if (this.qtdLinhasMacro >= 26) {
            this.highlightMacro.setVisible(false);
            this.labelLinhaAtual.setVisible(true);
            this.textoLinhaAtualMacro.setVisible(true);
        }

        this.macroprograma.setText(this.macroProgramaFormatado);
        this.cpu = componentes.cpu;
        this.memoriaPrincipal = componentes.memoriaPrincipal;
        this.cache = componentes.cache;
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.cpu.setCache(this.cache);
        this.translateMacro.setNode(this.highlightMacro);
        this.atualizarTela();
        this.pausar.setDisable(true);
        String tipoCache = cache.getTipoCache();
        if(tipoCache.equals(Constantes.CACHE_TIPO_SEM_CACHE)) {
            this.mostrarCache.setDisable(true);
            this.mostrarCache.setText("Sem cache");
        }
        this.resolvePathTelaCache(tipoCache);
    }

    public void resolvePathTelaCache(String tipoCache) {
        switch(tipoCache) {
            case CACHE_TIPO_ASS -> this.pathTelaCache = PATH_TELA_CACHE_ASS;
            case CACHE_TIPO_MD ->  this.pathTelaCache = PATH_TELA_CACHE_MD;
            case CACHE_TIPO_AC ->  this.pathTelaCache = PATH_TELA_CACHE_AC;
            default -> this.pathTelaCache = null;
        }
    }

    public void setMacroPrograma(String macroPrograma) {
        this.macroProgramaFormatado = this.assembler.parser.progFormatado();
        this.macroProgramaUsuario = macroPrograma;
        this.macroProgramaArray = this.macroProgramaFormatado.split("\\r?\\n|\\r");
    }

    public void executarMicEAtualizar(ActionEvent e) throws Exception {
        this.executarCiclo();
        this.atualizarTela();
    }

    public void executarCiclo() throws Exception {
        if (this.execucaoEncerrada) throw new Exception("Programa já finalizado.");

        this.cicloAt++;
        long ini = System.nanoTime();
        this.cpu.executarCiclo();
        long fim = System.nanoTime();
        acTempo += (long)((fim - ini) / 1000000.0);
//        System.out.println((fim - ini) / 1000000.0);
        this.atualizarTextoMics();

        if ("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.linhaAtualMacro = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);
            //Pega o valor do registrador "PC" para setar a linha atual.
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

        if (this.execucaoEncerrada) {
            this.desabilitarExecucao();
            this.executarTudo.setDisable(true);
        }
    }

    public void avaliarEstadoProgramaEAtivarExecucao() {
        if (this.execucaoEncerrada) return;

        this.executarMicroinstrucao.setDisable(false);
        this.executarMacroAtual.setDisable(false);
        this.valorPc.setDisable(false);
        this.executarTudo.setDisable(false);
    }

    // Execução macro instrução
    public void executarMacro() throws Exception {
        do {
            this.executarCiclo();
        } while (!"0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16)));
    }

    @FXML
    private void executarAtePc(ActionEvent event) throws Exception {
        if ("".equals(this.valorPc.getText())) {
            this.telaFalha(event, "", "Digite um valor para o PC!", "");
            throw new Exception("Valor para pc vazio.");
        }

        int valorParada = Integer.parseInt(this.valorPc.getText());

        if (valorParada >= this.qtdLinhasMacro) {
            this.telaFalha(event, "", "PC MAIOR QUE O PROGRAMA.", "");
            throw new Exception("PC MAIOR QUE O PROGRAMA.");
        }


        int valorPc = Conversao.binarioToInt(this.cpu.getValorRegistrador(0), 16);

        if (valorPc == valorParada) {
            this.executarMacro();
        }

        while ((valorParada != this.linhaAtualMacro) && (this.linhaAtualMacro < this.qtdLinhasMacro)) {
            try {
                this.executarMacro();
            } catch (Exception e) {
                break;
            }
        }

        this.atualizarTela();
        this.valorPc.setText("");
    }

    @FXML
    public void pausarExecucao(ActionEvent event) throws Exception {
        if (!this.executarTudo.isDisable()) return;

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
        tempo.setText(String.valueOf(acTempo));
        ciclo.setText(String.valueOf(cicloAt));

        if ("0".equals(Conversao.binarioToStrDecimal(this.cpu.getValorMPC(), 16))) {
            this.pularMacro(this.linhaAnteriorMacro, this.linhaAtualMacro);
            int linhaTexto = linhaAtualMacro;

            if (this.linhaAtualMacro >= this.qtdLinhasMacro) {
                linhaTexto = this.qtdLinhasMacro - 1;
            }
            this.textoLinhaAtualMacro.setText(this.macroProgramaArray[linhaTexto]);
        }

        if((this.stageAtual != null) && (this.stageAtual.isShowing())) {
            controllerCache.atualizarTabela();
        }
    }

    private void pularMacro(int linhaAnterior, int proxLinha) {
        ;
        if (proxLinha > this.qtdLinhasMacro) {
            proxLinha--;
        }

        this.linhaAnteriorMacro = proxLinha;

//        System.out.println("Linha atual: " + linhaAnterior +
//                "\nPróxima linha: " + proxLinha);

        if (this.execucaoEncerrada) {
            this.translateMacro.setByY(16 * (proxLinha - linhaAnterior));
            this.translateMacro.play();
            return;
        }

        this.bloquearExecucaoTemporariamente();

        this.translateMacro.setByY(16 * (proxLinha - linhaAnterior));
        this.translateMacro.play();

        //System.out.println("\nHighlightMacro movido.\n");
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

        try {
            posicaoMemoria = Integer.parseInt(busca);
        } catch (NumberFormatException e) {
            posicaoMemoria = this.assembler.parser.getValorVariavel(busca);
            if (posicaoMemoria == null) {
                this.telaFalha(event, "", "Endereço \" " + busca + "\" não encontrada", "");
                return;
            }
        }

        String posicaoMemoriaSTR = Integer.toBinaryString(posicaoMemoria);

        try {
            this.valorLidoMemoria.setText(memoriaPrincipal.ler(posicaoMemoriaSTR));
        } catch (Exception e) {
            this.telaFalha(event, "ENDEREÇO DEVE ESTAR", "", "ENTRE 0 E 4095!");
        }
    }

    @FXML
    private void confirmarVoltar(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA2_VOLTAR));
        this.rootTela2Voltar = loader.load();
        ControllerTela2Voltar controllerTela2Voltar = loader.getController();
        controllerTela2Voltar.setMacroPrograma(this.macroProgramaUsuario);
        controllerTela2Voltar.setStage2(this.stageTela2Voltar);
        this.stageAtual = new Stage();
        this.sceneTela2Voltar = new Scene(this.rootTela2Voltar);
        String css = getClass().getResource(PATH_CSS_TELA1FALHA).toExternalForm();
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
        this.divisoriaMemoriaEndereco.setTranslateX(8 * (deslocamentoBarraDivisao - 1));
    }

    private void atualizarMicroinstrucoes() throws IOException {
        this.microinstrucoes.setText(textoMics);
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
    }

    private void atualizarTextoMics() throws Exception {
        if (this.qtdLinhasTextoMics > Constantes.TEXTOMICS_NUM_LINHAS) {
            int i;

            for (i = 0; this.textoMics.charAt(i) != '\n'; i++) ;

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
        this.cache = CacheFactory.criarCache(this.cache.getTipoCache(), this.memoriaPrincipal);
        this.assembler.montar(this.memoriaPrincipal, this.macroProgramaFormatado);
        this.pularMacro(this.linhaAnteriorMacro, 0);
        this.cpu.setMemoriaPrincipal(this.memoriaPrincipal);
        this.cpu.setCache(this.cache);
        this.textoMics = "mar := pc; rd;\n";
        this.ativarExecucao();
        this.execucaoEncerrada = false;
        this.linhaAtualMacro = 0;
        this.qtdLinhasTextoMics = 1;
        this.cicloAt = 0;
        this.acTempo = 0;
        this.atualizarTela();
    }

    public void setStageAtual(Stage stage) {
        this.stageTela2Voltar = stage;
    }

    @FXML
    private void exibirCache(ActionEvent event) throws IOException, Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(this.pathTelaCache));
        this.rootCache = loader.load();
        this.controllerCache = loader.getController();

        this.controllerCache.setStage(this.stageCache);
        this.controllerCache.setCache(cache);

        this.stageAtual = new Stage();
        this.sceneCache = new Scene(this.rootCache);

        String css = getClass().getResource(PATH_CSS_TELA2).toExternalForm();
        this.sceneCache.getStylesheets().add(css);

        this.stageAtual.setScene(this.sceneCache);

        this.stageAtual.initModality(Modality.NONE);

        this.stageAtual.initStyle(StageStyle.DECORATED);

        this.stageAtual.setTitle("CACHE " + cache.getTipoCache());

        this.stageAtual.show();
    }


    public void telaFalha(ActionEvent e, String l1, String l2, String l3) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(PATH_TELA1_FALHA));
        this.rootFalha = loader.load();
        ControllerTela1Falha controllerTela1Falha = loader.getController();
        controllerTela1Falha.setTextoAlerta(l1, l2, l3);
        this.stageFalha = new Stage();
        this.sceneFalha = new Scene(this.rootFalha);
        String css = getClass().getResource(PATH_CSS_TELA1FALHA).toExternalForm();
        this.sceneFalha.getStylesheets().add(css);
        this.stageFalha.setScene(this.sceneFalha);
        this.stageFalha.initModality(Modality.APPLICATION_MODAL);
        this.stageFalha.initOwner(((Node) e.getSource()).getScene().getWindow());
        this.stageFalha.initStyle(StageStyle.UNDECORATED);
        this.stageFalha.showAndWait();
    }
}