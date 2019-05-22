import java.util.Objects;

public class Terminal extends Symbol {
    public static final Terminal IDENTIFIER = new Terminal(null, true, GrammarConsts.ID);
    public static final Terminal END = new Terminal("#");
    public static final Terminal EMPTY = new Terminal("");
    private Integer tokenId;


    public Terminal(String s) {
        super(s, false);
        this.tokenId = GrammarConsts.getGrammarConst(s);
    }

    public Terminal(String symbol, boolean isIdentifier, int tokenId) {
        super(symbol, false, isIdentifier);
        this.tokenId = tokenId;
    }

    public Terminal(Terminal t) {
        super(t.getSymbol(), false, true);
        tokenId = t.tokenId;
    }

    public static Terminal of(LexicalToken lexicalToken) {
        int tokenId = lexicalToken.getTokenId();
        Terminal t;
        if (tokenId == GrammarConsts.ID) {
            t = new Terminal(null, true, GrammarConsts.ID);
            t.setTokenString(lexicalToken.getTokenString());
        } else {
            t = new Terminal(lexicalToken.getTokenString());
            t.tokenId = lexicalToken.getTokenId();
        }
        return t;
    }

    @Override
    public boolean isIdentifier() {
        return super.isIdentifier();
    }

    public boolean isEmptySymbol() {
        return this == EMPTY || super.isEmptySymbol();
    }

    public boolean isEndSymbol() {
        return super.getSymbol() != null && super.getSymbol().equals("#");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Terminal terminal = (Terminal) o;
        return Objects.equals(tokenId, terminal.tokenId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), tokenId);
    }

    public int getTokenId() {
        return tokenId;
    }

    public void setTokenId(Integer tokenId) {
        this.tokenId = tokenId;
    }


}