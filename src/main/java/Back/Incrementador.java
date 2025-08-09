package Back;

public class Incrementador {
    private short saida = 0;

    public void incrementar(String saidaMBR) {
        short saida = Short.parseShort(saidaMBR, 2);
        this.saida = (short)(saida + 1);
    }

    public short getSaida() { return this.saida; }
}
