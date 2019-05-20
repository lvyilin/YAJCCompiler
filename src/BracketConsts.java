import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class BracketConsts {
    private static final BiMap<Character, Integer> brackets;

    static {
        brackets = HashBiMap.create();
        brackets.put('(', GrammarConsts.LPARENTHESIS);
        brackets.put(')', GrammarConsts.RPARENTHESIS);
        brackets.put('[', GrammarConsts.LBRACKET);
        brackets.put(']', GrammarConsts.RBRACKET);
        brackets.put('{', GrammarConsts.LBRACE);
        brackets.put('}', GrammarConsts.RBRACE);

    }

    public static boolean isBracket(char k) {
        return brackets.containsKey(k);
    }

    public static Integer getBracketId(char k) {
        return brackets.get(k);
    }
}



