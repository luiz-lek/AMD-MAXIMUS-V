package back.cpu;

import back.comum.Conversao;

import java.io.IOException;

public class MMUX {
    private String saida = "00000000000000000000000000000000";
    short MPCIncrementado = 0, ADDR = 0;
    private boolean controle = false;

    public void setMPCIncrementado(short MPCIncrementado) {
        this.MPCIncrementado = MPCIncrementado;
    }

    public void setADDR(String ADDR) {
        this.ADDR = Short.parseShort(ADDR, 2);
    }

    public void setControle(boolean controle) {
        this.controle = controle;
    }

    public void ativar() throws IOException {
        short saidaSH = (this.controle) ? this.ADDR : this.MPCIncrementado;
        this.saida = Conversao.shortToString(saidaSH, 16);
    }

    public String getSaida() { return saida; }

    @Override
    public String toString() {
        return ("MPCIncrementado: " + this.MPCIncrementado
                + "\nADDR: " + this.ADDR
                + "\nControle: " + this.controle
                + "\nSaida: " + this.saida);
    }
}
