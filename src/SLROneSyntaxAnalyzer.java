import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.*;

public class SLROneSyntaxAnalyzer extends SyntaxAnalyzer {
    private class SLROneProject {
        Nonterminal nonterminal;
        SymbolString symbolString;
        SemanticAction semanticAction;

        SLROneProject(Nonterminal nonterminal, SymbolString symbolString) {
            this.nonterminal = nonterminal;
            this.symbolString = symbolString;
            this.semanticAction = SemanticActionMatcher.matches(nonterminal, symbolString);
        }
    }

    private class SLROneProjectIndex {
        int projectNumber;
        int position;

        SLROneProjectIndex(int projectNumber, int position) {
            this.projectNumber = projectNumber;
            this.position = position;
        }

        SLROneProjectIndex getProjectNextIndex() {
            return new SLROneProjectIndex(projectNumber, position + 1);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SLROneProjectIndex that = (SLROneProjectIndex) o;
            return projectNumber == that.projectNumber &&
                    position == that.position;
        }

        @Override
        public int hashCode() {
            return Objects.hash(projectNumber, position);
        }

    }

    private class SLROneProjects {
        private ArrayList<SLROneProject> projects = new ArrayList<>();

        SLROneProjects(HashMap<Nonterminal, ProductionRule> productionRules, Nonterminal startSymbol) throws IllegalSyntaxException {
            boolean needAugmented = false;
            for (Map.Entry<Nonterminal, ProductionRule> entry : productionRules.entrySet()) {
                Nonterminal nonterminal = entry.getKey();
                ProductionRule productionRule = entry.getValue();
                for (SymbolString symbolString : productionRule.getSymbolStrings()) {
                    SLROneProject project = new SLROneProject(nonterminal, symbolString);
                    if (symbolString.length() == 0) {
                        throw new IllegalSyntaxException("Empty symbol string is not allowed");
                    }
                    if (nonterminal.equals(startSymbol)) {
                        if (needAugmented) throw new IllegalSyntaxException("This grammar needs to be augmented");
                        needAugmented = true;
                        startProjectIndex = new SLROneProjectIndex(projects.size(), 0);
                    }
                    projects.add(project);
                }
            }
        }

        SLROneProject getProject(SLROneProjectIndex index) {
            if (!isValidProjectIndex(index)) return null;
            return projects.get(index.projectNumber);
        }

        SLROneProject getProject(Integer index) {
            if (index < 0 || index >= projects.size()) return null;
            return projects.get(index);
        }

        boolean isValidProjectIndex(SLROneProjectIndex index) {
            if (index.projectNumber < 0 || index.projectNumber >= projects.size() || index.position < 0) return false;
            return index.position <= projects.get(index.projectNumber).symbolString.length();
        }
    }

    private class SLROneProjectSet {
        ArrayList<SLROneProjectIndex> indices;
        Integer number;
        ArrayList<Integer> goFrom; // SLR(1)
        Symbol goCondition;

        public SLROneProjectSet(ArrayList<SLROneProjectIndex> indices, Integer number, ArrayList<Integer> goFrom, Symbol goCondition) {
            this.number = number;
            this.indices = indices;
            this.goFrom = goFrom;
            this.goCondition = goCondition;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SLROneProjectSet that = (SLROneProjectSet) o;
            return Objects.equals(indices, that.indices);
        }

        @Override
        public int hashCode() {
            return Objects.hash(indices);
        }
    }

    public SLROneSyntaxAnalyzer(BufferedReader bufferedReader,
                                BufferedWriter bufferedWriter,
                                Nonterminal startSymbol,
                                HashMap<Nonterminal, ProductionRule> productionRules,
                                HashSet<Terminal> terminalHashSet,
                                HashSet<Nonterminal> nonterminalHashSet) throws IllegalSyntaxException {
        super(bufferedReader, bufferedWriter, startSymbol, productionRules);
        this.terminalHashSet = terminalHashSet;
        this.nonterminalHashSet = nonterminalHashSet;
        this.slrOneProjects = new SLROneProjects(productionRules, startSymbol);

    }


    private void getProjectClosure(ArrayList<SLROneProjectIndex> closure) {
        HashSet<SLROneProjectIndex> indicesSet = new HashSet<>(closure);
        int counter = 0;
        while (counter < closure.size()) {
            SLROneProjectIndex idx = closure.get(counter);
            SLROneProject project = slrOneProjects.getProject(idx);
            if (idx.position >= project.symbolString.length()) {
                ++counter;
                continue;
            }
            Symbol symbol = project.symbolString.getSymbols().get(idx.position);
            if (!symbol.isNonterminal()) {
                ++counter;
                continue;
            }
            Nonterminal nonterm = (Nonterminal) symbol;
            for (int i = 0; i < slrOneProjects.projects.size(); i++) {
                SLROneProject proj = slrOneProjects.projects.get(i);
                if (proj.nonterminal.equals(nonterm)) {
                    SLROneProjectIndex newIndex = new SLROneProjectIndex(i, 0);
                    boolean updated = indicesSet.add(newIndex);
                    if (updated) closure.add(newIndex);
                }
            }
            ++counter;
        }
    }


