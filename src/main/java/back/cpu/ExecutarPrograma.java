package back.cpu;

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
        this.controllerTela2.desabilitarExecucao();

        while (!this.controllerTela2.execucaoEncerrada) {
            if(this.controllerTela2.pausarPrograma) {
                this.controllerTela2.pausarPrograma = false;
                break;
            }
            this.controllerTela2.executarCiclo();
        }

        this.controllerTela2.pausar.setDisable(true);

        this.controllerTela2.atualizarTela();
        this.controllerTela2.avaliarEstadoProgramaEAtivarExecucao();
    }
}
