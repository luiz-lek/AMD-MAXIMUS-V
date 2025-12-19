package back.memorias;

import back.comum.Conversao;
import back.comum.MAX;
import java.util.LinkedList;
import java.util.stream.IntStream;

public class MemoriaPrincipal{
    private String[] memoria = new String[MAX.MEMP_NUM_ENDERECOS];
    private LinkedList<Integer> enderecosAcessadas = new LinkedList<>(); //Aramazena os endereços acessados,
                                                                         //para serem exibidos pela interface.
    public MemoriaPrincipal(){
        IntStream.range(0, MAX.MEMP_NUM_ENDERECOS).forEach(i -> this.memoria[i] = "0000000000000000");
    }

    public String ler(String endereco) throws Exception {
        String linha;
        short pos = Short.parseShort(endereco, 2);

        validarPosicao(pos);
        linha = this.memoria[pos];
        this.verificaAcessos((int)pos); //Insere o endereço na lista de acessados, caso ainda n esteja

        return linha;
    }

    public void escrever(String posicao, String palavra) throws Exception {
        short pos = Short.parseShort(posicao, 2);
        validarPosicao(pos);
        validarPalavra(palavra);
        this.memoria[pos] = palavra;

        this.verificaAcessos((int)pos); //Insere o endereço na lista de acessados, caso ainda n esteja
    }

    private void validarPosicao(short pos) throws Exception {
        if((pos < 0) || (pos >= MAX.MEMP_NUM_ENDERECOS)) throw new Exception("Posição " + pos + " da memória inválida.");
    }

    private void verificaAcessos(int pos) {
        if(enderecosAcessadas.contains(pos)) return;
        this.inserirOrdenadoPosAcessadas(pos);
    }

    private void validarPalavra(String palavra) throws Exception {
        if(palavra.length() != MAX.MEMP_TAM_PAL) throw new Exception("Tamanho de palavra inválido.");
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
        StringBuilder saida = new StringBuilder(); //Acessados na memória.
        int maiorEndereco = this.maiorEnderecoAcessado();

        String endereco;
                                                                //Concatena [número do endereço] + [: ]
        for(Integer i : this.enderecosAcessadas) {              //+ [palavra na memória] + [palavra na memória em decimal]
            endereco = i.toString();                            //Completa com 0 a esquerda dos números em decimal, para todas
            endereco = Conversao.ajustarDigitosDecimal(endereco, maiorEndereco); //linhas terem o mesmo tamanho
            saida.append(endereco).append(": ").append(this.memoria[i]);

            String decimal = Conversao.binarioToStrDecimal(this.memoria[i], 16);
            decimal = Conversao.ajustarDigitosDecimal(decimal, 5);

            saida.append("   ").append(decimal).append("\n");
        }

        return saida.toString();
    }

    public int maiorEnderecoAcessado() { //Retorna o endereço dentre os acessados com maior length.
        Integer maiorEndereco = this.enderecosAcessadas.getLast();
        String maiorEnderecoStr = Integer.toString(maiorEndereco);
        return maiorEnderecoStr.length();
    }
}