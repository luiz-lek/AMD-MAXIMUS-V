package Back;

public class Registradores {
    private Registrador[] registradores;
    private Registrador MAR, MBR, MPC;

    public Registradores(){
        registradores = new Registrador[]{new Registrador("PC", (short)0),
                                          new Registrador("AC", (short)0),
                                          new Registrador("SP", (short)0),
                                          new Registrador("IR", (short)0),
                                          new Registrador("TIR", (short)0),
                                          new Registrador("0", (short)0),
                                          new Registrador("+1", (short)1),
                                          new Registrador("-1", (short)-1),
                                          new Registrador("AMASK", (short)0),
                                          new Registrador("SMASK", (short)0),
                                          new Registrador("A", (short)0),
                                          new Registrador("B", (short)0),
                                          new Registrador("C", (short)0),
                                          new Registrador("D", (short)0),
                                          new Registrador("E", (short)0),
                                          new Registrador("F", (short)0)
                                        };

        this.MAR = new Registrador("MAR", (short)0);
        this.MBR = new Registrador("MBR", (short)0);
        this.MPC = new Registrador("MPC", (short)0);
    }

    public short getValor(String sinais){
        int i = 0;
        for(; sinais.charAt(i) != 1 && i < 16; i++);
        if(i == 16) return this.registradores[0].getValor();
        return this.registradores[i].getValor();
    }

    public short getValorMAR(){
        return this.MAR.getValor();
    }

    public short getValorMBR(){
        return this.MBR.getValor();
    }
    public short getValorMPC(){
        return this.MPC.getValor();
    }
}