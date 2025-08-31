package back.cpu;

import back.comum.Microinstrucao;

public class MemoriaControle {
    private final Microinstrucao[] memoria = {
            new Microinstrucao("00010000110000000000000000000000"), //0
            new Microinstrucao("00000000010100000110000000000000"),
            new Microinstrucao("10110000000100110000000000011100"),
            new Microinstrucao("00100010000101000011001100010011"),
            new Microinstrucao("00110010000101000000010000001011"),
            new Microinstrucao("00110000000000000000010000001001"),
            new Microinstrucao("00010000110000000011000000000000"),
            new Microinstrucao("00010000010000000000000000000000"),
            new Microinstrucao("11110000000100010000000000000000"),
            new Microinstrucao("00010001101000000011000100000000"),
            new Microinstrucao("01110000001000000000000000000000"), //10
            new Microinstrucao("00110000000000000000010000001111"),
            new Microinstrucao("00010000110000000011000000000000"),
            new Microinstrucao("00010000010000000000000000000000"),
            new Microinstrucao("11100000000100010001000000000000"),
            new Microinstrucao("00010000110000000011000000000000"),
            new Microinstrucao("00000000010100010110000100000000"),
            new Microinstrucao("10011000010110100000000000000000"),
            new Microinstrucao("01100000000100011010000100000000"),
            new Microinstrucao("00110010000101000000010000011001"),
            new Microinstrucao("00110000000000000000010000010111"), //20
            new Microinstrucao("00110000000000000000000100000000"),
            new Microinstrucao("01101000000100001000001100000000"),
            new Microinstrucao("01010000000000000000000100010110"),
            new Microinstrucao("01100000000000000000000000000000"),
            new Microinstrucao("00110000000000000000010000011011"),
            new Microinstrucao("01101000000100001000001100000000"),
            new Microinstrucao("01101000000100011000001100000000"),
            new Microinstrucao("00100010000101000011001100101000"),
            new Microinstrucao("00110010000101000000010000100011"),
            new Microinstrucao("00110000000000000000010000100001"), //30
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("01110000110000001010000000000111"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("01110001101000001010000100001010"),
            new Microinstrucao("00110000000000000000010000100110"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("01110000110000001010000000001101"),
            new Microinstrucao("00000000000110100010001100000000"),
            new Microinstrucao("01110000110000001010000000010000"),
            new Microinstrucao("00110010000101000000010000101110"), //40
            new Microinstrucao("00110000000000000000010000101100"),
            new Microinstrucao("00110000000000000000000100010110"),
            new Microinstrucao("01100000000000000000000000000000"),
            new Microinstrucao("01010000000000000000000100000000"),
            new Microinstrucao("01101000000100001000001100000000"),
            new Microinstrucao("00110010000101000000010000110010"),
            new Microinstrucao("00000000010100110111001100000000"),
            new Microinstrucao("00010001101000000010000000000000"),
            new Microinstrucao("01101000001100001000001100000000"),
            new Microinstrucao("00110010000101000000010001000001"), //50
            new Microinstrucao("00110010000101000000010000111011"),
            new Microinstrucao("00110000000000000000010000111000"),
            new Microinstrucao("00010000110100000001000000000000"),
            new Microinstrucao("00000000010100110111001100000000"),
            new Microinstrucao("01110000101000000010000000001010"),
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("00010000010000000000000000000000"),
            new Microinstrucao("01110000101000000001000000001010"),
            new Microinstrucao("00110000000000000000010000111110"),
            new Microinstrucao("00000000000100100111001000000000"), //60
            new Microinstrucao("01110001101000000010000100001010"),
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("00010000010000000000000000000000"),
            new Microinstrucao("11110000000100010000000000000000"),
            new Microinstrucao("00110010000101000000010001001001"),
            new Microinstrucao("00110000000000000000010001000110"),
            new Microinstrucao("00000000110100100010011000000000"),
            new Microinstrucao("00010000010000000000000000000000"),
            new Microinstrucao("11110000000100000000000000000000"),
            new Microinstrucao("00010000000110100000000100000000"), //70
            new Microinstrucao("00010000000100010000001000000000"),
            new Microinstrucao("01110000000100100000101000000000"),
            new Microinstrucao("00110000000000000000010001001100"),
            new Microinstrucao("00001000000110101001001100000000"),
            new Microinstrucao("01100000000100101010001000000000"),
            new Microinstrucao("00001000000110101001001100000000"),
            new Microinstrucao("00011000000110100000101000000000"),
            new Microinstrucao("01100000000100010110000101001011") //78
    };

    public Microinstrucao getPos(String MPC) {
        short mpcShort = Short.parseShort(MPC, 2);
        return this.memoria[mpcShort];
    }
}