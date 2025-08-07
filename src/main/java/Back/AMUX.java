package Back;

public class AMUX {
    private String saida = "0000000000000000";
    String regMBR = "0000000000000000", regA = "0000000000000000", controle = "0";

    public void setRegMBR(String regMBR) {
        this.regMBR = regMBR;
    }

    public void setRegA(String regA) {
        this.regA = regA;
    }

    public void setControle(String controle) {
        this.controle = controle;
    }

    public String ativar(){
        return this.saida = "0".equals(controle) ? regMBR : regA;
    }
}
