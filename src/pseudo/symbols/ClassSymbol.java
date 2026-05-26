package pseudo.symbols;

import java.util.LinkedHashMap;
import java.util.Map;

public class ClassSymbol extends ScopedSymbol implements Scope, Type {
    ClassSymbol superClass;
    public Map<String, Symbol> members = new LinkedHashMap<String, Symbol>();
    
    // ⋆˖⁺‧₊☽⛥Propiedades para modelar clases abstractas e interfaces⛥☾₊‧⁺˖⋆ //
    private boolean isInterface = false;
    private boolean isAbstract = false;
    private java.util.List<ClassSymbol> implementedInterfaces = new java.util.ArrayList<>();

    public ClassSymbol(String name, Scope enclosingScope, ClassSymbol superClass) {
        super(name, enclosingScope);
        this.superClass = superClass;
    }

    public boolean isInterface() { return isInterface; }
    public void setInterface(boolean isInterface) { this.isInterface = isInterface; }

    public boolean isAbstract() { return isAbstract; }
    public void setAbstract(boolean isAbstract) { this.isAbstract = isAbstract; }

    public java.util.List<ClassSymbol> getImplementedInterfaces() { return implementedInterfaces; }

    public Scope getParentScope() {
        if (superClass == null)
            return enclosingScope;
        return superClass;
    }

    public Symbol resolveMember(String name) {
        Symbol s = members.get(name);
        if (s != null)
            return s;

        if (superClass != null) {
            return superClass.resolveMember(name);
        }
        return null;
    }

    public Map<String, Symbol> getMembers() {
        return members;
    }

    public String toString() {
        return "class " + name + ":{" + members.keySet().toString() + "}";
    }
}

