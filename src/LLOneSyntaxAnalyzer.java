import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class LLOneSyntaxAnalyzer extends SyntaxAnalyzer {
    private HashMap<Nonterminal, HashMap<SymbolString, HashSet<Terminal>>> firstHashMap = new HashMap<>();
    private HashMap<Nonterminal, HashSet<Terminal>> followHashMap = new HashMap<>();
    private HashMap<Nonterminal, HashMap<Terminal, SymbolString>> analyzeTable = new HashMap<>();
    private Stack<Symbol> analyzeStack = new Stack<>();

    public LLOneSyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter,
                               Nonterminal startSymbol, HashMap<Nonterminal, ProductionRule> productionRules) {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
    }

    private void buildFirstMap() {
        //example: F->AB|i
        boolean updated = true;
        while (updated) {
            updated = false;
            for (Map.Entry<Nonterminal, ProductionRule> entry : getProductionRules().entrySet()) {
                Nonterminal nonterminal = entry.getKey();//nonterminal = F
                ProductionRule rule = entry.getValue();//rule = AB|i
                for (SymbolString symbolString : rule.getSymbolStrings()) {
                    //symbolString = AB
                    if (symbolString.isEmptySymbolString()) {
                        updated = addToFirstMap(nonterminal, symbolString, Terminal.EMPTY) || updated;
                    } else {
                        //symbolArrayList = AB
                        ArrayList<Symbol> symbolArrayList = symbolString.getSymbols();
                        Symbol symbol = symbolArrayList.get(0);
                        if (symbol.isNonterminal()) {
                            boolean allContainsEmpty = true;
                            for (Symbol sym : symbolArrayList) {
                                boolean containsEmpty = false;
                                if (!sym.isNonterminal()) break;
                                Nonterminal nont = (Nonterminal) sym;//A
                                HashSet<Terminal> hashSet = getFirst(nont);
                                for (Terminal ter : hashSet) {
                                    if (ter.isEndSymbol()) {
                                        containsEmpty = true;
                                        continue;
                                    }
                                    updated = addToFirstMap(nonterminal, symbolString, ter) || updated;
                                }
                                if (!containsEmpty) {
                                    allContainsEmpty = false;
                                    break;
                                }
                            }
                            if (allContainsEmpty) {
                                updated = addToFirstMap(nonterminal, symbolString, Terminal.EMPTY) || updated;
                            }
                        } else {
                            updated = addToFirstMap(nonterminal, symbolString, (Terminal) symbol) || updated;
                        }
                    }
                }
            }
        }
    }

    private void buildFollowSet() {
        addToFollowMap(getStartSymbol(), Terminal.END);
        boolean updated = true;
        while (updated) {
            updated = false;
            //example: F->AB|i
            for (Map.Entry<Nonterminal, ProductionRule> entry : getProductionRules().entrySet()) {
                Nonterminal nonterminal = entry.getKey();//nonterminal = F
                ProductionRule rule = entry.getValue();//rule = AB|i
                for (SymbolString symbolString : rule.getSymbolStrings()) {
                    //symbolString = AB
                    if (symbolString.isEmptySymbolString()) continue;
                    //symbolArrayList = AB
                    ArrayList<Symbol> symbolArrayList = symbolString.getSymbols();
                    for (int i = 0; i < symbolArrayList.size(); i++) {
                        Symbol symbol = symbolArrayList.get(i);
                        if (!symbol.isNonterminal()) continue;
                        Nonterminal nonterminal1 = (Nonterminal) symbol;
                        if (i + 1 < symbolArrayList.size()) {
                            Symbol nextSymbol = symbolArrayList.get(i + 1);
                            if (!nextSymbol.isNonterminal()) {
                                updated = addToFollowMap(nonterminal1, (Terminal) nextSymbol) || updated;
                                continue;
                            } else {
                                boolean containsEmpty = false;
                                HashSet<Terminal> first = getFirst((Nonterminal) nextSymbol);
                                for (Terminal terminal : first) {
                                    if (terminal.isEmptySymbol()) {
                                        containsEmpty = true;
                                        continue;
                                    }
                                    updated = addToFollowMap(nonterminal1, terminal) || updated;
                                }
                                if (!containsEmpty) continue;
                            }
                        }
                        if (nonterminal.equals(nonterminal1)) continue;
                        HashSet<Terminal> follow = followHashMap.get(nonterminal);
                        if (follow != null) {
                            for (Terminal terminal : follow) {
                                updated = addToFollowMap(nonterminal1, terminal) || updated;
                            }
                        }
                    }
                }
            }
        }
    }

    private void buildAnalyzeTable() {
        for (Map.Entry<Nonterminal, HashMap<SymbolString, HashSet<Terminal>>> entry : firstHashMap.entrySet()) {
            Nonterminal nonterminal = entry.getKey();
            for (Map.Entry<SymbolString, HashSet<Terminal>> entry1 : entry.getValue().entrySet()) {
                SymbolString symbolString = entry1.getKey();
                for (Terminal terminal : entry1.getValue()) {
                    if (!terminal.isEmptySymbol()) {
                        boolean addResult = addToAnalyzeTable(nonterminal, terminal, symbolString);
                        assert addResult;
                    } else {
                        for (Terminal terminal1 : followHashMap.get(nonterminal)) {
                            addToAnalyzeTable(nonterminal, terminal1, symbolString);
                        }
                    }
                }
            }
        }

    }

    private HashSet<Terminal> getFirst(Nonterminal nonterminal) {
        HashSet<Terminal> terminalHashSet = new HashSet<>();
        HashMap<SymbolString, HashSet<Terminal>> map = firstHashMap.get(nonterminal);
        if (map == null) return terminalHashSet;
        for (Map.Entry<SymbolString, HashSet<Terminal>> entry : map.entrySet()) {
            terminalHashSet.addAll(entry.getValue());
        }
        return terminalHashSet;
    }

    private boolean addToFirstMap(Nonterminal nonterminal, SymbolString symbolString, Terminal terminal) {
        HashMap<SymbolString, HashSet<Terminal>> hashMap;
        HashSet<Terminal> hashSet;
        if (firstHashMap.containsKey(nonterminal)) {
            hashMap = firstHashMap.get(nonterminal);
            if (hashMap.containsKey(symbolString)) {
                hashSet = hashMap.get(symbolString);
                return hashSet.add(terminal);
            }
            hashSet = new HashSet<>();
            hashSet.add(terminal);
            hashMap.put(symbolString, hashSet);
            firstHashMap.put(nonterminal, hashMap);
            return true;
        } else {
            hashMap = new HashMap<>();
            hashSet = new HashSet<>();
            hashSet.add(terminal);
            hashMap.put(symbolString, hashSet);
            firstHashMap.put(nonterminal, hashMap);
        }
        return true;
    }

    private boolean addToFollowMap(Nonterminal nonterminal, Terminal terminal) {
        HashSet<Terminal> hashSet;
        if (followHashMap.containsKey(nonterminal)) {
            hashSet = followHashMap.get(nonterminal);
            return hashSet.add(terminal);

        } else {
            hashSet = new HashSet<>();
            hashSet.add(terminal);
            followHashMap.put(nonterminal, hashSet);
        }
        return true;
    }

    private boolean addToAnalyzeTable(Nonterminal nonterminal, Terminal terminal, SymbolString symbolString) {
        HashMap<Terminal, SymbolString> map;

        if (analyzeTable.containsKey(nonterminal)) {
            map = analyzeTable.get(nonterminal);
            if (map.containsKey(terminal)) {
                return false;
            }
            map.put(terminal, symbolString);
            return true;
        }
        map = new HashMap<>();
        map.put(terminal, symbolString);
        analyzeTable.put(nonterminal, map);
        return true;
    }

    private void printFirstMap() {
        for (Map.Entry<Nonterminal, HashMap<SymbolString, HashSet<Terminal>>> entry : firstHashMap.entrySet()) {
            System.out.println(entry.getKey());
            for (Map.Entry<SymbolString, HashSet<Terminal>> entry1 : entry.getValue().entrySet()) {
                System.out.println("\t" + entry1.getKey());
                for (Terminal terminal : entry1.getValue()) {
                    System.out.println("\t\t" + terminal);
                }
            }
        }
    }

    private void printFollowMap() {
        for (Map.Entry<Nonterminal, HashSet<Terminal>> entry : followHashMap.entrySet()) {
            System.out.println(entry.getKey());
            for (Terminal terminal : entry.getValue()) {
                System.out.println("\t" + terminal);
            }
        }
    }

    private void printAnalyzeTable() {
        for (Map.Entry<Nonterminal, HashMap<Terminal, SymbolString>> entry : analyzeTable.entrySet()) {
            System.out.println(entry.getKey());
            for (Map.Entry<Terminal, SymbolString> entry1 : entry.getValue().entrySet()) {
                System.out.println("\t" + entry1.getKey() + ": " + entry1.getValue());
            }
        }
    }


    private SymbolString querySymbolString(Nonterminal nonterminal, Terminal terminal) {
        if (analyzeTable.containsKey(nonterminal)) {
            HashMap<Terminal, SymbolString> map = analyzeTable.get(nonterminal);
            if (map.containsKey(terminal)) {
                return map.get(terminal);
            }
        }
        return null;
    }

    private void initAnalyzeStack() {
        analyzeStack.push(Terminal.END);
        analyzeStack.push(getStartSymbol());
    }

    @Override
    public void analyze() throws IOException {
        try {
            readAllLexicalTokens();
            buildFirstMap();
            buildFollowSet();
            buildAnalyzeTable();

            System.out.println("-----First set-----");
            printFirstMap();
            System.out.println("-----Follow set-----");
            printFollowMap();
            System.out.println("-----Analyze table set-----");
            printAnalyzeTable();

            initAnalyzeStack();
            List<LexicalToken> lexicalTokens = getLexicalTokens();
            int lineNumber = 0;
            while (lineNumber < lexicalTokens.size()) {
                Symbol symbol = analyzeStack.peek();
                LexicalToken lexicalToken = lexicalTokens.get(lineNumber);
                if (!symbol.isNonterminal()) {
                    Terminal terminal = (Terminal) symbol;
                    if (terminal.isEndSymbol()) throw new SyntaxException(lexicalToken, lineNumber);
                    if (!terminal.isIdentifer() && !terminal.getSymbol().equals(lexicalToken.getTokenString())) {
                        throw new SyntaxException(lexicalToken, lineNumber);
                    }
                    analyzeStack.pop();
                    ++lineNumber;
                } else {
                    Nonterminal nonterminal = (Nonterminal) symbol;
                    SymbolString symbolString = querySymbolString(nonterminal, Terminal.of(lexicalToken));
                    if (symbolString == null) throw new SyntaxException(lexicalToken, lineNumber);
                    analyzeStack.pop();
                    ArrayList<Symbol> symbolArrayList = symbolString.getSymbols();
                    for (int j = symbolArrayList.size() - 1; j >= 0; --j) {
                        analyzeStack.push(symbolArrayList.get(j));
                    }
                }

            }
            // pop remaining empty symbol
            if (!analyzeStack.empty()) {
                while (!analyzeStack.empty()) {
                    Symbol symbol = analyzeStack.pop();
                    if (symbol.isNonterminal()) {
                        if (querySymbolString((Nonterminal) symbol, Terminal.END) == null)
                            throw new SyntaxException(lexicalTokens.get(lineNumber - 1), lineNumber - 1);
                    } else {
                        if (!((Terminal) symbol).isEndSymbol())
                            throw new SyntaxException(lexicalTokens.get(lineNumber - 1), lineNumber - 1);
                    }
                }
            }
            writeResult("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }

}
