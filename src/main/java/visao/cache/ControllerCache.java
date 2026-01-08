package visao.cache;

import back.memorias.Cache;
import javafx.stage.Stage;

public interface ControllerCache {
    public void setStage(Stage stage);
    public void setCache(Cache cache) throws Exception;
    public void atualizarTabela();
}
