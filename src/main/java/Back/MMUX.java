package Back;

public class MMUX {
    private short saida = 0;

    public short ativar(short MPCIncrementado, short ADDR, boolean controle){
        return this.saida = (controle) ? MPCIncrementado : ADDR;
    }
}
