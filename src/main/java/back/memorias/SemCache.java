package back.memorias;

public class SemCache implements Cache {
    private MemoriaPrincipal memoriaPrincipal;
    private boolean isLendo, isEscrevendo;

    public SemCache(MemoriaPrincipal memoriaPrincipal) { this.memoriaPrincipal = memoriaPrincipal; }

    public String ler(String endereco) throws Exception {
        boolean temp = isLendo;
        isLendo = !isLendo;

        if(temp) {
            String bloco = memoriaPrincipal.ler(endereco);
            return bloco;
        }

        return null;
    }


    public boolean escrever(String endereco, String dado) throws Exception {
        boolean temp = isEscrevendo;
        isEscrevendo = !isEscrevendo;

        if(temp) {
            memoriaPrincipal.escrever(endereco, dado);
            return false;
        }

        return true;
    }
}
