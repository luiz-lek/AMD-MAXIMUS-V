package Back;

import java.util.HashMap;
import java.util.Map;

public class MicroinstrucaoMap {

    private static final Map<Integer, String> mapa = new HashMap<>();

    static {
        mapa.put(0,  "mar := pc; rd;");
        mapa.put(1,  "pc := pc + 1; rd;");
        mapa.put(2,  "ir := mbr; if n then goto 28;");
        mapa.put(3,  "tir := lshift(ir + ir); if n then goto 19;");
        mapa.put(4,  "tir := lshift(tir); if n then goto 11;");
        mapa.put(5,  "alu := tir; if n then goto 9;");
        mapa.put(6,  "mar := ir; rd;");
        mapa.put(7,  "rd;");
        mapa.put(8,  "ac := mbr; goto 0;");
        mapa.put(9,  "mar := ir; mbr := ac; wr;");
        mapa.put(10, "wr; goto 0;");
        mapa.put(11, "alu := tir; if n then goto 15;");
        mapa.put(12, "mar := ir; rd;");
        mapa.put(13, "rd;");
        mapa.put(14, "ac := mbr + ac; goto 0;");
        mapa.put(15, "mar := ir; rd;");
        mapa.put(16, "ac := ac + 1; rd;");
        mapa.put(17, "a := inv(mbr);");
        mapa.put(18, "ac := ac + a; goto 0;");
        mapa.put(19, "tir := lshift(tir); if n then goto 25;");
        mapa.put(20, "alu := tir; if n then goto 23;");
        mapa.put(21, "alu := ac; if n then goto 0;");
        mapa.put(22, "pc := band(ir, amask); goto 0;");
        mapa.put(23, "alu := ac; if z then goto 22;");
        mapa.put(24, "goto 0;");
        mapa.put(25, "alu := tir; if n then goto 27;");
        mapa.put(26, "pc := band(ir, amask); goto 0;");
        mapa.put(27, "ac := band(ir, amask); goto 0;");
        mapa.put(28, "tir := lshift(ir + ir); if n then goto 40;");
        mapa.put(29, "tir := lshift(tir); if n then goto 35;");
        mapa.put(30, "alu := tir; if n then goto 33;");
        mapa.put(31, "a := ir + sp;");
        mapa.put(32, "mar := a; rd; goto 7;");
        mapa.put(33, "a := ir + sp;");
        mapa.put(34, "mar := a; mbr := ac; wr; goto 10;");
        mapa.put(35, "alu := tir; if n then goto 38;");
        mapa.put(36, "a := ir + sp;");
        mapa.put(37, "mar := a; rd; goto 13;");
        mapa.put(38, "a := ir + sp;");
        mapa.put(39, "mar := a; rd; goto 16;");
        mapa.put(40, "tir := lshift(tir); if n then goto 46;");
        mapa.put(41, "alu := tir; if n then goto 44;");
        mapa.put(42, "alu := ac; if n then goto 22;");
        mapa.put(43, "goto 0;");
        mapa.put(44, "alu := ac; if z then goto 0;");
        mapa.put(45, "pc := band(ir, amask); goto 0;");
        mapa.put(46, "tir := lshift(tir); if n then goto 50;");
        mapa.put(47, "sp := sp + (-1);");
        mapa.put(48, "mar := sp; mbr := pc; wr;");
        mapa.put(49, "pc := band(ir, amask); wr; goto 0;");
        mapa.put(50, "tir := lshift(tir); if n then goto 65;");
        mapa.put(51, "tir := lshift(tir); if n then goto 59;");
        mapa.put(52, "alu := tir; if n then goto 56;");
        mapa.put(53, "mar := ac; rd;");
        mapa.put(54, "sp := sp + (-1); rd;");
        mapa.put(55, "mar := sp; wr; goto 10;");
        mapa.put(56, "mar := sp; sp := sp + 1; rd;");
        mapa.put(57, "rd;");
        mapa.put(58, "mar := ac; wr; goto 10;");
        mapa.put(59, "alu := tir; if n then goto 62;");
        mapa.put(60, "sp := sp + (-1);");
        mapa.put(61, "mar := sp; mbr := ac; wr; goto 10;");
        mapa.put(62, "mar := sp; sp := sp + 1; rd;");
        mapa.put(63, "rd;");
        mapa.put(64, "ac := mbr; goto 0;");
        mapa.put(65, "tir := lshift(tir); if n then goto 73;");
        mapa.put(66, "alu := tir; if n then goto 70;");
        mapa.put(67, "mar := sp; sp := sp + 1; rd;");
        mapa.put(68, "rd;");
        mapa.put(69, "pc := mbr; goto 0;");
        mapa.put(70, "a := ac;");
        mapa.put(71, "ac := sp;");
        mapa.put(72, "sp := a; goto 0;");
        mapa.put(73, "alu := tir; if n then goto 76;");
        mapa.put(74, "a := band(ir, smask);");
        mapa.put(75, "sp := sp + a; goto 0;");
        mapa.put(76, "a := band(ir, smask);");
        mapa.put(77, "a := inv(a);");
        mapa.put(78, "a := a + 1; goto 75;");
    }

    public static String getDescricao(String linha) {
        int linhaINT = Integer.parseInt(linha, 2);
        return mapa.getOrDefault(linhaINT, "Instrução não encontrada");
    }
}