package Back;

public class LogicaMicrosequenciamento {
    private boolean saida = false, nBit = false, zBit = false;
    String COND = "00";

    public void setCOND(String COND) { this.COND = COND; }

    public void setNBitZBit(boolean nBit, boolean zBit){
        this.nBit = nBit;
        this.zBit = zBit;
    }

    public boolean gerarSaida(){
        if("00".equals(this.COND)) return this.saida = false;
        if("11".equals(this.COND)) return this.saida = true;
        if("01".equals(this.COND)){
            if(this.nBit) return this.saida = true;
            return this.saida = false;
        }
        if(this.zBit) return this.saida = true;
        return this.saida = false;
    }
}
