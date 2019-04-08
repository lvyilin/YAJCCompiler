import java.util.HashMap;
import java.util.Map;

public class KeywordConsts {
    private static final Map<String, Integer> keywords = new HashMap<String, Integer>() {{
        put("void", GrammarConsts.VOID);
        put("int", GrammarConsts.INTEGER);
        put("float", GrammarConsts.FLOAT);
        put("double", GrammarConsts.DOUBLE);
        put("char", GrammarConsts.CHAR);
        put("struct", GrammarConsts.STRUCT);

        put("if", GrammarConsts.IF);
        put("else", GrammarConsts.ELSE);
        put("for", GrammarConsts.FOR);
        put("do", GrammarConsts.DO);
        put("while", GrammarConsts.WHILE);
        put("return", GrammarConsts.RETURN);
    }};

    public static boolean isKeyword(String k) {
        return keywords.containsKey(k);
    }

    public static int getKeywordId(String k) {
        return keywords.get(k);
    }
}
