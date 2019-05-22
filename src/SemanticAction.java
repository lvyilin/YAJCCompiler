import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public interface SemanticAction {
    SemanticToken execute(Stack<Symbol> symbolStack, LinkedList<Symbol> handler, Nonterminal reduce, HashMap<Symbol, SymbolInfo> symbolMap);
}
