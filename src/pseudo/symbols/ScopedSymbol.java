package pseudo.symbols;

import java.util.Map;

public abstract class ScopedSymbol extends Symbol implements Scope {
    Scope enclosingScope;

    public ScopedSymbol(String name, Type type, Scope enclosingScope) {
        super(name, type);
        this.enclosingScope = enclosingScope;
    }

    public ScopedSymbol(String name, Scope enclosingScope) {
        super(name);
        this.enclosingScope = enclosingScope;
    }

    public Symbol resolve(String name) {
        Symbol s = getMembers().get(name);
        if (s != null)
            return s;

        // Si no está aquí, revisa el alcance padre/exterior
        if (getParentScope() != null) {
            return getParentScope().resolve(name);
        }
        return null; // No encontrado
    }

    public Symbol resolveType(String name) {
        return resolve(name);
    }

    public void define(Symbol sym) {
        getMembers().put(sym.name, sym);
        sym.scope = this; // Rastrear el alcance en cada símbolo
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public Scope getParentScope() {
        return getEnclosingScope();
    }

    public String getScopeName() {
        return name;
    }

    // Método abstracto para permitir que las subclases definan cómo guardar los
    // miembros
    public abstract Map<String, Symbol> getMembers();
}

