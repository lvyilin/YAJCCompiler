public class SymbolInfo {
    private Integer type;
    private String name;
    private Integer TC;
    private Integer FC;


    private Integer jump;

    public SymbolInfo(Symbol symbol) {
        this.name = symbol.getTokenString();
    }

    public SymbolInfo(Integer jmp) {
        this.jump = jmp;
    }

    public SymbolInfo() {
    }

    public Integer getTC() {
        return TC;
    }

    public Integer getFC() {
        return FC;
    }

    public void setFC(Integer FC) {
        this.FC = FC;
    }

    public void setTC(Integer TC) {
        this.TC = TC;
    }

    public Integer getJump() {
        return jump;
    }

    public void setJump(Integer jump) {
        this.jump = jump;
    }

    @Override
    public String toString() {
        return name != null ? name : String.valueOf(jump);
    }
}
