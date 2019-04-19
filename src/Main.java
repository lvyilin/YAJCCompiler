import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Main {


    public static void main(String[] args) {
//        String testFileName1 = "test/example1.c";
//        String testFileName2 = "test/example2.c";
        String testFileName3 = "test/example3.c";
        String testFileName4 = "test/example4.c";
//        String testFileName5 = "test/example5.c";
//        String testFileName6 = "test/example6.c";
        Compiler compiler = new Compiler();
        Nonterminal
                S = new Nonterminal("S"),
                A = new Nonterminal("A"),
                E = new Nonterminal("E"),
                E_ = new Nonterminal("E_"),
                T = new Nonterminal("T"),
                T_ = new Nonterminal("T_"),
                F = new Nonterminal("F"),
                M = new Nonterminal("M"),
                V = new Nonterminal("V");
        SymbolString emptySymbolString = new SymbolString(new ArrayList<>());
        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(V, new Terminal("="), E)));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, E_)));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(A, T, E_)));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(F, T_)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(M, F, T_)));
        SymbolString symbolString6 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("("), E, new Terminal(")"))));
        SymbolString symbolString7 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("+"))));
        SymbolString symbolString8 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("-"))));
        SymbolString symbolString9 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("*"))));
        SymbolString symbolString10 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("/"))));
        SymbolString symbolString11 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Terminal.IDENTIFIER)));

        ProductionRule productionRule0 = new ProductionRule(S, new ArrayList<>(Arrays.asList(symbolString1)));
        ProductionRule productionRule1 = new ProductionRule(A, new ArrayList<>(Arrays.asList(symbolString7, symbolString8)));
        ProductionRule productionRule2 = new ProductionRule(E, new ArrayList<>(Arrays.asList(symbolString2)));
        ProductionRule productionRule3 = new ProductionRule(E_, new ArrayList<>(Arrays.asList(symbolString3, emptySymbolString)));
        ProductionRule productionRule4 = new ProductionRule(T, new ArrayList<>(Arrays.asList(symbolString4)));
        ProductionRule productionRule5 = new ProductionRule(T_, new ArrayList<>(Arrays.asList(symbolString5, emptySymbolString)));
        ProductionRule productionRule6 = new ProductionRule(F, new ArrayList<>(Arrays.asList(symbolString6, symbolString11)));
        ProductionRule productionRule7 = new ProductionRule(M, new ArrayList<>(Arrays.asList(symbolString9, symbolString10)));
        ProductionRule productionRule8 = new ProductionRule(V, new ArrayList<>(Arrays.asList(symbolString11)));

        compiler.setStartSymbol(S);
        compiler.defineProductionRule(S, productionRule0);
        compiler.defineProductionRule(A, productionRule1);
        compiler.defineProductionRule(E, productionRule2);
        compiler.defineProductionRule(E_, productionRule3);
        compiler.defineProductionRule(T, productionRule4);
        compiler.defineProductionRule(T_, productionRule5);
        compiler.defineProductionRule(F, productionRule6);
        compiler.defineProductionRule(M, productionRule7);
        compiler.defineProductionRule(V, productionRule8);
        try {
//            compiler.compile(testFileName1);
//            compiler.compile(testFileName2);
            compiler.compile(testFileName3);
            compiler.compile(testFileName4);
//            compiler.compile(testFileName5);
//            compiler.compile(testFileName6);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
