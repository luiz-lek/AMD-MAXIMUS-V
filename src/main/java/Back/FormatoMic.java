package Back;

public class FormatoMic { //armazena os intervalos de cada campo da microinstrução
    public static final int AMUX = 0;
    public static final int[] COND = new int[]{1, 2};
    public static final int[] ALU = new int[]{3, 4};
    public static final int[] SH =  new int[]{5, 6};
    public static final int MBR = 7;
    public static final int MAR = 8;
    public static final int RD = 9;
    public static final int WR = 10;
    public static final int ENC = 11;
    public static final int[] C = new int[]{12, 15};
    public static final int[] B = new int[]{16, 19};
    public static final int[] A = new int[]{20, 23};
    public static final int[] ADDR = new  int[]{24, 32};
}