package Back;

public class Latch {
    String nome;
    private String valor = "0000000000000000";

    public Latch(String nome) {
        this.nome = nome;
    }

    public void setValor(String valor) { this.valor = valor; }
    public String getValor() { return this.valor; }
}