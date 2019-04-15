import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SyntaxAnalyzer {
    private BufferedReader br;
    private BufferedWriter bw;
    private int lineNumber = 0;
    private List<LexicalToken> tokens = new ArrayList<>();

    public SyntaxAnalyzer(BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        this.br = bufferedReader;
        this.bw = bufferedWriter;
    }

    private void A() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = lineNumber;
        try {
            token = nextToken();
            if (token == null) throw new SyntaxException(null, lineNumber);
            if (!token.matches("+") && !token.matches("-")) {
                revertToken();
                V();
                token = nextToken();
                if (token == null || !token.matches("=")) throw new SyntaxException(token, lineNumber);
                E();
            }

        } catch (SyntaxException e) {
//            revertToken(oldLineNumber);
            throw e;
        }

    }

    private void V() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.isIdentifier()) {
            revertToken();
            throw new SyntaxException(token, lineNumber);
        }
    }

    private void F() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = lineNumber;
        try {
            token = nextToken();
            if (token == null || !token.matches("(")) throw new SyntaxException(token, lineNumber);
            E();
            token = nextToken();
            if (token == null || !token.matches(")")) throw new SyntaxException(token, lineNumber);
        } catch (SyntaxException | NullPointerException e) {
            revertToken(oldLineNumber);
            token = nextToken();
            if (token == null || !token.isIdentifier()) {
                revertToken();
                throw e;
            }
        }

    }

    private void E() throws SyntaxException {
        T();
        E_();
    }

    private void T() throws SyntaxException {
        F();
        T_();
    }

    private void T_() throws SyntaxException {
        int oldLineNumber = lineNumber;
        try {
            M();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            return;
        }
        F();
        T_();
    }

    private void E_() throws SyntaxException {
        int oldLineNumber = lineNumber;
        try {
            A();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            return;
        }
        T();
        E_();
    }

    private void M() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.matches("*") && !token.matches("/")) {
            revertToken();
            throw new SyntaxException(token, lineNumber);
        }
    }


    private void Entrance() throws SyntaxException {
        if (!hasNextToken()) {
            return;
        }
        Stmt();
        Entrance();
    }

    private void Stmt() throws SyntaxException {
        int oldLineNumber = lineNumber;
        try {
            If();
        } catch (SyntaxException ignored) {
            revertToken(oldLineNumber);
            A();
            LexicalToken token;
            token = nextToken();
            if (token == null || !token.matches(";")) throw new SyntaxException(token, lineNumber);

        }
    }

    private void If() throws SyntaxException {
        LexicalToken token;
        token = nextToken();
        if (token == null || !token.matches("if")) throw new SyntaxException(token, lineNumber);
        token = nextToken();
        if (token == null || !token.matches("(")) throw new SyntaxException(token, lineNumber);
        Condition();
        token = nextToken();
        if (token == null || !token.matches(")")) throw new SyntaxException(token, lineNumber);
        Clause();
    }


    private void Condition() throws SyntaxException {
        LexicalToken token = nextToken();
        if (token == null || !token.matches("1") && !token.matches("0")) {
            revertToken();
            throw new SyntaxException(token, lineNumber);
        }
    }


    private void Clause() throws SyntaxException {
        LexicalToken token;
        int oldLineNumber = lineNumber;
        try {
            token = nextToken();
            if (token == null || !token.matches("{")) throw new SyntaxException(token, lineNumber);
            Entrance();
            token = nextToken();
            if (token == null || !token.matches("}")) throw new SyntaxException(token, lineNumber);
        } catch (SyntaxException e) {
            revertToken(oldLineNumber);
            oldLineNumber = lineNumber;
            try {
                Stmt();
            } catch (SyntaxException se) {
                revertToken(oldLineNumber);
                throw se;
            }
        }
    }

    public void analyze() throws IOException {
        try {
            while (true) {
                String line = br.readLine();
                if (line == null) break;
                tokens.add(LexicalToken.parseToken(line));
            }
            Entrance();
            LexicalToken token = nextToken();
            if (token != null) throw new SyntaxException(token, lineNumber);
            bw.write("Yes" + System.lineSeparator());
        } catch (SyntaxException se) {
            se.printStackTrace();
            bw.write("No" + System.lineSeparator());
        }
        bw.flush();
    }

    private LexicalToken nextToken() {
        if (!hasNextToken()) return null;
        return tokens.get(lineNumber++);
    }

    private boolean hasNextToken() {
        return lineNumber >= 0 & lineNumber < tokens.size();
    }

    private void revertToken() {
        --lineNumber;
    }

    private void revertToken(int newLineNumber) {
        lineNumber = newLineNumber;
    }
}
