package back.cpu;

import back.comum.Microinstrucao;
import back.memorias.Cache;
import back.memorias.MemoriaPrincipal;

public class CPU {
    private Cache cache;
    private final MemoriaControle memC =  new MemoriaControle();
    public Registradores registradores = new Registradores();
    private MAR mar = new MAR("MAR");
    private MBR mbr = new MBR("MBR");
    private Registrador mpc = new Registrador("MPC");
    public Microinstrucao mir = new Microinstrucao("00000000000000000000000000000000");
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

    private boolean rdIniciado = false, wrIniciado = false;
    private int ultSubcicloExe = 0;

    public void setCache(Cache cache) { this.cache = cache; }

    public void executarCiclo() throws Exception {
        this.subciclo1();
        this.subciclo2();
        this.subciclo3();
        this.subciclo4();
    }

    public void executarSubciclo() throws Exception {
         switch(this.ultSubcicloExe) {
            case(1) -> this.subciclo2();
            case(2) -> this.subciclo3();
            case(3) -> this.subciclo4();
            default -> this.subciclo1();
        };
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

        this.ultSubcicloExe = 1;
    }

    public void subciclo2() throws Exception { //Manda os sinais de controle do mir para todos os componentes.
        this.latB.setValor(this.registradores.getValor(decB.decodificar()));
        this.incrementador.incrementar(this.mpc.getValor());
        this.latA.setValor(this.registradores.getValor(decA.decodificar()));

        this.ultSubcicloExe = 2;
    }

    public void subciclo3() throws Exception {
        if(this.rdIniciado) { //Verifica se há uma leitura iniciada no ciclo anterior, caso tenha,
            this.cache.ler(this.getValorMar(), this.mbr);
            if(this.mbr.isReady()) this.rdIniciado = false;
        } else if(this.wrIniciado) { //Mesmo que o bloco a cima, mas para escrita.
            this.cache.escrever(this.getValorMar(), this.mbr);
            if(this.mbr.isReady()) this.wrIniciado = false;
        }

        this.amux.ativar(this.mbr.getValor(), this.latA.getValor());
        ula.ativar(this.amux.getSaida(), this.latB.getValor()); //No subciclo 3, após as entradas da ula estarem definidas,
        this.deslocador.ativar(this.ula.getSaida());            //a ula realiza o seu cálculo.
        if(mar.isAtivado()) mar.setValor(latB.getValor());

        this.ultSubcicloExe = 3;
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

        if(this.mbr.isRD()) {
            this.rdIniciado = true;
        }

        if(this.mbr.isWR()) {
            this.wrIniciado = true;
        }

        this.ultSubcicloExe = 4;
    }

    public String getValorMbr() { return this.mbr.getValor(); }

    public String getValorMar() { return this.mar.getValor(); }

    public String getValorMPC() { return this.mpc.getValor(); }

    public String getValorMpc() { return mpc.getValor(); }

    public String getValorMir() { return this.mir.getMic(); }

    public String getValorUla() { return this.ula.getSaida(); }

    public String getValorAmux() { return this.amux.getSaida(); }

    public int getUltSubcicloExe() { return this.ultSubcicloExe; }

    public String getValorRegistrador(int pos) throws Exception {
        if((pos < 0) || (15 < pos)) throw new Exception("Posição inválida.");
        return this.registradores.registradores[pos].getValor();
    }
}