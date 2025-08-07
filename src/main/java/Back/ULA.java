package Back;

public class ULA {
    private short saida;
    private boolean nBit, zBit;

    public ULA(){
        saida = 0;
        this.nBit = this.zBit = false;
    }

    public short ativar(short a, short b, String controle){
        switch (controle){
            case "00" -> this.soma(a, b);
            case "01" -> this.andBitABit(a, b);
            case "10" -> this.ident(a);
            case "11" -> this.Inv(a);
        }

        this.nBit = (this.saida < 0);
        this.zBit = (this.saida == 0);
        return this.saida;
    }

    private void soma(short a, short b){
        this.saida = (short)(a+b);
    }

    private void andBitABit(short a, short b){
        this.saida = (short) (a & b);
    }

    private void ident(short a){
        this.saida = a;
    }

    private void Inv(short a){
        this.saida = (short) ~a;
    }

    public short getSaida() {
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
