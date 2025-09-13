package back.comum;

public class FormatoMic { //Armazena os intervalos de cada campo da microinstrução
    public static final int AMUX = 0;
    public static final int[] COND = new int[]{1, 3};
    public static final int[] ALU = new int[]{3, 5};
    public static final int[] SH =  new int[]{5, 7};
    public static final int MBR = 7;
    public static final int MAR = 8;
    public static final int RD = 9;
    public static final int WR = 10;
    public static final int ENC = 11;
    public static final int[] C = new int[]{12, 16};
    public static final int[] B = new int[]{16, 20};
    public static final int[] A = new int[]{20, 24};
    public static final int ADDR = 24;
}