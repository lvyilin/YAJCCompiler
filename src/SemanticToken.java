import java.util.HashMap;

public class SemanticToken {
    private final Integer op;
    private final Integer arg1;
    private final Integer arg2;
    private final Integer result;

    @Override
    public String toString() {
        return op + "\t" + arg1 + "\t" + arg2 + "\t" + result;
    }

    public SemanticToken(Integer op, Integer arg1, Integer arg2, Integer result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

    public static String annotate(SemanticToken token, HashMap<Symbol, SymbolInfo> symbolInfoHashMap) {
        return null;
    }
}
