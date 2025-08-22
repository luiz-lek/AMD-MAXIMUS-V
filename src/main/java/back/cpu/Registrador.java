package back.cpu;

public class Registrador {
    private String valor = "0000000000000000";
    private String nome;

    public Registrador(String nome){
        this.nome = nome;
    }

    public String getValor() { return this.valor; }

    public void setValor(String valor) { this.valor = valor; }

    public String getNome() {
        return nome;
    }
}
