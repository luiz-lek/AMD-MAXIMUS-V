package back.memorias;

public interface Cache {
    public boolean escrever(String endereco, String dado) throws Exception;
    public String ler(String endereco) throws Exception;
    @Override
    public String toString();
}