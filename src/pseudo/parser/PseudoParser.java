package pseudo.parser;

import java.util.ArrayList;
import java.util.List;
import pseudo.lexer.*;
import pseudo.symbols.*;
import pseudo.intermedio.*;
import pseudo.exceptions.*;

// •───⋅⋆⁺‧₊☽⛦ Analizador sintactico ⛦☾₊‧⁺⋆⋅───•
public class PseudoParser {
    private ArrayList<Token> tokens;
    private int indiceToken = 0;
    
    // Variables para semantica y traduccion
    private SymbolTable symbolTable;
    private PseudoGenerador generador;

    private List<String> errors;
    /*
    private StringBuilder cScript;
    private StringBuilder pyScript;
    private int indentLevel;

    
    public PseudoParser() {
        this.symbolTable = new SymbolTable();
        this.errors = new ArrayList<>();
        this.cScript = new StringBuilder();
        this.pyScript = new StringBuilder();
        this.indentLevel = 0;
    }
    */

    public PseudoParser(SymbolTable ts, PseudoGenerador generador) {
        this.symbolTable = ts;
        this.generador = generador;
        this.errors = new ArrayList<>();
    }


    public void analizar(PseudoLexer lexer) throws SyntaxException {
        this.tokens = lexer.getTokens();
        this.indiceToken = 0;

        if (tokens == null || tokens.isEmpty()) return;

        programa();

        if (!errors.isEmpty()) {
            System.out.println("\n--- Errores Semanticos ---");
            for (String err : errors) {
                System.out.println(err);
            }
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

    private void registrarVariable(String nombre) {
        if (symbolTable.resolve(nombre) == null) {
            BuiltInTypeSymbol tipo = (BuiltInTypeSymbol) symbolTable.resolve("float");
            symbolTable.define(new VariableSymbol(nombre, tipo));
        }
    }

    /*
    private String getIndentation(int level) {
        StringBuilder spaces = new StringBuilder();
        for (int i = 0; i < level; i++) {
            spaces.append("    ");
        }
        return spaces.toString();
    }
    */

    // ================= REGLAS GRAMATICALES =================

    private void programa() throws SyntaxException {
        expect(TipoToken.INICIOPROGRAMA);
        declaracionVariables();
        sentencias();
        expect(TipoToken.FINPROGRAMA);
        generador.crearTuplaFinPrograma();
    }

    private void declaracionVariables() throws SyntaxException {
        if (match(TipoToken.VARIABLES)) {
            do {
                String varName = getToken().getNombre();
                expect(TipoToken.VARIABLE);
                BuiltInTypeSymbol tipo = (BuiltInTypeSymbol) symbolTable.resolve("float");
                if (symbolTable.resolve(varName) == null) {
                    symbolTable.define(new VariableSymbol(varName, tipo));
                }
            } while (match(TipoToken.COMA));
        }
    }

    private void sentencias() throws SyntaxException {
        while (indiceToken < tokens.size() &&
               !getToken().getTipo().getNombre().equals(TipoToken.FINPROGRAMA) &&
               !getToken().getTipo().getNombre().equals(TipoToken.FINSI) &&
               !getToken().getTipo().getNombre().equals(TipoToken.FINMIENTRAS) &&
               !getToken().getTipo().getNombre().equals(TipoToken.FINREPITE)) {
            sentencia();
        }
    }

    private void sentencia() throws SyntaxException {
        String tipoActual = getToken().getTipo().getNombre();
        
        if (tipoActual.equals(TipoToken.LEER)) {
            sentenciaLeer();
        } else if (tipoActual.equals(TipoToken.ESCRIBIR)) {
            sentenciaEscribir();
        } else if (tipoActual.equals(TipoToken.SI)) {
            sentenciaIf();
        } else if (tipoActual.equals(TipoToken.MIENTRAS)) {
            sentenciaMientras();
        } else if (tipoActual.equals(TipoToken.REPITE)) {
            sentenciaRepite();
        } else if (tipoActual.equals(TipoToken.VARIABLE)) {
            sentenciaAsignacion();
        } else {
            throw new SyntaxException("Sentencia no reconocida: " + getToken().getNombre());
        }
    }

    private void sentenciaAsignacion() throws SyntaxException {
        int indiceAux = indiceToken;
        String varName = getToken().getNombre();
        registrarVariable(varName);
        expect(TipoToken.VARIABLE);
        expect(TipoToken.IGUAL);
        expresion();
        
        generador.crearTuplaAsignacion(indiceAux, indiceToken); 
    }

    private void sentenciaLeer() throws SyntaxException {
        int indiceAux = indiceToken;
        expect(TipoToken.LEER);
        String varName = getToken().getNombre();
        registrarVariable(varName);
        expect(TipoToken.VARIABLE);
        generador.crearTuplaLeer(indiceAux + 1);
    }

    private void sentenciaEscribir() throws SyntaxException {
        int indiceAux = indiceToken;
        expect(TipoToken.ESCRIBIR);
        do {
            if (getToken().getTipo().getNombre().equals(TipoToken.CADENA)) {
                expect(TipoToken.CADENA);
            } else if (getToken().getTipo().getNombre().equals(TipoToken.VARIABLE)) {
                String varName = getToken().getNombre();
                registrarVariable(varName);
                expect(TipoToken.VARIABLE);
            }
        } while (match(TipoToken.COMA));
        
        generador.crearTuplaEscribir(indiceAux + 1, indiceToken);
    }

    private void sentenciaIf() throws SyntaxException {
        expect(TipoToken.SI);
        int indiceTupla = generador.getTuplas().size();
        
        condicion();
        expect(TipoToken.ENTONCES);
        sentencias();
        expect(TipoToken.FINSI);
        
        generador.conectarSi(indiceTupla);
    }

    private void sentenciaMientras() throws SyntaxException {
        expect(TipoToken.MIENTRAS);
        int indiceTupla = generador.getTuplas().size();
        
        expect(TipoToken.PARENTESISIZQ);
        condicion();
        expect(TipoToken.PARENTESISDER);
        
        sentencias();
        expect(TipoToken.FINMIENTRAS);
        
        generador.conectarMientras(indiceTupla);
    }

    private void sentenciaRepite() throws SyntaxException {
        expect(TipoToken.REPITE);
        expect(TipoToken.PARENTESISIZQ);
        Token variableIndice = getToken();
        registrarVariable(variableIndice.getNombre());
        expect(TipoToken.VARIABLE);
        expect(TipoToken.COMA);
        Token valorInicial = getToken();
        avanzarValor();
        expect(TipoToken.COMA);
        Token valorFinal = getToken();
        avanzarValor();
        expect(TipoToken.PARENTESISDER);

        // Inicializacion
        generador.getTuplas().add(new Asignacion(variableIndice, valorInicial, generador.getTuplas().size() + 2, generador.getTuplas().size() + 2));
        
        // Comparacion
        int indiceTuplaComparacion = generador.getTuplas().size();
        Token opMenorIgual = new Token(new TipoToken(TipoToken.OPRELACIONAL, "<="), "<=");
        generador.getTuplas().add(new Comparacion(variableIndice, opMenorIgual, valorFinal, generador.getTuplas().size() + 2, generador.getTuplas().size() + 2));

        sentencias();

        // Incremento
        Token opSuma = new Token(new TipoToken(TipoToken.OPARITMETICO, "+"), "+");
        Token numUno = new Token(new TipoToken(TipoToken.NUMERO, "1"), "1");
        generador.getTuplas().add(new Asignacion(variableIndice, variableIndice, opSuma, numUno, generador.getTuplas().size() + 2, generador.getTuplas().size() + 2));

        expect(TipoToken.FINREPITE);
        generador.conectarMientras(indiceTuplaComparacion);
    }

    private void condicion() throws SyntaxException {
        int indiceAux = indiceToken;
        avanzarValor();
        expect(TipoToken.OPRELACIONAL);
        avanzarValor();
        generador.crearTuplaComparacion(indiceAux);
    }

    private void expresion() throws SyntaxException {
        termino();
        while (getToken().getTipo().getNombre().equals(TipoToken.OPARITMETICO) && 
              (getToken().getNombre().equals("+") || getToken().getNombre().equals("-"))) {
            expect(TipoToken.OPARITMETICO);
            termino();
        }
    }

    private void termino() throws SyntaxException {
        factor();
        while (getToken().getTipo().getNombre().equals(TipoToken.OPARITMETICO) && 
              (getToken().getNombre().equals("*") || getToken().getNombre().equals("/"))) {
            expect(TipoToken.OPARITMETICO);
            factor();
        }
    }

    private void factor() throws SyntaxException {
        if (match(TipoToken.PARENTESISIZQ)) {
            expresion();
            expect(TipoToken.PARENTESISDER);
        } else {
            avanzarValor();
        }
    }

    private void avanzarValor() throws SyntaxException {
        if (getToken().getTipo().getNombre().equals(TipoToken.VARIABLE)) {
            registrarVariable(getToken().getNombre());
            expect(TipoToken.VARIABLE);
        } else if (getToken().getTipo().getNombre().equals(TipoToken.NUMERO)) {
            expect(TipoToken.NUMERO);
        } else {
            throw new SyntaxException("Se esperaba VARIABLE o NUMERO");
        }
    }

    /*
    private void verificarVariable(String varName) {
        if (symbolTable.resolve(varName) == null) {
            errors.add("Error semantico: Variable no declarada '" + varName + "'");
        }
    }

    private void verificarSiEsVariable(String tokenName) {
        if (getToken().getTipo().getNombre().equals(TipoToken.VARIABLE)) {
            verificarVariable(tokenName);
        }
    }
    */
}

