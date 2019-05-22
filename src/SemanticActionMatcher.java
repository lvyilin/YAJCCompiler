import java.util.ArrayList;
import java.util.HashMap;

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

    private static SemanticAction emptyAction = (symbolStack, handler, reduce, symbolMap) -> {
        symbolStack.push(reduce);
        return null;
    };

    private static SemanticAction assignAction = (symbolStack, handler, reduce, symbolMap) -> {
        symbolStack.push(reduce);
        return new SemanticToken(GrammarConsts.ASSIGN,
                symbolMap.get(handler.get(2)),
                null,
                symbolMap.get(handler.get(0)));
    };
    private static SemanticAction biOpAction = (symbolStack, handler, reduce, symbolMap) -> {
        Variable variable;
        if (Symbol.equalSymbol(reduce, handler.get(0)) || Symbol.equalSymbol(reduce, handler.get(2))) {
            variable = newTemp(symbolMap, reduce);
        } else {
            variable = new Variable(reduce);
        }
//            Variable variable = new Variable(nonterminal, nonterminal.getSymbol());
        symbolStack.push(variable);

        return new SemanticToken(GrammarConsts.getGrammarConst(handler.get(1).getSymbol()),
                symbolMap.get(handler.get(0)),
                symbolMap.get(handler.get(2)),
                symbolMap.get(variable));
    };

    private static SemanticAction renameAction = (symbolStack, handler, reduce, symbolMap) -> {
        Variable variable;
        if (handler.size() == 1) {
            variable = new Variable(reduce, handler.get(0).getTokenString());
            symbolMap.put(variable, symbolMap.get(handler.get(0)));
//                symbolMap.put(reduce, symbolMap.get(handler.get(0)));
        } else {
            assert handler.size() == 3;
            variable = new Variable(reduce, handler.get(1).getTokenString());
            symbolMap.put(variable, symbolMap.get(handler.get(1)));
//                symbolMap.put(reduce, symbolMap.get(handler.get(1)));
        }
        symbolStack.push(variable);
        return null;
    };

    private static SemanticAction identifierAction = (symbolStack, handler, reduce, symbolMap) -> {
        assert handler.get(0).isIdentifier();
        SymbolInfo info = entry(handler.get(0), symbolMap);
        Variable variable = new Variable(reduce, info.toString());
//            variable.setTokenString(handler.get(0).getTokenString());
        symbolMap.put(variable, info);
        symbolStack.push(variable);
//            symbolMap.put(reduce, info);
//            symbolStack.push(reduce);
        return null;
    };

    private static Variable newTemp(HashMap<Symbol, SymbolInfo> symbolMap, Nonterminal reduce) {
        String name = "__TMP" + symbolMap.size() + "__";
        Variable variable = new Variable(reduce);
        variable.setTokenString(name);
        SymbolInfo info = new SymbolInfo(variable);
        symbolMap.put(variable, info);
        return variable;
    }

    private static SymbolInfo entry(Symbol symbol, HashMap<Symbol, SymbolInfo> symbolMap) {
        Variable var = new Variable(symbol);
        return symbolMap.computeIfAbsent(var, k -> new SymbolInfo(var));
    }
}
