package pseudo.lexer;

public class TipoToken
{
    private String nombre;
    private String patron;

    public TipoToken(String nombre, String patron)
    {
        this.nombre = nombre;
        this.patron = patron;
    }

    public String getNombre()
    {
        return nombre;
    }

    public String getPatron()
    {
        return patron;
    }

    public static String NUMERO = "NUMERO";
    public static String CADENA = "CADENA";
    public static String OPARITMETICO = "OPARITMETICO";
    public static String OPRELACIONAL = "OPRELACIONAL";
    public static String IGUAL = "IGUAL";
    public static String COMA = "COMA";
    public static String PARENTESISIZQ = "PARENTESISIZQ";
    public static String PARENTESISDER = "PARENTESISDER";
    public static String INICIOPROGRAMA = "INICIOPROGRAMA";
    public static String FINPROGRAMA = "FINPROGRAMA";
    public static String LEER = "LEER";
    public static String ESCRIBIR = "ESCRIBIR";
    public static String SI = "SI";
    public static String ENTONCES = "ENTONCES";
    public static String FINSI = "FINSI";
    public static String MIENTRAS = "MIENTRAS";
    public static String FINMIENTRAS = "FINMIENTRAS";
    public static String VARIABLE = "VARIABLE";
    public static String ESPACIO = "ESPACIO";
    public static String ERROR = "ERROR";
    public static String VARIABLES = "VARIABLES";
    public static String REPITE = "REPITE";
    public static String FINREPITE = "FINREPITE";

    // ⋆˖⁺‧₊☽⛥Tokens de Java para la generacion de diagramas UML⛥☾₊‧⁺˖⋆ //
    public static String PUBLIC = "PUBLIC";
    public static String PRIVATE = "PRIVATE";
    public static String PROTECTED = "PROTECTED";
    public static String CLASS = "CLASS";
    public static String EXTENDS = "EXTENDS";
    public static String LLAVEIZQ = "LLAVEIZQ";
    public static String LLAVEDER = "LLAVEDER";
    public static String PUNTOYCOMA = "PUNTOYCOMA";
    
    // ⋆˖⁺‧₊☽⛥Tokens para interfaces y clases abstractas⛥☾₊‧⁺˖⋆ //
    public static String INTERFACE = "INTERFACE";
    public static String IMPLEMENTS = "IMPLEMENTS";
    public static String ABSTRACT = "ABSTRACT";
}


