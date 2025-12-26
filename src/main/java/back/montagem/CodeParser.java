package back.montagem;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeParser {
    public Map<String, Integer> labels = new HashMap<>();
    public Map<String, Integer> variaveis = new HashMap<>();

    public String[] operacoesSemOperando = new String[] {"PSHI", "POPI", "PUSH", "POP", "RETN", "SWAP"};
    public String[] operacoesApenasConstantes = new String[] {"LOCO", "LODL", "STOL", "ADDL", "SUBL"};
    public String[] progFormatado;

    private int posLivre;

    private boolean pulouLinha;

    public String[][] parse(String macroPrograma) throws IOException {
        if(macroPrograma == null) throw new IOException("Porograma vazio.\n");
        this.formatarPrograma(macroPrograma);
        return this.parseLinhasEFlags();
    }

    public String[][] parseLinhasEFlags() throws IOException {
        String[][] macroLimpo = new String[this.posLivre][];  //Converte o programa fornecido em uma matriz.
        String[] linhaLimpa;                                  //Cada índice dela armazena uma instrução no formato
        int i = 0;                                            //macroLimpo[i][0] = Mnemônico
                                                              //macroLimpo[i][1] = Operando
        for(String linha : this.progFormatado) {              //(caso não tenha operando, recebe null)
            linhaLimpa = this.parseLinhaEFlags(linha, i);     //As linhas vazias e flags são ignoradas.

            if(linhaLimpa != null) {
                macroLimpo[i] = linhaLimpa;
                i++;
            }
        }

        return Arrays.copyOf(macroLimpo, i);
    }

    public int getEIncrementaEnderecoLivre() { return this.posLivre++; }

    public Integer getValorVariavel(String variavel) {
        String upperCase = variavel.toUpperCase();

        return this.variaveis.get(upperCase);
    }

    public String[] parseLinhaEFlags(String linha, int numLinha) throws IOException {
        if(linha.isEmpty()) return null;

        int linhaLength = linha.length();

        String[] linhaFormatada = new String[2];
        StringBuilder mnemonico = new StringBuilder();
        int i;

        for(i = 0; i < linhaLength; i++) {
            if(linha.charAt(i) == ' ' || linha.charAt(i) == ':') break;
            mnemonico.append(linha.charAt(i));
        }

        if(i == linhaLength) { //Operação sem operando
            linhaFormatada[0] = linha;
            linhaFormatada[1] = null;
            return linhaFormatada;
        }

        String mnemonicoStr = mnemonico.toString();

        if(linha.charAt(i) == ':') { //Caso seja um label.
            this.labels.put(mnemonicoStr, numLinha);
            String linhaAposLabel = linha.substring(i+1).trim();
            return parseLinhaEFlags(linhaAposLabel, numLinha); //Retorna uma possível operação
        }                                                                               //existente após o label

        //Linha sem flag.
        linhaFormatada[0] = mnemonicoStr;

        String operando = this.extrairOperando(linha, mnemonicoStr, i);

        try { // Verifica se a operação deve trabalhar com constante ou variável.
            int operandoInt = Integer.parseInt(operando);
        } catch(NumberFormatException e) {
            if(this.mnemonicoComConstante(mnemonicoStr)){
                throw new IOException("\"" + mnemonicoStr + "\" " + "deve receber uma constante.");
            }
        }

        linhaFormatada[1] = operando;
        return linhaFormatada;
    }

    private boolean mnemonicoComOperando(String opcode) {
        for(String semOperando : this.operacoesSemOperando) {
            if(opcode.equals(semOperando)) {
                return false;
            }
        }

        return true;
    }

    private boolean mnemonicoComConstante(String mnemonico) {
        for(String mn : this.operacoesApenasConstantes) {
            if(mn.equals(mnemonico)) return true;
        }
        return false;
    }

    public String extrairOperando(String operacao, String opcode, int j) throws IOException {
        j++;
        StringBuilder operando = new StringBuilder();
        int operacaoLength = operacao.length();
        char c;

        for (; j < operacaoLength; j++) {
            c = operacao.charAt(j);
            if(c == ' ' || c == '#') break;
            operando.append(operacao.charAt(j));
        }

        String operandoSTR = operando.toString();

        if(this.mnemonicoComOperando(opcode)) {
            return operandoSTR;
        }

        if(!operandoSTR.isEmpty()) {
            throw new IOException("Operação não pode conter operando");
        }

        return null;
    }

    public boolean verificarOpercaoDeDesvio(String mnemonico) {
        return mnemonico.charAt(0) == 'J';
    }

    public int maiorLarguraFlag() {
        int maior = 0;

        for (String flag : this.labels.keySet()) {
            int length = flag.length();
            if(length > maior) maior = length;
        }

        return maior;
    }

    public void formatarPrograma(String programa) {
        String[] macroArray = programa.split("\\r?\\n");
        int tamProg = macroArray.length;
        this.progFormatado = new String[tamProg];

        int i = 0;
        int j = 0;

        for(; i < tamProg; i++) {
            String linha = this.formatarLinha(macroArray, i, tamProg);

            if(linha != null) {
                this.progFormatado[j] = linha;

                if (this.pulouLinha) {
                    i++;
                    this.pulouLinha = false;
                }

                j++;
            }
        }

        this.progFormatado = Arrays.copyOf(this.progFormatado, j);
        this.posLivre = j;
    }

    private String formatarLinha(String[] macroArray, int i, int tamProg) {
        if(macroArray[i].isBlank()) return null;

        String linha = macroArray[i].toUpperCase().trim();
        StringBuilder formatarFlag;

        boolean temLabel = false;

        int linhaLength = linha.length();
        int j = 0;

        for(j = 0; j < linhaLength; j++) {
            if(linha.charAt(j) == ':') {
                temLabel = true;
                this.labels.put(linha.substring(0, j), i);
                j++;
                break;
            }
        }

        if((temLabel) && (j >= linhaLength) && ((i+1) < tamProg)){
            formatarFlag = new StringBuilder(linha);
            formatarFlag.append(' ').append(macroArray[i + 1]);
            this.pulouLinha = true;
            linha = formatarFlag.toString();
        }

        return linha.toUpperCase();
    }

    public String progFormatado() {
        int possivelFlagLength = this.maiorLarguraFlag() + 2;
        int progLength = this.progFormatado.length;

        StringBuilder esquerda;
        String flag;
        StringBuilder progFormatado = new StringBuilder();

        for(int i = 0; i < progLength; i++) {
            esquerda = new StringBuilder();
            flag = buscarChave(i);

            int lengthAtual = 0;

            if(flag != null) {
                lengthAtual += flag.length() + 2;
            }

            for(int j = lengthAtual; j < possivelFlagLength; j++) esquerda.append(' ');
            esquerda.append(this.progFormatado[i]);
            progFormatado.append(esquerda).append("\n");
        }

        return progFormatado.toString();
    }

    private String buscarChave(int valor) {
        for(String chave : this.labels.keySet()) {
            if(this.labels.get(chave) == valor) {
                return chave;
            }
        }
        System.out.println();
        return null;
    }
}