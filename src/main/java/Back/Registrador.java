package Back;

public class Registrador {
    private short valor;
    private String nome;

    public Registrador(String nome, short valor){
        this.nome = nome;
    }

    public short getValor() {
        return valor;
    }

    public void setValor(short valor) {
        this.valor = valor;
    }

    public String getNome() {
        return nome;
    }
}
