package Back;

public class Deslocador {
    private String saida = "0000000000000000", saidaULA = "0000000000000000", controle = "11";

    public void setSaidaULA(String saidaULA) {
        this.saidaULA = saidaULA;
    }

    public void setControle(String controle) {
        this.controle = controle;
    }

    public String ativar(String saidaULA, String controle){
        short c = Short.parseShort(saidaULA);
        if("00".equals(controle)) return this.saida = saidaULA;
        if("01".equals(controle)){
            c = (short)(c << 1);
        } else {
            c = (short) (c >> 1);
        }
        return this.saida = String.format("%16s", Integer.toBinaryString(c & 0xFFFF)).replace(' ', '0');
    }

    public String getSaida() { return saida; }
}