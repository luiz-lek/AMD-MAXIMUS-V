package back.memorias;
import back.comum.MAX;
import back.comum.Conversao;

public class CacheAssociativaConjunto extends Cache {
    private LinhaCacheMD[][] cache = new LinhaCacheMD[MAX.CACHE_AC_NUM_LIHAS][2];
    int substituirLinha = 0;

    public CacheAssociativaConjunto(MemoriaPrincipal memoriaPrincipal) {
        this.memoriaPrincipal = memoriaPrincipal;

        for(int i = 0; i < MAX.CACHE_AC_NUM_LIHAS; i++){
            cache[i][0] = new LinhaCacheMD();
            cache[i][1] = new LinhaCacheMD();
        }
    }

    private String lerEndereco(String endereco) throws Exception {
        int linhaALer = getOffsetCacheEndereco(endereco);
        String tag = getTagEndereco(endereco);

        for(int i = 0; i < 2; i++) {
            if(this.cache[linhaALer][i].isBitValidade() && this.cache[linhaALer][i].equals(tag)) {

                //return this.cache[linhaALer]
            }
        }
    }

    public int getOffsetCacheEndereco(String endereco) throws Exception {
       String offset = endereco.substring(5, 10);
       return Conversao.binarioToInt(endereco, 5);
    }



    private String getTagEndereco(String endereco) { return endereco.substring(0, 5); }

    private String[] lerBloco(String endereco) throws Exception {
        String[] bloco = new String[4];
        short pos = Short.parseShort(endereco, 2);

        for(int i = 0; i < 4; i++, pos++) {
            bloco[i] = this.memoriaPrincipal.ler(endereco);
        }

        return bloco;
    }
}
