package pseudo.symbols;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseScope implements Scope {
    Scope enclosingScope;
    Map<String, Symbol> members = new HashMap<String, Symbol>();

    public BaseScope(Scope currentScope) {
        this.enclosingScope = currentScope;
    }

    public Scope getEnclosingScope() {
        return enclosingScope;
    }

    public void define(Symbol sym) {
        members.put(sym.getName(), sym);
        sym.scope = this;
    }

    public Symbol resolve(String name) {
        Symbol s = members.get(name);
        if (s != null)
            return s;

        if (getParentScope() != null)
            return getParentScope().resolve(name);

        return null;
    }

    @Override
    public Scope getParentScope() {
        return getEnclosingScope();
    }
}

