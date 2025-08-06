package Back;

public class Registradores {
    //P1 e L1 são +1 e -1 respectivamente
    private final Registrador PC, AC, SP, IR, TIR, ZERO, P1, L1, AMASK, SMASK, MAR, MBR, MPC, A, B, C, D, E, F;

    public Registradores(){
        PC = new Registrador("PC");
        AC = new Registrador("AC");
        SP = new Registrador("SP");
        IR = new Registrador("IR");
        TIR = new Registrador("TIR");
        ZERO = new Registrador("ZERO");
        P1 = new Registrador("+1");
        L1 = new Registrador("-1");
        AMASK = new Registrador("AMASK");
        SMASK = new Registrador("SMASK");
        MBR = new Registrador("MBR");
        MPC = new Registrador("MPC");
        MAR =  new Registrador("MAR");
        A =  new Registrador("A");
        B =  new Registrador("B");
        C =  new Registrador("C");
        D =  new Registrador("D");
        E =  new Registrador("E");
        F =  new Registrador("F");
    }
}
