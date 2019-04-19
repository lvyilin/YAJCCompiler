import java.util.ArrayList;

public class ProductionRule {
    private Nonterminal nonterminal;
    private ArrayList<SymbolString> symbolStrings;

    public ArrayList<SymbolString> getSymbolStrings() {
        return symbolStrings;
    }


    public ProductionRule(Nonterminal nonterminal, ArrayList<SymbolString> symbolStrings) {
        this.nonterminal = nonterminal;
        this.symbolStrings = symbolStrings;
    }
}
