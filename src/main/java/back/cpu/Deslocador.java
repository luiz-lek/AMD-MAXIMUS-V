package back.cpu;

public class Deslocador {
    private String saida = "0000000000000000";
    private String controle = "11";

    public void setControle(String controle) {
        this.controle = controle;
    }

    public void ativar(String saidaULA) {
        if (saidaULA == null || saidaULA.length() != 16 || !saidaULA.matches("[01]+")) {
            throw new IllegalArgumentException("Entrada deve ser string binária de 16 bits");
        }

        if ("01".equals(this.controle)) {
            this.saida = saidaULA.substring(1) + "0";
        } else if (!"00".equals(this.controle)) {
            char sinal = saidaULA.charAt(0);
            this.saida = sinal + saidaULA.substring(0, 15);
        } else {
            this.saida = saidaULA;
        }
    }

    public String getSaida() {
        return this.saida;
    }
}