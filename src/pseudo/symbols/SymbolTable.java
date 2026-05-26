package pseudo.symbols;

import java.util.*;

public class SymbolTable implements Scope 
{ 
    Map<String, Symbol> symbols = new HashMap<String, Symbol>();
    
    public SymbolTable() 
    { 
        initTypeSystem(); 
    }
    
    protected void initTypeSystem() 
    {
        define(new BuiltInTypeSymbol("int"));
        define(new BuiltInTypeSymbol("float"));
        
        // ⋆˖⁺‧₊☽⛥Tipos de datos comunes en Java⛥☾₊‧⁺˖⋆ //
        define(new BuiltInTypeSymbol("double"));
        define(new BuiltInTypeSymbol("boolean"));
        define(new BuiltInTypeSymbol("String"));
        define(new BuiltInTypeSymbol("void"));
    }
    
    public String getScopeName() 
    { 
        return "global"; 
    }
    
    public Scope getEnclosingScope() 
    { 
        return null; 
    }
    
    public void define(Symbol sym) 
    { 
        symbols.put(sym.name, sym); 
    }
    
    public Symbol resolve(String name) 
    { 
        return symbols.get(name); 
    }
    
    @Override
    public Scope getParentScope() {
        return null;
    }
    
    // ⋆˖⁺‧₊☽⛥Retorna el mapa de todos los simbolos registrados en el ambito global⛥☾₊‧⁺˖⋆ //
    public Map<String, Symbol> getSymbols() {
        return symbols;
    }

    public String toString() 
    { 
        return getScopeName() + ":" + symbols; 
    }
}

