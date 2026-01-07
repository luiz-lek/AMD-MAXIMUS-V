package visao;

import back.comum.Microinstrucao;
import back.comum.MicroinstrucaoMap;
import back.cpu.CPU;
import back.cpu.ULA;
import back.memorias.Cache;
import back.memorias.MemoriaPrincipal;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Set;

public class ControllerSimulacao {
    private Stage stage;
    @FXML
    private AnchorPane pane;

    @FXML
    private Label amux, cond, alu, deslocador, mbr, mar, read, write, enc, c, b, a, addr, microinstrucao, contadorCiclos;
    @FXML
    private Label saidaMmux, saidaMpc, saidaIncrementador, saidaLogicaMicro, saidaDecoderC, saidaDecoderB, saidaDecoderA,
            saidaLatchA, saidaLatchB, saidaAmux, saidaUla, saidaDeslocador, saidaMbr, saidaMar, pc, ac, sp, ir, tir, regA;

    private Label regs[] = new Label[11];

    private CPU cpu;
    private Cache cache;
    private MemoriaPrincipal memP;

    private int acCiclos = 0;

    public void setStage(Stage stage) { this.stage = stage; }

    public void setConteudo(CPU cpu, Cache cache) {
        this.cpu = cpu;
        this.cache = cache;
        this.memP = cache.getMemP();

        this.regs[0] = this.pc;
        this.regs[1] = this.ac;
        this.regs[2] = this.sp;
        this.regs[3] = this.ir;
        this.regs[4] = this.tir;
        this.regs[10] = this.regA;
    }

    @FXML
    private void executarSubciclo(ActionEvent e) {
        this.executarSubc();
    }

    private void executarSubc() {
        try{
            this.cpu.executarSubciclo();
            this.atualizarTela();
        } catch (Exception exe) {
            System.out.println("Erro ao executar subciclo.");;
        }
    }

    @FXML
    private void executarCiclo(ActionEvent e) {
        this.executarCicloCpu();
    }

