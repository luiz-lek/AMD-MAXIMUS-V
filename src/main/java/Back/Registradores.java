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

    public Registrador MPC = new Registrador("MPC");
    public Microinstrucao MIR = new Microinstrucao("00000000000000000000000000000000");

    public String getValor(String sinais){
        int i = 0;
        for(;i < 16 && sinais.charAt(i) != '1'; i++);
        if(i == 16) return this.registradores[0].getValor();
        return this.registradores[i].getValor();
    }
}