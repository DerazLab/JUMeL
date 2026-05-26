package pseudo.symbols;
import pseudo.exceptions.SemanticException;

public class AlcanceAnidado {
    public static void main(String[] args) throws SemanticException {
        Scope currentScope;

        currentScope = new GlobalScope();

        currentScope.define(new BuiltInTypeSymbol("int"));
        currentScope.define(new BuiltInTypeSymbol("float"));
        currentScope.define(new BuiltInTypeSymbol("void"));

        // int i = 9
        BuiltInTypeSymbol t = (BuiltInTypeSymbol) currentScope.resolve("int");
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'int'");
        }
        currentScope.define(new VariableSymbol("i", t));

        // float f(int x, float y)
        BuiltInTypeSymbol rt = (BuiltInTypeSymbol) currentScope.resolve("float");
        if (rt == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }

        t = (BuiltInTypeSymbol) currentScope.resolve("int"); // int x
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'int'");
        }

        BuiltInTypeSymbol t2 = (BuiltInTypeSymbol) currentScope.resolve("float"); // float y
        if (t2 == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }

        VariableSymbol[] arguments = { new VariableSymbol("x", t), new VariableSymbol("y", t2)};

        MethodSymbol m = new MethodSymbol("f", arguments, currentScope);
        currentScope.define(m);
        currentScope = m;

        // int x
        t = (BuiltInTypeSymbol) currentScope.resolve("int");
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

        currentScope = new LocalScope(currentScope);

        // float i
        t = (BuiltInTypeSymbol) currentScope.resolve("float");
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }
        currentScope.define(new VariableSymbol("i", t));

        currentScope = new LocalScope(currentScope);

        // float z = x + y
        t = (BuiltInTypeSymbol) currentScope.resolve("float");
        if (t == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }
        currentScope.define(new VariableSymbol("z", t));

        Symbol s = currentScope.resolve("x");
        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'x'");
        }

        s = currentScope.resolve("y");
        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'y'");
        }

        // i = z
        s = currentScope.resolve("i");
        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'i'");
        }

        s = currentScope.resolve("z");
        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'z'");
        }

        currentScope = currentScope.getEnclosingScope(); // Sale del bloque

        currentScope = new LocalScope(currentScope);

        t = (BuiltInTypeSymbol) currentScope.resolve("float"); // float z = i + 1

        if (t == null) {
            throw new SemanticException("Cannot resolve type 'float'");
        }

        currentScope.define(new VariableSymbol("z", t));

        s = currentScope.resolve("i");

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'i'");
        }

        s = currentScope.resolve("i"); // i = z

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'i'");
        }

        s = currentScope.resolve("z");

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'z'");
        }

        currentScope = currentScope.getEnclosingScope(); // Sale del bloque

        s = currentScope.resolve("i"); // return i

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'i'");
        }

        currentScope = currentScope.getEnclosingScope(); // Sale del bloque
        currentScope = currentScope.getEnclosingScope(); // Sale del metodo f

        // void g()
        rt = (BuiltInTypeSymbol) currentScope.resolve("void");

        if (rt == null) {
            throw new SemanticException("Cannot resolve type 'void'");
        }

        m = new MethodSymbol("g", null, currentScope);

        currentScope.define(m);

        currentScope = m;

        currentScope = new LocalScope(currentScope);

        s = currentScope.resolve("f"); // f(i,2)

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'f'");
        }

        s = currentScope.resolve("i");

        if (s == null) {
            throw new SemanticException("Cannot resolve symbol 'i'");
        }

        currentScope = currentScope.getEnclosingScope(); // Sale del bloque
        currentScope = currentScope.getEnclosingScope(); // Sale del metodo g

        System.out.println("Todos los simbolos resueltos.");
    }
}

