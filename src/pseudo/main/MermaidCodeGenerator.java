package pseudo.main;

import java.util.*;
import pseudo.symbols.*;

public class MermaidCodeGenerator {
    public static String generarMermaid(SymbolTable ts) {
        StringBuilder sb = new StringBuilder();
        sb.append("classDiagram\n");

        List<ClassSymbol> clases = obtenerClases(ts);

        // ⋆˖⁺‧₊☽⛥Generar relaciones de Herencia, Realizacion y Composicion primero⛥☾₊‧⁺˖⋆ //
        for (ClassSymbol cls : clases) {
            if (cls.getParentScope() instanceof ClassSymbol) {
                sb.append("    ").append(((ClassSymbol) cls.getParentScope()).getName())
                  .append(" <|-- ").append(cls.getName()).append("\n");
            }
            
            // Realizaciones de Interfaces en UML (Flecha discontinua <|..)
            for (ClassSymbol iface : cls.getImplementedInterfaces()) {
                sb.append("    ").append(iface.getName())
                  .append(" <|.. ").append(cls.getName()).append("\n");
            }

            for (Symbol member : cls.getMembers().values()) {
                if (member instanceof VariableSymbol) {
                    Type t = member.getType();
                    if (t instanceof ClassSymbol) {
                        sb.append("    ").append(cls.getName())
                          .append(" *-- ").append(((ClassSymbol) t).getName()).append("\n");
                    } else if (t != null) {
                        // Si el tipo es el nombre de alguna clase registrada en la tabla
                        Symbol resolved = ts.resolve(t.getName());
                        if (resolved instanceof ClassSymbol) {
                            sb.append("    ").append(cls.getName())
                              .append(" *-- ").append(resolved.getName()).append("\n");
                        }
                    }
                }
            }
        }

        // ⋆˖⁺‧₊☽⛥Generar las definiciones de clases con sus miembros y estereotipos⛥☾₊‧⁺˖⋆ //
        for (ClassSymbol cls : clases) {
            sb.append("    class ").append(cls.getName()).append(" {\n");
            
            if (cls.isInterface()) {
                sb.append("        <<interface>>\n");
            } else if (cls.isAbstract()) {
                sb.append("        <<abstract>>\n");
            }

            for (Symbol member : cls.getMembers().values()) {
                String prefix = obtenerPrefijo(member.getAccessModifier());
                if (member instanceof VariableSymbol) {
                    sb.append("        ").append(prefix)
                      .append(member.getType() != null ? member.getType().getName() : "Object")
                      .append(" ").append(member.getName()).append("\n");
                } else if (member instanceof MethodSymbol) {
                    MethodSymbol method = (MethodSymbol) member;
                    sb.append("        ").append(prefix).append(method.getName()).append("(");
                    
                    // Parametros del metodo
                    List<String> pList = new ArrayList<>();
                    for (Symbol p : method.getMembers().values()) {
                        pList.add((p.getType() != null ? p.getType().getName() : "Object") + " " + p.getName());
                    }
                    sb.append(String.join(", ", pList));
                    
                    sb.append(") ").append(method.getType() != null ? method.getType().getName() : "void").append("\n");
                }
            }
            sb.append("    }\n");
        }

        return sb.toString();
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
        for (Symbol sym : ts.toString().contains("global") ? resolverSimbolosGlobales(ts) : new ArrayList<Symbol>()) {
            if (sym instanceof ClassSymbol) {
                clases.add((ClassSymbol) sym);
            }
        }
        return clases;
    }

    private static List<Symbol> resolverSimbolosGlobales(SymbolTable ts) {
        return new ArrayList<>(ts.getSymbols().values());
    }
}
