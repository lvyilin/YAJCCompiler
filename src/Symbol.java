public class Symbol {
    private String symbol;
    private boolean isNonterminal;
    private boolean isIdentifer;

    public Symbol(String symbol) {
        this.symbol = symbol;
    }

    public Symbol(String symbol, boolean isNonterminal) {
        this.symbol = symbol;
        this.isNonterminal = isNonterminal;
        if (!isNonterminal && symbol == null) {
            isIdentifer = true;
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public boolean isNonterminal() {
        return isNonterminal;
    }

    public boolean isIdentifer() {
        return isIdentifer;
    }
}
