import java.util.Objects;

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
    }

    public Symbol(String symbol, boolean isNonterminal, boolean isIdentifer) {
        this.symbol = symbol;
        this.isNonterminal = isNonterminal;
        this.isIdentifer = isIdentifer;
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

    public boolean isEmptySymbol() {
        return isNonterminal && !isIdentifer && symbol.equals("");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return isNonterminal == symbol1.isNonterminal &&
                isIdentifer == symbol1.isIdentifer &&
                Objects.equals(symbol, symbol1.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, isNonterminal, isIdentifer);
    }

    @Override
    public String toString() {
        if (isIdentifer) {
            return "IDENTIFIER";
        }
        if (isEmptySymbol()) {
            return "EMPTY";
        }
        return symbol;
    }
}
