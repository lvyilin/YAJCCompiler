public class SymbolInfo {
    private int type;
    private String name;

    public SymbolInfo(Symbol symbol) {
        this.name = symbol.getTokenString();
    }

    @Override
    public String toString() {
        return name;
    }
}
