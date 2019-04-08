import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class LexicalAnalyzer {
    private static final int MAX_IDENTIFIER = 32;

    public static void analyze(BufferedReader br, BufferedWriter bw) throws IOException {
        while (true) {
            int ch = br.read();
            if (ch == -1) break;
            char c = (char) ch;
            StringBuilder token = new StringBuilder(MAX_IDENTIFIER);
            if (Character.isLetter(c)) {
                token.append(c);
                br.mark(1);
                c = (char) br.read();
                while (Character.isLetterOrDigit(c)) {
                    token.append(c);
                    br.mark(1);
                    c = (char) br.read();
                }
                br.reset();
                String tokenString = token.toString();
                if (KeywordConsts.isKeyword(tokenString)) {
                    writeToken(bw, KeywordConsts.getKeywordId(tokenString), tokenString);
                } else {
                    writeToken(bw, GrammarConsts.ID, tokenString);
                }
            } else if (Character.isDigit(c)) {
                token.append(c);
                br.mark(1);
                c = (char) br.read();
                boolean decimal = false;
                while (Character.isDigit(c) || (c == '.' && !decimal)) {
                    if (c == '.') decimal = true;
                    token.append(c);
                    br.mark(1);
                    c = (char) br.read();
                }
                br.reset();
                String tokenString = token.toString();
                writeToken(bw, decimal ? GrammarConsts.DECIMAL : GrammarConsts.INT, tokenString);
            } else if (SeparatorConsts.isSeparator(c)) {
                writeToken(bw, SeparatorConsts.getSeparatorId(c), String.valueOf(c));
            } else if (OperatorConsts.isOperator(String.valueOf(c))) {
                token.append(c);
                br.mark(c);
                c = (char) br.read();
                token.append(c);
                if (!OperatorConsts.isOperator(token.toString())) {
                    br.reset();
                    token.setLength(token.length() - 1);
                }
                String tokenString = token.toString();
                writeToken(bw, OperatorConsts.getOperatorId(tokenString), tokenString);
            } else if (BracketConsts.isBracket(c)) {
                writeToken(bw, BracketConsts.getBracketId(c), String.valueOf(c));
            } else if (Character.isWhitespace(c)) {
                // do nothing
            } else {
                System.out.println("Error: " + c);
                break;
            }
        }
        bw.flush();
    }

    private static void writeToken(BufferedWriter bw, int id, String token) throws IOException {
        bw.write(String.format("%d\t%s\r\n", id, token));
    }


}
