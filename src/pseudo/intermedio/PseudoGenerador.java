package pseudo.intermedio;
import java.util.ArrayList;
import pseudo.lexer.Token;

public class PseudoGenerador {
    private ArrayList<Tupla> tuplas = new ArrayList<>();
    private ArrayList<Token> tokens;

    public PseudoGenerador(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public void crearTuplaAsignacion(int indiceInicial, int indiceFinal) {
        if (indiceFinal - indiceInicial == 3) {
            tuplas.add(new Asignacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tuplas.size() + 2, tuplas.size() + 2));
        } else if (indiceFinal - indiceInicial == 5) {
            tuplas.add(new Asignacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tokens.get(indiceInicial + 3), tokens.get(indiceInicial + 4), tuplas.size() + 2, tuplas.size() + 2));
        }
    }

    public void crearTuplaLeer(int indiceInicial) {
        tuplas.add(new Leer(tokens.get(indiceInicial), tuplas.size() + 2, tuplas.size() + 2));
    }

    public void crearTuplaEscribir(int indiceInicial, int indiceFinal) {
        if (indiceFinal - indiceInicial == 1) {
            tuplas.add(new Escribir(tokens.get(indiceInicial), tuplas.size() + 2, tuplas.size() + 2));
        } else if (indiceFinal - indiceInicial == 3) {
            tuplas.add(new Escribir(tokens.get(indiceInicial), tokens.get(indiceInicial + 2), tuplas.size() + 2, tuplas.size() + 2));
        }
    }

    public void crearTuplaComparacion(int indiceInicial) {
        tuplas.add(new Comparacion(tokens.get(indiceInicial), tokens.get(indiceInicial + 1), tokens.get(indiceInicial + 2), tuplas.size() + 2, tuplas.size() + 2));
    }

    public void crearTuplaFinPrograma() {
        tuplas.add(new FinPrograma());
    }

    public void conectarSi(int tuplaInicial) {
        if (tuplaInicial >= tuplas.size()) return;
        tuplas.get(tuplaInicial).setSaltoFalso(tuplas.size() + 1);
    }

    public void conectarMientras(int tuplaInicial) {
        int tuplaFinal = tuplas.size() - 1;
        if (tuplaInicial >= tuplas.size() || tuplaInicial >= tuplaFinal) return;
        
        tuplas.get(tuplaInicial).setSaltoFalso(tuplas.size() + 1);
        tuplas.get(tuplaFinal).setSaltoVerdadero(tuplaInicial + 1);
        tuplas.get(tuplaFinal).setSaltoFalso(tuplaInicial + 1);
    }

    public ArrayList<Tupla> getTuplas() {
        return tuplas;
    }
}