    private void buildProjectSets() throws IllegalSyntaxException {
        if (startProjectIndex == null) throw new IllegalSyntaxException();
        projectSetsList.clear();
        HashMap<SLROneProjectSet, Integer> projectSetsHashMap = new HashMap<>();
        // add start project
        ArrayList<SLROneProjectIndex> startClosure = new ArrayList<>();
        startClosure.add(startProjectIndex);
        getProjectClosure(startClosure);
        SLROneProjectSet projectSet = new SLROneProjectSet(startClosure, 0, null, null);
        projectSetsList.add(projectSet);
        projectSetsHashMap.put(projectSet, 0);
        int i = 0;
        while (i < projectSetsList.size()) {
            projectSet = projectSetsList.get(i);
            HashMap<Symbol, HashSet<SLROneProjectIndex>> sameGoSymbolMap = new HashMap<>();
            for (SLROneProjectIndex index : projectSet.indices) {
                SLROneProjectIndex nextIndex = index.getProjectNextIndex();
                if (slrOneProjects.isValidProjectIndex(nextIndex)) {
                    SLROneProject project = slrOneProjects.getProject(nextIndex);
                    Symbol goSymbol = project.symbolString.getSymbols().get(index.position);

                    HashSet<SLROneProjectIndex> tmpSet;
                    if (sameGoSymbolMap.containsKey(goSymbol)) {
                        tmpSet = sameGoSymbolMap.get(goSymbol);
                    } else {
                        tmpSet = new HashSet<>();
                        sameGoSymbolMap.put(goSymbol, tmpSet);
                    }
                    tmpSet.add(nextIndex);
                }
            }
            for (Map.Entry<Symbol, HashSet<SLROneProjectIndex>> entry : sameGoSymbolMap.entrySet()) {
                Symbol goSymbol = entry.getKey();
                ArrayList<SLROneProjectIndex> closure = new ArrayList<>(entry.getValue());
                getProjectClosure(closure);
                ArrayList<Integer> goFromList = new ArrayList<>();
                goFromList.add(projectSet.number);
                SLROneProjectSet newProjectSet = new SLROneProjectSet(closure,
                        projectSetsList.size(),
                        goFromList,
                        goSymbol);
                if (!projectSetsHashMap.containsKey(newProjectSet)) {
                    projectSetsHashMap.put(newProjectSet, projectSetsList.size());
                    projectSetsList.add(newProjectSet);
                } else {
                    Integer projIdx = projectSetsHashMap.get(newProjectSet);
                    projectSetsList.get(projIdx).goFrom.add(projectSet.number);
                }

            }
            ++i;
        }
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

    private void printFollowMap() {
        for (Map.Entry<Nonterminal, HashSet<Terminal>> entry : followHashMap.entrySet()) {
            System.err.println(entry.getKey());
            for (Terminal terminal : entry.getValue()) {
                System.err.println("\t" + terminal);
            }
        }
    }

    private void addToAnalyzeTable(Integer state, Symbol symbol, Integer action) throws IllegalSyntaxException {
        HashMap<Symbol, Integer> map;

        if (analyzeTable.containsKey(state)) {
            map = analyzeTable.get(state);
            if (map.containsKey(symbol)) {
                if (map.get(symbol).equals(action)) return;

                printAnalyzeTable();
                throw new IllegalSyntaxException("Not a SLR(1) grammar");
            }
            map.put(symbol, action);
            return;
        }
        map = new HashMap<>();
        map.put(symbol, action);
        analyzeTable.put(state, map);
    }

    private void printAnalyzeTable() {
        for (Map.Entry<Integer, HashMap<Symbol, Integer>> entry : analyzeTable.entrySet()) {
            System.err.println(entry.getKey());
            for (Map.Entry<Symbol, Integer> entry1 : entry.getValue().entrySet()) {
                System.err.println("\t" + entry1.getKey() + ": " + entry1.getValue());
            }
        }
    }

    private void buildAnalyzeTable() throws IllegalSyntaxException {
        // go project
        for (SLROneProjectSet projectSetJ : projectSetsList) {
            if (projectSetJ.goFrom == null) continue;
            for (Integer goFrom : projectSetJ.goFrom) {
                SLROneProjectSet projectSetI = projectSetsList.get(goFrom);
                for (SLROneProjectIndex index : projectSetI.indices) {
                    SLROneProject project = slrOneProjects.getProject(index);
                    if (index.position < project.symbolString.length()) {
                        Symbol symbol = project.symbolString.getSymbols().get(index.position);
                        if (symbol.equals(projectSetJ.goCondition))
                            addToAnalyzeTable(projectSetI.number, symbol, projectSetJ.number);
                    }
                }
            }
        }
        // reduce project
        for (SLROneProjectSet projectSetI : projectSetsList) {
            for (SLROneProjectIndex index : projectSetI.indices) {
                SLROneProject project = slrOneProjects.getProject(index);
                if (index.position < project.symbolString.length()) continue;

                if (project.nonterminal.equals(getStartSymbol())) {
                    // acc
                    addToAnalyzeTable(projectSetI.number, Terminal.END, ACC);
                    continue;
                }
                HashSet<Terminal> followSet = followHashMap.get(project.nonterminal);
                for (Terminal terminal : followSet) {
                    addToAnalyzeTable(projectSetI.number, terminal, -(index.projectNumber + 1)); // r_i = -(num + 1)
                }
            }
        }
    }

    private void initStack() {
        analyzeStack.push(Terminal.END);
        stateStack.push(0);
    }

    private SLROneProjects slrOneProjects; // all projects
    private ArrayList<SLROneProjectSet> projectSetsList = new ArrayList<>(); // project set list
    private SLROneProjectIndex startProjectIndex;

    /*
    Storing the triplet (state, conditional symbol, action)
    For action (assume value is n):
        ACC: accept;
        Positive number: go to state #n
        Negative number: reduce by production rule #(-n-1)
     */
    private HashMap<Integer, HashMap<Symbol, Integer>> analyzeTable = new HashMap<>();
    private HashSet<Terminal> terminalHashSet;
    private HashSet<Nonterminal> nonterminalHashSet;
    private HashMap<Nonterminal, HashMap<SymbolString, HashSet<Terminal>>> firstHashMap = new HashMap<>();
    private HashMap<Nonterminal, HashSet<Terminal>> followHashMap = new HashMap<>();
    private static final Integer ACC = Integer.MAX_VALUE;
    private Stack<Integer> stateStack = new Stack<>();
    private Stack<Symbol> analyzeStack = new Stack<>();
    private HashMap<Symbol, SymbolInfo> symbolMap = new HashMap<>();
    private LinkedList<Symbol> handler = new LinkedList<>();

    @Override
    public void analyze() throws IOException {
        try {
            readAllLexicalTokens();
            buildProjectSets();
            buildFirstMap();
            buildFollowSet();
            buildAnalyzeTable();
            System.err.println("-----Follow set-----");
            printFollowMap();
            System.err.println("-----Analyze table set-----");
            printAnalyzeTable();

            initStack();
            List<LexicalToken> lexicalTokens = getLexicalTokens();
            int lineNumber = 0;
            try {
                while (lineNumber <= lexicalTokens.size()) {
//                while (!stateStack.peek().equals(ACC)) {
                    Integer state = stateStack.peek();
                    Terminal terminal;
                    if (lineNumber < lexicalTokens.size()) {
                        LexicalToken lexicalToken = lexicalTokens.get(lineNumber);

                        terminal = Terminal.of(lexicalToken);
                    } else {
                        terminal = Terminal.END;
                    }
                    Integer action = analyzeTable.get(state).get(terminal);
                    if (action >= 0) {
                        stateStack.push(action);
                        analyzeStack.push(terminal);
                        ++lineNumber;
                    } else {
                        action = -action - 1;
                        SLROneProject project = slrOneProjects.getProject(action);
                        int len = project.symbolString.length();
                        handler.clear();
                        while (len-- > 0) {
                            stateStack.pop();
                            handler.addFirst(analyzeStack.pop());
                        }
                        SemanticToken token = project.semanticAction.execute(analyzeStack, handler, project.nonterminal, symbolMap);
                        if (token != null) {
                            writeResult(token + System.lineSeparator());
                        }
                        Integer nextState = analyzeTable.get(stateStack.peek()).get(project.nonterminal);
                        stateStack.push(nextState);
                    }
                }
                if (stateStack.peek().equals(ACC)) {
                    assert analyzeStack.pop().equals(Terminal.END);
                    SLROneProject project = slrOneProjects.getProject(startProjectIndex.projectNumber);
                    int len = project.symbolString.length();
                    handler.clear();
                    while (len-- > 0) {
                        handler.addFirst(analyzeStack.pop());
                    }
                    SemanticToken token = project.semanticAction.execute(analyzeStack, handler, project.nonterminal, symbolMap);
                    if (token != null) {
                        writeResult(token + System.lineSeparator());
                    }
                } else {
                    throw new SyntaxException(lexicalTokens.get(lexicalTokens.size() - 1), lexicalTokens.size());
                }
            } catch (NullPointerException ne) {
                throw new SyntaxException(lexicalTokens.get(lineNumber), lineNumber + 1);
            }
//            writeResult("Yes" + System.lineSeparator());
        } catch (NullPointerException | SyntaxException | IllegalSyntaxException se) {
            se.printStackTrace();
            writeResult("No" + System.lineSeparator());
        }
    }
}
