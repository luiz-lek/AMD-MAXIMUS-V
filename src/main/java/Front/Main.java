package Front;

import Back.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Scanner;

public class Main{
    public static void lerEntrada(){
        Scanner entrada = new Scanner(System.in);

        String[] programa = new String[MAX.TAMPROG];//

        int i = lerPrograma(entrada, programa);

        Assembler assembler = new Assembler();
        MemoriaPrincipal memoriaPrincipal = new MemoriaPrincipal();

        while(true){
            try{
                assembler.montar(memoriaPrincipal, programa, i);
                break;
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage() + "\nDigite novamente o programa...");
                i = lerPrograma(entrada, programa);
            }
        }

        CPU cpu = new CPU();
        cpu.iniciar(memoriaPrincipal);

        //imprimirProgramaMemoria(programa, memoriaPrincipal, i);

        entrada.close();
    }

    public static int lerPrograma(Scanner entrada, String[] programa){
        String opcao, upperCase, trim;
        int i = 0;

        while(true){
            opcao = entrada.nextLine();
            upperCase = opcao.toUpperCase();
            trim = upperCase.trim();

            if(trim.isEmpty()) break;

            programa[i] = trim;
            i++;

            if(trim.equals("HALT")) break;
        }

        return i;
    }

    public static void imprimirProgramaMemoria(String[] prog, MemoriaPrincipal mem, int tamProg){
        for(int i =  0; i < tamProg; i++){
            System.out.println(prog[i] + " | " + mem.ler(Integer.toBinaryString(i)));
        }
    }

    public static void main(String[] args){
        lerEntrada();
    }
}

/*public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 500, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}*/