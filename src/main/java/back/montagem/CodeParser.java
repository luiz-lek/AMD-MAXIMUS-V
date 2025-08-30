package back.montagem;

import java.io.IOException;
import java.util.Arrays;

public class CodeParser {
    public static String[] parse(String macroPrograma) throws IOException {
        String[] macroArray = macroPrograma.toUpperCase().split("\\r?\\n");
        String[] macroLimpo = new String[macroArray.length];

        int i = 0;

        for(String linha : macroArray) {
            if(!linha.isEmpty()) {
               macroLimpo[i] = linha.trim();
               i++;
            }
        }

        return Arrays.copyOf(macroLimpo,i);
    }
}
