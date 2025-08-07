package Back;

public class MMUX {
    private short saida = 0, MPCIncrementado = 0, ADDR = 0;
    private boolean controle = false;

    public void setMPCIncrementado(short MPCIncrementado) {
        this.MPCIncrementado = MPCIncrementado;
    }

    public void setADDR(String ADDR) {
        this.ADDR = Short.parseShort(ADDR);
    }

    public void setControle(boolean controle) {
        this.controle = controle;
    }

    public short ativar(short MPCIncrementado, short ADDR, boolean controle){
        return this.saida = (this.controle) ? this.MPCIncrementado : this.ADDR;
    }
}
