package Back;

import java.io.IOException;

public class Incrementador {
    private short saida = 0;

    public void incrementar(String saidaMBR) throws IOException {
        short saida = ConversaoTipos.bitsToShort(saidaMBR, 16);
        this.saida = (short)(saida + 1);
    }

    public short getSaida() { return this.saida; }
}
