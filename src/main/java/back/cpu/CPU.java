package back.cpu;

import back.comum.MAX;
import back.comum.Microinstrucao;

public class CPU {
    private MemoriaPrincipal memP;
    private final MemoriaControle memC =  new MemoriaControle();
    public Registradores registradores = new Registradores();
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
    private boolean rdIniciado = false, wrIniciado = false; //Simulam o atraso de 2 ciclos para leitura e escrita pela cpu na memória.

    private int atraso = 0;
    public void setMemoriaPrincipal(MemoriaPrincipal memoria){
        this.memP = memoria;
    }

    public void executarCiclo() throws Exception {
        this.subciclo1();
        this.subciclo2();
        this.subciclo3();
        this.subciclo4();
    }

    public void subciclo1(){
        this.mir.setMic(memC.getPos(this.mpc.getValor()).getMic()); //Passa a instrução em mem[mpc] para o mir
                                                                    //e estabiliza suas saídas.
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
    }

    public void subciclo2() throws Exception { //Manda os sinais de controle do mir para todos os componentes.
        this.latB.setValor(this.registradores.getValor(decB.decodificar()));
        this.incrementador.incrementar(this.mpc.getValor());
        this.latA.setValor(this.registradores.getValor(decA.decodificar()));
    }

    public void subciclo3() throws Exception {
        if(this.rdIniciado) { //Verifica se há uma leitura iniciada no ciclo anterior, caso tenha,
            if(atraso >= MAX.ATRASOMEMORIA) {
                this.mbr.setValor(this.memP.ler(this.mar.getValor()));// o valor lido é passado para o MBR.
                this.mbr.setReady("1");
                this.rdIniciado = false;
                atraso = 0;
            } else {
                atraso++;
            }
        } else if (this.wrIniciado) { //Mesmo que o bloco a cima, mas para escrita.
            if(atraso >= MAX.ATRASOMEMORIA) {
                this.memP.escrever(this.mar.getValor(), this.mbr.getValor());
                this.mbr.setReady("1");
                this.wrIniciado = false;
                atraso = 0;
            } else {
                atraso++;
            }
        } else {
            this.mbr.setReady("0");
        }

        this.amux.ativar(this.mbr.getValor(), this.latA.getValor());
        ula.ativar(this.amux.getSaida(), this.latB.getValor()); //No subciclo 3, após as entradas da ula estarem definidas,
        this.deslocador.ativar(this.ula.getSaida());            //a ula realiza o seu cálculo.
        if(mar.isAtivado()) mar.setValor(latB.getValor());
    }

    public void subciclo4() throws Exception {
        if(decC.isENC()) this.registradores.setValor(decC.decodificar(), deslocador.getSaida());
        if(this.mbr.isAtivado()) mbr.setValor(this.deslocador.getSaida()); //Em caso de MBR acionado, a saída do
                                                                           //deslocador e passada para MBR.
        this.mbr.setRD(this.mir.getRD());  //Os campos rd e wr são como laths, segundo a descriçãoo do livro do Tanenbaum.
        this.mbr.setWR(this.mir.getWR());  //Eles só são passadas para o mbr no subciclo 4.
        this.logica.setNBitZBit(ula.isNBit(), ula.isZBit(), mbr.isReady());
        this.logica.gerarSaida();                           //Defini as últimas entradas da lógica, assim, decide para onde
        mmux.setMPCIncrementado(incrementador.getSaida());  //o microprograma vai seguir no próximo ciclo.
        this.mmux.setControle(logica.isSaida());
        this.mmux.ativar();
        this.mpc.setValor(this.mmux.getSaida());
        if(this.mbr.isRD()) this.rdIniciado = true;
        if(this.mbr.isWR()) this.wrIniciado = true;
    }

    public String getValorMbr() {
        return this.mbr.getValor();
    }

    public String getValorMar() { return this.mar.getValor(); }

    public String getValorMPC() { return this.mpc.getValor(); }

    public String getValorMpc() { return mpc.getValor(); }

    public String getValorMir() { return this.mir.getMic(); }

    public String getValorRegistrador(int pos) throws Exception {
        if((pos < 0) || (15 < pos)) throw new Exception("Posição inválida.");
        return this.registradores.registradores[pos].getValor();
    }
}