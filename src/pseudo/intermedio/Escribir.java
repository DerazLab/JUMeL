package pseudo.intermedio;

import pseudo.lexer.Token;
import pseudo.lexer.TipoToken;
import pseudo.symbols.SymbolTable;
import pseudo.symbols.VariableSymbol;

public class Escribir extends Tupla {
    Token cadena, variable;

    public Escribir(Token variableCadena, int sv, int sf) {
        super(sv, sf);
        if (variableCadena.getTipo().getNombre().equals(TipoToken.CADENA)) {
            this.cadena = variableCadena;
        } else {
            this.variable = variableCadena;
        }
    }

    public Escribir(Token cadena, Token variable, int sv, int sf) {
        super(sv, sf);
        this.cadena = cadena;
        this.variable = variable;
    }

    public String toString() {
        if (variable == null)
            return "(" + super.toString() + ", [" + cadena + "])";
        if (cadena == null)
            return "(" + super.toString() + ", [" + variable + "])";
        
        return "(" + super.toString() + ", [" + cadena + ", " + variable + "])";
    }

    public int ejecutar(SymbolTable ts) {
        if (cadena == null) {
            VariableSymbol v = (VariableSymbol) ts.resolve(variable.getNombre());
            float valor = v.getValor();
            System.out.println(valor);
        } else if (variable == null) {
            System.out.println(cadena.getNombre());
        } else {
            VariableSymbol v = (VariableSymbol) ts.resolve(variable.getNombre());
            float valor = v.getValor();
            System.out.println(cadena.getNombre() + valor);
        }
        return saltoVerdadero - 1;
    }
}