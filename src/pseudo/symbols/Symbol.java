package pseudo.symbols;

public class Symbol {
    String name;
    Type type;
    Scope scope;
    
    // ⋆˖⁺‧₊☽⛥Almacena el modificador de acceso de Java: public, private, protected, o vacio⛥☾₊‧⁺˖⋆ //
    private String accessModifier = "";

    public Symbol(String name) {
        this.name = name;
    }

    public Symbol(String name, Type type) {
        this(name);
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getAccessModifier() {
        return accessModifier;
    }

    public void setAccessModifier(String accessModifier) {
        this.accessModifier = accessModifier;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String toString() {
        if (type != null)
            return '<' + getName() + ":" + type + '>';

        return getName();
    }

}

