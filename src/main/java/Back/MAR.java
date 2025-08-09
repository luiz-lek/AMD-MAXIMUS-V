package Back;

public class MAR extends Registrador {
    private boolean ativado = false;

    public MAR(String nome) {
        super(nome);
    }

    public void setAtivado(String ativado) {
        this.ativado = !"0".equals(ativado);
    }

    public boolean isAtivado() { return ativado; }
}
