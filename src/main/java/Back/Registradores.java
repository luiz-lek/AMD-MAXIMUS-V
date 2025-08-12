package Back;

public class Registradores {
    public Registrador[] registradores = {
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

    public Registradores(){
        this.registradores[2].setValor("0001000000000000");
        this.registradores[6].setValor("0000000000000001");
        this.registradores[7].setValor("1111111111111111");
        this.registradores[8].setValor("0000111111111111");
        this.registradores[9].setValor("0000000011111111");
    }

    private int determinarReg(String sinais){
        int i = 0;
        for(;i < 16 && sinais.charAt(i) != '1'; i++);
        if(i == 16) return 0;
        return i;
    }

    public String getValor(String sinais){
        int i = this.determinarReg(sinais);
        return this.registradores[i].getValor();
    }

    public void setValor(String sinais, String valor){
        int i = determinarReg(sinais);
        registradores[i].setValor(valor);
    }

    public String getValoresMemoriaRascunho() {
        StringBuilder s = new StringBuilder();
        String nomeReg;

        for(int i = 0; i < 15; i++) {
            nomeReg = this.registradores[i].getNome();
            for(int j = nomeReg.length(); j < 5; j++) s.append(" ");
            s.append(this.registradores[i].getNome() + ": ");
            s.append(this.registradores[i].getValor() + "\n");
        }

        s.append("    " + this.registradores[15].getNome() + ": ");
        s.append(this.registradores[15].getValor());

        return s.toString();
    }
}