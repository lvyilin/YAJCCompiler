public class Terminal extends Symbol {
    public static final Terminal IDENTIFIER = new Terminal(null, true);

    public Terminal(String s) {
        super(s);
    }

    public Terminal(String symbol, boolean isNonterminal) {
        super(symbol, isNonterminal);
    }
}