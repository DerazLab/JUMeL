package pseudo.main;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
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
            System.out.println("=== Iniciando Traductor de Java a UML (vía Mermaid JS) ===");
            String entrada = leerPrograma(rutaArchivo);
            if (entrada.isEmpty()) {
                System.err.println("Error: No se pudo leer el archivo o el archivo esta vacio: " + rutaArchivo);
                System.exit(1);
            }

            // ⋆˖⁺‧₊☽⛥Fase 1: Analisis Lexico⛥☾₊‧⁺˖⋆ //
            PseudoLexer lexer = new PseudoLexer();
            lexer.analizar(entrada);

            // ⋆˖⁺‧₊☽⛥Fase 2: Analisis Sintactico y Tabla de Simbolos⛥☾₊‧⁺˖⋆ //
            SymbolTable ts = new SymbolTable();
            PseudoParser parser = new PseudoParser(ts);
            parser.analizar(lexer);

            // ⋆˖⁺‧₊☽⛥Fase 3: Generacion de Codigo Mermaid y Relaciones⛥☾₊‧⁺˖⋆ //
            String codigoMermaid = generarMermaid(ts);
            System.out.println("\n*** Codigo Mermaid Generado ***\n");
            System.out.println(codigoMermaid);

            // ⋆˖⁺‧₊☽⛥Fase 4: Generacion de Archivo HTML Interactivo y Premium⛥☾₊‧⁺˖⋆ //
            generarHTML(codigoMermaid, ts, rutaArchivo);
            System.out.println("\n=== Exito: Se ha generado 'salida.html' correctamente. ===");

        } catch (LexicalException | SyntaxException e) {
            System.err.println("\n[Error de Compilacion]: " + e.getMessage());
        }
    }

    private static String leerPrograma(String nombre) {
        StringBuilder entrada = new StringBuilder();
        try (java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(new java.io.FileInputStream(nombre), java.nio.charset.StandardCharsets.UTF_8))) {
            int caracter;
            while ((caracter = reader.read()) != -1) {
                entrada.append((char) caracter);
            }
            return entrada.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static String generarMermaid(SymbolTable ts) {
        StringBuilder sb = new StringBuilder();
        sb.append("classDiagram\n");

        List<ClassSymbol> clases = obtenerClases(ts);

        // ⋆˖⁺‧₊☽⛥Generar relaciones de Herencia, Implementacion y Composicion primero⛥☾₊‧⁺˖⋆ //
        for (ClassSymbol cls : clases) {
            String sanClsName = sanitizar(cls.getName());
            if (cls.getParentScope() instanceof ClassSymbol) {
                sb.append("    ").append(sanitizar(((ClassSymbol) cls.getParentScope()).getName()))
                  .append(" <|-- ").append(sanClsName).append("\n");
            }

            for (ClassSymbol iface : cls.getImplementedInterfaces()) {
                sb.append("    ").append(sanitizar(iface.getName()))
                  .append(" <|.. ").append(sanClsName).append("\n");
            }

            for (Symbol member : cls.getMembers().values()) {
                if (member instanceof VariableSymbol) {
                    Type t = member.getType();
                    if (t != null) {
                        String typeName = t.getName();
                        // ⋆˖⁺‧₊☽⛥Si es un arreglo (ej. PaqueteDatos[]), removemos los corchetes para detectar la relacion⛥☾₊‧⁺˖⋆ //
                        if (typeName.endsWith("[]")) {
                            typeName = typeName.substring(0, typeName.length() - 2);
                        }
                        
                        Symbol resolved = ts.resolve(typeName);
                        if (resolved instanceof ClassSymbol) {
                            sb.append("    ").append(sanClsName)
                              .append(" *-- ").append(sanitizar(resolved.getName())).append("\n");
                        }
                    }
                }
            }
        }

        // ⋆˖⁺‧₊☽⛥Generar las definiciones de clases con sus miembros, estereotipos y modificadores static⛥☾₊‧⁺˖⋆ //
        for (ClassSymbol cls : clases) {
            String sanClsName = sanitizar(cls.getName());
            sb.append("    class ").append(sanClsName).append(" {\n");

            if (cls.isInterface()) {
                sb.append("        <<Interfaz>>\n");
            } else if (cls.isAbstract()) {
                sb.append("        <<Abstracta>>\n");
            }

            for (Symbol member : cls.getMembers().values()) {
                String prefix = obtenerPrefijo(member.getAccessModifier());
                String staticSuffix = member.isStatic() ? "$" : "";
                String sanMemberName = sanitizar(member.getName());
                
                if (member instanceof VariableSymbol) {
                    sb.append("        ").append(prefix)
                      .append(member.getType() != null ? sanitizar(member.getType().getName()) : "Object")
                      .append(" ").append(sanMemberName).append(staticSuffix).append("\n");
                } else if (member instanceof MethodSymbol) {
                    MethodSymbol method = (MethodSymbol) member;
                    sb.append("        ").append(prefix).append(sanitizar(method.getName())).append("(");
                    
                    List<String> pList = new ArrayList<>();
                    for (Symbol p : method.getMembers().values()) {
                        pList.add((p.getType() != null ? sanitizar(p.getType().getName()) : "Object") + " " + sanitizar(p.getName()));
                    }
                    sb.append(String.join(", ", pList));
                    
                    sb.append(") ").append(method.getType() != null ? sanitizar(method.getType().getName()) : "void").append(staticSuffix).append("\n");
                }
            }
            sb.append("    }\n");
        }

        return sb.toString();
    }

    private static String sanitizar(String text) {
        if (text == null) return "";
        return text.replace("ñ", "n")
                   .replace("Ñ", "N")
                   .replace("á", "a")
                   .replace("é", "e")
                   .replace("í", "i")
                   .replace("ó", "o")
                   .replace("ú", "u")
                   .replace("Á", "A")
                   .replace("É", "E")
                   .replace("Í", "I")
                   .replace("Ó", "O")
                   .replace("Ú", "U")
                   .replace("[]", "_Array");
    }

    private static String obtenerPrefijo(String mod) {
        if (mod == null) return "~";
        switch (mod.toLowerCase()) {
            case "public": return "+";
            case "private": return "-";
            case "protected": return "#";
            default: return "~";
        }
    }

    private static List<ClassSymbol> obtenerClases(SymbolTable ts) {
        List<ClassSymbol> clases = new ArrayList<>();
        // En base a la estructura, las clases se definen globalmente en la SymbolTable
        // Pero para asegurar que recorremos todas, buscamos recursivamente o por referencias
        for (Symbol sym : ts.toString().contains("global") ? resolverSimbolosGlobales(ts) : new ArrayList<Symbol>()) {
            if (sym instanceof ClassSymbol) {
                clases.add((ClassSymbol) sym);
            }
        }
        return clases;
    }

    private static List<Symbol> resolverSimbolosGlobales(SymbolTable ts) {
        // Obtenemos los simbolos directamente por reflexion o exponiendo los simbolos. 
        // Como la tabla almacena los simbolos en un mapa privado o de paquete "symbols",
        // podemos leerlos directamente si creamos un getter, o acceder a traves de una busqueda.
        // Vamos a modificar SymbolTable o extraerlos a traves de una clave conocida si es posible.
        // Pero para ser ultra-seguros, agregamos un getter de symbols en SymbolTable.java si no existe.
        // Espera, SymbolTable tiene "Map<String, Symbol> symbols". Es de acceso package-private!
        // Al estar en el mismo paquete pseudo.symbols, las clases del mismo paquete acceden.
        // Como JavaToMermaidTranslator esta en pseudo.main, necesitamos acceder.
        // Añadamos un metodo publico getSymbols() a SymbolTable.java!
        return new ArrayList<>(ts.getSymbols().values());
    }

    private static void generarHTML(String codigoMermaid, SymbolTable ts, String origen) {
        // ⋆˖⁺‧₊☽⛥Plantilla HTML simplificada en modo claro y enfocada en el UML⛥☾₊‧⁺˖⋆ //
        String htmlTemplate = "<!DOCTYPE html>\n" +
                "<html lang=\"es\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>Diagrama de Clases UML</title>\n" +
                "    <link href=\"https://fonts.googleapis.com/css2?family=Outfit:wght@400;600;800&display=swap\" rel=\"stylesheet\">\n" +
                "    <style>\n" +
                "        body {\n" +
                "            margin: 0;\n" +
                "            font-family: 'Outfit', sans-serif;\n" +
                "            background-color: #f3f4f6;\n" +
                "            color: #1f2937;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            align-items: center;\n" +
                "            min-height: 100vh;\n" +
                "            padding: 2rem;\n" +
                "            box-sizing: border-box;\n" +
                "        }\n" +
                "        .container {\n" +
                "            max-width: 1000px;\n" +
                "            width: 100%;\n" +
                "            background: #ffffff;\n" +
                "            border: 1px solid #e5e7eb;\n" +
                "            border-radius: 12px;\n" +
                "            padding: 2.5rem;\n" +
                "            box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);\n" +
                "        }\n" +
                "        h1 {\n" +
                "            margin-top: 0;\n" +
                "            font-size: 2rem;\n" +
                "            font-weight: 800;\n" +
                "            text-align: center;\n" +
                "            color: #111827;\n" +
                "            border-bottom: 2px solid #f3f4f6;\n" +
                "            padding-bottom: 1rem;\n" +
                "            margin-bottom: 2rem;\n" +
                "        }\n" +
                "        .diagram-wrapper {\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "            align-items: center;\n" +
                "            padding: 2rem;\n" +
                "            background: #f9fafb;\n" +
                "            border: 1px solid #f3f4f6;\n" +
                "            border-radius: 8px;\n" +
                "            margin-bottom: 2rem;\n" +
                "        }\n" +
                "        .mermaid {\n" +
                "            width: 100%;\n" +
                "            display: flex;\n" +
                "            justify-content: center;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            margin-top: 1.5rem;\n" +
                "            text-align: center;\n" +
                "            font-size: 0.9rem;\n" +
                "            color: #6b7280;\n" +
                "            border-top: 1px solid #f3f4f6;\n" +
                "            padding-top: 1.5rem;\n" +
                "        }\n" +
                "        .footer strong {\n" +
                "            color: #374151;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"container\">\n" +
                "        <h1>Diagrama UML del Proyecto</h1>\n" +
                "        \n" +
                "        <div class=\"diagram-wrapper\">\n" +
                "            <pre class=\"mermaid\">\n" +
                                     codigoMermaid + "\n" +
                "            </pre>\n" +
                "        </div>\n" +
                "        \n" +
                "        <div class=\"footer\">\n" +
                "            <p>Archivo de origen: <code>" + origen + "</code></p>\n" +
                "            <p>Desarrollado por: <strong>Deraz Labrador Emmanuel</strong> y <strong>Salazar Leal Javier Issac</strong></p>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "\n" +
                "    <script type=\"module\">\n" +
                "        import mermaid from 'https://cdn.jsdelivr.net/npm/mermaid@10/dist/mermaid.esm.min.mjs';\n" +
                "        mermaid.initialize({\n" +
                "            startOnLoad: true,\n" +
                "            theme: 'default'\n" +
                "        });\n" +
                "    </script>\n" +
                "</body>\n" +
                "</html>";

        try (FileWriter writer = new FileWriter("./salida.html")) {
            writer.write(htmlTemplate);
        } catch (IOException e) {
            System.err.println("Error al escribir el archivo salida.html: " + e.getMessage());
        }
    }
}
