package back.cpu;

public class LogicaMicrosequenciamento {
    private boolean saida = false, nBit = false, zBit = false, ready = false;
    String COND = "000";

    public void setCOND(String COND) { this.COND = COND; }

    public void setNBitZBit(boolean nBit, boolean zBit, boolean ready) {
        this.nBit = nBit;
        this.zBit = zBit;
        this.ready = ready;
    }

    public void gerarSaida() throws Exception {
        switch (this.COND) {
            case "000" -> this.saida = false;
            case "011" -> this.saida = true;
            case "001" -> this.saida = this.nBit;
            case "010" -> this.saida = this.zBit;
            case "100" -> this.saida = !this.ready;
            case null, default -> throw new Exception("Controle de Logica inválido.");
        }
    }

    public boolean isSaida() { return saida; }
}
