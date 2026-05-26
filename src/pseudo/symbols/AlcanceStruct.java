package pseudo.symbols;
import pseudo.exceptions.SemanticException;

public class AlcanceStruct {
    public static void main(String[] args) throws SemanticException {
        Scope currentScope;

        currentScope = new GlobalScope();
        currentScope.define(new BuiltInTypeSymbol("int"));
        currentScope.define(new BuiltInTypeSymbol("float"));
        currentScope.define(new BuiltInTypeSymbol("void"));

        // Struct A
        StructSymbol ss = new StructSymbol("A", currentScope);
        currentScope.define(ss);
        currentScope = ss;

        // int x
        BuiltInTypeSymbol t = (BuiltInTypeSymbol) currentScope.resolve("int");
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'int'");
        }
        currentScope.define(new VariableSymbol("x", t));

        // float y
        t = (BuiltInTypeSymbol) currentScope.resolve("float");
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }
        currentScope.define(new VariableSymbol("y", t));

        currentScope = currentScope.getEnclosingScope(); // Sale del struct

        // void f()
        BuiltInTypeSymbol rt = (BuiltInTypeSymbol) currentScope.resolve("void");
        if (rt == null) {
            throw new SemanticException("Cannot resolve type 'void'");
        }
        MethodSymbol m = new MethodSymbol("f", null, currentScope);
        currentScope.define(m);
        currentScope = m;

        currentScope = new LocalScope(currentScope);

        // A a
        ss = (StructSymbol) currentScope.resolve("A");
        if (ss == null) {
            throw new SemanticException("Cannot resolve type 'A'");
        }
        currentScope.define(new VariableSymbol("a", ss));

        // a.x = 1;
        VariableSymbol v = (VariableSymbol) currentScope.resolve("a");
        if (v == null) {
            throw new SemanticException("Cannot resolve symbol 'a'");
        }

        // obtenemos el tipo de 'a'
        ss = (StructSymbol) v.type;

        // resolvemos 'x' dentro del struct
        v = (VariableSymbol) ss.resolveMember("x");
        if (v == null) {
            throw new SemanticException("Cannot resolve member 'x' in struct 'A'");
        }

        currentScope = currentScope.getEnclosingScope();
        currentScope = currentScope.getEnclosingScope();

        System.out.println("Todos los simbolos y agregaciones resueltos correctamente.");
    }
}

