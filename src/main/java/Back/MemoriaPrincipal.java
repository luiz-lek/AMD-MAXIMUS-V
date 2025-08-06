package Back;

public class MemoriaPrincipal{
    private String memoria[];

    public MemoriaPrincipal(){
        this.memoria = new String[MAX.TAMMEMP];
    }

    public String ler(short posicao) throws IllegalAccessError{
        try{
            validarPosicao(posicao);
            return this.memoria[posicao];
        } catch(IllegalAccessError e){
            throw e;
        }
    }

    public void escrever(short posicao, String palavra)
            throws IllegalAccessError, IllegalAccessError{
        try{
            validarPosicao(posicao);
            validarPalavra(palavra);
            this.memoria[posicao] = palavra;
        } catch(IllegalArgumentException | IllegalAccessError e){
            throw e;
        }
    }

    private void validarPosicao(short pos) throws IllegalAccessError{
        if((pos < 0) || (pos > MAX.TAMMEMP)) throw new IllegalAccessError("Posição da memória inválida.");
    }

    private void validarPalavra(String palavra) throws IllegalArgumentException{
        if(palavra.length() != MAX.TAMPAL) throw new IllegalArgumentException("Tamanho de palavra inválido.");
    }
}
