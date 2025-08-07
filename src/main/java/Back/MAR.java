package Back;

public class MAR extends Registrador {
    private String ativado = "0";

    public MAR(String nome) {
        super(nome);
    }

    public void setAtivado(String ativado) {
        this.ativado = ativado;
    }
}
