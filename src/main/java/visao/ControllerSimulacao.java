package visao;

import back.comum.Microinstrucao;
import back.comum.MicroinstrucaoMap;
import back.cpu.CPU;
import back.memorias.Cache;
import back.memorias.CacheFactory;
import back.memorias.MemoriaPrincipal;
import back.montagem.Assembler;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class ControllerSimulacao {
    private Stage stage;

    @FXML
    private AnchorPane pane;
    @FXML
    private Label amux, cond, alu, deslocador, mbr, mar, read, write, enc, c, b, a, addr, microinstrucao, contadorCiclos;
    @FXML
    private Label saidaMmux, saidaMpc, saidaIncrementador, saidaLogicaMicro, saidaDecoderC, saidaDecoderB, saidaDecoderA,
            saidaLatchA, saidaLatchB, saidaAmux, saidaUla, saidaDeslocador, saidaMbr, saidaMar, pc, ac, sp, ir, tir, regA;
    @FXML
    public Button botaoCiclo, botaoSubciclo, executarTudo, pausarPrograma;
    @FXML
    private Label regs[] = new Label[11];

    private Timeline timeline;
    private boolean rodando = false;
    private GerenciadorVisual gerenciadorVisual;
    private CPU cpu;
    private Cache cache;
    private MemoriaPrincipal memP;
    private int acCiclos = 0;
    private String macroPrograma;

    public void setStage(Stage stage) { this.stage = stage; }

    public void setConteudo(CPU cpu, Cache cache, String macroPrograma) {
        this.cpu = cpu;
        this.cache = cache;
        this.memP = cache.getMemP();
        this.macroPrograma = macroPrograma;

        this.regs[0] = this.pc;
        this.regs[1] = this.ac;
        this.regs[2] = this.sp;
        this.regs[3] = this.ir;
        this.regs[4] = this.tir;
        this.regs[10] = this.regA;

        this.gerenciadorVisual = new GerenciadorVisual(this.cpu, this.pane);
    }

    @FXML
    public void executarTudo() {
        if(rodando) return;

        this.timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                event -> {
                    try {
                        executarCicloCpu();
                    } catch (Exception e) {
                        e.printStackTrace();
                        timeline.stop();
                    }
                }
        ));

       timeline.setCycleCount(Timeline.INDEFINITE);
       timeline.play();

       rodando = true;
       destivarBotoesExecucao(true);
    }

    @FXML
    private void executarSubciclo(ActionEvent e) {
        this.executarSubc();
    }

    private void executarSubc() {
        try{
            cpu.executarSubciclo();
            atualizarTela();
        } catch (Exception exe) {
            System.out.println("Erro ao executar subciclo.");;
        }
    }

    @FXML
    private void executarCiclo(ActionEvent e) {
        this.executarCicloCpu();
    }

    public void executarCicloCpu() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(20),
                event -> {
                    executarSubc();
                }
        ));

        int qtdCiclos = 4 - cpu.getUltSubcicloExe();

        if(qtdCiclos == 0) {
            qtdCiclos = 4;
        }

        timeline.setCycleCount(qtdCiclos);
        timeline.play();
    }

    private void atualizarTela() {
        gerenciadorVisual.apagarCaminhos("ativo");
        gerenciadorVisual.apagarCaminhos("barramento-ativo");

        switch(cpu.getUltSubcicloExe()) {
            case(1) -> atualizarTelaSubc1();
            case(2) -> atualizarTelaSubc2();
            case(3) -> atualizarTelaSubc3();
            case(4) -> atualizarTelaSubc4();
            default -> {
                return;
            }
        }
    }

    private void atualizarTelaSubc1() {
        gerenciadorVisual.destacarSubc1();
        atualizarComponentesSubc1();
    }

    private void atualizarTelaSubc2() {
        gerenciadorVisual.destacarSubc2();
        atualizarComponentesSubc2();
    }

    private void atualizarTelaSubc3() {
        gerenciadorVisual.destacarSubc3();
        atualizarComponentesSubc3();
    }

    private void atualizarTelaSubc4() {
        gerenciadorVisual.destacarSubc4();
        acCiclos++;
        contadorCiclos.setText(Integer.toString(acCiclos));
        atualizarComponentesSubc4();
    }

    public void atualizarComponentesSubc1() {
        this.trocarLabelsMir();

        gerenciadorVisual.destacarAmux();
        gerenciadorVisual.destacarCaminho(".cond", "sempre-ativo");
        gerenciadorVisual.destacarCaminho(".cond", "ativo");
        gerenciadorVisual.destacarCaminho(".alu", "sempre-ativo");
        gerenciadorVisual.destacarCaminho(".alu", "ativo");
        gerenciadorVisual.destacarDeslocador();
        gerenciadorVisual.destacarMbr();
        gerenciadorVisual.destacarMar();
        gerenciadorVisual.destacarRD();
        gerenciadorVisual.destacarWR();
        gerenciadorVisual.destacarEnc();
        gerenciadorVisual.destacarCaminho(".c", "sempre-ativo");
        gerenciadorVisual.destacarCaminho(".b", "sempre-ativo");
        gerenciadorVisual.destacarCaminho(".a", "sempre-ativo");
        gerenciadorVisual.destacarCaminho(".c", "ativo");
        gerenciadorVisual.destacarCaminho(".b", "ativo");
        gerenciadorVisual.destacarCaminho(".a", "ativo");
    }

    private void atualizarComponentesSubc2() {
        saidaLatchA.setText(cpu.getLatchA());
        saidaLatchB.setText(cpu.getLatchB());
        saidaIncrementador.setText(cpu.getSaidaIncrementador());
        saidaDecoderA.setText(cpu.getValorDecA());
        saidaDecoderB.setText(cpu.getValorDecB());
    }

    private void atualizarComponentesSubc3() {
        saidaMbr.setText(cpu.getValorMbr());
        saidaMar.setText(cpu.getValorMar());
        saidaAmux.setText(cpu.getValorAmux());
        saidaUla.setText(cpu.getValorUla());
        saidaDeslocador.setText(cpu.getValorDeslocador());
    }

    private void atualizarComponentesSubc4() {
        int posRegAlterado = cpu.getUltRegAlterado();
        try{
            regs[posRegAlterado].setText(cpu.getValorRegistrador(posRegAlterado));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        saidaMbr.setText(cpu.getValorMbr());
        String saida = "0";
        if(cpu.isLogicaMic()) saida = "1";
        saidaLogicaMicro.setText(saida);
        saidaIncrementador.setText(cpu.getValorIncrementador());
        saidaMmux.setText(cpu.getValorMMux());
        saidaMpc.setText(cpu.getValorMpc());
    }

    private void trocarLabelsMir() {
        Microinstrucao mir = cpu.getMir();
        amux.setText(mir.getAMUX());
        cond.setText(mir.getCOND());
        alu.setText(mir.getALU());
        deslocador.setText(mir.getSH());
        mbr.setText(mir.getMBR());
        mar.setText(mir.getMAR());
        read.setText(mir.getRD());
        write.setText(mir.getWR());
        enc.setText(mir.getENC());
        c.setText(mir.getC());
        b.setText(mir.getB());
        a.setText(mir.getA());
        addr.setText(mir.getADDR());

        String inst;
        try{
            inst = MicroinstrucaoMap.getDescricao(cpu.getValorMPC());
        } catch(Exception e) {
            inst = null;
        }
        microinstrucao.setText(inst);
    }

    public void destivarBotoesExecucao(boolean estado) {
        botaoCiclo.setDisable(estado);
        botaoSubciclo.setDisable(estado);
        executarTudo.setDisable(estado);
    }

    @FXML
    public void pausarPrograma() {
        if (timeline != null) {
            timeline.stop();
        }
        rodando = false;
        destivarBotoesExecucao(false);
    }

    @FXML
    private void reiniciarExecucao(ActionEvent e) throws IOException {
        memP = new MemoriaPrincipal();

        Assembler assembler = new Assembler();
        assembler.montar(memP, macroPrograma);

        String tipoCache = cache.getTipoCache();
        cache = CacheFactory.criarCache(tipoCache, memP);

        cpu = new CPU(cache);

        amux.setText("0");
        cond.setText("000");
        alu.setText("00");
        deslocador.setText("0");
        mbr.setText("0");
        mar.setText("0");
        read.setText("0");
        write.setText("0");
        enc.setText("0");
        c.setText("0000");
        b.setText("0000");
        a.setText("0000");
        addr.setText("00000000");
//        this.microinstrucao.setText("00000000000000000000000000000000");
        contadorCiclos.setText("0");

        saidaMmux.setText("00000000");
        saidaMpc.setText("00000000");
        saidaIncrementador.setText("00000000");
        saidaLogicaMicro.setText("0");
        saidaDecoderC.setText("0000000000000000");
        saidaDecoderB.setText("0000000000000000");
        saidaDecoderA.setText("0000000000000000");
        saidaLatchA.setText("0000000000000000");
        saidaLatchB.setText("0000000000000000");
        saidaAmux.setText("0000000000000000");
        saidaUla.setText("0000000000000000");
        saidaDeslocador.setText("0000000000000000");
        saidaMbr.setText("0000000000000000");
        saidaMar.setText("000000000000");
        pc.setText("0000000000000000");
        ac.setText("0000000000000000");
        sp.setText("000000000000000an0");
        ir.setText("0000000000000000");
        tir.setText("0000000000000000");
        regA.setText("0000000000000000");

        gerenciadorVisual.apagarCaminhos("ativo");
        gerenciadorVisual.apagarCaminhos("barramento-ativo");

        microinstrucao.setText("");
    }
}
