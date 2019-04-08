import java.util.HashMap;
import java.util.Map;

public class SeparatorConsts {
    private static final Map<Character, Integer> separators = new HashMap<Character, Integer>() {{
        put(';', GrammarConsts.SEMICOLON);
        put(',', GrammarConsts.COMMA);
        put('"', GrammarConsts.DQUOTE);
        put('\'', GrammarConsts.APOSTROPHE);
        put('\\', GrammarConsts.BACKSLASH);
    }};

    public static boolean isSeparator(char k) {
        return separators.containsKey(k);
    }

    public static int getSeparatorId(char k) {
        return separators.get(k);
    }
}
