package back.cpu;

import back.comum.Microinstrucao;

public class MemoriaControle {
    private final Microinstrucao[] memoria = {
            new Microinstrucao("00001000110000000000000000000000"), //0
            new Microinstrucao("01001000000000000000000000000001"),
            new Microinstrucao("00000000000100000110000000000000"),
            new Microinstrucao("10011001000100110000000000011111"),
            new Microinstrucao("00010010000101000011001100010110"),
            new Microinstrucao("00011010000101000000010000001101"),
            new Microinstrucao("00011000000000000000010000001010"),
            new Microinstrucao("00001000110000000011000000000000"),
            new Microinstrucao("01001000000000000000000000001000"),
            new Microinstrucao("10111000000100010000000000000000"),
            new Microinstrucao("00001001101000000011000100000000"), //10
            new Microinstrucao("01001000000000000000000000001011"),
            new Microinstrucao("00111000000000000000000000000000"),
            new Microinstrucao("00011000000000000000010000010001"),
            new Microinstrucao("00001000110000000011000000000000"),
            new Microinstrucao("01001000000000000000000000001111"),
            new Microinstrucao("10110000000100010001000000000000"),
            new Microinstrucao("00001000110000000011000000000000"),
            new Microinstrucao("01001000000000000000000000010010"),
            new Microinstrucao("00000000000100010110000100000000"),
            new Microinstrucao("10001100000110100000000000000000"), //20
            new Microinstrucao("00110000000100011010000100000000"),
            new Microinstrucao("00011010000101000000010000011100"),
            new Microinstrucao("00011000000000000000010000011010"),
            new Microinstrucao("00011000000000000000000100000000"),
            new Microinstrucao("00110100000100001000001100000000"),
            new Microinstrucao("00101000000000000000000100011001"),
            new Microinstrucao("00111000000000000000000000000000"),
            new Microinstrucao("00011000000000000000010000011110"),
            new Microinstrucao("00110100000100001000001100000000"),
            new Microinstrucao("00110100000100011000001100000000"), //30
            new Microinstrucao("00010010000101000011001100101011"),
            new Microinstrucao("00011010000101000000010000100110"),
            new Microinstrucao("00011000000000000000010000100100"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("00111000110000001010000000001000"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("00111000001000001010000100001011"),
            new Microinstrucao("00011000000000000000010000101001"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("00111000010000001010000000001111"), //40
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("00111000110000001010000000010010"),
            new Microinstrucao("00011010000101000000010000110001"),
            new Microinstrucao("00011000000000000000010000101110"),
            new Microinstrucao("00011000000000000000000100011001"),
            new Microinstrucao("00111000000000000000000000000000"),
            new Microinstrucao("00101000000000000000000100000000"),
            new Microinstrucao("00110100000100001000001100000000"),
            new Microinstrucao("00011010000101000000010000110110"),
            new Microinstrucao("00000000000100100110001000000000"), //50
            new Microinstrucao("00001001101000000010000000000000"),
            new Microinstrucao("01001000000000000000000000110100"),
            new Microinstrucao("00110100000100001000001100000000"),
            new Microinstrucao("00011010000101000000010001000110"),
            new Microinstrucao("00011010000101000000010001000000"),
            new Microinstrucao("00011000000000000000010000111101"),
            new Microinstrucao("00001000110000000001000000000000"),
            new Microinstrucao("01001000000000000000000000111010"),
            new Microinstrucao("00000000000100100110001000000000"),
            new Microinstrucao("00111000101000000010000000001011"), //60
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("01001000000000000000000000111110"),
            new Microinstrucao("00111000101000000001000000001011"),
            new Microinstrucao("00011000000000000000010001000011"),
            new Microinstrucao("00000000000100100111001000000000"),
            new Microinstrucao("00111001101000000010000100001011"),
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("01001000000000000000000001000100"),
            new Microinstrucao("10111000000100010000000000000000"),
            new Microinstrucao("00011010000101000000010001001110"), //70
            new Microinstrucao("00011000000000000000010001001011"),
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("01001000000000000000000001001001"),
            new Microinstrucao("10000001000100000000000000000000"),
            new Microinstrucao("00001000000110100000000100000000"),
            new Microinstrucao("00001000000100010000001000000000"),
            new Microinstrucao("00111000000100100000101000000000"),
            new Microinstrucao("00011000000000000000010010100001"),
            new Microinstrucao("00000100000110100011100100000000"),
            new Microinstrucao("00110000000100100010101000000000"), //80
            new Microinstrucao("00000100000110100011100100000000"),
            new Microinstrucao("00001100000110100000101000000000"),
            new Microinstrucao("00110000000110100110101010100000"), //83
    };

    public Microinstrucao getPos(String MPC) {
        short mpcShort = Short.parseShort(MPC, 2);
        return this.memoria[mpcShort];
    }
}