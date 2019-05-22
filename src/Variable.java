import java.util.Objects;

public class Variable extends Symbol {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Variable variable = (Variable) o;
        return super.getTokenString().equals(variable.getTokenString());
    }

    @Override
    public String toString() {
        return getTokenString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getTokenString());
    }


    public Variable(Symbol symbol) {
        super(symbol.getSymbol(), symbol.isNonterminal(), symbol.isIdentifier(), symbol.getTokenString());
    }

    public Variable(Symbol symbol, String tokenString) {
        super(symbol.getSymbol(), symbol.isNonterminal(), symbol.isIdentifier(), tokenString);
    }
}
