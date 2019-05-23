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
        operators.put("jmp", GrammarConsts.GOTO);
    }

    public static boolean isOperator(String k) {
        return operators.containsKey(k);
    }

    public static Integer getOperatorId(String k) {
        return operators.get(k);
    }

    public static String getOperatorString(Integer i) {
        return operators.inverse().get(i);
    }

    public static boolean isCompareOperator(Integer i) {
        switch (i) {
            case GrammarConsts.LT:
            case GrammarConsts.LE:
            case GrammarConsts.GT:
            case GrammarConsts.GE:
            case GrammarConsts.EQ:
                return true;
            default:
                return false;
        }
    }

    public static boolean isBiOperator(Integer i) {
        switch (i) {
            case GrammarConsts.PLUS:
            case GrammarConsts.SUBTRACT:
            case GrammarConsts.MULTIPLE:
            case GrammarConsts.DIVIDE:
            case GrammarConsts.MOD:
            case GrammarConsts.LSHIFT:
            case GrammarConsts.RSHIFT:
                return true;
            default:
                return false;
        }
    }
}
