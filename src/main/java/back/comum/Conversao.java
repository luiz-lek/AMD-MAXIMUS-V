package back.comum;

import java.io.IOException;

public class Conversao {
    public static short bitsToShort(String bits, int qtdBitsEsperada) throws IOException {
        if(bits.length() != qtdBitsEsperada) throw new IOException("String com " + bits.length() + " bits");
        return (short) Integer.parseInt(bits, 2);
    }

    public static int binarioToInt(String bits, int qtdBitsEsperada) throws IOException {
        if(bits.length() != qtdBitsEsperada) throw new IOException("String com " + bits.length() + " bits\nQuantidade esperada: " + qtdBitsEsperada);
        return Integer.parseInt(bits, 2);
    }

    public static String shortToString(short bits) {
        StringBuilder bitsString = new StringBuilder(Integer.toBinaryString(bits & 0xFFFF));

        while(bitsString.length() < 16) bitsString.insert(0, "0");

        return bitsString.toString();
    }

    public static String binarioToHexadecimal(String bits) {

        int valorNumerico = Integer.parseInt(bits, 2);

        return String.format("%#04x", valorNumerico);
    }

    public static String binarioToInt(String bits, int qtdBitsEsperada, boolean retornarComoString) throws IOException {
        int valorNumerico = binarioToInt(bits, qtdBitsEsperada);
        return Integer.toString(valorNumerico);
    }

    public static String binarioToStrDecimal(String bits, int qtdBitsEsperada) throws Exception {
        if(bits.length() != qtdBitsEsperada) throw new Exception("String com " + bits.length() + " bits\nQuantidade esperada: " + qtdBitsEsperada);

        int valorNumerico = Integer.parseInt(bits, 2);
        return Integer.toString(valorNumerico);
    }

    public static String ajustarDigitosDecimal(String digitos, int qtdBitsDesejada) throws IOException {
        int digitosLength = digitos.length();
        StringBuilder digitosFormatado = new StringBuilder();

        for(int i = digitosLength; i < qtdBitsDesejada; i++) digitosFormatado.append("0");

        digitosFormatado.append(digitos);

        return digitosFormatado.toString();
    }
}
