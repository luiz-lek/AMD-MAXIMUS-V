package back.montagem;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CodeParser {
    public Map<String, Integer> flags = new HashMap<>();
    public Map<String, Integer> variaveis = new HashMap<>();
    public int tamPrograma = 0, posLivre;
    public String[] progLimpo;

    public String[] parse(String macroPrograma) throws IOException {
        this.parseFlag(macroPrograma);
        return this.progLimpo;
    }

    public void parseFlag(String macroPrograma) throws IOException {
        String[] macroArray = macroPrograma.toUpperCase().split("\\r?\\n");
        String[] macroLimpo = new String[macroArray.length];

        int i = 0;

        for(String linha : macroArray) {
            if(!linha.isEmpty()) {
               macroLimpo[i] = linha.trim();

               int j;
               for(j = 0; j < macroLimpo[i].length(); j++){
                   if(macroLimpo[i].charAt(j) == ':') {
                       String f = macroLimpo[i].substring(0, j);

                       System.out.println("Flag atual: " + f);

                       if(flags.containsKey(f)) throw new IOException("Flag já foi usada");

                       this.flags.put(f, i);
                       break;
                   }
               }

               i++;
            }
        }

        this.tamPrograma = i;
        this.posLivre = i;
        this.progLimpo = Arrays.copyOf(macroLimpo, i);
    }

    public int getEIncremntaPosLivre() {
        return this.posLivre++;
    }

    public int getValorVariavel(String variavel) {
        String upeerCase = variavel.toUpperCase();

        return this.variaveis.get(upeerCase);
    }
}
