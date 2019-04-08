import java.util.HashMap;
import java.util.Map;

public class OperatorConsts {
    private static final Map<String, Integer> operators = new HashMap<String, Integer>() {{
        put(".", GrammarConsts.DOT);
        put("+", GrammarConsts.PLUS);
        put("-", GrammarConsts.SUBTRACT);
        put("*", GrammarConsts.MULTIPLE);
        put("/", GrammarConsts.DIVIDE);
        put("%", GrammarConsts.MOD);
        put("=", GrammarConsts.ASSIGN);
        put("<", GrammarConsts.LT);
        put("<=", GrammarConsts.LE);
        put(">", GrammarConsts.GT);
        put(">=", GrammarConsts.GE);
        put("==", GrammarConsts.EQ);
        put("&", GrammarConsts.AND);
        put("&&", GrammarConsts.LAND);
        put("|", GrammarConsts.OR);
        put("||", GrammarConsts.LOR);
        put("!", GrammarConsts.NOT);
        put("!=", GrammarConsts.NEQ);

        put("++", GrammarConsts.INCREASE);
        put("--", GrammarConsts.DECREASE);
        put("<<", GrammarConsts.LSHIFT);
        put(">>", GrammarConsts.RSHIFT);
        put("+=", GrammarConsts.PLUSA);
        put("-=", GrammarConsts.SUBTRACTA);
        put("*=", GrammarConsts.MULTIPLEA);
        put("/=", GrammarConsts.DIVIDEA);
        put("%=", GrammarConsts.MODA);


    }};

    public static boolean isOperator(String k) {
        return operators.containsKey(k);
    }

    public static int getOperatorId(String k) {
        return operators.get(k);
    }
}
