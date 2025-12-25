package visao;

import back.cpu.CPU;
import back.memorias.Cache;
import back.montagem.Assembler;
import back.memorias.MemoriaPrincipal;

public class ComponentesTela2 {
    public CPU cpu;
    public MemoriaPrincipal memoriaPrincipal;
    public Assembler assembler;
    public Cache cache;

    public ComponentesTela2(CPU cpu, MemoriaPrincipal memoriaPrincipal, Assembler assembler, Cache cache) {
        this.cpu = cpu;
        this.memoriaPrincipal = memoriaPrincipal;
        this.assembler = assembler;
        this.cache = cache;
    }
}
