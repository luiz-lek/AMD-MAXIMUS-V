package Back;

public class Decodificador {
    public String entrada = "0000", saida = "0000000000000000", ativado = "0";

    public Decodificador() {
        saida = "0000000000000000";
    }

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public void setAtivado(String ativado) {
        this.ativado = ativado;
    }

    public String decodificar() throws IllegalArgumentException {
        int tam = this.entrada.length();

        if(tam != 4) throw new IllegalArgumentException("Valor precisa ter 4 bits.");

        StringBuilder decodificado = new StringBuilder();

        int entradaINT = Integer.parseInt(this.entrada, 2), i = 0;

        for(; i < entradaINT; i++) decodificado.append("0");
        decodificado.append("1");
        i++;
        for(; i < 16; i++) decodificado.append("0");

        this.saida = decodificado.toString();
        return this.saida;
    }
}