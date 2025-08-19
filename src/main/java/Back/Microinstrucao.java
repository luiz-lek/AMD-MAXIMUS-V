package Back;

public class Microinstrucao {
    private String mic;

    public Microinstrucao(String mic) throws IllegalArgumentException{
        if(mic.length() != 32) throw new IllegalArgumentException("Microinstrução com tamanho inválido");
        this.mic = mic;
    }

    public String getMic() { return this.mic; }

    public void setMic(String mic){ this.mic = mic; }

    public String getAMUX() { return String.valueOf(this.mic.charAt(FormatoMic.AMUX)); }

    public String getCOND() { return this.mic.substring(FormatoMic.COND[0], FormatoMic.COND[1]); }

    public String getALU() { return this.mic.substring(FormatoMic.ALU[0], FormatoMic.ALU[1]); }

    public String getSH() { return this.mic.substring(FormatoMic.SH[0], FormatoMic.SH[1]); }

    public String getMBR() { return String.valueOf(this.mic.charAt(FormatoMic.MBR)); }

    public String getMAR() { return String.valueOf(this.mic.charAt(FormatoMic.MAR)); }

    public String getRD() { return String.valueOf(this.mic.charAt(FormatoMic.RD)); }

    public String getWR() { return String.valueOf(this.mic.charAt(FormatoMic.WR)); }

    public String getENC() { return String.valueOf(this.mic.charAt(FormatoMic.ENC)); }

    public String getC() { return this.mic.substring(FormatoMic.C[0], FormatoMic.C[1]); }

    public String getB() { return this.mic.substring(FormatoMic.B[0], FormatoMic.B[1]); }

    public String getA() { return this.mic.substring(FormatoMic.A[0], FormatoMic.A[1]); }

    public String getADDR() { return this.mic.substring(FormatoMic.ADDR); }
}