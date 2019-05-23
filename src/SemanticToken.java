public class SemanticToken {
    private final Integer op;
    private final SymbolInfo arg1;
    private final SymbolInfo arg2;
    private SymbolInfo result;

    public void setJumpResult(Integer i) {
        this.result = new SymbolInfo(i);
    }


    @Override
    public String toString() {
        return OperatorConsts.getOperatorString(op) + "\t" + arg1 + "\t" + arg2 + "\t" + result;
    }

    public SemanticToken(Integer op, SymbolInfo arg1, SymbolInfo arg2, SymbolInfo result) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.result = result;
    }

}
