// ⋆˖⁺‧₊☽⛥Clase base para probar herencia⛥☾₊‧⁺˖⋆ //
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
        // contenido del metodo con llaves internas
        if (matricula > 0) {
            System.out.println(nombre + " esta estudiando.");
        }
    }
}
