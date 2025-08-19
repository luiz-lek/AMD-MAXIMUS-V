package Back;

import java.io.IOException;

public class Latch {
    String nome;
    private String valor = "0000000000000000";

    public Latch(String nome) {
        this.nome = nome;
    }

    public void setValor(String valor) throws IOException {
        if(valor.length() != 16) throw new IOException("Valor precisa ter 16 bits.\n" +
                                                       "Quentidade recebida: " + valor);
        this.valor = valor;
    }

    public String getValor() { return this.valor; }
}