package pseudo.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

public class StructSymbol extends ScopedSymbol implements Type {
    Map<String, Symbol> fields = new LinkedHashMap<String, Symbol>();

    public StructSymbol(String name, Scope parent) {
        super(name, parent);
    }

    public Symbol resolveMember(String name) {
        return fields.get(name);
    }

    public Map<String, Symbol> getMembers() {
        return fields;
    }

    public String toString() {
        return "struct " + name + ":{" + fields.keySet().toString() + "}";
    }
}

