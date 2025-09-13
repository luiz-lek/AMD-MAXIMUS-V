package back.cpu;

public class Latch {
    String nome;
    private String valor = "0000000000000000";

    public Latch(String nome) {
        this.nome = nome;
    }

    public void setValor(String valor) throws Exception {
        if(valor.length() != 16) throw new Exception("Valor precisa ter 16 bits.\n" +
                                                       "Queatidade recebida: " + valor);
        this.valor = valor;
    }

    public String getValor() { return this.valor; }
}