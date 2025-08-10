package Back;

public class MAR extends Registrador {
    private boolean ativado = false;

    public MAR(String nome) {
        super(nome);
        super.setValor("000000000000");
    }

    public void setAtivado(String ativado) {
        this.ativado = !"0".equals(ativado);
    }

    public boolean isAtivado() { return ativado; }

    @Override
    public void setValor(String valor) throws IllegalArgumentException {
        super.setValor(valor.substring(4));
        if(this.getValor().length() != 12) throw new IllegalArgumentException("MAR precisa receber 12 bits.");
    }
}
