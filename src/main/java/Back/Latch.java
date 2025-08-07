package Back;

public class Latch {
    String nome;
    short saida = 0;

    public Latch(String nome) {
        this.nome = nome;
    }

    public void setValor(short valor) { this.saida = valor; }
    public short getValor() { return this.saida; }
}
