package pseudo.parser;

import java.util.ArrayList;
import java.util.List;
import pseudo.lexer.*;
import pseudo.symbols.*;
import pseudo.exceptions.*;

// •───⋅⋆⁺‧₊☽⛥ Analizador sintactico adaptado para Java ⛦☾₊‧⁺⋆⋅───•
public class PseudoParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    
    // Variables para semantica y traduccion
    private SymbolTable symbolTable;
    private Scope currentScope;
    private List<String> errors;

    public PseudoParser(SymbolTable ts) {
        this.symbolTable = ts;
        this.currentScope = ts;
        this.errors = new ArrayList<>();
    }

    // ⋆˖⁺‧₊☽⛥Constructor compatible con llamadas existentes⛥☾₊‧⁺˖⋆ //
    public PseudoParser(SymbolTable ts, Object generadorIgnorado) {
        this(ts);
    }

    public void analizar(PseudoLexer lexer) throws SyntaxException {
        this.tokens = lexer.getTokens();
        this.indiceToken = 0;
        this.currentScope = symbolTable;

        if (tokens == null || tokens.isEmpty()) return;

        // ⋆˖⁺‧₊☽⛥Analiza secuencialmente las clases del archivo fuente de Java⛥☾₊‧⁺˖⋆ //
        while (indiceToken < tokens.size()) {
            clase();
        }
    }

    // ================= FUNCIONES AUXILIARES =================

    private Token getToken() {
        if (indiceToken < tokens.size()) {
            return tokens.get(indiceToken);
        }
        return tokens.get(tokens.size() - 1);
    }

    private boolean match(String nombre) {
        if (indiceToken < tokens.size() && tokens.get(indiceToken).getTipo().getNombre().equals(nombre)) {
            indiceToken++;
            return true;
        }
        return false;
    }

    private void expect(String nombre) throws SyntaxException {
        if (!match(nombre)) {
            String encontrado = (indiceToken < tokens.size()) ? tokens.get(indiceToken).getTipo().getNombre() : "EOF";
            throw new SyntaxException("Se esperaba '" + nombre + "', pero se encontro '" + encontrado + "'");
        }
    }

    private void consumirCuerpoMetodo() throws SyntaxException {
        if (match(TipoToken.LLAVEIZQ)) {
            int braceCount = 1;
            while (braceCount > 0 && indiceToken < tokens.size()) {
                Token t = tokens.get(indiceToken);
                if (t.getTipo().getNombre().equals(TipoToken.LLAVEIZQ)) {
                    braceCount++;
                } else if (t.getTipo().getNombre().equals(TipoToken.LLAVEDER)) {
                    braceCount--;
                }
                indiceToken++;
            }
        } else {
            expect(TipoToken.PUNTOYCOMA);
        }
    }

    // ================= REGLAS GRAMATICALES =================

    private void clase() throws SyntaxException {
        String accessMod = "";
        
        // ⋆˖⁺‧₊☽⛥Detectar modificador de acceso opcional para la clase⛥☾₊‧⁺˖⋆ //
        if (getToken().getTipo().getNombre().equals(TipoToken.PUBLIC)) {
            accessMod = "public";
            expect(TipoToken.PUBLIC);
        } else if (getToken().getTipo().getNombre().equals(TipoToken.PRIVATE)) {
            accessMod = "private";
            expect(TipoToken.PRIVATE);
        } else if (getToken().getTipo().getNombre().equals(TipoToken.PROTECTED)) {
            accessMod = "protected";
            expect(TipoToken.PROTECTED);
        }

        boolean isAbstract = false;
        if (match(TipoToken.ABSTRACT)) {
            isAbstract = true;
        }

        boolean isInterface = false;
        if (match(TipoToken.INTERFACE)) {
            isInterface = true;
        } else {
            expect(TipoToken.CLASS);
        }
        
        String className = getToken().getNombre();
        expect(TipoToken.VARIABLE);

        ClassSymbol superClass = null;
        if (match(TipoToken.EXTENDS)) {
            String superClassName = getToken().getNombre();
            expect(TipoToken.VARIABLE);
            
            // ⋆˖⁺‧₊☽⛥Registra dinamicamente la superclase en la tabla global para soportar herencia⛥☾₊‧⁺˖⋆ //
            Symbol resolved = symbolTable.resolve(superClassName);
            if (resolved instanceof ClassSymbol) {
                superClass = (ClassSymbol) resolved;
            } else {
                superClass = new ClassSymbol(superClassName, symbolTable, null);
                symbolTable.define(superClass);
            }
        }

        List<ClassSymbol> implInterfaces = new ArrayList<>();
        if (match(TipoToken.IMPLEMENTS)) {
            do {
                String interfaceName = getToken().getNombre();
                expect(TipoToken.VARIABLE);
                
                Symbol resolved = symbolTable.resolve(interfaceName);
                ClassSymbol interfaceSym;
                if (resolved instanceof ClassSymbol) {
                    interfaceSym = (ClassSymbol) resolved;
                } else {
                    interfaceSym = new ClassSymbol(interfaceName, symbolTable, null);
                    interfaceSym.setInterface(true);
                    symbolTable.define(interfaceSym);
                }
                implInterfaces.add(interfaceSym);
            } while (match(TipoToken.COMA));
        }

        ClassSymbol classSym = new ClassSymbol(className, currentScope, superClass);
        classSym.setAccessModifier(accessMod);
        classSym.setAbstract(isAbstract);
        classSym.setInterface(isInterface);
        classSym.getImplementedInterfaces().addAll(implInterfaces);
        
        currentScope.define(classSym);
        
        // ⋆˖⁺‧₊☽⛥Establece el ambito actual dentro de la clase⛥☾₊‧⁺˖⋆ //
        Scope saveScope = currentScope;
        currentScope = classSym;

        expect(TipoToken.LLAVEIZQ);
        while (indiceToken < tokens.size() && !getToken().getTipo().getNombre().equals(TipoToken.LLAVEDER)) {
            miembro();
        }
        expect(TipoToken.LLAVEDER);

        currentScope = saveScope;
    }

    private void miembro() throws SyntaxException {
        String accessMod = "";
        
        // ⋆˖⁺‧₊☽⛥Detecta el modificador de acceso del miembro de la clase⛥☾₊‧⁺˖⋆ //
        if (getToken().getTipo().getNombre().equals(TipoToken.PUBLIC)) {
            accessMod = "public";
            expect(TipoToken.PUBLIC);
        } else if (getToken().getTipo().getNombre().equals(TipoToken.PRIVATE)) {
            accessMod = "private";
            expect(TipoToken.PRIVATE);
        } else if (getToken().getTipo().getNombre().equals(TipoToken.PROTECTED)) {
            accessMod = "protected";
            expect(TipoToken.PROTECTED);
        }

        // ⋆˖⁺‧₊☽⛥Ignora modificadores adicionales como abstract, static y final⛥☾₊‧⁺˖⋆ //
        while (getToken().getTipo().getNombre().equals(TipoToken.ABSTRACT) || 
               (getToken().getTipo().getNombre().equals(TipoToken.VARIABLE) && 
                (getToken().getNombre().equals("static") || getToken().getNombre().equals("final")))) {
            if (getToken().getTipo().getNombre().equals(TipoToken.ABSTRACT)) {
                expect(TipoToken.ABSTRACT);
            } else {
                expect(TipoToken.VARIABLE);
            }
        }

        String typeName = getToken().getNombre();
        if (getToken().getTipo().getNombre().equals(TipoToken.VARIABLE)) {
            expect(TipoToken.VARIABLE);
        } else {
            expect(getToken().getTipo().getNombre());
        }

        // ⋆˖⁺‧₊☽⛥Soporte para tipos array como PaqueteDatos[]⛥☾₊‧⁺˖⋆ //
        while (getToken().getTipo().getNombre().equals(TipoToken.ERROR) && 
               (getToken().getNombre().equals("[") || getToken().getNombre().equals("]"))) {
            typeName += getToken().getNombre();
            indiceToken++;
        }

        String memberName = getToken().getNombre();
        expect(TipoToken.VARIABLE);

        // ⋆˖⁺‧₊☽⛥Identificar si se trata de un metodo o un atributo de la clase⛥☾₊‧⁺˖⋆ //
        if (getToken().getTipo().getNombre().equals(TipoToken.PARENTESISIZQ)) {
            expect(TipoToken.PARENTESISIZQ);
            
            List<VariableSymbol> params = new ArrayList<>();
            if (!getToken().getTipo().getNombre().equals(TipoToken.PARENTESISDER)) {
                do {
                    String pTypeName = getToken().getNombre();
                    expect(TipoToken.VARIABLE);
                    
                    // ⋆˖⁺‧₊☽⛥Soporte para tipos array en parametros como PaqueteDatos[]⛥☾₊‧⁺˖⋆ //
                    while (getToken().getTipo().getNombre().equals(TipoToken.ERROR) && 
                           (getToken().getNombre().equals("[") || getToken().getNombre().equals("]"))) {
                        pTypeName += getToken().getNombre();
                        indiceToken++;
                    }

                    String pName = getToken().getNombre();
                    expect(TipoToken.VARIABLE);
                    
                    Type pType = (Type) symbolTable.resolve(pTypeName);
                    if (pType == null) {
                        pType = new BuiltInTypeSymbol(pTypeName);
                        symbolTable.define((Symbol) pType);
                    }
                    params.add(new VariableSymbol(pName, pType));
                } while (match(TipoToken.COMA));
            }
            expect(TipoToken.PARENTESISDER);

            VariableSymbol[] paramsArray = params.toArray(new VariableSymbol[0]);
            MethodSymbol methodSym = new MethodSymbol(memberName, paramsArray, currentScope);
            methodSym.setAccessModifier(accessMod);
            
            Type retType = (Type) symbolTable.resolve(typeName);
            if (retType == null) {
                retType = new BuiltInTypeSymbol(typeName);
                symbolTable.define((Symbol) retType);
            }
            methodSym.setType(retType);

            currentScope.define(methodSym);

            consumirCuerpoMetodo();
        } else {
            Type attrType = (Type) symbolTable.resolve(typeName);
            if (attrType == null) {
                attrType = new BuiltInTypeSymbol(typeName);
                symbolTable.define((Symbol) attrType);
            }
            VariableSymbol varSym = new VariableSymbol(memberName, attrType);
            varSym.setAccessModifier(accessMod);
            currentScope.define(varSym);

            if (match(TipoToken.IGUAL)) {
                while (indiceToken < tokens.size() && !getToken().getTipo().getNombre().equals(TipoToken.PUNTOYCOMA)) {
                    indiceToken++;
                }
            }
            expect(TipoToken.PUNTOYCOMA);
        }
    }
}
