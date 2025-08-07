package Back;

public class LogicaMicrosequenciamento {
    private boolean saida = false;

    public boolean ativar(boolean nBit, boolean zBit, String ADDR){
        if("00".equals(ADDR)) return this.saida = false;
        if("11".equals(ADDR)) return this.saida = true;
        if("01".equals(ADDR)){
            if(nBit) return this.saida = true;
            return this.saida = false;
        }
        if(zBit) return this.saida = true;
        return this.saida = false;
    }
}
