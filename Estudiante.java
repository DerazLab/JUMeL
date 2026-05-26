// ⋆˖⁺‧₊☽⛥Clase base para probar herencia de clases⛥☾₊‧⁺˖⋆ //
public class Persona {
    private String curp;
    protected String fechaNacimiento;

    public void mostrarDatos() {
        System.out.println("CURP: " + curp);
    }
}

// ⋆˖⁺‧₊☽⛥Clase para probar relacion de composicion⛥☾₊‧⁺˖⋆ //
public class Direccion {
    public String calle;
    private int numero;
}

// ⋆˖⁺‧₊☽⛥Clase principal que hereda de Persona y contiene Direccion⛥☾₊‧⁺˖⋆ //
public class Estudiante extends Persona {
    private int matricula;
    public String nombre;

    // Composicion/Agregacion con Direccion
    public Direccion domicilio;

    public void estudiar() {
        if (matricula > 0) {
            System.out.println(nombre + " esta estudiando.");
        }
    }
}

// ⋆˖⁺‧₊☽⛥Interfaz para probar realizacion/implementacion y estereotipo <<interface>>⛥☾₊‧⁺˖⋆ //
public interface IProtocolo {
    public void enrutarDatos();
}

// ⋆˖⁺‧₊☽⛥Clase abstracta para probar herencia y estereotipo <<abstract>>⛥☾₊‧⁺˖⋆ //
public abstract class NodoRed {
    protected String direccionIP;
    public int anchoBanda;

    public void establecerConexion() {
        System.out.println("Conectando a la red...");
    }

    public abstract void transmitir();
}

// ⋆˖⁺‧₊☽⛥Clase de soporte para composicion⛥☾₊‧⁺˖⋆ //
public class PaqueteDatos {
    private String payload;
}

// ⋆˖⁺‧₊☽⛥Clase compleja que hereda, implementa y compone⛥☾₊‧⁺˖⋆ //
public class ServidorWired extends NodoRed implements IProtocolo {
    private String hostName;
    
    // Composicion/Agregacion con PaqueteDatos
    public PaqueteDatos colaMensajes;

    public void enrutarDatos() {
        System.out.println("Enrutando datos...");
    }

    public void transmitir() {
        System.out.println("Transmitiendo paquetes...");
    }
}
