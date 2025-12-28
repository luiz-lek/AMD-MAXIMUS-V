package back.memorias;

import back.cpu.MBR;

public interface Cache {
    void escrever(String endereco, MBR mbr) throws Exception;
    void ler(String endereco, MBR mbr) throws Exception;
    int size();
    String getTipoCache();
}