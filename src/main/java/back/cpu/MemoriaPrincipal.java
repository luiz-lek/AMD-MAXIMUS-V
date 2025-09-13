package back.cpu;

import back.comum.Conversao;
import back.comum.MAX;

import java.io.IOException;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class MemoriaPrincipal{
    private String[] memoria = new String[MAX.TAMMEMP];
    private LinkedList<Integer> enderecosAcessadas = new LinkedList<>(); // Aramazena os endereços acessados,
                                                                        // para serem exibidos pela interface.
    public MemoriaPrincipal(){
        IntStream.range(0, MAX.TAMMEMP).forEach(i -> this.memoria[i] = "0000000000000000");
    }

    public String ler(String posicao) throws Exception {
        short pos = Short.parseShort(posicao, 2);
        validarPosicao(pos);
        this.verificaAcessos((int)pos); // Insere o endereço na lista de acessados, caso ainda n esteja

        return this.memoria[pos];
    }

    public void escrever(String posicao, String palavra) throws Exception {
        short pos = Short.parseShort(posicao, 2);
        validarPosicao(pos);
        validarPalavra(palavra);
        this.memoria[pos] = palavra;

        this.verificaAcessos((int)pos); // Insere o endereço na lista de acessados, caso ainda n esteja
    }

    private void validarPosicao(short pos) throws Exception {
        if((pos < 0) || (pos >= MAX.TAMMEMP)) throw new Exception("Posição " + pos + " da memória inválida.");
    }

    private void verificaAcessos(int pos) {
        if(enderecosAcessadas.contains(pos)) return;
        this.inserirOrdenadoPosAcessadas(pos);
    }

    private void validarPalavra(String palavra) throws Exception {
        if(palavra.length() != MAX.TAMPAL) throw new Exception("Tamanho de palavra inválido.");
    }

    public void inserirOrdenadoPosAcessadas(int pos) {
        if(this.enderecosAcessadas.isEmpty()){
            this.enderecosAcessadas.add(pos);
            return;
        }

        int i = 0;

        for(Integer val: enderecosAcessadas){
            if(val >= pos) break;
            i++;
        }

        this.enderecosAcessadas.add(i, pos);
    }

    public String posicoesAcessadasBinario() throws Exception { //Retorna uma string em binário de todos os endereços
        StringBuilder saida = new StringBuilder(); // Acessados na memória.
        int maiorEndereco = this.maiorEndereco();

        String endereco;

        for(Integer i : this.enderecosAcessadas) {
            endereco = i.toString();
            endereco = Conversao.ajustarDigitosDecimal(endereco, maiorEndereco);

            saida.append(endereco).append(": ").append(this.memoria[i]);

            String decimal = Conversao.binarioToStrDecimal(this.memoria[i], 16);
            decimal = Conversao.ajustarDigitosDecimal(decimal, 5);

            saida.append("   ").append(decimal).append("\n");
        }

        return saida.toString();
    }

    public String posicoesAcessadasDecimal() throws Exception { //Mesmo que o método acima, mas em decimal
        StringBuilder saida = new StringBuilder();               // e sem o número dos endereços

        for(Integer i : enderecosAcessadas) {
            saida.append(Conversao.binarioToStrDecimal(memoria[i], 16)).append("\n");
        }

        return saida.toString();
    }

    public int maiorEndereco() {
        Integer maiorEndereco = this.enderecosAcessadas.getLast();
        String maiorEnderecoStr =  Integer.toString(maiorEndereco);
        return maiorEnderecoStr.length();
    }
}