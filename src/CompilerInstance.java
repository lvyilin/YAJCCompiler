import java.util.ArrayList;
import java.util.Arrays;

public class CompilerInstance {
    public static Compiler compiler1;// Textbook P132
    public static Compiler compiler2;// Ex 3
    public static Compiler compiler3;// Ex 4
    public static Compiler compiler4;// Textbook P155
    public static Compiler compiler5;// Ex 5
    public static Compiler compiler6;// Ex 5 extended

    static {
        initCompiler1();
        initCompiler2();
        initCompiler3();
        initCompiler4();
        initCompiler5();
        initCompiler6();
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

    private static void initCompiler3() {
        compiler3 = new Compiler();
        compiler3.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.OPERATOR_PRECEDENCE);
        Nonterminal
                E = new Nonterminal("E"),
                T = new Nonterminal("T"),
                F = new Nonterminal("F");

        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("+"), T)));
        SymbolString symbolString11 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("-"), T)));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T)));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("*"), F)));
        SymbolString symbolString33 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("/"), F)));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(F)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("("), E, new Terminal(")"))));
        SymbolString symbolString6 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Terminal.IDENTIFIER)));

        ProductionRule productionRule0 = new ProductionRule(E, new ArrayList<>(Arrays.asList(symbolString1, symbolString11, symbolString2)));
        ProductionRule productionRule1 = new ProductionRule(T, new ArrayList<>(Arrays.asList(symbolString3, symbolString33, symbolString4)));
        ProductionRule productionRule2 = new ProductionRule(F, new ArrayList<>(Arrays.asList(symbolString5, symbolString6)));

        compiler3.setStartSymbol(E);
        compiler3.defineProductionRule(E, productionRule0);
        compiler3.defineProductionRule(T, productionRule1);
        compiler3.defineProductionRule(F, productionRule2);
    }

    private static void initCompiler4() {
        compiler4 = new Compiler();
        compiler4.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.SLR_ONE);
        Nonterminal
                B_ = new Nonterminal("B_"),
                B = new Nonterminal("B"),
                D = new Nonterminal("D"),
                S = new Nonterminal("S");
        SymbolString symbolString0 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(B)));
        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("b"), D, new Terminal(";"), S, new Terminal("e"))));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(D, new Terminal(";"), new Terminal("d"))));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("d"))));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("s"), new Terminal(";"), S)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("s"))));

        ProductionRule productionRule0 = new ProductionRule(B_, new ArrayList<>(Arrays.asList(symbolString0)));
        ProductionRule productionRule1 = new ProductionRule(B, new ArrayList<>(Arrays.asList(symbolString1)));
        ProductionRule productionRule2 = new ProductionRule(D, new ArrayList<>(Arrays.asList(symbolString2, symbolString3)));
        ProductionRule productionRule3 = new ProductionRule(S, new ArrayList<>(Arrays.asList(symbolString4, symbolString5)));

        compiler4.setStartSymbol(B_);
        compiler4.defineProductionRule(B_, productionRule0);
        compiler4.defineProductionRule(B, productionRule1);
        compiler4.defineProductionRule(D, productionRule2);
        compiler4.defineProductionRule(S, productionRule3);
    }

    private static void initCompiler5() {
        compiler5 = new Compiler();
        compiler5.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.SLR_ONE);
        Nonterminal
                A = new Nonterminal("A"),
                V = new Nonterminal("V"),
                E = new Nonterminal("E"),
                T = new Nonterminal("T"),
                F = new Nonterminal("F");
        SymbolString symbolString0 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(V, new Terminal("="), E)));
        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("+"), T)));
        SymbolString symbolString11 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("-"), T)));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T)));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("*"), F)));
        SymbolString symbolString33 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("/"), F)));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(F)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("("), E, new Terminal(")"))));
        SymbolString symbolString6 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Terminal.IDENTIFIER)));

        ProductionRule productionRule11 = new ProductionRule(A, new ArrayList<>(Arrays.asList(symbolString0)));
        ProductionRule productionRule0 = new ProductionRule(E, new ArrayList<>(Arrays.asList(symbolString1, symbolString11, symbolString2)));
        ProductionRule productionRule1 = new ProductionRule(T, new ArrayList<>(Arrays.asList(symbolString3, symbolString33, symbolString4)));
        ProductionRule productionRule2 = new ProductionRule(F, new ArrayList<>(Arrays.asList(symbolString5, symbolString6)));
        ProductionRule productionRule3 = new ProductionRule(V, new ArrayList<>(Arrays.asList(symbolString6)));

        compiler5.setStartSymbol(A);
        compiler5.defineProductionRule(A, productionRule11);
        compiler5.defineProductionRule(E, productionRule0);
        compiler5.defineProductionRule(T, productionRule1);
        compiler5.defineProductionRule(F, productionRule2);
        compiler5.defineProductionRule(V, productionRule3);
    }

    private static void initCompiler6() {
        compiler6 = new Compiler();
        compiler6.setSyntaxAnalyzerType(Compiler.SyntaxAnalyzeEnum.SLR_ONE);
        Nonterminal
                A = new Nonterminal("A"),
                V = new Nonterminal("V"),
                E = new Nonterminal("E"),
                T = new Nonterminal("T"),
                F = new Nonterminal("F"),
                Entrance = new Nonterminal("Entrance"),
                Entrance_ = new Nonterminal("Entrance_"),
                Stmt = new Nonterminal("Stmt"),
                If = new Nonterminal("If"),
                Else = new Nonterminal("Else"),
                Condition = new Nonterminal("Condition"),
                Clause = new Nonterminal("Clause");

        SymbolString symbolString0 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(V, new Terminal("="), E)));
        SymbolString symbolString1 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("+"), T)));
        SymbolString symbolString11 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(E, new Terminal("-"), T)));
        SymbolString symbolString2 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T)));
        SymbolString symbolString3 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("*"), F)));
        SymbolString symbolString33 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(T, new Terminal("/"), F)));
        SymbolString symbolString4 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(F)));
        SymbolString symbolString5 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(new Terminal("("), E, new Terminal(")"))));
        SymbolString symbolString6 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Terminal.IDENTIFIER)));
        SymbolString symbolString7 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Entrance, Stmt)));
        SymbolString symbolString77 = new SymbolString(new ArrayList<Symbol>(Arrays.asList(Stmt)));
        SymbolString symbolString8 = new SymbolString(new ArrayList<>(Arrays.asList(A, new Terminal(";"))));
        SymbolString symbolString9 = new SymbolString(new ArrayList<>(Arrays.asList(If)));
        SymbolString symbolString10 = new SymbolString(new ArrayList<>(Arrays.asList(new Terminal("if"), new Terminal("("), Condition, new Terminal(")"), Clause, Else)));
        SymbolString symbolString101 = new SymbolString(new ArrayList<>(Arrays.asList(new Terminal("else"), Clause)));
        SymbolString symbolString12 = new SymbolString(new ArrayList<>(Arrays.asList(Terminal.IDENTIFIER, new Terminal("=="), Terminal.IDENTIFIER)));
        SymbolString symbolString121 = new SymbolString(new ArrayList<>(Arrays.asList(Terminal.IDENTIFIER, new Terminal("<"), Terminal.IDENTIFIER)));
        SymbolString symbolString122 = new SymbolString(new ArrayList<>(Arrays.asList(Terminal.IDENTIFIER, new Terminal("<="), Terminal.IDENTIFIER)));
        SymbolString symbolString123 = new SymbolString(new ArrayList<>(Arrays.asList(Terminal.IDENTIFIER, new Terminal(">"), Terminal.IDENTIFIER)));
        SymbolString symbolString124 = new SymbolString(new ArrayList<>(Arrays.asList(Terminal.IDENTIFIER, new Terminal(">="), Terminal.IDENTIFIER)));
        SymbolString symbolString13 = new SymbolString(new ArrayList<>(Arrays.asList(new Terminal("{"), Entrance, new Terminal("}"))));
        SymbolString symbolString14 = new SymbolString(new ArrayList<>(Arrays.asList(Stmt)));
        SymbolString symbolString15 = new SymbolString(new ArrayList<>(Arrays.asList(Entrance)));

        ProductionRule productionRule11 = new ProductionRule(A, new ArrayList<>(Arrays.asList(symbolString0)));
        ProductionRule productionRule0 = new ProductionRule(E, new ArrayList<>(Arrays.asList(symbolString1, symbolString11, symbolString2)));
        ProductionRule productionRule1 = new ProductionRule(T, new ArrayList<>(Arrays.asList(symbolString3, symbolString33, symbolString4)));
        ProductionRule productionRule2 = new ProductionRule(F, new ArrayList<>(Arrays.asList(symbolString5, symbolString6)));
        ProductionRule productionRule3 = new ProductionRule(V, new ArrayList<>(Arrays.asList(symbolString6)));
        ProductionRule productionRule4 = new ProductionRule(Entrance, new ArrayList<>(Arrays.asList(symbolString7, symbolString77)));
        ProductionRule productionRule5 = new ProductionRule(Stmt, new ArrayList<>(Arrays.asList(symbolString8, symbolString9)));
        ProductionRule productionRule6 = new ProductionRule(If, new ArrayList<>(Arrays.asList(symbolString10)));
        ProductionRule productionRule66 = new ProductionRule(If, new ArrayList<>(Arrays.asList(symbolString101)));
        ProductionRule productionRule7 = new ProductionRule(Condition, new ArrayList<>(Arrays.asList(symbolString12, symbolString121, symbolString122, symbolString123, symbolString124)));
        ProductionRule productionRule8 = new ProductionRule(Clause, new ArrayList<>(Arrays.asList(symbolString13, symbolString14)));
        ProductionRule productionRule9 = new ProductionRule(Entrance_, new ArrayList<>(Arrays.asList(symbolString15)));

        compiler6.setStartSymbol(Entrance_);
        compiler6.defineProductionRule(Entrance_, productionRule9);
        compiler6.defineProductionRule(Entrance, productionRule4);
        compiler6.defineProductionRule(Stmt, productionRule5);
        compiler6.defineProductionRule(If, productionRule6);
        compiler6.defineProductionRule(Else, productionRule66);
        compiler6.defineProductionRule(Condition, productionRule7);
        compiler6.defineProductionRule(Clause, productionRule8);
        compiler6.defineProductionRule(A, productionRule11);
        compiler6.defineProductionRule(E, productionRule0);
        compiler6.defineProductionRule(T, productionRule1);
        compiler6.defineProductionRule(F, productionRule2);
        compiler6.defineProductionRule(V, productionRule3);
    }
}
