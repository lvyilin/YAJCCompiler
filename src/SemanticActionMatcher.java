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
        if (symbolString.length() == 2) {
            if (symbolString.getSymbols().get(0).getSymbol().equals("else")) {
                return elseAction;
            }
        }
        if (symbolString.length() == 3) {
            ArrayList<Symbol> symbols = symbolString.getSymbols();
            boolean symbol0 = symbols.get(0).isNonterminal();
            boolean symbol1 = symbols.get(1).isNonterminal();
            boolean symbol2 = symbols.get(2).isNonterminal();
            if (symbol0 && symbol2 && !symbol1) {
                Integer id = ((Terminal) symbols.get(1)).getTokenId();
                if (id.equals(GrammarConsts.ASSIGN)) return assignAction;
                if (OperatorConsts.isBiOperator(id)) return biOpAction;
                if (OperatorConsts.isCompareOperator(id)) return cmpOpAction;

            } else if (!symbol0 && !symbol2 && symbol1) {
                if (BracketConsts.isBracket(((Terminal) symbols.get(0)).getTokenId()) &&
                        BracketConsts.isBracket(((Terminal) symbols.get(2)).getTokenId())) {
                    return renameAction;
                }
            } else if (!symbol0 && !symbol2) {
                Integer id = ((Terminal) symbols.get(1)).getTokenId();
                if (OperatorConsts.isCompareOperator(id)) return cmpOpAction;
            }
        }
        if (symbolString.length() == 6) {
            if (symbolString.getSymbols().get(0).getSymbol().equals("if")) {
                return ifAction;
            }
        }
        return emptyAction;
    }

    private static SemanticAction emptyAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        symbolStack.push(reduce);
    };

    private static SemanticAction assignAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        symbolStack.push(reduce);
        tokens.add(new SemanticToken(GrammarConsts.ASSIGN,
                symbolMap.get(handler.get(2)),
                null,
                symbolMap.get(handler.get(0))));
    };

    private static SemanticAction biOpAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        Variable variable;
        if (Symbol.equalSymbol(reduce, handler.get(0)) || Symbol.equalSymbol(reduce, handler.get(2))) {
            variable = newTemp(symbolMap, reduce);
        } else {
            variable = new Variable(reduce);
        }
//            Variable variable = new Variable(nonterminal, nonterminal.getSymbol());
        symbolStack.push(variable);

        tokens.add(new SemanticToken(GrammarConsts.getGrammarConst(handler.get(1).getSymbol()),
                symbolMap.get(handler.get(0)),
                symbolMap.get(handler.get(2)),
                symbolMap.get(variable)));
    };

    private static SemanticAction renameAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        Variable variable = null;
        int idx = handler.size() == 1 ? 0 : handler.size() == 3 ? 1 : -1;
        if (idx != -1) {
            Symbol sym = handler.get(idx);
            if (sym.getTokenString() != null) {
                SymbolInfo symInfo = symbolMap.get(sym);
                symInfo.setJump(tokens.size() - 1);
                variable = new Variable(reduce, sym.getTokenString());
                symbolMap.put(variable, symInfo);
            }
        }
        if (variable != null) {
            symbolStack.push(variable);
        } else {
            variable = newTemp(symbolMap, reduce);
            SymbolInfo symInfo = symbolMap.get(variable);
            symInfo.setJump(tokens.size() - 1);
            symbolStack.push(variable);
        }
    };

    private static SemanticAction identifierAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        assert handler.get(0).isIdentifier();
        SymbolInfo info = entry(handler.get(0), symbolMap);
        Variable variable = new Variable(reduce, info.toString());
//            variable.setTokenString(handler.get(0).getTokenString());
        symbolMap.put(variable, info);
        symbolStack.push(variable);
//            symbolMap.put(reduce, info);
//            symbolStack.push(reduce);
    };

    private static SemanticAction cmpOpAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        Variable conditionVar = newTemp(symbolMap, reduce);
        symbolStack.push(conditionVar);

        SymbolInfo info = symbolMap.get(conditionVar);
        info.setTC(tokens.size());

        Symbol left = handler.get(0), right = handler.get(2);
        SymbolInfo leftInfo, rightInfo;
        if (!left.isNonterminal()) {
            leftInfo = entry(left, symbolMap);
        } else {
            leftInfo = symbolMap.get(left);
        }
        if (!right.isNonterminal()) {
            rightInfo = entry(right, symbolMap);
        } else {
            rightInfo = symbolMap.get(right);
        }
        tokens.add(new SemanticToken(GrammarConsts.getGrammarConst(handler.get(1).getSymbol()), //TC
                leftInfo,
                rightInfo,
                null));

        info.setFC(tokens.size());
        tokens.add(new SemanticToken(GrammarConsts.GOTO, //FC
                null, null,
                null));

    };

    private static SemanticAction ifAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        Variable conditionVar = (Variable) handler.get(2);
        Variable elseVar = (Variable) handler.get(5);
        Integer q = tokens.size();
        SymbolInfo elseInfo = symbolMap.get(elseVar);
        Integer p = elseInfo.getFC();

        tokens.get(elseInfo.getFC()).setJumpResult(q);
        SymbolInfo conditionInfo = symbolMap.get(conditionVar);
        tokens.get(conditionInfo.getFC()).setJumpResult(p + 1);

        tokens.get(conditionInfo.getTC()).setJumpResult(conditionInfo.getFC() + 1);
    };

    private static SemanticAction elseAction = (symbolStack, handler, reduce, symbolMap, tokens) -> {
        assert handler.size() == 2;
        Variable elseVar = newTemp(symbolMap, reduce);
        symbolStack.push(elseVar);

        SymbolInfo info = symbolMap.get(elseVar);
        int insertPos = symbolMap.get(handler.get(1)).getJump();
        info.setFC(insertPos);
        tokens.add(insertPos, new SemanticToken(GrammarConsts.GOTO, //FC
                null, null,
                null));
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
