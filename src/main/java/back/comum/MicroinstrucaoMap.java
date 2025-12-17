package back.comum;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MicroinstrucaoMap {

    private static final Map<Integer, String> mapa = new HashMap<>();

    static { //Armazena a descrição de cada linha da memória de controle
        mapa.put(0,  "mar := pc; rd;");
        mapa.put(1,  "if not ready goto 1;");
        mapa.put(2,  "pc := pc + 1;");
        mapa.put(3,  "ir := mbr; if n then goto 31;");
        mapa.put(4,  "tir := lshift(ir + ir); if n then goto 22;");
        mapa.put(5,  "tir := lshift(tir); if n then goto 13;");
        mapa.put(6,  "alu := tir; if n then goto 10;");
        mapa.put(7,  "mar := ir; rd;");
        mapa.put(8,  "if not ready goto 8;");
        mapa.put(9,  "ac := mbr; goto 0;");
        mapa.put(10, "mar := ir; mbr := ac; wr;");
        mapa.put(11, "if not ready goto 11;");
        mapa.put(12, "goto 0;");
        mapa.put(13, "alu := tir; if n then goto 17;");
        mapa.put(14, "mar := ir; rd;");
        mapa.put(15, "if not ready goto 15;");
        mapa.put(16, "ac := mbr + ac; goto 0;");
        mapa.put(17, "mar := ir; rd;");
        mapa.put(18, "if not ready goto 18;");
        mapa.put(19, "ac := ac + 1;");
        mapa.put(20, "a := inv(mbr);");
        mapa.put(21, "ac := ac + a; goto 0;");
        mapa.put(22, "tir := lshift(tir); if n then goto 28;");
        mapa.put(23, "alu := tir; if n then goto 26;");
        mapa.put(24, "alu := ac; if n then goto 0;");
        mapa.put(25, "pc := band(ir, amask); goto 0;");
        mapa.put(26, "alu := ac; if z then goto 25;");
        mapa.put(27, "goto 0;");
        mapa.put(28, "alu := tir; if n then goto 30;");
        mapa.put(29, "pc := band(ir, amask); goto 0;");
        mapa.put(30, "ac := band(ir, amask); goto 0;");
        mapa.put(31, "tir := lshift(ir + ir); if n then goto 43;");
        mapa.put(32, "tir := lshift(tir); if n then goto 38;");
        mapa.put(33, "alu := tir; if n then goto 36;");
        mapa.put(34, "a := ir + sp;");
        mapa.put(35, "mar := a; rd; goto 8;");
        mapa.put(36, "a := ir + sp;");
        mapa.put(37, "mar := a; mbr := ac; wr; goto 11;");
        mapa.put(38, "alu := tir; if n then goto 41;");
        mapa.put(39, "a := ir + sp;");
        mapa.put(40, "mar := a; rd; goto 15;");
        mapa.put(41, "a := ir + sp;");
        mapa.put(42, "mar := a; rd; goto 18;");
        mapa.put(43, "tir := lshift(tir); if n then goto 48;");
        mapa.put(44, "alu := tir; if n then goto 46;");
        mapa.put(45, "alu := ac; if n then goto 25;");
        mapa.put(46, "goto 0;");
        mapa.put(47, "alu := ac; if z then goto 0;");
        mapa.put(48, "pc := band(ir, amask); goto 0;");
        mapa.put(49, "tir := lshift(tir); if n then goto 54;");
        mapa.put(50, "sp := sp + (-1);");
        mapa.put(51, "mar := sp; mbr := pc; wr;");
        mapa.put(52, "if not ready goto 52;");
        mapa.put(53, "pc := band(ir, amask); goto 0;");
        mapa.put(54, "tir := lshift(tir); if n then goto 70;");
        mapa.put(55, "tir := lshift(tir); if n then goto 64;");
        mapa.put(56, "alu := tir; if n then goto 61;");
        mapa.put(57, "mar := ac; rd;");
        mapa.put(58, "if not ready goto 58;");
        mapa.put(59, "sp := sp + (-1);");
        mapa.put(60, "mar := sp; wr; goto 11;");
        mapa.put(61, "mar := sp; sp := sp + 1; rd;");
        mapa.put(62, "if not ready goto 62;");
        mapa.put(63, "mar := ac; wr; goto 11;");
        mapa.put(64, "alu := tir; if n then goto 67;");
        mapa.put(65, "sp := sp + (-1);");
        mapa.put(66, "mar := sp; mbr := ac; wr; goto 11;");
        mapa.put(67, "mar := sp; sp := sp + 1; rd;");
        mapa.put(68, "if not ready goto 68;");
        mapa.put(69, "ac := mbr; goto 0;");
        mapa.put(70, "tir := lshift(tir); if n then goto 78;");
        mapa.put(71, "alu := tir; if n then goto 75;");
        mapa.put(72, "mar := sp; sp := sp + 1; rd;");
        mapa.put(73, "if not ready goto 73;");
        mapa.put(74, "pc := mbr; goto 0;");
        mapa.put(75, "a := ac;");
        mapa.put(76, "ac := sp;");
        mapa.put(77, "sp := a; goto 0;");
        mapa.put(78, "alu := tir; if n then goto 81;");
        mapa.put(79, "a := band(ir, smask);");
        mapa.put(80, "sp := sp + a; goto 0;");
        mapa.put(81, "a := band(ir, smask);");
        mapa.put(82, "a := inv(a);");
        mapa.put(83, "a := a + 1; goto 80;");
    }

    public static String getDescricao(String linha) throws Exception {
        int linhaINT = Conversao.binarioToInt(linha, 16);
        return mapa.getOrDefault(linhaINT, "Instrução não encontrada");
    }
}