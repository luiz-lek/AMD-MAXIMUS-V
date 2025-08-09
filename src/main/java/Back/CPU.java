package Back;

public class CPU {
    MemoriaPrincipal memP;
    MemoriaControle memC =  new MemoriaControle();
    Registradores registradores = new Registradores();
    MAR mar = new MAR("MAR");
    MBR mbr = new MBR("MBR");
    Microinstrucao mir = new Microinstrucao("00000000000000000000000000000000");
    Latch latA = new Latch("A");
    Latch latB = new Latch("B");
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
        this.memP = memP;
        int i = 0;

        while(true){
            System.out.println("Microinstrução: " + MicroinstrucaoMap.getDescricao(registradores.MPC.getValor()));
            // System.out.println("MIR: " + mir.getMic());
            this.subciclo1();
            this.subciclo2();
            this.subciclo3();
            this.subciclo4();
//            System.out.println("ULA\n" + ula.toString()+ "\n");
//            System.out.println("\nMMUX\n" + mmux.toString());
//            System.out.println("IR: " + registradores.registradores[3].getValor());
//            System.out.println("TIR: " + registradores.registradores[4].getValor());
//            System.out.println("MAR: " + this.mar.getValor());
//            System.out.println("MBR: " + this.mbr.getValor());
            //System.out.println("Memoria[10]: " + memP.ler("0000000000001010"));
            i++;
        }
    }

    public void subciclo1(){
        this.mir.setMic(memC.getPos(registradores.MPC.getValor()).getMic()); //passa a instrução em mem[mpc] para o mir
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
        incrementador.incrementar(this.registradores.MPC.getValor());
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
        this.registradores.MPC.setValor(this.mmux.getSaida());
        if(this.mbr.isRD()) this.mbr.setValor(this.memP.ler(this.mar.getValor()));
        if(this.mbr.isWR()) this.memP.escrever(this.mar.getValor(), this.mbr.getValor());
    }
}
