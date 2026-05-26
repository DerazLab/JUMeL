package pseudo.intermedio;
import java.util.ArrayList;
import pseudo.symbols.SymbolTable;

public class PseudoInterprete {
    SymbolTable ts;

    public PseudoInterprete(SymbolTable ts) {
        this.ts = ts;
    }

    public void interpretar(ArrayList<Tupla> tuplas) {
        if(tuplas.isEmpty()) return;
        
        int indiceTupla = 0;
        Tupla t = tuplas.get(0);
        
        do {
            indiceTupla = t.ejecutar(ts);
            if (indiceTupla != -1 && indiceTupla < tuplas.size()) {
                t = tuplas.get(indiceTupla);
            }
        } while (!(t instanceof FinPrograma));
    }
}