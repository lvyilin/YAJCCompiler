import java.util.Objects;

public abstract class Symbol {
    private String symbol;
    private boolean isNonterminal;
    private boolean isIdentifier;

    private String tokenString;

    public Symbol(String symbol) {
        this.symbol = symbol;
    }

    public Symbol(String symbol, boolean isNonterminal) {
        this.symbol = symbol;
        this.isNonterminal = isNonterminal;
    }

    public Symbol(String symbol, boolean isNonterminal, boolean isIdentifier) {
        this.symbol = symbol;
        this.isNonterminal = isNonterminal;
        this.isIdentifier = isIdentifier;
    }

    public Symbol(String symbol, boolean isNonterminal, boolean isIdentifier, String tokenString) {
        this.symbol = symbol;
        this.isNonterminal = isNonterminal;
        this.isIdentifier = isIdentifier;
        this.tokenString = tokenString;
    }


    public String getSymbol() {
        return symbol;
    }

    public boolean isNonterminal() {
        return isNonterminal;
    }

    public boolean isIdentifier() {
        return isIdentifier;
    }

    public boolean isEmptySymbol() {
        return isNonterminal && !isIdentifier && symbol.equals("");
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol1 = (Symbol) o;
        return isNonterminal == symbol1.isNonterminal &&
                isIdentifier == symbol1.isIdentifier &&
                Objects.equals(symbol, symbol1.symbol);
    }

    public static boolean equalSymbol(Symbol a, Symbol b) {
        return a.isNonterminal == b.isNonterminal &&
                a.isIdentifier == b.isIdentifier &&
                Objects.equals(a.symbol, b.symbol);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, isNonterminal, isIdentifier);
    }

    @Override
    public String toString() {
        if (isIdentifier) {
            return "IDENTIFIER";
        }
        if (isEmptySymbol()) {
            return "EMPTY";
        }
        return symbol;
    }

    public String getTokenString() {
        return tokenString;
    }

    public void setTokenString(String tokenString) {
        this.tokenString = tokenString;
    }

}
