import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class SemanticActionMatcher {
    public static SemanticAction matches(Nonterminal nonterminal, SymbolString symbolString) {
        if (symbolString.length() == 1) {
            if (symbolString.getSymbols().get(0).isNonterminal()) {
                return renameAction;
            } else {
                return identifierAction;
            }
        }
        if (symbolString.length() == 3) {
            ArrayList<Symbol> symbols = symbolString.getSymbols();
            if (symbols.get(0).isNonterminal() &&
                    symbols.get(2).isNonterminal() &&
                    !symbols.get(1).isNonterminal()) {
                switch (((Terminal) symbols.get(1)).getTokenId()) {
                    case GrammarConsts.ASSIGN:
                        return assignAction;
                    case GrammarConsts.PLUS:
                    case GrammarConsts.SUBTRACT:
                    case GrammarConsts.MULTIPLE:
                    case GrammarConsts.DIVIDE:
                        return biOpAction;
                    default:
                        break;
                }
            } else if (!symbols.get(0).isNonterminal()
                    && !symbols.get(2).isNonterminal()
                    && symbols.get(1).isNonterminal()) {
                if (((Terminal) symbols.get(0)).getTokenId() == GrammarConsts.LPARENTHESIS &&
                        ((Terminal) symbols.get(2)).getTokenId() == GrammarConsts.RPARENTHESIS) {
                    return renameAction;
                }
            }
        }
        return emptyAction;
    }

    private static SemanticAction emptyAction = new SemanticAction() {
        @Override
        public SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap) {
            return null;
        }
    };

    private static SemanticAction assignAction = new SemanticAction() {
        @Override
        public SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap) {
            return new SemanticToken(GrammarConsts.ASSIGN,
                    symbolMap.get(handler.get(2)).getPlace(),
                    null,
                    symbolMap.get(handler.get(0)).getPlace());
        }
    };
    private static SemanticAction biOpAction = new SemanticAction() {
        @Override
        public SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap) {
            Nonterminal nonterminal = reduce;
            if (reduce.equals(handler.get(0)) || reduce.equals(handler.get(2))) {
                nonterminal = newTemp(symbolMap);
                symbolStack.pop();
                symbolStack.push(nonterminal);
            }
            return new SemanticToken(GrammarConsts.getGrammarConst(handler.get(1).getSymbol()),
                    symbolMap.get(handler.get(0)).getPlace(),
                    symbolMap.get(handler.get(2)).getPlace(),
                    symbolMap.get(nonterminal).getPlace());
        }
    };

    private static SemanticAction renameAction = new SemanticAction() {
        @Override
        public SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap) {
            if (handler.size() == 1) {
                symbolMap.put(reduce, symbolMap.get(handler.get(0)));
            } else {
                assert handler.size() == 3;
                symbolMap.put(reduce, symbolMap.get(handler.get(1)));
            }
            return null;
        }
    };

    private static SemanticAction identifierAction = new SemanticAction() {
        @Override
        public SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap) {
            assert handler.get(0).isIdentifier();
            int place = entry(handler.get(0), symbolMap);
            symbolMap.put(reduce, new SymbolInfo(place));
            return null;
        }
    };

    private static Nonterminal newTemp(HashMap<Symbol, SymbolInfo> symbolMap) {
        SymbolInfo info = new SymbolInfo(symbolMap.size());
        Nonterminal nont = new Nonterminal("__TMP" + info.getPlace() + "__");
        symbolMap.put(nont, info);
        return nont;
    }

    private static int entry(Symbol symbol, HashMap<Symbol, SymbolInfo> symbolMap) {
        return symbolMap.computeIfAbsent(new Variable((Terminal) symbol), k -> new SymbolInfo(symbolMap.size())).getPlace();
    }
}
