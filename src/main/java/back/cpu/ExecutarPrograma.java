package back.cpu;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import visao.ControllerTela2;

public class ExecutarPrograma implements Runnable {
    private ControllerTela2 controllerTela2;

    public ExecutarPrograma(ControllerTela2 controllerTela2) {
        this.controllerTela2 = controllerTela2;
    }

    @Override
    public void run() {
        try {
            this.executarTodoPrograma();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void executarTodoPrograma() throws Exception {
        this.controllerTela2.executarTudo.setDisable(true);

        while (!controllerTela2.execucaoEncerrada) {
            controllerTela2.executarCiclo();
        }

        this.atualizarTela();
    }
}
