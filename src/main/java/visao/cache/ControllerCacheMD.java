package visao.cache;

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

    private int linhaModificada = -1;
    private int linhaSubstituida = -1;
    private int linhaHit = -1;
    private int linhaSubsEMod = -1;

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

        this.tabela.setRowFactory(tv -> new javafx.scene.control.TableRow<LinhaCacheMD>() {
            @Override
            protected void updateItem(LinhaCacheMD item, boolean empty) {
                super.updateItem(item, empty);

                getStyleClass().removeAll("linha-hit", "linha-modificada", "linha-substituida", "linha-subsemod");
                if (item == null || empty) {
                    setStyle("");
                } else {
                    int index = getIndex();

                    if (index == linhaHit) {
                        getStyleClass().add("linha-hit");
                    } else if (index == linhaModificada) {
                        getStyleClass().add("linha-modificada");
                    } else if (index == linhaSubstituida) {
                        getStyleClass().add("linha-substituida");
                    } else if (index == linhaSubsEMod) {
                        getStyleClass().add("linha-subsemod");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    public void atualizarTabela() {
        this.linhaHit = this.cache.getLinhaHit();
        this.linhaModificada = this.cache.getLinhaModificada();
        this.linhaSubstituida = this.cache.getLinhaSubstituida();
        this.linhaSubsEMod = this.cache.getLinhaSubsEMod();

        this.tabela.refresh();
    }
}
