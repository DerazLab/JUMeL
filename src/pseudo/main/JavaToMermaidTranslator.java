package pseudo.main;

import pseudo.lexer.*;
import pseudo.parser.*;
import pseudo.symbols.*;
import pseudo.exceptions.*;

public class JavaToMermaidTranslator {
    public static void main(String[] args) {
        String rutaArchivo = "./Estudiante.java";
        if (args.length > 0) {
            rutaArchivo = args[0];
        }

        try {
            System.out.println("=== Iniciando Traductor de Java a UML (vía Mermaid JS) [SOLID-Architected] ===");
            
            // ⋆˖⁺‧₊☽⛥Fase 1: Lectura de archivo (ProgramReader)⛥☾₊‧⁺˖⋆ //
            String entrada = ProgramReader.leerPrograma(rutaArchivo);
            if (entrada.isEmpty()) {
                System.err.println("Error: No se pudo leer el archivo o el archivo esta vacio: " + rutaArchivo);
                System.exit(1);
            }

            // ⋆˖⁺‧₊☽⛥Fase 2: Análisis Léxico⛥☾₊‧⁺˖⋆ //
            PseudoLexer lexer = new PseudoLexer();
            lexer.analizar(entrada);

            // ⋆˖⁺‧₊☽⛥Fase 3: Análisis Sintáctico y Tabla de Símbolos⛥☾₊‧⁺˖⋆ //
            SymbolTable ts = new SymbolTable();
            PseudoParser parser = new PseudoParser(ts);
            parser.analizar(lexer);

            // ⋆˖⁺‧₊☽⛥Fase 4: Generación de Código Mermaid (MermaidCodeGenerator)⛥☾₊‧⁺˖⋆ //
            String codigoMermaid = MermaidCodeGenerator.generarMermaid(ts);
            System.out.println("\n*** Codigo Mermaid Generado ***\n");
            System.out.println(codigoMermaid);

            // ⋆˖⁺‧₊☽⛥Fase 5: Generación de Archivo HTML Interactivo y Premium (HtmlReportGenerator)⛥☾₊‧⁺˖⋆ //
            HtmlReportGenerator.generarReporte("./salida.html", codigoMermaid, rutaArchivo);
            System.out.println("\n=== Exito: Se ha generado 'salida.html' correctamente. ===");

        } catch (LexicalException | SyntaxException e) {
            System.err.println("\n[Error de Compilacion]: " + e.getMessage());
        }
    }
}
