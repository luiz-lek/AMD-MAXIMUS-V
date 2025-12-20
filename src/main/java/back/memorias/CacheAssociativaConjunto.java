package back.memorias;
import back.comum.MAX;
import back.comum.Conversao;

import java.io.IOException;

public class CacheAssociativaConjunto extends Cache {
    private LinhaCacheMD[][] cache = new LinhaCacheMD[MAX.CACHE_AC_NUM_LIHAS][2];
    private boolean substituirLinha = true;

    public CacheAssociativaConjunto(MemoriaPrincipal memoriaPrincipal) {
        this.memoriaPrincipal = memoriaPrincipal;

        for(int i = 0; i < MAX.CACHE_AC_NUM_LIHAS; i++){
            cache[i][0] = new LinhaCacheMD();
            cache[i][1] = new LinhaCacheMD();
        }
    }

    public String lerEndereco(String endereco) throws Exception {
        int linhaLeitura = linhaDeEscritaCache(endereco);
        String tag = getTagEndereco(endereco);

        for(int i = 0; i < 2; i++) {
            LinhaCacheMD linha = this.cache[linhaLeitura][i];
            if(linha.isBitValidade() && linha.comparaTag(tag)) {
                return linha.getEndBloco(endereco); //Cache hit
            }
            if(!linha.isBitValidade()) {
                linha.substituir(tag, this.lerBlocoMP(endereco));
                return null;
            }
        }

        int pos = linhaASubstituir();
        LinhaCacheMD linha = this.cache[linhaLeitura][pos];

        if(linha.isDirtyBit()) this.escreverBlocoMP(endereco, linha.getBloco());
        linha.substituir(tag, this.lerBlocoMP(endereco));
        this.substituirLinha = !this.substituirLinha;

        return null; //Cache miss
        //O cache miss é disparado, mas, para fins de simplicidade, ela já pega o dado da MP para disponibilizar na próxima vez
        //que tentar acessar o endereço, depois de 100 ciclos da cpu
    }

    public boolean escrever(String endereco, String dado) throws Exception {
        int linhaEscrita = linhaDeEscritaCache(endereco);
        String tag = getTagEndereco(endereco);

        for(int i = 0; i < 2; i++) {
            LinhaCacheMD linha = this.cache[linhaEscrita][i];
            if(linha.isBitValidade() && linha.comparaTag(tag)) {
                if(!linha.isDirtyBit()) linha.setDirtyBit('1');
                linha.substituirPalavraBloco(endereco, dado);
                System.out.println("\n\n");
                return true;
            }

            if(!linha.isBitValidade()) {
                String[] bloco = lerBlocoMP(endereco);
                System.out.println("Bloco: " + Conversao.binarioToInt(endereco, 12));
                linha.substituir(tag, bloco);
                linha.substituirPalavraBloco(endereco, dado);
                return false;
            }
        }

        int pos = linhaASubstituir();
        LinhaCacheMD linha = this.cache[linhaEscrita][pos];
        String[] bloco = lerBlocoMP(endereco);

        if(linha.isDirtyBit()) this.escreverBlocoMP(endereco, bloco);
        linha.substituir(tag, bloco);
        linha.substituirPalavraBloco(endereco, dado);
        this.substituirLinha = !this.substituirLinha;

        return false;
    }

    private int linhaASubstituir()  { return this.substituirLinha ? 1 : 0;}

    private int linhaDeEscritaCache(String endereco) throws Exception {
       String offset = endereco.substring(5, 10);
       return Conversao.binarioToInt(offset, 5);
    }

    private String getTagEndereco(String endereco) { return endereco.substring(0, 5); }

    private String[] lerBlocoMP(String endereco) throws Exception {
        endereco = endereco.substring(0, 10);
        endereco += "00";

        String[] bloco = new String[4];
        short pos = Short.parseShort(endereco, 2);

        for(int i = 0; i < 4; i++) {
            bloco[i] = this.memoriaPrincipal.ler(endereco);
            endereco = Conversao.shortToString(++pos, 12);
        }

        return bloco;
    }

    private void escreverBlocoMP(String endereco, String[] bloco) throws Exception {
        endereco = endereco.substring(0, 10);
        endereco += "00";

        short pos = Short.parseShort(endereco, 2);

        for(int i = 0; i < 4; i++, pos++) {
            this.memoriaPrincipal.escrever(endereco, bloco[i]);
            pos++;
            endereco = Conversao.shortToString(pos, 12);
        }
    }

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