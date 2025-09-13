package back.cpu;

public class Deslocador {
    private String saida = "0000000000000000";
    private String controle = "11";

    public void setControle(String controle) {
        this.controle = controle;
    }

    public void ativar(String saidaULA) throws Exception {
        if (saidaULA == null || saidaULA.length() != 16 || !saidaULA.matches("[01]+")) {
            throw new Exception("Entrada ser string binária de 16 bits");
        }

        if ("01".equals(this.controle)) { // Desloca um bit pra esquerda, e põe 0 na direita
            this.saida = saidaULA.substring(1) + "0";
        } else if (!"00".equals(this.controle)) { // Desloca para a direita
            char sinal = saidaULA.charAt(0);
            this.saida = sinal + saidaULA.substring(0, 15);
        } else { // Não desloca
            this.saida = saidaULA;
        }
    }

    public String getSaida() {
        return this.saida;
    }
}