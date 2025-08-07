package Back;

public class Incrementador {
    public short incrementar(String saidaMBR) {
        short saida = Short.parseShort(saidaMBR);
        return (short)(saida + 1);
    }
}
