package Back;

public class ULA {
    private short saida;
    private boolean nBit, zBit;

    public ULA(){
        saida = 0;
        this.nBit = this.zBit = false;
    }

    public enum CONTROLE{
        Soma,
        And,
        Ident,
        Inv
    }

    public short ativar(short a, short b, CONTROLE controle){
        switch (controle){
            case Soma -> this.soma(a, b);
            case And -> this.andBitABit(a, b);
            case Ident -> this.ident(a);
            case Inv -> this.Inv(a);
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
