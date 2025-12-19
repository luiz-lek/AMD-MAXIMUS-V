package back.memorias;

public class LinhaCacheMD {
    private char bitValidade = '0';
    private char dirtyBit = '0';
    private String tag = "00000";
    private String[] bloco = new String[]{"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000"};

    public void substituirLinha(String tag, String[] bloco) {
        this.bitValidade = '1';
        this.dirtyBit = '0';
        this.tag = tag;
        this.bloco = bloco;
    }

    public boolean isBitValidade() { return this.bitValidade == '1'; }

    private boolean isDirtyBit() { return this.dirtyBit == '1'; }

    public String getEndBloco(String offsetBloco) {
        return switch (offsetBloco) {
            case "00" -> this.bloco[0];
            case "01" -> this.bloco[1];
            case "10" -> this.bloco[2];
            default -> this.bloco[3];
        };
    }

    public String getLinha() { return bitValidade + dirtyBit + tag + bloco[0] + bloco[1] + bloco[2] + bloco[3]; }
}
