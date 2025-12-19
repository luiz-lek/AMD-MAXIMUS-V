package back.memorias;
import back.comum.MAX;

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


}
