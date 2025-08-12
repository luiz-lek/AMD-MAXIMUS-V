package Front;

import Back.Assembler;
import Back.CPU;
import Back.MemoriaPrincipal;
import Back.MicroinstrucaoMap;
import javafx.event.ActionEvent;
//import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.io.IOException;

public class Controller {
    @FXML
    private Label label, mir, mbr, mar, mpc, valorLidoMemoria;
    @FXML
    private TextArea lerPrograma, microinstrucoes, memoria, memoriaRascunho;
    @FXML
    private TextField posicaoMemoriaLida;
    @FXML
    private Button carregarNaMemoria, pularMic, lerEndereco;

    Assembler assembler = new Assembler();
    private MemoriaPrincipal memoriaPrincipal;
    private CPU cpu;

    public void lerPrograma(ActionEvent e) throws IOException {
        String[] programa;
        this.microinstrucoes.setText("");
        programa = this.lerPrograma.getText().toUpperCase().split("\\r?\\n");

        System.out.println("Programa lido");
        for(String linha : programa) System.out.println(linha);

        memoriaPrincipal = new MemoriaPrincipal();

        try{
            assembler.montar(memoriaPrincipal, programa, programa.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "\nDigite novamente o programa...");
                throw ex;
        }


        cpu = new CPU();
        cpu.setMemoriaPrincipal(memoriaPrincipal);
        this.executarCicloAux();
    }

    public void executarCiclo(ActionEvent e) {
        this.executarCicloAux();
    }

    private void executarCicloAux(){
        StringBuilder temp = new StringBuilder();
        temp.append(this.microinstrucoes.getText());
        temp.append(MicroinstrucaoMap.getDescricao(cpu.getMpc()) + "\n");
        this.microinstrucoes.setText(temp.toString());
        this.microinstrucoes.positionCaret(microinstrucoes.getText().length());
        this.microinstrucoes.setScrollTop(Double.MAX_VALUE);
        this.cpu.executarCiclo();
        this.memoria.setText(this.memoriaPrincipal.lerStack(this.cpu.getValorRegistrador(2)));
        this.mbr.setText(this.cpu.getMbrValor());
        this.mar.setText(this.cpu.getMarValorHexadecimal());
        this.mir.setText(this.cpu.getMir());
        this.mpc.setText(this.cpu.getMpcValor());
        this.memoriaRascunho.setText(this.cpu.getValoresMemoriaRascunho());
    }

    public void lerPosicaoMemoria(ActionEvent e) throws IOException {
        int posicaoMemoria = Integer.parseInt(posicaoMemoriaLida.getText());

        String posicaoMemoriaSTR = Integer.toBinaryString(posicaoMemoria);

        try{
            this.valorLidoMemoria.setText(memoriaPrincipal.ler(posicaoMemoriaSTR));
        } catch (IllegalAccessError ex) {
            System.out.println(ex.getMessage());
            this.posicaoMemoriaLida.setText("");
            this.posicaoMemoriaLida.setPromptText("Endereço deve estar entre 0 e 4095!");
        }
    }
}
