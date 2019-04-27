import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class OPGSyntaxAnalyzer extends SyntaxAnalyzer {
    private LinkedList<Symbol> analyzeList = new LinkedList<>();
    //    private HashMap<Nonterminal, HashMap<Nonterminal, Integer>> operatorPrecedenceTable = new HashMap<>();
    //    private HashMap<Terminal, Integer> stackPrecedence = new HashMap<>();
//    private HashMap<Terminal, Integer> comparePrecedence = new HashMap<>();
    private HashSet<Terminal> terminalHashSet;
    private HashMap<Terminal, HashMap<Terminal, Precedence>> precedenceMatrix = new HashMap<>();
    private HashMap<Nonterminal, HashSet<Terminal>> firstVt = new HashMap<>();
    private HashMap<Nonterminal, HashSet<Terminal>> lastVt = new HashMap<>();

    private enum Precedence {
        NA, EQ, GT, LE
    }

    public OPGSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                             Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules, HashSet<Terminal> hashSet) {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
        this.terminalHashSet = hashSet;
    }

    private boolean addVtMap(Nonterminal nonterminal, Terminal terminal, HashMap<Nonterminal, HashSet<Terminal>> vtMap) {
        HashSet<Terminal> set = vtMap.computeIfAbsent(nonterminal, k -> new HashSet<>());
        return set.add(terminal);
    }

    private void buildFirstVt() {
        LinkedList<Nonterminal> nonterminalList = new LinkedList<>();
        HashMap<Nonterminal, ProductionRule> rules = getProductionRules();
        for (Map.Entry<Nonterminal, ProductionRule> entry : rules.entrySet()) {
            Nonterminal nonterminal = entry.getKey();
            for (SymbolString string : entry.getValue().getSymbolStrings()) {
                ArrayList<Symbol> symbolList = string.getSymbols();
                if (symbolList.size() > 0) {
                    Symbol firstSymbol = symbolList.get(0);
                    if (firstSymbol.isNonterminal()) {
                        nonterminalList.add(nonterminal);
                        if (symbolList.size() > 1) {
                            Symbol secondSymbol = symbolList.get(1);
                            if (!secondSymbol.isNonterminal()) {
                                addVtMap(nonterminal, (Terminal) secondSymbol, firstVt);
                            }
                        }
                    } else {
                        addVtMap(nonterminal, (Terminal) firstSymbol, firstVt);
                    }
                }

            }
        }
        boolean updated = true;
        while (updated) {
            updated = false;
            for (Nonterminal nonterminal : nonterminalList) {
                ProductionRule rule = rules.get(nonterminal);
                for (SymbolString string : rule.getSymbolStrings()) {
                    ArrayList<Symbol> symbolList = string.getSymbols();
                    if (symbolList.size() > 0) {
                        Symbol firstSymbol = symbolList.get(0);
                        if (firstSymbol.isNonterminal()) {
                            HashSet<Terminal> srcSet = firstVt.get(firstSymbol);
                            for (Terminal terminal : srcSet) {
                                updated = addVtMap(nonterminal, terminal, firstVt) || updated;
                            }
                        }
                    }
                }
            }
        }
    }

    private void buildLastVt() {
        LinkedList<Nonterminal> nonterminalList = new LinkedList<>();
        HashMap<Nonterminal, ProductionRule> rules = getProductionRules();
        for (Map.Entry<Nonterminal, ProductionRule> entry : rules.entrySet()) {
            Nonterminal nonterminal = entry.getKey();
            for (SymbolString string : entry.getValue().getSymbolStrings()) {
                ArrayList<Symbol> symbolList = string.getSymbols();
                if (symbolList.size() > 0) {
                    Symbol lastSymbol = symbolList.get(symbolList.size() - 1);
                    if (lastSymbol.isNonterminal()) {
                        nonterminalList.add(nonterminal);
                        if (symbolList.size() > 1) {
                            Symbol secondSymbol = symbolList.get(symbolList.size() - 2);
                            if (!secondSymbol.isNonterminal()) {
                                addVtMap(nonterminal, (Terminal) secondSymbol, lastVt);
                            }
                        }
                    } else {
                        addVtMap(nonterminal, (Terminal) lastSymbol, lastVt);
                    }
                }

            }
        }
        boolean updated = true;
        while (updated) {
            updated = false;
            for (Nonterminal nonterminal : nonterminalList) {
                ProductionRule rule = rules.get(nonterminal);
                for (SymbolString string : rule.getSymbolStrings()) {
                    ArrayList<Symbol> symbolList = string.getSymbols();
                    if (symbolList.size() > 0) {
                        Symbol lastSymbol = symbolList.get(symbolList.size() - 1);
                        if (lastSymbol.isNonterminal()) {
                            HashSet<Terminal> srcSet = lastVt.get(lastSymbol);
                            for (Terminal terminal : srcSet) {
                                updated = addVtMap(nonterminal, terminal, lastVt) || updated;
                            }
                        }
                    }
                }
            }
        }
    }

    private void buildPrecedence() throws IllegalSyntaxException {
        buildFirstVt();
        buildLastVt();
        initPrecedenceMatrix();
        HashMap<Nonterminal, ProductionRule> rules = getProductionRules();
        for (Map.Entry<Nonterminal, ProductionRule> entry : rules.entrySet()) {
            Nonterminal nonterminal = entry.getKey();
            for (SymbolString string : entry.getValue().getSymbolStrings()) {
                ArrayList<Symbol> symbolList = string.getSymbols();
                for (int i = 0; i < symbolList.size() - 1; i++) {
                    Symbol symbol1 = symbolList.get(i);
                    Symbol symbol2 = symbolList.get(i + 1);
                    if (!symbol1.isNonterminal()) {
                        Terminal terminal1 = (Terminal) symbol1;
                        if (!symbol2.isNonterminal()) {
                            setPrecedenceMatrixElement(terminal1, (Terminal) symbol2, Precedence.EQ);
                        } else {
                            if (i < symbolList.size() - 2) {
                                Symbol symbol3 = symbolList.get(i + 2);
                                if (!symbol3.isNonterminal()) {
                                    setPrecedenceMatrixElement(terminal1, (Terminal) symbol3, Precedence.EQ);
                                }
                            }
                            HashSet<Terminal> hashSet = firstVt.get(symbol2);
                            for (Terminal terminal : hashSet) {
                                setPrecedenceMatrixElement(terminal1, terminal, Precedence.LE);
                            }
                        }
                    } else {
                        if (!symbol2.isNonterminal()) {
                            Terminal terminal2 = (Terminal) symbol2;
                            HashSet<Terminal> hashSet = lastVt.get(symbol1);
                            for (Terminal terminal : hashSet) {
                                setPrecedenceMatrixElement(terminal, terminal2, Precedence.GT);
                            }
                        }
                    }
                }
            }
        }

    }

    private void initPrecedenceMatrix() throws IllegalSyntaxException {
        for (Terminal terminal1 : terminalHashSet) {
            for (Terminal terminal2 : terminalHashSet) {
                setPrecedenceMatrixElement(terminal1, terminal2, Precedence.NA);
            }
        }
    }

    private Precedence getPrecedenceMatrixElement(Terminal terminal1, Terminal terminal2) {
        HashMap<Terminal, Precedence> map = precedenceMatrix.get(terminal1);
        if (map == null) return null;
        Precedence precedence = map.get(terminal2);
        if (precedence == null) return null;
        return precedence;
    }

    private void setPrecedenceMatrixElement(Terminal terminal1, Terminal terminal2, Precedence precedence) throws IllegalSyntaxException {
        HashMap<Terminal, Precedence> map = precedenceMatrix.computeIfAbsent(terminal1, k -> new HashMap<>());
        if (map.containsKey(terminal2)) {
            Precedence prec = map.get(terminal2);
            if (prec == precedence) return;
            if (prec != Precedence.NA) {
                throw new IllegalSyntaxException();
            }
        }
        map.put(terminal2, precedence);
    }

    private int compare(Terminal t1, Terminal t2) {
//        return getStackPrecedence(t1) - getComparePrecedence(t2);
        Precedence precedence = getPrecedenceMatrixElement(t1, t2);
        assert precedence != null;
        assert precedence != Precedence.NA;
        switch (precedence) {
            case GT:
                return 1;
            case LE:
                return -1;
        }
        return 0;
    }


