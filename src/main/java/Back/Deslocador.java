package Back;

public class Deslocador {
    private short saida = 0;

    public short ativar(short saidaULA, String controle){
        if("00".equals(controle)) return this.saida = saidaULA;
        if("01".equals(controle)) return this.saida = (short)(saidaULA << 1);
        return this.saida = (short)(saidaULA >> 1);
    }
}