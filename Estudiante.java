// ⋆˖⁺‧₊☽⛥ Clase base que representa a cualquier persona en la comunidad universitaria ⛥☾₊‧⁺˖⋆ //
public class Persona {
    private String curp;
    protected String fechaNacimiento;

    public void mostrarDatos() {
        System.out.println("CURP: " + curp);
    }
}

// ⋆˖⁺‧₊☽⛥ Clase que representa la ubicacion física de una persona ⛥☾₊‧⁺˖⋆ //
public class Direccion {
    public String calle;
    private int numero;
}

// ⋆˖⁺‧₊☽⛥ Clase que representa un estudiante, heredando de Persona y compuesto
// por Direccion ⛥☾₊‧⁺˖⋆ //
public class Estudiante extends Persona {
    private int matricula;
    public String nombre;

    // Relacion de composicion con Direccion
    public Direccion domicilio;

    public void estudiar() {
        if (matricula > 0) {
            System.out.println(nombre + " esta estudiando.");
        }
    }
}

// ⋆˖⁺‧₊☽⛥ Interfaz para la comunicacion en la red del campus ⛥☾₊‧⁺˖⋆ //
public interface IProtocolo {
    public void enrutarDatos();
}

// ⋆˖⁺‧₊☽⛥ Clase abstracta que modela cualquier nodo conectado a la red ⛥☾₊‧⁺˖⋆
// //
public abstract class NodoRed {
    protected String direccionIP;
    public int anchoBanda;

    public void establecerConexion() {
        System.out.println("Conectando a la red del campus...");
    }

    public abstract void transmitir();
}

// ⋆˖⁺‧₊☽⛥ Estructura de datos simulada para transferir informacion en la red
// ⛥☾₊‧⁺˖⋆ //
public class PaqueteDatos {
    private String payload;
}

// ⋆˖⁺‧₊☽⛥ Servidor que gestiona las sesiones académicas y de red ⛥☾₊‧⁺˖⋆ //
public class ServidorWired extends NodoRed implements IProtocolo {
    private String hostName;

    // Relacion de composicion con PaqueteDatos
    public PaqueteDatos colaMensajes;

    public Estudiante estudianteSesion;

    public void enrutarDatos() {
        System.out.println("Enrutando datos de la sesion del estudiante...");
    }

    public void transmitir() {
        System.out.println("Transmitiendo paquetes de datos académicos...");
    }
}
