public class Terminal extends Symbol {
    public static final Terminal IDENTIFIER = new Terminal(null, false, true);
    public static final Terminal END = new Terminal("#", false);
    public static final Terminal EMPTY = new Terminal("", false);

    public Terminal(String s) {
        super(s, false);
    }

    private Terminal(String symbol, boolean isNonterminal) {
        super(symbol, isNonterminal);
    }

    private Terminal(String symbol, boolean isNonterminal, boolean isIdentifier) {
        super(symbol, isNonterminal, isIdentifier);
    }

    public static Terminal of(LexicalToken lexicalToken) {
        String symbol = lexicalToken.getTokenString();
        int tokenId = lexicalToken.getTokenId();
        if (tokenId == GrammarConsts.ID) return IDENTIFIER;
        return new Terminal(symbol);
    }

    @Override
    public boolean isIdentifer() {
        return super.isIdentifer();
    }

    public boolean isEmptySymbol() {
        return this == EMPTY || super.isEmptySymbol();
    }

    public boolean isEndSymbol() {
        return super.getSymbol() != null && super.getSymbol().equals("#");
    }
}