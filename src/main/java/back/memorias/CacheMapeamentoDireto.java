package back.memorias;
import back.comum.CONSTS;
import back.comum.Conversao;

import java.io.IOException;

public class CacheMapeamentoDireto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    private LinhaCacheMD[] cache = new LinhaCacheMD[CONSTS.CACHE_MD_NUM_LIHAS];
    private int substituirLinha = 0;

    public CacheMapeamentoDireto(MemoriaPrincipal memoriaPrincipal) {
        this.memoriaPrincipal = memoriaPrincipal;

        for(int i = 0; i < CONSTS.CACHE_MD_NUM_LIHAS; i++){
            cache[i] = new LinhaCacheMD(4);
        }
    }

    @Override
    public String ler(String endereco) throws Exception {
        int linhaLeitura = extrairlinhaDeEscritaCache(endereco);
        String tag = extrairTag(endereco);

        LinhaCacheMD linha = this.cache[linhaLeitura];
        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            return linha.getEndBloco(endereco); //Cache hit
        }

        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, linha.getBloco());
        linha.substituir(tag, this.memoriaPrincipal.lerBloco(endereco));

        return null; //Cache miss
        //O cache miss é disparado, mas, para fins de simplicidade, ela já pega o dado da MP para disponibilizar na próxima vez
        //que a cpu tentar acessar o endereço, depois de 100 ciclos.
    }

    @Override
    public boolean escrever(String endereco, String dado) throws Exception {
        int linhaEscrita = extrairlinhaDeEscritaCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD linha = this.cache[linhaEscrita];

        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            if(!linha.isDirtyBit()) linha.setDirtyBit('1');
            linha.substituirPalavraBloco(endereco, dado);
            return true; // Cache hit
        }

        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);

        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, bloco);
        linha.substituir(tag, bloco);
        linha.substituirPalavraBloco(endereco, dado);

        return false; // Cache miss
    }

    private int extrairlinhaDeEscritaCache(String endereco) throws Exception {
        String offset = endereco.substring(4, 10);
        return Conversao.binarioToInt(offset, 6);
    }

    private String extrairTag(String endereco) { return endereco.substring(0, 4); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(short i = 0; i < 63; i++) {
            try {
                String endereco = Conversao.shortToString(i, 6);
                sb.append(endereco).append("     ");
                sb.append(cache[i].getLinha()).append('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String endereco = Conversao.shortToString((short)63, 6);
            sb.append(endereco).append("     ");
            sb.append(cache[63].getLinha());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}