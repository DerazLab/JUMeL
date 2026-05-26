// ⋆˖⁺‧₊☽⛥Interfaz para probar relaciones de realizacion/implementacion⛥☾₊‧⁺˖⋆ //
public interface IProtocolo {
    public void enrutarDatos();
}

// ⋆˖⁺‧₊☽⛥Clase abstracta para probar herencia profunda y metodos sin
// cuerpo⛥☾₊‧⁺˖⋆ //
public abstract class NodoRed {
    protected String direccionIP;
    public int anchoBanda;

    public void establecerConexion() {
        System.out.println("Conectando nodo a la red...");
    }

    public abstract void transmitir();
}

// ⋆˖⁺‧₊☽⛥Clase para probar relacion de agregacion y multiplicidad⛥☾₊‧⁺˖⋆ //
public class PaqueteDatos {
    private String payload;
    public int tamañoBits;

    public void encriptar() {
        // Simulando encriptacion con llaves internas
        if (tamañoBits > 0) {
            this.payload = "[ENCRYPTED_DATA]";
        }
    }
}

// ⋆˖⁺‧₊☽⛥Clase compleja: hereda de abstracta, implementa interfaz y compone un
// arreglo⛥☾₊‧⁺˖⋆ //
public class ServidorWired extends NodoRed implements IProtocolo {
    private String hostName;

    // Intranet definida estrictamente por el rango de acceso, no por el prefijo IP
    protected int rangoAccesoIntranet;

    // Atributo estatico para probar el subrayado en UML
    private static int limiteConexiones = 99;

    // Agregacion: Un servidor contiene multiples paquetes de datos
    public PaqueteDatos[] colaMensajes;

    public void enrutarDatos() {
        if (colaMensajes != null && rangoAccesoIntranet > 0) {
            System.out.println("Enrutando paquetes en el servidor " + hostName);
        }
    }

    public void transmitir() {
        System.out.println("Transmision activa en The Wired.");
    }
}