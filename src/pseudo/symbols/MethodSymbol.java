package pseudo.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

public class MethodSymbol extends ScopedSymbol {
    Map<String, Symbol> members = new LinkedHashMap<String, Symbol>();

    public MethodSymbol(String name, VariableSymbol[] orderedArgs, Scope enclosingScope) {
        super(name, enclosingScope);

        if (orderedArgs != null) {
            for (VariableSymbol v : orderedArgs) {
                define(v);
            }
        }
    }

    public Map<String, Symbol> getMembers() {
        return members;
    }
}

