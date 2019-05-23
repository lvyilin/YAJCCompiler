import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class KeywordConsts {
    private static final BiMap<String, Integer> keywords;

    static {
        keywords = HashBiMap.create();
        keywords.put("void", GrammarConsts.VOID);
        keywords.put("int", GrammarConsts.INTEGER);
        keywords.put("float", GrammarConsts.FLOAT);
        keywords.put("double", GrammarConsts.DOUBLE);
        keywords.put("char", GrammarConsts.CHAR);
        keywords.put("struct", GrammarConsts.STRUCT);
        keywords.put("if", GrammarConsts.IF);
        keywords.put("else", GrammarConsts.ELSE);
        keywords.put("for", GrammarConsts.FOR);
        keywords.put("do", GrammarConsts.DO);
        keywords.put("while", GrammarConsts.WHILE);
        keywords.put("return", GrammarConsts.RETURN);
        keywords.put("goto", GrammarConsts.GOTO);

    }

    public static boolean isKeyword(String k) {
        return keywords.containsKey(k);
    }

    public static Integer getKeywordId(String k) {
        return keywords.get(k);
    }

    public static String getKeywordString(int val) {
        return keywords.inverse().get(val);
    }
}
