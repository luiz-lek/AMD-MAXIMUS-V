package Back;

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
        tabela.put("HALT", "0000000000000000");
    }

    public void montar(MemoriaPrincipal mem, String[] programa, int tamProg) throws IOException {
        if(tamProg == 0) throw new IOException("Programa vazio");
        for(int i = 0; i < tamProg; i++){
            try {
                String binario = macroPraBinario(programa[i]);
                mem.escrever(Integer.toBinaryString(i), binario);
            }  catch (IOException e){
                throw e;
            }
        }
    }

    public String macroPraBinario(String instrucao) throws IOException {
        StringBuilder opcode = new StringBuilder();
        int tam = instrucao.length(), i = 0;

        for(; i < tam; i++){
            if(instrucao.charAt(i) == ' ') break;
            opcode.append(instrucao.charAt(i));
        }

        if(!tabela.containsKey(opcode.toString())) throw new IOException("Opcode " + opcode + " inválido.");

        StringBuilder binario = new StringBuilder();
        binario.append(tabela.get(opcode.toString()));

        if(i == tam) return binario.toString();

        try {
            String binarioSTR =  binario.toString();
            int tamBin = binarioSTR.length();

            if (tamBin == 8) {//opcode de 8 bits
                binario.append(operandoPraBinario(i, tam, 255, 8, instrucao));
            } else if (binarioSTR.equals("0111") || binarioSTR.equals("0000000000000000")) {// instrução sem operando
                binario.append(operandoPraBinario(i, tam, 4095, 12, instrucao));
            } else {
                binario.append(operandoPraBinario(i, tam, Integer.MAX_VALUE, 12, instrucao));
            }
        } catch (IllegalArgumentException e) {
            throw e;
        }

        return binario.toString();
    }

    public String operandoPraBinario(int i, int tam, int limite, int completar, String instrucao) throws IOException{
        StringBuilder operandoSTR = new StringBuilder();
        StringBuilder numFinal = new StringBuilder();
        String numBin;
        int operandoINT = -1;

        while(instrucao.charAt(i) == ' ') i++;

        for(; i < tam; i++) operandoSTR.append(instrucao.charAt(i));

        try {
            operandoINT = Integer.parseInt(operandoSTR.toString());
        } catch (NumberFormatException e) {
            System.out.println("Erro: " + e.getMessage());
        }

        if((operandoINT < 0) || (operandoINT > limite)) throw new IOException("Erro: Operando de estar entre 0 e "
                + limite + ".");
        numBin = Integer.toBinaryString(operandoINT);

        completar -= numBin.length();

        for(i = 0; i < completar; i++) numFinal.append('0');    //completa com 0 nos bits mais significativos
        numFinal.append(numBin);

        return numFinal.toString();
    }
}