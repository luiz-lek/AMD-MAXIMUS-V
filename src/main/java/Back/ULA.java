package Back;

public class ULA {
    private String saida = "0000000000000000";
    private boolean nBit = false, zBit = false;
    private String controle = "00"; // "00" = soma, "01" = AND, "10" = ident, "11" = inv

    public void setControle(String controle) {
        this.controle = controle;
    }

    public void ativar(String A, String B) {
        short a = (short) Integer.parseInt(A, 2);
        short b = (short) Integer.parseInt(B, 2);
        short c;

        switch (this.controle) {
            case "00" -> c = soma(a, b);
            case "01" -> c = andBitABit(a, b);
            case "10" -> c = ident(a);
            default   -> c = inv(a);
        }

        this.zBit = (c == 0);

        this.saida = String.format("%16s", Integer.toBinaryString(c & 0xFFFF)).replace(' ', '0');

        nBit = '1' == this.saida.charAt(0);
    }

    private short soma(short a, short b) {
        return (short) (a + b);
    }

    private short andBitABit(short a, short b) {
        return (short) (a & b);
    }

    private short ident(short a) {
        return a;
    }

    private short inv(short a) {
        return (short) ~a;
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

    @Override
    public String toString() {
        return ("Saida: " + saida
                + "\nnBit: " + nBit
                + "\nzBit: " + zBit
                + "\nControle: " + controle);
    }
}
