package back.memorias;

import back.comum.Conversao;

import java.io.IOException;

public class LinhaCacheMD {
    private String indice;
    private char bitValidade = '0';
    private char dirtyBit = '0';
    private String tag;
    private String[] bloco = new String[]{"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000"};

    public LinhaCacheMD(int tamTag, short indice) throws IOException {
       String tag = "";
       for(int i = 0; i < tamTag; i++) tag += "0";
       this.tag = tag;
       this.indice = Conversao.shortToString(indice, 10 - tamTag);
    }

    public void substituir(String tag, String[] bloco) {
        this.bitValidade = '1';
        this.dirtyBit = '0';
        this.tag = tag;
        this.bloco = bloco;
    }

    public char getBitValidade() { return bitValidade; }

    public String getIndice() { return indice; }

    public char getDirtyBit() { return dirtyBit; }

    public String getTag() { return tag; }

    public boolean isBitValidade() { return this.bitValidade == '1'; }

    public boolean isDirtyBit() { return this.dirtyBit == '1'; }

    public void setDirtyBit() { this.dirtyBit = '1'; }

    public String getDadoBloco(String offsetBloco) {
        offsetBloco = offsetBloco.substring(10, 12);
        return switch (offsetBloco) {
            case "00" -> this.bloco[0];
            case "01" -> this.bloco[1];
            case "10" -> this.bloco[2];
            default -> this.bloco[3];
        };
    }

    public void substituirPalavraBloco(String endereco, String palavra) {
        String bits = endereco.substring(10, 12);
        int indice = Integer.parseInt(bits, 2);
        this.bloco[indice] = palavra;
    }

    public boolean comparaTag(String tag) {
        return this.tag.equals(tag);
    }

    public String[] getBloco() { return this.bloco; }

    public String getBloco0() { return this.bloco[0]; }

    public String getBloco1() { return this.bloco[1]; }

    public String getBloco2() { return this.bloco[2]; }

    public String getBloco3() { return this.bloco[3]; }

    public String reconstruirEndereco() {
        String end = tag + indice + "00";
        return end;
    }

    public String getLinha() { return bitValidade + "       " + dirtyBit + "       " + tag
            + "     " + bloco[0] + "     " + bloco[1] + "     " + bloco[2] + "     " +  bloco[3];
    }
}
