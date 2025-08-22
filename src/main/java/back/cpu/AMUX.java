package back.cpu;

public class AMUX {
    private String saida = "0000000000000000";
    String controle = "0";

    public void setControle(String controle) {
        this.controle = controle;
    }

    public void ativar(String regMBR, String latA) {
        this.saida = "0".equals(controle) ? latA : regMBR;
    }

    public String getSaida(){ return this.saida; }
}
