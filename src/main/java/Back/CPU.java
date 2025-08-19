package Back;

import java.io.IOException;

public class CPU {
    private MemoriaPrincipal memP;
    private final MemoriaControle memC =  new MemoriaControle();
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
    private boolean rdIniciado = false, wrIniciado = false; //simulam o atraso de 2 ciclos para leitura e escrita na memória pela cpu.

    public void setMemoriaPrincipal(MemoriaPrincipal memoria){
        this.memP = memoria;
    }

    public void executarCiclo() throws IOException {
        this.subciclo1();
        this.subciclo2();
        this.subciclo3();
        this.subciclo4();
    }

    public void subciclo1(){
        this.mir.setMic(memC.getPos(this.mpc.getValor()).getMic()); //passa a instrução em mem[mpc] para o mir e estabiliza suas saídas.
    }

    public void subciclo2() throws IOException { //manda os sinais de controle do mir para todos os componentes.
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

    public void subciclo3() {
        if(this.rdIniciado) {// Verifica se há uma leitura iniciada no ciclo anterior, caso tenha,
            this.mbr.setValor(this.memP.ler(this.mar.getValor()));// o valor é passado para o mbr
            this.mbr.setRD("0");
            this.rdIniciado = false;
        }

        if(this.wrIniciado) { //mesmo que o bloco a cima, mas para escrita
            this.memP.escrever(this.mar.getValor(), this.mbr.getValor());
            this.mbr.setWR("0");
            this.wrIniciado = false;
        }

        this.amux.ativar(this.mbr.getValor(), this.latA.getValor());
        ula.ativar(this.amux.getSaida(), this.latB.getValor());
        this.deslocador.ativar(this.ula.getSaida());
        if(mar.isAtivado()) mar.setValor(latB.getValor());
    }

    public void subciclo4() throws IOException {
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
        if(this.mbr.isRD()) this.rdIniciado = true;
        if(this.mbr.isWR()) this.wrIniciado = true;
    }

    public String getMbrValor() {
        return this.mbr.getValor();
    }

    public String getMarValorHexadecimal() {
        return ConversaoTipos.binaryToHexadecimal(this.mar.getValor());
    }

    public String getMpcValor() throws IOException {
        return Integer.toString(ConversaoTipos.bitsToInt(this.mpc.getValor(), 16));
    }

    public String getValorRegistrador(int pos) throws IllegalAccessError{
        if((pos < 0) || (15 < pos)) throw new IllegalAccessError("Posição inválida.");
        return this.registradores.registradores[pos].getValor();
    }

    public String getMpc() {
        return mpc.getValor();
    }

    public String getMir() { return this.mir.getMic(); }
}
