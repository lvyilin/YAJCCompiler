import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

public class Compiler {
    private HashMap<Nonterminal, ProductionRule> productionRules = new HashMap<>();
    private Nonterminal startSymbol;
    private SyntaxAnalyzeEnum syntaxAnalyzerType = SyntaxAnalyzeEnum.RECURSIVE;
    private HashSet<Terminal> terminalHashSet = new HashSet<>();

    public void setStartSymbol(Nonterminal startSymbol) {
        this.startSymbol = startSymbol;
    }

    public void setSyntaxAnalyzerType(SyntaxAnalyzeEnum syntaxAnalyzerType) {
        this.syntaxAnalyzerType = syntaxAnalyzerType;
    }

    public HashSet<Terminal> getTerminalHashSet() {
        return terminalHashSet;
    }


    public enum SyntaxAnalyzeEnum {
        RECURSIVE, LL_ONE, OPERATOR_PRECEDENCE
    }


    public void defineProductionRule(Nonterminal nonterminal, ProductionRule productionRule) {
        productionRules.put(nonterminal, productionRule);
        for (SymbolString string : productionRule.getSymbolStrings()) {
            for (Symbol symbol : string.getSymbols()) {
                if (!symbol.isNonterminal()) {
                    terminalHashSet.add((Terminal) symbol);
                }
            }
        }
    }

    public void compile(String fileName) throws IOException {
        String baseName = getBaseName(fileName);
        String preprocessedFileName = baseName + ".i";
        String lexicalFileName = baseName + ".lex";
        String syntaxFileName = baseName + ".syn";


        BufferedReader br1 = new BufferedReader(new FileReader(fileName));
        BufferedWriter bw1 = new BufferedWriter(new FileWriter(preprocessedFileName));
        Preprocessor.process(br1, bw1);
        br1.close();
        bw1.close();

        BufferedReader br2 = new BufferedReader(new FileReader(preprocessedFileName));
        BufferedWriter bw2 = new BufferedWriter(new FileWriter(lexicalFileName));
        LexicalAnalyzer.analyze(br2, bw2);
        br2.close();
        bw2.close();

        BufferedReader br3 = new BufferedReader(new FileReader(lexicalFileName));
//        BufferedWriter bw3 = new BufferedWriter(new OutputStreamWriter(System.out));
        BufferedWriter bw3 = new BufferedWriter(new FileWriter(syntaxFileName));

//        SyntaxAnalyzer syntaxAnalyzer = new RecursiveSyntaxAnalyzer(br3, bw3,startSymbol, productionRules);
        SyntaxAnalyzer syntaxAnalyzer = null;
        switch (syntaxAnalyzerType) {
            case LL_ONE:
                syntaxAnalyzer = new LLOneSyntaxAnalyzer(br3, bw3, startSymbol, productionRules);
                break;
            case RECURSIVE:
                syntaxAnalyzer = new RecursiveSyntaxAnalyzer(br3, bw3, startSymbol, productionRules);
                break;
            case OPERATOR_PRECEDENCE:
                syntaxAnalyzer = new OPGSyntaxAnalyzer(br3, bw3, startSymbol, productionRules, terminalHashSet);

        }
        syntaxAnalyzer.analyze();
        br3.close();
        bw3.flush();

    }

    public String getBaseName(String fileName) {
        if (fileName == null) return null;
        int i = fileName.lastIndexOf('.');
        if (i != -1) {
            return fileName.substring(0, i);
        }
        return fileName;
    }
}
