package Back;

public class ULA {
    private String saida = "0000000000000000";
    private boolean nBit = false, zBit = false;

    public String ativar(String A, String B, String controle){
        short a = Short.parseShort(A);
        short b = Short.parseShort(B);
        short c;

        switch (controle){
            case "00" -> c = this.soma(a, b);
            case "01" -> c = this.andBitABit(a, b);
            case "10" -> c = this.ident(a);
            default -> c = this.Inv(a);
        }

        this.nBit = (c < 0);
        this.zBit = (c == 0);
        return this.saida = String.format("%16s", Integer.toBinaryString(c & 0xFFFF)).replace(' ', '0');
    }

    private short soma(short a, short b){
        return (short)(a+b);
    }

    private short andBitABit(short a, short b){
        return (short) (a & b);
    }

    private short ident(short a){
        return a;
    }

    private short Inv(short a){ return (short) ~a; }

    public String getSaida() {
        return this.saida;
    }

    public boolean isnBit() {
        return this.nBit;
    }

    public boolean iszBit() {
        return this.zBit;
    }

    @Override
    public String toString(){
        return ("Saida: " + saida
                +"\nnBit: " + nBit
                +"\nzBit: " + zBit);
    }
}
