import java.util.HashMap;
import java.util.Map;

public class BracketConsts {
    private static final Map<Character, Integer> brackets = new HashMap<Character, Integer>() {{
        put('(', GrammarConsts.LPARENTHESIS);
        put(')', GrammarConsts.RPARENTHESIS);
        put('[', GrammarConsts.LBRACKET);
        put(']', GrammarConsts.RBRACKET);
        put('{', GrammarConsts.LBRACE);
        put('}', GrammarConsts.RBRACE);
    }};

    public static boolean isBracket(char k) {
        return brackets.containsKey(k);
    }

    public static int getBracketId(char k) {
        return brackets.get(k);
    }
}


