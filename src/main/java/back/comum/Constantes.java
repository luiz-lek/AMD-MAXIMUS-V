package back.comum;

public final class Constantes {
    public static final int MEMP_NUM_ENDERECOS = 4096;
    public static final int MEMC_NUM_LINHAS = 256;
    public static final int MEMP_TAM_PAL = 16;
    public static final int TEXTOMICS_NUM_LINHAS = 100;
    public static final int ATRASO_MEMORIA = 100;

    public static final int MEMP_TAM_BLOCO = 4;

    //Dimensões das caches
    //TA: tempo de acesso
    public static final int CACHE_AC_NUM_LIHAS = 32;
    public static final int CACHE_AC_TAM_CONJUNTO = 10;
    public static final int CACHE_AC_TA = 2;

    public static final int CACHE_MD_NUM_LINHAS = 64;
    public static final int CACHE_MD_TA = 1;

    public static final int CACHE_ASS_NUM_LINHAS = 64;
    public static final int CACHE_ASS_TA = 4;


    //Fxml da tela das caches
    public static final String PATH_TELA_CACHE_AC = "/fxml/CacheAC.fxml";
    public static final String PATH_TELA_CACHE_MD = "/fxml/CacheMD.fxml";
    public static final String PATH_TELA_CACHE_ASS = "/fxml/CacheASS.fxml";

    //Path das telas da simulação
    public static final String PATH_TELA1 = "/fxml/Tela1.fxml";
    public static final String PATH_TELA1_FALHA = "/fxml/Tela1Falha.fxml";
    public static final String PATH_TELA2 = "/fxml/Tela2.fxml";
    public static final String PATH_TELA2_VOLTAR = "/fxml/Tela2Voltar.fxml";

    //Css telas
    public static final String PATH_CSS_TELA1 = "/css/StyleTela1.css";
    public static final String PATH_CSS_TELA1FALHA = "/css/StyleTela1Falha.css";
    public static final String PATH_CSS_TELA2 = "/css/StyleTela2.css";

    //Nome das caches
    public static final String CACHE_TIPO_ASS = "ASSOCIATIVA";
    public static final String CACHE_TIPO_MD = "MAPEAMENTO DIRETO";
    public static final String CACHE_TIPO_AC = "ASOOCIATIVO POR CONJUNTO";
    public static final String CACHE_TIPO_SEM_CACHE = "SC";
}