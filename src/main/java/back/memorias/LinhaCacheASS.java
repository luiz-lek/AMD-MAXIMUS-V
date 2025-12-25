package back.memorias;

import back.comum.Conversao;

import java.io.IOException;

public class LinhaCacheASS {
    private char bitValidade = '0';
    private char dirtyBit = '0';
    private String numBloco = "0000000000";
    private String[] bloco = new String[]{"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000"};
    private String indBloco;

    public LinhaCacheASS(short indice, int tamNumBloco) throws IOException {
        this.indBloco = Conversao.shortToString(indice, 6);
    }

    public void substituir(String numBloco, String[] bloco) throws IOException {
        this.bitValidade = '1';
        this.dirtyBit = '0';
        this.numBloco = numBloco;
        this.bloco = bloco;
    }

    public String getIdentificadorBloco() { return this.numBloco; }

    public char getBitValidade() { return this.bitValidade; }

    public String getEndereco() { return this.indBloco; }

    public boolean isValidade() { return this.bitValidade == '1'; }

    public void setDirtyBit() { this.dirtyBit = '1'; }

    public boolean isDirtyBit() { return this.dirtyBit == '1'; }

    public String getNumBloco() { return this.numBloco; }

    public char getValidade() { return this.bitValidade; }

    public char getDirtyBit() { return this.dirtyBit; }

    public String getBloco0() { return this.bloco[0]; }

    public String getBloco1() { return this.bloco[1]; }

    public String getBloco2() { return this.bloco[2]; }

    public String getBloco3() { return this.bloco[3]; }

    public String[] getBloco() { return this.bloco; }

    public boolean comparaNumBloco(String numBloco) { return this.numBloco.equals(numBloco); }

    public String getEndBloco(String endereco) {
        String offsetBloco = endereco.substring(10, 12);
        return switch (offsetBloco) {
            case "00" -> this.bloco[0];
            case "01" -> this.bloco[1];
            case "10" -> this.bloco[2];
            default -> this.bloco[3];
        };
    }

    public void substituirPalavraBloco(String endereco, String palavra) {
        String offsetBloco = endereco.substring(10, 12);
        int indice = Integer.parseInt(offsetBloco, 2);
        this.bloco[indice] = palavra;
    }

    public String getLinha() { return bitValidade + "       " + dirtyBit + "       " + numBloco
            + "     " + bloco[0] + "     " + bloco[1] + "     " + bloco[2] + "     " +  bloco[3];
    }
}
