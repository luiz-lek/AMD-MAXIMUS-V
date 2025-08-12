package Back;

public class Decodificador {
    public String entrada = "0000", saida = "0000000000000000";
    private boolean ENC = false;

    public void setEntrada(String entrada) {
        this.entrada = entrada;
    }

    public void setENC(String ativado) {
        this.ENC = !"0".equals(ativado);
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

    public boolean isENC() {
        return ENC;
    }
}