package back.memorias;

import back.comum.CONSTS;
import back.cpu.MBR;

import java.io.IOException;

public class CacheAssociativa implements Cache {
    private MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheASS[] cache = new LinhaCacheASS[CONSTS.CACHE_ASS_NUM_LIHAS];

    private int acSubstituir = 0;
    private int linhaSubstituir = 0;

    private int tamBloco;
    private int tamEndBloco;

    private int tempoResposta = CONSTS.ATRASO_MEMORIA + 4;
    private int acAtraso = 0;

    private boolean rd = false, wr = false;

    public CacheAssociativa(MemoriaPrincipal memoriaPrincipal) throws IOException {
        this.memoriaPrincipal = memoriaPrincipal;

        this.tamEndBloco = (int)(Math.log(CONSTS.MEMP_TAM_BLOCO) / Math.log(2));
        this.tamBloco = (int)(Math.log(CONSTS.MEMP_NUM_ENDERECOS) / Math.log(2)) - this.tamBloco;

        for (short i = 0; i < CONSTS.CACHE_ASS_NUM_LIHAS; i++) {
            cache[i] = new LinhaCacheASS(i);
        }
    }

    @Override
    public void ler(String endereco, MBR mbr) throws Exception {
        LinhaCacheASS linha;
        String numBloco = this.extrairNumBloco(endereco);
        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);


        if(rd) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            linha = this.cache[linhaSubstituir];
            if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, bloco);
            linha.substituir(numBloco, bloco);

            String dado = linha.getEndBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');

            this.rd = false;
            this.acAtraso = 0;
            return;
        }

        int i;
        for(i = 0; i < CONSTS.CACHE_ASS_NUM_LIHAS; i++) {
            linha = cache[i];
            if (!linha.isValidade()) {
                this.linhaSubstituir = i;
                break; //Cache miss
            }

            if(linha.comparaNumBloco(numBloco)) {
                String dado = linha.getEndBloco(endereco);
                mbr.setValor(dado);
                mbr.setReady('1');
                return; //Cache hit
            }
        }

        if(i >= CONSTS.CACHE_ASS_NUM_LIHAS) this.linhaSubstituir = this.definiEIncrementaBlocoSubstituir();

        this.rd = true;
        mbr.setReady('0'); //Cache miss, agora espera 100 ciclos
        this.acAtraso = 0;
    }

    @Override
    public void escrever(String endereco, MBR mbr) throws Exception {
        LinhaCacheASS linha;
        String numBloco = this.extrairNumBloco(endereco);
        String[] bloco;

        if(this.wr) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            linha = this.cache[linhaSubstituir];
            if(linha.isDirtyBit()) {
                String[] blocoAlterado = linha.getBloco();
                this.memoriaPrincipal.escreverBloco(endereco, blocoAlterado);
            }

            bloco = this.memoriaPrincipal.lerBloco(endereco);
            linha.substituir(numBloco, bloco);

            String dado = linha.getEndBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1'); //Cache hit
            return;
        }

        int i;
        for (i = 0; i < CONSTS.CACHE_ASS_NUM_LIHAS; i++) {
            linha = cache[i];

            if(!linha.isValidade()) {
                this.linhaSubstituir = i;
                break;
            }
            if(linha.comparaNumBloco(numBloco)) {
                String dado = mbr.getValor();
                linha.substituirPalavraBloco(endereco, dado);
                if(!linha.isDirtyBit()) linha.setDirtyBit();
                return;
            }
        }

        if(i >= CONSTS.CACHE_ASS_NUM_LIHAS) this.linhaSubstituir = this.definiEIncrementaBlocoSubstituir();

        this.wr = true;
        this.acAtraso = 0;

        mbr.setReady('0'); //Cache miss
    }


    public int definiEIncrementaBlocoSubstituir() {
        return acSubstituir++ % CONSTS.CACHE_ASS_NUM_LIHAS;
    }

    public String extrairNumBloco(String endereco) {
        return endereco.substring(0, tamBloco);
    }

    public int size() {
        return CONSTS.CACHE_ASS_NUM_LIHAS;
    }

    public LinhaCacheASS getLinha(int pos) {
        return cache[pos];
    }
}