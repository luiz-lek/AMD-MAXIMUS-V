package visao;

import back.comum.CONSTS;
import back.memorias.Cache;
import back.memorias.CacheAssociativaConjunto;
import back.memorias.LinhaCacheMD;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ControllerCacheAC {
    private Stage stage;
    private CacheAssociativaConjunto cache;
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
    @FXML
    private ChoiceBox<String> escolhaBloco;
    @FXML
    private Button trocarBloco;
    @FXML
    private Label numBloco;

    private int tam;

    public void setStage(Stage stage) { this.stage  = stage; }

    public void setCache(Cache cache) throws Exception {
        this.cache = (CacheAssociativaConjunto) cache;

        this.tam = cache.size();
        for(int i = 0; i < tam; i++) {
            LinhaCacheMD[] linha = this.cache.getLinha(i);
            this.tabela.getItems().add(linha[0]);
        }

        this.linha.setCellValueFactory( new PropertyValueFactory<>("endereco") );
        this.validade.setCellValueFactory( new PropertyValueFactory<>("bitValidade") );
        this.modificacao.setCellValueFactory( new PropertyValueFactory<>("dirtyBit") );
        this.tag.setCellValueFactory( new PropertyValueFactory<>("tag") );
        this.bloco0.setCellValueFactory( new PropertyValueFactory<>("bloco0") );
        this.bloco1.setCellValueFactory( new PropertyValueFactory<>("bloco1") );
        this.bloco2.setCellValueFactory( new PropertyValueFactory<>("bloco2") );
        this.bloco3.setCellValueFactory( new PropertyValueFactory<>("bloco3") );

        for(int i = 0; i < CONSTS.CACHE_AC_TAM_CONJUNTO; i++) {
            String numBloco = Integer.toString(i);
            escolhaBloco.getItems().add(numBloco);
        }
        escolhaBloco.setValue("0");
    }

    @FXML
    private void trocarBlocoTabela(ActionEvent actionEvent) {
        this.tabela.getItems().clear();
        int numBloco = Integer.parseInt(escolhaBloco.getValue());

        for(int i = 0; i < tam; i++) {
            LinhaCacheMD[] linha = this.cache.getLinha(i);
            this.tabela.getItems().add(linha[numBloco]);
        }

        this.numBloco.setText("BLOCO " + numBloco);
    }
}
