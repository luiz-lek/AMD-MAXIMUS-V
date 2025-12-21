package back.memorias;
import back.comum.MAX;
import back.comum.Conversao;

import java.io.IOException;

public class CacheMapeamentoDireto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    private LinhaCacheMD[][] cache = new LinhaCacheMD[MAX.CACHE_AC_NUM_LIHAS][2];
    private int substituirLinha = 0;

    public CacheMapeamentoDireto(MemoriaPrincipal memoriaPrincipal) {
        this.memoriaPrincipal = memoriaPrincipal;

        for(int i = 0; i < MAX.CACHE_AC_NUM_LIHAS; i++){
            cache[i][0] = new LinhaCacheMD();
            cache[i][1] = new LinhaCacheMD();
        }
    }

    @Override
    public String ler(String endereco) throws Exception {
        int linhaLeitura = extrairlinhaDeEscritaCache(endereco);
        String tag = extrairTag(endereco);

        for(int i = 0; i < 2; i++) {
            LinhaCacheMD linha = this.cache[linhaLeitura][i];
            if(linha.isBitValidade() && linha.comparaTag(tag)) {
                return linha.getEndBloco(endereco); //Cache hit
            }
            if(!linha.isBitValidade()) {
                linha.substituir(tag, this.memoriaPrincipal.lerBloco(endereco));
                return null;
            }
        }

        int pos = definiEIncrementaBlocoASubstituir();
        LinhaCacheMD linha = this.cache[linhaLeitura][pos];

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

        for(int i = 0; i < 2; i++) {
            LinhaCacheMD linha = this.cache[linhaEscrita][i];
            if(linha.isBitValidade() && linha.comparaTag(tag)) {
                if(!linha.isDirtyBit()) linha.setDirtyBit('1');
                linha.substituirPalavraBloco(endereco, dado);
                return true; //Cache hit
            }

            if(!linha.isBitValidade()) {
                String[] bloco = this.memoriaPrincipal.lerBloco(endereco);
                linha.substituir(tag, bloco);
                linha.substituirPalavraBloco(endereco, dado);
                return false; //Cache miss
            }
        }

        int pos = definiEIncrementaBlocoASubstituir();
        LinhaCacheMD linha = this.cache[linhaEscrita][pos];
        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);

        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, bloco);
        linha.substituir(tag, bloco);
        linha.substituirPalavraBloco(endereco, dado);

        return false; //cache miss
    }

    private int definiEIncrementaBlocoASubstituir()  { return this.substituirLinha++ % 2;} // Defini o bloco da linha q
    // deve sersubstituido, simulando a política de
    // substituição random.
    private int extrairlinhaDeEscritaCache(String endereco) throws Exception {
        String offset = endereco.substring(5, 10);
        return Conversao.binarioToInt(offset, 5);
    }

    private String extrairTag(String endereco) { return endereco.substring(0, 5); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for(short i = 0; i < 31; i++) {
            try {
                String endereco = Conversao.shortToString(i, 5);
                sb.append(endereco).append("      ");
                sb.append(cache[i][0].getLinha());
                sb.append("         ");
                sb.append(cache[i][1].getLinha()).append('\n');
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            String endereco = Conversao.shortToString((short)31, 5);
            sb.append(endereco).append("      ");
            sb.append(cache[31][0].getLinha());
            sb.append("         ");
            sb.append(cache[31][1].getLinha());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}