package Back;

public class Decodificador {
    public String saida;

    public Decodificador() {
        saida = "0000000000000000";
    }

    public String decodificar(String codificado) throws IllegalArgumentException {
        int tam = codificado.length();

        if(tam != 4) throw new IllegalArgumentException("Valor precisa ter 4 bits.");

        StringBuilder decodificado = new StringBuilder();

        int codificadoINT = Integer.parseInt(codificado, 2), i = 0;

        for(; i < codificadoINT; i++) decodificado.append("0");
        decodificado.append("1");
        i++;
        for(; i < 16; i++) decodificado.append("0");

        this.saida = decodificado.toString();
        return this.saida;
    }
}