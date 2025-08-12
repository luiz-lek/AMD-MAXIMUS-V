package Back;

import java.util.stream.IntStream;

public class MemoriaPrincipal{
    private String[] memoria = new String[MAX.TAMMEMP];

    public MemoriaPrincipal(){
        IntStream.range(0, MAX.TAMMEMP).forEach(i -> this.memoria[i] = "0000000000000000");
    }

    public String ler(String posicao) throws IllegalAccessError {
        short pos = Short.parseShort(posicao, 2);
        try{
            validarPosicao(pos);
            return this.memoria[pos];
        } catch(IllegalAccessError e){
            throw e;
        }
    }

    public void escrever(String posicao, String palavra)
            throws IllegalAccessError, IllegalAccessError {
        short pos = Short.parseShort(posicao, 2);
        try{
            validarPosicao(pos);
            validarPalavra(palavra);
            this.memoria[pos] = palavra;
        } catch(IllegalArgumentException | IllegalAccessError e){
            throw e;
        }
    }

    private void validarPosicao(short pos) throws IllegalAccessError{
        if((pos < 0) || (pos >= MAX.TAMMEMP)) throw new IllegalAccessError("Posição da memória inválida.");
    }

    private void validarPalavra(String palavra) throws IllegalArgumentException{
        if(palavra.length() != MAX.TAMPAL) throw new IllegalArgumentException("Tamanho de palavra inválido.");
    }

    public void imprimir(){
        for(int i = 0; i < 20; i++){
            System.out.println(this.memoria[i]);
        }
    }

    public String lerStack(String posicaoSP){
        int sp =  Short.parseShort(posicaoSP, 2);
        StringBuilder stack = new StringBuilder();
        StringBuilder saida = new StringBuilder();
        int auxCont = 0;

        if(sp >= MAX.TAMMEMP) return "Sem elementos na pilha.";

        stack.append("SP -> " + sp + ": " + this.memoria[sp] + "\n");
        sp++;

        for(int i = sp; i < MAX.TAMMEMP; i++) {
            stack.append("          " + i + ": "  + this.memoria[i] + "\n");
            auxCont++;
        }

        for(int i = auxCont; i < 21; i++) saida.append("\n");
        saida.append(stack);

        return saida.toString();
    }
}
