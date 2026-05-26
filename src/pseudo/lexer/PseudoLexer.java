package pseudo.lexer;

import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import pseudo.exceptions.*;

public class PseudoLexer {
    private ArrayList<TipoToken> tipos = new ArrayList<>();
    private ArrayList<Token> tokens = new ArrayList<>();

    public PseudoLexer() {
        // ⋆˖⁺‧₊☽⛥Registramos comentarios al inicio para evitar que '/' sea confundido con un operador aritmetico⛥☾₊‧⁺˖⋆ //
        tipos.add(new TipoToken("COMENTARIO", "//.*|/\\*(?s).*?\\*/"));

        tipos.add(new TipoToken(TipoToken.NUMERO, "-?[0-9]+(\\.[0-9]+)?"));
        tipos.add(new TipoToken(TipoToken.CADENA, "\".*\""));
        tipos.add(new TipoToken(TipoToken.OPARITMETICO, "[*/+-]"));
        tipos.add(new TipoToken(TipoToken.OPRELACIONAL, "<=|>=|==|<|>|!="));
        tipos.add(new TipoToken(TipoToken.IGUAL, "="));
        tipos.add(new TipoToken(TipoToken.COMA, ","));
        tipos.add(new TipoToken(TipoToken.PARENTESISIZQ, "\\("));
        tipos.add(new TipoToken(TipoToken.PARENTESISDER, "\\)"));
        tipos.add(new TipoToken(TipoToken.INICIOPROGRAMA, "inicio-programa"));
        tipos.add(new TipoToken(TipoToken.FINPROGRAMA, "fin-programa"));
        tipos.add(new TipoToken(TipoToken.LEER, "leer"));
        tipos.add(new TipoToken(TipoToken.ESCRIBIR, "escribir"));
        tipos.add(new TipoToken(TipoToken.SI, "si"));
        tipos.add(new TipoToken(TipoToken.ENTONCES, "entonces"));
        tipos.add(new TipoToken(TipoToken.FINSI, "fin-si"));
        tipos.add(new TipoToken(TipoToken.MIENTRAS, "mientras"));
        tipos.add(new TipoToken(TipoToken.FINMIENTRAS, "fin-mientras"));
        //variables
        tipos.add(new TipoToken(TipoToken.VARIABLES, "variables *:"));
        tipos.add(new TipoToken(TipoToken.REPITE, "repite"));
        tipos.add(new TipoToken(TipoToken.FINREPITE, "fin-repite"));
        
        // ⋆˖⁺‧₊☽⛥Registramos las palabras reservadas y simbolos de Java antes del patron general de VARIABLE⛥☾₊‧⁺˖⋆ //
        tipos.add(new TipoToken(TipoToken.PUBLIC, "public\\b"));
        tipos.add(new TipoToken(TipoToken.PRIVATE, "private\\b"));
        tipos.add(new TipoToken(TipoToken.PROTECTED, "protected\\b"));
        tipos.add(new TipoToken(TipoToken.CLASS, "class\\b"));
        tipos.add(new TipoToken(TipoToken.EXTENDS, "extends\\b"));
        tipos.add(new TipoToken(TipoToken.LLAVEIZQ, "\\{"));
        tipos.add(new TipoToken(TipoToken.LLAVEDER, "\\}"));
        tipos.add(new TipoToken(TipoToken.PUNTOYCOMA, ";"));
        
        tipos.add(new TipoToken(TipoToken.VARIABLE, "[a-zA-Z_][a-zA-Z0-9_]*"));
        tipos.add(new TipoToken(TipoToken.ESPACIO, "[ \t\f\r\n]+"));
        tipos.add(new TipoToken(TipoToken.ERROR, "[^ \t\f\n]+"));
        
    }

    public ArrayList<Token> getTokens()
    {
        return tokens;
    }

    public void analizar(String entrada) throws LexicalException
    {
        StringBuffer er = new StringBuffer();

        for (TipoToken tt: tipos)
            er.append(String.format("|(?<%s>%s)", tt.getNombre().equals("COMENTARIO") ? "COMENTARIO" : tt.getNombre(), tt.getPatron()));

        Pattern p = Pattern.compile(new String(er.substring(1)));
        Matcher m = p.matcher(entrada);

        while (m.find())
        {
            for (TipoToken tt: tipos)
            {
                // ⋆˖⁺‧₊☽⛥Ignoramos espacios en blanco y comentarios de forma directa⛥☾₊‧⁺˖⋆ //
                if (m.group(TipoToken.ESPACIO) != null)
                    continue;
                else if (m.group("COMENTARIO") != null)
                    continue;
                else if (m.group(tt.getNombre()) != null)
                {
                    String nombre = m.group(tt.getNombre());

                    if (tt.getNombre().equals(TipoToken.CADENA))
                    {
                        nombre = nombre.substring(1, nombre.length()-1);
                    }

                    tokens.add(new Token(tt, nombre));
                    break;
                }
            }
        }
    }

}

