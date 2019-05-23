import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public interface SemanticAction {
    void execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap, List<SemanticToken> tokens);
}
