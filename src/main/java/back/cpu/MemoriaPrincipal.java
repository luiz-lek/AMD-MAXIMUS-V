package back.cpu;

import back.comum.MAX;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.IntStream;

public class MemoriaPrincipal{
    private String[] memoria = new String[MAX.TAMMEMP];
    private LinkedList<Integer> posAcessadas = new LinkedList<>();

    public MemoriaPrincipal(){
        IntStream.range(0, MAX.TAMMEMP).forEach(i -> this.memoria[i] = "0000000000000000");
    }

    public String ler(String posicao) throws IllegalAccessError {
        short pos = Short.parseShort(posicao, 2);
        validarPosicao(pos);
        this.verificaAcessos((int)pos);

        return this.memoria[pos];
    }

    public void escrever(String posicao, String palavra)
            throws IllegalAccessError, IllegalAccessError {
        short pos = Short.parseShort(posicao, 2);

        validarPosicao(pos);
        validarPalavra(palavra);
        this.memoria[pos] = palavra;

        this.verificaAcessos((int)pos);
    }

    private void validarPosicao(short pos) throws IllegalAccessError{
        if((pos < 0) || (pos >= MAX.TAMMEMP)) throw new IllegalAccessError("Posição da memória inválida.");
    }

    private void verificaAcessos(int pos) {
        if(posAcessadas.contains(pos)) return;
        this.inserirPosAcessadas(pos);
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

        stack.append("SP -> ").append(sp).append(": ").append(this.memoria[sp]);
        sp++;

        int i;

        for(i = sp; i < MAX.TAMMEMP; i++) {
            stack.append("\n          " + i).append(": ").append(this.memoria[i]);
            auxCont++;
        }

        for(i = auxCont; i < 22; i++) saida.append("\n");
        saida.append(stack);

        return saida.toString();
    }

    public void inserirPosAcessadas(int pos) {
        if(this.posAcessadas.isEmpty()){
            this.posAcessadas.add(pos);
            return;
        }

        int i = 0;

        for(Integer val: posAcessadas){
            if(val >= pos) break;
            i++;
        }

        this.posAcessadas.add(i, pos);
    }

    public String posicoesAcessadas() {
        StringBuilder saida = new StringBuilder();

        for(Integer i : posAcessadas) {
            saida.append(i.toString()).append(": ").append(memoria[i]).append("\n");
        }

        return saida.toString();
    }
}