    private void executarCicloCpu() {
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(100),
                event -> {
                    this.executarSubc();
                }
        ));

        int qtdCiclos = 4 - this.cpu.getUltSubcicloExe();

        if(qtdCiclos == 0) {
            qtdCiclos = 4;
        }

        timeline.setCycleCount(qtdCiclos);
        timeline.play();
    }

    private void atualizarTela() {
        this.apagarCaminhos("ativo");
        this.apagarCaminhos("barramento-ativo");

        switch(this.cpu.getUltSubcicloExe()) {
            case(1) -> this.atualizarTelaSubc1();
            case(2) -> this.atualizarTelaSubc2();
            case(3) -> this.atualizarTelaSubc3();
            case(4) -> this.atualizarTelaSubc4();
            default -> {
                return;
            }
        }
    }

    private void apagarCaminhos(String atividade) {
        Set<Node> fiosDestacados = this.pane.lookupAll("." + atividade);
        System.out.println("Encontrou os caminhos destacados.\n");

        for(Node fio : fiosDestacados) {
            fio.getStyleClass().remove(atividade);
        }
    }


    private void atualizarTelaSubc1() {
        this.destacarSubc1();
        this.atualizarComponentesSubc1();
    }

    private void atualizarTelaSubc2() {
        this.destacarSubc2();
        this.atualizarComponentesSubc2();
    }

    private void atualizarTelaSubc3() {
        this.destacarSubc3();
        this.atualizarComponentesSubc3();
    }

    private void atualizarTelaSubc4() {
        this.destacarSubc4();
        this.atualizarComponentesSubc4();
    }

    private void destacarSubc1() {
        this.apagarCaminhos("sempre-ativo");
        this.destacarCaminho(".subciclo-1", "ativo");
        this.destacarCaminho(".barramento-regs", "barramento-ativo");
        this.destacarCaminho(".addr-mmux", "barramento-ativo");
        this.destacarCaminho(".busca-micro", "barramento-ativo");
    }

    private void destacarSubc2() {
        this.destacarCaminho(".subciclo-2", "ativo");
        this.destacarCaminho(".barramento-latch", "barramento-ativo");
        this.destacarCaminho(".barramento-mpc", "barramento-ativo");
    }

    private void destacarSubc3() {
        this.destacarCaminho(".subciclo-3", "ativo");
        if(this.cpu.getMbr().isReady()) {
            this.destacarCaminho(".mbr-ready", "ativo");
            if(this.cpu.isLeituraFeita()) this.destacarCaminho(".mbr-leitura", "barramento-ativo");
            else this.destacarCaminho(".mbr-escrita", "barramento-ativo");
        }
        this.destacarCaminho(".mbr-amux", "barramento-ativo");
        this.destacarCaminho(".amux-ula", "barramento-ativo");
        this.destacarCaminho(".ula-deslocador", "barramento-ativo");
        if(this.cpu.getMar().isAtivado()) this.destacarCaminho(".latchb-mar", "barramento-ativo");
    }

    public void destacarSubc4() {
        this.destacarCaminho(".subciclo-4", "ativo");
        this.acCiclos++;
        this.contadorCiclos.setText(Integer.toString(acCiclos));

        if(this.cpu.isEnc()) {
            this.destacarCaminho(".c-regs", "ativo");
            this.destacarCaminho(".deslocador-regs", "barramento-ativo");
            this.destacarCaminho(".deslocador-regs-mbr", "barramento-ativo");
        }
        if(this.cpu.getMbr().isAtivado()) {
            this.destacarCaminho(".deslocador-mbr", "barramento-ativo");
            this.destacarCaminho(".deslocador-regs-mbr", "barramento-ativo");
        }

        ULA ula = this.cpu.getUla();
        if(ula.isNBit()) this.destacarCaminho(".nbit", "ativo");
        else if(ula.isZBit()) this.destacarCaminho(".zbit", "ativo");

        if(this.cpu.isLogicaMic()) this.destacarCaminho(".logica-micro", "ativo");

        this.destacarCaminho(".incrementador-mmux", "barramento-ativo");
        this.destacarCaminho(".mmux-mpc", "barramento-ativo");
    }

    public void atualizarComponentesSubc1() {
        this.trocarLabelsMir();

        this.destacarAmux();
        this.destacarCaminho(".cond", "sempre-ativo");
        this.destacarCaminho(".cond", "ativo");
        this.destacarCaminho(".alu", "sempre-ativo");
        this.destacarCaminho(".alu", "ativo");
        this.destacarDeslocador();
        this.destacarMbr();
        this.destacarMar();
        this.destacarRD();
        this.destacarWR();
        this.destacarEnc();
        this.destacarCaminho(".c", "sempre-ativo");
        this.destacarCaminho(".b", "sempre-ativo");
        this.destacarCaminho(".a", "sempre-ativo");
        this.destacarCaminho(".c", "ativo");
        this.destacarCaminho(".b", "ativo");
        this.destacarCaminho(".a", "ativo");
    }

    private void atualizarComponentesSubc2() {
        this.saidaLatchA.setText(this.cpu.getLatchA());
        this.saidaLatchB.setText(this.cpu.getLatchB());
        this.saidaIncrementador.setText(this.cpu.getSaidaIncrementador());
        this.saidaDecoderA.setText(this.cpu.getValorDecA());
        this.saidaDecoderB.setText(this.cpu.getValorDecB());
    }

    private void atualizarComponentesSubc3() {
        this.saidaMbr.setText(this.cpu.getValorMbr());
        this.saidaMar.setText(this.cpu.getValorMar());
        this.saidaAmux.setText(this.cpu.getValorAmux());
        this.saidaUla.setText(this.cpu.getValorUla());
        this.saidaDeslocador.setText(this.cpu.getValorDeslocador());
    }

    private void atualizarComponentesSubc4() {
        int posRegAlterado = this.cpu.getUltRegAlterado();
        try{
            this.regs[posRegAlterado].setText(this.cpu.getValorRegistrador(posRegAlterado));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.saidaMbr.setText(this.cpu.getValorMbr());
        String saida = "0";
        if(this.cpu.isLogicaMic()) saida = "1";
        this.saidaLogicaMicro.setText(saida);
        this.saidaIncrementador.setText(this.cpu.getValorIncrementador());
        this.saidaMmux.setText(this.cpu.getValorMMux());
        this.saidaMpc.setText(this.cpu.getValorMpc());
    }

    private void trocarLabelsMir() {
        Microinstrucao mir = this.cpu.getMir();
        this.amux.setText(mir.getAMUX());
        this.cond.setText(mir.getCOND());
        this.alu.setText(mir.getALU());
        this.deslocador.setText(mir.getSH());
        this.mbr.setText(mir.getMBR());
        this.mar.setText(mir.getMAR());
        this.read.setText(mir.getRD());
        this.write.setText(mir.getWR());
        this.enc.setText(mir.getENC());
        this.c.setText(mir.getC());
        this.b.setText(mir.getB());
        this.a.setText(mir.getA());
        this.addr.setText(mir.getADDR());

        String inst;
        try{
            inst = MicroinstrucaoMap.getDescricao(this.cpu.getValorMPC());
        } catch(Exception e) {
            inst = null;
        }
        this.microinstrucao.setText(inst);
    }

    private void destacarCaminho(String classe, String atividade) {
        Set<Node> fiosDestacar = pane.lookupAll(classe);

        for(Node fio : fiosDestacar) {
            fio.getStyleClass().add(atividade);
        }
    }

    public void destacarAmux() {
        String amux = this.cpu.getMir().getAMUX();
        if("1".equals(amux)) {
            this.destacarCaminho(".amux", "sempre-ativo");
            this.destacarCaminho(".amux", "ativo");
        }
    }

    public void destacarDeslocador() {
        String deslocador = this.cpu.getMir().getSH();
        if("1".equals(deslocador)) {
            this.destacarCaminho(".sh", "sempre-ativo");
            this.destacarCaminho(".sh", "ativo");
        }
    }

    public void destacarMbr() {
        String mbr = this.cpu.getMir().getMBR();
        if("1".equals(mbr)) {
            this.destacarCaminho(".mbr", "sempre-ativo");
            this.destacarCaminho(".mbr", "ativo");
        }
    }

    public void destacarMar() {
        String mar = this.cpu.getMir().getMAR();
        if("1".equals(mar)) {
            this.destacarCaminho(".mar", "sempre-ativo");
            this.destacarCaminho(".mar", "ativo");
        }
    }

    public void destacarRD() {
        String rd = this.cpu.getMir().getRD();
        if("1".equals(rd)) {
            this.destacarCaminho(".rd", "sempre-ativo");
            this.destacarCaminho(".rd", "ativo");
        }
    }

    public void destacarWR() {
        String wr = this.cpu.getMir().getWR();
        if("1".equals(wr)) {
            this.destacarCaminho(".wr", "sempre-ativo");
            this.destacarCaminho(".wr", "ativo");
        }
    }

    public void destacarEnc() {
        String enc = this.cpu.getMir().getENC();
        if("1".equals(enc)) {
            this.destacarCaminho(".enc", "sempre-ativo");
            this.destacarCaminho(".enc", "ativo");
        }
    }
}
