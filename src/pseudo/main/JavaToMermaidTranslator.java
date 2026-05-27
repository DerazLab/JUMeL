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
            System.out.println("=== Iniciando Traductor de Java a UML (vía Mermaid JS) [SOLID-Architected & Multi-File] ===");
            
            // ⋆˖⁺‧₊☽⛥Fase 1: Escaneo y lectura de archivos .java⛥☾₊‧⁺˖⋆ //
            java.util.List<String> archivos = ProgramReader.obtenerArchivosJava(rutaArchivo);
            if (archivos.isEmpty()) {
                System.err.println("Error: No se encontraron archivos .java en la ruta especificada: " + rutaArchivo);
                System.exit(1);
            }

            System.out.println("Se detectaron " + archivos.size() + " archivo(s) .java para procesar.");

            // ⋆˖⁺‧₊☽⛥Fase 2: Análisis de símbolos acumulativos⛥☾₊‧⁺˖⋆ //
            SymbolTable ts = new SymbolTable();
            int procesados = 0;

            for (String archivo : archivos) {
                String entrada = ProgramReader.leerPrograma(archivo);
                if (entrada.isEmpty()) {
                    continue;
                }

                // Análisis Léxico por archivo
                PseudoLexer lexer = new PseudoLexer();
                lexer.analizar(entrada);

                // Análisis Sintáctico compartiendo la misma Tabla de Símbolos global
                PseudoParser parser = new PseudoParser(ts);
                parser.analizar(lexer);
                procesados++;
            }

            if (procesados == 0) {
                System.err.println("Error: No se pudo procesar ningún archivo .java correctamente.");
                System.exit(1);
            }

            // ⋆˖⁺‧₊☽⛥Fase 3: Generación de Código Mermaid consolidado (MermaidCodeGenerator)⛥☾₊‧⁺˖⋆ //
            String codigoMermaid = MermaidCodeGenerator.generarMermaid(ts);
            System.out.println("\n*** Codigo Mermaid Generado ***\n");
            System.out.println(codigoMermaid);

            // ⋆˖⁺‧₊☽⛥Fase 4: Generación de Archivo HTML Interactivo y Premium (HtmlReportGenerator)⛥☾₊‧⁺˖⋆ //
            HtmlReportGenerator.generarReporte("./salida.html", codigoMermaid, rutaArchivo);
            System.out.println("\n=== Exito: Se ha generado 'salida.html' correctamente. ===");

        } catch (LexicalException | SyntaxException e) {
            System.err.println("\n[Error de Compilacion]: " + e.getMessage());
        }
    }
}
