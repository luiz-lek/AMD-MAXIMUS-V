//package back.cpu;
//
//import javafx.fxml.FXML;
//import visao.ControllerSimulacao;
//
///* Thread para executar todo o programa MAC-1
//   Foi criada para evitar o congelamento da tela, e consequentemente, o progrma crashar,
//   nos casos de o macroprograma conter loop infinito. */
//
//public class ExecutarPrograma implements Runnable {
//    private ControllerSimulacao controllerSimulacao;
//
//    public ExecutarPrograma(ControllerSimulacao controllerSimulacao) {
//        this.controllerSimulacao = controllerSimulacao;
//    }
//
//    @Override
//    public void run() {
//        try {
//            this.executarTodoPrograma();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @FXML
//    public void executarTodoPrograma() throws Exception {
//        this.controllerSimulacao.executarTudo.setDisable(true);
//        this.controllerSimulacao.desabilitarExecucao(); // Desabilita os botões de executar enquanto o programa
//                                                        // não é finalizado ou pausado
//        while (!this.controllerSimulacao.execucaoEncerrada) {
//            if(this.controllerSimulacao.pausarPrograma) {// Verifica se o botão de pausar foi acionado
//                this.controllerSimulacao.pausarPrograma = false;
//                break;
//            }
//
//            this.controllerSimulacao.executarCiclo();
//        }
//
//        this.controllerSimulacao.pausar.setDisable(true);
//        this.controllerSimulacao.avaliarEstadoProgramaEAtivarExecucao();
//        this.controllerSimulacao.atualizarTela();
//    }
//}
