package pseudo.main;

import java.io.FileReader;
import java.io.IOException;
import pseudo.lexer.*;
import pseudo.parser.*;
import pseudo.symbols.*;
import pseudo.intermedio.*;
import pseudo.exceptions.*;

public class PruebaTuplas {
    public static void main(String[] arg) {
        try {
            String entrada = leerPrograma("./ejemplo.alg"); 
            
            PseudoLexer lexer = new PseudoLexer();
            lexer.analizar(entrada);

            System.out.println("*** Analisis Lexico ***\n");
            for (Token t : lexer.getTokens()) {
                System.out.println(t);
            }

            System.out.println("\n*** Analisis Sintactico y Generacion de Tuplas ***\n");
            
            SymbolTable ts = new SymbolTable();
            PseudoGenerador generador = new PseudoGenerador(lexer.getTokens()); 
            PseudoParser parser = new PseudoParser(ts, generador); 
            
            parser.analizar(lexer);

            System.out.println("*** Tuplas generadas ***\n");
            for (Tupla t: generador.getTuplas()) {
                System.out.println(t);
            }

            System.out.println("\n*** Ejecucion del programa ***\n");
            PseudoInterprete interprete = new PseudoInterprete(ts);
            interprete.interpretar(generador.getTuplas());

        } catch (LexicalException | SyntaxException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    private static String leerPrograma(String nombre) {
        String entrada = "";
        try {
            FileReader reader = new FileReader(nombre);
            int caracter;
            while ((caracter = reader.read()) != -1) {
                entrada += (char) caracter;
            }
            reader.close();
            return entrada;
        } catch (IOException e) {
            return "";
        }
    }
}