package back.cpu;

import back.comum.Conversao;

import java.io.IOException;

public class Incrementador {
    private short saida = 0;

    public void incrementar(String saidaMBR) throws Exception {
        short saida = Conversao.bitsToShort(saidaMBR, 16);
        this.saida = (short)(saida + 1);
    }

    public short getSaida() { return this.saida; }
}
