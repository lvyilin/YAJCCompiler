public class Terminal extends Symbol {
    public static final Terminal IDENTIFIER = new Terminal(null, false, true, GrammarConsts.ID);
    public static final Terminal END = new Terminal("#", false);
    public static final Terminal EMPTY = new Terminal("", false);
    private int tokenId;

    public Terminal(String s) {
        super(s, false);
    }

    private Terminal(String symbol, boolean isNonterminal) {
        super(symbol, isNonterminal);
    }

    private Terminal(LexicalToken lexicalToken) {
        super(lexicalToken.getTokenString());
        tokenId = lexicalToken.getTokenId();
    }

//    private Terminal(String symbol, boolean isNonterminal, boolean isIdentifier) {
//        super(symbol, isNonterminal, isIdentifier);
//    }

    private Terminal(String symbol, boolean isNonterminal, boolean isIdentifier, int tokenId) {
        super(symbol, isNonterminal, isIdentifier);
        this.tokenId = tokenId;
    }

    public static Terminal of(LexicalToken lexicalToken) {
        int tokenId = lexicalToken.getTokenId();
        if (tokenId == GrammarConsts.ID) return IDENTIFIER;
        return new Terminal(lexicalToken);
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
}