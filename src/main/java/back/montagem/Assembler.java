package back.montagem;

import back.cpu.MemoriaPrincipal;

import java.io.IOException;
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

    private Map<String, Integer> flags;

    public String[] montar(MemoriaPrincipal mem, String programa) throws IOException {
        flags = new HashMap<>();

        String[] programaFormatado = CodeParser.parse(programa);

        int tamProg = programaFormatado.length;

        if(tamProg == 0) throw new IOException("Programa vazio");

        for(int i = 0; i < tamProg; i++){
            String programaTrim = programaFormatado[i].trim();
            programaFormatado[i] = programaTrim;
            String binario = this.macroPraBinario(programaFormatado[i], i);

            if(binario != null) {
                System.out.println("Linha" + i + ": " + binario);
                mem.escrever(Integer.toBinaryString(i), binario);
            }
        }

        System.out.println(flags.toString());
        return programaFormatado;
    }

    public String macroPraBinario(String instrucao, int posEscMem) throws IOException {
        StringBuilder opcode = new StringBuilder();
        int tam = instrucao.length(), i = 0;

        for(; i < tam; i++){
            if((instrucao.charAt(i) == ' ') || (instrucao.charAt(i) == ':')) break;
            opcode.append(instrucao.charAt(i));
        }

        if((i < tam) && (instrucao.charAt(i) == ':')){
            String op = opcode.toString();

            if(flags.containsKey(op)) throw new IOException("Flag já foi usada" + op);
            flags.put(op, posEscMem);

            String aposFlag  = instrucao.substring(i + 2, tam);
            return macroPraBinario(aposFlag, posEscMem);
        }

        if(!tabela.containsKey(opcode.toString())) throw new IOException("Opcode " + opcode + " inválido.");

        StringBuilder binario = new StringBuilder();
        binario.append(tabela.get(opcode.toString()));

        if(i == tam) return binario.toString();

        String binarioSTR =  binario.toString();
        int tamBin = binarioSTR.length();

        if (tamBin == 8) {//opcode de 8 bits
            binario.append(operandoPraBinario(i, tam, 255, 8, instrucao));
        } else if (binarioSTR.equals("0111") || binarioSTR.equals("0000000000000000")) {// instrução sem operando
            binario.append(operandoPraBinario(i, tam, 4095, 12, instrucao));
        } else {
            binario.append(operandoPraBinario(i, tam, Integer.MAX_VALUE, 12, instrucao));
        }

        return binario.toString();
    }

    public String operandoPraBinario(int i, int tam, int limite, int completar, String instrucao) throws IOException{
        StringBuilder operandoSTR = new StringBuilder();
        StringBuilder numFinal = new StringBuilder();
        String numBin;
        int operando;

        while(instrucao.charAt(i) == ' ') i++;

        for(; i < tam; i++) operandoSTR.append(instrucao.charAt(i));

        try {
            operando = Integer.parseInt(operandoSTR.toString());
        } catch (NumberFormatException e) {
            Integer op = flags.get(operandoSTR.toString());

            if(op == null) throw new IOException("Nenhum valor associado a flag " + operandoSTR.toString());

            operando = op;
        }

        if((operando < 0) || (operando > limite)) throw new IOException("Erro: Operando deve estar entre 0 e "
                + limite + ".");
        numBin = Integer.toBinaryString(operando);

        completar -= numBin.length();

        for(i = 0; i < completar; i++) numFinal.append('0'); //completa com 0 nos bits mais significativos
        numFinal.append(numBin);

        return numFinal.toString();
    }
}