package Front;

import Back.Assembler;
import Back.CPU;
import Back.MemoriaPrincipal;
import Back.MicroinstrucaoMap;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import jdk.swing.interop.SwingInterOpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Controller {
    @FXML
    private Label label;
    @FXML
    private TextArea lerPrograma, microinstrucoes, memoria, memoriaRascunho;
    @FXML
    private TextField mbr, mar, mpc, mir;
    @FXML
    private Button executar;
    @FXML
    private Button pularMic;

    private String[] programa;
    Assembler assembler = new Assembler();
    private MemoriaPrincipal memoriaPrincipal;
    private CPU cpu;

    public void lerPrograma(ActionEvent e) throws IOException {
        this.microinstrucoes.setText("");
        this.programa = this.lerPrograma.getText().toUpperCase().split("\\r?\\n");

        System.out.println("Programa lido");
        for(String linha : programa) System.out.println(linha);

        memoriaPrincipal = new MemoriaPrincipal();

        try{
            assembler.montar(memoriaPrincipal, this.programa, this.programa.length);
        } catch (IOException ex) {
            System.out.println(ex.getMessage() + "\nDigite novamente o programa...");
                throw ex;
        }


        cpu = new CPU();
        cpu.setMemoriaPrincipal(memoriaPrincipal);
        this.executarCicloAux();
    }

    public void executarCiclo(ActionEvent e) throws IOException {
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
}
