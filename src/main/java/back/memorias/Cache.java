package back.memorias;

import back.cpu.MBR;

public interface Cache {
    public void escrever(String endereco, MBR mbr) throws Exception;
    public void ler(String endereco, MBR mbr) throws Exception;
    public int size();
    public String getTipoCache();
}