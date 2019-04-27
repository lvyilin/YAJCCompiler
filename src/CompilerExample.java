import java.util.ArrayList;
import java.util.Arrays;

public class CompilerExample {
    public static Compiler compiler1;// Textbook P132
    public static Compiler compiler2;// Ex 3

    static {
        initCompiler1();
        initCompiler2();
    }

    private static void initCompiler1() {
        compiler1 = new Compiler();
        compiler1.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.OPERATOR_PRECEDENCE);
        Nonterminal
                E = new Nonterminal("E"),
                T = new Nonterminal("T"),
                F = new Nonterminal("F");

        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("+"), T)));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T)));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("*"), F)));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(F)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("("), E, new Terminal(")"))));
        SymbolString symbolString6 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Terminal.IDENTIFIER)));

        ProductionRule productionRule0 = new ProductionRule(E, new ArrayList<>(Arrays.asList(symbolString1, symbolString2)));
        ProductionRule productionRule1 = new ProductionRule(T, new ArrayList<>(Arrays.asList(symbolString3, symbolString4)));
        ProductionRule productionRule2 = new ProductionRule(F, new ArrayList<>(Arrays.asList(symbolString5, symbolString6)));

        compiler1.setStartSymbol(E);
        compiler1.defineProductionRule(E, productionRule0);
        compiler1.defineProductionRule(T, productionRule1);
        compiler1.defineProductionRule(F, productionRule2);
    }

    private static void initCompiler2() {
        compiler2 = new Compiler();
        compiler2.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.LL_ONE);
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

        compiler2.setStartSymbol(S);
        compiler2.defineProductionRule(S, productionRule0);
        compiler2.defineProductionRule(A, productionRule1);
        compiler2.defineProductionRule(E, productionRule2);
        compiler2.defineProductionRule(E_, productionRule3);
        compiler2.defineProductionRule(T, productionRule4);
        compiler2.defineProductionRule(T_, productionRule5);
        compiler2.defineProductionRule(F, productionRule6);
        compiler2.defineProductionRule(M, productionRule7);
        compiler2.defineProductionRule(V, productionRule8);
    }
}
