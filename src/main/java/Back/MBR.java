package Back;

public class MBR extends Registrador{
    private String RD = "0", WR = "0", ativado = "0";

    public MBR(String nome) {
        super(nome);
    }

    public void setRD(String RD) {
        this.RD = RD;
    }

    public void setWR(String WR) {
        this.WR = WR;
    }

    public void setAtivado(String ativado) {
        this.ativado = ativado;
    }
}
