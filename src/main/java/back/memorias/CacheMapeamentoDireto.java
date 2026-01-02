package back.memorias;
import static back.comum.Constantes.*;
import static back.comum.Conversao.*;
import back.cpu.MBR;

import java.io.IOException;

public class CacheMapeamentoDireto implements Cache {
    public MemoriaPrincipal memoriaPrincipal;
    public LinhaCacheMD[] cache = new LinhaCacheMD[CACHE_MD_NUM_LINHAS];

    private int substituirLinha = 0;
    private int tamTag;
    private int tamIndice;
    private int tamBloco;
    private int tamEnderecoBloco;
    private int tempoResposta = ATRASO_MEMORIA + CACHE_MD_TA;
    private int acAtraso = 0;
    private int linhaModificada = -1;
    private int linhaSubstituida = -1;
    private int linhaHit = -1;
    private int linhaSubsEMod = -1;

    private boolean rd = false, wr = false;

    private String tipoCache;

    public CacheMapeamentoDireto(MemoriaPrincipal memoriaPrincipal, String tipoCache) throws IOException {
        this.memoriaPrincipal = memoriaPrincipal;

        this.tamBloco = (int)(Math.log(MEMP_TAM_BLOCO) / Math.log(2));
        this.tamEnderecoBloco = (int)(Math.log(MEMP_NUM_ENDERECOS) / Math.log(2)) - this.tamBloco;
        this.tamIndice = (int)(Math.log(CACHE_MD_NUM_LINHAS) / Math.log(2));
        this.tamTag = this.tamEnderecoBloco - this.tamIndice;

        for(short i = 0; i < CACHE_MD_NUM_LINHAS; i++){
            cache[i] = new LinhaCacheMD(this.tamTag, i);
        }

        this.tipoCache = tipoCache;
    }

    @Override
    public void ler(String endereco, MBR mbr) throws Exception {
        int linhaLeitura = extrairIndiceCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD linha = this.cache[linhaLeitura];

        if(this.rd) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++; //Não altera nada, apenas incrementa os ciclos de atraso;
                return;
            }

            String[] bloco;
            if (linha.isDirtyBit()) {
                String endSubs = linha.reconstruirEndereco();
                bloco = linha.getBloco();
                this.memoriaPrincipal.escreverBloco(endSubs, bloco);
            }
            bloco = memoriaPrincipal.lerBloco(endereco);
            linha.substituir(tag, bloco);

            this.definiLinhaAlterada(1, linhaLeitura);

            this.rd = false;
            this.acAtraso = 0;

            String dado = linha.getDadoBloco(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');
            return;
        }

        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            String dado = linha.getDadoBloco(endereco);

            this.definiLinhaAlterada(2, linhaLeitura);

            mbr.setValor(dado);
            mbr.setReady('1');//Cache hit
            return;
        }

        this.rd = true;
        mbr.setReady('0'); //Cache miss
    }

    @Override
    public void escrever(String endereco, MBR mbr) throws Exception {
        int linhaEscrita = extrairIndiceCache(endereco);
        String tag = extrairTag(endereco);
        LinhaCacheMD linha = this.cache[linhaEscrita];

        if(this.wr) {
            if(this.acAtraso < this.tempoResposta) {
                this.acAtraso++;
                return;
            }

            String[] bloco;
            if(linha.isDirtyBit()) {
                String endSubs = linha.reconstruirEndereco();
                bloco = linha.getBloco();
                this.memoriaPrincipal.escreverBloco(endSubs, bloco);
            }

            bloco = this.memoriaPrincipal.lerBloco(endereco);
            linha.substituir(tag, bloco);

            String dado = mbr.getValor();
            linha.substituirPalavraBloco(endereco, dado);
            linha.setDirtyBit();

            this.definiLinhaAlterada(3, linhaEscrita);

            this.wr = false;
            this.acAtraso = 0;

            mbr.setReady('1');
            return;
        }

        if(linha.isBitValidade() && linha.comparaTag(tag)) {
            if(!linha.isDirtyBit()) linha.setDirtyBit();
            String dado = mbr.getValor();
            linha.substituirPalavraBloco(endereco, dado);

            this.definiLinhaAlterada(0, linhaEscrita);

            mbr.setReady('1'); //Cache hit
            return;
        }

        this.wr = true;
        mbr.setReady('0'); //Cache miss
    }

    public void definiLinhaAlterada(int op, int numLinha) {
        switch (op) {
            case 0 -> {
                this.linhaModificada = numLinha;
                this.linhaSubstituida = -1;
                this.linhaHit = -1;
                this.linhaSubsEMod = -1;
            }
            case 1 -> {
                this.linhaSubstituida = numLinha;
                this.linhaModificada = -1;
                this.linhaHit = -1;
                this.linhaSubsEMod = -1;
            }
            case 2 -> {
                this.linhaHit = numLinha;
                this.linhaModificada = -1;
                this.linhaSubstituida = -1;
                this.linhaSubsEMod = -1;
            }
            default -> {
                this.linhaSubsEMod = numLinha;
                this.linhaHit = -1;
                this.linhaModificada = -1;
                this.linhaSubstituida = -1;
            }
        }
    }

    public int getLinhaModificada() { return this.linhaModificada; }

    public int getLinhaSubstituida() {  return this.linhaSubstituida; }

    public int getLinhaHit() {  return this.linhaHit; }

    public int getLinhaSubsEMod() { return this.linhaSubsEMod; }

    private int extrairIndiceCache(String endereco) throws Exception {
        String offset = endereco.substring(this.tamTag, this.tamEnderecoBloco);
        return binarioToInt(offset, this.tamIndice);
    }

    private String extrairTag(String endereco) { return endereco.substring(0, this.tamTag); }

    public LinhaCacheMD getLinha(int pos) throws Exception {
        if(pos < 0 || pos >= CACHE_MD_NUM_LINHAS) throw new Exception("Posição na cache inválida.");
        return this.cache[pos];
    }

    public int size() { return CACHE_MD_NUM_LINHAS; }

    public String getTipoCache() { return this.tipoCache; }

    public MemoriaPrincipal getMemP() { return this.memoriaPrincipal; }
}