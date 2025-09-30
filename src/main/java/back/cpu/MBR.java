package back.cpu;

public class MBR extends Registrador {
    private boolean RD = false, WR = false, ativado = false;

    public MBR(String nome) {
        super(nome);
    }

    public void setRD(String RD) {
        this.RD = !"0".equals(RD);
    }

    public void setWR(String WR) {
        this.WR = !"0".equals(WR);
    }

    public void setAtivado(String ativado) {
        this.ativado = !"0".equals(ativado);
    }

    public boolean isRD() {
        return this.RD;
    }

    public boolean isWR() {
        return this.WR;
    }

    public boolean isAtivado() {
        return this.ativado;
    }
}
