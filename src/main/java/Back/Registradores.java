package Back;

public class Registradores {
    private Registrador[] registradores = {
            new Registrador("PC"),
            new Registrador("AC"),
            new Registrador("SP"),
            new Registrador("IR"),
            new Registrador("TIR"),
            new Registrador("0"),
            new Registrador("+1"),
            new Registrador("-1"),
            new Registrador("AMASK"),
            new Registrador("SMASK"),
            new Registrador("A"),
            new Registrador("B"),
            new Registrador("C"),
            new Registrador("D"),
            new Registrador("E"),
            new Registrador("F")
    };
    private Registrador MAR = new Registrador("MAR");
    private Registrador MBR = new Registrador("MBR");
    private Registrador MPC = new Registrador("MPC");
    private Registrador MIR = new Registrador("MIR");

    public Registradores(){
        this.MIR.setValor("00000000000000000000000000000000");
    }

    public String getValorSTR(String sinais){
        int i = 0;
        for(;i < 16 && sinais.charAt(i) != '1'; i++);
        if(i == 16) return this.registradores[0].getValor();
        return this.registradores[i].getValor();
    }
}