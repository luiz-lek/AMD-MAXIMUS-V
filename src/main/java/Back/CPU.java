package Back;

import java.util.IllegalFormatCodePointException;

public class CPU {
    private MemoriaPrincipal memP;
    private MemoriaControle memC =  new MemoriaControle();
    private Registradores registradores = new Registradores();
    private MAR mar = new MAR("MAR");
    private MBR mbr = new MBR("MBR");
    private Registrador mpc = new Registrador("MPC");
    private Microinstrucao mir = new Microinstrucao("00000000000000000000000000000000");
    private Latch latA = new Latch("A");
    private Latch latB = new Latch("B");
    private AMUX amux = new AMUX();
    private MMUX mmux = new MMUX();
    private ULA ula =  new ULA();
    private Deslocador deslocador  = new Deslocador();
    private Incrementador incrementador = new Incrementador();
    private Decodificador decA = new Decodificador();
    private Decodificador decB = new Decodificador();
    private Decodificador decC = new Decodificador();
    private LogicaMicrosequenciamento logica = new LogicaMicrosequenciamento();

    public void iniciar(MemoriaPrincipal memP){
        this.memP = memP;

        while(!"1111111100000000".equals(this.registradores.registradores[3].getValor())){
            System.out.println(MicroinstrucaoMap.getDescricao(this.mpc.getValor()));
            this.subciclo1();
            this.subciclo2();
            this.subciclo3();
            this.subciclo4();
            for(int i = 0; i < 16; i++){
                System.out.println(this.registradores.registradores[i].getNome() + ": " +
                        this.registradores.registradores[i].getValor());
            }
            System.out.println("MAR: " + this.mar.getValor());
            System.out.println("MBR: " + this.mbr.getValor());
            System.out.println("MPC: " + this.mpc.getValor());
            System.out.println("\n");
        }
    }

    public void subciclo1(){
        this.mir.setMic(memC.getPos(this.mpc.getValor()).getMic()); //passa a instrução em mem[mpc] para o mir
    }

    public void subciclo2(){
        this.amux.setControle(this.mir.getAMUX());
        this.logica.setCOND(this.mir.getCOND());
        this.ula.setControle(this.mir.getALU());
        this.deslocador.setControle(this.mir.getSH());
        this.mbr.setAtivado(this.mir.getMBR());
        this.mar.setAtivado(this.mir.getMAR());
        this.decC.setENC(this.mir.getENC());
        this.decC.setEntrada(this.mir.getC());
        this.decB.setEntrada(this.mir.getB());
        this.decA.setEntrada(this.mir.getA());
        this.mmux.setADDR(this.mir.getADDR());
        this.latB.setValor(registradores.getValor(decB.decodificar()));
        incrementador.incrementar(this.mpc.getValor());
        this.latA.setValor(registradores.getValor(decA.decodificar()));
    }

    public void subciclo3(){
        this.amux.ativar(this.mbr.getValor(), this.latA.getValor());
        ula.ativar(this.amux.getSaida(), this.latB.getValor());
        this.deslocador.ativar(this.ula.getSaida());
        if(mar.isAtivado()) mar.setValor(latB.getValor());
    }

    public void subciclo4(){
        if(decC.isENC()) this.registradores.setValor(decC.decodificar(), deslocador.getSaida());
        if(this.mbr.isAtivado()) mbr.setValor(this.deslocador.getSaida());
        this.mbr.setRD(this.mir.getRD());
        this.mbr.setWR(this.mir.getWR());
        this.logica.setNBitZBit(ula.isNBit(), ula.isZBit());
        this.logica.gerarSaida();
        mmux.setMPCIncrementado(incrementador.getSaida());
        this.mmux.setControle(logica.isSaida());
        this.mmux.ativar();
        this.mpc.setValor(this.mmux.getSaida());
        if(this.mbr.isRD()) this.mbr.setValor(this.memP.ler(this.mar.getValor()));
        if(this.mbr.isWR()) this.memP.escrever(this.mar.getValor(), this.mbr.getValor());
    }
}
