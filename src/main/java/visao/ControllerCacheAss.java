package visao;

import back.memorias.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerCacheAss implements ControllerCache {
    private Stage stage;
    private CacheAssociativa cache;
    @FXML
    private TableView<LinhaCacheASS> tabela;
    @FXML
    private TableColumn<LinhaCacheASS,String> linha;
    @FXML
    private TableColumn<LinhaCacheASS, Character> validade;
    @FXML
    private TableColumn<LinhaCacheASS, Character> modificacao;
    @FXML
    private TableColumn<LinhaCacheASS, String> numBloco;
    @FXML
    private TableColumn<LinhaCacheASS, String> bloco0, bloco1, bloco2, bloco3;

    public void setStage(Stage stage) { this.stage  = stage; }

    public void setCache(Cache cache) throws Exception {
        this.cache = (CacheAssociativa) cache;

        int tam = cache.size();
        for(int i = 0; i < tam; i++) {
            this.tabela.getItems().add(this.cache.getLinha(i));
        }

        this.linha.setCellValueFactory( new PropertyValueFactory<>("endereco") );
        this.validade.setCellValueFactory( new PropertyValueFactory<>("bitValidade") );
        this.modificacao.setCellValueFactory( new PropertyValueFactory<>("dirtyBit") );
        this.numBloco.setCellValueFactory( new PropertyValueFactory<>("numBloco") );
        this.bloco0.setCellValueFactory( new PropertyValueFactory<>("bloco0") );
        this.bloco1.setCellValueFactory( new PropertyValueFactory<>("bloco1") );
        this.bloco2.setCellValueFactory( new PropertyValueFactory<>("bloco2") );
        this.bloco3.setCellValueFactory( new PropertyValueFactory<>("bloco3") );
    }

    public void atualizarTabela() { this.tabela.refresh(); }
}