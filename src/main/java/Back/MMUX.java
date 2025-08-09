package Back;

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

    public void ativar(){
        short saidaSH = (this.controle) ? this.ADDR : this.MPCIncrementado;
        this.saida = String.format("%16s", Integer.toBinaryString(saidaSH & 0xFFFF)).replace(' ', '0');
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
