import java.util.Objects;

public class Variable extends Terminal {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Variable variable = (Variable) o;
        return tokenString.equals(variable.tokenString);
    }

    @Override
    public String toString() {
        return tokenString;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tokenString);
    }

    private String tokenString;

    public Variable(Terminal terminal) {
        super(terminal.getSymbol(), true, terminal.getTokenId());
        this.tokenString = terminal.getTokenString();
    }
}
