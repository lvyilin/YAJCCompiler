import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class OPGSyntaxAnalyzer extends SyntaxAnalyzer {
    private HashMap<Nonterminal, HashMap<Nonterminal, Integer>> operatorPrecedenceTable = new HashMap<>();
    private LinkedList<Symbol> analyzeList = new LinkedList<>();
    private LinkedList<Symbol> leftestPrimePhrase = new LinkedList<>();
    private HashMap<Terminal, Integer> stackPrecedence = new HashMap<>();
    private HashMap<Terminal, Integer> comparePrecedence = new HashMap<>();

    public OPGSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                             Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules) {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
    }

    private void buildPrecedence() {
//        stackPrecedence.put()
    }

    private int compare(Terminal t1, Terminal t2) {
        return getStackPrecedence(t1) - getComparePrecedence(t2);
    }

    private int getStackPrecedence(Terminal nonterminal) {
        if (nonterminal == null) return -1;
        return stackPrecedence.get(nonterminal);
    }

    private int getComparePrecedence(Terminal nonterminal) {
        if (nonterminal == null) return -1;
        return comparePrecedence.get(nonterminal);
    }

    private void initAnalyzeStack() {
        analyzeList.add(Terminal.END);
        List<LexicalToken> lexicalTokens = getLexicalTokens();
        for (LexicalToken token : lexicalTokens) {
            analyzeList.add(Terminal.of(token));
        }
        analyzeList.add(Terminal.END);
    }

    //    private int findIndex(Iterator<Symbol> iterator, Predicate<Integer> comparator) {
//        Symbol cur = null, next = null;
//        int farest = 0, ret = 0;
//        while (iterator.hasNext()) {
//            while (iterator.hasNext()) {
//                cur = iterator.next();
//                if (!cur.isNonterminal()) break;
//                ++farest;
//                ++ret;
//            }
//            while (iterator.hasNext()) {
//                next = iterator.next();
//                if (!next.isNonterminal()) break;
//                ++farest;
//
//            }
//            if (next == null) return -1;
//            int com = compare((Terminal) cur, (Terminal) next);
//            if (comparator.test(com)) return ret;
//            ret = farest;
//        }
//        return -1;
//
//    }
    private boolean canReduce(SymbolString symbolString) {
        ArrayList<Symbol> list = symbolString.getSymbols();
        if (list.size() != leftestPrimePhrase.size()) return false;
        int i = 0;
        for (Symbol symbol : leftestPrimePhrase) {
            if (symbol.isNonterminal()) {
                if (!list.get(i).isNonterminal()) return false;
            } else {
                if (!symbol.equals(list.get(i))) return false;
            }
            ++i;
        }
        return true;
    }

    private Nonterminal reduce() {
        HashMap<Nonterminal, ProductionRule> rules = getProductionRules();
        for (Map.Entry<Nonterminal, ProductionRule> entry : rules.entrySet()) {
            Nonterminal nonterminal = entry.getKey();//nonterminal = F
            ProductionRule rule = entry.getValue();//rule = AB|i
            for (SymbolString symbolString : rule.getSymbolStrings()) {
                if (canReduce(symbolString)) return nonterminal;
            }
        }
        return null;
    }

    @Override
    public void analyze() throws IOException {
        try {
            readAllLexicalTokens();
            buildPrecedence();
            initAnalyzeStack();

            List<LexicalToken> lexicalTokens = getLexicalTokens();
            ListIterator<Symbol> iterator = analyzeList.listIterator();
            Symbol curSymbol = null, nextSymbol = null, prevSymbol = null;
            int lineNumber = 0, rightIndex, leftIndex;
            do {
                while (true) {
                    while (iterator.hasNext()) {
                        nextSymbol = iterator.next();
                        if (!nextSymbol.isNonterminal()) break;
                        ++lineNumber;
                    }
                    if (!iterator.hasNext())
                        throw new SyntaxException(lexicalTokens.get(lineNumber), lineNumber + 1);
                    if (curSymbol != null) {
                        int com = compare((Terminal) curSymbol, (Terminal) nextSymbol);
                        if (com > 0) break;
                    }
                    curSymbol = nextSymbol;
                }
                leftIndex = rightIndex = lineNumber - 1;
                ListIterator<Symbol> iterator1 = analyzeList.listIterator(rightIndex);
                curSymbol = iterator1.next();
                leftestPrimePhrase.addFirst(curSymbol);
                while (true) {
                    while (iterator1.hasPrevious()) {
                        prevSymbol = iterator.previous();
                        leftestPrimePhrase.addFirst(prevSymbol);
                        --leftIndex;
                        if (!prevSymbol.isNonterminal()) break;
                    }

                    if (!iterator1.hasPrevious())
                        throw new SyntaxException(lexicalTokens.get(lineNumber), lineNumber + 1);
                    int com = compare((Terminal) prevSymbol, (Terminal) curSymbol);
                    if (com < 0) break;
                    curSymbol = prevSymbol;
                }
                Nonterminal nonterminal = reduce();
                analyzeList.set(leftIndex, nonterminal);
                analyzeList.subList(leftIndex + 1, rightIndex).clear();

            } while (analyzeList.size() == 3);
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }
}
