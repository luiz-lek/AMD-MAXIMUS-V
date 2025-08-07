package Back;

public class CPU {
    MemoriaControle memC =  new MemoriaControle();
    Registradores registradores = new Registradores();
    MAR mar = new MAR("MAR");
    MBR mbr = new MBR("MBR");
    Latch LatA = new Latch("A");
    Latch LatB = new Latch("B");
    AMUX amux = new AMUX();
    MMUX mmux = new MMUX();
    ULA ula =  new ULA();
    Deslocador deslocador  = new Deslocador();
    Incrementador incrementador = new Incrementador();
    Decodificador decA = new Decodificador();
    Decodificador decB = new Decodificador();
    Decodificador decC = new Decodificador();
    LogicaMicrosequenciamento logica = new LogicaMicrosequenciamento();

    public void iniciar(MemoriaPrincipal memP){
        while(!"0000000000000000".equals(mbr.getValor())){
            this.subciclo1();
        }
    }

    public void subciclo1(){
        this.registradores.MIR.setMic(memC.getPos(registradores.MPC.getValor()).getMic()); //passa a instrução em mem[mpc] para o mir
        Microinstrucao mic = registradores.MIR;
        this.amux.setControle(mic.getMic());
        this.logica.setCOND(mic.getCOND());
        this.ula.setControle(mic.getALU());
        this.deslocador.setControle(mic.getSH());
        mbr.setAtivado(mic.getMBR());
        mar.setAtivado(mic.getMAR());
        mbr.setRD(mic.getRD());
        mbr.setWR(mic.getWR());
        decC.setAtivado(mic.getENC());
        decC.setEntrada(mic.getC());
        decB.setEntrada(mic.getB());
        LatB.setValor(registradores.getValor(decB.decodificar()));
        decA.setEntrada(mic.getA());
        LatA.setValor(registradores.getValor(decA.decodificar()));
        mmux.setADDR(mic.getADDR());
    }

    public void subciclo2(){

    }
}
