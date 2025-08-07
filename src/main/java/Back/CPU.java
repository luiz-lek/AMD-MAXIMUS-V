package Back;

public class CPU {
    Registradores registradores = new Registradores();
    Latch A = new Latch("A");
    Latch B = new Latch("B");
    AMUX amux = new AMUX();
    ULA ula =  new ULA();
    Deslocador deslocador  = new Deslocador();

}
