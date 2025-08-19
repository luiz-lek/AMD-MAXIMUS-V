package Back;

import java.io.IOException;

public class ConversaoTipos {
    public static short bitsToShort(String bits, int qtdBitsEsperada) throws IOException {
        if(bits.length() != qtdBitsEsperada) throw new IOException("String com " + bits.length() + " bits");
        return (short) Integer.parseInt(bits, 2);
    }

    public static int bitsToInt(String bits, int qtdBitsEsperada) throws IOException {
        if(bits.length() != qtdBitsEsperada) throw new IOException("String com " + bits.length() + " bits\nQuantidade esperada: " + qtdBitsEsperada);
        return Integer.parseInt(bits, 2);
    }

    public static String shorToString(short bits) {
        StringBuilder bitsString = new StringBuilder(Integer.toBinaryString(bits & 0xFFFF));

        while(bitsString.length() < 16) bitsString.insert(0, "0");

        return bitsString.toString();
    }

    public static String binaryToHexadecimal(String bits) {

        int valorNumerico = Integer.parseInt(bits, 2);

        return String.format("%#04x", valorNumerico);
    }
}
