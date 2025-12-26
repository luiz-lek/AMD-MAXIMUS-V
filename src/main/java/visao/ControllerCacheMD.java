package visao;

import back.memorias.Cache;
import back.memorias.CacheMapeamentoDireto;
import back.memorias.LinhaCacheMD;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerCacheMD implements ControllerCache {
    private Stage stage;
    private CacheMapeamentoDireto cache;
    @FXML
    private TableView<LinhaCacheMD> tabela;
    @FXML
    private TableColumn<LinhaCacheMD,String> linha;
    @FXML
    private TableColumn<LinhaCacheMD, Character> validade;
    @FXML
    private TableColumn<LinhaCacheMD,Character> modificacao;
    @FXML
    private TableColumn<LinhaCacheMD,String> tag;
    @FXML
    private TableColumn<LinhaCacheMD,String> bloco0, bloco1, bloco2, bloco3;

    public void setStage(Stage stage) { this.stage  = stage; }

    public void setCache(Cache cache) throws Exception {
        this.cache = (CacheMapeamentoDireto) cache;

        int tam = cache.size();
        for(int i = 0; i < tam; i++) {
            this.tabela.getItems().add(this.cache.getLinha(i));
        }

        this.linha.setCellValueFactory( new PropertyValueFactory<>("indice") );
        this.validade.setCellValueFactory( new PropertyValueFactory<>("bitValidade") );
        this.modificacao.setCellValueFactory( new PropertyValueFactory<>("dirtyBit") );
        this.tag.setCellValueFactory( new PropertyValueFactory<>("tag") );
        this.bloco0.setCellValueFactory( new PropertyValueFactory<>("bloco0") );
        this.bloco1.setCellValueFactory( new PropertyValueFactory<>("bloco1") );
        this.bloco2.setCellValueFactory( new PropertyValueFactory<>("bloco2") );
        this.bloco3.setCellValueFactory( new PropertyValueFactory<>("bloco3") );
    }

    public void atualizarTabela() { this.tabela.refresh(); }
}
