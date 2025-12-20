package back.cpu;

import back.comum.Conversao;

import java.io.IOException;

public class ULA {
    private String saida = "0000000000000000";
    private boolean nBit = false, zBit = false;
    private String controle = "00"; //"00": soma, "01": AND, "10": ident, "11": inv

    public void setControle(String controle) {
        this.controle = controle;
    }

    public void ativar(String A, String B) throws IOException {
        short a = (short)Integer.parseInt(A, 2);
        short b = (short)Integer.parseInt(B, 2);
        short c;

        switch (this.controle) {
            case "00" -> c = this.soma(a, b);
            case "01" -> c = this.andBitABit(a, b);
            case "11" -> c = this.inv(a);
            default -> c = this.ident(a);
        }

        this.zBit = (c == 0);
        this.nBit = (c < 0);

        this.saida = Conversao.shortToString(c, 16);
    }

    private short soma(short a, short b) {
        return (short)(a + b);
    }

    private short andBitABit(short a, short b) {
        return (short)(a & b);
    }

    private short ident(short a) {
        return a;
    }

    private short inv(short a) {
        return (short)~a;
    }

    public String getSaida() {
        return this.saida;
    }

    public boolean isNBit() {
        return this.nBit;
    }

    public boolean isZBit() {
        return this.zBit;
    }
}