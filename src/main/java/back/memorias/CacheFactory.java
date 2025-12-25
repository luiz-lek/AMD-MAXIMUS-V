package back.memorias;

import java.io.IOException;

import static back.comum.Constantes.*;

public class CacheFactory {
    private CacheFactory() {
        throw new UnsupportedOperationException("Classe não pode ser instanciada.");
    }

    public static Cache criarCache(String tipoCache, MemoriaPrincipal memoriaPrincipal) throws IOException {
        return switch (tipoCache) {
            case  CACHE_TIPO_ASS -> new CacheAssociativa(memoriaPrincipal, tipoCache);
            case CACHE_TIPO_MD -> new CacheMapeamentoDireto(memoriaPrincipal, tipoCache);
            case CACHE_TIPO_AC -> new CacheAssociativaConjunto(memoriaPrincipal, tipoCache);
            case CACHE_TIPO_SEM_CACHE ->  new SemCache(memoriaPrincipal, tipoCache);
            default -> throw new IOException("Tipo de cache inválido");
        };
    }
}
