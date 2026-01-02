package back.memorias;

import static back.comum.Constantes.*;
import back.cpu.MBR;

import java.io.IOException;

public class CacheAssociativa implements Cache {
    private MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheASS[] cache = new LinhaCacheASS[CACHE_ASS_NUM_LINHAS];

    private int acSubstituir = 0;
    private int linhaSubstituir = 0;
    private int tamBloco;
    private int tamEndBloco;
    private int tempoResposta = ATRASO_MEMORIA + CACHE_ASS_TA;
    private int acAtraso = 0;
    private int linhaModificada;
    private int linhaSubstituida;
    private int linhaHit;

    private String tipoCache;

    private boolean rd = false, wr = false;

    public CacheAssociativa(MemoriaPrincipal memoriaPrincipal, String tipoCache) throws IOException {
        this.memoriaPrincipal = memoriaPrincipal;

        this.tamEndBloco = (int)(Math.log(MEMP_TAM_BLOCO) / Math.log(2));
        this.tamBloco = (int)(Math.log(MEMP_NUM_ENDERECOS) / Math.log(2)) - this.tamEndBloco;

        for(short i = 0; i < CACHE_ASS_NUM_LINHAS; i++) {
            cache[i] = new LinhaCacheASS(i, this.tamBloco);
        }

        this.tipoCache = tipoCache;
    }

    @Override
    public void ler(String endereco, MBR mbr) throws Exception {
        LinhaCacheASS linha;
        String numBloco = this.extrairNumBloco(endereco);

        if(rd) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            String[] bloco = this.memoriaPrincipal.lerBloco(endereco);
            linha = this.cache[linhaSubstituir];
            if(linha.isDirtyBit()) {
                String enderecoSubs = linha.reconstruirEndereco();
                this.memoriaPrincipal.escreverBloco(enderecoSubs, bloco);
            }
            linha.substituir(numBloco, bloco);

            String dado = linha.getEndBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');

            this.rd = false;
            this.acAtraso = 0;
            return;
        }

        int i;
        for(i = 0; i < CACHE_ASS_NUM_LINHAS; i++) {
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

        if(i >= CACHE_ASS_NUM_LINHAS) this.linhaSubstituir = this.definiEIncrementaBlocoSubstituir();

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

            String dado = mbr.getValor();
            linha.substituirPalavraBloco(endereco, dado);
            mbr.setReady('1'); //Cache hit

            this.wr = false;
            this.acAtraso = 0;
            return;
        }

        int i;
        for (i = 0; i < CACHE_ASS_NUM_LINHAS; i++) {
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

        if(i >= CACHE_ASS_NUM_LINHAS) this.linhaSubstituir = this.definiEIncrementaBlocoSubstituir();

        this.wr = true;
        mbr.setReady('0'); //Cache miss
    }

    public int getLinhaModificada() { return this.linhaModificada; }

    public int getLinhaSubstituida() {  return this.linhaSubstituida; }

    public int getLinhaHit() {  return this.linhaHit; }

    @Override
    public String getTipoCache() { return this.tipoCache; }

    public int definiEIncrementaBlocoSubstituir() {
        return acSubstituir++ % CACHE_ASS_NUM_LINHAS;
    }

    public String extrairNumBloco(String endereco) {
        return endereco.substring(0, tamBloco);
    }

    public int size() {
        return CACHE_ASS_NUM_LINHAS;
    }

    public LinhaCacheASS getLinha(int pos) {
        return cache[pos];
    }

    public MemoriaPrincipal getMemP() { return this.memoriaPrincipal; }
}