import java.util.ArrayList;

public class SymbolString {
    private ArrayList<Symbol> symbols;

    public SymbolString(ArrayList<Symbol> symbols) {
        this.symbols = symbols;
    }

    public ArrayList<Symbol> getSymbols() {
        return symbols;
    }

    public boolean isEmptySymbolString() {
        return symbols.isEmpty();
    }
}