//    private int getStackPrecedence(Terminal nonterminal) {
//        if (nonterminal == null) return -1;
//        return stackPrecedence.get(nonterminal);
//    }
//
//    private int getComparePrecedence(Terminal nonterminal) {
//        if (nonterminal == null) return -1;
//        return comparePrecedence.get(nonterminal);
//    }

    private void initAnalyzeStack() {
        List<LexicalToken> lexicalTokens = getLexicalTokens();
        for (LexicalToken token : lexicalTokens) {
            analyzeList.add(Terminal.of(token));
        }
    }

    private boolean canReduce(SymbolString symbolString, LinkedList<Symbol> leftestPrimePhrase) {
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

    private Nonterminal reduce(LinkedList<Symbol> leftestPrimePhrase) {
        HashMap<Nonterminal, ProductionRule> rules = getProductionRules();
        for (Map.Entry<Nonterminal, ProductionRule> entry : rules.entrySet()) {
            Nonterminal nonterminal = entry.getKey();//nonterminal = F
            ProductionRule rule = entry.getValue();//rule = AB|i
            for (SymbolString symbolString : rule.getSymbolStrings()) {
                if (canReduce(symbolString, leftestPrimePhrase)) return nonterminal;
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
            ListIterator<Symbol> iterator;

            Symbol curSymbol = null, nextSymbol = null, prevSymbol = null;
            int curSymbolIndex, nextSymbolIndex, prevSymbolIndex, leftIndex, rightIndex;
            while (analyzeList.size() > 1) {
                LinkedList<Symbol> leftestPrimePhrase = new LinkedList<>();
                iterator = analyzeList.listIterator();
                while (iterator.hasNext()) {
                    curSymbol = iterator.next();
                    if (!curSymbol.isNonterminal()) break;
                }
                curSymbolIndex = iterator.nextIndex() - 1;
                while (iterator.hasNext()) {
                    nextSymbolIndex = iterator.nextIndex();
                    nextSymbol = iterator.next();
                    if (!nextSymbol.isNonterminal()) {
                        int com = compare((Terminal) curSymbol, (Terminal) nextSymbol);
                        if (com > 0) break;
                        curSymbolIndex = nextSymbolIndex;
                        curSymbol = nextSymbol;
                    }
                }
                if (curSymbolIndex < 0 || curSymbolIndex >= analyzeList.size())
                    throw new SyntaxException(lexicalTokens.get(analyzeList.size() - 1), analyzeList.size());

                rightIndex = curSymbolIndex;
                ListIterator<Symbol> iterator1 = analyzeList.listIterator(curSymbolIndex);
                leftestPrimePhrase.add(curSymbol);
                if (curSymbolIndex < analyzeList.size() - 1) {
                    Symbol curNextNonTerminal = analyzeList.get(curSymbolIndex + 1);
                    if (curNextNonTerminal.isNonterminal()) {
                        leftestPrimePhrase.add(curNextNonTerminal);
                        ++rightIndex;
                    }
                }

                while (iterator1.hasPrevious()) {
                    prevSymbolIndex = iterator1.previousIndex();
                    prevSymbol = iterator1.previous();
                    if (!prevSymbol.isNonterminal()) {
                        int com = compare((Terminal) prevSymbol, (Terminal) curSymbol);
                        if (com < 0) break;
                        leftestPrimePhrase.addFirst(prevSymbol);
                        curSymbolIndex = prevSymbolIndex;
                        curSymbol = prevSymbol;
                    }
                }
                leftIndex = curSymbolIndex;
                if (curSymbolIndex > 0) {
                    Symbol curPrevNonTerminal = analyzeList.get(curSymbolIndex - 1);
                    if (curPrevNonTerminal.isNonterminal()) {
                        leftestPrimePhrase.addFirst(curPrevNonTerminal);
                        --leftIndex;
                    }
                }

                Nonterminal nonterminal = reduce(leftestPrimePhrase);
                if (nonterminal == null)
                    throw new SyntaxException(lexicalTokens.get(leftIndex), leftIndex + 1);
                analyzeList.set(leftIndex, nonterminal);
                if (leftIndex + 1 < rightIndex) {
                    analyzeList.subList(leftIndex + 1, rightIndex + 1).clear();
                }

            }
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        } catch (IllegalSyntaxException e) {
            e.printStackTrace();
        }
    }
}
