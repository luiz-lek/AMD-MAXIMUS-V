package back.memorias;
import back.comum.CONSTS;
import back.comum.Conversao;
import back.cpu.MBR;

import java.io.IOException;

public class CacheMapeamentoDireto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheMD[] cache = new LinhaCacheMD[CONSTS.CACHE_MD_NUM_LIHAS];
    private int substituirLinha = 0;

    private int tamTag;
    private int tamIndice;
    private int tamBloco;
    private int tamEnderecoBloco;

    private boolean rd = false, wr = false;
    private int tempoResposta = CONSTS.ATRASO_MEMORIA + 1;
    private int acAtraso = 0;

    public CacheMapeamentoDireto(MemoriaPrincipal memoriaPrincipal) throws IOException {
        this.memoriaPrincipal = memoriaPrincipal;

        this.tamBloco = (int)(Math.log(CONSTS.MEMP_TAM_BLOCO) / Math.log(2));
        this.tamEnderecoBloco = (int)(Math.log(CONSTS.MEMP_NUM_ENDERECOS) / Math.log(2)) - this.tamBloco;
        this.tamIndice = (int)(Math.log(CONSTS.CACHE_MD_NUM_LIHAS) / Math.log(2));
        this.tamTag = this.tamEnderecoBloco - this.tamIndice;

        for(short i = 0; i < CONSTS.CACHE_MD_NUM_LIHAS; i++){
            cache[i] = new LinhaCacheMD(this.tamTag, i);
        }
    }

    @Override
    public void ler(String endereco, MBR mbr) throws Exception {
        int linhaLeitura = extrairlinhaDeEscritaCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD linha = this.cache[linhaLeitura];

        if(this.rd) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++; //Não altera nada, apenas incrementa os ciclos de atraso;
                return;
            }

            String[] bloco;
            if (linha.isDirtyBit()) {
                bloco = linha.getBloco();
                this.memoriaPrincipal.escreverBloco(endereco, bloco);
            }
            bloco = memoriaPrincipal.lerBloco(endereco);
            linha.substituir(tag, bloco);

            this.rd = false;
            this.acAtraso = 0;

            String dado = linha.getEndBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');
            return;
        }

        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            String dado = linha.getEndBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');//Cache hit
            return;
        }

        this.rd = true;
        mbr.setReady('0'); //Cache miss
    }

    @Override
    public void escrever(String endereco, MBR mbr) throws Exception {
        int linhaEscrita = extrairlinhaDeEscritaCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD linha = this.cache[linhaEscrita];

        if(this.wr) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            String[] bloco;
            if(linha.isDirtyBit()) {
                bloco = linha.getBloco();
                this.memoriaPrincipal.escreverBloco(endereco, bloco);
            }

            bloco = this.memoriaPrincipal.lerBloco(endereco);
            linha.substituir(tag, bloco);

            String dado = mbr.getValor();
            linha.substituirPalavraBloco(endereco, dado);

            this.wr = false;
            this.acAtraso = 0;

            mbr.setReady('1');
            return;
        }

        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            if(!linha.isDirtyBit()) linha.setDirtyBit();
            String dado = mbr.getValor();
            linha.substituirPalavraBloco(endereco, dado);
            mbr.setReady('1'); //Cache hit
            return;
        }

//        String[] bloco = this.memoriaPrincipal.lerBloco(endereco);
//        if(linha.isDirtyBit()) this.memoriaPrincipal.escreverBloco(endereco, bloco);
//        linha.substituir(tag, bloco);
//        linha.substituirPalavraBloco(endereco, dado);

        this.wr = true;
        mbr.setReady('0'); //Cache miss
    }

    private int extrairlinhaDeEscritaCache(String endereco) throws Exception {
        String offset = endereco.substring(this.tamTag, this.tamEnderecoBloco);
        return Conversao.binarioToInt(offset, this.tamIndice);
    }

    private String extrairTag(String endereco) { return endereco.substring(0, this.tamTag); }

    public LinhaCacheMD getLinha(int pos) throws Exception {
        if(pos < 0 || pos >= CONSTS.CACHE_MD_NUM_LIHAS) throw new Exception("Posição na cache inválida.");
        return this.cache[pos];
    }

    public int size() { return CONSTS.CACHE_MD_NUM_LIHAS; }
}