package back.memorias;
import static back.comum.Constantes.*;
import back.comum.Conversao;
import back.cpu.MBR;

import java.io.IOException;

public class CacheAssociativaConjunto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheMD[][] cache = new LinhaCacheMD[CACHE_AC_NUM_LIHAS][CACHE_AC_TAM_CONJUNTO];
    private int acSubstituicao = 0;
    private int blocoSubstituir = 0;
    private int qtdBitsTag;
    private int qtdBitsIndiceCache;
    private int qtdBitsOffsetBloco;
    private int qtdBitsEnderecoBloco;
    private int tempoResposta = ATRASO_MEMORIA + CACHE_AC_TA;
    private int acAtraso = 0;
    private int linhaModificada;
    private int linhaSubstituida;
    private int linhaHit;

    private boolean rd = false, wr = false;

    private String tipoCache;

    public CacheAssociativaConjunto(MemoriaPrincipal memoriaPrincipal, String tipoCache) throws IOException {
        this.qtdBitsOffsetBloco = (int)(Math.log(MEMP_TAM_BLOCO) / Math.log(2));
        int qtdBitsEnderecoMemP = (int)(Math.log(MEMP_NUM_ENDERECOS) / Math.log(2));
        this.qtdBitsEnderecoBloco = qtdBitsEnderecoMemP - this.qtdBitsOffsetBloco;
        this.qtdBitsIndiceCache = (int)(Math.log(CACHE_AC_NUM_LIHAS) / Math.log(2));
        this.qtdBitsTag = this.qtdBitsEnderecoBloco - this.qtdBitsIndiceCache;

        this.memoriaPrincipal = memoriaPrincipal;

        for(short i = 0; i < CACHE_AC_NUM_LIHAS; i++) {
            for(int j = 0; j < CACHE_AC_TAM_CONJUNTO; j++) {
                cache[i][j] = new LinhaCacheMD(this.qtdBitsTag, i);
            }
        }

        this.tipoCache = tipoCache;
    }

    @Override
    public void ler(String endereco, MBR mbr) throws Exception {
        int linhaLeitura = extrairIndiceCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD[] conjunto = this.cache[linhaLeitura];

        if(this.rd) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }
            String dado = this.lerBlocoConjuntoRD(conjunto, endereco, tag);
            mbr.setValor(dado);
            mbr.setReady('1'); //Cache hit
            this.rd = false;
            this.acAtraso = 0;
            return;
        }

        int i;
        for(i = 0; i < CACHE_AC_TAM_CONJUNTO; i++) {
            LinhaCacheMD linha = conjunto[i];
            if(!linha.isBitValidade()) {
                this.blocoSubstituir = i;
                break; //Cache miss
            }
            if(linha.comparaTag(tag)) {
                String dado = linha.getDadoBloco(endereco);
                mbr.setValor(dado);
                mbr.setReady('1'); //Cache hit
                return;
            }
        }

        if(i >= CACHE_AC_TAM_CONJUNTO) {
            this.blocoSubstituir = this.definiEIncrementaBlocoASubstituir();
        }

        this.rd = true;
        mbr.setReady('0');//Cache miss
        // A cache agora espera os 100 ciclos da cpu para poder finalizar a leitura.
    }

    private String lerBlocoConjuntoRD(LinhaCacheMD[] conjunto, String endereco, String tag) throws Exception {
        LinhaCacheMD linha = conjunto[this.blocoSubstituir];
        String dado;
        String[] bloco;
        if(linha.isDirtyBit()) {
            String endSubs = linha.reconstruirEndereco();
            bloco = linha.getBloco();
            this.memoriaPrincipal.escreverBloco(endSubs, bloco);
        }

        bloco = this.memoriaPrincipal.lerBloco(endereco);
        linha.substituir(tag, bloco);
        dado = linha.getDadoBloco(endereco);
        return dado;
    }

    @Override
    public void escrever(String endereco, MBR mbr) throws Exception {
        int linhaEscrita = extrairIndiceCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD[] conjunto = this.cache[linhaEscrita];

        if(this.wr) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            this.lerBlocoConjuntoWR(conjunto, endereco, tag, mbr);
            mbr.setReady('1');
            this.wr = false;
            this.acAtraso = 0;
            return; //Cache hit
        }

        int i;
        for(i = 0; i < CACHE_AC_TAM_CONJUNTO; i++) {
            LinhaCacheMD linha = conjunto[i];
            if(!linha.isBitValidade()) {
                this.blocoSubstituir = i;
                break; //Cache miss
            }
            if(linha.comparaTag(tag)) {
                if(!linha.isDirtyBit()) linha.setDirtyBit();
                String dado = mbr.getValor();
                linha.substituirPalavraBloco(endereco, dado);
                mbr.setReady('1'); //Cache hit
                return;
            }
        }

        if(i >= CACHE_AC_TAM_CONJUNTO) this.blocoSubstituir = this.definiEIncrementaBlocoASubstituir();

        this.wr = true;
        mbr.setReady('0'); //Cache miss
    }

    private void lerBlocoConjuntoWR(LinhaCacheMD[] conjunto, String endereco, String tag, MBR mbr) throws Exception {
        String dado = mbr.getValor();
        LinhaCacheMD linha = conjunto[this.blocoSubstituir];
        String[] bloco;

        if(linha.isDirtyBit()) {
            String endSubs = linha.reconstruirEndereco();
            bloco = linha.getBloco();
            this.memoriaPrincipal.escreverBloco(endereco, bloco);
        }

        bloco = this.memoriaPrincipal.lerBloco(endereco);
        linha.substituir(tag, bloco);
        linha.substituirPalavraBloco(endereco, dado);
    }

    public int getLinhaModificada() { return this.linhaModificada; }

    public int getLinhaSubstituida() {  return this.linhaSubstituida; }

    public int getLinhaHit() {  return this.linhaHit; }

    private int definiEIncrementaBlocoASubstituir()  { return this.acSubstituicao++ % CACHE_AC_TAM_CONJUNTO;} // Defini o bloco da linha q
                                                                                           // deve sersubstituido, simulando a política de
                                                                                           // substituição random.
    private int extrairIndiceCache(String endereco) throws Exception {
       String indice = endereco.substring(this.qtdBitsTag, this.qtdBitsEnderecoBloco);
       return Conversao.binarioToInt(indice, this.qtdBitsIndiceCache);
    }


    private String extrairTag(String endereco) { return endereco.substring(0, qtdBitsTag); }

    public int size() { return CACHE_AC_NUM_LIHAS; }

    public LinhaCacheMD[] getLinha(int indice) { return this.cache[indice]; }

    public String getTipoCache() { return this.tipoCache; }
}