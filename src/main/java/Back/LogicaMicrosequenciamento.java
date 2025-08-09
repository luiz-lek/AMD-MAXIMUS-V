package Back;

public class LogicaMicrosequenciamento {
    private boolean saida = false, nBit = false, zBit = false;
    String COND = "00";

    public void setCOND(String COND) { this.COND = COND; }

    public void setNBitZBit(boolean nBit, boolean zBit){
        this.nBit = nBit;
        this.zBit = zBit;
    }

    public void gerarSaida(){
        switch (this.COND) {
            case "00" -> this.saida = false;
            case "11" -> this.saida = true;
            case "01" -> this.saida = this.nBit;
            case null, default -> this.saida = this.zBit;
        }
    }

    public boolean isSaida() { return saida; }
}
