package visao;

import back.cpu.CPU;
import back.cpu.ULA;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.Set;

public class GerenciadorVisual {
    @FXML
    private AnchorPane pane;

    private CPU cpu;

    public GerenciadorVisual(CPU cpu, AnchorPane pane) {
        this.cpu = cpu;
        this.pane = pane;
    }

    public void destacarSubc1() {
        apagarCaminhos("sempre-ativo");
        destacarCaminho(".subciclo-1", "ativo");
        destacarCaminho(".barramento-regs", "barramento-ativo");
        destacarCaminho(".addr-mmux", "barramento-ativo");
        destacarCaminho(".busca-micro", "barramento-ativo");
    }

    public void destacarSubc2() {
        destacarCaminho(".subciclo-2", "ativo");
        destacarCaminho(".barramento-latch", "barramento-ativo");
        destacarCaminho(".barramento-mpc", "barramento-ativo");
    }

    public void destacarSubc3() {
        destacarCaminho(".subciclo-3", "ativo");
        if(cpu.getMbr().isReady()) {
            destacarCaminho(".mbr-ready", "ativo");
            if(cpu.isLeituraFeita()) destacarCaminho(".mbr-leitura", "barramento-ativo");
            else destacarCaminho(".mbr-escrita", "barramento-ativo");
        }
        destacarCaminho(".mbr-amux", "barramento-ativo");
        destacarCaminho(".amux-ula", "barramento-ativo");
        destacarCaminho(".ula-deslocador", "barramento-ativo");
        if(cpu.getMar().isAtivado()) destacarCaminho(".latchb-mar", "barramento-ativo");
    }

    public void destacarSubc4() {
        destacarCaminho(".subciclo-4", "ativo");

        if(cpu.isEnc()) {
            destacarCaminho(".c-regs", "ativo");
            destacarCaminho(".deslocador-regs", "barramento-ativo");
            destacarCaminho(".deslocador-regs-mbr", "barramento-ativo");
        }
        if(this.cpu.getMbr().isAtivado()) {
            destacarCaminho(".deslocador-mbr", "barramento-ativo");
            destacarCaminho(".deslocador-regs-mbr", "barramento-ativo");
        }

        ULA ula = cpu.getUla();
        if(ula.isNBit()) destacarCaminho(".nbit", "ativo");
        else if(ula.isZBit()) destacarCaminho(".zbit", "ativo");

        if(cpu.isLogicaMic()) destacarCaminho(".logica-micro", "ativo");

        destacarCaminho(".incrementador-mmux", "barramento-ativo");
        destacarCaminho(".mmux-mpc", "barramento-ativo");
    }

    public void destacarCaminho(String classe, String atividade) {
        Set<Node> fiosDestacar = pane.lookupAll(classe);

        for(Node fio : fiosDestacar) {
            fio.getStyleClass().add(atividade);
        }
    }

    public void destacarAmux() {
        String amux = cpu.getMir().getAMUX();
        if("1".equals(amux)) {
            destacarCaminho(".amux", "sempre-ativo");
            destacarCaminho(".amux", "ativo");
        }
    }

    public void destacarDeslocador() {
        String deslocador = cpu.getMir().getSH();
        if("1".equals(deslocador)) {
            destacarCaminho(".sh", "sempre-ativo");
            destacarCaminho(".sh", "ativo");
        }
    }

    public void destacarMbr() {
        String mbr = cpu.getMir().getMBR();
        if("1".equals(mbr)) {
            destacarCaminho(".mbr", "sempre-ativo");
            destacarCaminho(".mbr", "ativo");
        }
    }

    public void destacarMar() {
        String mar = this.cpu.getMir().getMAR();
        if("1".equals(mar)) {
            destacarCaminho(".mar", "sempre-ativo");
            destacarCaminho(".mar", "ativo");
        }
    }

    public void destacarRD() {
        String rd = cpu.getMir().getRD();
        if("1".equals(rd)) {
            destacarCaminho(".rd", "sempre-ativo");
            destacarCaminho(".rd", "ativo");
        }
    }

    public void destacarWR() {
        String wr = cpu.getMir().getWR();
        if("1".equals(wr)) {
            destacarCaminho(".wr", "sempre-ativo");
            destacarCaminho(".wr", "ativo");
        }
    }

    public void destacarEnc() {
        String enc = cpu.getMir().getENC();
        if("1".equals(enc)) {
            destacarCaminho(".enc", "sempre-ativo");
            destacarCaminho(".enc", "ativo");
        }
    }

    public void apagarCaminhos(String atividade) {
        Set<Node> fiosDestacados = pane.lookupAll("." + atividade);
        System.out.println("Encontrou os caminhos destacados.\n");

        for(Node fio : fiosDestacados) {
            fio.getStyleClass().remove(atividade);
        }
    }
}
