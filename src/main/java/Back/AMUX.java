package Back;

public class AMUX {
    private short saida = 0;

    public short ativar(short regMBR, short regA, String controle){
        return this.saida = "0".equals(controle) ? regMBR : regA;
    }
}
