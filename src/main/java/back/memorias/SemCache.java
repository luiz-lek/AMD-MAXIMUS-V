package back.memorias;

import back.comum.Constantes;
import back.cpu.MBR;

public class SemCache implements Cache {
    private MemoriaPrincipal memoriaPrincipal;
    private boolean rd = false, wr = false;

    private int tempoResposta = Constantes.ATRASO_MEMORIA;
    private int acAtraso = 0;

    private String tipoCache;

    public SemCache(MemoriaPrincipal memoriaPrincipal, String tipoCache) {
        this.memoriaPrincipal = memoriaPrincipal;
        this.tipoCache = tipoCache;
    }

    public void ler(String endereco, MBR mbr) throws Exception {
        if(this.rd) {
            if(acAtraso < Constantes.ATRASO_MEMORIA) {
                acAtraso++;
                return;
            }
            String dado = this.memoriaPrincipal.ler(endereco);
            mbr.setValor(dado);
            mbr.setReady('1');

            this.rd = false;
            this.acAtraso = 0;

            return;
        }

        this.rd = true;
        this.acAtraso = 0;

        mbr.setReady('0');
    }


    public void escrever(String endereco, MBR mbr) throws Exception {
        if(this.wr) {
            if(acAtraso < Constantes.ATRASO_MEMORIA) {
                acAtraso++;
                return;
            }
            String dado = mbr.getValor();
            this.memoriaPrincipal.escrever(endereco, dado);
            mbr.setValor(dado);
            mbr.setReady('1');

            this.wr = false;
            this.acAtraso = 0;

            return;
        }

        this.wr = true;
        this.acAtraso = 0;

        mbr.setReady('0');
    }

    public int size() { return 0; }

    @Override
    public String getTipoCache() { return this.tipoCache; }
}
