package back.memorias;
import back.comum.MAX;

public class CacheAssociativaConjunto extends Cache {
    private String linhas[] = new String[MAX.CACHE_AC_NUM_LIHAS];

    public CacheAssociativaConjunto(MemoriaPrincipal memoriaPrincipal) {
        this.memoriaPrincipal = memoriaPrincipal;
    }
}
