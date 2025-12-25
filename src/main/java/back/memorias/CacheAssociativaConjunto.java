package back.memorias;
import back.comum.CONSTS;
import back.comum.Conversao;
import back.cpu.MBR;

import java.io.IOException;

public class CacheAssociativaConjunto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheMD[][] cache = new LinhaCacheMD[CONSTS.CACHE_AC_NUM_LIHAS][CONSTS.CACHE_AC_TAM_CONJUNTO];
    private int acSubstituicao = 0;
    private int blocoSubstituir = 0;
    private int tamTag;
    private int tamIndice;
    private int tamBloco;
    private int tamEnderecoBloco;
    private int tempoResposta = CONSTS.ATRASO_MEMORIA + 2;
    private int acAtraso = 0;

    private boolean rd = false, wr = false;

    public CacheAssociativaConjunto(MemoriaPrincipal memoriaPrincipal) throws IOException {
        this.tamBloco = (int)(Math.log(CONSTS.MEMP_TAM_BLOCO) / Math.log(2));
        this.tamEnderecoBloco = (int)(Math.log(CONSTS.MEMP_NUM_ENDERECOS) / Math.log(2)) - this.tamBloco;
        this.tamIndice = (int)(Math.log(CONSTS.CACHE_AC_NUM_LIHAS) / Math.log(2));
        this.tamTag = this.tamEnderecoBloco - this.tamIndice;
        this.memoriaPrincipal = memoriaPrincipal;

        for(short i = 0; i < CONSTS.CACHE_AC_NUM_LIHAS; i++){
            for(int j = 0; j < CONSTS.CACHE_AC_TAM_CONJUNTO; j++) {
                cache[i][j] = new LinhaCacheMD(this.tamTag, i);
            }
        }
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
            this.lerBlocoConjuntoRD(conjunto, endereco, tag, mbr);
            mbr.setReady('1'); //Cache hit
            this.rd = false;
            this.acAtraso = 0;
            return;
        }

        int i;
        for(i = 0; i < CONSTS.CACHE_AC_TAM_CONJUNTO; i++) {
            LinhaCacheMD linha = conjunto[i];
            if(!linha.isBitValidade()) {
                this.blocoSubstituir = i;
                break; //Cache miss
            }
            if(linha.comparaTag(tag)) {
                String dado = linha.getEndBloco(endereco);
                mbr.setValor(dado);
                mbr.setReady('1'); //Cache hit
                return;
            }
        }

        if(i >= CONSTS.CACHE_AC_TAM_CONJUNTO) {
            this.blocoSubstituir = definiEIncrementaBlocoASubstituir();
        }

        this.rd = true;
        mbr.setReady('0');//Cache miss
        // A cache agora espera os 100 ciclos da cpu para poder finalizar a leitura.
    }

    private void lerBlocoConjuntoRD(LinhaCacheMD[] conjunto, String endereco, String tag, MBR mbr) throws Exception {
        String dado = mbr.getValor();
        LinhaCacheMD linha = conjunto[this.blocoSubstituir];

        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, linha.getBloco());
        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);
        linha.substituir(tag, bloco);
        dado = linha.getEndBloco(endereco);

        mbr.setValor(dado);
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
        for(i = 0; i < CONSTS.CACHE_AC_TAM_CONJUNTO; i++) {
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

        if(i >= CONSTS.CACHE_AC_TAM_CONJUNTO) this.blocoSubstituir = definiEIncrementaBlocoASubstituir();

        this.wr = true;
        mbr.setReady('0'); //Cache miss
    }

    private void lerBlocoConjuntoWR(LinhaCacheMD[] conjunto, String endereco, String tag, MBR mbr) throws Exception {
        String dado = mbr.getValor();
        LinhaCacheMD linha = conjunto[this.blocoSubstituir];

        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, linha.getBloco());
        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);
        linha.substituir(tag, bloco);
        linha.substituirPalavraBloco(endereco, dado);
    }

    private int definiEIncrementaBlocoASubstituir()  { return this.acSubstituicao++ % CONSTS.CACHE_AC_TAM_CONJUNTO;} // Defini o bloco da linha q
                                                                                           // deve sersubstituido, simulando a política de
                                                                                           // substituição random.
    private int extrairIndiceCache(String endereco) throws Exception {
       String indice = endereco.substring(this.tamTag, this.tamEnderecoBloco);
       return Conversao.binarioToInt(indice, this.tamIndice);
    }

    private String extrairTag(String endereco) { return endereco.substring(0, tamTag); }

    public int size() { return CONSTS.CACHE_AC_NUM_LIHAS; }

    public LinhaCacheMD[] getLinha(int indice) { return this.cache[indice]; }
}