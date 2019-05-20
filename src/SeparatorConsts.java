import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class SeparatorConsts {
    private static final BiMap<Character, Integer> separators;

    static {
        separators = HashBiMap.create();
        separators.put(';', GrammarConsts.SEMICOLON);
        separators.put(',', GrammarConsts.COMMA);
        separators.put('"', GrammarConsts.DQUOTE);
        separators.put('\'', GrammarConsts.APOSTROPHE);
        separators.put('\\', GrammarConsts.BACKSLASH);
    }

    public static boolean isSeparator(char k) {
        return separators.containsKey(k);
    }

    public static Integer getSeparatorId(char k) {
        return separators.get(k);
    }
}
