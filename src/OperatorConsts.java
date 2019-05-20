import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;


public class OperatorConsts {
    private static final BiMap<String, Integer> operators;

    static {
        operators = HashBiMap.create();
        operators.put(".", GrammarConsts.DOT);
        operators.put("+", GrammarConsts.PLUS);
        operators.put("-", GrammarConsts.SUBTRACT);
        operators.put("*", GrammarConsts.MULTIPLE);
        operators.put("/", GrammarConsts.DIVIDE);
        operators.put("%", GrammarConsts.MOD);
        operators.put("=", GrammarConsts.ASSIGN);
        operators.put("<", GrammarConsts.LT);
        operators.put("<=", GrammarConsts.LE);
        operators.put(">", GrammarConsts.GT);
        operators.put(">=", GrammarConsts.GE);
        operators.put("==", GrammarConsts.EQ);
        operators.put("&", GrammarConsts.AND);
        operators.put("&&", GrammarConsts.LAND);
        operators.put("|", GrammarConsts.OR);
        operators.put("||", GrammarConsts.LOR);
        operators.put("!", GrammarConsts.NOT);
        operators.put("!=", GrammarConsts.NEQ);
        operators.put("++", GrammarConsts.INCREASE);
        operators.put("--", GrammarConsts.DECREASE);
        operators.put("<<", GrammarConsts.LSHIFT);
        operators.put(">>", GrammarConsts.RSHIFT);
        operators.put("+=", GrammarConsts.PLUSA);
        operators.put("-=", GrammarConsts.SUBTRACTA);
        operators.put("*=", GrammarConsts.MULTIPLEA);
        operators.put("/=", GrammarConsts.DIVIDEA);
        operators.put("%=", GrammarConsts.MODA);
    }

    public static boolean isOperator(String k) {
        return operators.containsKey(k);
    }

    public static Integer getOperatorId(String k) {
        return operators.get(k);
    }
}
