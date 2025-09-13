package back.montagem;

import back.cpu.MemoriaPrincipal;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Assembler {
    private static final Map<String, String> tabela = new HashMap<>();

    static{
        tabela.put("LODD", "0000");
        tabela.put("STOD", "0001");
        tabela.put("ADDD", "0010");
        tabela.put("SUBD", "0011");
        tabela.put("JPOS", "0100");
        tabela.put("JZER", "0101");
        tabela.put("JUMP", "0110");
        tabela.put("LOCO", "0111");
        tabela.put("LODL", "1000");
        tabela.put("STOL", "1001");
        tabela.put("ADDL", "1010");
        tabela.put("SUBL", "1011");
        tabela.put("JNEG", "1100");
        tabela.put("JNZE", "1101");
        tabela.put("CALL", "1110");
        tabela.put("INSP", "11111100");
        tabela.put("DESP", "11111110");
        tabela.put("PSHI", "1111000000000000");
        tabela.put("POPI", "1111001000000000");
        tabela.put("PUSH", "1111010000000000");
        tabela.put("POP", "1111011000000000");
        tabela.put("RETN", "1111100000000000");
        tabela.put("SWAP", "1111101000000000");
    }

    private int tamProg;
    public CodeParser parser = new CodeParser();

    public void montar(MemoriaPrincipal mem, String programa) throws Exception {
        String[][] programaFormatado = this.parser.parse(programa); // Retira flags, linhas vazias e separa cada linha
        String programaEmBinario = "";                              // em mnemônico e opereando


        int i = 0;

        for(String[] linha : programaFormatado){
            programaEmBinario = macroPraBinario(linha);
            mem.escrever(Integer.toBinaryString(i), programaEmBinario);
            i++;
        }

        this.tamProg = i;
    }

    public String macroPraBinario(String instrucao[]) throws Exception { //Instrução[0] contém o mnemônimo
                                                                        //e instrucao[1] um possível operando.
        if(!tabela.containsKey(instrucao[0])) throw new IOException("Mnemônimo " + instrucao[0] + " inválido.");

        StringBuilder binario = new StringBuilder(tabela.get(instrucao[0]));

        if(instrucao[1] == null) return binario.toString(); // Instrução sem operando

        String binarioStr = binario.toString();
        int tamBin = binarioStr.length();

        if (tamBin == 8) { // Operando com 8 bits tem limite entre 0 e 255.
            binario.append(operandoPraBinario(instrucao[0], instrucao[1], 255, 8));
        } else if ("LOCO".equals(instrucao[0])) { // Loco tem limite até 4095
            binario.append(operandoPraBinario(instrucao[0], instrucao[1], 4095, 12));
        } else { //mnemônimo com 4 bits, sem limite no operando
            binario.append(operandoPraBinario(instrucao[0], instrucao[1], Integer.MAX_VALUE, 12));
        }

        return binario.toString();
    }

    public String operandoPraBinario(String operacao, String operando, int limite, int completar) throws Exception {
        StringBuilder numFinal = new StringBuilder();
        String numBin;
        Integer op;

        try { //Caso seja uma constante.
            op = Integer.parseInt(operando);
        } catch (NumberFormatException e) { //É uma flag, variável, ou jump inválido(com uma flag inexistente).
            op = this.parser.flags.get(operando);
            if(op == null) { //Não encontrou flag, então verifica se a operaçao é do tipo jump.
                if(parser.verificarOpercaoDeDesvio(operacao)) { //Verifica se é um jump inválido.
                    throw new Exception("Desvio para flag \"" + operando + "\"impossível, flag inexistente.");
                }
                op = this.parser.variaveis.get(operando);
                if(op == null) { //A variável ainda não existe, então aloca uma nova posição..
                    op = this.parser.getEIncrementaPosLivre();
                    this.parser.variaveis.put(operando, op);
                }
            }
        }

        if((op < 0) || (op > limite)) throw new IOException("Erro: Operando deve estar entre 0 e "
                + limite + ".");
        numBin = Integer.toBinaryString(op);

        completar -= numBin.length();

        for(int i = 0; i < completar; i++) numFinal.append('0'); //completa com 0 nos bits mais significativos
        numFinal.append(numBin);

        //System.out.println(this.parser.flags.toString());
        //System.out.println(this.parser.variaveis.toString());
        return numFinal.toString();
    }

    public int getTamProg() {
        return tamProg;
    }
}