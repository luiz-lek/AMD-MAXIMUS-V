package visao;

import back.comum.Microinstrucao;
import back.cpu.CPU;
import back.cpu.Registrador;
import back.memorias.Cache;
import back.memorias.MemoriaPrincipal;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Set;

public class ControllerSimulacao {
    private Stage stage;
    @FXML
    private AnchorPane pane;

    @FXML
    private Label amux, cond, alu, deslocador, mbr, mar, read, write, enc, c, b, a, addr;
    @FXML
    private Label saidaMmux, saidaMpc, saidaIncrementador, saidaLogicaMicro, saidaDecoderC, saidaDecoderB, saidaDecoderA,
            saidaLatchA, saidaLatchB, saidaAmux, saidaUla, saidaDeslocador, saidaMbr, saidaMar, pc, sp, ir, tir, regA;

    @FXML
    private Button botaoCiclo, botaoSubciclo;

    private CPU cpu;
    private Cache cache;
    private MemoriaPrincipal memP;

    public void setStage(Stage stage) { this.stage = stage; }

    public void setConteudo(CPU cpu, Cache cache) {
        this.cpu = cpu;
        this.cache = cache;
        this.memP = cache.getMemP();
    }

    @FXML
    private void executarSubciclo(ActionEvent e) {
        try{
            this.cpu.executarSubciclo();
            this.atualizarTela();
        } catch (Exception exe) {
            System.out.println("Erro ao executar subciclo.");;
        }
    }

    @FXML
    private void executarCiclo(ActionEvent e) {
        try {
            this.cpu.executarCiclo();
        } catch (Exception exe) {
            return;
        }
    }

    private void atualizarTela() {
        this.apagarCaminhos("ativo");

        switch(this.cpu.getUltSubcicloExe()) {
            case(1) -> this.destacarSubc1();
            case(2) -> this.destacarSubc2();
            case(3) -> this.destacarSubc3();
            case(4) -> this.destacarSubc4();
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

    private void destacarSubc1() {
        this.apagarCaminhos("sempre-ativo");
        this.trocarLabelsMir();
        this.destacarCaminho(".subciclo-1", "ativo");
        this.destacarControles();
    }

    private void destacarSubc2() {
        this.destacarCaminho(".subciclo-2", "ativo");
    }

    private void destacarSubc3() {
        this.destacarCaminho(".subciclo-3", "ativo");
    }

    public void destacarSubc4() {
        this.destacarCaminho(".subciclo-4", "ativo");
    }

    public void destacarControles() {
        this.destacarAmux();
        this.destacarCaminho(".cond", "sempre-ativo");
        this.destacarCaminho(".alu", "sempre-ativo");
        this.destacarDeslocador();
        this.destacarMbr();
        this.destacarMar();
        this.destacarRD();
        this.destacarWR();
        this.destacarEnc();
        this.destacarCaminho(".c", "sempre-ativo");
        this.destacarCaminho(".b", "sempre-ativo");
        this.destacarCaminho(".a", "sempre-ativo");
    }

    private void trocarLabelsMir() {
        Microinstrucao mir = this.cpu.mir;
        this.amux.setText(mir.getAMUX());
        this.cond.setText(mir.getCOND());
        this.alu.setText(mir.getALU());
        this.deslocador.setText(mir.getSH());
        this.mbr.setText(mir.getMBR());
        this.mar.setText(mir.getMAR());
        this.read.setText(mir.getRD());
        this.write.setText(mir.getWR());
        this.enc.setText(mir.getENC());
        this.addr.setText(mir.getADDR());
    }

    private void destacarCaminho(String classe, String atividade) {
        Set<Node> fiosDestacar = pane.lookupAll(classe);

        for(Node fio : fiosDestacar) {
            fio.getStyleClass().add(atividade);
        }
    }

    public void destacarAmux() {
        String amux = this.cpu.mir.getAMUX();
        if("1".equals(amux)) {
            this.destacarCaminho(".amux", "sempre-ativo");
        }
    }

    public void destacarDeslocador() {
        String deslocador = this.cpu.mir.getSH();
        if("1".equals(deslocador)) {
            this.destacarCaminho(".sh", "sempre-ativo");
        }
    }

    public void destacarMbr() {
        String mbr = this.cpu.mir.getMBR();
        if("1".equals(mbr)) {
            this.destacarCaminho(".mbr", "sempre-ativo");
        }
    }

    public void destacarMar() {
        String mar = this.cpu.mir.getMAR();
        if("1".equals(mar)) {
            this.destacarCaminho(".mar", "sempre-ativo");
        }
    }

    public void destacarRD() {
        String rd = this.cpu.mir.getRD();
        if("1".equals(rd)) {
            this.destacarCaminho(".rd", "sempre-ativo");
        }
    }

    public void destacarWR() {
        String wr = this.cpu.mir.getWR();
        if("1".equals(wr)) {
            this.destacarCaminho(".wr", "sempre-ativo");
        }
    }

    public void destacarEnc() {
        String enc = this.cpu.mir.getENC();
        if("1".equals(enc)) {
            this.destacarCaminho(".enc", "sempre-ativo");
        }
    }
}
